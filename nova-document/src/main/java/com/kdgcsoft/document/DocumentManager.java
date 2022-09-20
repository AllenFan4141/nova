package com.kdgcsoft.document;

import cn.hutool.core.io.FileUtil;
import com.kdgcsoft.document.config.DocumentProperties;
import com.kdgcsoft.document.event.DocumentManagerInitedEvent;
import com.kdgcsoft.document.exception.DocumentException;
import com.kdgcsoft.document.interfaces.DocumentRepository;
import com.kdgcsoft.document.interfaces.DocumentSession;
import com.kdgcsoft.document.modal.DocumentInfo;
import com.kdgcsoft.document.modal.RepositorySetting;
import com.kdgcsoft.document.repository.LocalJackrabbitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.SimpleCredentials;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fyin
 * @date 2022年09月20日 11:37
 */
@Slf4j
public class DocumentManager {

    /**
     * 初始化状态
     */
    private boolean inited = false;
    private ApplicationContext applicationContext;
    private DocumentRepository repository;
    private RepositorySetting setting;
    private Map<String, DocumentSession> sessionMap = new ConcurrentHashMap<>();
    /**
     * 默认的工作空间的前缀
     */
    private static final String DEFAULT_WORKSPACE_KEY = "_default_workspace";
    /**
     * 默认的工作空间的前缀
     */
    private static final String DEFAULT_USER_KEY = "_default_user";
    /**
     * 默认session的key
     */
    private static final String DEFAULT_SESSION_KEY = DEFAULT_WORKSPACE_KEY + "." + DEFAULT_USER_KEY;

    public DocumentManager(DocumentProperties documentProperties, ApplicationContext applicationContext) {
        this.inited = false;
        this.applicationContext = applicationContext;
        this.setting = RepositorySetting.withProperties(documentProperties);
    }

    public DocumentManager(RepositorySetting setting, ApplicationContext applicationContext) {
        this.inited = false;
        this.applicationContext = applicationContext;
        this.setting = setting;
    }

    public void init() throws DocumentException {
        log.info("DocumentManager start init.");
        this.inited = false;
        switch (this.setting.getType()) {
            case localDir:
                this.repository = new LocalJackrabbitRepository();
                break;
            case localJackrabbit:
                this.repository = new LocalJackrabbitRepository();
                break;
            case remoteJackrabbit:
                this.repository = new LocalJackrabbitRepository();
                break;
        }
        log.info("Creating repository instance of [{}].", this.repository.getClass().getName());
        this.repository.init(this.setting);
        this.inited = true;
        log.info("DocumentManager init complete.");
        this.applicationContext.publishEvent(new DocumentManagerInitedEvent(this));
    }

    public boolean isInited() {
        return this.inited;
    }

    /**
     * 使用默认用户名连接默认工作空间
     *
     * @return
     */
    public DocumentSession getSession() {
        DocumentSession session;
        if (sessionMap.containsKey(DEFAULT_SESSION_KEY)) {
            session = sessionMap.get(DEFAULT_SESSION_KEY);
            try {
                if (!session.isValid()) {
                    session = this.repository.getSession();
                    sessionMap.put(DEFAULT_SESSION_KEY, session);
                }
            } catch (DocumentException e) {
                log.error(e.getMessage(), e);
            }
        } else {
            session = this.repository.getSession();
            sessionMap.put(DEFAULT_SESSION_KEY, session);
        }
        return session;
    }

    /**
     * 使用指定用户连接默认工作空间
     *
     * @param user
     * @return
     */
    public DocumentSession getSession(SimpleCredentials user) {
        DocumentSession session;
        String sessionKey = DEFAULT_WORKSPACE_KEY + "." + user.getUserID();
        if (sessionMap.containsKey(sessionKey)) {
            session = sessionMap.get(sessionKey);
        } else {
            session = this.repository.getSession(user);
            sessionMap.put(sessionKey, session);
        }
        return session;
    }

    /**
     * 使用默认用户连接指定工作空间
     *
     * @param workspaceName
     * @return
     */
    public DocumentSession getSession(String workspaceName) {
        DocumentSession session;
        String sessionKey = workspaceName + "." + DEFAULT_USER_KEY;
        if (sessionMap.containsKey(sessionKey)) {
            session = sessionMap.get(sessionKey);
        } else {
            session = this.repository.getSession(sessionKey);
            sessionMap.put(sessionKey, session);
        }
        return session;
    }

    /**
     * 使用指定用户连接指定工作空间
     *
     * @param user
     * @param workspaceName
     * @return
     * @throws DocumentException
     */
    public DocumentSession getSession(SimpleCredentials user, String workspaceName) {
        DocumentSession session;
        String sessionKey = workspaceName + "." + user.getUserID();
        if (sessionMap.containsKey(sessionKey)) {
            session = sessionMap.get(sessionKey);
        } else {
            session = this.repository.getSession(sessionKey);
            sessionMap.put(sessionKey, session);
        }
        return session;
    }


    public DocumentInfo putFile(String path, File file) {
        return this.putFile(path, file, file.getName());
    }

    public DocumentInfo putFile(String path, File file, String fileName) {
        return this.putFile(path, FileUtil.getInputStream(file), fileName);
    }

    public DocumentInfo putFile(String path, InputStream is, String fileName) {
        return this.getSession().putFile(path, is, fileName);
    }

    public DocumentInfo getFileInfo(String id) {
        return this.getSession().getFileInfo(id);
    }

    public InputStream readFile(String id) {
        return this.getSession().readFile(id);
    }

    public void readFile(String id, OutputStream outputStream) {
        this.getSession().readFile(id, outputStream);
    }

    public DocumentInfo createDir(Node parentNode, String path) {
        return this.getSession().createDir(parentNode, path);
    }

    public boolean removeDir(String path) throws DocumentException {
        return false;
    }

    public DocumentInfo createDir(String path) throws DocumentException {
        return this.getSession().createDir(path);
    }

    public boolean exist(String id) {
        return this.getSession().exist(id);
    }

}
