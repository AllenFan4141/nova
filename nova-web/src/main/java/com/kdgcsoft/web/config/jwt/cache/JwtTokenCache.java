package com.kdgcsoft.web.config.jwt.cache;

import com.kdgcsoft.web.common.model.LoginUser;

/**
 * jwt token的缓存,用来实现动态刷新等操作
 *
 * @author fyin
 * @date 2022年09月08日 17:48
 */
public interface JwtTokenCache {
    /**
     * 从缓存中根据key获取缓存的LoginUser对象,不存在时返回null
     *
     * @param key key
     * @return
     */
    LoginUser get(String key);

    /**
     * 根据key移除对应的缓存
     *
     * @param key key
     * @return
     */
    void remove(String key);

    /**
     * 根据key缓存指定对象并设置过期时间
     *
     * @param key       key
     * @param loginUser 缓存的对象
     * @param timeout   过期时间(ms)
     * @return
     */
    void put(String key, LoginUser loginUser, long timeout);

    /**
     * 刷新key的有效期
     *
     * @param key key
     * @param timeout 过期时间(ms)
     */
    void refresh(String key, long timeout);
}
