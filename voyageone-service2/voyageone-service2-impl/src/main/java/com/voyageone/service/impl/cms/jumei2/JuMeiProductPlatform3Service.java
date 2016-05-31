package com.voyageone.service.impl.cms.jumei2;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ExceptionUtil;
import com.voyageone.components.jumei.JumeiHtDealService;
import com.voyageone.components.jumei.bean.HtDealUpdate_DealInfo;
import com.voyageone.components.jumei.bean.HtDeal_UpdateDealPriceBatch_UpdateData;
import com.voyageone.components.jumei.reponse.HtDealCopyDealResponse;
import com.voyageone.components.jumei.reponse.HtDealUpdateDealPriceBatchResponse;
import com.voyageone.components.jumei.request.HtDealCopyDealRequest;
import com.voyageone.components.jumei.request.HtDealUpdateDealPriceBatchRequest;
import com.voyageone.components.jumei.request.HtDealUpdateRequest;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.bean.cms.businessmodel.EnumJuMeiUpdateState;
import com.voyageone.service.bean.cms.jumei2.SkuPriceBean;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.daoext.cms.CmsBtJmProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionSkuDaoExt;
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
import java.util.ArrayList;
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
    @Autowired
    CmsBtJmPromotionSkuDaoExt daoExtCmsBtJmPromotionSku;
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
                List<SkuPriceBean> listSkuPrice = daoExtCmsBtJmPromotionSku.getJmSkuPriceInfoListByPromotionProductId(modelJmProduct.getId());
                String jmSkuNoList = getjmSkuNo(listSkuPrice);
                jmHtDealCopy(model, shopBean, modelJmProduct.getOriginJmHashId());//再售
                jmHtDealUpdate(model, shopBean, jmSkuNoList);//更新deal信息   limit   jmSkuNo
                jmHtDealUpdateDealPriceBatch(model, shopBean, listSkuPrice);//更新价格
            }
            else if (model.getPriceStatus() == 1) //更新价格
            {
                CmsBtJmProductModel modelJmProduct = daoExtCmsBtJmProduct.getByProductCodeChannelId(model.getProductCode(), model.getChannelId());
                List<SkuPriceBean> listSkuPrice = daoExtCmsBtJmPromotionSku.getJmSkuPriceInfoListByPromotionProductId(modelJmProduct.getId());
                jmHtDealUpdateDealPriceBatch(model, shopBean, listSkuPrice);
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

    private String getjmSkuNo(List<SkuPriceBean> listSkuPrice) {
        String jmSkuNoList = "";
        for (SkuPriceBean skuPriceBean : listSkuPrice) {
            jmSkuNoList += skuPriceBean.getJmSkuNo() + ",";
        }
        if (jmSkuNoList.length() > 0) {
            jmSkuNoList = jmSkuNoList.substring(0, jmSkuNoList.length() - 1);
        }
        return jmSkuNoList;
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
    //更新特卖信息
    private void jmHtDealUpdate(CmsBtJmPromotionProductModel model, ShopBean shopBean, String jumei_sku_no) throws Exception {
        HtDealUpdateRequest request = new HtDealUpdateRequest();
        request.setJumei_hash_id(model.getJmHashId());
        HtDealUpdate_DealInfo update_dealInfo = new HtDealUpdate_DealInfo();
        update_dealInfo.setUser_purchase_limit(model.getLimit()); //限购数量
        update_dealInfo.setJumei_sku_no(jumei_sku_no);           //jmskuno
        request.setUpdate_data(update_dealInfo);
        serviceJumeiHtDeal.update(shopBean, request);
    }
    private void  jmHtDealUpdateDealPriceBatch(CmsBtJmPromotionProductModel model, ShopBean shopBean, List<SkuPriceBean> listSkuPrice) throws Exception {
        HtDealUpdateDealPriceBatchRequest request = new HtDealUpdateDealPriceBatchRequest();
        HtDeal_UpdateDealPriceBatch_UpdateData updateData = null;
        List<HtDeal_UpdateDealPriceBatch_UpdateData> list = new ArrayList<>();
        for (SkuPriceBean skuPriceBean : listSkuPrice) {
            updateData = new HtDeal_UpdateDealPriceBatch_UpdateData();
            list.add(updateData);
            updateData.setJumei_hash_id(model.getJmHashId());
            updateData.setJumei_sku_no(skuPriceBean.getJmSkuNo());
            updateData.setDeal_price(skuPriceBean.getDealPrice());
            updateData.setMarket_price(skuPriceBean.getMarketPrice());
        }
        serviceJumeiHtDeal.updateDealPriceBatch(shopBean,request);
    }
    private Long getTime(Date d) throws Exception {
        long l = d.getTime() / 1000 - 8 * 3600;
        return l;
    }
}
