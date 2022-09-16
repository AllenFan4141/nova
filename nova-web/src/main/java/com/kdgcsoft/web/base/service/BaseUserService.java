package com.kdgcsoft.web.base.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kdgcsoft.web.base.entity.BaseDept;
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


    /**
     * 检查是否有重复的登录名
     *
     * @param entity
     * @return
     */
    public boolean hasRepeat(BaseUser entity) {
        return baseMapper.exists(new LambdaQueryWrapper<BaseUser>().eq(BaseUser::getLoginName, entity.getLoginName()).ne(entity.getUserId() != null, BaseUser::getUserId, entity.getUserId()));
    }


}




