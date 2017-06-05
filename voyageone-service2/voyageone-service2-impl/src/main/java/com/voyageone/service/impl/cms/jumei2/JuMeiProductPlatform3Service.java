package com.voyageone.service.impl.cms.jumei2;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.*;
import com.voyageone.components.jumei.JumeiHtDealService;
import com.voyageone.components.jumei.JumeiHtMallService;
import com.voyageone.components.jumei.bean.*;
import com.voyageone.components.jumei.reponse.*;
import com.voyageone.components.jumei.request.*;
import com.voyageone.service.bean.cms.OperationResult;
import com.voyageone.service.bean.cms.jumei.SkuPriceBean;
import com.voyageone.service.bean.cms.jumei.UpdateJmParameter;
import com.voyageone.service.dao.cms.CmsBtJmPromotionDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.dao.wms.WmsBtInventoryCenterLogicDao;
import com.voyageone.service.daoext.cms.CmsBtJmProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionSkuDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CmsBtBrandBlockService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.jumei.JMShopBeanService;
import com.voyageone.service.impl.cms.product.ProductPlatformService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

//import com.voyageone.service.impl.cms.jumei.platform.JMShopBeanService;
//import com.voyageone.service.impl.cms.jumei.platform.JuMeiProductAddPlatformService;

/**
 * Created by dell on 2016/4/19.
 */
@Service
public class JuMeiProductPlatform3Service extends BaseService {
    private static final Logger LOG = LoggerFactory.getLogger(JuMeiProductPlatform3Service.class);
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
    @Autowired
    ProductService productService;
    @Autowired
    FeedInfoService feedInfoService;
    @Autowired
    CmsBtBrandBlockService CmsBtBrandBlockService;
    @Autowired
    JumeiHtMallService jumeiHtMallService;
    @Autowired
    ProductPlatformService productPlatformService;
    @Autowired
    WmsBtInventoryCenterLogicDao wmsBtInventoryCenterLogicDao;
    @Autowired
    SxProductService sxProductService;
    private List<String> newJmDealSkuNoList = new ArrayList<>();

    public  List<OperationResult> updateJmByPromotionId(int promotionId) {
        List<OperationResult> listOperationResult = new ArrayList<>();
        HashMap<String, Boolean> mapMasterBrand = new HashMap<>();//
        CmsBtJmPromotionModel modelCmsBtJmPromotion = daoCmsBtJmPromotion.select(promotionId);
        long activityBeginTime = DateTimeUtilBeijing.toLocalTime(modelCmsBtJmPromotion.getActivityStart());//北京时间转本地时区时间戳
        boolean isBegin = activityBeginTime < new Date().getTime();//活动是否看开始
        ShopBean shopBean = serviceJMShopBean.getShopBean(modelCmsBtJmPromotion.getChannelId());
        $info(promotionId + " 聚美上新开始");
        List<CmsBtJmPromotionProductModel> listCmsBtJmPromotionProductModel = daoExtCmsBtJmPromotionProduct.selectJMCopyList(promotionId);

        List<String> productCodes = listCmsBtJmPromotionProductModel.stream().map((productModel) -> String.valueOf(productModel.getProductCode())).collect(Collectors.toList());

        // 获取产品信息
        JongoQuery query = new JongoQuery();
        query.setQuery("{\"common.fields.code\": {$in: #}}");
        query.setParameters(productCodes);
        List<CmsBtProductModel> productMongos = productService.getList(modelCmsBtJmPromotion.getChannelId(), query);

        // 获取活动中sku列表
        List<Map<String, Object>> listSku = daoExtCmsBtJmPromotionSku.selectExportListByPromotionId(promotionId, productCodes);

        // 获取product中目前有效销售的sku
        Map<String, List<jmHtDealCopyDealSkusData>> productSkus = new HashMap<>();
        Map<String, List<String>> stockProducts = new HashMap<>();
        List<String> stockSkus = new ArrayList<>();
        productMongos.forEach((product) -> {

            // 按原始channel分组productCode,方便库存同步调用,因为库存同步按原始channel来同步库存
            if (stockProducts.get(product.getOrgChannelId()) != null) {
                stockProducts.get(product.getOrgChannelId()).add(product.getCommon().getFields().getCode());
            } else {
                List<String> tempProductCodes = new ArrayList<>();
                tempProductCodes.add(product.getCommon().getFields().getCode());
                stockProducts.put(product.getOrgChannelId(), tempProductCodes);
            }

//            List<String> newJmDealSkuNoList = new ArrayList<>();
            try {
                HtDealGetDealByHashIDRequest getDealByHashIDRequest = new HtDealGetDealByHashIDRequest();
                getDealByHashIDRequest.setJumei_hash_id(product.getPlatform(27).getpNumIId());
                getDealByHashIDRequest.setFields("start_time,end_time,deal_status,product_id,sku_list");
                HtDealGetDealByHashIDResponse getDealByHashIDResponse = serviceJumeiHtDeal.getDealByHashID(shopBean, getDealByHashIDRequest);
                newJmDealSkuNoList = getDealByHashIDResponse.getSkuList().stream().map( (skuInfo) -> skuInfo.get("sku_no").toString()).collect(Collectors.toList());
            } catch (Exception ex) {
                ex.printStackTrace();
                OperationResult result=new OperationResult();
                result.setMsg(ex.getMessage());
                result.setCode(product.getCommon().getFields().getCode());
                result.setResult(false);
                //加入错误列表
                listOperationResult.add(result);
            }

            // 取得逻辑库存
            Map<String, String> queryMap = new HashMap<>();
            queryMap.put("channelId", product.getOrgChannelId());
            queryMap.put("code", product.getCommon().getFields().getCode());
//            List<WmsBtInventoryCenterLogicModel> inventoryList = wmsBtInventoryCenterLogicDao.selectItemDetailByCode(queryMap);

            List<jmHtDealCopyDealSkusData> skuList = new ArrayList<>();
            product.getPlatform(27).getSkus()
                    .forEach((skuInfo) -> {
                        String skuCode = skuInfo.getStringAttribute("skuCode");
                        String jmSkuNo = skuInfo.getStringAttribute("jmSkuNo");
                        if (newJmDealSkuNoList.contains(jmSkuNo)) {
                            stockSkus.add(skuCode);

                            jmHtDealCopyDealSkusData dealCopyDealSkuData = new jmHtDealCopyDealSkusData();

                            if (Boolean.valueOf(skuInfo.getStringAttribute("isSale"))) {

                                List<Object> promotionSkuList = listSku.stream()
                                        .filter((promotionSku) -> skuCode.equals(String.valueOf(promotionSku.get("skuCode"))))
                                        .collect(Collectors.toList());

                                if (promotionSkuList.size() <= 0)
                                    return;

                                Map<String, String> promotionSkuMap = (Map<String, String>) promotionSkuList.get(0);

                                if (!StringUtil.isEmpty(promotionSkuMap.get("jmSkuNo"))) {

                                    dealCopyDealSkuData.setStocks(String.valueOf("1"));
                                    if (skuInfo.getIntAttribute("qty") > 0) {
                                        dealCopyDealSkuData.setStocks(skuInfo.getStringAttribute("qty"));
                                    }
                                    dealCopyDealSkuData.setSku_no(String.valueOf(promotionSkuMap.get("jmSkuNo")));
                                    dealCopyDealSkuData.setDeal_price(String.valueOf(promotionSkuMap.get("dealPrice")));
                                    dealCopyDealSkuData.setMarket_price(String.valueOf(promotionSkuMap.get("marketPrice")));
                                    skuList.add(dealCopyDealSkuData);
                                }
                            } else {
                                dealCopyDealSkuData.setStocks("1");
                                dealCopyDealSkuData.setSku_no(jmSkuNo);
                                dealCopyDealSkuData.setDeal_price(skuInfo.getStringAttribute("priceSale"));
                                dealCopyDealSkuData.setMarket_price(skuInfo.getStringAttribute("priceMsrp"));
                                skuList.add(dealCopyDealSkuData);
                            }
                        }
                    });

            if (skuList.size() > 0)
                productSkus.put(product.getCommon().getFields().getCode(), skuList);
        });

        for (CmsBtJmPromotionProductModel model : listCmsBtJmPromotionProductModel) {

            $debug(promotionId + " code:" + model.getProductCode() + "上新begin");
            OperationResult result = updateJm(modelCmsBtJmPromotion, model, shopBean, mapMasterBrand, isBegin, productSkus.get(model.getProductCode()));

            $debug(promotionId + " code:" + model.getProductCode() + "上新end");
            listOperationResult.add(result);
        }

        // 发送库存同步信息
        stockProducts.forEach((orgChannelId, products) -> {
            try {
                // 每100个调用一次库存同步
                List<List<String>> skuCodeList = CommonUtil.splitList(stockSkus, 100);
                for (List<String> skus : skuCodeList) {
                    $info("发送sku库存同步请求:" + skus);
                    Map<String, Object> result = sxProductService.synInventoryToPlatform(orgChannelId, "27", null, skus);

                    // 同步库存失败结果返回
                    ((ArrayList<String>) result.get("errorCodeList")).forEach(code -> {
                        OperationResult oResult = new OperationResult();
                        oResult.setResult(false);
                        oResult.setMsg(String.valueOf(result.get("errorMsg")));
                        oResult.setCode(code);
                        listOperationResult.add(oResult);
                    });
                }
            } catch (IOException ex) {
                $error("聚美活动上传同步平台库存调用失败");
                ex.printStackTrace();
                throw new BusinessException("聚美活动上传同步平台库存调用失败");
            }
        });

        mapMasterBrand.clear();

        $info(promotionId + " 聚美上新end");
        return listOperationResult;
    }

   protected  UpdateJmParameter getUpdateJmParameter(CmsBtJmPromotionModel modelCmsBtJmPromotion,CmsBtJmPromotionProductModel model, ShopBean shopBean) {
       UpdateJmParameter parameter = new UpdateJmParameter();
       parameter.cmsBtJmPromotionProductModel = model;
       parameter.cmsBtJmPromotionModel = modelCmsBtJmPromotion;
       parameter.shopBean = shopBean;
       parameter.cmsBtProductModel = productService.getProductByCode(model.getChannelId(), model.getProductCode());//todo cmsBtProductModel null处理11
       if(parameter.cmsBtProductModel==null){throw new  BusinessException("CmsBtProduct商品信息不存在.");}
       parameter.platform=parameter.cmsBtProductModel.getPlatform(CartEnums.Cart.JM);//todo  platform null处理11
       if(parameter.platform==null){throw new  BusinessException("CmsBtProduct商品聚美信息不存在.");}
       return parameter;
   }
    public OperationResult updateJm(CmsBtJmPromotionModel modelCmsBtJmPromotion, CmsBtJmPromotionProductModel cmsBtJmPromotionProductModel
            , ShopBean shopBean, HashMap<String, Boolean> mapMasterBrand, boolean isBegin, List<jmHtDealCopyDealSkusData> dealSkuList) {
        OperationResult result=new OperationResult();
        try {

            UpdateJmParameter parameter = getUpdateJmParameter(modelCmsBtJmPromotion, cmsBtJmPromotionProductModel, shopBean);

            parameter.setBegin(isBegin);//活动是否开始
            api_beforeCheck(parameter,mapMasterBrand);//api调用前check
            if (parameter.cmsBtJmPromotionProductModel.getSynchStatus() != 2) {
                // 再售
                if (StringUtil.isEmpty(parameter.cmsBtJmPromotionProductModel.getJmHashId())) {
                    //6.1.1再售接口前check   copyDeal_beforeCheck(4.1)
                   // copyDeal_beforeCheck(parameter);
                    //6.1.2调用再售接口  copyDeal
                    copyDeal(parameter, dealSkuList);
                    //6.1.3再售接口后check   在方法copyDeal内部调用copyDeal_afterCheck(4.2)
                } else {
                    parameter.cmsBtJmPromotionProductModel.setStockStatus(1);//库存设置待更新
                    parameter.cmsBtJmPromotionProductModel.setSynchStatus(2);//有jmHashId 已上传
                }
                setOriginJmHashId(parameter);
                parameter.cmsBtJmPromotionProductModel.setPriceStatus(1);
                // 更新jm价格 限购limit 库存 延期
                updateJMDeal(parameter);
            } else {
                // 更新jm价格 限购limit 库存 延期
                updateJMDeal(parameter);
            }
        } catch (Exception ex) {
            cmsBtJmPromotionProductModel.setErrorMsg(ex.getMessage());
            // model.setUpdateState(EnumJuMeiUpdateState.Error.getId());//同步更新失败
            $error("JuMeiProductPlatform3Service.addProductAndDealByPromotionId " + result.getId(), ex);
            result.setMsg(ex.getMessage());
            result.setCode(cmsBtJmPromotionProductModel.getProductCode());
            result.setResult(false);
            try {
                if (cmsBtJmPromotionProductModel.getErrorMsg().length() > 600) {
                    cmsBtJmPromotionProductModel.setErrorMsg(cmsBtJmPromotionProductModel.getErrorMsg().substring(0, 600));
                }
                daoCmsBtJmPromotionProduct.update(cmsBtJmPromotionProductModel);

            } catch (Exception cex) {
                $error("JuMeiProductPlatform3Service.addProductAndDealByPromotionId", cex);
            }
        }
        return  result;
    }
    public  void  setOriginJmHashId( UpdateJmParameter parameter)
    {
        //todo  逻辑待定最新jmHashId(取该商品的最新jmHashId,这样最靠谱)
        parameter.platform.setpNumIId(parameter.cmsBtJmPromotionProductModel.getJmHashId());
        // 回写jmHashId,不需要做任何上新逻辑
        productPlatformService.updateProductPlatformNoSx(parameter.cmsBtProductModel.getChannelId(), parameter.cmsBtProductModel.getProdId(), parameter.platform, parameter.cmsBtJmPromotionProductModel.getModifier(), "回写最新聚美HashId", false);
    }
    //所有api调用前check
    public void api_beforeCheck(UpdateJmParameter parameter, HashMap<String,Boolean> mapMasterBrand) {
        String errorMsg = "";
        String platformBrandId = parameter.platform.getpBrandId();
        String masterBrand = parameter.cmsBtProductModel.getCommon().getFields().getBrand();
        LOG.info("jm黑名单:begin");
        if (mapMasterBrand.containsKey(masterBrand)) {
            if (mapMasterBrand.get(masterBrand)) {
                errorMsg = "该商品品牌已在黑名单,操作失败";
            }
        } else {
            CmsBtFeedInfoModel cmsBtFeedInfoModel = feedInfoService.getProductByCode(parameter.cmsBtProductModel.getChannelId(), parameter.cmsBtProductModel.getCommon().getFields().getCode());
            if(cmsBtFeedInfoModel != null) {
                String feedBrand = cmsBtFeedInfoModel.getBrand();
                LOG.info(String.format("begin ChannelId:%s,cartId:%s,feedBrand:%s,masterBrand:%s,platformBrandId:%s", parameter.cmsBtProductModel.getChannelId(), 27, feedBrand, masterBrand, platformBrandId));
                if (CmsBtBrandBlockService.isBlocked(parameter.cmsBtProductModel.getChannelId(), 27, feedBrand, masterBrand, platformBrandId)) {
                    errorMsg = "该商品品牌已在黑名单,操作失败";
                    mapMasterBrand.put(masterBrand, true);
                } else {
                    mapMasterBrand.put(masterBrand, false);
                }
                LOG.info("end " + masterBrand + ":" + mapMasterBrand.get(masterBrand));
            }
        }
        if (!StringUtils.isEmpty(errorMsg)) {

        }
        // 6.0.1
        else if ("1".equalsIgnoreCase(parameter.cmsBtProductModel.getLock()))//1:lock, 0:unLock
        {
            errorMsg = "商品被Lock，如确实需要上传商品，请先解锁";
        }
        //parameter.platform.get
        //6.0.2
        else if (parameter.cmsBtJmPromotionProductModel.getDealPrice().doubleValue() > parameter.cmsBtJmPromotionProductModel.getMarketPrice().doubleValue()) {
            errorMsg = "市场价必须大于团购价";
        }
        if (!StringUtils.isEmpty(errorMsg)) {
            if (parameter.cmsBtJmPromotionProductModel.getSynchStatus() != 2) {
                parameter.cmsBtJmPromotionProductModel.setSynchStatus(3);
            }
            else if(parameter.cmsBtJmPromotionProductModel.getDealEndTimeStatus()==1)
            {
                parameter.cmsBtJmPromotionProductModel.setDealEndTimeStatus(3);
            }
            else if(parameter.cmsBtJmPromotionProductModel.getPriceStatus()==1)
            {
                parameter.cmsBtJmPromotionProductModel.setPriceStatus(3);
            }
            throw new BusinessException(errorMsg);
        }
    }
    void updateJMDeal( UpdateJmParameter parameter) throws Exception {
        if (parameter.cmsBtJmPromotionProductModel.getPriceStatus() == 1) //更新价格
        {
            updateDeal(parameter);//更新deal   商品属性 价格
        }
        if (parameter.cmsBtJmPromotionProductModel.getDealEndTimeStatus() == 1) {
            updateDealEndTime(parameter);//deal延期
        }
        if (parameter.cmsBtJmPromotionProductModel.getStockStatus() != 2) {
            updateDealStock(parameter);//更新库存
        }

    }
    //再售前check
    public  void copyDeal_beforeCheck(UpdateJmParameter parameter) {
        CmsBtJmPromotionProductModel model = parameter.cmsBtJmPromotionProductModel;
        String errorMsg = "";
        if (parameter.cmsBtJmPromotionModel.getIsPromotionFullMinus())//满减专场
        {
            CmsBtJmPromotionProductModel modelPromotionProduct = daoExtCmsBtJmPromotionProduct.selectDateRepeatByCode(model.getCmsBtJmPromotionId(), model.getChannelId(), model.getProductCode(), model.getActivityStart(), model.getActivityEnd());
            if (modelPromotionProduct != null) { //活动日期重叠
                errorMsg = "该商品已于相关时间段内，在其它专场中完成上传，为避免财务结算问题，请放弃导入,JmPromotionId:" + modelPromotionProduct.getCmsBtJmPromotionId() + "存在该商品";//取一个活动id
            }
        } else { //非满减专场 非大促
            CmsBtJmPromotionProductModel modelPromotionProduct = daoExtCmsBtJmPromotionProduct.selectFullMinusDateRepeat(model.getCmsBtJmPromotionId(), model.getChannelId(), model.getProductCode(), model.getActivityStart(), model.getActivityEnd());
            if (modelPromotionProduct != null) { //活动日期重叠
                errorMsg = "该商品已在该大促时间范围内的其它未过期聚美专场中，完成上传，且开始时间与大促开始时间不一致。无法加入当前大促专场。聚美会监控大促专场的营销数据，禁止商品在活动启动前偷跑，大促商品必须有预热。请放弃导入,JmPromotionId:" + modelPromotionProduct.getCmsBtJmPromotionId() + "存在该商品";//取一个活动id

            }
        }
        if (StringUtils.isEmpty(errorMsg) && parameter.cmsBtJmPromotionModel.getPromotionType() == 2)//大促专场
        {
            CmsBtJmPromotionProductModel modelPromotionProduct = daoExtCmsBtJmPromotionProduct.selectDateRepeatByCode(model.getCmsBtJmPromotionId(), model.getChannelId(), model.getProductCode(), model.getActivityStart(), model.getActivityEnd());
            if (modelPromotionProduct != null && modelPromotionProduct.getActivityStart() != model.getActivityStart()) { //活动日期重叠 开始时间不相等
                errorMsg = "该商品已于相关时间段内，在其它专场中完成上传，为避免财务结算问题，请放弃导入,JmPromotionId:" + modelPromotionProduct.getCmsBtJmPromotionId() + "存在该商品";//取一个活动id

            }
        }
        if(!StringUtils.isEmpty(errorMsg))
        {
          throw  new BusinessException(errorMsg);
        }
    }

    public CmsBtJmPromotionModel getCmsBtJmPromotionModelBySellHashId(String SellHashId) {
        Map<String, Object> map = new HashMap<>();
        map.put("jmHashId", SellHashId);
        CmsBtJmPromotionProductModel cmsBtJmPromotionProductModel = daoCmsBtJmPromotionProduct.selectOne(map);
        if(cmsBtJmPromotionProductModel==null) return  null;
        return daoCmsBtJmPromotion.select(cmsBtJmPromotionProductModel.getCmsBtJmPromotionId());
    }
    //
    private void updateDealEndTime(UpdateJmParameter parameter) throws Exception {
        CmsBtJmPromotionModel modelCmsBtJmPromotion=parameter.cmsBtJmPromotionModel;
        CmsBtJmPromotionProductModel model=parameter.cmsBtJmPromotionProductModel;
        ShopBean shopBean=parameter.shopBean;
        if (modelCmsBtJmPromotion.getActivityEnd().getTime() > model.getActivityEnd().getTime()) {
            jmHtDealUpdateDealEndTime(modelCmsBtJmPromotion, model, shopBean);
            model.setDealEndTimeStatus(2);
            model.setErrorMsg("");
            model.setActivityEnd(modelCmsBtJmPromotion.getActivityEnd());
            daoCmsBtJmPromotionProduct.update(model);
            LOG.info("延期成功:"+parameter.cmsBtJmPromotionProductModel.getProductCode());
        }
    }

    private void updateDeal(UpdateJmParameter parameter) throws Exception {
        CmsBtJmPromotionProductModel model=parameter.cmsBtJmPromotionProductModel;
        ShopBean shopBean=parameter.shopBean;
        List<SkuPriceBean> listSkuPrice = daoExtCmsBtJmPromotionSku.selectJmSkuPriceInfoListByPromotionProductId(model.getId());
        jmHtDealUpdateDealPriceBatch(model, shopBean, listSkuPrice);//更新deal价格
        if(parameter.isBegin) {
            //活动开始 更新mallPrice 价格
            jmhtMall_UpdateMallPriceBatch(model, shopBean, listSkuPrice);//更新商城价格
        }
        //
        String jmSkuNoList = getjmSkuNo(listSkuPrice);
        jmHtDealUpdate(model, shopBean, jmSkuNoList);//更新deal信息   limit   jmSkuNo
        model.setPriceStatus(2);
        if (model.getUpdateStatus() == 1) {
            model.setUpdateStatus(2);//价格和商品属性都更新成功后 设置为已变更
        }
        model.setErrorMsg("");
        daoCmsBtJmPromotionProduct.update(model);
    }
    private void updateDealStock(UpdateJmParameter parameter) throws Exception {
        CmsBtJmPromotionProductModel model=parameter.cmsBtJmPromotionProductModel;
        ShopBean shopBean=parameter.shopBean;
        List<SkuPriceBean> listSkuPrice = daoExtCmsBtJmPromotionSku.selectJmSkuPriceInfoListByPromotionProductId(model.getId());
        jmHtDealUpdateDealStockBatch(model, shopBean, listSkuPrice);//更新deal信息   limit   jmSkuNo
        model.setStockStatus(2);
        model.setErrorMsg("");
        daoCmsBtJmPromotionProduct.update(model);
    }
    //再售
    private void copyDeal( UpdateJmParameter parameter, List<jmHtDealCopyDealSkusData> dealSkuList) throws Exception {
        CmsBtJmPromotionModel modelCmsBtJmPromotion = parameter.cmsBtJmPromotionModel;
        CmsBtJmPromotionProductModel model = parameter.cmsBtJmPromotionProductModel;
        ShopBean shopBean = parameter.shopBean;
       // CmsBtJmProductModel modelJmProduct = daoExtCmsBtJmProduct.selectByProductCodeChannelId(model.getProductCode(), model.getChannelId());
        jmHtDealCopy(parameter,parameter.platform.getpNumIId(), dealSkuList);//再售
        model.setActivityStart(modelCmsBtJmPromotion.getActivityStart());
        model.setActivityEnd(modelCmsBtJmPromotion.getActivityEnd());
        model.setSynchStatus(2);
        model.setStockStatus(1);//库存设置待更新
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
    private void jmHtDealCopy(UpdateJmParameter parameter, String originJmHashId, List<jmHtDealCopyDealSkusData> dealSkuList) throws Exception {
        HtDealCopyDealRequest request = new HtDealCopyDealRequest();
        request.setStart_time(parameter.cmsBtJmPromotionModel.getActivityStart());
        request.setEnd_time(parameter.cmsBtJmPromotionModel.getActivityEnd());
        request.setJumei_hash_id(originJmHashId);//原始jmHashId
        if (dealSkuList != null)
            request.setSkus_data(dealSkuList);
        else
            request.setSkus_data(new ArrayList<jmHtDealCopyDealSkusData>());
        try {
            HtDealCopyDealResponse response = serviceJumeiHtDeal.copyDeal(parameter.shopBean, request);
            // error_code":100015的场合先做一下商城价格同步再做再售
            if(response.getBody() != null && response.getBody().indexOf("100015")>-1){
                $info("100015 重新刷商城价格");
                StringBuffer failCause = new StringBuffer();
                List<HtMallSkuPriceUpdateInfo> htMallSkuPriceUpdateInfos = new ArrayList<>(dealSkuList.size());
                dealSkuList.forEach(dealSku->{
                    HtMallSkuPriceUpdateInfo htMallSkuPriceUpdateInfo = new HtMallSkuPriceUpdateInfo();
                    // 因为商城价格不允许阶梯价格所以就用第一个sku的价格作为商城价格
                    htMallSkuPriceUpdateInfo.setMarket_price(Double.parseDouble(dealSkuList.get(0).getMarket_price()));
                    htMallSkuPriceUpdateInfo.setMall_price(Double.parseDouble(dealSkuList.get(0).getDeal_price()));
                    htMallSkuPriceUpdateInfo.setJumei_sku_no(dealSku.getSku_no());
                    htMallSkuPriceUpdateInfos.add(htMallSkuPriceUpdateInfo);
                });
                boolean isSuccess = jumeiHtMallService.updateMallSkuPrice(parameter.shopBean, htMallSkuPriceUpdateInfos, failCause);
                if(!isSuccess){
                    throw new BusinessException("商城价格刷新失败" + failCause.toString());
                }else{
                    // 商城价格成功后再做再售处理
                    response = serviceJumeiHtDeal.copyDeal(parameter.shopBean, request);
                }
            }
            if (response.is_Success()) {
                parameter.cmsBtJmPromotionProductModel.setJmHashId(response.getJumei_hash_id());
            } else {
                //再售后check
                String errorMsg = copyDeal_afteErrorCheck(parameter, response);
                if(!StringUtils.isEmpty(parameter.cmsBtJmPromotionProductModel.getJmHashId()))
                {
                    setOriginJmHashId(parameter);
                }
                throw new BusinessException(errorMsg + response.getBody());
            }
        } catch (Exception ex) {
            parameter.cmsBtJmPromotionProductModel.setSynchStatus(3);
            parameter.cmsBtJmPromotionProductModel.setPriceStatus(0);
            if(parameter.cmsBtJmPromotionProductModel.getDealEndTimeStatus()!=2) {
                parameter.cmsBtJmPromotionProductModel.setDealEndTimeStatus(0);
            }
            throw ex;
        }
    }
    //再售后check
    public  String copyDeal_afteErrorCheck(UpdateJmParameter parameter,HtDealCopyDealResponse response) throws Exception {

        CmsBtJmPromotionModel jmPromotion = parameter.cmsBtJmPromotionModel;
        CmsBtJmPromotionProductModel jmPromotionProduct = parameter.cmsBtJmPromotionProductModel;
        String errorMsg = "";
        if (!StringUtils.isEmpty(response.getJumei_hash_id())) {
            parameter.cmsBtJmPromotionProductModel.setJmHashId(response.getJumei_hash_id());
        } else if (StringUtils.isEmpty(response.getSell_hash_id())) {//sell_hash_id 不存在
            errorMsg = "请登录聚美后台，检查商品相关【产品库】与【Deal】信息，如发现“待审核”、“审核通过”、“取消送审”字样，请将相关 聚美产品ID，聚美产品品牌，聚美HashID申报给聚美运营，进行人工审核并发布.";
            return errorMsg;
        } else {
            HtDealGetDealByHashIDRequest getDealByHashIDRequest = new HtDealGetDealByHashIDRequest();
            getDealByHashIDRequest.setJumei_hash_id(response.getSell_hash_id());
            HtDealGetDealByHashIDResponse getDealByHashIDResponse = serviceJumeiHtDeal.getDealByHashID(parameter.shopBean, getDealByHashIDRequest);
            String sell_hash_id = response.getSell_hash_id();
            long jmActivityStartTime = DateTimeUtilBeijing.toLocalTime(parameter.cmsBtJmPromotionModel.getActivityStart());
            long jmActivityEndTime = DateTimeUtilBeijing.toLocalTime(parameter.cmsBtJmPromotionModel.getActivityEnd());
            long sellJmEndTime = DateTimeUtilBeijing.toLocalTime(getDealByHashIDResponse.getEnd_time());
            long sellJmStartTime = DateTimeUtilBeijing.toLocalTime(getDealByHashIDResponse.getStart_time());
            LOG.info("sellJmStartTime:"+getDealByHashIDResponse.getStart_time().toString());
            LOG.info("sellJmEndTime:"+getDealByHashIDResponse.getEnd_time().toString());
            CmsBtJmPromotionModel sellJmPromotion = getCmsBtJmPromotionModelBySellHashId(response.getSell_hash_id());
            String sellJmPromotionName="";
                    if(sellJmPromotion!=null) {
                        sellJmPromotionName =sellJmPromotion.getName();
                    }
                    else
                    {
                        sellJmPromotionName="Sell_hash_id:"+response.getSell_hash_id();
                    }
            if(sellJmPromotion==null)
            {
                if (sellJmStartTime == jmActivityStartTime)//开始时间相等
                {
                    jmPromotionProduct.setJmHashId(sell_hash_id);//设置当前专场jmHashId
                    if (sellJmEndTime < jmActivityEndTime)//【sell_hash_id】的结束时间小于当前专场结束时间的场合
                    {//调用延迟Deal结束时间API
                        jmPromotionProduct.setDealEndTimeStatus(1);//设置为待延期
                    }
                    errorMsg = String.format("该商品已加入专场【%s】，并上传成功。介于开始时间相同，并进行了延期。如需变更价格，请重新点击【重刷】/【批量同步价格】。操作将影响关联专场，请慎重", sell_hash_id);
                    return errorMsg;
                } else {
                    errorMsg = String.format("存在不属于任何专场的有效聚美【%s】，开始时间【%s】，结束时间【%s】，请确认其所属专场，并告知技术部以进行数据处理。", sell_hash_id, getDealByHashIDResponse.getStart_time(), getDealByHashIDResponse.getEnd_time());
                    return errorMsg;
                }
            }
            //1017 3
//            if (jmPromotion.getIsPromotionFullMinus())//当前专场为 满减专场
//            { //4.2.3
//                errorMsg = String.format("该商品已加入专场【%s】，并上传成功。满减专场不可与其它专场共用商品，请于专场【%s】结束后，再进行上传", sellJmPromotionName, sellJmPromotionName);
//                return errorMsg;
//            }

            //1017  4
//            if (!jmPromotion.getIsPromotionFullMinus())//当前专场为 非满减专场
//            { //4.2.4
//                if (sellJmPromotion.getIsPromotionFullMinus())//在售专场为 满减专场
//                {
//                    errorMsg = String.format("该商品已加入满减专场【%s】，并上传成功。满减专场不可与其它专场共用商品，请于专场【%s】结束后，再进行上传.", sellJmPromotionName, sellJmPromotionName);
//                    return errorMsg;
//                }
//            }
            // 4.2.5


            //1017 3.3 if (jmPromotion.getPromotionType() == 2 && !jmPromotion.getIsPromotionFullMinus())//当前专场为 大促非满减专场
//            {
//                if (!sellJmPromotion.getIsPromotionFullMinus())//在售专场为 非满减专场
//                {
                    if (sellJmStartTime == jmActivityStartTime)//开始时间相等
                    {
                        jmPromotionProduct.setJmHashId(sell_hash_id);//设置当前专场jmHashId
                        if (sellJmEndTime < jmActivityEndTime)//【sell_hash_id】的结束时间小于当前专场结束时间的场合
                        {//调用延迟Deal结束时间API
                            jmPromotionProduct.setDealEndTimeStatus(1);//设置为待延期
                        }
                        errorMsg = String.format("该商品已加入专场【%s】，并上传成功。介于开始时间相同，当前大促专场引用了同一HashID，并进行了延期。如需变更价格，请重新点击【重刷】/【批量同步价格】。操作将影响关联专场，请慎重", sellJmPromotionName);
                        return errorMsg;
                    }
              //  }
         //   }
            //1017 4.2.6
//            if (jmPromotion.getPromotionType() == 2 && !jmPromotion.getIsPromotionFullMinus())//当前专场为  大促非满减专场
//            {
//                if (sellJmStartTime != jmActivityStartTime)//开始时间不相等
//                {
//                    errorMsg = String.format("该商品已加入专场【%s】，并上传成功。聚美平台监控大促开场，严禁大促专场商品出现时间异常。介于开始时间不相同，该商品已无法在当前大促专场进行售卖，请替换商品.", sellJmPromotionName);
//                    return errorMsg;
//                }
//            }
            //4.2.7
            if (jmPromotion.getPromotionType() != 2 && !jmPromotion.getIsPromotionFullMinus())//当前专场为  非大促 非满减专场
            {
                if (!sellJmPromotion.getIsPromotionFullMinus())//在售专场为 非满减专场
                {
                    if (sellJmStartTime > jmActivityEndTime)//在售专场开始时间>当前专场结束时间
                    {
                        errorMsg = String.format("该商品已加入专场【%s】，并上传成功。介于其有效期为%s】（年月日 时分秒）至【%s】（年月日 时分秒），晚于当前专场，该商品已无法在当前专场进行售卖，请替换商品", sellJmPromotionName, getDealByHashIDResponse.getStart_time(), getDealByHashIDResponse.getEnd_time());
                        return errorMsg;
                    }
                }
            }
            //4.2.8
//            if (jmPromotion.getPromotionType() != 2 && !jmPromotion.getIsPromotionFullMinus())//当前专场为  非大促 非满减专场
//            {
//                if (!sellJmPromotion.getIsPromotionFullMinus())//在售专场为非满减专场
//                {
                    if (DateTimeUtil.addDays(jmPromotion.getPrePeriodStart(), -5).getTime() >= getDealByHashIDResponse.getEnd_time().getTime())//【sell_hash_id】的结束时间早于当前专场结束时间5天或以上 时
                    {
                        errorMsg = String.format("该商品已加入专场%s，并上传成功。请于【%s】（年月日 时分秒）之后，再进行上传", sellJmPromotionName, DateTimeUtil.format(getDealByHashIDResponse.getEnd_time(), "yyyy-MM-dd HH:mm:ss"));
                        return errorMsg;
                    }
             //   }
           // }
            //4.2.9
            if (jmPromotion.getPromotionType() != 2)//当前专场为  非大促
            {
//                if (!sellJmPromotion.getIsPromotionFullMinus())//在售专场为非满减专场
//                {
                    if (DateTimeUtil.addDays(jmPromotion.getPrePeriodStart(), -6).getTime() < getDealByHashIDResponse.getEnd_time().getTime())//【sell_hash_id】的结束时间早于当前专场结束时间5天或以下 时，
                    {
                        //4.2.9.1
                        jmPromotionProduct.setJmHashId(sell_hash_id);//设置当前专场jmHashId
                        errorMsg = String.format("该商品已加入专场【%s】，并上传成功。当前专场引用了同一HashID，并进行了延期。该商品无预热。如需变更价格，请重新点击【重刷】/【批量同步价格】。操作将影响关联专场，请慎重。", sellJmPromotionName);

                        //4.2.9.2 新HashID替换MongoDB中，该商品的Origin HashID 参考步骤6.0.3
                        //4.2.9.2 //在售专场的结束时间小于当前专场结束时间
                        if (sellJmEndTime < jmActivityEndTime) {
                            //调用延迟Deal结束时间API
                            jmPromotionProduct.setDealEndTimeStatus(1);
                            jmPromotionProduct.setActivityEnd(getDealByHashIDResponse.getEnd_time());
                            try {
                                updateDealEndTime(parameter);//自动延期

                            }
                            catch (Exception ex) {
                                errorMsg+=ex.getMessage();
                            }
                        }
                        return errorMsg;
                    }
              //  }
            }
            //4.2.10   新加 1017
            if (jmPromotion.getPromotionType()== 2)//当前专场为  非大促
            {
                if (DateTimeUtil.addDays(jmPromotion.getActivityStart(), -6).getTime() < getDealByHashIDResponse.getEnd_time().getTime())//【sell_hash_id】的结束时间早于当前专场结束时间5天或以下 时，
                {
                    //4.2.9.1
                    jmPromotionProduct.setJmHashId(sell_hash_id);//设置当前专场jmHashId
                    errorMsg = String.format("该商品已加入专场【%s】，并上传成功。当前专场引用了同一HashID，并进行了延期。该商品无预热。如需变更价格，请重新点击【重刷】/【批量同步价格】。操作将影响关联专场，请慎重。介于开始时间不同，聚美平台监控大促开场，请及时联络聚美运营进行Deal时间批量调整慎重。", sellJmPromotionName);

                    //4.2.9.2 新HashID替换MongoDB中，该商品的Origin HashID 参考步骤6.0.3
                    //4.2.9.2 //在售专场的结束时间小于当前专场结束时间
                    if (sellJmEndTime < jmActivityEndTime) {
                        //调用延迟Deal结束时间API
                        jmPromotionProduct.setDealEndTimeStatus(1);
                        jmPromotionProduct.setActivityEnd(getDealByHashIDResponse.getEnd_time());
                        try {
                            updateDealEndTime(parameter);//自动延期
                        }
                        catch (Exception ex) {
                            errorMsg+=ex.getMessage();
                        }
                    }
                    return errorMsg;
                }
            }
        }
        // throw new BusinessException("productCode:" +  parameter.cmsBtJmPromotionProductModel.getProductCode() + "jmHtDealCopyErrorMsg:" + response.getErrorMsg());
        return errorMsg;
    }

    //更新特卖信息
    private void jmHtDealUpdate(CmsBtJmPromotionProductModel model, ShopBean shopBean, String jumei_sku_no) throws Exception {
        HtDealUpdateRequest request = new HtDealUpdateRequest();
        request.setJumei_hash_id(model.getJmHashId());
        HtDealUpdate_DealInfo update_dealInfo = new HtDealUpdate_DealInfo();
        update_dealInfo.setUser_purchase_limit(model.getLimit()); //限购数量
        update_dealInfo.setJumei_sku_no(jumei_sku_no);           //jmskuno
        request.setUpdate_data(update_dealInfo);
        try {
            HtDealUpdateResponse response= serviceJumeiHtDeal.update(shopBean, request);
            if(!response.is_Success())
            {
                model.setPriceStatus(3);
                throw new BusinessException("productId:" + model.getId() + "jmHtDealCopyErrorMsg:" + response.getErrorMsg());
                // $error(response.getBody());
            }
        }
        catch (Exception ex)
        {
            model.setPriceStatus(3);
            throw  ex;
        }

    }
    private void jmHtDealUpdateDealEndTime(CmsBtJmPromotionModel modelCmsBtJmPromotion,CmsBtJmPromotionProductModel model, ShopBean shopBean) throws Exception {

        //设置请求参数
        HtDealUpdateDealEndTimeRequest request = new HtDealUpdateDealEndTimeRequest();
        request.setJumei_hash_id(model.getJmHashId());
        request.setEnd_time(modelCmsBtJmPromotion.getActivityEnd());

        try {
            HtDealUpdateDealEndTimeResponse response = serviceJumeiHtDeal.updateDealEndTime(shopBean, request);
            if (!response.is_Success()) {
                model.setDealEndTimeStatus(3);
                throw new BusinessException("productId:" + model.getId() + "updateDealEndTime:" + response.getErrorMsg());
            }
        }catch (Exception ex)
        {
            model.setDealEndTimeStatus(3);
            throw  ex;
        }
    }
    //更新价格
    private void   jmHtDealUpdateDealPriceBatch(CmsBtJmPromotionProductModel model, ShopBean shopBean, List<SkuPriceBean> listSkuPrice) throws Exception {

        //设置请求参数
        HtDealUpdateDealPriceBatchRequest request = new HtDealUpdateDealPriceBatchRequest();
        HtDeal_UpdateDealPriceBatch_UpdateData updateData = null;
        List<HtDeal_UpdateDealPriceBatch_UpdateData> list = new ArrayList<>();
        for (SkuPriceBean skuPriceBean : listSkuPrice) {
            updateData = new HtDeal_UpdateDealPriceBatch_UpdateData();
            // 如果该sku未上新到平台则不刷新该sku价格
            if (StringUtils.isEmpty(skuPriceBean.getJmSkuNo()))
                continue;

            list.add(updateData);
            updateData.setJumei_hash_id(model.getJmHashId());
            updateData.setJumei_sku_no(skuPriceBean.getJmSkuNo());
            updateData.setDeal_price(skuPriceBean.getDealPrice());
            updateData.setMarket_price(skuPriceBean.getMarketPrice());
        }

        try {

            if (list.size() == 0) {
                throw new BusinessException("productId:" + model.getId() + " jmHtDealUpdateDealPriceBatch:sku未上新或不存在");
            }
            String errorMsg="";
            List<List<HtDeal_UpdateDealPriceBatch_UpdateData>> pageList = CommonUtil.splitList(list,10);
            for(List<HtDeal_UpdateDealPriceBatch_UpdateData> page:pageList) {
                request.setUpdate_data(page);
                $info("jmHtDealUpdateDealPriceBatch:"+ model.getProductCode()+JacksonUtil.bean2Json(request.getUpdate_data()));
                HtDealUpdateDealPriceBatchResponse response = serviceJumeiHtDeal.updateDealPriceBatch(shopBean, request);
                if (!response.is_Success()) {
                    model.setPriceStatus(3);
                  //  throw new BusinessException("productId:" + model.getId() + "jmHtDealCopyErrorMsg:" + response.getErrorMsg());
                    errorMsg+=response.getErrorMsg();
                }
            }
            if(!StringUtil.isEmpty(errorMsg))
            {
                throw new BusinessException("productId:" + model.getId() + "jmHtDealCopyErrorMsg:" + errorMsg);
            }
        }
        catch (Exception ex)
        {
            model.setPriceStatus(3);
            throw  ex;
        }
    }
    //更新价格
    private void   jmhtMall_UpdateMallPriceBatch(CmsBtJmPromotionProductModel model, ShopBean shopBean, List<SkuPriceBean> listSkuPrice) throws Exception {

        List<HtMallSkuPriceUpdateInfo> list = new ArrayList<>();
        HtMallSkuPriceUpdateInfo updateData = null;
        //设置请求参数
        for (SkuPriceBean skuPriceBean : listSkuPrice) {
            updateData = new HtMallSkuPriceUpdateInfo();
            list.add(updateData);
            updateData.setJumei_sku_no(skuPriceBean.getJmSkuNo());
            updateData.setMall_price(skuPriceBean.getDealPrice());
            updateData.setMarket_price(skuPriceBean.getMarketPrice());
        }
        try {
            String errorMsg = "";
            List<List<HtMallSkuPriceUpdateInfo>> pageList = CommonUtil.splitList(list, 10);
            for (List<HtMallSkuPriceUpdateInfo> page : pageList) {
                $info("jmhtMall_UpdateMallPriceBatch :" + model.getProductCode() + JacksonUtil.bean2Json(page));
                StringBuffer sb = new StringBuffer();
                if (!jumeiHtMallService.updateMallSkuPrice(shopBean, page, sb)) {
                    model.setPriceStatus(3);
                    errorMsg += sb.toString();
                }
            }
            if (!StringUtil.isEmpty(errorMsg)) {
                throw new BusinessException("productId:" + model.getId() + "jmHtDealCopyErrorMsg:" + errorMsg);
            }
        } catch (Exception ex) {
            model.setPriceStatus(3);
            throw ex;
        }
    }
    //批量同步deal库存
    private void   jmHtDealUpdateDealStockBatch(CmsBtJmPromotionProductModel model, ShopBean shopBean, List<SkuPriceBean> listSkuPrice) throws Exception {
        //取orgChanelId
        CmsBtProductModel productInfo = productService.getProductByCode(model.getChannelId(), model.getProductCode());
        String orgChanelId=StringUtils.isEmpty(productInfo.getOrgChannelId())?model.getChannelId():productInfo.getOrgChannelId();

        ///取skuCode
        List<String> skuList=listSkuPrice.stream().map(m->m.getSkuCode()).collect(Collectors.toList());

        //取skuCode库存
         Map<String, Integer> skuLogicQtyMap = productService.getLogicQty(orgChanelId,skuList);

        //设置请求参数
        HtDealUpdateDealStockBatchRequest request = new HtDealUpdateDealStockBatchRequest();
        HtDeal_UpdateDealStockBatch_UpdateData updateData = null;
        List<HtDeal_UpdateDealStockBatch_UpdateData> list = new ArrayList<>();
        for (SkuPriceBean skuPriceBean : listSkuPrice) {
            updateData = new HtDeal_UpdateDealStockBatch_UpdateData();
            list.add(updateData);
            updateData.setJumei_sku_no(skuPriceBean.getJmSkuNo());
            Integer stock = skuLogicQtyMap.get(skuPriceBean.getSkuCode());//获取库存
            if(stock!=null) {
                updateData.setStock(stock);
            }
        }

        try {
            String errorMsg="";
            List<List<HtDeal_UpdateDealStockBatch_UpdateData>> pageList = CommonUtil.splitList(list,10);
            for(List<HtDeal_UpdateDealStockBatch_UpdateData> page:pageList) {
                request.setUpdate_data(page);
                $info("jmHtDealUpdateDealStockBatch:"+ model.getProductCode()+JacksonUtil.bean2Json(request.getUpdate_data()));
                HtDealUpdateDealStockBatchResponse response = serviceJumeiHtDeal.updateDealStockBatch(shopBean, request);
                if (!response.is_Success()) {
                    model.setStockStatus(3);
                    //throw new BusinessException("productId:" + model.getId() + "jmHtDealUpdateDealStockBatch:" + response.getErrorMsg());
                    errorMsg+=response.getErrorMsg();
                }
            }
            if(!StringUtil.isEmpty(errorMsg))
            {

                throw new BusinessException("productId:" + model.getId() + "jmHtDealUpdateDealStockBatch:" + errorMsg);
            }
        }
        catch (Exception ex)
        {
            model.setStockStatus(3);
            throw  ex;
        }
    }

    /**
     * 修改聚美下架Deal关联的Sku(下架)
     * 聚美平台上商品如果只剩下一个skuCode,下架时会报异常（每个商品至少要有一个sku），这里直接抛出异常，另外处理
     */
    private OperationResult updateSkuIsEnableDeal(ShopBean shop, String jumeiHashId, String jumeiSkuNo, String code) {

        OperationResult result = new OperationResult();

        HtDealUpdateSkuIsEnableRequest request = new HtDealUpdateSkuIsEnableRequest();
        request.setJumei_hash_id(jumeiHashId);
        request.setJumei_sku_no(jumeiSkuNo);
        request.setIs_enable("0");

        try {
            HtDealUpdateSkuIsEnableResponse response = serviceJumeiHtDeal.updateSkuIsEnable(shop, request);
            if (response != null) {
                $info("聚美上新修改聚美Deal关联的Sku下架 " + response.getBody());
                if (!response.is_Success()) {
                    if (!StringUtils.isEmpty(response.getError_code())
                            && !"100013".equals(response.getError_code())) {
                        $warn("聚美上新修改聚美Deal关联的Sku下架失败! msg=%s", response.getErrorMsg());
                        result.setMsg(String.format("聚美上新修改聚美Deal关联的Sku下架失败! msg=%s", response.getErrorMsg()));
                        result.setCode(code);
                        result.setResult(false);
//                        throw new BusinessException(String.format("聚美上新修改聚美Deal关联的Sku下架失败! msg=%s", response.getErrorMsg()));
                    }
                }
            }
        } catch (Exception e) {
            $error(String.format("聚美上新修改聚美Deal关联的Sku下架 调用聚美API失败 channelId=%s, " + "cartId=%s msg=%s", shop.getOrder_channel_id(), shop.getCart_id(), e.getMessage()), e);
//            throw new BusinessException(String.format("聚美上新修改聚美Deal关联的Sku下架 调用聚美API失败 channelId=%s, " + "cartId=%s msg=%s", shop.getOrder_channel_id(), shop.getCart_id(), e.getMessage()));
            result.setMsg(String.format("聚美上新修改聚美Deal关联的Sku下架 调用聚美API失败 channelId=%s, " + "cartId=%s msg=%s", shop.getOrder_channel_id(), shop.getCart_id(), e.getMessage()));
            result.setCode(code);
            result.setResult(false);
        }

        return result;
    }

}
