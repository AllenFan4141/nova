package com.kdgcsoft.common.util;

import cn.hutool.core.util.StrUtil;

/**
 * @author fyin
 * @date 2022年09月02日 16:33
 */
public class NovaUtil {
    public static boolean isUUID(String uuid) {
        if (StrUtil.isEmpty(uuid)) {
            return false;
        }
        String regex = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
        if (uuid.matches(regex)) {
            return true;
        }
        return false;
    }
}
