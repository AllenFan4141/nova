package com.kdgcsoft.web.base.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kdgcsoft.web.base.entity.BaseDic;
import com.kdgcsoft.web.base.entity.BaseDicItem;
import com.kdgcsoft.web.base.mapper.BaseDicItemMapper;
import icu.mhb.mybatisplus.plugln.base.service.impl.JoinServiceImpl;
import icu.mhb.mybatisplus.plugln.core.JoinLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author YINFAN
 */
@Service
public class BaseDicItemService extends ServiceImpl<BaseDicItemMapper, BaseDicItem> {
    /**
     * 缓存名称
     */
    private static final String CACHE_NAME = "BaseDicItem";
    @Autowired
    BaseDicService dicService;


    /**
     * 检查是否有编码重复的数据
     *
     * @param entity
     * @return
     */
    public boolean hasRepeat(BaseDicItem entity) {
        return baseMapper.exists(new LambdaQueryWrapper<BaseDicItem>()
                .eq(BaseDicItem::getDicCode, entity.getDicCode())
                .eq(BaseDicItem::getValue, entity.getValue())
                .ne(entity.getId() != null, BaseDicItem::getId, entity.getId())
        );
    }

    @CacheEvict(value = CACHE_NAME, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public int deleteByDicCode(String dicCode) {
        return baseMapper.delete(new QueryWrapper<BaseDicItem>().lambda().eq(BaseDicItem::getDicCode, dicCode));
    }


    @Cacheable(value = CACHE_NAME, key = "#root.methodName+'.'+#dicCode")
    public List<BaseDicItem> listItemsByDicCode(String dicCode) {
        List<BaseDicItem> items = baseMapper.selectList(new QueryWrapper<BaseDicItem>().lambda()
                .eq(BaseDicItem::getDicCode, dicCode)
                .orderByAsc(BaseDicItem::getOrderNo));
        return items;
    }
}




