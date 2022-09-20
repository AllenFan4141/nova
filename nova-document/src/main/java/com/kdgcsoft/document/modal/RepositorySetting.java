package com.kdgcsoft.document.modal;

import com.kdgcsoft.document.config.DocumentProperties;
import com.kdgcsoft.document.enums.RepositoryType;
import lombok.Getter;

/**
 * @author fyin
 * @date 2021-05-19 14:37
 */
@Getter
public class RepositorySetting {
    private String baseDir;
    private String repositoryName;
    private RepositoryType type = RepositoryType.localJackrabbit;
    private String repositoryUrl;
    private String repositoryConfigPath;
    private String username;
    private String password;

    public RepositorySetting type(RepositoryType type) {
        this.type = type;
        return this;
    }

    public RepositorySetting dir(String baseDir) {
        this.baseDir = baseDir;
        return this;
    }

    public RepositorySetting name(String repositoryName) {
        this.repositoryName = repositoryName;
        return this;
    }

    public RepositorySetting url(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
        return this;
    }

    public RepositorySetting configPath(String repositoryConfigUrl) {
        this.repositoryConfigPath = repositoryConfigUrl;
        return this;
    }

    public RepositorySetting username(String username) {
        this.username = username;
        return this;
    }

    public RepositorySetting password(String password) {
        this.password = password;
        return this;
    }

    public static RepositorySetting withProperties(DocumentProperties properties) {
        RepositorySetting setting = new RepositorySetting()
                .dir(properties.getBaseDir())
                .name(properties.getName())
                .url(properties.getRepositoryUrl())
                .username(properties.getUsername())
                .password(properties.getPassword())
                .type(properties.getType())
                .configPath(properties.getRepositoryConfig());
        return setting;
    }
}
