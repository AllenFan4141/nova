package com.kdgcsoft.document.interfaces;

import com.kdgcsoft.document.exception.DocumentException;
import com.kdgcsoft.document.modal.DocumentInfo;

import javax.jcr.Node;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文档操作的公共接口
 *
 * @author fyin
 * @date 2021-05-19 14:27
 */
public interface DocumentSession {
    /**
     * session是否支持全文检索
     *
     * @return
     * @throws DocumentException
     */
    boolean supportFullTextSearch() throws DocumentException;

    /**
     * 向指定路径下放置文件
     *
     * @param path 文件放置路径
     * @param file 文件
     * @return
     * @throws DocumentException
     */
    DocumentInfo putFile(String path, File file) throws DocumentException;

    /**
     * 向指定路径下以指定的文件名放置文件
     *
     * @param path     路径
     * @param file     文件
     * @param fileName 指定文件名称
     * @return
     * @throws DocumentException
     */
    DocumentInfo putFile(String path, File file, String fileName) throws DocumentException;

    /**
     * 以流的方式向指定路径下放置文件
     *
     * @param path     文件路径
     * @param is       流
     * @param fileName 指定的文件名
     * @return
     * @throws DocumentException
     */
    DocumentInfo putFile(String path, InputStream is, String fileName) throws DocumentException;

    /**
     * 在根节点下创建路径
     *
     * @param path
     * @return
     * @throws DocumentException
     */
    DocumentInfo createDir(String path) throws DocumentException;

    /**
     * 在指定节点下创建路径
     *
     * @param parentNode
     * @param path
     * @return
     * @throws DocumentException
     */
    DocumentInfo createDir(Node parentNode, String path) throws DocumentException;

    /**
     * 删除路径
     *
     * @param path
     * @return
     * @throws DocumentException
     */
    boolean removeDir(String path) throws DocumentException;

    /**
     * 获得文件的基本信息
     *
     * @param id
     * @return
     * @throws DocumentException
     */
    DocumentInfo getFileInfo(String id) throws DocumentException;

    /**
     * 读取文件
     *
     * @param id
     * @return
     * @throws DocumentException
     */
    InputStream readFile(String id) throws DocumentException;

    /**
     * 指定文件是否存在
     *
     * @param id
     * @return
     * @throws DocumentException
     */
    boolean exist(String id) throws DocumentException;

    /**
     * 将指定文件ID写到输出流中
     *
     * @param id
     * @param outputStream
     * @throws DocumentException
     */
    void readFile(String id, OutputStream outputStream) throws DocumentException;

    /**
     * 当前session是否可用
     *
     * @return
     * @throws DocumentException
     */
    boolean isValid() throws DocumentException;
}
