package com.kdgcsoft.web.config.security.detailservice;

import com.kdgcsoft.web.base.entity.BaseUser;
import com.kdgcsoft.web.base.service.BaseUserService;
import com.kdgcsoft.web.common.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 普通用户的验证策略
 *
 * @author fyin
 * @date 2022年08月30日 11:21
 */
public class NormalUserDetailService implements UserDetailsService {
    @Autowired
    BaseUserService baseUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BaseUser baseUser = baseUserService.findByLoginName(username);
        if (baseUser == null) {
            throw new UsernameNotFoundException("");
        } else {
            LoginUser loginUser = baseUser.toLoginUser();
            return loginUser;
        }
    }
}
