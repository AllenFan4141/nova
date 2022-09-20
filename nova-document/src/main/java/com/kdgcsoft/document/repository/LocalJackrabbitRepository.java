package com.kdgcsoft.document.repository;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.kdgcsoft.document.consts.DocumentConst;
import com.kdgcsoft.document.exception.DocumentException;
import com.kdgcsoft.document.interfaces.DocumentRepository;
import com.kdgcsoft.document.interfaces.DocumentSession;
import com.kdgcsoft.document.modal.RepositorySetting;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.jackrabbit.commons.cnd.CndImporter;
import org.apache.jackrabbit.core.TransientRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.nodetype.NodeTypeManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author fyin
 * @date 2021-05-19 14:35
 */
@Slf4j
public class LocalJackrabbitRepository implements DocumentRepository {
    /**
     * 仓库基础目录和仓库名的验证正则,仅能包含数字和字母
     */
    private static final String REG_CH = "^[a-zA-Z0-9]+$";
    /**
     * 路径查找器
     */
    private final PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();

    /**
     * 仓库是否支持元数据的写入
     */
    private boolean supportMetaData = false;

    /**
     * 默认仓库名称
     */
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";
    private RepositorySetting setting;
    private String repositoryName;
    private String repositoryConfigPath;
    private String baseDir;
    private String username;
    private String password;
    private TransientRepository repository;

    @Override
    public void init(RepositorySetting setting) {
        this.setting = setting;
        checkAndSetDefault();
        this.repository = createRepository();
        extConfig();
    }

    private void checkAndSetDefault() {
        this.baseDir = StrUtil.emptyToDefault(this.setting.getBaseDir(), DocumentConst.DEF_DIR);
        this.repositoryName = StrUtil.emptyToDefault(this.setting.getRepositoryName(), DocumentConst.DEF_WORKSPACE);
        this.repositoryConfigPath = StrUtil.emptyToDefault(this.setting.getRepositoryConfigPath(), DocumentConst.DEF_CONFIG_XML);
        if (!ReUtil.isMatch(REG_CH, this.repositoryName)) {
            throw new DocumentException("仓库名仅能包含字母与数字");
        }
        if (!ReUtil.isMatch(REG_CH, this.baseDir)) {
            throw new DocumentException("仓库根目录名仅能包含字母与数字");
        }
        this.username = StrUtil.emptyToDefault(this.setting.getUsername(), DEFAULT_USERNAME);
        this.password = StrUtil.emptyToDefault(this.setting.getPassword(), DEFAULT_PASSWORD);
    }

    /**
     * 创建Repository
     *
     * @return Repository
     */
    private TransientRepository createRepository() {
        try {
            Resource resource = pathResolver.getResource(this.repositoryConfigPath);
            File basedir = new File(this.baseDir);
            //创建仓库基本目录
            FileUtil.mkdir(basedir);
            if (resource.isReadable()) {
                //因为仓库创建类只支持传入文件,所以将配置文件拷贝到仓库的基础目录下
                File configFile = new File(baseDir, this.repositoryConfigPath);
                //由于默认的配置文件存在jar包中,所以不能使用resource.getFile来获取,需要使用InputStream进行文件拷贝
                FileUtils.copyInputStreamToFile(resource.getInputStream(), configFile);
                log.info("Copy config file form [{}] to [{}]", resource.getURL(), configFile.getAbsolutePath());
                log.info("Repository will create with config file:[{}]", configFile.getAbsolutePath());
                TransientRepository transientRepository = new TransientRepository(configFile, basedir);
                log.debug("Will create repository at path: {}", transientRepository.getHomeDir());
                return transientRepository;
            } else {
                throw new IOException("Can't read config file:" + resource.getURL());
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new DocumentException(e);
        }

    }

    /**
     * 对仓库进行进一步的设置,这里注册了混合节点类型,可以使节点属性也支持全文检索
     */
    private void extConfig() {
        try {
            log.debug("connect to repository");
            Session session = this.repository.login(new SimpleCredentials(this.username, this.password.toCharArray()), this.repositoryName);
            log.debug("注册Mixin混合节点类型.");
            registerMixinNodeTypes(session);
            log.debug("disconnect to repository");
            session.logout();
            log.info("Repository created at path '{}' with workspace '{}'", this.repository.getHomeDir(), this.repositoryName);
        } catch (NoSuchWorkspaceException e) {
            log.error("不存在的工作空间'{}',请检查{}中defaultWorkspace是否与配置文件中的uframe.document.name={}一致", this.repositoryName, this.repositoryConfigPath, this.repositoryName);
            log.error("如果您想连接指定的workspace,请参照以下步骤:");
            log.error("1.在{}/workspaces下创建执行的工作空间名称,如:'test'", this.baseDir);
            log.error("2.拷贝{}/workspaces/default下workspace.xml文件至{}/workspaces/test目录下,并将文件中的<Workspace name=\"default\"> 改为<Workspace name=\"test\">", this.baseDir, this.baseDir);
            log.error("3.修改配置文件uframe.document.name=test,系统启动即可通过DocumentManager.getSession方法获得指定workspace的操作session");
            throw new DocumentException(e);
        } catch (RepositoryException e) {
            log.error(e.getMessage(), e);
            throw new DocumentException(e);
        }
    }

    private void registerMixinNodeTypes(Session session) {
        try {
            NodeTypeManager nodeTypeManager = session.getWorkspace().getNodeTypeManager();
            if (nodeTypeManager.hasNodeType(DocumentConst.MIXIN_NAME)) {
                supportMetaData = true;
            } else {
                if (supportRegisterMixin()) {
                    // 注册动态属性，以提供按照属性检索的能力。
                    // 只支持本地仓库，RMI远程仓库会报错，因为API不支持。官方问题编号：JCR-3206
                    try {
                        Resource resource = this.pathResolver.getResource(DocumentConst.MIXIN_FILE);
                        CndImporter.registerNodeTypes(new InputStreamReader(resource.getInputStream()), session, false);
                        supportMetaData = true;
                    } catch (Exception e) {
                        supportMetaData = false;
                        log.error("注册Mixin节点类型失败，将不支持存储文件自定义属性", e);
                    }
                } else {
                    supportMetaData = false;
                }
            }
            if (!supportMetaData) {
                log.warn("Jackrabbit以RMI方式连接远程仓库时，不支持注册Mixin节点类型，因此将无法存储文件自定义属性。");
                log.warn("如果需要存储自定义属性，请把custom_nodetypes.xml文件拷贝到远程仓库的repository/nodetypes/下并重新启动仓库。");
            }
        } catch (RepositoryException e) {
            throw new DocumentException(e);
        }
    }

    @Override
    public DocumentSession getSession(SimpleCredentials user, String workspaceName) {
        try {
            Session session = this.repository.login(new SimpleCredentials(this.username, this.password.toCharArray()), workspaceName);
            return new LocalJackrabbitSession(session, user.getUserID(), supportMetaData);
        } catch (RepositoryException e) {
            log.error(e.getMessage(), e);
            throw new DocumentException(e);
        }
    }

    @Override
    public DocumentSession getSession() {
        return getSession(new SimpleCredentials(this.username, this.password.toCharArray()), DocumentConst.DEF_WORKSPACE);
    }

    @Override
    public DocumentSession getSession(SimpleCredentials user) {
        return getSession(user, DocumentConst.DEF_WORKSPACE);
    }

    @Override
    public DocumentSession getSession(String workspaceName) {
        return getSession(new SimpleCredentials(this.username, this.password.toCharArray()), workspaceName);
    }

    @Override
    public boolean supportRegisterMixin() {
        return true;
    }
}
