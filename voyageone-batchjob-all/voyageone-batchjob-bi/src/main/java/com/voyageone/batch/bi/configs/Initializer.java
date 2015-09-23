package com.voyageone.batch.bi.configs;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.batch.bi.spider.service.driver.FireFoxDriverService;
import com.voyageone.batch.configs.FileProperties;

/**
 * Created by Kylin on 2015/7/17.
 */
public class Initializer {

    @Autowired
    private FireFoxDriverService fireFoxDriverService;

    /**
     * 初始化所有配置类的内容
     */
    public void init() throws IOException {

    	// 初始化 KV 文件
    	FileProperties.init();
    	//	初始化 DriverConfigBean
        DriverConfigs.init(fireFoxDriverService);

    }
}
