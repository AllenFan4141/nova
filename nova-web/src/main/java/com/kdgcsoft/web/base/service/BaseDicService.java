package com.kdgcsoft.web.base.service;

import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.kdgcsoft.web.base.entity.BaseDic;
import com.kdgcsoft.web.base.entity.BaseDicItem;
import com.kdgcsoft.web.base.mapper.BaseDicMapper;
import icu.mhb.mybatisplus.plugln.base.service.impl.JoinServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YINFAN
 */
@Service
public class BaseDicService extends JoinServiceImpl<BaseDicMapper, BaseDic> {
    /**
     * 缓存名称
     */
    private static final String CACHE_NAME = "BaseDic";

    @Autowired
    BaseDicItemService dicItemService;


    @Cacheable(value = CACHE_NAME, key = "#root.methodName+'.'+#dicCode")
    public Map<String, String> dicValueMap(String dicCode) {
        List<BaseDicItem> items = dicItemService.listItemsByDicCode(dicCode);
        HashMap<String, String> map = MapUtil.newHashMap();
        items.forEach(baseDicItem -> map.put(baseDicItem.getValue(), baseDicItem.getText()));
        return map;
    }

    /**
     * 根据字典编码和值 获取字典文字
     *
     * @param dicCode
     * @param value
     * @return
     */
    public String getDicText(String dicCode, Object value) {
        if (value == null) {
            return null;
        }
        //这里需要调用代理类提供的方法 而不是自己内部的方法 才能使缓存生效
        BaseDicService dicService = SpringUtil.getBean(BaseDicService.class);
        Map<String, String> dicValueMap = dicService.dicValueMap(dicCode);
        if (dicValueMap.containsKey(value.toString())) {
            return dicValueMap.get(value.toString());
        } else {
            return value.toString();
        }
    }
}