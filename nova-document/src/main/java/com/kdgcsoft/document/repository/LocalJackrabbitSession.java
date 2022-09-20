package com.kdgcsoft.document.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.kdgcsoft.document.consts.DocumentConst;
import com.kdgcsoft.document.enums.Type;
import com.kdgcsoft.document.exception.DocumentException;
import com.kdgcsoft.document.interfaces.DocumentSession;
import com.kdgcsoft.document.modal.DocumentInfo;
import com.kdgcsoft.document.modal.MetaProperty;
import com.kdgcsoft.document.util.JackrabbitUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.jackrabbit.commons.JcrUtils;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;

/**
 * @author fyin
 * @date 2021-05-20 9:45
 */
@Slf4j
public class LocalJackrabbitSession implements DocumentSession {
    /**
     * 节点名称的格式化字符串
     */
    private static final String NODE_NAME_FMT = "{}-{}";
    /**
     * 当前session用户
     */
    private String sessionUser;
    /**
     * 当前session是否支持文件元数据信息
     */
    private boolean supportMetadata;
    /**
     * jcr session
     */
    private Session session;

    public LocalJackrabbitSession(Session session, String user, boolean supportMetadata) {
        this.session = session;
        this.sessionUser = user;
        this.supportMetadata = supportMetadata;
    }

    @Override
    public boolean supportFullTextSearch() throws DocumentException {
        return false;
    }

    @Override
    public DocumentInfo createDir(Node parentNode, String path) throws DocumentException {
        List<String> paths = StrUtil.split(path, '/', true, true);
        Node dirNode = parentNode;
        if (CollUtil.isEmpty(paths)) {
            log.warn("empty path to create.");
        }
        try {
            for (String dir : paths) {
                dirNode = JcrUtils.getOrAddFolder(dirNode, dir);
                if (this.supportMetadata && dirNode.canAddMixin(DocumentConst.MIXIN_NAME)) {
                    dirNode.addMixin(DocumentConst.MIXIN_NAME);
                    setDefaultProperties(dirNode);
                }
            }
            return buildDocumentInfo(dirNode);
        } catch (RepositoryException e) {
            log.error("目录创建出错", e);
            throw new DocumentException(e);
        }
    }

    @Override
    public boolean removeDir(String path) throws DocumentException {
        return false;
    }

    @Override
    public DocumentInfo createDir(String path) throws DocumentException {
        try {
            return createDir(session.getRootNode(), path);
        } catch (RepositoryException e) {
            log.error(e.getMessage(), e);
            throw new DocumentException(e);
        }
    }

    @Override
    public DocumentInfo putFile(String path, File file) throws DocumentException {
        return this.putFile(path, file, file.getName());
    }

    @Override
    public DocumentInfo putFile(String path, File file, String fileName) throws DocumentException {
        return this.putFile(path, FileUtil.getInputStream(file), fileName);
    }

    @Override
    public DocumentInfo putFile(String path, InputStream is, String fileName) throws DocumentException {
        try {
            fileName = JackrabbitUtil.cleanFileName(fileName);
            Long time = System.currentTimeMillis();
            String nodeName = StrUtil.format(NODE_NAME_FMT, time, fileName);
            log.info("putFile '{}' to path '{}'", fileName, path);
            DocumentInfo dirInfo = this.createDir(path);
            Node dirNode = session.getNodeByIdentifier(dirInfo.getId());
            Node fileNode = JackrabbitUtil.putFile(this.session, dirNode, is, nodeName);
            if (this.supportMetadata && fileNode.canAddMixin(DocumentConst.MIXIN_NAME)) {
                fileNode.addMixin(DocumentConst.MIXIN_NAME);
                setDefaultProperties(fileNode);
                fileNode.setProperty(MetaProperty.META_FILE_NAME, fileName);
                Long fileSize = fileNode.getNode(Node.JCR_CONTENT).getProperty(Property.JCR_DATA).getLength();
                fileNode.setProperty(MetaProperty.META_FILE_SIZE, fileSize);
                fileNode.setProperty(MetaProperty.META_FILE_EXT, FileUtil.extName(fileName).toLowerCase());
            }
            session.save();
            DocumentInfo documentInfo = buildDocumentInfo(fileNode);
            return documentInfo;
        } catch (RepositoryException e) {
            log.error(e.getMessage(), e);
            throw new DocumentException(e);
        }
    }

    @Override
    public DocumentInfo getFileInfo(String id) throws DocumentException {

        try {
            Node node = JackrabbitUtil.getNodeById(session, id);
            DocumentInfo info = buildDocumentInfo(node);
            return info;
        } catch (RepositoryException e) {
            log.error(e.getMessage(), e);
            throw new DocumentException(e);
        }
    }

    @Override
    public InputStream readFile(String id) throws DocumentException {

        try {
            Node node = JackrabbitUtil.getNodeById(session, id);
            InputStream inputStream = JcrUtils.readFile(node);
            return inputStream;
        } catch (RepositoryException e) {
            log.error("文件读取出错", e);
            throw new DocumentException(e);
        }
    }

    @Override
    public void readFile(String id, OutputStream outputStream) throws DocumentException {
        try {
            Node node = JackrabbitUtil.getNodeById(session, id);
            JcrUtils.readFile(node, outputStream);
        } catch (RepositoryException e) {
            log.error("文件读取出错", e);
            throw new DocumentException(e);
        } catch (IOException e) {
            log.error("文件读取出错", e);
            throw new DocumentException(e);
        }
    }

    private DocumentInfo buildDocumentInfo(Node node) throws DocumentException {
        if (node == null) {
            return null;
        }
        DocumentInfo info = new DocumentInfo();
        try {
            info.setId(node.getIdentifier());
            info.setName(node.getName());
            info.setPath(node.getPath());
            if (node.getPrimaryNodeType().isNodeType(NodeType.NT_FILE)) {
                info.setType(Type.FILE);
                info.setFileName(node.getProperty(MetaProperty.META_FILE_NAME).getString());
                info.setFileExt(node.getProperty(MetaProperty.META_FILE_EXT).getString());
                info.setCreateBy(node.getProperty(MetaProperty.META_CREATE_BY).getString());
                info.setCreateTime(node.getProperty(MetaProperty.META_CREATE_TIME).getDate().getTime());
                info.setFileSize(node.getProperty(MetaProperty.META_FILE_SIZE).getLong());
            } else {
                info.setType(Type.DIR);
            }
            return info;
        } catch (PathNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new DocumentException(e);
        } catch (RepositoryException e) {
            log.error(e.getMessage(), e);
            throw new DocumentException(e);
        }
    }


    private void setDefaultProperties(Node node) {
        try {
            if (this.supportMetadata) {
                node.setProperty(MetaProperty.META_CREATE_BY, sessionUser);
                node.setProperty(MetaProperty.META_CREATE_TIME, Calendar.getInstance());
                node.setProperty(MetaProperty.META_MODIFY_BY, sessionUser);
                node.setProperty(MetaProperty.META_MODIFY_TIME, Calendar.getInstance());
            }
        } catch (RepositoryException e) {
            log.error("节点自定义属性设置出错", e);
            throw new DocumentException(e);
        }
    }

    @Override
    public boolean exist(String id) throws DocumentException {
        try {
            return JackrabbitUtil.hasNode(session, id);
        } catch (RepositoryException e) {
            log.warn("文件仓库中未查到节点:" + id);
            return false;
        }
    }

    @Override
    public boolean isValid() throws DocumentException {
        return session.isLive();
    }
}
