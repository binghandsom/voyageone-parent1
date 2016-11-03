package com.voyageone.tools.dao.codegen;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VoDaoGenerator {
    public static void main(String[] arg) throws InterruptedException, SQLException, InvalidConfigurationException, XMLParserException, IOException, URISyntaxException {
        runWith("cmsGeneratorConfig.xml");
//        runWith("vmsGeneratorConfig.xml");
    }

    private static void runWith(String configFileName) throws IOException, XMLParserException, InvalidConfigurationException, SQLException, InterruptedException, URISyntaxException {
        List<String> warnings = new ArrayList<>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        File configurationFile = new File(VoDaoGenerator.class.getResource(configFileName).toURI());
        Configuration config = cp.parseConfiguration(configurationFile);
        DefaultShellCallback shellCallback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
        myBatisGenerator.generate(null);
        warnings.forEach(System.out::println);
    }
}
