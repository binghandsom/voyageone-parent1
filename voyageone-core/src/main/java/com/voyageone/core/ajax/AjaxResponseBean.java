package com.voyageone.core.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.GsonBuilder;
import com.voyageone.base.BaseController;
import com.voyageone.base.exception.SystemException;
import com.voyageone.common.Constants;
import com.voyageone.common.util.StringUtils;

import com.voyageone.core.CoreConstants;
import com.voyageone.core.init.MessageHelp;
import com.voyageone.core.interceptor.CommonHandleInterceptor;

import com.voyageone.core.utilbean.FormValidateBean;

/**
 * 用于返回 Ajax 请求的结果数据
 *
 * @author jacky
 */
public class AjaxResponseBean {

    /**
     * 结果 OK/NG
     */
    private String result = Constants.AJAX_RESULT_NG;

    /**
     * 消息code
     */
    private String messageCode = "";

    /**
     * 消息
     */
    private String message = "";

    /**
     * 类型
     */
    private int messageType;

    /**
     * formbean校验消息
     */
    private List<FormValidateBean> formValidateList;

    /**
     * 数据体信息
     */
    private Object resultInfo;

    /**
     * 页面可访问控件列表
     */
    private Object permissions;

    /**
     * 迁移URL
     */
    private String forward = "";

    /**
     * token
     */
    private String token = "";

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * @return the messageCode
     */
    public String getMessageCode() {
        return messageCode;
    }

    /**
     * @param messageCode the messageCode to set
     */
    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the messageType
     */
    public int getMessageType() {
        return messageType;
    }

    /**
     * @param messageType the messageType to set
     */
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    /**
     * @return the formValidateList
     */
    public List<FormValidateBean> getFormValidateList() {
        return formValidateList;
    }

    /**
     * @param formValidateList the formValidateList to set
     */
    public void setFormValidateList(List<FormValidateBean> formValidateList) {
        this.formValidateList = formValidateList;
    }

    /**
     * @return the resultInfo
     */
    public Object getResultInfo() {
        return resultInfo;
    }

    /**
     * @param resultInfo the resultInfo to set
     */
    public <T extends AjaxResponseBean> T setResultInfo(Object resultInfo) {
        this.resultInfo = resultInfo;
        return (T) this;
    }

    /**
     * @return the permissions
     */
    public Object getPermissions() {
        return permissions;
    }

    /**
     * @param permissions the permissions to set
     */
    public void setPermissions(Object permissions) {
        this.permissions = permissions;
    }

    /**
     * @return the forward
     */
    public String getForward() {
        return forward;
    }

    /**
     * @param forward the forward to set
     */
    public void setForward(String forward) {
        this.forward = forward;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return new GsonBuilder().serializeNulls().create().toJson(this);
    }

    /**
     * 将内容写到Response的输出流中, 默认 UTF-8
     *
     * @param request
     * @param response
     */
    public void writeTo(HttpServletRequest request, HttpServletResponse response) throws SystemException {
        // 设置返回用token
        String token = (String) request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_TOKEN);
        if (token != null) {
            String url = request.getServletPath();
            if (!CommonHandleInterceptor.EXCLUDE_URL_LIST_TOKEN.contains(url)) {
                setToken(token);
            }
        }

        writeTo(response, "UTF-8");
    }

    /**
     * 将内容写到Response的输出流中
     */
    public void writeTo(HttpServletResponse response, String encoding) throws SystemException {
        if (!StringUtils.isEmpty(encoding)) {
            response.setCharacterEncoding(encoding);
        }

        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(toString());
        } catch (IOException e) {
            throw new SystemException(this.getClass() + "'s writeTo() has IOException.", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 帮助方法，用于设置reqResult的通用结果
     *
     * @param result 返回的具体结果
     */
    @SuppressWarnings("unchecked")
    public <T extends AjaxResponseBean> T setResult(boolean result) {
        // 结果
        setResult(result ? Constants.AJAX_RESULT_OK : Constants.AJAX_RESULT_NG);

        return (T) this;
    }

    /**
     * 帮助方法，用于设置reqResult的通用结果
     *
     * @param result  返回的具体结果
     * @param msgCode 返回的信息代码
     * @param msgType 附带的信息类型
     */
    @SuppressWarnings("unchecked")
    public <T extends AjaxResponseBean> T setResult(boolean result, String msgCode, int msgType) {
        // 结果
        setResult(result ? Constants.AJAX_RESULT_OK : Constants.AJAX_RESULT_NG);
        // 消息代码
        setMessageCode(msgCode);
        // 附带信息
        String msg = MessageHelp.getMessage(msgType, msgCode);
        if (!StringUtils.isEmpty(msg)) {
            setMessage(msg);
        }
        // 附带的信息类型
        setMessageType(msgType);

        return (T) this;
    }

    /**
     * 帮助方法，用于设置reqResult的通用结果
     *
     * @param result  返回的具体结果
     * @param msgType 附带的信息类型
     * @param msg     返回的信息
     */
    @SuppressWarnings("unchecked")
    public <T extends AjaxResponseBean> T setResult(boolean result, int msgType, String msg) {
        // 结果
        setResult(result ? Constants.AJAX_RESULT_OK : Constants.AJAX_RESULT_NG);
        // 附带信息
        if (!StringUtils.isEmpty(msg)) {
            setMessage(msg);
        }
        // 附带的信息类型
        setMessageType(msgType);

        return (T) this;
    }

    /**
     * 自动创建一个新的 ResponseBean。
     * <p><b>注意，这个辅助方法，只适用于纯粹的 AjaxResponseBean。继承实现它的子类请不要使用这个方法</b></p>
     * <p><b>通过该方法创建的 Bean 请通过 WriteTo 返回给前台</b></p>
     *
     * @param result {boolean}
     * @return {AjaxResponseBean}
     */
    public static AjaxResponseBean newResult(boolean result) {
        return new AjaxResponseBean().setResult(result);
    }

    /**
     * 自动创建一个新的 ResponseBean
     * <p><b>注意，这个辅助方法，只适用于纯粹的 AjaxResponseBean。继承实现它的子类请不要使用这个方法</b></p>
     * <p><b>通过该方法创建的 Bean，不需要通过 WriteTo 返回</b></p>
     *
     * @param result  {boolean}
     * @param request {HttpServletRequest}
     * @return {AjaxResponseBean}
     */
    public static AjaxResponseBean newResult(boolean result, HttpServletRequest request) {

        AjaxResponseBean bean = new AjaxResponseBean().setResult(result);

        String url = request.getServletPath();

        // 如果是排除的路径，则不需要处理 token
        if (CommonHandleInterceptor.EXCLUDE_URL_LIST_TOKEN.contains(url)) return bean;

        // 尝试获取 token
        String token = (String) request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_TOKEN);

        // 没有 token 的话，直接跳过
        if (StringUtils.isEmpty(token)) return bean;

        bean.setToken(token);

        return bean;
    }

    /**
     * 自动创建一个新的 ResponseBean
     * <p><b>注意，这个辅助方法，只适用于纯粹的 AjaxResponseBean。继承实现它的子类请不要使用这个方法</b></p>
     * <p><b>通过该方法创建的 Bean，不需要通过 WriteTo 返回</b></p>
     */
    public static AjaxResponseBean newResult(boolean result, BaseController controller) {

        return newResult(result, controller.getRequest());
    }
}
