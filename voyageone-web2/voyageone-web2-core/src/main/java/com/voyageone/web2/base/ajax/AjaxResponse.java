package com.voyageone.web2.base.ajax;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 用于返回 Ajax 请求的结果数据
 * @author jonas
 */
public class AjaxResponse {
    /**
     * 消息code
     */
    private String code = "";

    /**
     * 消息
     */
    private String message = "";

    /**
     * 数据体信息
     */
    private AjaxResponseData result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AjaxResponseData getResult() {
        return result;
    }

    public void setResult(AjaxResponseData result) {
        this.result = result;
    }

    public void writeTo(HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.print(new Gson().toJson(this));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
