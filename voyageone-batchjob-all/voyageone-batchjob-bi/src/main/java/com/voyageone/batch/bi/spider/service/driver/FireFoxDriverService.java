package com.voyageone.batch.bi.spider.service.driver;

import com.voyageone.batch.bi.bean.configsbean.DriverConfigBean;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * Created by Kylin on 2015/7/16.
 */
public interface FireFoxDriverService {

    List<DriverConfigBean> initialLoginFireFoxDriverAll() throws Exception;

    void initialRemoteLoginFireFoxDriver(DriverConfigBean driverConfigBean) throws Exception;

    void initialLocalLoginFireFoxDriver(DriverConfigBean driverConfigBean) throws Exception;

    void reflashFireFoxDriver(DriverConfigBean driverBean);

    WebDriver openProcessFireFoxDriver(DriverConfigBean driverConfigBean) throws Exception;

    WebDriver openProcessRemoteFireFoxDriver() throws Exception;

}
