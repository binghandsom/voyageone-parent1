package com.voyageone.service.impl.cms.jumei2;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.bean.cms.businessmodel.JMPromotionProduct.UpdateRemarkParameter;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.*;
import com.voyageone.service.bean.cms.jumei.*;
import com.voyageone.service.bean.cms.jumei2.JmProduct;
import com.voyageone.service.dao.cms.CmsBtJmPromotionDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionTagProductDao;
import com.voyageone.service.daoext.cms.*;
import com.voyageone.service.impl.cms.jumei.CmsMtJmConfigService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.JMProductUpdateMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.JmPromotionProductStockSyncMQMessageBody;
import com.voyageone.service.model.cms.*;
import com.voyageone.service.model.util.MapModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toList;

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
    CmsMtJmConfigService serviceCmsMtJmConfig;
    @Autowired
    CmsBtJmPromotionDao daoCmsBtJmPromotion;
    @Autowired
    CmsBtJmPromotionTagProductDao daoCmsBtJmPromotionTagProduct;
    @Autowired
    CmsBtJmPromotionTagProductDaoExt daoExtCmsBtJmPromotionTagProduct;
    @Autowired
    CmsBtPromotionCodesDaoExtCamel daoExtCamelCmsBtPromotionCodes;
    @Autowired
    CmsBtJmPromotionSku3Service cmsBtJmPromotionSku3Service;
    @Autowired
    PromotionService promotionService;
    @Autowired
    CmsBtJmPromotionTagProductService cmsBtJmPromotionTagProductService;
    @Autowired
    ProductService productService;
    @Autowired
    CmsBtJmPromotion3Service service3CmsBtJmPromotion;
    @Autowired
    CmsBtPromotionGroupsDaoExtCamel daoExtCamelCmsBtPromotionGroups;
    @Autowired
    CmsBtPromotionSkusDaoExtCamel daoExtCamelCmsBtPromotionSkus;
    @Autowired
    CmsMqSenderService cmsMqSenderService;

    public CmsBtJmPromotionProductModel select(int id) {
        return dao.select(id);
    }

    public List<MapModel> getPageByWhere(Map<String, Object> map) {
        return daoExt.selectPageByWhere(map);
    }

    public InitResult init(InitParameter parameter, String channelId, String language) {
        //return null;
        InitResult result = new InitResult();
        CmsBtJmPromotionModel model = daoCmsBtJmPromotion.select(parameter.getJmPromotionRowId());

        if (model == null)
            throw new BusinessException("该活动不存在");

        result.setModelPromotion(model);
        result.setListTag(service3CmsBtJmPromotion.getTagListByPromotionId(parameter.getJmPromotionRowId()));//聚美活动的所有tag
        result.setChangeCount(selectChangeCountByPromotionId(parameter.getJmPromotionRowId()));//获取变更数量
        result.setProductCount(selectCountByPromotionId(parameter.getJmPromotionRowId()));

        Date activityEnd = result.getModelPromotion().getActivityEnd();

        if (activityEnd != null) {
            long activityEndTime = DateTimeUtilBeijing.toLocalTime(activityEnd);//北京时间转本地时区时间戳
            result.setIsEnd(activityEndTime < new Date().getTime());//活动是否结束            用活动时间
        }
        if (result.getModelPromotion().getPrePeriodStart() != null) {
            long preStartLocalTime = DateTimeUtilBeijing.toLocalTime(result.getModelPromotion().getPrePeriodStart());//北京时间转本地时区时间戳
            result.setIsBegin(preStartLocalTime < new Date().getTime());//活动是否看开始     用预热时间
            // int hour = DateTimeUtil.getDateHour(DateTimeUtilBeijing.getCurrentBeiJingDate());
            result.setIsUpdateJM(true);
            // result.setIsUpdateJM(!(hour == 10));//是否可以更新聚美  10到11点一小时之内不允许更新聚美平台
            boolean isBefore5DaysBeforePreBegin = DateTimeUtil.addDays(new Date(), 20).getTime() < preStartLocalTime;//是否是预热开始前20天之前  预热开始前20天之前不让更新聚美
            if (isBefore5DaysBeforePreBegin)// 预热开始前20天之前不让更新聚美
            {
                result.setIsUpdateJM(false);
            }
        }
        // // ODO: 2016/8/10  测试完 取消注释  spt begin
        // result.setIsUpdateJM(true);
        // // ODO: 2016/8/10   测试完 取消注释  spt end
        // 获取brand list
        result.setBrandList(TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, channelId, language));

        //判断活动是否过期
        result.getModelPromotion().setPassDated(activityEnd.getTime() < new Date().getTime());

        return result;
    }

    public int getCountByWhere(Map<String, Object> map) {
        return daoExt.selectCountByWhere(map);
    }

    public int delete(int id) {
        return dao.delete(id);
    }

    //批量修改价格 批量变更价格
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
        daoExtCamelCmsBtPromotionCodes.updateJmPromotionPrice(parameter.getJmPromotionId(), parameter.getListPromotionProductId());
        return result;
    }

    //批量修改价格
    @VOTransactional
    public CallResult batchUpdateSkuDealPrice(BatchUpdateSkuPriceParameterBean parameter, String userName) {
        CallResult result = new CallResult();
        if (parameter.getListPromotionProductId().isEmpty())
            return result;

        parameter.getListPromotionProductId().forEach(id -> {
            cmsBtJmPromotionSku3Service.UpdateSkuDealPrice(parameter, id, userName);
        });
        daoExt.updateAvgPriceByListPromotionProductId(parameter.getListPromotionProductId());//更新平均值 最大值 最小值    已上新的更新为已经变更
        daoExtCamelCmsBtPromotionCodes.updateJmPromotionPrice(parameter.getJmPromotionId(), parameter.getListPromotionProductId());
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

    //全量上传
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
        //if 已上传  then 设置为 价格待更新
        daoExt.copyDealAll_UpdatePriceStatus(promotionId);
        //if 未上传 then 设置为 待上传
        daoExt.copyDealAll_UpdateSynchStatus(promotionId);
        return result;
    }

    //批量删除  product  已经再售的不删
    @VOTransactional
    public void batchDeleteProduct(BatchDeleteProductParameter parameter, String channelId) {
        CmsBtJmPromotionModel model = daoCmsBtJmPromotion.select(parameter.getPromotionId());
        //Map<String,Object> map=new HashedMap();

        // 2.7.1
        if (model.getPrePeriodStart().getTime() < DateTimeUtilBeijing.getCurrentBeiJingDate().getTime()) {
            throw new BusinessException("预热已经开始,不能批量删除!");
        }
        //获取未上传的jmproduct
        List<CmsBtJmPromotionProductModel> listNotSych = daoExt.selectNotSynchListByPromotionProductIds(parameter.getListPromotionProductId());
        List<String> listNotSychCode = getListNotSychCode(listNotSych);//获取未上传的code

        if (listNotSychCode.size() > 0) {
            productService.removeTagByCodes(channelId, listNotSychCode, model.getRefTagId());
        }
        //2.7.2.1 只删除未上传的商品  先删除sku  tag  再删除product
        daoExtCmsBtJmPromotionSku.batchDeleteSku(parameter.getListPromotionProductId());
        daoExtCmsBtJmPromotionTagProduct.batchDeleteTag(parameter.getListPromotionProductId());
        daoExt.batchDeleteProduct(parameter.getListPromotionProductId());


        //2.7.2.2  已经上传的商品  写入错误信息
        daoExt.updateSynch2ErrorMsg(parameter.getListPromotionProductId(), "该商品已调用过聚美上传API，聚美平台静止相关操作纪录的删除。为保证数据一致性，该商品无法删除");

        //2.7.3 删除 CmsBtPromotionCodes  CmsBtPromotionSkus
        CmsBtPromotionModel modelCmsBtPromotion = promotionService.getCmsBtPromotionModelByJmPromotionId(parameter.getPromotionId());
        if (modelCmsBtPromotion != null && listNotSych.size() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("listProductCode", listNotSychCode);
            map.put("promotionId", modelCmsBtPromotion.getId());
            daoExtCamelCmsBtPromotionCodes.deleteByPromotionCodeList(map);
            daoExtCamelCmsBtPromotionSkus.deleteByPromotionCodeList(map);
        }
    }

    public List<String> getListNotSychCode(List<CmsBtJmPromotionProductModel> listNotSych) {
        List<String> codeList = new ArrayList<>();
        listNotSych.stream().forEach((o) -> {
            codeList.add(o.getProductCode());
        });
        return codeList;
    }

    // 全量删除
    @VOTransactional //删除全部product  已经再售的不删
    public CallResult deleteAllProduct(int jmPromotionId) {
        CallResult result = new CallResult();
        CmsBtJmPromotionModel model = daoCmsBtJmPromotion.select(jmPromotionId);
        if (model.getPrePeriodStart().getTime() < DateTimeUtilBeijing.getCurrentBeiJingDate().getTime()) {
            result.setMsg("活动预热已开始,不能删除!");
            result.setResult(false);
            return result;
        }

        if (this.existsCopyDealByPromotionId(jmPromotionId)) {
            result.setResult(false);
            result.setMsg("该专场内存在商品已完成上传，禁止删除!");
        }
        List<String> codes = daoExt.selectCodesByJmPromotionId(jmPromotionId);
        productService.removeTagByCodes(model.getChannelId(), codes, model.getRefTagId());
        //先删除sku 再删除product
        daoExtCmsBtJmPromotionSku.deleteAllSku(jmPromotionId);
        daoExt.deleteAllProduct(jmPromotionId);
        CmsBtPromotionModel modelCmsBtPromotion = promotionService.getCmsBtPromotionModelByJmPromotionId(jmPromotionId);
        if (modelCmsBtPromotion != null) {
            daoExtCamelCmsBtPromotionCodes.deleteByPromotionId(modelCmsBtPromotion.getId());
            daoExtCamelCmsBtPromotionGroups.deleteByPromotionId(modelCmsBtPromotion.getId());
            daoExtCamelCmsBtPromotionSkus.deleteByPromotionId(modelCmsBtPromotion.getId());
        }
        return result;
    }

    public CallResult updateDealEndTimeAll(ParameterUpdateDealEndTimeAll parameter) {
        CallResult result = new CallResult();
        CmsBtJmPromotionModel modelCmsBtJmPromotion = daoCmsBtJmPromotion.select(parameter.getPromotionId());
        //1017
//        if (modelCmsBtJmPromotion.getIsPromotionFullMinus())//该专场为 满减专场的场合
//        {
//            result.setMsg("该专场为满减专场,不允许延期");
//            result.setResult(false);
//            return result;
//        }
        modelCmsBtJmPromotion.setActivityEnd(parameter.getDealEndTime());
        daoCmsBtJmPromotion.update(modelCmsBtJmPromotion);
        daoExt.updateDealEndTimeAll(parameter);//商品改变延期状态
        return result;
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
        List<MapModel> mapModelList = daoExtCmsBtJmPromotionSku.selectViewListByPromotionProductId(promotionProductId, modelPromotionProduct.getCmsBtJmPromotionId());
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

    //修改单个商品tag
    @VOTransactional
    public int updatePromotionProductTag(UpdatePromotionProductTagParameter parameter, String channelId, String userName) {
        cmsBtJmPromotionTagProductService.updatePromotionProductTag(parameter, channelId, userName);
        return 1;
    }

    //批量修改商品tag
    @VOTransactional
    public int updatePromotionListProductTag(UpdateListPromotionProductTagParameter parameter, String channelId, String userName) {

        if (parameter.getListPromotionProductId() == null || parameter.getListPromotionProductId().size() == 0)
            return 0;
        UpdatePromotionProductTagParameter parameterProductTag = new UpdatePromotionProductTagParameter();
        parameterProductTag.setTagList(parameter.getTagList());

        parameter.getListPromotionProductId().forEach(id -> {
            parameterProductTag.setId(id);
            updatePromotionProductTag(parameterProductTag, channelId, userName);
        });
        return 1;
    }


    public int selectChangeCountByPromotionId(long JmPromotionId) {
        return daoExt.selectChangeCountByPromotionId(JmPromotionId);
    }

    public int selectCountByPromotionId(long JmPromotionId) {
        return daoExt.selectCountByPromotionId(JmPromotionId);
    }


    public int updateRemark(UpdateRemarkParameter parameter) {
        return daoExt.updateRemark(parameter.getJmPromotionProductId(), parameter.getRemark());
    }

    public List<JmProduct> getPromotionTagProductList(int tagId) {

        List<CmsBtJmPromotionProductModel> jmPromotionProductModelList = getPromotionProductInTag(tagId);

        if (jmPromotionProductModelList.isEmpty())
            return new ArrayList<>(0);

        return jmPromotionProductModelList
                .stream()
                .map(jmPromotionProductModel -> {
                    CmsBtJmProductModel productModel = daoExtCmsBtJmProductDaoExt.selectByProductCodeChannelId(jmPromotionProductModel.getProductCode(), jmPromotionProductModel.getChannelId());
                    return new JmProduct(productModel, jmPromotionProductModel);
                })
                .collect(toList());
    }

    public void saveProductSort(CmsBtTagModel tagModel, List<JmProduct> jmProductList, final String username) {
        // 查询老数据
        CmsBtJmPromotionTagProductModel parameter = new CmsBtJmPromotionTagProductModel();
        parameter.setCmsBtTagId(tagModel.getId());
        List<CmsBtJmPromotionTagProductModel> jmPromotionTagProductModelList = daoCmsBtJmPromotionTagProduct.selectList(parameter);
        // 清空老数据
        if (!jmPromotionTagProductModelList.isEmpty())
            jmPromotionTagProductModelList.forEach(cmsBtJmPromotionTagProductModel -> daoCmsBtJmPromotionTagProduct.delete(cmsBtJmPromotionTagProductModel.getId()));
        // 插入新数据
        jmProductList
                .stream()
                .map(JmProduct::getJmPromotionProduct)
                .filter(jmPromotionProduct -> jmPromotionProduct != null)
                .forEach(jmPromotionProduct -> {
                    CmsBtJmPromotionTagProductModel tagProduct = new CmsBtJmPromotionTagProductModel();
                    tagProduct.setChannelId(tagModel.getChannelId());
                    tagProduct.setCmsBtJmPromotionProductId(jmPromotionProduct.getId());
                    tagProduct.setCmsBtTagId(tagModel.getId());
                    tagProduct.setTagName(tagModel.getTagName());
                    tagProduct.setCreater(username);
                    tagProduct.setModifier(username);

                    daoCmsBtJmPromotionTagProduct.insert(tagProduct);
                });
    }

    public List<CmsBtJmPromotionProductModel> getPromotionProductInTag(Integer tagId) {
        CmsBtJmPromotionTagProductModel parameter = new CmsBtJmPromotionTagProductModel();
        parameter.setCmsBtTagId(tagId);
        List<CmsBtJmPromotionTagProductModel> jmPromotionTagProductModelList = daoCmsBtJmPromotionTagProduct.selectList(parameter);
        if (jmPromotionTagProductModelList.isEmpty())
            return new ArrayList<>(0);

        return jmPromotionTagProductModelList
                .stream()
                .map(jmPromotionTagProductModel -> dao.select(jmPromotionTagProductModel.getCmsBtJmPromotionProductId()))
                .collect(toList());
    }

    /**
     * 上传更新聚美平台发送消息
     *
     * @param cmsBtJmPromotionId
     * @param sender
     */
    public void sendMessage(int cmsBtJmPromotionId, String sender, String channelId) {
        JMProductUpdateMQMessageBody mqMessageBody = new JMProductUpdateMQMessageBody();
        mqMessageBody.setChannelId(channelId);
        mqMessageBody.setCmsBtJmPromotionId(cmsBtJmPromotionId);
        mqMessageBody.setSender(sender);
        cmsMqSenderService.sendMessage(mqMessageBody);
    }


    /** 库存同步
     * @param sender
     */
    public void sendMessageJmPromotionProductStockSync(String sender) {
        JmPromotionProductStockSyncMQMessageBody mqMessageBody = new JmPromotionProductStockSyncMQMessageBody();
        mqMessageBody.setSender(sender);
        cmsMqSenderService.sendMessage(mqMessageBody);
    }
}

