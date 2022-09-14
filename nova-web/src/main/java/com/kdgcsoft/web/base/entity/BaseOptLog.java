package com.kdgcsoft.web.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.kdgcsoft.web.base.enums.Enabled;
import com.kdgcsoft.web.base.enums.OptType;
import lombok.Data;

/**
 * 操作日志
 *
 * @TableName base_opt_log
 */
@TableName(value = "base_opt_log")
@Data
public class BaseOptLog extends BaseEntity implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long logId;

    /**
     * 操作标题
     */
    private String logTitle;

    /**
     * 日志请求地址
     */
    private String logUrl;

    /**
     * 请求方式
     */
    private String httpMethod;

    /**
     * java方法调用
     */
    private String javaMethod;

    /**
     * 操作类型
     */
    private OptType optType;

    /**
     * 操作时间
     */
    private Date optTime;
    /**
     * 操作IP
     */
    private String optIp;

    /**
     * 操作耗时
     */
    private Long timeCost;

    /**
     * 响应是否成功
     */
    private Enabled success;

    /**
     * 响应结果
     */
    private String response;
    /**
     * 请求内容
     */
    private String request;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;
    /**
     * 操作的文字描述信息,使用SpEL表达式解析后的文字描述
     */
    private String optDescription;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}