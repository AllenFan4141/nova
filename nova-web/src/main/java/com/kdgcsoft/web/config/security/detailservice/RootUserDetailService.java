package com.kdgcsoft.web.config.security.detailservice;

import cn.hutool.core.util.StrUtil;
import com.kdgcsoft.web.common.consts.WebConst;
import com.kdgcsoft.web.common.enums.UserType;
import com.kdgcsoft.web.common.model.LoginUser;
import com.kdgcsoft.web.config.NovaProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * root用户的验证逻辑
 *
 * @author fyin
 * @date 2022年08月30日 11:19
 */
public class RootUserDetailService implements UserDetailsService {
    @Autowired
    NovaProperties novaProperties;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!StrUtil.equals(novaProperties.getRootName(), username)) {
            throw new UsernameNotFoundException("用户不存在");
        } else {
            LoginUser root = new LoginUser();
            root.setUserType(UserType.ROOT);
            root.setUserName(novaProperties.getRootName());
            root.setFullName(WebConst.DEF_ROOT_FULL_NAME);
            root.setAvatar(WebConst.DEF_ROOT_AVATAR);
            root.setUserId(0L);
            root.setOrgId(0L);
            root.setDeptId(0L);
            root.setUserPassword(passwordEncoder.encode(novaProperties.getRootPassword()));
            return root;
        }
    }
}
