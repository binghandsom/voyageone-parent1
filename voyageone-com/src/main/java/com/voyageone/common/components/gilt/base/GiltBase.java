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
        //设置系统级参数
        Map<String, String> tempParm = params;
        tempParm.put("API_TOKEN",shopBean.getAppKey());

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

        String result = HttpUtils.get(post_url.toString(), parm_url.toString());
        //转换错误信息
        GiltErrorResult res = JsonUtil.jsonToBean(result, GiltErrorResult.class);
        if (res.getType() != null){
            throw new Exception("调用Gilt API错误：" + result);
        }

        return result;
    }



}
