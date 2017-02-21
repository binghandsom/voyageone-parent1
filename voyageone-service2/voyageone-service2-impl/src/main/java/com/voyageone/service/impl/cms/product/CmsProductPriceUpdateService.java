package com.voyageone.service.impl.cms.product;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Carts;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchRefreshRetailPriceMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.ProductPriceUpdateMQMessageBody;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 当产品sku的价格变更时，同步至code和group的价格范围, 目前同步中国建议售价、中国指导价和中国最终售价
 * 参数 channelId, prodId, cartId，具体设值参照 CmsBtPriceLogModel
 * 实施方法，不比较输入值和现有值的大小，直接重新计算价格区间
 * @author jiangjusheng on 2016/07/11
 * @version 2.0.0
 */
@Service
public class CmsProductPriceUpdateService extends BaseService {

    @Autowired
    ProductService productService;
    @Autowired
    ProductGroupService productGroupService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;
    @Autowired
    private CmsBtPriceLogService cmsBtPriceLogService;

    /**
     * 更新product及group的价格
     * @param messageBody messageBody
     * @throws Exception Exception
     */
    public void updateProductAndGroupPrice(ProductPriceUpdateMQMessageBody messageBody) throws Exception {

        //$info("参数" + JacksonUtil.bean2Json(messageMap));
        String channelId = StringUtils.trimToNull(messageBody.getChannelId());

        int cartId = messageBody.getCartId();
        Long prodId = messageBody.getProdId();

        $info( String.format("CmsProcductPriceUpdateService start channelId = %s  cartId = %d  prodId = %d",channelId,cartId,prodId));
        JongoQuery queryObj = new JongoQuery();
        queryObj.setQuery("{'prodId':#,'platforms.P#.skus':{$exists:true}}");
        queryObj.setParameters(prodId, cartId);
        queryObj.setProjectionExt("platforms.P" + cartId + ".mainProductCode", "platforms.P" + cartId + ".skus.priceMsrp", "platforms.P" + cartId + ".skus.priceRetail", "platforms.P" + cartId + ".skus.priceSale", "platforms.P" + cartId + ".skus.isSale");
        CmsBtProductModel prodObj = productService.getProductByCondition(channelId, queryObj);
        if (prodObj == null) {
            $error("CmsProcductPriceUpdateService 产品不存在 参数=" + JacksonUtil.bean2Json(messageBody));
            throw new BusinessException(String.format("产品不存在, params=%s", JacksonUtil.bean2Json(messageBody)));
        }

        CmsBtProductModel_Platform_Cart platObj =  prodObj.getPlatform(cartId);
        if (platObj == null) {
            $error("CmsProcductPriceUpdateService 产品数据不正确 参数=" + JacksonUtil.bean2Json(messageBody));
            throw new BusinessException(String.format("产品Platform不存在, prodId=%d, channelId=%s, cartId=%d", prodId, channelId, cartId));
        }
        // 主商品code
        String mProdCode = StringUtils.trimToNull(platObj.getMainProductCode());
        if (mProdCode == null) {
            $error("CmsProcductPriceUpdateService 产品数据不正确 缺少主商品code 参数=" + JacksonUtil.bean2Json(messageBody));
            throw new BusinessException(String.format("产品缺少主商品code prodId=%d, channelId=%s", prodId, channelId));
        }
        List<BaseMongoMap<String, Object>> skuList = platObj.getSkus();
        if (skuList == null || skuList.isEmpty()) {
            $error("CmsProcductPriceUpdateService 产品数据不正确 缺少platforms.skus 参数=" + JacksonUtil.bean2Json(messageBody));
            throw new BusinessException(String.format("产品参数不正确, 缺少platforms.skus prodId=%d, channelId=%s, cartId=%d", prodId, channelId, cartId));
        }
        CmsBtProductGroupModel grpObj = productGroupService.selectMainProductGroupByCode(channelId, mProdCode, cartId);
        if (grpObj == null) {
            $error("CmsProcductPriceUpdateService 产品对应的group不存在 参数=" + JacksonUtil.bean2Json(messageBody));
//            throw new BusinessException(String.format("产品对于的group不存在, mainProductCode=%s, channelId=%s, cartId=%d", mProdCode, channelId, cartId));
        }

        // 先计算价格范围
        List<Double> priceMsrpList = skuList.stream()
                .filter(skuObj -> skuObj.getAttribute("isSale") != null && (boolean)skuObj.getAttribute("isSale"))
                .map(skuObj -> skuObj.getDoubleAttribute("priceMsrp")).sorted().collect(Collectors.toList());
        if (priceMsrpList == null || priceMsrpList.isEmpty()) {
            $error("CmsProcductPriceUpdateService 产品数据sku priceMsrp不正确 参数=" + JacksonUtil.bean2Json(messageBody));
            throw new BusinessException(String.format("产品数据sku priceMsrp不正确, params=%s", JacksonUtil.bean2Json(messageBody)));
        }
        List<Double> priceRetailList = skuList.stream()
                .filter(skuObj -> skuObj.getAttribute("isSale") != null && (boolean)skuObj.getAttribute("isSale"))
                .map(skuObj -> skuObj.getDoubleAttribute("priceRetail")).sorted().collect(Collectors.toList());
        if (priceRetailList == null || priceRetailList.isEmpty()) {
            $error("CmsProcductPriceUpdateService 产品数据sku priceRetail不正确 参数=" + JacksonUtil.bean2Json(messageBody));
            throw new BusinessException(String.format("产品数据sku priceRetail不正确, params=%s", JacksonUtil.bean2Json(messageBody)));
        }
        List<Double> priceSaleList = skuList.stream()
                .filter(skuObj -> skuObj.getAttribute("isSale") != null && (boolean)skuObj.getAttribute("isSale"))
                .map(skuObj -> skuObj.getDoubleAttribute("priceSale")).sorted().collect(Collectors.toList());
        if (priceSaleList == null || priceSaleList.isEmpty()) {
            $error("CmsProcductPriceUpdateService 产品数据sku priceSale不正确 参数=" + JacksonUtil.bean2Json(messageBody));
            throw new BusinessException(String.format("产品数据sku priceSale不正确, params=%s", JacksonUtil.bean2Json(messageBody)));
        }
        double newPriceMsrpSt = priceMsrpList.get(0);
        double newPriceMsrpEd = priceMsrpList.get(priceMsrpList.size() - 1);
        double newPriceRetailSt = priceRetailList.get(0);
        double newPriceRetailEd = priceRetailList.get(priceRetailList.size() - 1);
        double newPriceSaleSt = priceSaleList.get(0);
        double newPriceSaleEd = priceSaleList.get(priceSaleList.size() - 1);

        // 先更新产品platforms价格范围（不更新common中的价格范围）
        JongoUpdate updObj = new JongoUpdate();
        updObj.setQuery("{'prodId':#,'platforms.P#.skus':{$exists:true}}");
        updObj.setQueryParameters(prodId, cartId);
        updObj.setUpdate("{$set:{'platforms.P#.pPriceMsrpSt':#,'platforms.P#.pPriceMsrpEd':#, 'platforms.P#.pPriceRetailSt':#,'platforms.P#.pPriceRetailEd':#, 'platforms.P#.pPriceSaleSt':#,'platforms.P#.pPriceSaleEd':#, 'modified':#,'modifier':#}}");
        updObj.setUpdateParameters(cartId, newPriceMsrpSt, cartId, newPriceMsrpEd, cartId, newPriceRetailSt, cartId, newPriceRetailEd, cartId, newPriceSaleSt, cartId, newPriceSaleEd, DateTimeUtil.getNowTimeStamp(), CmsMqRoutingKey.CMS_BATCH_COUNT_PRODUCT_PRICE);
        WriteResult rs = productService.updateFirstProduct(updObj, channelId);
        $debug("CmsProcductPriceUpdateService 产品platforms价格范围更新结果 " + rs.toString());

        if(grpObj != null) {
            // 然后更新group中的价格范围
            // 先取得group中各code的价格范围
            queryObj = new JongoQuery();
            queryObj.setQuery("{'platforms.P#.mainProductCode':#}");
            queryObj.setParameters(cartId, mProdCode);
            queryObj.setProjectionExt("platforms.P" + cartId + ".pPriceMsrpSt", "platforms.P" + cartId + ".pPriceMsrpEd", "platforms.P" + cartId + ".pPriceRetailSt", "platforms.P" + cartId + ".pPriceRetailEd", "platforms.P" + cartId + ".pPriceSaleSt", "platforms.P" + cartId + ".pPriceSaleEd");
            List<CmsBtProductModel> prodObjList = productService.getList(channelId, queryObj);
            if (prodObj == null || prodObjList.isEmpty()) {
                $error("CmsProcductPriceUpdateService 产品不存在 参数=" + queryObj.toString());
                throw new BusinessException(String.format("产品不存在, params=%s", queryObj.toString()));
            }

            List<Double> priceMsrpStList = prodObjList.stream().map(prodObjItem -> prodObjItem.getPlatformNotNull(cartId).getDoubleAttribute("pPriceMsrpSt")).sorted().collect(Collectors.toList());
            if (priceMsrpStList == null || priceMsrpStList.isEmpty()) {
                $error("CmsProcductPriceUpdateService 产品数据pPriceMsrpSt不正确 参数=" + queryObj.toString());
                throw new BusinessException(String.format("产品数据pPriceMsrpSt不正确, params=%s", queryObj.toString()));
            }
            List<Double> priceMsrpEdList = prodObjList.stream().map(prodObjItem -> prodObjItem.getPlatformNotNull(cartId).getDoubleAttribute("pPriceMsrpEd")).sorted().collect(Collectors.toList());
            if (priceMsrpEdList == null || priceMsrpEdList.isEmpty()) {
                $error("CmsProcductPriceUpdateService 产品数据pPriceMsrpEd不正确 参数=" + queryObj.toString());
                throw new BusinessException(String.format("产品数据pPriceMsrpEd不正确, params=%s", queryObj.toString()));
            }
            List<Double> priceRetailStList = prodObjList.stream().map(prodObjItem -> prodObjItem.getPlatformNotNull(cartId).getDoubleAttribute("pPriceRetailSt")).sorted().collect(Collectors.toList());
            if (priceRetailStList == null || priceRetailStList.isEmpty()) {
                $error("CmsProcductPriceUpdateService 产品数据pPriceRetailSt不正确 参数=" + queryObj.toString());
                throw new BusinessException(String.format("产品数据pPriceRetailSt不正确, params=%s", queryObj.toString()));
            }
            List<Double> priceRetailEdList = prodObjList.stream().map(prodObjItem -> prodObjItem.getPlatformNotNull(cartId).getDoubleAttribute("pPriceRetailEd")).sorted().collect(Collectors.toList());
            if (priceRetailEdList == null || priceRetailEdList.isEmpty()) {
                $error("CmsProcductPriceUpdateService 产品数据pPriceRetailEd不正确 参数=" + queryObj.toString());
                throw new BusinessException(String.format("产品数据pPriceRetailEd不正确, params=%s", queryObj.toString()));
            }
            List<Double> priceSaleStList = prodObjList.stream().map(prodObjItem -> prodObjItem.getPlatformNotNull(cartId).getDoubleAttribute("pPriceSaleSt")).sorted().collect(Collectors.toList());
            if (priceSaleStList == null || priceSaleStList.isEmpty()) {
                $error("CmsProcductPriceUpdateService 产品数据pPriceSaleSt不正确 参数=" + queryObj.toString());
                throw new BusinessException(String.format("产品数据pPriceSaleSt不正确, params=%s", queryObj.toString()));
            }
            List<Double> priceSaleEdList = prodObjList.stream().map(prodObjItem -> prodObjItem.getPlatformNotNull(cartId).getDoubleAttribute("pPriceSaleEd")).sorted().collect(Collectors.toList());
            if (priceSaleEdList == null || priceSaleEdList.isEmpty()) {
                $error("CmsProcductPriceUpdateService 产品数据pPriceSaleEd不正确 参数=" + queryObj.toString());
                throw new BusinessException(String.format("产品数据pPriceSaleEd不正确, params=%s", queryObj.toString()));
            }
            newPriceMsrpSt = priceMsrpStList.get(0);
            newPriceMsrpEd = priceMsrpEdList.get(priceMsrpEdList.size() - 1);
            newPriceRetailSt = priceRetailStList.get(0);
            newPriceRetailEd = priceRetailEdList.get(priceRetailEdList.size() - 1);
            newPriceSaleSt = priceSaleStList.get(0);
            newPriceSaleEd = priceSaleEdList.get(priceSaleEdList.size() - 1);

            // 更新group中的价格范围
            updObj = new JongoUpdate();
            updObj.setQuery("{'mainProductCode':#,'cartId':#}");
            updObj.setQueryParameters(mProdCode, cartId);
            updObj.setUpdate("{$set:{'priceMsrpSt':#,'priceMsrpEd':#, 'priceRetailSt':#,'priceRetailEd':#, 'priceSaleSt':#,'priceSaleEd':#, 'modified':#,'modifier':#}}");
            updObj.setUpdateParameters(newPriceMsrpSt, newPriceMsrpEd, newPriceRetailSt, newPriceRetailEd, newPriceSaleSt, newPriceSaleEd, DateTimeUtil.getNowTimeStamp(), CmsMqRoutingKey.CMS_BATCH_COUNT_PRODUCT_PRICE);

            rs = productGroupService.updateFirst(updObj, messageBody.getChannelId());
            $debug("CmsProcductPriceUpdateService 产品group价格范围更新结果 " + rs.toString());
        }
    }

    /**
     * 更新product的retailPrice
     * @param messageBody messageBody
     */
    public List<CmsBtOperationLogModel_Msg> updateProductRetailPrice(AdvSearchRefreshRetailPriceMQMessageBody messageBody) {
        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();
        String channelId = StringUtils.trimToNull(messageBody.getChannelId());
        String userName = StringUtils.trimToNull(messageBody.getSender());
        List<String> codeList = messageBody.getCodeList();
        Integer cartId = messageBody.getCartId();

        // 是否自动最终售价同步指导价格
        CmsChannelConfigBean autoPriceCfg = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_SALE);
        String msg = "高级检索 重新计算指导售价";
        if (autoPriceCfg != null && "1".equals(autoPriceCfg.getConfigValue1())) {
            // 自动同步
            msg += "(自动同步到最终售价)";
        } else {
            // 不自动同步
            msg += "(未同步到最终售价)";
        }

        JongoQuery queryObj = new JongoQuery();
        JongoUpdate updObj = new JongoUpdate();

        CmsChannelConfigBean autoSyncPricePromotion = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_PROMOTION, cartId.toString());
        if (autoSyncPricePromotion == null) {
            autoSyncPricePromotion = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_PROMOTION);
        }

        if(autoSyncPricePromotion == null){
            autoSyncPricePromotion = new CmsChannelConfigBean();
            autoSyncPricePromotion.setConfigValue1("0");
            autoSyncPricePromotion.setConfigValue2("0");
            autoSyncPricePromotion.setConfigValue3("0");
        }

        ShopBean shopObj = Shops.getShop(channelId, cartId.toString());
        CartBean cartObj = Carts.getCart(cartId);
        if (shopObj == null) {
            throw new BusinessException(String.format("该店铺未配置对应的平台, channelId=%s, cartId=%s", channelId, cartId));
        }

        for (String prodCode : codeList) {
            try {
                $info("channleId=" + channelId + " cartId=" + cartId + " prodCode=" + prodCode);
                queryObj.setQuery("{'common.fields.code':#,'platforms.P#':{$exists:true}}");
                queryObj.setParameters(prodCode, cartId);
//                queryObj.setProjectionExt("prodId", "channelId", "orgChannelId", "platforms.P" + cartId + ".pNumIId", "platforms.P" + cartId + ".status", "platforms.P" + cartId + ".skus", "common.fields", "common.skus");
                CmsBtProductModel prodObj = productService.getProductByCondition(channelId, queryObj);
                if (prodObj == null) {
                    CmsBtOperationLogModel_Msg failInfo = new CmsBtOperationLogModel_Msg();
                    failInfo.setSkuCode(prodCode);
                    failInfo.setMsg(String.format("产品不存在, channelId=%s, code=%s, cartId=%s", channelId, prodCode, cartId));
                    failList.add(failInfo);

                    $warn("CmsProductPriceUpdateService.updateProductRetailPrice 产品不存在 channelId=%s, code=%s, cartId=%d", channelId, prodCode, cartId);
                    continue;
                }
                List<BaseMongoMap<String, Object>> skuList = prodObj.getPlatform(cartId).getSkus();
                if (skuList == null || skuList.isEmpty()) {
                    CmsBtOperationLogModel_Msg failInfo = new CmsBtOperationLogModel_Msg();
                    failInfo.setSkuCode(prodCode);
                    failInfo.setMsg(String.format("产品sku数据不存在, channelId=%s, code=%s, cartId=%s", channelId, prodCode, cartId));
                    failList.add(failInfo);

                    $warn("CmsProductPriceUpdateService.updateProductRetailPrice 产品sku数据不存在 channelId=%s, code=%s, cartId=%d", channelId, prodCode, cartId);
                    continue;
                }

                Integer chg = 0;
                // 计算指导价
                try {
                    if ($isDebugEnabled()) {
                        for (BaseMongoMap skuObj : skuList) {
                            $debug("CmsProductPriceUpdateService.updateProductRetailPrice 计算前的sku价格 skuCode=%s, priceMsrp=%s, priceRetail=%s, priceSale=%s", skuObj.getStringAttribute("skuCode"), skuObj.getDoubleAttribute("priceMsrp"), skuObj.getDoubleAttribute("priceRetail"), skuObj.getDoubleAttribute("priceSale"));
                        }
                    }
                    CmsBtProductModel old = JacksonUtil.json2Bean(JacksonUtil.bean2Json(prodObj),CmsBtProductModel.class);
                    priceService.setPrice(prodObj, cartId, false);
                    // 价格计算前后比较300
                    chg = priceService.skuCompare(old, prodObj, cartId);

                    if ($isDebugEnabled()) {
                        for (BaseMongoMap skuObj : skuList) {
                            $debug("CmsProductPriceUpdateService.updateProductRetailPrice 计算后的sku价格 skuCode=%s, priceMsrp=%s, priceRetail=%s, priceSale=%s", skuObj.getStringAttribute("skuCode"), skuObj.getDoubleAttribute("priceMsrp"), skuObj.getDoubleAttribute("priceRetail"), skuObj.getDoubleAttribute("priceSale"));
                        }
                    }
                } catch (Exception exp) {

                    CmsBtOperationLogModel_Msg failInfo = new CmsBtOperationLogModel_Msg();
                    failInfo.setSkuCode(prodCode);
                    failInfo.setMsg(String.format("调用共通函数priceService.setPrice计算指导价时出错, channelId=%s, code=%s, cartId=%s, errmsg=%s", channelId, prodCode, cartId, exp.getMessage()));
                    failList.add(failInfo);

                    $error(String.format("CmsProductPriceUpdateService.updateProductRetailPrice 调用共通函数计算指导价时出错 channelId=%s, code=%s, cartId=%d, errmsg=%s", channelId, prodCode, cartId, exp.getMessage()), exp);
                    continue;
                }

                // 保存计算结果
                updObj.setQuery("{'common.fields.code':#}");
                updObj.setQueryParameters(prodCode);
                updObj.setUpdate("{$set:{'platforms.P#.skus':#}}");
                updObj.setUpdateParameters(cartId, prodObj.getPlatform(cartId).getSkus());
                WriteResult rs = productService.updateFirstProduct(updObj, channelId);
                $debug("CmsRefreshRetailPriceTask 保存计算结果 " + rs.toString());

                // 记录价格变更履历/同步价格范围
                List<CmsBtPriceLogModel> logModelList = new ArrayList<>(1);
                for (BaseMongoMap skuObj : skuList) {
                    String skuCode = skuObj.getStringAttribute("skuCode");
                    CmsBtPriceLogModel cmsBtPriceLogModel = new CmsBtPriceLogModel();
                    cmsBtPriceLogModel.setChannelId(channelId);
                    cmsBtPriceLogModel.setProductId(prodObj.getProdId().intValue());
                    cmsBtPriceLogModel.setCode(prodCode);
                    cmsBtPriceLogModel.setCartId(cartId);
                    cmsBtPriceLogModel.setSku(skuCode);
                    cmsBtPriceLogModel.setSalePrice(skuObj.getDoubleAttribute("priceSale"));
                    cmsBtPriceLogModel.setMsrpPrice(skuObj.getDoubleAttribute("priceMsrp"));
                    cmsBtPriceLogModel.setRetailPrice(skuObj.getDoubleAttribute("priceRetail"));
                    CmsBtProductModel_Sku comSku = prodObj.getCommonNotNull().getSku(skuCode);
                    if (comSku == null) {
                        cmsBtPriceLogModel.setClientMsrpPrice(0d);
                        cmsBtPriceLogModel.setClientRetailPrice(0d);
                        cmsBtPriceLogModel.setClientNetPrice(0d);
                    } else {
                        cmsBtPriceLogModel.setClientMsrpPrice(comSku.getClientMsrpPrice());
                        cmsBtPriceLogModel.setClientRetailPrice(comSku.getClientRetailPrice());
                        cmsBtPriceLogModel.setClientNetPrice(comSku.getClientNetPrice());
                    }
                    cmsBtPriceLogModel.setComment("高级检索-重新计算指导售价");
                    cmsBtPriceLogModel.setCreated(new Date());
                    cmsBtPriceLogModel.setCreater(userName);
                    cmsBtPriceLogModel.setModified(new Date());
                    cmsBtPriceLogModel.setModifier(userName);
                    logModelList.add(cmsBtPriceLogModel);
                }
                int cnt = cmsBtPriceLogService.addLogListAndCallSyncPriceJob(logModelList);
                $debug("CmsRefreshRetailPriceTask修改商品价格 记入价格变更履历结束 结果=" + cnt);


                priceService.updatePlatFormPrice(channelId, chg, prodObj, cartId, userName);
//                // 只有最终售价变化了，才需要上新
//                if (autoPriceCfg != null && "1".equals(autoPriceCfg.getConfigValue1())) {
//                    // 最终售价被自动同步
//                    if (PlatFormEnums.PlatForm.TM.getId().equals(cartObj.getPlatform_id())) {
//                        // 天猫平台直接调用API
//                        try {
//                            if("2".equals(autoSyncPricePromotion.getConfigValue1())){
//                                //取得该channel cartId的所有的活动
//                                List<CmsBtPromotionBean> promtions = promotionService.getByChannelIdCartId(channelId, cartId);
//                                if(!ListUtils.isNull(promtions)) {
//                                    List<Integer> promotionIds = promotionService.getDateRangePromotionIds(promtions, new Date(), autoSyncPricePromotion.getConfigValue2(), autoSyncPricePromotion.getConfigValue3());
//                                    if(!ListUtils.isNull(promotionIds)) {
//                                        if (promotionCodeService.getCmsBtPromotionCodeInPromtionCnt(prodObj.getCommon().getFields().getCode(), promotionIds) >0){
//                                            continue;
//                                        }
//                                    }
//                                }
//                            }
//
//                            priceService.updateSkuPrice(channelId, cartId, prodObj);
//                        } catch (Exception e) {
//                            CmsBtOperationLogModel_Msg failInfo = new CmsBtOperationLogModel_Msg();
//                            failInfo.setSkuCode(prodCode);
//                            failInfo.setMsg(String.format("修改商品价格 调用天猫API失败, channelId=%s, code=%s, errmsg=%s", channelId, prodCode, e.getMessage()));
//                            failList.add(failInfo);
//
//                            $error(String.format("CmsProductPriceUpdateService.updateProductRetailPrice 修改商品价格 调用天猫API失败 channelId=%s, cartId=%d msg=%s", channelId, cartId, e.getMessage()), e);
//                        }
//                    }
//                }
            }catch (Exception e){
                CmsBtOperationLogModel_Msg failInfo = new CmsBtOperationLogModel_Msg();
                failInfo.setSkuCode(prodCode);
                failInfo.setMsg(String.format("CmsProductPriceUpdateService.updateProductRetailPrice执行出错, channelId=%s, code=%s, cartId=%s, errmsg=%s", channelId, prodCode, cartId, e.getMessage()));
                failList.add(failInfo);

                $error(e);
            }
        }

        // 记录商品修改历史
        $debug("CmsRefreshRetailPriceTask 开始记入商品修改历史");
        long sta = System.currentTimeMillis();
        productStatusHistoryService.insertList(channelId, codeList, cartId, EnumProductOperationType.BatchRefreshRetailPrice, msg, userName);
        $debug("CmsRefreshRetailPriceTask 记入商品修改历史结束 耗时" + (System.currentTimeMillis() - sta));

//        // 只有最终售价变化了，才需要上新
//        if (autoPriceCfg != null && "1".equals(autoPriceCfg.getConfigValue1())) {
//            // 最终售价被自动同步
//            if (!PlatFormEnums.PlatForm.TM.getId().equals(cartObj.getPlatform_id())) {
//                // 插入上新程序
//                $debug("CmsRefreshRetailPriceTask 开始记入SxWorkLoad表");
//                sta = System.currentTimeMillis();
//                sxProductService.insertSxWorkLoad(cmsProduct, Arrays.asList(cartId.toString()), modifier);
//                sxProductService.insertSxWorkLoad(channelId, codeList, cartId, userName);
//                $debug("CmsRefreshRetailPriceTask 记入SxWorkLoad表结束 耗时" + (System.currentTimeMillis() - sta));
//            }
//        }
        return failList;
    }

}
