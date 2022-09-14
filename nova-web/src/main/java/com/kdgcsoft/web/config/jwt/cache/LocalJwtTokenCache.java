package com.kdgcsoft.web.config.jwt.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.kdgcsoft.web.common.model.LoginUser;

/**
 * @author fyin
 * @date 2022年09月09日 9:22
 */
public class LocalJwtTokenCache implements JwtTokenCache {

    private TimedCache<String, LoginUser> timedCache = CacheUtil.newTimedCache(0L);

    @Override
    public LoginUser get(String key) {
        return timedCache.get(key);
    }

    @Override
    public void remove(String key) {
        timedCache.remove(key);
    }

    @Override
    public void put(String key, LoginUser loginUser, long timeout) {
        timedCache.put(key, loginUser, timeout);
    }

    @Override
    public void refresh(String key, long timeout) {
        if (timedCache.containsKey(key)) {
            LoginUser loginUser = timedCache.get(key);
            timedCache.remove(key);
            timedCache.put(key, loginUser, timeout);
        }
    }
}
