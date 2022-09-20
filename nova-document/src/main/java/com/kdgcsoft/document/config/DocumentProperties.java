package com.kdgcsoft.document.config;

import com.kdgcsoft.document.enums.RepositoryType;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fyin
 * @date 2021-05-19 10:50
 */
@ConfigurationProperties(prefix = "nova.document")
@Data
public class DocumentProperties {
    /**
     * 文件仓库根目录
     */
    @Value("${nova.document.baseDir:repository}")
    private String baseDir = "repository";
    /**
     * 仓库名称
     */
    @Value("${nova.document.name:default}")
    private String name = "default";
    /**
     * 仓库类型
     */
    @Value("${nova.document.repository.type:localJackrabbit}")
    private RepositoryType type = RepositoryType.localJackrabbit;
    /**
     * jackrabbit创建仓库时的配置文件存放路径
     */
    @Value("${nova.document.repository.config:repository.xml}")
    private String repositoryConfig;
    /**
     * 远程jackrabbit仓库的连接地址
     */
    @Value("${nova.document.repository.url:}")
    private String repositoryUrl = "";
    @Value("${nova.document.repository.username:}")
    private String username = "";
    @Value("${nova.document.repository.password:}")
    private String password = "";

}
