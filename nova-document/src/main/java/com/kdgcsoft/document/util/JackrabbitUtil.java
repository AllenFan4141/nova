package com.kdgcsoft.document.util;

import com.kdgcsoft.common.util.MimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.jackrabbit.commons.JcrUtils;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fyin
 * @date 2021-05-19 15:43
 */
@Slf4j
public class JackrabbitUtil {
    private static final String CLEAN_PATTERN_REGEX ="[`~!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？|\\s*|\t|\r|\n]";

    /**
     * 清除文件名中可能引发问题的字符串
     *
     * @param str
     * @return
     */
    public static String cleanFileName(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile(CLEAN_PATTERN_REGEX);
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static Node putFile(Session session, Node dirNode, InputStream is, String nodeName) throws RepositoryException {
        Node fileNode = JcrUtils.putFile(dirNode, nodeName, MimeUtil.getMimeType(nodeName), is);
        session.save();
        return fileNode;
    }

    public static Node getNodeById(Session session, String nodeId) throws RepositoryException {
        return session.getNodeByIdentifier(nodeId);
    }

    public static boolean hasNode(Session session, String nodeId) throws RepositoryException {
        return session.getNodeByIdentifier(nodeId) != null;
    }

    public static InputStream readFile(Node node) throws RepositoryException {
        return JcrUtils.readFile(node);
    }

    public static void readFile(Node node, OutputStream outputStream) throws IOException, RepositoryException {
        JcrUtils.readFile(node, outputStream);
    }
}
