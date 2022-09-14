package com.kdgcsoft.web.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fyin
 * @date 2022年08月30日 9:34
 */
@ConfigurationProperties(prefix = "nova")
@Getter
public class NovaProperties {
    /**
     * 项目的系统名称
     */
    @Value("${nova.name:'nova'}")
    private String name;
    /**
     * 项目的一些描述信息
     */
    @Value("${nova.description:''}")
    private String description;

    /**
     * 项目是否为调试模式
     */
    @Value("${nova.debug:false}")
    private boolean debug = false;
    /**
     * 项目是否为前后台分离项目
     */
    @Value("${nova.font-backend:true}")
    private boolean fontBackend = true;
    /**
     * 是否启用root用户
     */
    @Value("${nova.root.enabled:true}")
    private boolean rootEnabled = true;

    /**
     * root用户的登录名
     */
    @Value("${nova.root.name:root}")
    private String rootName;
    /**
     * root用户的密码
     */
    @Value("${nova.root.password:root}")
    private String rootPassword;

    /**
     * 非前后端分离项目中登录页面的地址
     */
    @Value("${nova.loginPageUrl:/login}")
    private String loginPageUrl;

    /**
     * 登录请求发送的地址
     */
    @Value("${nova.loginUrl:/auth/login}")
    private String loginUrl;

    /**
     * 退出登录请求发送的地址
     */
    @Value("${nova.logoutUrl:/auth/logout}")
    private String logoutUrl;

    /**
     * 系统白名单路径,多个用逗号分隔
     */
    @Value("${nova.whiteList:}")
    private String whiteList;

    /**
     * 同一用户允许同时登录的最大连接数 -1表示无限制
     */
    @Value("${nova.maxSession:-1}")
    private Integer maxSession;

    @Value("${nova.jwt.cacheType:local}")
    private String jwtCacheType = "local";

    /**
     * 获取 jwt字符串的http头
     */
    @Value("${nova.token.header:Authorization}")
    private String tokenHeader;

    /**
     * jwt加密 令牌秘钥
     */
    @Value("${nova.token.secret:abcdefghijklmnopqrstuvwxyz}")
    private String tokenSecret;

    /**
     * jwt令牌有效期（默认30分钟）
     */
    @Value("${token.expireTime:30}")
    private int tokenExpireTime;

    /**
     * 是否记录sql日志
     */
    @Value("${nova.sql.print:true}")
    private boolean printSql = true;
    /**
     * 是否记录sql日志
     */
    @Value("${nova.sql.logSql:true}")
    private boolean logSql = true;
    /**
     * 是否只记录慢sql日志
     */
    @Value("${nova.sql.logOnlySlowSql:true}")
    private boolean logOnlySlowSql = true;
    /**
     * 慢sql的毫秒数
     */
    @Value("${nova.sql.slowSqlMillis:1000}")
    private long slowSqlMillis = 1000L;
}
