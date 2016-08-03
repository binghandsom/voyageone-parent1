package com.voyageone.service.impl.cms.jumei2;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.*;
import com.voyageone.service.bean.cms.jumei.*;
import com.voyageone.service.dao.cms.CmsBtJmPromotionDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionTagProductDao;
import com.voyageone.service.dao.cms.CmsBtPromotionDao;
import com.voyageone.service.daoext.cms.*;
import com.voyageone.service.impl.cms.CmsMtChannelValuesService;
import com.voyageone.service.impl.cms.jumei.CmsMtJmConfigService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.*;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.util.MapModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
//    @Autowired
//    JuMeiProductPlatformService serviceJuMeiProductPlatform;
    @Autowired
    CmsMtJmConfigService serviceCmsMtJmConfig;
//    @Autowired
//    JMShopBeanService serviceJMShopBean;
    @Autowired
    CmsBtJmPromotionDao daoCmsBtJmPromotion;
    @Autowired
    CmsBtJmPromotionTagProductDao daoCmsBtJmPromotionTagProduct;
    @Autowired
    CmsBtJmPromotionTagProductDaoExt daoExtCmsBtJmPromotionTagProduct;
    @Autowired
    private ProductService productService;
    @Autowired
    private CmsBtJmPromotion3Service service3CmsBtJmPromotion;
    @Autowired
    CmsBtPromotionCodesDaoExtCamel daoExtCamelCmsBtPromotionCodes;
    @Autowired
    private CmsBtPromotionGroupsDaoExtCamel daoExtCamelCmsBtPromotionGroups;
    @Autowired
    private CmsBtPromotionSkusDaoExtCamel daoExtCamelCmsBtPromotionSkus;
    @Autowired
    private CmsMtChannelValuesService cmsMtChannelValuesService;
@Autowired
private CmsBtPromotionDao daoCmsBtPromotion;
    public CmsBtJmPromotionProductModel select(int id) {
        return dao.select(id);
    }

    public List<MapModel> getPageByWhere(Map<String, Object> map) {
        return daoExt.selectPageByWhere(map);
    }

    public InitResult init(InitParameter parameter, String channelId, String language) {
        InitResult result = new InitResult();
        result.setModelPromotion(daoCmsBtJmPromotion.select(parameter.getJmPromotionRowId()));//CmsBtJmPromotion
        result.setListTag(service3CmsBtJmPromotion.getTagListByPromotionId(parameter.getJmPromotionRowId()));//聚美活动的所有tag
        result.setChangeCount(selectChangeCountByPromotionId(parameter.getJmPromotionRowId()));//获取变更数量

        long preStartLocalTime = DateTimeUtilBeijing.toLocalTime(result.getModelPromotion().getPrePeriodStart());//北京时间转本地时区时间戳
        long activityEndTime = DateTimeUtilBeijing.toLocalTime(result.getModelPromotion().getActivityEnd());//北京时间转本地时区时间戳
        result.setIsBegin(preStartLocalTime < new Date().getTime());//活动是否看开始     用预热时间
        result.setIsEnd(activityEndTime < new Date().getTime());//活动是否结束            用活动时间
        int hour = DateTimeUtil.getDateHour(DateTimeUtilBeijing.getCurrentBeiJingDate());
        result.setIsUpdateJM(!(hour == 10));//是否可以更新聚美  10到11点一小时之内不允许更新聚美平台
        boolean isBefore5DaysBeforePreBegin = DateTimeUtil.addDays(new Date(), 5).getTime() < preStartLocalTime;//是否是预热开始前5天之前  预热开始前5天之前不让更新聚美
        if(isBefore5DaysBeforePreBegin)// 预热开始前5天之前不让更新聚美
        {
           result.setIsUpdateJM(false);
        }
        // 获取brand list
        result.setBrandList(TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, channelId, language));
        return result;
    }

    public int getCountByWhere(Map<String, Object> map) {
        return daoExt.selectCountByWhere(map);
    }

    public int delete(int id) {
        return dao.delete(id);
    }

//    @VOTransactional
//    public int updateDealPrice(BigDecimal dealPrice, int id, String userName) {
//        CmsBtJmPromotionProductModel model = dao.select(id);
//        model.setDealPrice(dealPrice);
//        model.setModifier(userName);
//        dao.update(model);
//        return daoExtCmsBtJmPromotionSku.updateDealPrice(dealPrice, model.getId());
//    }

//    @VOTransactional
//    public void deleteByPromotionId(int jmPromotionId) {
//        daoExt.deleteByPromotionId(jmPromotionId);
//        daoExtCmsBtJmPromotionSku.deleteByPromotionId(jmPromotionId);
//
//    }
    public CmsBtPromotionModel getCmsBtPromotionModel(int jmPromotionId)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("promotionId",jmPromotionId);
        map.put("cartId", CartEnums.Cart.JM.getValue());
        CmsBtPromotionModel promotion = daoCmsBtPromotion.selectOne(map);
        return  promotion;
    }
    //批量修改价格
    @VOTransactional
    public CallResult batchUpdateDealPrice(BatchUpdatePriceParameterBean parameter) {
        CallResult result = new CallResult();
//        <option value="0">中国官网价格</option> <!--msrp_rmb-->
//        <option value="1">中国指导价格</option> <!--retail_price-->
//        <option value="2">中国最终售价</option> <!--sale_price-->

        if (parameter.getListPromotionProductId().isEmpty()) return result;

        String price = "";
        if (parameter.getPriceValueType() == 1) {//价格
            price = Double.toString(parameter.getPrice());
            //parameter.setDiscount(BigDecimalUtil.divide(price));
        } else//折扣 0：市场价 1：团购价
        {
            if (parameter.getPriceType() == 0)//团购价 deal_price
            {
                price = "a.msrp_rmb*" + Double.toString(parameter.getDiscount());//中国官网价格
            } else if (parameter.getPriceType() == 1) //市场价 market_price
            {
                price = "a.retail_price*" + Double.toString(parameter.getDiscount());//中国指导价格
            } else if (parameter.getPriceType() == 2) {
                price = "a.sale_price*" + Double.toString(parameter.getDiscount());//中国最终售价
            }
        }
        if (StringUtils.isEmpty(price)) {
            result.setResult(false);
            result.setMsg("修改价格失败!");
            return result;
        }
        price = "CEIL(" + price + ")";//向上取整

        CmsBtJmPromotionSkuModel modelCmsBtJmPromotionSku = daoExtCmsBtJmPromotionSku.selectNotUpdateDealPrice(parameter.getListPromotionProductId(), price);
        if (modelCmsBtJmPromotionSku != null) {
            result.setResult(false);
            result.setMsg(String.format("skuCode:%s更新后的团购价大于市场价,不能更新!", modelCmsBtJmPromotionSku.getSkuCode()));
            return result;
        }
        daoExtCmsBtJmPromotionSku.batchUpdateDealPrice(parameter.getListPromotionProductId(), price);//更新sku价格
        daoExt.updateAvgPriceByListPromotionProductId(parameter.getListPromotionProductId());//更新平均值 最大值 最小值    已上新的更新为已经变更
        return result;
    }

    //批量同步价格  1. then price_status=1
    public void batchSynchPrice(BatchSynchPriceParameter parameter) {

        if (parameter.getListPromotionProductId().isEmpty()) return;
        daoExt.batchSynchPrice(parameter.getListPromotionProductId());
    }

    //全量同步价格
    public CallResult synchAllPrice(int promotionId) {
        CallResult result = new CallResult();
        CmsBtJmPromotionModel model = daoCmsBtJmPromotion.select(promotionId);
        if (model.getPrePeriodStart().getTime() < DateTimeUtilBeijing.getCurrentBeiJingDate().getTime()) {
            result.setMsg("预热已经开始,不能全量同步价格!");
            result.setResult(false);
            return result;
        }
        daoExt.synchAllPrice(promotionId);
        return result;
    }

    @VOTransactional
    //批量上传
    //批量再售 1. if未上传  then synch_status=1  2.if已上传&预热未开始  then price_status=1
    public void batchCopyDeal(BatchCopyDealParameter parameter) {
        if (parameter.getListPromotionProductId().isEmpty()) return;
        CmsBtJmPromotionModel modelCmsBtJmPromotion = daoCmsBtJmPromotion.select(parameter.getPromotionId());

        //2.8.3
        //更新为待上传   1. if未上传  then synch_status=1
        daoExt.batchCopyDeal(parameter.getListPromotionProductId());
        //更新为价格待更新  2. if已上传  then update_status=1
         daoExt.batchCopyDealUpdatePrice(parameter.getListPromotionProductId());

    }

    //全部再售    //1. if未上传  then synch_status=1   2.if已上传  then price_status=1
    @VOTransactional
    public CallResult copyDealAll(int promotionId) {
        CallResult result = new CallResult();
        CmsBtJmPromotionModel model = daoCmsBtJmPromotion.select(promotionId);
        if (model.getPrePeriodStart().getTime() < DateTimeUtilBeijing.getCurrentBeiJingDate().getTime()) {
            result.setMsg("预热已经开始,不能全量上传!");
            result.setResult(false);
            return result;
        }
        daoExt.copyDealAll_UpdatePriceStatus(promotionId);
        daoExt.copyDealAll_UpdateSynchStatus(promotionId);
        return result;
    }

    //批量删除  product  已经再售的不删
    @VOTransactional
    public void batchDeleteProduct(BatchDeleteProductParameter parameter) {
        CmsBtJmPromotionModel model = daoCmsBtJmPromotion.select(parameter.getPromotionId());

        // 2.7.1
        if (model.getPrePeriodStart().getTime() < DateTimeUtilBeijing.getCurrentBeiJingDate().getTime()) {
            throw new BusinessException("预热已经开始,不能批量删除!");
        }
        //2.7.2.1 只删除未上传的商品  先删除sku 再删除product
        daoExtCmsBtJmPromotionSku.batchDeleteSku(parameter.getListPromotionProductId());
        daoExt.batchDeleteProduct(parameter.getListPromotionProductId());

        //2.7.2.2  已经上传的商品  写入错误信息
        daoExt.updateSynch2ErrorMsg(parameter.getListPromotionProductId(), "该商品已调用过聚美上传API，聚美平台静止相关操作纪录的删除。为保证数据一致性，该商品无法删除");

        //2.7.3 删除 CmsBtPromotionCodes  CmsBtPromotionSkus
        CmsBtPromotionModel modelCmsBtPromotion = getCmsBtPromotionModel(parameter.getPromotionId());
        if (modelCmsBtPromotion != null) {
            List<CmsBtJmPromotionProductModel> listNotSych = daoExt.selectNotSynchListByPromotionProductIds(parameter.getListPromotionProductId());
            List<String> listNotSychCode = getListNotSychCode(listNotSych);//获取未上传的code
            Map<String, Object> map = new HashMap<>();
            map.put("listProductCode", listNotSychCode);
            map.put("promotionId", modelCmsBtPromotion.getId());
            daoExtCamelCmsBtPromotionCodes.deleteByPromotionCodeList(map);
            daoExtCamelCmsBtPromotionSkus.deleteByPromotionCodeList(map);
        }
    }
    public List<String> getListNotSychCode(List<CmsBtJmPromotionProductModel> listNotSych)
    {
        List<String> codeList=new ArrayList<>();
        listNotSych.stream().forEach((o)->{
            codeList.add(o.getProductCode());
        });
        return codeList;
    }
    @VOTransactional //删除全部product  已经再售的不删
    public void deleteAllProduct(int jmPromotionId) {
        //先删除sku 再删除product
        daoExtCmsBtJmPromotionSku.deleteAllSku(jmPromotionId);
        daoExt.deleteAllProduct(jmPromotionId);
        CmsBtPromotionModel modelCmsBtPromotion = getCmsBtPromotionModel(jmPromotionId);
        if (modelCmsBtPromotion != null) {
            daoExtCamelCmsBtPromotionCodes.deleteByPromotionId(modelCmsBtPromotion.getId());
            daoExtCamelCmsBtPromotionGroups.deleteByPromotionId(modelCmsBtPromotion.getId());
            daoExtCamelCmsBtPromotionSkus.deleteByPromotionId(modelCmsBtPromotion.getId());
        }
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
    public int updatePromotionProduct(UpdatePromotionProductParameter parameter, String userName) {
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
    public int updatePromotionProductTag(UpdatePromotionProductTagParameter parameter, String userName) {
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
    private void updateCmsBtProductTags(CmsBtJmPromotionProductModel model, CmsBtJmPromotionModel modelPromotion, UpdatePromotionProductTagParameter parameter, String modifier) {
        //更新商品Tags  sunpt
        CmsBtProductModel productModel = productService.getProductByCode(model.getChannelId(), model.getProductCode());
        if (productModel != null) {
            List<String> tags = productModel.getTags();
            int size = tags.size();
            //1.移除该活动的所有tag
            for (int i = size - 1; i >= 0; i--) {
                String tag = String.format("-%s-", modelPromotion.getRefTagId().toString());
                if (tags.get(i).indexOf(tag) == 0) {
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

    public int selectChangeCountByPromotionId(long cmsBtJmPromotionProductId) {
        return daoExt.selectChangeCountByPromotionId(cmsBtJmPromotionProductId);
    }
}

