package com.voyageone.components.gilt.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.gilt.GiltBase;
import com.voyageone.components.gilt.bean.GiltHealthcheck;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author aooer 2016/2/3.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class GiltHealthcheckService extends GiltBase {

    private static final String URI = "healthchecks";


    /**
     * @api {get} /healthchecks/ping Api健康检测Ping
     * @apiVersion 0.0.1
     * @apiGroup public1
     * @apiPermission 认证商家
     * @apiDescription Api健康检查 如果ping通知返回pong的状态，则服务启动并正常运行
     * @apiSuccess  {String} status 服务运行正常返回 "pong".
     */
    public GiltHealthcheck ping() throws Exception {
        return JacksonUtil.json2Bean(reqGiltApi(URI +"/ping",new HashMap<String,String>()),GiltHealthcheck.class);
    }

    /**
     * @api {get} /healthchecks/status Api健康检测Status
     * @apiVersion 0.0.1
     * @apiGroup public
     * @apiPermission 认证商家
     * @apiDescription Api健康检查 如果返回ok的状态，则服务正常运行
     * @apiSuccess  {String} status 服务运行正常返回 "ok".
     */
    public GiltHealthcheck status() throws Exception {
        return JacksonUtil.json2Bean(reqGiltApi(URI +"/status",new HashMap<String,String>()),GiltHealthcheck.class);
    }
}
