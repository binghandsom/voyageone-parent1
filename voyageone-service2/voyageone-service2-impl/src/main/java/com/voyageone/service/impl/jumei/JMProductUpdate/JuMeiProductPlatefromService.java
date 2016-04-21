package com.voyageone.service.impl.jumei.JMProductUpdate;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ExceptionUtil;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.dao.jumei.CmsBtJmPromotionProductDao;
import com.voyageone.service.model.jumei.CmsBtJmPromotionModel;
import com.voyageone.service.model.jumei.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.jumei.businessmodel.EnumJuMeiSynchState;
import com.voyageone.service.model.jumei.businessmodel.EnumJuMeiUpdateState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dell on 2016/4/19.
 */
@Service
public class JuMeiProductPlatefromService {
    @Autowired
    JuMeiProductAddPlatefromService serviceJuMeiProductAddPlatefrom;
    @Autowired
    JuMeiProductUpdatePlatefromService serviceJuMeiProductUpdatePlatefrom;
    @Autowired
    CmsBtJmPromotionProductDao daoCmsBtJmPromotionProduct;
    @Autowired
    JMShopBeanService serviceJMShopBean;
    @Autowired
    JuMeiProductPlatefromDataService service;
    private static final Logger LOG = LoggerFactory.getLogger(JuMeiProductAddPlatefromService.class);

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
        CallResult result=new CallResult();
        try {
            if (model.getState() == 0) {//上新
                 result = serviceJuMeiProductAddPlatefrom.addProductAndDeal(shippingSystemId, model, shopBean);//上新
                if (!result.isResult()) {
                    model.setErrorMsg(result.getMsg());
                    model.setState(EnumJuMeiUpdateState.Error.getId());//同步更新失败
                    daoCmsBtJmPromotionProduct.update(model);
                }
            } else if (model.getSynchState() == 0 || model.getSynchState() == 1) //更新 copyDeal
            {
                serviceJuMeiProductUpdatePlatefrom.updateProductAddDeal(shippingSystemId, model, shopBean);////更新 copyDeal
            } else { //只更新商品
                serviceJuMeiProductUpdatePlatefrom.updateJMProductInfo(shippingSystemId, model, shopBean);
            }
        } catch (Exception ex) {
            model.setErrorMsg(ExceptionUtil.getErrorMsg(ex));
            model.setUpdateState(EnumJuMeiUpdateState.Error.getId());//同步更新失败
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
}
