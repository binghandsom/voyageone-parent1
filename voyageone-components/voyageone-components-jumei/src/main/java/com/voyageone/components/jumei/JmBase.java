package com.voyageone.components.jumei;

import com.google.gson.JsonSyntaxException;
import com.taobao.top.schema.Util.StringUtil;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.MD5;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.ComponentBase;
import com.voyageone.components.jumei.bean.JMErrorResult;
import com.voyageone.components.jumei.bean.NotSignString;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by sn3 on 2015-07-16.
 */
public class JmBase extends ComponentBase {

    public static final int C_MAX_API_REPEAT_TIME = 3;

    protected String reqJmApi(ShopBean shopBean, String api_url) throws Exception {
        return reqJmApi(shopBean, api_url, new HashMap<>());
    }

    private String replaceSpicialChart(String org) {
//        String result = null;
//        if (org != null) {
//            result = org.replaceAll("&", "、");
//            result = result.replaceAll("\\?", "？");
//            result = result.replaceAll("\\+", "＋");
//        }
//        return result;
        return org;
    }

    protected String reqJmApi(ShopBean shopBean, String api_url, Map<String, Object> params) throws Exception {

        for (Object value : params.values()) {
            if (value != null) {
                if (!(value instanceof String) && !(value instanceof NotSignString)) {
                    throw new Exception("String or NotSignString type is only support!");
                }
            }
        }

        Map<String, Object> paramsTmp = new HashMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            Object valueObj = entry.getValue();
            Object newValueObj;
            if (valueObj instanceof NotSignString) {
                newValueObj = new NotSignString(replaceSpicialChart(((NotSignString)valueObj).content));
            } else {
                newValueObj = replaceSpicialChart((String)valueObj);
            }
            paramsTmp.put(entry.getKey(), newValueObj);
        }

        StringBuilder post_url = new StringBuilder();

        String call_url = shopBean.getApp_url() + api_url;
        post_url.append(call_url);
        post_url.append("?");



        //设置系统级参数
        paramsTmp.put("client_id", shopBean.getAppKey());
        paramsTmp.put("client_key", shopBean.getSessionKey());
        //生成签名
        String sign = getSignRequest(shopBean, paramsTmp);
        paramsTmp.put("sign", sign);

        StringBuilder parm_url = new StringBuilder();
        //拼接URL
        for (Map.Entry<String, Object> entry : paramsTmp.entrySet()) {
            if(!StringUtils.isEmpty(entry.getKey())){
                parm_url.append("&").append(entry.getKey()).append("=");
            }
            Object value = entry.getValue();
            if (value instanceof String) {
                if(!StringUtils.isEmpty((String)value)){
                    parm_url.append(URLEncoder.encode((String) value, "UTF-8"));
//                    parm_url.append(value);
                }
            } else if (value instanceof NotSignString) {
                NotSignString notSignString = (NotSignString)value;
                if(!StringUtils.isEmpty(notSignString.content)){
                    parm_url.append(notSignString.content);
                }
            }
        }
        if (parm_url.length() != 0){
            parm_url.delete(0,1);
        }


        String result = HttpUtils.post(post_url.toString(), parm_url.toString());
        logger.info("result：" + result);

        //转换错误信息
        if (result != null && result.contains("\"error\"")) {
            Map<String, Object> resultMap = JsonUtil.jsonToMap(result);
            if (resultMap.containsKey("error") && !"0".equals(resultMap.get("error"))) {
                throw new BusinessException("调用聚美API错误：" + result+post_url+parm_url);
            }
        } else {
            JMErrorResult res;
            try {
                res = JsonUtil.jsonToBean(result, JMErrorResult.class);
                if (res.getCode() != null) {
                    throw new BusinessException("调用聚美API错误：" +  result+post_url+parm_url);
                }
            } catch (JsonSyntaxException ignored) {
            }
        }


        return result;
    }

    protected String reqOnTimeoutRepert(String post_url, String parm_url) throws Exception {
        for (int intApiErrorCount = 0; intApiErrorCount < C_MAX_API_REPEAT_TIME; intApiErrorCount++) {
            try {
                return  HttpUtils.post(post_url, parm_url);
            } catch (Exception e) {
                logger.info("time out :"+ intApiErrorCount+1);
                // 最后一次出错则直接抛出
                Thread.sleep(1000);
                if (C_MAX_API_REPEAT_TIME - intApiErrorCount == 1) throw e;
            }
        }
        return "";
    }

    /**
     * @param shopBean ShopBean
     * @param params Map<String, Object>
     * @return 加密好的签名
     */
    public String getSignRequest(ShopBean shopBean,Map<String, Object> params){
        Map<String, Object> sortedParams = new TreeMap<>();
        //1.先按照参数的字母顺序排序
        sortedParams.putAll(params);

        StringBuilder query = new StringBuilder();
        //把client_sign 夹在字符串的两端
        if (!StringUtil.isEmpty(shopBean.getAppSecret())) {
            query.append(shopBean.getAppSecret());
        }
        //2.把所有参数名和参数值串在一起
        for (Map.Entry<String, Object> param : sortedParams.entrySet()) {
            if (param.getValue() != null && param.getValue() instanceof String) {
                if (!StringUtils.isEmpty(param.getKey())) {
                    query.append(param.getKey());
                }
                if (!StringUtils.isEmpty((String)param.getValue())) {
                    query.append(param.getValue());
                }
            }
        }
        //把client_sign（假设是 abc） 夹在字符串的两端
        if (!StringUtil.isEmpty(shopBean.getAppSecret())) {
            query.append(shopBean.getAppSecret());
        }
        //使用MD5 进行加密，再转化成大写
        //logger.info("md5:"+query.toString());
        return MD5.getMD5(query.toString()).toUpperCase();
    }
}
