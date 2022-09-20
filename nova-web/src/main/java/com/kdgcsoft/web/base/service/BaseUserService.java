package com.kdgcsoft.web.base.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kdgcsoft.web.base.entity.BaseDept;
import com.kdgcsoft.web.base.entity.BaseOrg;
import com.kdgcsoft.web.base.entity.BaseUser;
import com.kdgcsoft.web.base.mapper.BaseUserMapper;
import com.kdgcsoft.web.common.model.PageRequest;
import icu.mhb.mybatisplus.plugln.core.JoinLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class BaseUserService extends ServiceImpl<BaseUserMapper, BaseUser> {
    @Autowired
    BaseOrgService baseOrgService;

    public PageRequest pageUserByOrgId(PageRequest pageRequest, Long orgId, String search) {
        List<Long> orgIds = baseOrgService.getChildrenIds(orgId);
        orgIds.add(orgId);
//        baseMapper.joinSelectPage(pageRequest, BaseUser.class, new JoinLambdaWrapper<BaseUser>()
//                .selectAll(BaseUser.class)
//                .selectAs(BaseOrg::getOrgName, BaseUser::getOrgName)
//                .selectAs(BaseDept::getName, BaseUser::getDeptName)
//                .leftJoin(BaseOrg.class, BaseOrg::getId, BaseUser::getOrgId)
//                .leftJoin(BaseDept.class, BaseDept::getId, BaseUser::getDeptId)
//                .in(BaseUser::getOrgId, orgIds)
//                .like(StrUtil.isNotEmpty(search), BaseUser::getUserName, search)
//                .eq(BaseUser::getDeleted, Deleted.NO)
//                .orderByDesc(BaseUser::getUserId));
        JoinLambdaWrapper<BaseUser> wrapper = new JoinLambdaWrapper<>(BaseUser.class);
        wrapper.leftJoin(BaseOrg.class, BaseOrg::getOrgId, BaseUser::getOrgId)
                .select(BaseOrg::getOrgName).end()
                .leftJoin(BaseDept.class, BaseDept::getDeptId, BaseUser::getDeptId)
                .select(BaseDept::getDeptName).end()
                .in(BaseUser::getOrgId, orgIds)
                .like(StrUtil.isNotEmpty(search), BaseUser::getUserName, search)
                .orderByAsc(BaseUser::getUserId);

        baseMapper.joinSelectPage(pageRequest, wrapper, BaseUser.class);
        return pageRequest;
    }

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




