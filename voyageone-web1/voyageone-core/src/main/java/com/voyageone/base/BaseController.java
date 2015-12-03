package com.voyageone.base;

import com.voyageone.core.CoreConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.UserSessionBean;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * 通用 Controller 的基类
 * Created on 2015-04-29 15:29:52
 *
 * @author Jonas
 * @version 0.0.4
 */
public abstract class BaseController extends BaseAppComponent {

    @Autowired
    protected HttpServletRequest request;

    /**
     * 小型的辅助类，方便创建基于 Map 模拟的 Bean 对象
     */
    public class JsonObj extends HashMap<String, Object> {
        public JsonObj add(String name, Object value) {
            put(name, value);
            return this;
        }
    }

    /**
     * 从字符串生成下载内容
     */
    protected ResponseEntity<byte[]> genResponseEntityFromString(String downloadFileName, String content) {

        return genResponseEntityFromBytes(downloadFileName, content.getBytes());
    }

    /**
     * 从指定位置的文件，生成下载内容
     */
    protected ResponseEntity<byte[]> genResponseEntityFromFile(String downloadFileName, String srcFile) {
        try {
            return genResponseEntityFromStream(downloadFileName, new FileInputStream(srcFile));
        } catch (FileNotFoundException e) {
            logger.warn("File Not Found for " + srcFile);
        }

        return null;
    }

    /**
     * 从指定的文件流，生成下载内容
     */
    protected ResponseEntity<byte[]> genResponseEntityFromStream(String downloadFileName, InputStream inputStream) {
        try {
            return genResponseEntityFromBytes(downloadFileName, IOUtils.toByteArray(inputStream));
        } catch (IOException e) {
            logger.warn("IOException", e);
        }

        return null;
    }

    /**
     * 生成下载内容
     */
    protected ResponseEntity<byte[]> genResponseEntityFromBytes(String downloadFileName, byte[] bytes) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", downloadFileName);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    /**
     * 统一的 Request 提供
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * 统一的 Session 提供
     */
    public HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 统一的当前登陆用户提供
     */
    public UserSessionBean getUser() {
        return (UserSessionBean) getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
    }

    /**
     * 统一的当前语言环境提供
     */
    public String getLang() {
        return (String) getSession().getAttribute(CoreConstants.VOYAGEONE_USER_LANG);
    }

    /**
     * 获取当前用户的时区，如果没有登录，则返回 0，保持服务器时区
     */
    public int getUserTimeZone() {
        UserSessionBean userSessionBean = getUser();

        return userSessionBean == null ? 0 : userSessionBean.getTimeZone();
    }

    /**
     * 辅助方法，返回一个结果为 true 的 AjaxResponseBean
     */
    public AjaxResponseBean success(Object resultInfo) {
        return AjaxResponseBean.newResult(true, this).setResultInfo(resultInfo);
    }
}