package com.voyageone.service.impl.cms.jumei2;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ExceptionUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jumei.JumeiHtDealService;
import com.voyageone.components.jumei.bean.HtDealUpdate_DealInfo;
import com.voyageone.components.jumei.bean.HtDeal_UpdateDealPriceBatch_UpdateData;
import com.voyageone.components.jumei.reponse.HtDealCopyDealResponse;
import com.voyageone.components.jumei.reponse.HtDealUpdateDealEndTimeResponse;
import com.voyageone.components.jumei.reponse.HtDealUpdateDealPriceBatchResponse;
import com.voyageone.components.jumei.reponse.HtDealUpdateResponse;
import com.voyageone.components.jumei.request.HtDealCopyDealRequest;
import com.voyageone.components.jumei.request.HtDealUpdateDealEndTimeRequest;
import com.voyageone.components.jumei.request.HtDealUpdateDealPriceBatchRequest;
import com.voyageone.components.jumei.request.HtDealUpdateRequest;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.bean.cms.jumei.SkuPriceBean;
import com.voyageone.service.dao.cms.CmsBtJmPromotionDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.daoext.cms.CmsBtJmProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionSkuDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.jumei.platform.JMShopBeanService;
import com.voyageone.service.impl.cms.jumei.platform.JuMeiProductAddPlatformService;
import com.voyageone.service.model.cms.CmsBtJmProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by dell on 2016/4/19.
 */
@Service
public class JuMeiProductPlatform3Service extends BaseService {
    @Autowired
    CmsBtJmPromotionDao daoCmsBtJmPromotion;
    @Autowired
    CmsBtJmPromotionProductDao daoCmsBtJmPromotionProduct;
    @Autowired
    CmsBtJmPromotionProductDaoExt daoExtCmsBtJmPromotionProduct;
    @Autowired
    JMShopBeanService serviceJMShopBean;
   // @Autowired
   // JuMeiProductUpdateService service;
    @Autowired
    JumeiHtDealService serviceJumeiHtDeal;

    @Autowired
    CmsBtJmProductDaoExt daoExtCmsBtJmProduct;
    @Autowired
    CmsBtJmPromotionSkuDaoExt daoExtCmsBtJmPromotionSku;
    private static final Logger LOG = LoggerFactory.getLogger(JuMeiProductAddPlatformService.class);

    public void updateJmByPromotionId(int promotionId) throws Exception {
        CmsBtJmPromotionModel modelCmsBtJmPromotion = daoCmsBtJmPromotion.select(promotionId);
        ShopBean shopBean = serviceJMShopBean.getShopBean(modelCmsBtJmPromotion.getChannelId());
        LOG.info(promotionId + " 聚美上新开始");
        List<CmsBtJmPromotionProductModel> listCmsBtJmPromotionProductModel = daoExtCmsBtJmPromotionProduct.selectJMCopyList(promotionId);
        try {
            for (CmsBtJmPromotionProductModel model : listCmsBtJmPromotionProductModel) {
                updateJm(modelCmsBtJmPromotion, model, shopBean);
            }
        } catch (Exception ex) {
            LOG.error("addProductAndDealByPromotionId上新失败", ex);
            ex.printStackTrace();
        }
        LOG.info(promotionId + " 聚美上新end");
    }
    public CallResult updateJm( CmsBtJmPromotionModel modelCmsBtJmPromotion,CmsBtJmPromotionProductModel model, ShopBean shopBean) throws Exception {
        CallResult result = new CallResult();
        try {
            if (model.getSynchStatus() == 1 || model.getSynchStatus() == 0 || model.getSynchStatus() == 3) {
                //获取在售商品的model
                CmsBtJmPromotionProductModel onSaleModel = daoExtCmsBtJmPromotionProduct.selectOnSaleByCode(model.getChannelId(), model.getProductCode());
                if (onSaleModel != null) {
                    model.setErrorMsg("存在在售商品JmHashId:" + onSaleModel.getJmHashId() + "已在其它聚美专场，且未过期。该商品上传失败不能上传");
                    daoCmsBtJmPromotionProduct.update(model);
                    return result;
                }
                // 再售
                if(StringUtil.isEmpty(model.getJmHashId())) {
                    copyDeal(modelCmsBtJmPromotion, model, shopBean);
                }
                //更新deal   商品属性 价格
                updateDeal(model, shopBean);
                if (model.getDealEndTimeStatus() == 1) {
                    updateDealEndTime(modelCmsBtJmPromotion, model, shopBean);//deal延期
                }

            } else {
                if (model.getPriceStatus() == 1) //更新价格
                {
                    updateDeal(model, shopBean);//更新deal   商品属性 价格
                }
                if (model.getDealEndTimeStatus() == 1) {
                    updateDealEndTime(modelCmsBtJmPromotion, model, shopBean);//deal延期
                }
            }
        } catch (Exception ex) {
            model.setErrorMsg(ex.getMessage());
            // model.setUpdateState(EnumJuMeiUpdateState.Error.getId());//同步更新失败
            LOG.error("JuMeiProductPlatform3Service.addProductAndDealByPromotionId", ex);
            try {
                if (model.getErrorMsg().length() > 600) {
                    model.setErrorMsg(model.getErrorMsg().substring(0, 600));
                }
                daoCmsBtJmPromotionProduct.update(model);
            } catch (Exception cex) {
                LOG.error("JuMeiProductPlatform3Service.addProductAndDealByPromotionId", cex);
            }
            result.setResult(false);
            result.setMsg(ex.getMessage());
        }
        return result;
    }

    private void updateDealEndTime(CmsBtJmPromotionModel modelCmsBtJmPromotion, CmsBtJmPromotionProductModel model, ShopBean shopBean) throws Exception {
        if (modelCmsBtJmPromotion.getActivityEnd().getTime() > model.getActivityEnd().getTime()) {
            jmHtDealUpdateDealEndTime(modelCmsBtJmPromotion, model, shopBean);
            model.setDealEndTimeStatus(2);
            model.setErrorMsg("");
            model.setActivityEnd(modelCmsBtJmPromotion.getActivityEnd());
            daoCmsBtJmPromotionProduct.update(model);
        }
    }

    private void updateDeal(CmsBtJmPromotionProductModel model, ShopBean shopBean) throws Exception {
        List<SkuPriceBean> listSkuPrice = daoExtCmsBtJmPromotionSku.selectJmSkuPriceInfoListByPromotionProductId(model.getId());
        jmHtDealUpdateDealPriceBatch(model, shopBean, listSkuPrice);//更新deal价格
        String jmSkuNoList = getjmSkuNo(listSkuPrice);
        jmHtDealUpdate(model, shopBean, jmSkuNoList);//更新deal信息   limit   jmSkuNo
        model.setPriceStatus(2);
        if (model.getUpdateStatus() == 1) {
            model.setUpdateStatus(2);//价格和商品属性都更新成功后 设置为已变更
        }
        model.setErrorMsg("");
        daoCmsBtJmPromotionProduct.update(model);
    }

    //再售
    private void copyDeal(CmsBtJmPromotionModel modelCmsBtJmPromotion, CmsBtJmPromotionProductModel model, ShopBean shopBean) throws Exception {
        CmsBtJmProductModel modelJmProduct = daoExtCmsBtJmProduct.selectByProductCodeChannelId(model.getProductCode(), model.getChannelId());
        jmHtDealCopy(modelCmsBtJmPromotion, model, shopBean, modelJmProduct.getOriginJmHashId());//再售
        model.setActivityStart(modelCmsBtJmPromotion.getActivityStart());
        model.setActivityEnd(modelCmsBtJmPromotion.getActivityEnd());
        model.setSynchStatus(2);
        daoCmsBtJmPromotionProduct.update(model);//在售之后先保存  避免下面调用接口失败 jmHashId丢失

        if (modelCmsBtJmPromotion.getStatus() == null || modelCmsBtJmPromotion.getStatus() == 0) {//更新互动
            modelCmsBtJmPromotion.setStatus(1);//已经有商品上新
            daoCmsBtJmPromotion.update(modelCmsBtJmPromotion);
        }
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
    private void jmHtDealCopy(CmsBtJmPromotionModel modelCmsBtJmPromotion,CmsBtJmPromotionProductModel model, ShopBean shopBean, String originJmHashId) throws Exception {
        HtDealCopyDealRequest request = new HtDealCopyDealRequest();
        request.setStart_time(modelCmsBtJmPromotion.getActivityStart());
        request.setEnd_time(modelCmsBtJmPromotion.getActivityEnd());
        request.setJumei_hash_id(originJmHashId);//原始jmHashId
        HtDealCopyDealResponse response = serviceJumeiHtDeal.copyDeal(shopBean, request);
        if (response.is_Success()) {
            model.setJmHashId(response.getJumei_hash_id());
        } else {
            if(!StringUtils.isEmpty(response.getJumei_hash_id())) {
                model.setJmHashId(response.getJumei_hash_id());
            }
            model.setSynchStatus(3);
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
        HtDealUpdateResponse response= serviceJumeiHtDeal.update(shopBean, request);
        if(!response.is_Success())
        {
            model.setPriceStatus(3);
            throw new BusinessException("productId:" + model.getId() + "jmHtDealCopyErrorMsg:" + response.getErrorMsg());
           // $error(response.getBody());
        }
    }
    private void jmHtDealUpdateDealEndTime(CmsBtJmPromotionModel modelCmsBtJmPromotion,CmsBtJmPromotionProductModel model, ShopBean shopBean) throws Exception {
        HtDealUpdateDealEndTimeRequest request = new HtDealUpdateDealEndTimeRequest();
        request.setJumei_hash_id(model.getJmHashId());
        request.setEnd_time(modelCmsBtJmPromotion.getActivityEnd());
        HtDealUpdateDealEndTimeResponse response = serviceJumeiHtDeal.updateDealEndTime(shopBean, request);
        if (!response.is_Success()) {
            model.setDealEndTimeStatus(3);
            throw new BusinessException("productId:" + model.getId() + "updateDealEndTime:" + response.getErrorMsg());
        }
    }
    //更新价格
    private void   jmHtDealUpdateDealPriceBatch(CmsBtJmPromotionProductModel model, ShopBean shopBean, List<SkuPriceBean> listSkuPrice) throws Exception {
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
        request.setUpdate_data(list);
        HtDealUpdateDealPriceBatchResponse response= serviceJumeiHtDeal.updateDealPriceBatch(shopBean, request);
        if(!response.is_Success())
        {
            model.setPriceStatus(3);
            throw new BusinessException("productId:" + model.getId() + "jmHtDealCopyErrorMsg:" + response.getErrorMsg());
        }
    }
}
