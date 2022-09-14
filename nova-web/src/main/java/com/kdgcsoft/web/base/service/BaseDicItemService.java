package com.kdgcsoft.web.base.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kdgcsoft.web.base.entity.BaseDic;
import com.kdgcsoft.web.base.entity.BaseDicItem;
import com.kdgcsoft.web.base.mapper.BaseDicItemMapper;
import icu.mhb.mybatisplus.plugln.base.service.impl.JoinServiceImpl;
import icu.mhb.mybatisplus.plugln.core.JoinLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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



    @Cacheable(value = CACHE_NAME, key = "#root.methodName+'.'+#dicCode")
    public List<BaseDicItem> listItemsByDicCode(String dicCode) {
        List<BaseDicItem> items = baseMapper.selectList(new QueryWrapper<BaseDicItem>().lambda()
                .eq(BaseDicItem::getDicCode, dicCode)
                .orderByAsc(BaseDicItem::getOrderNo));
        return items;
    }
}




