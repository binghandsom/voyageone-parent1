package com.voyageone.batch.bi.spider.service.driver.impl;

import java.net.URL;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.batch.bi.bean.configsbean.DriverConfigBean;
import com.voyageone.batch.bi.mapper.UserMapper;
import com.voyageone.batch.bi.spider.service.base.CommonSettingService;
import com.voyageone.batch.bi.spider.service.driver.PhantomjsDriverService;
import com.voyageone.batch.bi.util.BiPhantomJSDriver;
import com.voyageone.batch.bi.util.BiRemoteWebDriver;
import com.voyageone.batch.bi.util.FileUtils;
import com.voyageone.batch.bi.util.RedisFactory;
import com.voyageone.batch.configs.FileProperties;

/**
 * Created by Kylin on 2015/7/16.
 */
@Service
public class PhantomjsDriverServiceImpl implements PhantomjsDriverService {

    private static final Log logger = LogFactory.getLog(PhantomjsDriverServiceImpl.class);
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
        	PhantomJSDriver driverGetData = null;
        	try {
	            DesiredCapabilities capability = DesiredCapabilities.firefox();
	            capability.setCapability("bi_sessionId", sessionId);
	            String url = FileProperties.readValue("bi.spider.driver.path");
	            WebDriver loginDriver = new BiRemoteWebDriver(new URL(url), capability);
	
				String phantomjsPath = FileUtils.getRootPath() + FileProperties.readValue("phantomjs_bin");
	
				DesiredCapabilities caps = new DesiredCapabilities();
				caps.setJavascriptEnabled(true);
				caps.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
				caps.setPlatform(org.openqa.selenium.Platform.WINDOWS);
				caps.setVersion("38");
				caps.setCapability("takesScreenshot", true);
				caps.setCapability(
						PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
						phantomjsPath);
				driverGetData = new BiPhantomJSDriver(caps);
	            
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
	            logger.info("PhantomjsDriverServiceImpl openProcessDriver OK");
	            return driverGetData;
        	} catch(Exception e) {
        		if (driverGetData != null) {
        			try {
        				driverGetData.quit();
        			} catch(Exception ex) {}
        		}
        		throw e;
        	}
        } else {
            return null;
        }
    }

}
