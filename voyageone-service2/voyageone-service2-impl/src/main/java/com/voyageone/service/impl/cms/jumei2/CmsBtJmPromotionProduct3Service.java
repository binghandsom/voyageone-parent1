package com.voyageone.service.impl.cms.jumei2;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.bean.cms.businessmodel.ProductIdListInfo;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.ProductTagInfo;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.UpdatePromotionProductParameter;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.UpdatePromotionProductTagParameter;
import com.voyageone.service.bean.cms.jumei.*;
import com.voyageone.service.dao.cms.CmsBtJmPromotionDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionTagProductDao;
import com.voyageone.service.daoext.cms.CmsBtJmProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionSkuDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionTagProductDaoExt;
import com.voyageone.service.impl.cms.jumei.CmsMtJmConfigService;
import com.voyageone.service.impl.cms.jumei.platform.JMShopBeanService;
import com.voyageone.service.impl.cms.jumei.platform.JuMeiProductPlatformService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtJmProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionTagProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.util.MapModel;
import org.mortbay.util.ajax.AjaxFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotionProduct3Service {
    @Autowired
    CmsBtJmPromotionProductDao dao;
    @Autowired
    CmsBtJmPromotionProductDaoExt daoExt;
    @Autowired
    CmsBtJmProductDaoExt daoExtCmsBtJmProductDaoExt;
    @Autowired
    CmsBtJmPromotionSkuDaoExt daoExtCmsBtJmPromotionSku;
    @Autowired
    JuMeiProductPlatformService serviceJuMeiProductPlatform;
    @Autowired
    CmsMtJmConfigService serviceCmsMtJmConfig;
    @Autowired
    JMShopBeanService serviceJMShopBean;
    @Autowired
    CmsBtJmPromotionDao daoCmsBtJmPromotion;
    @Autowired
    CmsBtJmPromotionTagProductDao daoCmsBtJmPromotionTagProduct;
    @Autowired
    CmsBtJmPromotionTagProductDaoExt daoExtCmsBtJmPromotionTagProduct;
    @Autowired
    private ProductService productService;
    public CmsBtJmPromotionProductModel select(int id) {
        return dao.select(id);
    }

    public List<MapModel> getPageByWhere(Map<String, Object> map) {
        return daoExt.selectPageByWhere(map);
    }

    public int getCountByWhere(Map<String, Object> map) {
        return daoExt.selectCountByWhere(map);
    }

    public int delete(int id) {
        return dao.delete(id);
    }

    @VOTransactional
    public int updateDealPrice(BigDecimal dealPrice, int id, String userName) {
        CmsBtJmPromotionProductModel model = dao.select(id);
        model.setDealPrice(dealPrice);
        model.setModifier(userName);
        dao.update(model);
        return daoExtCmsBtJmPromotionSku.updateDealPrice(dealPrice, model.getId());
    }

    @VOTransactional
    public void deleteByPromotionId(int promotionId) {
        daoExt.deleteByPromotionId(promotionId);
        daoExtCmsBtJmPromotionSku.deleteByPromotionId(promotionId);
    }

    @VOTransactional
    public void deleteByProductIdList(ProductIdListInfo parameter) {
        daoExt.deleteByProductIdListInfo(parameter);
        daoExtCmsBtJmPromotionSku.deleteByProductIdListInfo(parameter);
    }

    //批量更新价格
    @VOTransactional
    public void batchUpdateDealPrice(BatchUpdatePriceParameterBean parameter) {
//        <option value="0">中国官网价格</option> <!--msrp_rmb-->
//        <option value="1">中国指导价格</option> <!--retail_price-->
//        <option value="2">中国最终售价</option> <!--sale_price-->
        if (parameter.getListPromotionProductId().size() == 0) return;
        String price = "";
        if (parameter.getPriceValueType() == 1) {//价格
            price = Double.toString(parameter.getPrice());
            //parameter.setDiscount(BigDecimalUtil.divide(price));
        } else//折扣 0：市场价 1：团购价
        {
            if (parameter.getPriceType() == 0)//团购价 deal_price
            {
                price = "b.msrp_rmb*" + Double.toString(parameter.getDiscount());//中国官网价格
            } else if (parameter.getPriceType() == 1) //市场价 market_price
            {
                price = "b.retail_price*" + Double.toString(parameter.getDiscount());//中国指导价格
            } else if (parameter.getPriceType() == 2) {
                price = "b.sale_price*" + Double.toString(parameter.getDiscount());//中国最终售价
            }
        }
        daoExt.batchUpdateDealPrice(parameter.getListPromotionProductId(), price);
        daoExtCmsBtJmPromotionSku.batchUpdateDealPrice(parameter.getListPromotionProductId(), price);
    }

    //批量同步价格
    public void batchSynchPrice(BatchSynchPriceParameter parameter) {
        if (parameter.getListPromotionProductId().size() == 0) return;
        daoExt.batchSynchPrice(parameter.getListPromotionProductId());
    }

    //全量同步价格
    public void synchAllPrice(int promotionId) {
        daoExt.synchAllPrice(promotionId);
    }

    //批量再售
    public void batchCopyDeal(BatchCopyDealParameter parameter) {
        if (parameter.getListPromotionProductId().size() == 0) return;
        daoExt.batchCopyDeal(parameter.getListPromotionProductId());
    }

    //全部再售
    public CallResult copyDealAll(int promotionId) {
        CallResult result = new CallResult();
        CmsBtJmPromotionModel model = daoCmsBtJmPromotion.select(promotionId);
        if (model.getPrePeriodStart().getTime() < new Date().getTime()) {
            result.setMsg("预热已经开始,不能全量上传!");
            result.setResult(false);
            return result;
        }
        daoExt.copyDealAll(promotionId);
        return result;
    }

    //批量删除 product  已经再售的不删
    @VOTransactional
    public void batchDeleteProduct(BatchDeleteProductParameter parameter) {
        //先删除sku 再删除product
        daoExtCmsBtJmPromotionSku.batchDeleteSku(parameter.getListPromotionProductId());
        daoExt.batchDeleteProduct(parameter.getListPromotionProductId());
    }

    @VOTransactional //删除全部product  已经再售的不删
    public void deleteAllProduct(int promotionId) {
        //先删除sku 再删除product
        daoExtCmsBtJmPromotionSku.deleteAllSku(promotionId);
        daoExt.deleteAllProduct(promotionId);
    }

    public boolean existsCopyDealByPromotionId(int promotionId) {
        Map<String, Object> map = new HashMap<>();
        map.put("cmsBtJmPromotionId", promotionId);
        map.put("synchStatus", 2);
        return dao.selectOne(map) != null;
    }

    //商品预览
    public ProductViewBean getProductView(int promotionProductId) {
        ProductViewBean productViewBean = new ProductViewBean();
        CmsBtJmPromotionProductModel modelPromotionProduct = dao.select(promotionProductId);
        CmsBtJmProductModel modelProduct = daoExtCmsBtJmProductDaoExt.selectByProductCodeChannelId(modelPromotionProduct.getProductCode(), modelPromotionProduct.getChannelId());
        productViewBean.setModelJmPromotionProduct(modelPromotionProduct);
        productViewBean.setModelJmProduct(modelProduct);
        List<MapModel> mapModelList = daoExtCmsBtJmPromotionSku.selectViewListByPromotionProductId(promotionProductId);
        productViewBean.setSkuList(mapModelList);
        return productViewBean;
    }

    //更新PromotionProduct 目前只更新 limit
    public int updatePromotionProduct(UpdatePromotionProductParameter parameter,String userName) {
        CmsBtJmPromotionProductModel model = dao.select(parameter.getId());
        if (model.getLimit() != parameter.getLimit()) {
            model.setLimit(parameter.getLimit());
            model.setUpdateStatus(1);
            model.setModifier(userName);
            return dao.update(model);
        }
        return 1;
    }

    @VOTransactional
    public int updatePromotionProductTag(UpdatePromotionProductTagParameter parameter,String userName) {
        String tagNameList = "";
        for (ProductTagInfo tagInfo : parameter.getTagList()) {
            tagNameList += "|" + tagInfo.getTagName();
        }
        CmsBtJmPromotionProductModel model = dao.select(parameter.getId());
        model.setPromotionTag(tagNameList);//1.更新 CmsBtJmPromotionProductModel tag
        model.setModifier(userName);
        dao.update(model);//1

        CmsBtJmPromotionModel modelPromotion = daoCmsBtJmPromotion.select(model.getCmsBtJmPromotionId());
        daoExtCmsBtJmPromotionTagProduct.deleteByCmsBtJmPromotionProductId(parameter.getId());//2删除旧的tag
        CmsBtJmPromotionTagProductModel modelCmsBtJmPromotionTagProduct = null;
        //3.添加新的tag
        for (ProductTagInfo tagInfo : parameter.getTagList()) {
            modelCmsBtJmPromotionTagProduct = new CmsBtJmPromotionTagProductModel();
            modelCmsBtJmPromotionTagProduct.setCmsBtTagId(tagInfo.getTagId());
            modelCmsBtJmPromotionTagProduct.setTagName(tagInfo.getTagName());
            modelCmsBtJmPromotionTagProduct.setCmsBtJmPromotionProductId(parameter.getId());
            modelCmsBtJmPromotionTagProduct.setChannelId(model.getChannelId());
            modelCmsBtJmPromotionTagProduct.setModifier(userName);
            modelCmsBtJmPromotionTagProduct.setCreated(new Date());
            modelCmsBtJmPromotionTagProduct.setModified(new Date());
            modelCmsBtJmPromotionTagProduct.setCreater(userName);
            daoCmsBtJmPromotionTagProduct.insert(modelCmsBtJmPromotionTagProduct);
        }
        //更新mongo  product  tag
        updateCmsBtProductTags(model, modelPromotion, parameter, userName);
        return 1;
    }
    //更新mongo  product  tag
    private void updateCmsBtProductTags(CmsBtJmPromotionProductModel model,CmsBtJmPromotionModel modelPromotion,UpdatePromotionProductTagParameter parameter,String modifier) {
        //更新商品Tags  sunpt
        CmsBtProductModel productModel = productService.getProductByCode(model.getChannelId(), model.getProductCode());
        if (productModel != null) {
            List<String> tags = productModel.getTags();
            int size = tags.size();
            //1.移除该活动的所有tag
            for (int i = size - 1; i >= 0; i--) {
                String tag = String.format("-%s-", modelPromotion.getRefTagId().toString());
                if (tags.get(i).indexOf(tag)== 0) {
                    tags.remove(i);
                }
            }
            //2.添加新的tag
            for (ProductTagInfo tagInfo : parameter.getTagList()) {
                tags.add(String.format("-%s-%s-", modelPromotion.getRefTagId(), tagInfo.getTagId()));
            }
            productModel.setTags(tags);
            //3.更新
            productService.updateTags(model.getChannelId(), productModel.getProdId(), tags, modifier);
        }
    }

    public  int selectChangeCountByPromotionId(long cmsBtJmPromotionProductId)
    {
        return  daoExt.selectChangeCountByPromotionId(cmsBtJmPromotionProductId);
    }
}

