package com.kdgcsoft.web.base.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kdgcsoft.common.util.TreeUtil;
import com.kdgcsoft.web.base.entity.BaseOrg;
import com.kdgcsoft.web.base.mapper.BaseOrgMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class BaseOrgService extends ServiceImpl<BaseOrgMapper, BaseOrg> {

    public List<BaseOrg> listAllByOrder() {
        return baseMapper.selectList(new LambdaQueryWrapper<BaseOrg>().orderByAsc(BaseOrg::getOrderNo));
    }

    /**
     * 返回组织机构的树形结构(带排序)
     *
     * @return
     */
    public List<BaseOrg> tree() {
        return TreeUtil.buildTree(this.listAllByOrder());
    }


    /**
     * 检查是否有编码重复的数据
     *
     * @param entity
     * @return
     */
    public boolean hasRepeat(BaseOrg entity) {
        return baseMapper.exists(new LambdaQueryWrapper<BaseOrg>()
                .eq(BaseOrg::getOrgCode, entity.getOrgCode())
                .ne(entity.getOrgId() != null, BaseOrg::getOrgId, entity.getOrgId())
        );
    }
}




