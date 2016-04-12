package com.voyageone.components.jumei.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.jumei.bean.JmGetDealInfoReq;
import com.voyageone.components.jumei.bean.JmGetDealInfoRes;
import com.voyageone.components.jumei.JmBase;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 聚美特卖(Deal)服务
 * @author aooer 2016/1/28.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class JumeiDealService extends JmBase {

    /* api地址 */
    private static final String GET_DEAL_BY_ID = "v1/htProduct/getDealByHashID";

    /**
     *  根据商品ID获取特卖(Deal)详情;
     * @param shopBean 系统级参数
     * @param getDealInfoReq 应用级参数
     * @return 商品信息
     */
    public JmGetDealInfoRes getDealByHashID(ShopBean shopBean, JmGetDealInfoReq getDealInfoReq) throws Exception {
        //check
        if(shopBean==null){
            throw new IllegalArgumentException("shopBean must not null!");
        }
        //Assert.notNull(shopBean,"shopBean must not null!");
        getDealInfoReq.check();

        Map<String,Object> params=new HashMap<>();
        params.put("product_id", getDealInfoReq.getProductId());
        params.put("fields", getDealInfoReq.getFields());

        String reqResult = reqJmApi(shopBean, GET_DEAL_BY_ID, params);
        if(reqResult==null){
            logger.warn("reqResult is null");
        }
        return JacksonUtil.json2Bean(reqResult,JmGetDealInfoRes.class);
    }

}
