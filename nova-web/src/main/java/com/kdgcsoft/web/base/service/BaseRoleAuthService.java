package com.kdgcsoft.web.base.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kdgcsoft.web.base.entity.BaseRole;
import com.kdgcsoft.web.base.entity.BaseRoleAuth;
import com.kdgcsoft.web.base.entity.BaseRoleUser;
import com.kdgcsoft.web.base.mapper.BaseRoleAuthMapper;
import com.kdgcsoft.web.common.model.LoginUser;
import icu.mhb.mybatisplus.plugln.core.JoinLambdaWrapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class BaseRoleAuthService extends ServiceImpl<BaseRoleAuthMapper, BaseRoleAuth> {


    public List<String> getUserAuthCodes(LoginUser loginUser) {
        JoinLambdaWrapper<BaseRoleAuth> wrapper = new JoinLambdaWrapper(BaseRoleAuth.class);
        wrapper.leftJoin(BaseRoleUser.class, BaseRoleUser::getRoleId, BaseRoleAuth::getRoleId)
                .eq(BaseRoleUser::getUserId, loginUser.getUserId()).end()
                .select(BaseRoleAuth::getAuthCode);
        return baseMapper.joinSelectList(wrapper, String.class);

    }

}




