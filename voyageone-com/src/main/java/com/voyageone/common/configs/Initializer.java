package com.voyageone.common.configs;

import java.io.IOException;

/**
 * 访问
 * Created by Jonas on 4/16/2015.
 */
public class Initializer {

    /**
     * 初始化所有配置类的内容
     */
    public void init() throws IOException {
        // 初始化 KV 文件
        Properties.init();
    }
}
