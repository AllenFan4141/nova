package com.kdgcsoft.document.interfaces;

import com.kdgcsoft.document.modal.RepositorySetting;

import javax.jcr.SimpleCredentials;

/**
 * 文档操作类统一接口
 *
 * @author fyin
 * @date 2022年09月20日 11:45
 */
public interface DocumentRepository {
    void init(RepositorySetting setting);

    DocumentSession getSession();

    /**
     * 以指定用户名获得默认工作空间的session
     *
     * @param user
     * @return
     */
    DocumentSession getSession(SimpleCredentials user);

    /**
     * 以默认用户获得指定工作空间的session
     *
     * @param workspaceName
     * @return
     */
    DocumentSession getSession(String workspaceName);

    /**
     * 以指定用户名获得指定工作空间的session
     *
     * @param user
     * @param workspaceName
     * @return
     */
    DocumentSession getSession(SimpleCredentials user, String workspaceName);

    /**
     * 是否支持动态注入mixin节点
     *
     * @return
     */
    boolean supportRegisterMixin();
}
