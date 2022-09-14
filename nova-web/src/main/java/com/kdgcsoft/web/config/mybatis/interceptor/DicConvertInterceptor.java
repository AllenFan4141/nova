package com.kdgcsoft.web.config.mybatis.interceptor;

/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.kdgcsoft.common.anno.DicBind;
import com.kdgcsoft.web.base.service.BaseDicService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * 字典转换拦截器,用于将结果集中的字典值转换出对应的字典文字描述
 *
 * @author hubin nieqiurong TaoYu
 * @since 2016-07-07
 */
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
@Slf4j
public class DicConvertInterceptor implements Interceptor {
    private BaseDicService dicService;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (dicService == null) {
            this.dicService = SpringUtil.getBean(BaseDicService.class);
        }
        DefaultResultSetHandler handler = (DefaultResultSetHandler) invocation.getTarget();
        Statement statement = (Statement) invocation.getArgs()[0];
        List<Object> resultList = handler.handleResultSets(statement);
        convertResultList(resultList);
        return resultList;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof ResultSetHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * 从结果集中找到要转换的字典列表
     *
     * @param resultList
     * @return
     */
    private void convertResultList(List<Object> resultList) {
        if (CollUtil.isEmpty(resultList)) {
            return;
        }
        //只取第一条数据的类型就行了
        Object resultRow = resultList.get(0);
        //没有数据不需要转换
        if (resultRow == null) {
            return;
        }
        Class<?> resultType = resultRow.getClass();
        //简单值类型不需要转换
        if (ClassUtil.isSimpleValueType(resultType)) {
            return;
        }
        //map类型没办法获取类型上的注解所以也无法转换
        if (resultType.isAssignableFrom(Map.class)) {
            return;
        }
        //类属性中不包含DicConvert注解则跳过
        if (!hasDicBindAnno(resultType)) {
            return;
        }

        Field[] fields = resultType.getDeclaredFields();
        for (Object o : resultList) {
            for (Field field : fields) {
                bindDicTargetField(o, field);
            }
        }
    }

    /**
     * 指定类是否有字典绑定字段
     *
     * @param clazz 指定类
     * @return
     */
    public boolean hasDicBindAnno(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //获取实体类属性上的字典注解
            DicBind dicConvert = field.getAnnotation(DicBind.class);
            if (dicConvert != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据对象字典字段和值 给目标字段赋值为字典文字
     *
     * @param o        对象
     * @param dicField 字典字段
     */
    public void bindDicTargetField(Object o, Field dicField) {
        DicBind dicBind = dicField.getAnnotation(DicBind.class);
        if (dicBind == null) {
            return;
        }
        Object fieldValue = BeanUtil.getFieldValue(o, dicField.getName());
        //字典字段没有值则返回
        if (fieldValue == null) {
            return;
        }

        //要绑定的字段不存在则返回
        if (!ReflectUtil.hasField(o.getClass(), dicBind.target())) {
            return;
        }
        //匹配字典编码,指定了字典编码就使用字典编码,没有指定编码就用类名(一般是枚举)
        String dicCode = StrUtil.emptyToDefault(dicBind.code(), dicField.getType().getSimpleName());
        String dicText = dicService.getDicText(dicCode, fieldValue);
        if (dicText != null) {
            BeanUtil.setFieldValue(o, dicBind.target(), dicText);
        }
    }
}