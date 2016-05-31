package com.voyageone.service.impl.cms.jumei2;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ExceptionUtil;
import com.voyageone.components.jumei.JumeiHtDealService;
import com.voyageone.components.jumei.reponse.HtDealCopyDealResponse;
import com.voyageone.components.jumei.request.HtDealCopyDealRequest;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.bean.cms.businessmodel.EnumJuMeiUpdateState;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.daoext.cms.CmsBtJmProductDaoExt;
import com.voyageone.service.impl.cms.jumei.platform.JMShopBeanService;
import com.voyageone.service.impl.cms.jumei.platform.JuMeiProductAddPlatformService;
import com.voyageone.service.impl.cms.jumei.platform.JuMeiProductUpdatePlatformService;
import com.voyageone.service.impl.cms.jumei.platform.JuMeiProductUpdateService;
import com.voyageone.service.model.cms.CmsBtJmProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by dell on 2016/4/19.
 */
@Service
public class JuMeiProductPlatform3Service {
    @Autowired
    CmsBtJmPromotionProductDao daoCmsBtJmPromotionProduct;
    @Autowired
    JMShopBeanService serviceJMShopBean;
    @Autowired
    JuMeiProductUpdateService service;
    @Autowired
    JumeiHtDealService serviceJumeiHtDeal;

    @Autowired
    CmsBtJmProductDaoExt daoExtCmsBtJmProduct;

    private static final Logger LOG = LoggerFactory.getLogger(JuMeiProductAddPlatformService.class);

    public void updateJmByPromotionId(int promotionId) throws Exception {
        CmsBtJmPromotionModel modelCmsBtJmPromotion = service.getCmsBtJmPromotion(promotionId);
        ShopBean shopBean = serviceJMShopBean.getShopBean(modelCmsBtJmPromotion.getChannelId());
        LOG.info(promotionId + " 聚美上新开始");

        List<CmsBtJmPromotionProductModel> listCmsBtJmPromotionProductModel = service.getJuMeiNewListPromotionProduct(promotionId);
        int shippingSystemId = service.getShippingSystemId(modelCmsBtJmPromotion.getChannelId());
        try {
            for (CmsBtJmPromotionProductModel model : listCmsBtJmPromotionProductModel) {
                updateJm(model, shopBean, shippingSystemId);
            }
        } catch (Exception ex) {
            LOG.error("addProductAndDealByPromotionId上新失败", ex);
            ex.printStackTrace();
        }
        LOG.info(promotionId + " 聚美上新end");
    }

    public CallResult updateJm(CmsBtJmPromotionProductModel model, ShopBean shopBean, int shippingSystemId) throws Exception {
        CallResult result = new CallResult();
        try {
            if (model.getSynchStatus() == 1) {
                // 再售
                CmsBtJmProductModel modelJmProduct = daoExtCmsBtJmProduct.getByProductCodeChannelId(model.getProductCode(), model.getChannelId());
                jmHtDealCopy(model, shopBean, modelJmProduct.getOriginJmHashId());
            } else if (model.getPriceStatus() == 1) //更新价格
            {

            } else { //只更新商品

            }
        } catch (Exception ex) {
            model.setErrorMsg(ExceptionUtil.getErrorMsg(ex));
            // model.setUpdateState(EnumJuMeiUpdateState.Error.getId());//同步更新失败
            LOG.error("addProductAndDealByPromotionId", ex);
            try {
                if (model.getErrorMsg().length() > 600) {
                    model.setErrorMsg(model.getErrorMsg().substring(0, 600));
                }
                daoCmsBtJmPromotionProduct.update(model);
            } catch (Exception cex) {
                LOG.error("addProductAndDealByPromotionId", cex);
                ex.printStackTrace();
            }
            result.setResult(false);
            result.setMsg(ExceptionUtil.getErrorMsg(ex));
        }
        return result;
    }

    //再售
    private void jmHtDealCopy(CmsBtJmPromotionProductModel model, ShopBean shopBean, String originJmHashId) throws Exception {
        HtDealCopyDealRequest request = new HtDealCopyDealRequest();
        request.setStart_time(getTime(model.getActivityStart()));
        request.setEnd_time(getTime(model.getActivityEnd()));
        request.setJumei_hash_id(originJmHashId);//原始jmHashId
        HtDealCopyDealResponse response = serviceJumeiHtDeal.copyDeal(shopBean, request);
        if (response.is_Success()) {
            model.setJmHashId(response.getJumei_hash_id());
        } else {
            throw new BusinessException("productId:" + model.getId() + "jmHtDealCopyErrorMsg:" + response.getErrorMsg());
        }
    }
    public static Long getTime(Date d) throws Exception {
        long l = d.getTime() / 1000 - 8 * 3600;
        return l;
    }
}
