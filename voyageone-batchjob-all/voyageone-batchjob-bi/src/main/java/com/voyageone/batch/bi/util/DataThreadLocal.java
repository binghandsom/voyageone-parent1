package com.voyageone.batch.bi.util;


import com.voyageone.batch.bi.bean.formbean.FormUser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.SessionId;

/**
 * Created by Kylin on 2015/6/10.
 */
public class DataThreadLocal {

    private static final ThreadLocal<FormUser> userLocal  = new ThreadLocal<FormUser>();
    private static final ThreadLocal<WebDriver> webDriverLocal  = new ThreadLocal<WebDriver>();
    private static final ThreadLocal<SessionId> sessionIdThreadLocal  = new ThreadLocal<SessionId>();

    public static void addUser(FormUser user) {
        userLocal.remove();
        userLocal.set(user);
    }

    public static FormUser getUser() {
        return userLocal.get();
    }

    public static void addWebDriver(WebDriver webDriver) {
        webDriverLocal.remove();
        webDriverLocal.set(webDriver);
    }

    public static WebDriver getWebDriver() {
        return webDriverLocal.get();
    }

    public static void addSessionId(SessionId sessionId) {
        sessionIdThreadLocal.remove();
        sessionIdThreadLocal.set(sessionId);
    }

    public static SessionId getSessionId() {
        return sessionIdThreadLocal.get();
    }

}
