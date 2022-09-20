import axios from 'axios'
import store from '@/store'
import storage from 'store'
import notification from 'ant-design-vue/es/notification'
import message from 'ant-design-vue/es/message'
import { VueAxios } from './axios'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import errorCode from '@/utils/errorCode'
import qs from 'qs'
import { blobValidate } from '@/utils/ruoyi'
import { saveAs } from 'file-saver'

// 是否显示重新登录
let isReloginShow

// 创建 axios 实例
const request = axios.create({
  // API 请求的默认前缀
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 6000 // 请求超时时间
})

// 异常拦截处理器
const errorHandler = (error) => {
  console.error(error)
  console.log(error.response)
  console.log(error.response.data !== undefined && error.response.data.data !== undefined)
// 请求出错但是有响应数据,则是后台处理过的错误,需要正常展示
  if (error.response.data !== undefined && error.response.data.data !== undefined) {
    const errorMessage = error.response.data.data.message
    notification.error({
      message: errorMessage,
      duration: 5 * 1000
    })
    console.error(error.response.data.data.stackTrace)
    error.message = errorMessage
  } else {
    let { message } = error
    if (message === 'Network Error') {
      message = '后端接口连接异常'
    } else if (message.includes('timeout')) {
      message = '系统接口请求超时'
    } else if (message.includes('Request failed with status code')) {
      message = '系统接口' + message.substr(message.length - 3) + '异常'
    }
    notification.error({
      message: message,
      duration: 5 * 1000
    })
  }
  return Promise.reject(error)
}

// 请求发起前的统一拦截器 request interceptor
request.interceptors.request.use(config => {
  const token = storage.get(ACCESS_TOKEN)
  // 如果 token 存在
  // 让每个请求携带自定义 token 请根据实际情况自行修改
  if (token) {
    config.headers['Authorization'] = 'Bearer ' + token // 让每个请求携带自定义token 请根据实际情况自行修改
    // config.headers['accessAccess-Token'] = token
  }
  // 处理params参数
  if (config.params) {
    const url = config.url + '?' + qs.stringify(config.params, { indices: false })
    config.params = {}
    config.url = url
  }
  return config
}, errorHandler)

// 对请求的响应的统一拦截器 response interceptor
request.interceptors.response.use((res) => {
  // 请求rul
  const requestUrl = res.config.url
  // 未设置状态码则默认成功状态
  const code = res.data.code || 200
  console.log(res)
  // 获取错误信息
  const msg = errorCode[code] || res.data.msg || errorCode['default']
  // 二进制数据则直接返回
  if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
    return res.data
  }
  if (code === 200) {
    if (res.data.success) {
      return res.data
    } else {
      notification.error({
        message: res.data.msg
      })
      res.message = res.data.msg
      return Promise.reject(res)
    }
  } else if (code === 401) {
    if (!isReloginShow) {
      isReloginShow = true
      notification.open({
        message: '系统提示',
        description: '登录状态已过期，请重新登录',
        btn: h => {
          return h(
            'a-button',
            {
              props: {
                type: 'primary',
                size: 'small'
              },
              on: {
                click: () => {
                  store.dispatch('Logout').then(() => {
                    isReloginShow = false
                    location.href = '/index'
                  })
                }
              }
            },
            '确认'
          )
        },
        duration: 0,
        onClose: () => {
          isReloginShow = false
        }
      })
    }
  } else if (code === 500) {
    if (requestUrl !== '/login') {
      notification.error({
        message: msg,
        description: msg
      })
    }
  } else if (code !== 200) {
    notification.error({
      message: msg
    })
  } else {
    return res.data
  }
  return Promise.reject(msg)
}, errorHandler)

const installer = {
  vm: {},
  install (Vue) {
    Vue.use(VueAxios, request)
  }
}

// 通用下载方法
export function download (url, params, filename) {
  const notificationKey = 'download'
  notification.open({
    key: notificationKey,
    message: '正在下载数据，请稍候',
    duration: null,
    icon: h => {
      return h(
        'a-icon',
        {
          props: {
            type: 'loading'
          }
        }
      )
      }
  })
  return request.post(url, params, {
    transformRequest: [(params) => { return qs.stringify(params, { indices: false }) }],
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    responseType: 'blob'
  }).then(async (data) => {
    const isLogin = await blobValidate(data)
    if (isLogin) {
      const blob = new Blob([data])
      saveAs(blob, filename)
      message.success('下载成功')
    } else {
      const resText = await data.text()
      const rspObj = JSON.parse(resText)
      const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
      message.error(errMsg)
    }
    notification.close(notificationKey)
  }).catch((r) => {
    message.error('下载文件出现错误，请联系管理员！')
    notification.close(notificationKey)
  })
}

export default request

export {
  installer as VueAxios,
  request as axios
}
