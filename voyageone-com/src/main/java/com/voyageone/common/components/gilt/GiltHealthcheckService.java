package com.voyageone.common.components.gilt;

import com.voyageone.common.components.gilt.base.GiltBase;
import com.voyageone.common.components.gilt.bean.GiltHealthcheck;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JacksonUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author aooer 2016/2/3.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class GiltHealthcheckService extends GiltBase {

    private static final String URL = "healthchecks";

    /**
     *  Api健康检查 ping
     *  If the status returned is "pong" then the service is up and running.
     * @param shopBean shopBean
     * @return GiltHealthcheck
     * @throws Exception
     */
    public GiltHealthcheck ping(ShopBean shopBean) throws Exception {
        return JacksonUtil.json2Bean(reqGiltApi(shopBean,URL+"/ping",new HashMap<String,String>()),GiltHealthcheck.class);
    }

    /**
     *  Api健康检查 status
     *  If the status returned is "ok" then the service is healthy.
     * @param shopBean shopBean
     * @return GiltHealthcheck
     * @throws Exception
     */
    public GiltHealthcheck status(ShopBean shopBean) throws Exception {
        return JacksonUtil.json2Bean(reqGiltApi(shopBean,URL+"/status",new HashMap<String,String>()),GiltHealthcheck.class);
    }
}
