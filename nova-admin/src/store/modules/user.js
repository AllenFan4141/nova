import storage from 'store'
import { login, getInfo, logout } from '@/api/login'
import { ACCESS_TOKEN } from '@/store/mutation-types'

const user = {
  state: {
    token: '',
    name: '',
    welcome: '',
    avatar: '',
    roles: [],
    info: {}
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_INFO: (state, info) => {
      state.info = info
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    }
  },

  actions: {
    // 登录
    Login ({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo).then(res => {
          storage.set(ACCESS_TOKEN, res.data, 7 * 24 * 60 * 60 * 1000)
          commit('SET_TOKEN', res.data)
          resolve()
        })
        .catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo ({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo(state.token).then(res => {
          const userInfo = res.data
          const avatar = userInfo.avatar === '' ? require('@/assets/images/profile.jpg') : process.env.VUE_APP_BASE_API + userInfo.avatar
          if (userInfo.permissions && userInfo.permissions.length > 0) { // 验证返回的roles是否是一个非空数组
           // commit('SET_ROLES', userInfo.roles)
            commit('SET_PERMISSIONS', userInfo.permissions)
          }
          commit('SET_ROLES', ['ROLE_DEFAULT'])
          commit('SET_NAME', userInfo.nickName)
          commit('SET_AVATAR', avatar)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    Logout ({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          resolve()
        }).catch(error => {
          reject(error)
        }).finally(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          storage.remove(ACCESS_TOKEN)
        })
      })
    }

  }
}

export default user
