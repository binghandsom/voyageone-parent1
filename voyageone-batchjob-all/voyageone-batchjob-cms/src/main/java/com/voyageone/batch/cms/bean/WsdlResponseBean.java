package com.voyageone.batch.cms.bean;

import com.voyageone.base.exception.SystemException;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;


import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于返回 webservice请求的结果数据
 * Created by zero on 7/28/2015.
 *
 * @author zero
 *
 */
public class WsdlResponseBean {

    /**
     * 结果 OK/NG
     */
    private String result = "NG";

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
     * 数据体信息
     */
    private Object resultInfo;

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
     * @return the resultInfo
     */
    public Object getResultInfo() {
        return resultInfo;
    }

    /**
     * @param resultInfo the resultInfo to set
     */
    public void setResultInfo(Object resultInfo) {
        this.resultInfo = resultInfo;
    }
//
//    @Override
//    public String toString() {
//        return JsonUtil.getJsonString(this);
//    }
//
//    /**
//     * 将内容写到Response的输出流中, 默认 UTF-8
//     *
//     * @param request
//     * @param response
//     */
//    public void writeTo(HttpServletResponse response) throws SystemException {
//        writeTo(response, "UTF-8");
//    }
//
//    /**
//     * 将内容写到Response的输出流中
//     */
//    public void writeTo(HttpServletResponse response, String encoding) throws SystemException {
//        if (!StringUtils.isEmpty(encoding)) {
//            response.setCharacterEncoding(encoding);
//        }
//
//        PrintWriter out = null;
//        try {
//            out = response.getWriter();
//            out.print(toString());
//        } catch (IOException e) {
//            throw new SystemException(this.getClass() + "'s writeTo() has IOException.", e);
//        } finally {
//            if (out != null) {
//                out.close();
//            }
//        }
//    }
//
//    /**
//     * 帮助方法，用于设置reqResult的通用结果
//     * @param result
//     * 		返回的具体结果
//     * @param msgCode
//     * 		返回的信息代码
//     * @param msgType
//     * 		附带的信息类型
//     */
//    @SuppressWarnings("unchecked")
//    public <T extends WsdlResponseBean> T setResult(String result, String msgCode, int msgType) {
//        // 结果
//        setResult(result);
//        // 消息代码
//        setMessageCode(msgCode);
//        // 附带信息
//        String msg = MessageHelp.getMessage(msgType, msgCode);
//        if (!StringUtils.isEmpty(msg)) {
//            setMessage(msg);
//        }
//        // 附带的信息类型
//        setMessageType(msgType);
//
//        return (T) this;
//    }
//
//    /**
//     * 帮助方法，用于设置reqResult的通用结果
//     * @param result
//     * 		返回的具体结果
//     * @param msgType
//     * 		附带的信息类型
//     * @param msg
//     * 		返回的信息
//     */
//    @SuppressWarnings("unchecked")
//    public <T extends WsdlResponseBean> T setResult(String result, int msgType, String msg) {
//        // 结果
//        setResult(result);
//        // 附带信息
//        if (!StringUtils.isEmpty(msg)) {
//            setMessage(msg);
//        }
//        // 附带的信息类型
//        setMessageType(msgType);
//
//        return (T) this;
//    }

}
