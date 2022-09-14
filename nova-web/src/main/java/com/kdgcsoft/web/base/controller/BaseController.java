package com.kdgcsoft.web.base.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.kdgcsoft.common.util.MimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.kdgcsoft.web.common.consts.WebConst.*;

/**
 * @author fyin
 * @date 2022年08月30日 10:44
 */
public class BaseController {
    @Autowired
    public HttpServletRequest request;
    @Autowired
    public HttpServletResponse response;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 设置List的最大长度
        binder.setAutoGrowCollectionLimit(10000);
    }

    /**
     * 构建文件下载的header
     *
     * @param fileName
     * @return
     */
    public HttpHeaders buildDownloadHeader(String fileName) {
        try {
            String header = request.getHeader("User-Agent")
                    .toUpperCase();
            /**
             * IE低版本会有MSIE,IE11高版本没有可以通过TRIDENT判断
             */
            if (header.contains(HEADER_AGENT_MSIE) || header.contains(HEADER_AGENT_TRIDENT) || header.contains(HEADER_AGENT_EDGE)) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
                //IE下载文件名空格变+号问题
                fileName = fileName.replaceAll("\\+", "%20");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();

        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        /**
         * 文件名加上双引号避免firefox文件名带空格会截断
         */
        headers.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return headers;
    }

    public ResponseEntity<FileSystemResource> renderFile(File file) {
        if (file == null) {
            return null;
        }
        return ResponseEntity.ok()
                .headers(buildDownloadHeader(file.getName()))
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new FileSystemResource(file));
    }

    public ResponseEntity<InputStreamResource> renderFile(InputStream inputStream, String fileName, long size) {
        if (inputStream == null) {
            return null;
        }
        return ResponseEntity.ok()
                .headers(buildDownloadHeader(fileName))
                .contentLength(size)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(inputStream));
    }

    public ResponseEntity<FileSystemResource> renderFile(File file, String fileType) {
        if (file == null) {
            return null;
        }
        return ResponseEntity.ok()
                .headers(buildDownloadHeader(file.getName()))
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType(fileType))
                .body(new FileSystemResource(file));
    }

    public ResponseEntity<ByteArrayResource> renderFile(byte[] bytes, String fileType, String fileName) {
        if (bytes.length < 1) {
            return null;
        }
        return ResponseEntity.ok()
                .headers(buildDownloadHeader(fileName))
                .contentLength(bytes.length)
                .contentType(StrUtil.isNotEmpty(fileType) ? MediaType.parseMediaType(fileType) : MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(bytes));
    }

    public ResponseEntity<ByteArrayResource> renderFile(byte[] bytes, String fileName) {
        if (bytes.length < 1) {
            return null;
        }
        return ResponseEntity.ok()
                .headers(buildDownloadHeader(fileName))
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(bytes));
    }

    public void renderImage(byte[] bytes, String imageName) {
        String contentType = MimeUtil.getMimeType(imageName);
        response.setContentType(contentType);
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            os.write(bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ModelAndView view(String viewname) {
        return view(viewname, null);
    }

    public ModelAndView view(String viewname, Map<String, Object> modelMap) {
        ModelAndView view = new ModelAndView();
        view.setViewName(viewname);
        view.addAllObjects(modelMap);
        return view;
    }

    public ModelAndView view(ModelAndView view, Map<String, Object> modelMap) {
        view.addAllObjects(modelMap);
        return view;
    }

    public Map<String, Object> getParamsMap() {
        Map<String, Object> params = new HashMap<>();
        Map<String, String[]> map = request.getParameterMap();
        for (String key : map.keySet()) {
            String[] values = map.get(key);
            params.put(key, ArrayUtil.join(values, ","));
        }
        return params;
    }

}
