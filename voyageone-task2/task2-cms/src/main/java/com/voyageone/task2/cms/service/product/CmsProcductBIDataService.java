package com.voyageone.task2.cms.service.product;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 从bi基础数据表取得产品的bi信息，并保存 (浏览量 访客数 加购件数 收藏人数)
 *
 * @author jiangjusheng on 2016/08/30
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_TASK_AdvSearch_GetBIDataJob)
public class CmsProcductBIDataService extends BaseMQCmsService {

    @Autowired
    ProductService productService;
    @Autowired
    ProductGroupService productGroupService;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        $info("CmsProcductBIDataService start");
        $info("参数" + JacksonUtil.bean2Json(messageMap));
        String channelId = StringUtils.trimToNull((String) messageMap.get("channelId"));
        Integer cartId = (Integer) messageMap.get("cartId");
        if (channelId == null || cartId == null) {
            $error("CmsProcductBIDataService 缺少参数");
            return;
        }




        // 先更新产品platforms价格范围（不更新common中的价格范围）
        JongoUpdate updObj = new JongoUpdate();
        updObj.setQuery("{'prodId':#,'platforms.P#.skus':{$exists:true}}");
//        updObj.setQueryParameters(prodId, cartId);
        updObj.setUpdate("{$set:{'platforms.P#.pPriceMsrpSt':#,'platforms.P#.pPriceMsrpEd':#, 'platforms.P#.pPriceRetailSt':#,'platforms.P#.pPriceRetailEd':#, 'platforms.P#.pPriceSaleSt':#,'platforms.P#.pPriceSaleEd':#, 'modified':#,'modifier':#}}");
//        updObj.setUpdateParameters(cartId, newPriceMsrpSt, cartId, newPriceMsrpEd, cartId, newPriceRetailSt, cartId, newPriceRetailEd, cartId, newPriceSaleSt, cartId, newPriceSaleEd, DateTimeUtil.getNowTimeStamp(), MqRoutingKey.CMS_TASK_ProdcutPriceUpdateJob);
        WriteResult rs = productService.updateFirstProduct(updObj, channelId);
        $debug("CmsProcductPriceUpdateService 产品platforms价格范围更新结果 " + rs.toString());


    }

}
