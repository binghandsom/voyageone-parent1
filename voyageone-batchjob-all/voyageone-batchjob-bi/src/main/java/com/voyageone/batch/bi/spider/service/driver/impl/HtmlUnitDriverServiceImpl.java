package com.voyageone.batch.bi.spider.service.driver.impl;

import java.net.URL;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.voyageone.batch.bi.bean.configsbean.DriverConfigBean;
import com.voyageone.batch.bi.mapper.UserMapper;
import com.voyageone.batch.bi.spider.service.base.CommonSettingService;
import com.voyageone.batch.bi.spider.service.driver.HtmlUnitDriverService;
import com.voyageone.batch.bi.util.BiRemoteWebDriver;
import com.voyageone.batch.bi.util.RedisFactory;
import com.voyageone.batch.configs.FileProperties;

/**
 * Created by Kylin on 2015/7/16.
 */
@Service
public class HtmlUnitDriverServiceImpl implements HtmlUnitDriverService {

    //private static final Log logger = LogFactory.getLog(HtmlUnitDriverServiceImpl.class);
    @Autowired
    private CommonSettingService commonSettingService;

    @Autowired
    private UserMapper userMapper;

    private String getSessionId(int shopId) {
        String key = "logined_sessionid_shopid=" + shopId;
        return RedisFactory.get(key);
    }


    @Override
    public WebDriver openProcessDriver(DriverConfigBean driverConfigBean) throws Exception {
        String sessionId = getSessionId(driverConfigBean.getShopBean().getShop_id());

        if (sessionId != null) {
            DesiredCapabilities capability = DesiredCapabilities.firefox();
            capability.setCapability("bi_sessionId", sessionId);
            String url = FileProperties.readValue("bi.spider.driver.path");
            WebDriver loginDriver = new BiRemoteWebDriver(new URL(url), capability);

            HtmlUnitDriver driverGetData = new HtmlUnitDriver(BrowserVersion.FIREFOX_38);
            driverGetData.setJavascriptEnabled(true);
            
            driverGetData.get(driverConfigBean.getShopBean().getReflash_url());
            driverGetData.manage().deleteAllCookies();
            Set<Cookie> allCookies = loginDriver.manage().getCookies();
            for (Cookie cookie : allCookies) {
                Cookie newCookie = new Cookie(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath(), cookie.getExpiry());
                try {
                    driverGetData.manage().addCookie(newCookie);
                } catch (Exception ex) {
                }
            }
            driverGetData.get(driverConfigBean.getShopBean().getReflash_url());
            return driverGetData;
        } else {
            return null;
        }
    }

}
