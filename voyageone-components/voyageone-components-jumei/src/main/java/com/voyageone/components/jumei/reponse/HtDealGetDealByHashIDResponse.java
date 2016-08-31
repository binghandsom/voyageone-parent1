package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.common.util.JacksonUtil;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * HtDealCopyDealResponse
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtDealGetDealByHashIDResponse extends BaseJMResponse {
    // {
//        "error": {
//        "code": "500"
//         }
// }
    private String error_code;
    private boolean is_Success;
    private String errorMsg;
    private String body;
    String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean is_Success() {
        return is_Success;
    }

    public void setIs_Success(boolean is_Success) {
        this.is_Success = is_Success;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getBody() {
        return body;
    }
    // 北京时间  开始时间
    Date start_time;

    //北京时间  结束时间
    Date end_time;

    String product_id;
    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }



    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setBody(String body) throws IOException {
        this.body = body;
        try {
            Map<String, Object> map = JacksonUtil.jsonToMap(body);
            if (map.containsKey("error")) {
                if (map.get("error") instanceof Map) {
                    Map<String, Object> mapError = (Map<String, Object>) map.get("error");
                    this.setError_code(mapError.get("code").toString());
                } else {
                    this.setError_code("-1");
                }
            } else {
                if (map.containsKey("start_time")) {
                    long startTime = Long.parseLong(map.get("start_time").toString());
                    //startTime*1000;
                    this.setStart_time(DateTimeUtilBeijing.toBeiJingDate(new Date(startTime * 1000)));
                }
                if (map.containsKey("end_time")) {
                    long endTime = Long.parseLong(map.get("end_time").toString());
                    this.setEnd_time(DateTimeUtilBeijing.toBeiJingDate(new Date(endTime * 1000)));
                }
                if (map.containsKey("product_id")) {
                    this.setProduct_id(map.get("product_id").toString());
                }
            }
            if ("".equals(this.error_code)) {
                this.setIs_Success(true);
            } else {
                this.setErrorMsg(this.getRequestUrl()+this.body);
            }
        } catch (Exception ex) {
            logger.error("setBody ",ex);
            this.setIs_Success(false);
            this.setErrorMsg("返回参数解析错误" + this.body);
        }
    }
}
