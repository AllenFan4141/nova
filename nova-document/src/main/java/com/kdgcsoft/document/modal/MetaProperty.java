package com.kdgcsoft.document.modal;

/**
 * @author fyin
 * @date 2021-05-21 15:52
 */
public class MetaProperty {
    private static final String METADATA_NAMESPACE = "meta:";
    /**
     * meta中的文件名
     */
    public static final String META_FILE_NAME = METADATA_NAMESPACE + "fileName";
    /**
     * meta中的文件大小
     */
    public static final String META_FILE_SIZE = METADATA_NAMESPACE + "fileSize";
    /**
     * meta中的文件扩展名
     */
    public static final String META_FILE_EXT = METADATA_NAMESPACE + "fileExt";
    /**
     * meta中的创建人
     */
    public static final String META_CREATE_BY = METADATA_NAMESPACE + "createBy";
    /**
     * meta中的创建时间
     */
    public static final String META_CREATE_TIME = METADATA_NAMESPACE + "createTime";
    /**
     * meta中的最后修改人
     */
    public static final String META_MODIFY_BY = METADATA_NAMESPACE + "modifyBy";
    /**
     * meta中的最后修改时间
     */
    public static final String META_MODIFY_TIME = METADATA_NAMESPACE + "modifyTime";
}
