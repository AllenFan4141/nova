package com.kdgcsoft.web.config.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.kdgcsoft.web.base.entity.BaseEntity;
import com.kdgcsoft.web.config.security.util.SecurityUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 数据操作自动填充处理程序
 *
 * @author fyin
 * @date 2021-04-25 14:54
 */
@Component
public class CommonMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId = SecurityUtil.getForceLoginUserId();
        setFieldValByName(BaseEntity.PROPERTY_CREATE_TIME, new Date(), metaObject);
        setFieldValByName(BaseEntity.PROPERTY_UPDATE_TIME, new Date(), metaObject);

        setFieldValByName(BaseEntity.PROPERTY_CREATE_BY, userId, metaObject);
        setFieldValByName(BaseEntity.PROPERTY_UPDATE_BY, userId, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId = SecurityUtil.getForceLoginUserId();
        setFieldValByName(BaseEntity.PROPERTY_UPDATE_TIME, new Date(), metaObject);
        setFieldValByName(BaseEntity.PROPERTY_UPDATE_BY, userId, metaObject);
    }
}
