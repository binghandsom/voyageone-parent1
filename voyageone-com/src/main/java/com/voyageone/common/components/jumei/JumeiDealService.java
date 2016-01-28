package com.voyageone.common.components.jumei;

import com.voyageone.common.components.jumei.Bean.*;
import com.voyageone.common.components.jumei.base.JmBase;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JacksonUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 聚美特卖(Deal)服务
 * @author aooer 2016/1/28.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class JumeiDealService extends JmBase{

    /* api地址 */
    private static final String GET_DEAL_BY_ID = "v1/htProduct/getDealByHashID";

    /**
     *  根据商品ID获取特卖(Deal)详情;
     * @param shopBean 系统级参数
     * @param jmDealBean 应用级参数
     * @return 商品信息
     */
    public Deal_Info getDealByHashID(ShopBean shopBean,JmDealBean jmDealBean) throws Exception {
        //check
        Assert.notNull(shopBean,"shopBean must not null!");
        jmDealBean.check();

        Map<String,Object> params=new HashMap<>();
        params.put("product_id",jmDealBean.getProductId());
        params.put("fields",jmDealBean.getFields());

        String reqResult = reqJmApi(shopBean, GET_DEAL_BY_ID, params);
        Deal_Info dealInfo=JacksonUtil.json2Bean(reqResult,Deal_Info.class);
        if(dealInfo==null){
            logger.warn("dealInfo is null");
        }
        return dealInfo;
    }

}
