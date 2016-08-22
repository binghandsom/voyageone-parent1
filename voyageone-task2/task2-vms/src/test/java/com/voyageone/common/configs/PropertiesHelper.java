package com.voyageone.common.configs;

/**
 * 为单元测试提供修改 Properties 内容的能力
 * Created by jonasvlag on 16/7/14.
 *
 * @version 2.3.0
 * @since 2.3.0
 */
public class PropertiesHelper {
    public static void putValue(String key, String value) {
        Properties.putValue(key, value);
    }
}
