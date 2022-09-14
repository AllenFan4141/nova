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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import com.kdgcsoft.web.config.security.util.SecurityUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.sql.Statement;
import java.text.DateFormat;
import java.util.*;

/**
 * 性能分析拦截器，用于输出每条 SQL 语句及其执行时间
 *
 * @author hubin nieqiurong TaoYu
 * @since 2016-07-07
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
})
@Slf4j
public class PerformanceInterceptor implements Interceptor {

    /**
     * SQL 执行最大时长，超过自动停止运行，有助于发现问题。
     */
    @Setter
    @Getter
    @Accessors(chain = true)
    private long slowSqlMillis = 1000L;
    /**
     * 是否写入日志文件
     * <p>true 写入日志文件，不阻断程序执行！</p>
     * <p>超过设定的最大执行时长异常提示！</p>
     */
    @Setter
    @Getter
    @Accessors(chain = true)
    private boolean logSql = true;
    /**
     * 是否只记录慢Sql
     */
    @Setter
    @Getter
    private boolean logOnlySlowSql = true;
    /**
     * 是否打印sql在控制台
     */
    @Setter
    @Getter
    private boolean printSql = true;

    /**
     * 打印sql的格式化文件
     */
    private static final String SQL_FMT = StringPool.NEWLINE +
            "┌ ID    : {sqlId}" + StringPool.NEWLINE +
            "├ SQL   : {sql}" + StringPool.NEWLINE +
            "├ Params: {params}" + StringPool.NEWLINE +
            "└ Type:{type},UserId:{userId},Result:{result},Times:{times}.";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 计算执行 SQL 耗时
        long start = SystemClock.now();
        Object result = invocation.proceed();
        long timing = SystemClock.now() - start;
        /**
         * 如果不打印sql也不记录sql则 为了节省性能直接跳过
         */
        if (!this.printSql && !this.logSql) {
            return result;
        }

        //判断是否是慢sql
        boolean isSlowSql = this.slowSqlMillis > 0 && timing > this.slowSqlMillis;

        //如果打印sql 则调用system.print
        String fmtSql = buildSql(result, invocation, timing);
        //控制台打印sql
        if (this.printSql) {
            System.err.println(fmtSql);
        }

        //日志记录sql的话就根据情况
        if (this.logSql) {
            //如果不是只记录慢sql的话 就全都打印
            if (!this.logOnlySlowSql) {
                log.info(fmtSql);
            } else if (this.logOnlySlowSql && isSlowSql) {
                //如果是只记录慢sql且当前sql是慢sql的话才记录
                log.info(fmtSql);
            } else {
                //其他情况不记录(只记录慢sql但是当前不是慢sql)sql
            }
        }
        return result;
    }

    /**
     * 获得格式化后的sql日志
     *
     * @param result
     * @param invocation
     * @param timing
     * @return
     */
    public String buildSql(Object result, Invocation invocation, long timing) {
        String resultInfo = "";
        if (result != null) {
            if (result instanceof List) {
                List list = (List) result;
                resultInfo = list.size() + " rows";
            } else if (result instanceof Map) {
                Map map = (Map) result;
                resultInfo = map.size() + " rows";
            } else {
                resultInfo = result.getClass().getSimpleName();
            }
        }

        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        // 格式化 SQL 打印执行结果
        Object target = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(target);
        MappedStatement ms = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        BoundSql boundSql = statementHandler.getBoundSql();
        Map<String, Object> fmtMap = MapUtil.newHashMap();
        fmtMap.put("sqlId", ms.getId());
        fmtMap.put("sql", StrUtil.replace(boundSql.getSql(), StringPool.NEWLINE, " "));
        fmtMap.put("params", getParamsString(boundSql, ms));
        fmtMap.put("type", ms.getSqlCommandType().name());
        fmtMap.put("userId", SecurityUtil.getForceLoginUserId());
        fmtMap.put("result", resultInfo);
        fmtMap.put("times", timing + "ms");
        return StrUtil.format(SQL_FMT, fmtMap);
    }


    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties prop) {
    }


    private String getParamsString(BoundSql boundSql, MappedStatement ms) {
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        Object parameterObject = boundSql.getParameterObject();
        List<String> paramValuesList = new ArrayList<>();
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = ms.getConfiguration().getTypeHandlerRegistry();
            /* 如果参数满足：org.apache.ibatis.type.TypeHandlerRegistry#hasTypeHandler(java.lang.Class<?>)
            org.apache.ibatis.type.TypeHandlerRegistry#TYPE_HANDLER_MAP
            * 即是不是属于注册类型(TYPE_HANDLER_MAP...等/有没有相应的类型处理器)
             * */
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                paramValuesList.add(getParameterValue(parameterObject));
            } else {
                //装饰器，可直接操作属性值 ---》 以parameterObject创建装饰器
                //MetaObject 是 Mybatis 反射工具类，通过 MetaObject 获取和设置对象的属性值
                MetaObject metaObject = ms.getConfiguration().newMetaObject(parameterObject);
                //循环 parameterMappings 所有属性
                for (ParameterMapping parameterMapping : parameterMappings) {
                    //获取property属性
                    String propertyName = parameterMapping.getProperty();
                    //是否声明了propertyName的属性和get方法
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        paramValuesList.add(getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        //判断是不是sql的附加参数
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        paramValuesList.add(getParameterValue(obj));
                    }
                }
            }
        }
        return CollUtil.join(paramValuesList, ",");
    }

    private String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "to_date('" + formatter.format(obj) + "','yyyy-MM-dd hh24:mi:ss')";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }
}