package com.voyageone.task2.cms.mqjob.usa;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsBtProductUpdateListDelistStatusMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by dell on 2017/7/18.
 */
@Service
@RabbitListener()
public class CmsBtProductUpdateListDelistStatusMQJob extends TBaseMQCmsService<CmsBtProductUpdateListDelistStatusMQMessageBody> {
    @Autowired
    ProductService productService;
    @Autowired
    PlatformProductUploadService platformProductUploadService;

    @Autowired
    CmsBtProductDao cmsBtProductDao;

    @Override
    public void onStartup(CmsBtProductUpdateListDelistStatusMQMessageBody messageBody) throws Exception {
        $info("接收到上下架消息体,messageBody:" + JacksonUtil.bean2Json(messageBody));
        if (messageBody != null) {
            Integer cartId = messageBody.getCartId();
            String channelId = messageBody.getChannelId();
            String activeStatus = messageBody.getActiveStatus();
            List<String> productCodes = messageBody.getProductCodes();
            Integer days = messageBody.getDays();
            if (ListUtils.notNull(productCodes)) {
                for (String productCode : productCodes) {
                    //获取到商品对象
                    CmsBtProductModel cmsBtProductModel = productService.getProductByCode(channelId, productCode);
                    Map<String, CmsBtProductModel_Platform_Cart> usPlatforms = cmsBtProductModel.getUsPlatforms();
                    if (usPlatforms != null) {
                        CmsBtProductModel_Platform_Cart usPlatform = usPlatforms.get("P" + cartId);
                        if (usPlatform != null){
                            String status = usPlatform.getStatus();
                            //status为Approve状态才可以进行上下架
                            if (CmsConstants.ProductStatus.Approved.name().equals(status)) {

                                JongoUpdate jongoUpdate = new JongoUpdate();
                                jongoUpdate.setQuery("{\"common.fields.code\":#}");
                                jongoUpdate.setQueryParameters(productCode);
                                jongoUpdate.setUpdate("{$set:{\"usPlatforms.P" + cartId + ".pStatus\":#}}");
                                if ("list".equals(activeStatus)) {
                                    //上架操作
                                    jongoUpdate.setUpdateParameters(CmsConstants.PlatformStatus.OnSale.name());
                                }
                                if ("deList".equals(activeStatus)) {
                                    //下架操作
                                    jongoUpdate.setUpdateParameters(CmsConstants.PlatformStatus.InStock.name());
                                }
                                BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkUpdateWithJongo(channelId, Collections.singletonList(jongoUpdate));
                                $info("执行上下架操作,channelId: " + channelId + " productCode:" + productCode + " bulkWriteResult:" + bulkWriteResult);

                                //设置滞后发布日期
                                if (days != 0){
                                    Date date = new Date();
                                    Calendar calendar = new GregorianCalendar();
                                    calendar.setTime(date);
                                    calendar.add(calendar.DATE, days);
                                    date = calendar.getTime();
                                    platformProductUploadService.saveCmsBtUsWorkloadModel(channelId, cartId, productCode, date, 0, messageBody.getSender());
                                }else {
                                    platformProductUploadService.saveCmsBtUsWorkloadModel(channelId, cartId, productCode, null, 0, messageBody.getSender());
                                }
                            }
                        }
                    }
                }
            }

        }

    }
}
