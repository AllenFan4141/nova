package com.kdgcsoft.document.modal;

import com.kdgcsoft.document.enums.Type;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 文档信息
 *
 * @author fyin
 * @date 2021-05-19 10:39
 */
@Setter
@Getter
@Accessors(chain = true)
public class DocumentInfo {
    /**
     * 文件ID
     */
    private String id;
    /**
     * 文件的存储名
     */
    private String name;
    /**
     * 文件的存储路径
     */
    private String path;
    /**
     * 类型,目录还是文件
     */
    private Type type;
    /**
     * 文件原始名
     */
    private String fileName;
    /**
     * 文件扩展名
     */
    private String fileExt;
    private String createBy;
    private Date createTime;
    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * 文件下载地址,从session中读取的时候是没有的
     */
    private String url;
}
