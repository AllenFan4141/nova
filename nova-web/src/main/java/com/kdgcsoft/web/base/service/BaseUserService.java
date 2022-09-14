package com.kdgcsoft.web.base.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kdgcsoft.web.base.entity.BaseUser;
import com.kdgcsoft.web.base.mapper.BaseUserMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class BaseUserService extends ServiceImpl<BaseUserMapper, BaseUser> {

    /**
     * 根据登录名查找用户
     *
     * @param loginName
     * @return
     */
    public BaseUser findByLoginName(String loginName) {
        return baseMapper.selectOne(new LambdaQueryWrapper<BaseUser>().eq(BaseUser::getLoginName, loginName));
    }
}




