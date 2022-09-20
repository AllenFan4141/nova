package com.kdgcsoft.web.common.consts;

/**
 * 全局公共的静态属性
 *
 * @author fyin
 * @date 2022年08月30日 10:25
 */
public class WebConst {
    /**
     * 上下文中使用当前登陆人的占位符
     */
    public static final String LOGIN_USER = "loginUser";
    /**
     * 默认的root用户的显示名称
     */
    public static final String DEF_ROOT_FULL_NAME = "超级管理员";
    /**
     * 默认root用户的头像
     */
    public static final String DEF_ROOT_AVATAR = "root.png";
    /**
     * 默认的异步执行器的名称
     */
    public static final String ASYNC_EXECUTOR_NAME = "asyncExecutor";
    /**
     * websocket的默认端点
     */
    public static final String WEB_SOCKET_ENDPOINT = "/websocket";

    public static final String HEADER_AGENT_MSIE = "MSIE";
    public static final String HEADER_AGENT_TRIDENT = "TRIDENT";
    public static final String HEADER_AGENT_EDGE = "EDGE";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";
    /**
     * 获取令牌中登录用户的唯一ID的 key
     */
    public static final String LOGIN_UUID = "login_uuid";

    /**
     * 登录用户 jwt缓存的 key的前缀
     */
    public static final String JWT_TOKEN_CACHE_PREFIX = "login_jwt_token:";
    /**
     * 本地jwt缓存的名称
     */
    public static final String LOCAL_JWT_CACHE = "local";
    /**
     * redis jwt缓存的名称
     */
    public static final String REDIS_JWT_CACHE = "redis";

    /**
     * 默认的树形结构的根节点ID
     */
    public static final Long DEF_TREE_ROOT_ID = 0L;

}
