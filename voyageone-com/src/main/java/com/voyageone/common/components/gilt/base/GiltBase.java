package com.voyageone.common.components.gilt.base;

import com.taobao.top.schema.Util.StringUtil;
import com.voyageone.common.components.gilt.bean.GiltErrorResult;
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
 * GiltBase
 * @author aooer 2016/2/1.
 * @version 2.0.0
 * @since 2.0.0.
 *   查看JmBase com.voyageone.common.components.jumei.base.JmBase
 */
public abstract class GiltBase {

    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * 发送请求Api
     * @param shopBean 系统级参数
     * @param api_url api路径
     * @param params 应用参数
     * @return Json String
     * @throws Exception
     */
    protected String reqGiltApi(ShopBean shopBean, String api_url, Map<String, String> params) throws Exception {
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
        GiltErrorResult res = JsonUtil.jsonToBean(result, GiltErrorResult.class);
        if (res.getCode() != null){
            throw new Exception("调用Gilt API错误：" + result);
        }

        return result;
    }


    /**
     * @param shopBean 系统级参数
     * @param params 应用级参数
     * @return 加密好的签名
     */
    private String getSignRequest(ShopBean shopBean,Map<String, String> params){
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
