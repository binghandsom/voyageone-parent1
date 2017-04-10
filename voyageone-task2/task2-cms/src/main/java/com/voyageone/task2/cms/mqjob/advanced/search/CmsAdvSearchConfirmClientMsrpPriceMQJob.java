package com.voyageone.task2.cms.mqjob.advanced.search;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.prices.PlatformPriceService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchConfirmClientMsrpPriceMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchConfirmRetailPriceMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 高级检索-确认客户成本价Job
 *
 * @Author dell
 * @Create 2016-12-30 17:18
 */
@Service
@RabbitListener()
public class CmsAdvSearchConfirmClientMsrpPriceMQJob extends TBaseMQCmsService<AdvSearchConfirmClientMsrpPriceMQMessageBody> {

    @Autowired
    private ProductService productService;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Override
    public void onStartup(AdvSearchConfirmClientMsrpPriceMQMessageBody messageBody) throws Exception {
        BulkJongoUpdateList bulkList = new BulkJongoUpdateList(100, cmsBtProductDao, messageBody.getChannelId());

        for(String code:messageBody.getCodeList()) {
            CmsBtProductModel cmsBtProduct = productService.getProductByCode(messageBody.getChannelId(), code);
            $info("code: " + code);
            if(cmsBtProduct != null && !ListUtils.isNull(cmsBtProduct.getCommonNotNull().getSkus())) {
                boolean updFlg = false;
                for(CmsBtProductModel_Sku sku :cmsBtProduct.getCommonNotNull().getSkus()) {
                    if(Double.compare(sku.getClientMsrpPrice(), sku.getConfClientMsrpPrice()) != 0 || !"0".equals(sku.getClientMsrpPriceChgFlg())){
                        sku.setConfClientMsrpPrice(sku.getClientMsrpPrice());
                        sku.setClientMsrpPriceChgFlg("0");
                        updFlg = true;
                    }
                }
                if(updFlg) {
                    JongoUpdate updObj = new JongoUpdate();
                    updObj.setQuery("{'common.fields.code':#}");
                    updObj.setQueryParameters(code);
                    updObj.setUpdate("{$set:{'common.skus' :#,}}");
                    updObj.setUpdateParameters(cmsBtProduct.getCommonNotNull().getSkus());
                    BulkWriteResult rs = bulkList.addBulkJongo(updObj);
                    if (rs != null) {
                        $info(String.format("确认客户成本价批量确认 channelId=%s 执行结果=%s", messageBody.getChannelId(), rs.toString()));
                    }
                }
            }
        }
        BulkWriteResult rs = bulkList.execute();
        if (rs != null) {
            $info(String.format("确认客户成本价批量确认 channelId=%s 执行结果=%s", messageBody.getChannelId(), rs.toString()));
        }
    }

    public void confirmClientMsrpPrice(String channelId, String code) {
        CmsBtProductModel cmsBtProduct = productService.getProductByCode(channelId, code);
        cmsBtProduct.getCommonNotNull().getSkus().forEach(sku -> {
            sku.setConfClientMsrpPrice(sku.getClientMsrpPrice());
            sku.setClientMsrpPriceChgFlg("0");
        });
    }
}
