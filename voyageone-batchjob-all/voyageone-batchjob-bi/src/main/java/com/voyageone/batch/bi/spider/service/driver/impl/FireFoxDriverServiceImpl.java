package com.voyageone.batch.bi.spider.service.driver.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.batch.bi.bean.configsbean.DriverConfigBean;
import com.voyageone.batch.bi.bean.modelbean.ShopChannelEcommBean;
import com.voyageone.batch.bi.mapper.UserMapper;
import com.voyageone.batch.bi.spider.constants.data.DataSearchConstants;
import com.voyageone.batch.bi.spider.service.base.CommonSettingService;
import com.voyageone.batch.bi.spider.service.driver.FireFoxDriverService;
import com.voyageone.batch.bi.util.BiRemoteWebDriver;
import com.voyageone.batch.bi.util.RedisFactory;
import com.voyageone.batch.configs.FileProperties;

/**
 * Created by Kylin on 2015/7/16.
 */
@Service
public class FireFoxDriverServiceImpl implements FireFoxDriverService {

    private static final Log logger = LogFactory.getLog(FireFoxDriverServiceImpl.class);
    @Autowired
    private CommonSettingService commonSettingService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<DriverConfigBean> initialLoginFireFoxDriverAll() {
        List<DriverConfigBean> result = new ArrayList<>();
        List<ShopChannelEcommBean> shops = commonSettingService.initShopConfigBeanList();
        for (ShopChannelEcommBean shop : shops) {
            DriverConfigBean driverConfigBean = new DriverConfigBean();
            driverConfigBean.setShopBean(shop);
            result.add(driverConfigBean);
        }
        return result;
    }

    @Override
    public void initialRemoteLoginFireFoxDriver(DriverConfigBean driverConfigBean) throws Exception {
    	openInitialLoginLocalFireFoxDriver(driverConfigBean, false);
    }

    @Override
    public void initialLocalLoginFireFoxDriver(DriverConfigBean driverConfigBean) throws Exception {
    	openInitialLoginLocalFireFoxDriver(driverConfigBean, true);
    }
    
    private void openInitialLoginLocalFireFoxDriver(DriverConfigBean driverConfigBean, boolean isLocal) throws Exception {
    	WebDriver driver = driverConfigBean.getInitial_driver();
    	if (driver == null) {
    		DesiredCapabilities capability = DesiredCapabilities.firefox();

            capability.setBrowserName("firefox");
            capability.setPlatform(Platform.WINDOWS);
            capability.setVersion("3.8");

            FirefoxProfile firefoxProfile = new FirefoxProfile();
            firefoxProfile.setPreference("network.cookie.cookieBehavior", 0);
            capability.setCapability(FirefoxDriver.PROFILE, firefoxProfile);

            if (isLocal) {
                driver = new FirefoxDriver(capability);
            } else {
                String url = FileProperties.readValue("bi.spider.driver.path");
                driver = new RemoteWebDriver(new URL(url), capability);
            }
            
            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
            
            driverConfigBean.setInitial_driver(driver);
    	}

        switch (driverConfigBean.getShopBean().getEcomm_id()) {
            case DataSearchConstants.ECOMM_TM:
                driver = goLoginTM(driverConfigBean);
                break;

            case DataSearchConstants.ECOMM_TB:
                driver = goLoginTM(driverConfigBean);
                break;

            case DataSearchConstants.ECOMM_OF:
                break;

            case DataSearchConstants.ECOMM_TG:
                driver = goLoginTM(driverConfigBean);
                break;

            case DataSearchConstants.ECOMM_JD:
                driver = goLoginJD(driverConfigBean);
                break;

            case DataSearchConstants.ECOMM_CN:
                break;

            case DataSearchConstants.ECOMM_JG:
                driver = goLoginJD(driverConfigBean);
                break;

            case DataSearchConstants.ECOMM_JM:
                driver = goLoginJM(driverConfigBean);
                break;                
        }

        driverConfigBean.setInitial_driver(driver);
        if (driver != null) {
            setSessionId(driverConfigBean);
        } else {
            driverConfigBean.setSession_id(null);
        }
    }
    
    private void setSessionId(DriverConfigBean driverConfigBean) {
        RemoteWebDriver remoteWebDriver = (RemoteWebDriver) driverConfigBean.getInitial_driver();
        SessionId sessionId = remoteWebDriver.getSessionId();
        driverConfigBean.setSession_id(sessionId);
        String key = "logined_sessionid_shopid=" + driverConfigBean.getShopBean().getShop_id();
        RedisFactory.set(key, sessionId.toString());
        
    }

    private String getSessionId(int shopId) {
        String key = "logined_sessionid_shopid=" + shopId;
        return RedisFactory.get(key);
    }

    @Override
    public void reflashFireFoxDriver(DriverConfigBean driverBean) {
        while (true) {
        	WebDriver driver = driverBean.getInitial_driver();
        	if (driver == null) {
        		break;
        	}
        	boolean isLogin = false;
            try {
                String strReflashUrl = driverBean.getShopBean().getReflash_url();
                driver.get(strReflashUrl); 
//                String curUrlTmp = driver.getCurrentUrl().replace("http://", "").replace("https://", "").toLowerCase();
//                if (curUrlTmp.indexOf("login")>=0 || curUrlTmp.indexOf("err")>=0) {
//                    isLogin = false;
//                } else {
                isLogin = true;
//                }
//            } catch (WebDriverException wex) {
//            	logger.error("reflashFireFoxDriver Error:", wex);
//            	driverBean.setInitial_driver(null);
            } catch (Exception ex) {
                logger.error("reflashFireFoxDriver Error:", ex);
                //break;
            }
            try {
	            if (isLogin) {
	            	int number = (int) ((double)(1 + Math.random()) * 5.0);
	            	Thread.sleep(60000 * number);
	            } else {
	            	Thread.sleep(10000 * 1);
	            	initialRemoteLoginFireFoxDriver(driverBean);
                    Thread.sleep(10000 * 3);
	            }
            } catch (InterruptedException e) {
                logger.error("reflashFireFoxDriver Interrupte Error:", e);
            } catch (Exception ex) {
                logger.error("reflashFireFoxDriver initialLoginFireFoxDriver Error:", ex);
                break;
            }
        }
    }

    @Override
    public WebDriver openProcessFireFoxDriver(DriverConfigBean driverConfigBean) throws Exception {

        String sessionId = getSessionId(driverConfigBean.getShopBean().getShop_id());

        if (sessionId != null) {

            DesiredCapabilities capability = DesiredCapabilities.firefox();
            capability.setCapability("bi_sessionId", sessionId);

            String url = FileProperties.readValue("bi.spider.driver.path");
            WebDriver driver = new BiRemoteWebDriver(new URL(url), capability);

            capability = DesiredCapabilities.firefox();
            WebDriver driverGetData = new RemoteWebDriver(new URL(url), capability);
            driverGetData.get(driverConfigBean.getShopBean().getReflash_url());
            driverGetData.manage().deleteAllCookies();
            Set<Cookie> allCookies = driver.manage().getCookies();
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

    public WebDriver openProcessRemoteFireFoxDriver() throws Exception {
        DesiredCapabilities capability = DesiredCapabilities.firefox();
        capability.setBrowserName("firefox");
        capability.setPlatform(Platform.WINDOWS);
        capability.setVersion("3.8");

        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setPreference("network.cookie.cookieBehavior", 0);
        capability.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
        String url = FileProperties.readValue("bi.spider.driver.path");
        WebDriver driver = new RemoteWebDriver(new URL(url), capability);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        return driver;
    }
    /**
     * 进行登录
     *
     * @param driverConfigBean
     * @throws Exception
     */
    private WebDriver goLoginTM(DriverConfigBean driverConfigBean) throws Exception {

        WebDriver driver = driverConfigBean.getInitial_driver();

        driver.get(driverConfigBean.getShopBean().getLogin_url());
        int sheepLoginCnt = 0;
        while (driver.getCurrentUrl().indexOf("login.jhtml") >= 0) {
            String loginTitle = driver.getTitle();
            driver.findElement(By.xpath("//div/label[@for=\"TPL_username_1\"]"));
            driver.findElement(By.id("TPL_username_1")).sendKeys("");
            Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
            String user = driverConfigBean.getShopBean().getUser_name();
            for (int i = 0; i < user.length(); i++) {
                String key = user.substring(i, i + 1);
                driver.findElement(By.id("TPL_username_1")).sendKeys(key);
                Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
            }

            driver.findElement(By.id("TPL_password_1")).sendKeys("");
            Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
            String password = driverConfigBean.getShopBean().getUser_ps();
            for (int i = 0; i < password.length(); i++) {
                String key = password.substring(i, i + 1);
                driver.findElement(By.id("TPL_password_1")).sendKeys(key);
                Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
            }

            driver.findElement(By.id("J_SubmitStatic")).click();

            boolean logined = false;
            for (int i = 0; i < 10; i++) {
                if (!loginTitle.equals(driver.getTitle())) {
                    logined = true;
                    break;
                }
                Thread.sleep(DataSearchConstants.THREAD_SLEEP_LOGIN);
            }

            if (logined) {
                return driver;
            } else {
                driver.get("about:blank");
                driver.get(driverConfigBean.getShopBean().getLogin_url());
            }
            Thread.sleep(DataSearchConstants.THREAD_SLEEP_LOGIN);

            sheepLoginCnt++;

            if (sheepLoginCnt > 2) {
                if (checkVerificationTM(driver)) {
                    //检查登录是否完成。
                    //new Exception("渠道：" + driverConfigBean.getOrder_channel_code() + ";店铺：" + driverConfigBean.getShop_code() + ";" + "\u9a8c\u8bc1\u7801\u4e0d\u80fd\u4e3a\u7a7a");
                }
                break;
            }
        }

        return driver;
    }

    /**
     * JD进行登录
     *
     * @param driverConfigBean
     * @throws Exception
     */
    private static WebDriver goLoginJD(DriverConfigBean driverConfigBean) throws Exception {
        WebDriver driver = driverConfigBean.getInitial_driver();
        driver.get(driverConfigBean.getShopBean().getLogin_url());
        int sheepLoginCnt = 0;
        while (driver.getCurrentUrl().indexOf("login.aspx") >= 0) {
            String loginTitle = driver.getTitle();
            driver.findElement(By.xpath("//div/label[@for=\"loginname\"]"));
            driver.findElement(By.id("loginname")).sendKeys("");
            Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
            String user = driverConfigBean.getShopBean().getUser_name();
            for (int i = 0; i < user.length(); i++) {
                String key = user.substring(i, i + 1);
                driver.findElement(By.id("loginname")).sendKeys(key);
                Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
            }

            driver.findElement(By.id("nloginpwd")).sendKeys("");
            Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
            String password = driverConfigBean.getShopBean().getUser_ps();
            for (int i = 0; i < password.length(); i++) {
                String key = password.substring(i, i + 1);
                driver.findElement(By.id("nloginpwd")).sendKeys(key);
                Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
            }

            driver.findElement(By.id("loginsubmit")).click();

            boolean logined = false;
            for (int i = 0; i < 10; i++) {
                if (!loginTitle.equals(driver.getTitle())) {
                    logined = true;
                    break;
                }
                Thread.sleep(DataSearchConstants.THREAD_SLEEP_LOGIN);
            }

            if (logined) {
                return driver;
            } else {
                driver.get("about:blank");
                driver.get(driverConfigBean.getShopBean().getLogin_url());
            }


            Thread.sleep(DataSearchConstants.THREAD_SLEEP_LOGIN);

            sheepLoginCnt++;

            if (sheepLoginCnt > 2) {
                if (checkVerificationJD(driver)) {
                    //检查登录是否完成。
                    throw new Exception("渠道：" + driverConfigBean.getShopBean().getOrder_channel_code() +
                            ";店铺：" + driverConfigBean.getShopBean().getShop_code() +
                            ";" + "\u9a8c\u8bc1\u7801\u4e0d\u80fd\u4e3a\u7a7a");
                }
                break;
            }
        }

        return null;
    }
    
    
    /**
     * 进行登录
     *
     * @param driverConfigBean
     * @throws Exception
     */
    private static WebDriver goLoginJM(DriverConfigBean driverConfigBean) throws Exception {
        WebDriver driver = driverConfigBean.getInitial_driver();
        driver.get(driverConfigBean.getShopBean().getLogin_url());
        int sheepLoginCnt = 0;
        while (driver.getCurrentUrl().indexOf("login") >= 0) {
            String loginTitle = driver.getTitle();
            driver.findElement(By.xpath("//div[@class=\"login-content\"]"));
            WebElement loginName = driver.findElement(By.id("userName"));
            loginName.sendKeys("");
            Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
            String user = driverConfigBean.getShopBean().getUser_name();
            for (int i = 0; i < user.length(); i++) {
                String key = user.substring(i, i + 1);
                loginName.sendKeys(key);
                Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
            }
            
            WebElement loginPwd = driver.findElement(By.id("password"));
            loginPwd.sendKeys("");
            Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
            String password = driverConfigBean.getShopBean().getUser_ps();
            for (int i = 0; i < password.length(); i++) {
                String key = password.substring(i, i + 1);
                loginPwd.sendKeys(key);
                Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
            }

            driver.findElement(By.id("submit-btn")).click();

            boolean logined = false;
            for (int i = 0; i < 10; i++) {
                Thread.sleep(DataSearchConstants.THREAD_SLEEP_LOGIN);
                if (!loginTitle.equals(driver.getTitle())) {
                    logined = true;
                    break;
                }
                Thread.sleep(DataSearchConstants.THREAD_SLEEP_LOGIN);
            }

            if (logined) {
                return driver;
            } else {
                driver.get("about:blank");
                driver.get(driverConfigBean.getShopBean().getLogin_url());
            }


            Thread.sleep(DataSearchConstants.THREAD_SLEEP_LOGIN);

            sheepLoginCnt++;

            if (sheepLoginCnt > 2) {
                if (checkVerificationJD(driver)) {
                    //检查登录是否完成。
                    throw new Exception("渠道：" + driverConfigBean.getShopBean().getOrder_channel_code() +
                            ";店铺：" + driverConfigBean.getShopBean().getShop_code() +
                            ";" + "\u9a8c\u8bc1\u7801\u4e0d\u80fd\u4e3a\u7a7a");
                }
                break;
            }
        }

        return null;
    }

    private static boolean checkVerificationTM(WebDriver driver) {
        if (driver.findElement(By.name("TPL_username")) != null) {
            return true;
        }
        return false;
    }

    private static boolean checkVerificationJD(WebDriver driver) {
        if (driver.findElement(By.name("loginname")) != null) {
            return true;
        }
        return false;
    }
}
