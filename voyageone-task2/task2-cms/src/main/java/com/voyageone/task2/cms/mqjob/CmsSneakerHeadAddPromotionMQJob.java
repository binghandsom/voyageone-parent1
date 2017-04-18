package com.voyageone.task2.cms.mqjob;

import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.PromotionDetailAddBean;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.AddProductSaveParameter;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.promotion.PromotionCodeService;
import com.voyageone.service.impl.cms.promotion.PromotionDetailService;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsSneakerHeadAddPromotionMQMessageBody;
import com.voyageone.service.model.cms.CmsBtPromotionCodesModel;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by james on 2017/3/28.
 * 根据活动里的商品的找出属于同一个group下的其他商品并加入活动
 */
@Service
@RabbitListener()
public class CmsSneakerHeadAddPromotionMQJob extends TBaseMQCmsService<CmsSneakerHeadAddPromotionMQMessageBody> {

    private final PromotionService promotionService;

    private final PromotionCodeService promotionCodeService;

    private final ProductGroupService productGroupService;

    private final PromotionDetailService promotionDetailService;

    @Autowired
    public CmsSneakerHeadAddPromotionMQJob(PromotionService promotionService, PromotionCodeService promotionCodeService, ProductGroupService productGroupService, PromotionDetailService promotionDetailService) {
        this.promotionService = promotionService;
        this.promotionCodeService = promotionCodeService;
        this.productGroupService = productGroupService;
        this.promotionDetailService = promotionDetailService;
    }

    @Override
    public void onStartup(CmsSneakerHeadAddPromotionMQMessageBody messageBody) throws Exception {

        CmsBtPromotionModel cmsBtPromotionModel = promotionService.getByPromotionId(messageBody.getPromotionId());

        List<CmsBtPromotionCodesModel> cmsBtPromotionCodesModels = promotionCodeService.getPromotionCodeList(messageBody.getPromotionId(), null);

        Set<String> importProductCodes = new HashSet<>();

        if (!ListUtils.isNull(cmsBtPromotionCodesModels)) {
            List<String> codes = cmsBtPromotionCodesModels.stream().map(CmsBtPromotionCodesModel::getProductCode).collect(Collectors.toList());
            for (int i = 0; i < codes.size(); i++) {
                String code = codes.get(i);
                CmsBtProductGroupModel cmsBtProductGroupModel = productGroupService.selectProductGroupByCode(cmsBtPromotionModel.getChannelId(), code, cmsBtPromotionModel.getCartId());
                if (cmsBtProductGroupModel != null) {
                    cmsBtProductGroupModel.getProductCodes().forEach(item -> {
                        if (codes.contains(item)) {
                            if(!code.equalsIgnoreCase(item)) {
                                codes.remove(item);
                            }
                        } else {
                            importProductCodes.add(item);
                        }
                    });
                }
            }
        }
        List<CmsBtOperationLogModel_Msg> msg = new ArrayList<>();
        if (importProductCodes.size() > 0) {
            importProductCodes.forEach(code -> {
                try {
                    $info(code);
                    addPromotionDetail(cmsBtPromotionModel.getChannelId(), cmsBtPromotionModel.getCartId(), code, messageBody.getPromotionId(), messageBody.getSender());
                } catch (Exception e) {
                    CmsBtOperationLogModel_Msg item = new CmsBtOperationLogModel_Msg();
                    item.setSkuCode(code);
                    item.setMsg(Arrays.toString(e.getStackTrace()));
                    msg.add(item);
                    $error(e);
                }
            });
            if(msg.size() > 0){
                cmsSuccessIncludeFailLog(messageBody, "group加入活动失败", msg);
            }
        }


    }

    private void addPromotionDetail(String channelId, Integer cartId, String code, Integer promotionId, String modifier) {
        AddProductSaveParameter addProductSaveParameter = new AddProductSaveParameter();
        addProductSaveParameter.setCartId(cartId);
        addProductSaveParameter.setPriceTypeId(3);
        addProductSaveParameter.setRoundType(4);
        addProductSaveParameter.setSkuUpdType(3);
        addProductSaveParameter.setOptType("=");
        PromotionDetailAddBean request = new PromotionDetailAddBean();
        request.setModifier(modifier);
        request.setChannelId(channelId);
        request.setCartId(cartId);
        request.setProductCode(code);
        request.setPromotionId(promotionId);
        request.setTagId(null);
        request.setTagPath(null);
        request.setAddProductSaveParameter(addProductSaveParameter);
        if (promotionDetailService.check_addPromotionDetail(request)) {
            promotionDetailService.addPromotionDetail(request, true);
        }
    }
}
