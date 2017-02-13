package com.voyageone.service.impl.cms.vomqjobservice;

import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.CmsBtPromotionBean;
import com.voyageone.service.impl.cms.promotion.PromotionCodeService;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.UpdateProductSalePriceMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/2/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsUpdateProductSalePriceServiceTest {
    @Autowired
    PromotionService promotionService;
    @Autowired
    PromotionCodeService promotionCodeService;

    @Test
    public void process1() throws Exception {
        String channelId = "928";
        String cartId="27";
        String code="009-10154-01";
        CmsChannelConfigBean autoSyncPricePromotion = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_PROMOTION, cartId.toString());
        if (autoSyncPricePromotion == null) {
            autoSyncPricePromotion = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_PROMOTION);
        }

        if(autoSyncPricePromotion == null){
            autoSyncPricePromotion = new CmsChannelConfigBean();
            autoSyncPricePromotion.setConfigValue1("2");
            autoSyncPricePromotion.setConfigValue2("15");
            autoSyncPricePromotion.setConfigValue3("15");
        }

        List<CmsBtPromotionBean> promtions = promotionService.getByChannelIdCartId("928", 27);
        if(!ListUtils.isNull(promtions)) {
            List<Integer> promotionIds = promotionService.getDateRangePromotionIds(promtions, new Date(), autoSyncPricePromotion.getConfigValue2(), autoSyncPricePromotion.getConfigValue3());
            if(!ListUtils.isNull(promotionIds)) {
                if (promotionCodeService.getCmsBtPromotionCodeInPromtionCnt(code, promotionIds) >0){
                    System.out.println("true");
                }else{
                    System.out.println("false");
                }

            }
        }
    }

    @Autowired
    CmsUpdateProductSalePriceService cmsUpdateProductSalePriceService;
    @Test
    public void process() throws Exception {
        String json = "{\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"james\",\"productCodes\":[\"10006255\"],\"cartId\":28,\"channelId\":\"928\",\"params\":{\"cartId\":29,\"_option\":\"saleprice\",\"productIds\":[\"10006255\"],\"isSelAll\":0,\"priceType\":\"priceSale\",\"optionType\":\"=\",\"priceValue\":null,\"roundType\":2,\"skuUpdType\":3},\"userId\":9}";
        UpdateProductSalePriceMQMessageBody model = JacksonUtil.json2Bean(json, UpdateProductSalePriceMQMessageBody.class);
        cmsUpdateProductSalePriceService.process(model);
    }

}