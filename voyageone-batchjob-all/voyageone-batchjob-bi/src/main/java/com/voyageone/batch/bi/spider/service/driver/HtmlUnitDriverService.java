package com.voyageone.batch.bi.spider.service.driver;

import org.openqa.selenium.WebDriver;

import com.voyageone.batch.bi.bean.configsbean.DriverConfigBean;

/**
 * Created by Kylin on 2015/7/16.
 */
public interface HtmlUnitDriverService {
//
//    List<DriverConfigBean> initialLoginFireFoxDriverAll() throws Exception;
//
//    void initialLoginFireFoxDriver(DriverConfigBean driverConfigBean) throws Exception;
//
//    void reflashFireFoxDriver(String reflashUrl, WebDriver driver);

    WebDriver openProcessDriver(DriverConfigBean driverConfigBean) throws Exception;

}
