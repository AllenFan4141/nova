package com.kdgcsoft.web.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.kdgcsoft.web.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author fyin
 * @date 2022年09月01日 15:07
 */
@Setter
@Getter
public class SysUser extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private String userId;
}
