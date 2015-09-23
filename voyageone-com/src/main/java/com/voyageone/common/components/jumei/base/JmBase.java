package com.voyageone.common.components.jumei.base;

import com.taobao.top.schema.Util.StringUtil;
import com.voyageone.common.components.jumei.Bean.JMErrorResult;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.MD5;
import com.voyageone.common.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by sn3 on 2015-07-16.
 */
public class JmBase {

    protected Log logger = LogFactory.getLog(getClass());

    protected String reqJmApi(ShopBean shopBean, String api_url, Map<String, String> params) throws Exception {

        StringBuilder post_url = new StringBuilder();

        String call_url = shopBean.getApp_url() + api_url;
        post_url.append(call_url);
        post_url.append("?");



        //设置系统级参数
        Map<String, String> tempParm = params;
        tempParm.put("client_id",shopBean.getAppKey());
        tempParm.put("client_key", shopBean.getSessionKey());
        //生成签名
        String sign = getSignRequest(shopBean,params);
        tempParm.put("sign",sign);

        StringBuilder parm_url = new StringBuilder();
        //拼接URL
        for (String key : tempParm.keySet()) {
            if(!StringUtils.isEmpty(key)){
                parm_url.append("&"  + key + "=");
            }
            if(!StringUtils.isEmpty(tempParm.get(key))){
                parm_url.append(tempParm.get(key));
            }
        }
        if (parm_url.length() != 0){
            parm_url.delete(0,1);
        }

        String result = HttpUtils.post(post_url.toString(), parm_url.toString());
        //转换错误信息
        JMErrorResult res = JsonUtil.jsonToBean(result, JMErrorResult.class);
        if (res.getCode() != null){
            throw new Exception("调用聚美API错误：" + result);
        }

        return result;
    }


    /**
     * @param shopBean
     * @param params
     * @return 加密好的签名
     */
    public String getSignRequest(ShopBean shopBean,Map<String, String> params){
        Map<String, String> sortedParams = new TreeMap<String,String>();
        sortedParams.putAll(params);
        //1.先按照参数的字母顺序排序
        Set<Map.Entry<String, String>> paramSet =sortedParams.entrySet();
        StringBuilder query = new StringBuilder();
        //把client_sign 夹在字符串的两端
        if (!StringUtil.isEmpty(shopBean.getAppSecret())) {
            query.append(shopBean.getAppSecret());
        }
        //2.把所有参数名和参数值串在一起
        for (Map.Entry<String, String> param : paramSet) {
            if (!StringUtils.isEmpty(param.getKey())) {
                query.append(param.getKey());
            }
            if (!StringUtils.isEmpty(param.getKey())) {
                query.append(param.getValue());
            }
        }
        //把client_sign（假设是 abc） 夹在字符串的两端
        if (!StringUtil.isEmpty(shopBean.getAppSecret())) {
            query.append(shopBean.getAppSecret());
        }
        //使用MD5 进行加密，再转化成大写
        String encryptStr = MD5.getMD5(query.toString()).toUpperCase();
        return encryptStr;
    }
}
