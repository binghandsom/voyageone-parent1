package com.voyageone.task2.cms.mqjob;

import com.voyageone.common.util.ListUtils;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.promotion.PromotionCodeService;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsSneakerHeadAddPromotionMQMessageBody;
import com.voyageone.service.model.cms.CmsBtPromotionCodesModel;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by james on 2017/3/28.
 *
 */
@Service
@RabbitListener()
public class CmsSneakerHeadAddPromotionMQJob extends TBaseMQCmsService<CmsSneakerHeadAddPromotionMQMessageBody> {

    @Autowired
    PromotionService promotionService;

    @Autowired
    PromotionCodeService promotionCodeService;

    @Autowired
    ProductGroupService productGroupService;


    @Override
    public void onStartup(CmsSneakerHeadAddPromotionMQMessageBody messageBody) throws Exception {

        CmsBtPromotionModel cmsBtPromotionModel = promotionService.getByPromotionId(messageBody.getPromotionId());

        List<CmsBtPromotionCodesModel> cmsBtPromotionCodesModels = promotionCodeService.getPromotionCodeList(messageBody.getPromotionId(),null);

        if(!ListUtils.isNull(cmsBtPromotionCodesModels)) {
            List<String> codes = cmsBtPromotionCodesModels.stream().map(CmsBtPromotionCodesModel::getProductCode).collect(Collectors.toList());
            productGroupService.selectMainProductGroupByCode();
        }
    }
}
