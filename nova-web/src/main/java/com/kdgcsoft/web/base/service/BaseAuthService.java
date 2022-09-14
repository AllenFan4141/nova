package com.kdgcsoft.web.base.service;

import com.kdgcsoft.web.common.consts.I18N;
import com.kdgcsoft.web.common.consts.WebConst;
import com.kdgcsoft.web.common.model.JsonResult;
import com.kdgcsoft.web.common.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author fyin
 * @date 2022年08月25日 17:15
 */
@Service
@Slf4j
public class BaseAuthService {
    @Autowired
    private TokenService tokenService;
    @Resource
    private AuthenticationManager authenticationManager;

    public JsonResult login(String username, String password, boolean rememberMe) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            String token = tokenService.createToken(loginUser);
            return JsonResult.OK(I18N.AUTH_SUCCESS).data(token);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return JsonResult.ERROR(e.getMessage());
        }
    }
}
