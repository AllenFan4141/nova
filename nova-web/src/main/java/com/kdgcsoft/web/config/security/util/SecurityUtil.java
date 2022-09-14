package com.kdgcsoft.web.config.security.util;

import com.kdgcsoft.common.exception.BizException;
import com.kdgcsoft.web.common.enums.UserType;
import com.kdgcsoft.web.common.model.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author fyin
 * @date 2022年08月30日 14:26
 */
public class SecurityUtil {
    /**
     * 强制获得当前登录用户的ID,没有当前登录用户则使用-1
     *
     * @return
     */
    public static Long getForceLoginUserId() {
        try {
            LoginUser loginUser = getLoginUser();
            return loginUser.getUserId();
        } catch (Exception e) {
            return -1L;
        }
    }

    /**
     * 获取当前登陆用户
     *
     * @return
     */
    public static LoginUser getLoginUser() {
        LoginUser loginUser;
        try {
            loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new BizException("当前登录用户获取出错", e);
        }
        return loginUser;
    }

    /**
     * 判断当前用户是否是Root用户
     *
     * @return
     */
    public static boolean isRootUser() {
        return getLoginUser().getUserType() == UserType.ROOT;
    }

    /**
     * 判断当前登录用户是否是普通用户
     *
     * @return
     */
    public static boolean isNormalUser() {
        return getLoginUser().getUserType() == UserType.NORMAL;
    }

    /**
     * 获取当前的身份认证
     *
     * @return
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
