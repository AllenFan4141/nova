package com.kdgcsoft.document.enums;

/**
 * 仓库类型
 *
 * @author fyin
 * @date 2021-05-19 11:01
 */
public enum RepositoryType {
    localDir("目录存储"),
    localJackrabbit("本地jackrabbit仓库"),
    remoteJackrabbit("远程jackrabbit仓库");

    private String text;

    RepositoryType(String text) {
        this.text = text;
    }
}
