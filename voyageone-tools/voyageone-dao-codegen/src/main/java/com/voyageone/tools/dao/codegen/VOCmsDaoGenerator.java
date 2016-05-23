package com.voyageone.tools.dao.codegen;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2016/5/5.
 */
public class VOCmsDaoGenerator {
    public static void main(String[] arg) throws Exception {
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        File configurationFile = new File(VOCmsDaoGenerator.class.getResource("cmsGeneratorConfig.xml").toURI());
//
//
//        System.out.println(configurationFile.exists());

        Configuration config = cp.parseConfiguration(configurationFile);

        DefaultShellCallback shellCallback = new DefaultShellCallback(true);

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);

        myBatisGenerator.generate(null);

        System.out.println(warnings);

    }
}
