package com.kdgcsoft.web.base.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kdgcsoft.common.util.TreeUtil;
import com.kdgcsoft.web.base.entity.BaseDept;
import com.kdgcsoft.web.base.entity.BaseOrg;
import com.kdgcsoft.web.base.mapper.BaseDeptMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class BaseDeptService extends ServiceImpl<BaseDeptMapper, BaseDept>{

    public List<BaseDept> listAllByOrder() {
        return baseMapper.selectList(new LambdaQueryWrapper<BaseDept>().orderByAsc(BaseDept::getOrderNo));
    }

    /**
     * 返回组织机构的树形结构(带排序)
     *
     * @return
     */
    public List<BaseDept> tree() {
        return TreeUtil.buildTree(this.listAllByOrder());
    }


    /**
     * 检查是否有编码重复的数据
     *
     * @param entity
     * @return
     */
    public boolean hasRepeat(BaseDept entity) {
        return baseMapper.exists(new LambdaQueryWrapper<BaseDept>()
                .eq(BaseDept::getDeptCode, entity.getDeptCode())
                .ne(entity.getDeptId() != null, BaseDept::getDeptId, entity.getDeptId())
        );
    }
}




