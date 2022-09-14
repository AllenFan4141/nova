package com.kdgcsoft.web.base.service;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import com.kdgcsoft.web.common.consts.WebConst;
import com.kdgcsoft.web.common.model.LoginUser;
import com.kdgcsoft.web.common.util.ServletUtils;
import com.kdgcsoft.web.config.NovaProperties;
import com.kdgcsoft.web.config.jwt.cache.JwtTokenCache;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * token验证处理
 *
 * @author fyin
 */
@Component
public class TokenService {


    @Autowired
    NovaProperties novaProperties;


    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
    /**
     * 令牌有效期不足20分钟自动刷新
     */
    private static final Long MILLIS_MINUTE_TEN = 20 * MILLIS_MINUTE;


    @Autowired
    private JwtTokenCache jwtTokenCache;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUserFromRequest(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getJwtTokenFromRequest(request);
        if (StrUtil.isNotBlank(token)) {
            try {
                Claims claims = parseToken(token);
                // 解析对应的权限以及用户信息
                String uuid = (String) claims.get(WebConst.LOGIN_UUID);
                String jwtCacheKey = getJwtCacheKey(uuid);
                LoginUser user = jwtTokenCache.get(jwtCacheKey);
                return user;
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (ObjectUtil.isNotNull(loginUser) && StrUtil.isNotEmpty(loginUser.getUuid())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String uuid) {
        if (StrUtil.isNotBlank(uuid)) {
            String jwtCacheKey = getJwtCacheKey(uuid);
            jwtTokenCache.remove(jwtCacheKey);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        String uuid = IdUtil.fastUUID();
        loginUser.setUuid(uuid);
        setUserAgent(loginUser);

        cacheToken(loginUser);

        Map<String, Object> claims = MapUtil.newHashMap();
        claims.put(WebConst.LOGIN_UUID, uuid);
        claims.put(Claims.SUBJECT, loginUser.getUsername());
        return createToken(claims);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 对令牌进行缓存
     *
     * @param loginUser 登录信息
     */
    public void cacheToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + novaProperties.getTokenExpireTime() * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String jwtCacheKey = getJwtCacheKey(loginUser.getUuid());
        jwtTokenCache.put(jwtCacheKey, loginUser, novaProperties.getTokenExpireTime() * MILLIS_MINUTE);
    }

    /**
     * 刷新令牌有效期 更新LoginUser中的有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setExpireTime(System.currentTimeMillis() + novaProperties.getTokenExpireTime() * MILLIS_MINUTE);
        // 根据uuid获取loginUser缓存
        String jwtCacheKey = getJwtCacheKey(loginUser.getUuid());
        jwtTokenCache.refresh(jwtCacheKey, novaProperties.getTokenExpireTime() * MILLIS_MINUTE);
    }

    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = ServletUtils.getUserAgent();
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOs().getName());
        loginUser.setIpAddress(ServletUtils.getClientIp());
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, novaProperties.getTokenSecret())
                .setIssuedAt(new Date())
                .compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(novaProperties.getTokenSecret()).parseClaimsJws(token).getBody();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 从请求中获取jwt token字符串
     *
     * @param request
     * @return token
     */
    private String getJwtTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(novaProperties.getTokenHeader());
        if (StrUtil.isNotEmpty(token) && token.startsWith(WebConst.TOKEN_PREFIX)) {
            token = token.replace(WebConst.TOKEN_PREFIX, "");
        }
        return token;
    }

    /**
     * 根据用户唯一ID 生成jwt缓存的key
     *
     * @param uuid
     * @return
     */
    private String getJwtCacheKey(String uuid) {
        return WebConst.JWT_TOKEN_CACHE_PREFIX + uuid;
    }
}
