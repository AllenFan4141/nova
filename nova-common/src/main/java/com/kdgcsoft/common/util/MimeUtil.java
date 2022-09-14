package com.kdgcsoft.common.util;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class MimeUtil {

    private static Log logger = LogFactory.get();

    private static Properties propFile = new Properties();

    static {
        InputStream in = null;
        try {

            in = MimeUtil.class.getResourceAsStream("/mimetype.properties");
            propFile.load(in);

        } catch (FileNotFoundException e) {
            logger.error("找不到mime定义文件mimetype.properties，将无法判断文件mime！");
        } catch (IOException e) {
            logger.error("读取mimetype.properties失败，将无法判断文件mime！");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("关闭文件失败", e);
            }
        }
    }

    private MimeUtil() {
        //工具类不需要new
    }

    /**
     * 根据文件扩展名获取mimeType
     *
     * @param filename 文件名。必须包含".扩展名"的结构。如找不到匹配的扩展名，则返回application/octet-stream。
     * @return Mime名称
     */
    public static String getMimeType(String filename) {
        String extension = FileNameUtil.extName(filename);
        if (extension == null || "".equals(extension)) {
            return "application/octet-stream";
        }

        String type = propFile.getProperty(extension);
        if (type == null || "".equals(type)) {
            type = "application/octet-stream";
        } else {
            // 存在多个时，只返回第一个
            type = type.split(",")[0].trim();
        }

        return type;
    }

    /**
     * 获取mineType
     *
     * @param fileName
     * @return
     */
/*	private static String getMimeType2(String filename) {
		String fileType = FilenameUtils.getExtension(filename);
		MimeTable mt = MimeTable.getDefaultTable();
		String mimeType = mt.getContentTypeFor(filename);
		if (mimeType == null) {
			mimeType = getMimeType(fileType);
		}

		if (fileType == null || "".equals(fileType)) {
			mimeType = "application/octet-stream";
		} else {
			if ("doc".equalsIgnoreCase(fileType)) {
				mimeType = "application/msword";
			} else if ("docx".equalsIgnoreCase(fileType)) {
				mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
			} else if ("xls".equalsIgnoreCase(fileType)) {
				mimeType = "application/vnd.ms-excel";
			} else if ("xlsx".equalsIgnoreCase(fileType)) {
				mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
			} else if ("ppt".equalsIgnoreCase(fileType)) {
				mimeType = "application/vnd.ms-powerpoint";
			} else if ("pptx".equalsIgnoreCase(fileType)) {
				mimeType = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
			} else if ("pdf".equalsIgnoreCase(fileType)) {
				mimeType = "application/pdf";
			} else {
				mimeType = "application/octet-stream";
			}
		}
		return mimeType;
	}
*/
}
