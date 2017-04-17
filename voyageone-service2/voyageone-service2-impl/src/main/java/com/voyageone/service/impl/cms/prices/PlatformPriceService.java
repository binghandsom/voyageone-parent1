package com.voyageone.service.impl.cms.prices;

import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import com.taobao.api.request.TmallItemPriceUpdateRequest;
import com.taobao.api.response.TmallItemPriceUpdateResponse;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Carts;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.components.jd.service.JdSkuService;
import com.voyageone.components.jumei.JumeiHtDealService;
import com.voyageone.components.jumei.JumeiHtMallService;
import com.voyageone.components.jumei.bean.HtDeal_UpdateDealPriceBatch_UpdateData;
import com.voyageone.components.jumei.bean.HtMallSkuPriceUpdateInfo;
import com.voyageone.components.jumei.reponse.HtDealUpdateDealPriceBatchResponse;
import com.voyageone.components.jumei.request.HtDealUpdateDealPriceBatchRequest;
import com.voyageone.components.tmall.service.TbItemService;
import com.voyageone.service.bean.cms.CmsBtPromotionBean;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.ims.ImsBtProductDao;
import com.voyageone.service.impl.cms.product.*;
import com.voyageone.service.impl.cms.promotion.PromotionCodeService;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.service.impl.cms.promotion.PromotionTejiabaoService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.impl.cms.vomq.vomessage.body.*;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.service.model.ims.ImsBtProductModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.voyageone.common.CmsConstants.ChannelConfig.PRICE_CALCULATOR;
import static com.voyageone.common.CmsConstants.ChannelConfig.PRICE_CALCULATOR_FORMULA;

/**
 * 指导价变更批量确认
 *
 * @author jiangjusheng on 2016/09/20
 * @version 2.0.0
 */
@Service
public class PlatformPriceService extends VOAbsLoggable {

    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private CmsBtPriceConfirmLogService priceConfirmLogService;
    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private PromotionCodeService promotionCodeService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private PromotionTejiabaoService promotionTejiabaoService;
    @Autowired
    private JdSkuService jdSkuService;
    @Autowired
    private JumeiHtMallService jumeiHtMallService;
    @Autowired
    private JumeiHtDealService serviceJumeiHtDeal;
    @Autowired
    private TbItemService tbItemService;
    @Autowired
    private ImsBtProductDao imsBtProductDao;
    @Autowired
    private CmsBtPriceLogService cmsBtPriceLogService;
    @Autowired
    private ProductGroupService productGroupService;

    /**
     * confirmPlatformsRetailPrice
     * @param messageBody
     */
    public void confirmPlatformsRetailPrice(AdvSearchConfirmRetailPriceMQMessageBody messageBody) {

        String channelId = StringUtils.trimToNull(messageBody.getChannelId());
        String userName = StringUtils.trimToNull(messageBody.getSender());
        List<String> codeList = messageBody.getCodeList();
        Integer cartId = messageBody.getCartId();

        JongoQuery qryObj = new JongoQuery();
        JongoUpdate updObj = new JongoUpdate();
        BulkJongoUpdateList bulkList = new BulkJongoUpdateList(1000, cmsBtProductDao, channelId);

        // 获取产品的信息
        qryObj.setQuery("{'common.fields.code':{$in:#},'platforms.P" + cartId + ".skus.0':{$exists:true}}");
        qryObj.setParameters(codeList);
        qryObj.setProjection("{'common.fields.code':1,'platforms.P" + cartId + ".skus':1,'platforms.P" + cartId + ".cartId':1,'_id':0}");

        List<String> newCodeList = new ArrayList<>();
        boolean isUpdFlg = false;
        List<CmsBtProductModel> prodObjList = productService.getList(channelId, qryObj);
        for (CmsBtProductModel prodObj : prodObjList) {
            String prodCode = prodObj.getCommonNotNull().getFieldsNotNull().getCode();

            List<BaseMongoMap<String, Object>> skuList = prodObj.getPlatform(cartId).getSkus();
            for (BaseMongoMap skuObj : skuList) {
                Boolean isSaleFlg = (Boolean) skuObj.get("isSale");
                String chgFlg = StringUtils.trimToEmpty(skuObj.getStringAttribute("priceChgFlg"));
                if ((chgFlg.startsWith("U") || chgFlg.startsWith("D")) && isSaleFlg) {
                    // 指导价有变更
                    skuObj.put("priceChgFlg", "0");
                    skuObj.put("confPriceRetail", skuObj.getDoubleAttribute("priceRetail"));
                    isUpdFlg = true;
                }
            }

            // 更新产品的信息
            if (isUpdFlg) {
                newCodeList.add(prodCode);
                updObj.setQuery("{'common.fields.code':#}");
                updObj.setQueryParameters(prodCode);
                updObj.setUpdate("{$set:{'platforms.P" + cartId + ".skus':#,'modified':#,'modifier':#}}");
                updObj.setUpdateParameters(skuList, DateTimeUtil.getNowTimeStamp(), userName);
                BulkWriteResult rs = bulkList.addBulkJongo(updObj);
                if (rs != null) {
                    $debug(String.format("指导价变更批量确认 channelId=%s 执行结果=%s", channelId, rs.toString()));
                    // 保存确认历史
                    priceConfirmLogService.addConfirmed(channelId, prodCode, prodObj.getPlatformNotNull(cartId), userName);
                }
            }
        }

        // 记录商品修改历史
        productStatusHistoryService.insertList(channelId, newCodeList, cartId, EnumProductOperationType.BatchConfirmRetailPrice, "", userName);
        BulkWriteResult rs = bulkList.execute();
        if (rs != null) {
            $debug(String.format("指导价变更批量确认 channelId=%s 结果=%s", channelId, rs.toString()));
        }
    }

    /**
     * 重新计算该商品所有平台的商品数据并更新
     * @param newProduct
     * @param cartId
     * @param synSalePriceFlg
     * @param userName
     * @param comment
     * @throws PriceCalculateException
     * @throws IllegalPriceConfigException
     */
    public void updateProductPlatformPrice(CmsBtProductModel newProduct, Integer cartId, boolean synSalePriceFlg, String userName, String comment)
    throws PriceCalculateException, IllegalPriceConfigException {

        String channelId = newProduct.getChannelId();
        String prodCode = newProduct.getCommon().getFields().getCode();

        Integer chg = priceService.setPrice(newProduct, cartId, synSalePriceFlg);

        // 判断是否更新平台价格 如果要更新直接更新(因为无法判断涨幅,默认价格下跌)
        publishPlatFormPrice(channelId, chg, newProduct, cartId, userName, true, false);

        // 记录价格变更履历/同步价格范围
        List<CmsBtPriceLogModel> logModelList = new ArrayList<>(1);
        for (BaseMongoMap skuObj : newProduct.getPlatform(cartId).getSkus()) {
            String skuCode = skuObj.getStringAttribute("skuCode");
            CmsBtPriceLogModel cmsBtPriceLogModel = new CmsBtPriceLogModel();
            cmsBtPriceLogModel.setChannelId(channelId);
            cmsBtPriceLogModel.setProductId(newProduct.getProdId().intValue());
            cmsBtPriceLogModel.setCode(prodCode);
            cmsBtPriceLogModel.setCartId(cartId);
            cmsBtPriceLogModel.setSku(skuCode);
            cmsBtPriceLogModel.setSalePrice(skuObj.getDoubleAttribute("priceSale"));
            cmsBtPriceLogModel.setMsrpPrice(skuObj.getDoubleAttribute("priceMsrp"));
            cmsBtPriceLogModel.setRetailPrice(skuObj.getDoubleAttribute("priceRetail"));
            CmsBtProductModel_Sku comSku = newProduct.getCommonNotNull().getSku(skuCode);
            if (comSku == null) {
                cmsBtPriceLogModel.setClientMsrpPrice(0d);
                cmsBtPriceLogModel.setClientRetailPrice(0d);
                cmsBtPriceLogModel.setClientNetPrice(0d);
            } else {
                cmsBtPriceLogModel.setClientMsrpPrice(comSku.getClientMsrpPrice());
                cmsBtPriceLogModel.setClientRetailPrice(comSku.getClientRetailPrice());
                cmsBtPriceLogModel.setClientNetPrice(comSku.getClientNetPrice());
            }
            cmsBtPriceLogModel.setComment(comment);
            cmsBtPriceLogModel.setCreated(new Date());
            cmsBtPriceLogModel.setCreater(userName);
            cmsBtPriceLogModel.setModified(new Date());
            cmsBtPriceLogModel.setModifier(userName);
            logModelList.add(cmsBtPriceLogModel);
        }

        // 插入价格变更履历
        int cnt = cmsBtPriceLogService.addLogListAndCallSyncPriceJob(logModelList);
        $debug("CmsProductVoRateUpdateService修改商品价格 记入价格变更履历结束 结果=" + cnt);
    }

    /**
     * 同步所有平台的商品真实售价
     * @param channelId
     * @param chg
     * @param cmsProduct
     * @param modifier
     */
    public void publishPlatFormPrice(String channelId, Integer chg, CmsBtProductModel cmsProduct, String modifier, Boolean priceIsDown){
        for(String key : cmsProduct.getPlatforms().keySet()) {
            Integer cartId = cmsProduct.getPlatforms().get(key).getCartId();
            if (cartId == null || cartId < CmsConstants.ACTIVE_CARTID_MIN) continue;
            publishPlatFormPrice(channelId, chg, cmsProduct, cartId, modifier, priceIsDown, false);
        }
    }

    /**
     * 同步单个平台的商品真实售价
     * @param channelId
     * @param chg
     * @param cmsProduct
     * @param cartId
     * @param modifier
     * @param priceIsDown
     */
    public void publishPlatFormPrice(String channelId, Integer chg, CmsBtProductModel cmsProduct, Integer cartId, String modifier, boolean priceIsDown, boolean isSmSx){

        // 如果存在销售的sku变化,则通过上新来处理
        if((chg & 1) == 1
                && CmsConstants.ProductStatus.Approved.name().equals(cmsProduct.getPlatform(cartId).getStatus())){
            $info("存在 isSale 变化 插入sxworkload表" );
            insertWorkload(cmsProduct, cartId, modifier, isSmSx);
        }
        // 只是价格变化, 调用平台价格处理
        else if((chg & 2) == 2){
            $info("只是价格变化 直接更新平台价格");
            if(CmsConstants.ProductStatus.Approved.name().equals(cmsProduct.getPlatform(cartId).getStatus())
                    && StringUtils.isNotEmpty(cmsProduct.getPlatform(cartId).getpNumIId())) {

                CmsChannelConfigBean autoSyncPricePromotion = priceService.getAutoSyncPricePromotionOption(channelId, cartId);

                try {

                    // 价格下跌, 为0,只要有活动就不同步, 为2,的时候判断活动是否在范围内
                    // 根据活动前后时间判断是否同步平台售价
                    if (priceIsDown && ("2".equals(autoSyncPricePromotion.getConfigValue1())
                            || "0".equals(autoSyncPricePromotion.getConfigValue1()))) {

                        //取得该channel cartId的所有的活动
                        List<CmsBtPromotionBean> promtions = promotionService.getByChannelIdCartId(channelId, cartId);
                        if (!ListUtils.isNull(promtions)) {
                            // 当前时间内有有效活动,就不同步
                            List<Integer> promotionIds;
                            if ("0".equals(autoSyncPricePromotion.getConfigValue1()))
                                promotionIds = promotionService.getDateRangePromotionIds(promtions, new Date());
                            else
                                promotionIds = promotionService.getDateRangePromotionIds(promtions, new Date(), autoSyncPricePromotion.getConfigValue2(), autoSyncPricePromotion.getConfigValue3());

                            if (!ListUtils.isNull(promotionIds)) {
                                if (promotionCodeService.getCmsBtPromotionCodeInPromtionCnt(cmsProduct.getCommon().getFields().getCode(), promotionIds) > 0) {
                                    $info(String.format("channel=%s code=%s cartId=%d 有活动保护期 不更新平台价格", channelId, cmsProduct.getCommon().getFields().getCode(), cartId));
                                    return;
                                }
                            }
                        }
                    }

                    // 更新平台价格
                    updateSkuPrice(channelId, cartId, cmsProduct, modifier);
                } catch (Exception e) {
                    $warn("updateSkuPrices失败", e.getMessage());
                    e.printStackTrace();
                }
            } else if (CmsConstants.ProductStatus.Approved.name().equals(cmsProduct.getPlatform(cartId).getStatus())) {
                $info("存在 isSale 变化 插入sxworkload表" );
                insertWorkload(cmsProduct, cartId, modifier, isSmSx);
            }
        }
    }

    /**
     * 同步平台价格的处理逻辑
     * @param channleId
     * @param cartId
     * @param productModel
     * @param modifier
     * @throws Exception
     */
    public void updateSkuPrice(String channleId, int cartId, CmsBtProductModel productModel, String modifier) throws Exception {
        updateSkuPrice(channleId, cartId, productModel, false, modifier);
    }

    /**
     * 更新商品SKU的价格
     * 需要查询 voyageone_ims.ims_bt_product表，若对应的产品quantity_update_type为s：更新sku价格；为p：则更新商品价格(用最高一个sku的价格)
     * CmsBtProductModel中需要属性：common.fields.code, platforms.Pxx.pNumIId, platforms.Pxx.status, platforms.Pxx.skus.skuCode, platforms.Pxx.skus.priceSale,platforms.Pxx.skus.priceMsrp
     */
    public void updateSkuPrice(String channelId, int cartId, CmsBtProductModel productModel, boolean isUpdateJmDealPrice, String userName) throws Exception {
        logger.info("PriceService　更新商品SKU的价格 ");
        ShopBean shopObj = Shops.getShop(channelId, Integer.toString(cartId));
        CartBean cartObj = Carts.getCart(cartId);
        if (shopObj == null || cartObj == null) {
            $error("PriceService 未配置平台 channelId=%s, cartId=%d", channelId, cartId);
            throw new BusinessException("该店铺未配置销售平台！");
        }

        String prodCode = org.apache.commons.lang3.StringUtils.trimToNull(productModel.getCommonNotNull().getFieldsNotNull().getCode());
        if (prodCode == null) {
            $error("PriceService 产品数据不全 缺少code channelId=%s, cartId=%d, prod=%s", channelId, cartId, productModel.toString());
            throw new BusinessException("产品数据不全,缺少code！");
        }
        CmsBtProductModel_Platform_Cart platObj = productModel.getPlatform(cartId);
        if (platObj == null) {
            $error("PriceService 产品数据不全 缺少Platform channelId=%s, cartId=%d, prod=%s", channelId, cartId, productModel.toString());
            throw new BusinessException("产品数据不全,缺少Platform！");
        }
        if (!CmsConstants.ProductStatus.Approved.name().equals(platObj.getStatus())) {
            $warn("PriceService 产品未上新,不可修改价格 channelId=%s, cartId=%d, prod=%s", channelId, cartId, productModel.getCommon().getFields().getCode());
            return;
        }
        if ("1".equals(platObj.getLock())) {
            $warn("PriceService 产品被锁定,不可修改价格 channelId=%s, cartId=%d, prod=%s", channelId, cartId, productModel.getCommon().getFields().getCode());
            return;
        }

        List<BaseMongoMap<String, Object>> skuList = platObj.getSkus();
        if (skuList == null || skuList.isEmpty()) {
            $error("PriceService 产品sku数据不存在 channelId=%s, code=%s, cartId=%d", channelId, prodCode, cartId);
            throw new BusinessException("产品数据不全,缺少sku数据！");
        }
        String updType = null;
        if (PlatFormEnums.PlatForm.TM.getId().equals(cartObj.getPlatform_id()) || PlatFormEnums.PlatForm.JD.getId().equals(cartObj.getPlatform_id())) {
            // 先要判断更新类型
            ImsBtProductModel imsBtProductModel = imsBtProductDao.selectImsBtProductByChannelCartCode(productModel.getOrgChannelId(), cartId, prodCode);
            if (imsBtProductModel == null) {
                $error("PriceService 产品数据不全 未配置ims_bt_product表 channelId=%s, cartId=%d, prod=%s", channelId, cartId, productModel.toString());
                throw new BusinessException("产品数据不全,未配置ims_bt_product表！");
            }
            updType = org.apache.commons.lang3.StringUtils.trimToNull(imsBtProductModel.getQuantityUpdateType());
            if (updType == null || (!"s".equals(updType) && !"p".equals(updType))) {
                $error("PriceService 产品数据不全 未配置ims_bt_product表quantity_update_type channelId=%s, cartId=%d, prod=%s", channelId, cartId, productModel.toString());
                throw new BusinessException("产品数据不全,未配置ims_bt_product表quantity_update_type！");
            }
        }

        // 判断上新时销售价用的是建议售价还是最终售价
        CmsChannelConfigBean priceConfig = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.PRICE_SX_KEY, cartId + CmsConstants.ChannelConfig.PRICE_SX_PRICE_CODE);
        String priceConfigValue = null;
        if (priceConfig != null) {
            // 取得价格对应的configValue名
            priceConfigValue = org.apache.commons.lang3.StringUtils.trimToNull(priceConfig.getConfigValue1());
        }
        if (PlatFormEnums.PlatForm.TM.getId().equals(cartObj.getPlatform_id())) {
            // 天猫平台直接调用API
            tmUpdatePriceBatch(shopObj, skuList, priceConfigValue, updType, platObj.getpNumIId());
            // 全店特价宝价格更新
            promotionTejiabaoService.updateTejiabaoPrice(channelId, cartId, productModel.getCommon().getFields().getCode(), productModel.getPlatform(cartId).getSkus(), "priceService");

        } else if (PlatFormEnums.PlatForm.JM.getId().equals(cartObj.getPlatform_id())) {
            // votodo -- PriceService  聚美平台 更新商品SKU的价格

            jmUpdateDealPriceBatch(shopObj, platObj, priceConfigValue, isUpdateJmDealPrice);
            jmUpdateMallPriceBatch(shopObj, skuList, priceConfigValue);

        } else if (PlatFormEnums.PlatForm.JD.getId().equals(cartObj.getPlatform_id())) {
            // votodo -- JdSkuService  京东平台 更新商品SKU的价格
            jdUpdatePriceBatch(shopObj, skuList, priceConfigValue, updType);
        }
        // 其他平台价格变成通过上新程序修改价格
        else {
            sxProductService.insertSxWorkLoad(channelId, productModel.getCommon().getFields().getCode(), cartId, userName);
        }
    }

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
     * updateProductVoRate
     * @param messageBody
     * @return
     * @throws Exception
     */
    public List<CmsBtOperationLogModel_Msg> updateProductVoRate(ProductVoRateUpdateMQMessageBody messageBody) throws Exception {

        $info("CmsProductVoRateUpdateService start");

        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();

        String channelId = org.apache.commons.lang3.StringUtils.trimToNull(messageBody.getChannelId());
        List<String> codeList = messageBody.getCodeList();
        String userName = org.apache.commons.lang3.StringUtils.trimToEmpty(messageBody.getSender());

        String voRate = messageBody.getVoRate();
        String msg;
        if (voRate == null) {
            msg = "高价检索 批量更新VO扣点 清空";
        } else {
            msg = "高价检索 批量更新VO扣点 " + voRate;
        }

        JongoQuery queryObj = new JongoQuery();
        queryObj.setQuery("{'common.fields.code': {$in: #}}");
        queryObj.setParameters(codeList);
//        queryObj.setProjectionExt("prodId", "channelId", "orgChannelId", "platforms", "common.fields", "common.skus");
        List<CmsBtProductModel> prodObj = productService.getList(channelId, queryObj);

        List<String> successList = new ArrayList<>();
        for (CmsBtProductModel productModel : prodObj) {

            String code = productModel.getCommon().getFields().getCode();

            productModel.getPlatforms().forEach((s, platform) -> {
                Integer cartId = platform.getCartId();

                if (cartId < CmsConstants.ACTIVE_CARTID_MIN)
                    return;

                // 如果该平台使用的FORMULA计算价格,则跳过通过voRate的价格计算处理
                CmsChannelConfigBean priceCalculatorConfig = CmsChannelConfigs.getConfigBeanWithDefault(channelId, PRICE_CALCULATOR, cartId.toString());
                if (priceCalculatorConfig == null || PRICE_CALCULATOR_FORMULA.equals(priceCalculatorConfig.getConfigValue1()))
                    return;

                try {

                    updateProductPlatformPrice(productModel, cartId, false, userName, msg);
                    // 保存计算结果
                    JongoUpdate updObj = new JongoUpdate();
                    updObj.setQuery("{'common.fields.code':#}");
                    updObj.setQueryParameters(code);
                    updObj.setUpdate("{$set:{'platforms.P#.skus':#}}");
                    updObj.setUpdateParameters(cartId, productModel.getPlatform(cartId).getSkus());
                    WriteResult rs = productService.updateFirstProduct(updObj, channelId);
                    $debug("CmsProductVoRateUpdateService 保存计算结果 " + rs.toString());

                } catch (Exception exp) {

                    $error(String.format("CmsProductVoRateUpdateService 调用共通函数计算指导价时出错 channelId=%s, code=%s, cartId=%d, errmsg=%s", channelId, code, cartId, exp.getMessage()), exp);

                    CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                    errorInfo.setSkuCode(code);
                    errorInfo.setMsg(String.format("调用共通函数计算指导价时出错 cartId=%d, errmsg=%s", cartId, exp.getMessage()));
                    return;
                }
            });

            successList.add(code);
        }

        // 记录商品修改历史
        $debug("CmsProductVoRateUpdateService 开始记入价格变更履历");
        long sta = System.currentTimeMillis();
        productStatusHistoryService.insertList(channelId, successList, -1, EnumProductOperationType.BatchUpdate, msg, userName);
        $debug("CmsProductVoRateUpdateService 记入价格变更履历结束 耗时" + (System.currentTimeMillis() - sta));

        return failList;
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
        CmsChannelConfigBean autoPriceCfg = priceService.getAutoSyncPriceSaleOption(channelId, cartId);
        String msg = "高级检索 重新计算指导售价";
        if (autoPriceCfg != null && !"0".equals(autoPriceCfg.getConfigValue1())) {
            // 自动同步
            msg += "(自动同步到最终售价)";
        } else {
            // 不自动同步
            msg += "(未同步到最终售价)";
        }


        ShopBean shopObj = Shops.getShop(channelId, cartId.toString());
        if (shopObj == null) {
            throw new BusinessException(String.format("该店铺未配置对应的平台, channelId=%s, cartId=%s", channelId, cartId));
        }

        JongoQuery queryObj = new JongoQuery();
        queryObj.setQuery("{'common.fields.code': {$in: #},'platforms.P#':{$exists:true}}");
        queryObj.setParameters(codeList, cartId);
        List<CmsBtProductModel> prodObjList = productService.getList(channelId, queryObj);

        List<String> successList = new ArrayList<>();
        for (CmsBtProductModel prodObj : prodObjList) {
            $info(String.format("channel=%s cartId=%d code=%s", channelId,cartId,prodObj.getCommon().getFields().getCode()));
            String prodCode = prodObj.getCommon().getFields().getCode();


            List<BaseMongoMap<String, Object>> skuList = prodObj.getPlatform(cartId).getSkus();
            if (skuList == null || skuList.isEmpty()) {
                CmsBtOperationLogModel_Msg failInfo = new CmsBtOperationLogModel_Msg();
                failInfo.setSkuCode(prodCode);
                failInfo.setMsg(String.format("产品sku数据不存在, channelId=%s, code=%s, cartId=%s", channelId, prodCode, cartId));
                failList.add(failInfo);

                $warn("CmsProductPriceUpdateService.updateProductRetailPrice 产品sku数据不存在 channelId=%s, code=%s, cartId=%d", channelId, prodCode, cartId);
                continue;
            }

            try {

                // 重新计算价格
                Integer chg = priceService.setPrice(prodObj, cartId, false);

                // 判断是否更新平台价格 如果要更新直接更新
                publishPlatFormPrice(channelId, chg, prodObj, cartId, userName, true, false);

                // 保存计算结果
                JongoUpdate updObj = new JongoUpdate();
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
            }catch (Exception e){
                CmsBtOperationLogModel_Msg failInfo = new CmsBtOperationLogModel_Msg();
                failInfo.setSkuCode(prodCode);
                failInfo.setMsg(String.format("CmsProductPriceUpdateService.updateProductRetailPrice执行出错, channelId=%s, code=%s, cartId=%s, errmsg=%s", channelId, prodCode, cartId, e.getMessage()));
                failList.add(failInfo);

                $error(e);
            }

            successList.add(prodCode);
        }

        // 记录商品修改历史
        $debug("CmsRefreshRetailPriceTask 开始记入商品修改历史");
        long sta = System.currentTimeMillis();
        productStatusHistoryService.insertList(channelId, successList, cartId, EnumProductOperationType.BatchRefreshRetailPrice, msg, userName);
        $debug("CmsRefreshRetailPriceTask 记入商品修改历史结束 耗时" + (System.currentTimeMillis() - sta));

        return failList;
    }

    /**
     * 根据输入的公式重新计算中国最终售价
     * @param mqMessageBody UpdateProductSalePriceMQMessageBody
     * @return List<CmsBtOperationLogModel_Msg>
     */
    public List<CmsBtOperationLogModel_Msg> updateProductSalePrice(UpdateProductSalePriceMQMessageBody mqMessageBody){
        long threadNo =  Thread.currentThread().getId();

        // 获取参数信息
        String channelId = mqMessageBody.getChannelId();
        Integer cartId = mqMessageBody.getCartId();
        List<String> productCodes = mqMessageBody.getProductCodes();
        String userName = mqMessageBody.getSender();
        Map<String, Object> params = mqMessageBody.getParams();

        // 检查商品价格 notChkPrice=1时表示忽略价格超过阈值
        Integer notChkPriceFlg = (Integer) params.get("notChkPrice");
        String priceType = StringUtils.trimToNull((String) params.get("priceType"));
        String optionType = StringUtils.trimToNull((String) params.get("optionType"));
        String priceValue = StringUtils.trimToNull((String) params.get("priceValue"));
        // 无特殊处理:0  小数点向上取整:1    个位向下取整:2    个位向上取整:3
        Integer roundType = (Integer) params.get("roundType");
        if (roundType == null) {
            roundType = 0;
        }
        // 商品内，SKU价格不统一:0  商品内，SKU统一最高价:1 商品内，SKU统一最低价:2   黄金码:3
        Integer skuUpdType = (Integer) params.get("skuUpdType");
        if (skuUpdType == null) {
            skuUpdType = 0;
        }

        List<CmsBtPriceLogModel> priceLogList = new ArrayList<>();
        BulkJongoUpdateList bulkList = new BulkJongoUpdateList(1000, cmsBtProductDao, channelId);
        List<CmsBtOperationLogModel_Msg> errorInfos = new ArrayList<>();

        //
        CmsChannelConfigBean autoSyncPriceMsrpConfig = priceService.getAutoSyncPriceMsrpOption(channelId, cartId);

        // 阀值
        CmsChannelConfigBean mandatoryBreakThresholdConfig = priceService.getMandatoryBreakThresholdOption(channelId, cartId);

        // 获取产品的信息
        JongoQuery qryObj = new JongoQuery();
        qryObj.setQuery("{'common.fields.code':{$in:#},'platforms.P" + cartId + ".skus.0':{$exists:true}}");
        qryObj.setParameters(productCodes);
        List<CmsBtProductModel> prodObjList = productService.getList(channelId, qryObj);
        if(ListUtils.isNull(prodObjList)) return errorInfos;

        $debug("批量修改商品价格 开始批量处理");
        int i=0;
        for (CmsBtProductModel prodObj : prodObjList) {
            i++;

            $info(String.format("threadNo=%d %d/%d",threadNo, i , prodObjList.size()));

//            prodObj.setChannelId(channelId); // 为后面调用priceService.setPrice使用
            List<BaseMongoMap<String, Object>> skuList = prodObj.getPlatform(cartId).getSkus();
            String prodCode = prodObj.getCommon().getFields().getCode();

            // 先取出最高价/最低价
            Double maxPriceSale = null;

            if (priceType != null) {
                if (skuUpdType == 1) {
                    // 统一最高价
                    for (BaseMongoMap skuObj : skuList) {
                        double befPriceSale = skuObj.getDoubleAttribute(priceType);
                        if (maxPriceSale == null) {
                            maxPriceSale = befPriceSale;
                        } else if (maxPriceSale < befPriceSale) {
                            maxPriceSale = befPriceSale;
                        }
                    }
                } else if (skuUpdType == 2) {
                    // 统一最低价
                    for (BaseMongoMap skuObj : skuList) {
                        double befPriceSale = skuObj.getDoubleAttribute(priceType);
                        if (maxPriceSale == null) {
                            maxPriceSale = befPriceSale;
                        } else if (maxPriceSale > befPriceSale) {
                            maxPriceSale = befPriceSale;
                        }
                    }
                }else if(skuUpdType == 3){
                    try {
                        Map<String, String> goldSize;
                        Map<String, CmsBtProductModel_Sku> skuInfo = new HashMap();
                        //找出黄金尺码
                        prodObj.getCommonNotNull().getSkus().forEach(sku -> {
                            skuInfo.put(sku.getSkuCode(), sku);
                        });
                        goldSize = sxProductService.getSizeMap(channelId, prodObj.getCommon().getFields().getBrand(), prodObj.getCommon().getFields().getProductType(), prodObj.getCommon().getFields().getSizeType());
                        // 统一最低价
                        for (BaseMongoMap skuObj : skuList) {
                            if(skuObj.get("isSale") != null ){
                                double befPriceSale = skuObj.getDoubleAttribute(priceType);
                                CmsBtProductModel_Sku comSkuInfo = skuInfo.get(skuObj.getStringAttribute("skuCode"));
                                if(comSkuInfo != null && comSkuInfo.getQty() > 0 && goldSize.containsKey(comSkuInfo.getSize())){
                                    if(maxPriceSale == null || maxPriceSale < befPriceSale){
                                        maxPriceSale = befPriceSale;
                                    }
                                }
                            }
                        }
                        $debug("黄金尺码最大值"+ (maxPriceSale==null?"": maxPriceSale+""));
                    } catch (BusinessException e){
                        $warn(e.getMessage());
                        CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                        errorInfo.setSkuCode(prodCode);
                        errorInfo.setMsg("获取黄金尺码最大值异常");
                        errorInfos.add(errorInfo);
                        continue;
                    }
                }
            }

            try {
                Boolean synPrice = false;
                for (BaseMongoMap skuObj : skuList) {

                    String skuCode = skuObj.getStringAttribute("skuCode");
                    if (StringUtils.isEmpty(skuCode)) {
                        $warn(String.format("setProductSalePrice: 缺少数据 code=%s, para=%s", prodCode, skuCode, params.toString()));
                        throw new BusinessException(prodCode, String.format("商品[code=%s, cartId=%s, channelId=%s]的数据错误，没有skuCode。", prodCode, cartId, channelId), null);
                    }

                    // 修改后的最终售价
                    Double rs;
                    if (StringUtils.isEmpty(priceType)) {
                        // 使用固定值
                        if (priceValue == null) {
                            $warn(String.format("setProductSalePrice: 没有填写金额 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                            throw new BusinessException(prodCode, String.format("商品[code=%s, cartId=%s, channelId=%s]的数据错误，没有填写价格。", prodCode, cartId, channelId), null);
                        }
                        rs = getFinalSalePrice(null, optionType, priceValue, roundType);
                    } else {
                        Object basePrice;
                        if (maxPriceSale == null) {
                            basePrice = skuObj.getAttribute(priceType);
                        } else {
                            basePrice = maxPriceSale;
                        }
                        if (basePrice != null) {
                            BigDecimal baseVal = new BigDecimal(basePrice.toString());
                            rs = getFinalSalePrice(baseVal, optionType, priceValue, roundType);
                        } else {
                            $warn(String.format("setProductSalePrice: 缺少数据 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                            throw new BusinessException(prodCode, String.format("商品[code=%s, cartId=%s, channelId=%s, sku=%s]的数据错误，没有priceType的数据。", prodCode, cartId, channelId, skuCode), null);
                        }
                    }

                    if (rs == null) {
                        $warn(String.format("setProductSalePrice: 数据错误 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                        throw new BusinessException(prodCode, String.format("商品[code=%s, cartId=%s, channelId=%s, sku=%s]的价格计算发生错误。请联系IT.", prodCode, cartId, channelId, skuCode), null);
                    }
                    if (rs < 0) {
                        $warn(String.format("setProductSalePrice: 数据错误 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                        throw new BusinessException(prodCode, String.format("商品[code=%s, cartId=%s, channelId=%s, sku=%s]的最终售价计算结果为负数，请重新输入。", prodCode, cartId, channelId, skuCode), null);
                    }
                    // 修改前的最终售价
                    double befPriceSale = skuObj.getDoubleAttribute("priceSale");
                    if (rs == befPriceSale) {
                        // 修改前后价格相同
//                        $info(String.format("setProductSalePrice: 修改前后价格相同 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                        continue;
                    } else if(rs < befPriceSale){
                        synPrice = true;
                    }

                    Object priceRetail = skuObj.get("priceRetail");
                    if (priceRetail == null) {
                        $warn(String.format("setProductSalePrice: 缺少数据 priceRetail为空 code=%s, sku=%s", prodCode, skuCode));
                        throw new BusinessException(prodCode, String.format("商品[code=%s, cartId=%s, channelId=%s, sku=%s]的数据错误，没有priceRetail的数据。", prodCode, cartId, channelId, skuCode), null);
                    }
                    // 指导价
                    Double result;
                    if (priceRetail instanceof Double) {
                        result = (Double) priceRetail;
                    } else {
                        if (!StringUtil.isEmpty(priceRetail.toString())) {
                            result = new Double(priceRetail.toString());
                        } else {
                            $warn(String.format("setProductSalePrice: 数据错误 priceRetail格式错误 code=%s, sku=%s", prodCode, skuCode));
                            throw new BusinessException(prodCode, String.format("商品[code=%s, cartId=%s, channelId=%s, sku=%s]的数据错误，priceRetail格式错误。", prodCode, cartId, channelId, skuCode), null);
                        }
                    }

                    // 要更新最终售价变化状态
                    skuObj.setAttribute("priceSale", rs);
                    String diffFlg = priceService.getPriceDiffFlg(channelId, skuObj, cartId);
                    skuObj.setAttribute("priceDiffFlg", diffFlg);

                    // 价格变更check
                    if(1 != notChkPriceFlg) {
                        priceService.priceCheck(skuObj, autoSyncPriceMsrpConfig, mandatoryBreakThresholdConfig);
                    }

                    CmsBtPriceLogModel cmsBtPriceLogModel = new CmsBtPriceLogModel();
                    cmsBtPriceLogModel.setChannelId(channelId);
                    cmsBtPriceLogModel.setProductId(prodObj.getProdId().intValue());
                    cmsBtPriceLogModel.setCode(prodCode);
                    cmsBtPriceLogModel.setCartId(cartId);
                    cmsBtPriceLogModel.setSku(skuCode);
                    cmsBtPriceLogModel.setSalePrice(rs);
                    cmsBtPriceLogModel.setMsrpPrice(skuObj.getDoubleAttribute("priceMsrp"));
                    cmsBtPriceLogModel.setRetailPrice(result);
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
                    cmsBtPriceLogModel.setComment("高级检索 设置最终售价");
                    cmsBtPriceLogModel.setCreated(new Date());
                    cmsBtPriceLogModel.setCreater(userName);
                    cmsBtPriceLogModel.setModified(new Date());
                    cmsBtPriceLogModel.setModifier(userName);
                    priceLogList.add(cmsBtPriceLogModel);
                }

                // 根据中国最终售价来判断 中国建议售价是否需要自动提高价格
                try {
                    priceService.unifySkuPriceMsrp(prodObj.getPlatform(cartId).getSkus(),channelId,cartId);
                }catch (Exception e) {
                    $error(String.format("批量修改商品价格　调用priceService.unifySkuPriceMsrp失败 channelId=%s, cartId=%s msg=%s", channelId, cartId.toString(), e.getMessage()), e);
                    throw new BusinessException(prodCode, String.format("批量修改商品价格　调用priceService.unifySkuPriceMsrp失败 channelId=%s, cartId=%s", channelId, cartId), e);
                }

                // 更新产品的信息
                JongoUpdate updObj = new JongoUpdate();
                updObj.setQuery("{'common.fields.code':#}");
                updObj.setUpdate("{$set:{'platforms.P" + cartId + ".skus':#,'modified':#,'modifier':#}}");
                updObj.setQueryParameters(prodCode);
                updObj.setUpdateParameters(skuList, DateTimeUtil.getNowTimeStamp(), userName);
                BulkWriteResult rs = bulkList.addBulkJongo(updObj);
                if (rs != null) {
                    $debug(String.format("批量修改商品价格 channelId=%s 执行结果=%s", userName, rs.toString()));
                }

                // 更新平台价格(因为批量修改价格,不存在修改sku的isSale的情况,默认调用API刷新价格)
                publishPlatFormPrice(channelId, 2, prodObj, cartId, userName, synPrice, false);
            } catch (BusinessException be) {
                CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                errorInfo.setSkuCode(prodCode);
                errorInfo.setMsg(be.getMessage());
                errorInfos.add(errorInfo);
            }
        }

        BulkWriteResult rs = bulkList.execute();
        if (rs != null) {
            $debug(String.format("批量修改商品价格 channelId=%s 结果=%s", channelId, rs.toString()));
        }

        // 需要记录价格变更履历
        $debug("批量修改商品价格 开始记入价格变更履历");
        long sta = System.currentTimeMillis();
        int cnt = cmsBtPriceLogService.addLogListAndCallSyncPriceJob(priceLogList);
        $debug("批量修改商品价格 记入价格变更履历结束 结果=" + cnt + " 耗时" + (System.currentTimeMillis() - sta));

        // jw旗舰店天猫国际的价格发生变化 同步到liking 因为liking店的价格取决与旗舰店的价格
        if(channelId.equals("010") && cartId == 23) {
            sxProductService.insertSxWorkLoad(channelId, productCodes, 928, userName);
        }
        return errorInfos;
    }

    /**
     * 处理上新逻辑
     * @param cmsProduct
     * @param cartId
     * @param modifier
     */
    private void insertWorkload(CmsBtProductModel cmsProduct, Integer cartId, String modifier, Boolean isSmSx) {

        if(cartId >= 20 && cartId < 999) {
            sxProductService.insertSxWorkLoad(cmsProduct, Arrays.asList(cartId.toString()), modifier, isSmSx);
        }
    }

    /**
     * 同步天猫平台的价格
     * @param shopBean
     * @param skuList
     * @param priceConfigValue
     * @param updType
     * @param pNumIId
     * @throws Exception
     */
    private void tmUpdatePriceBatch(ShopBean shopBean, List<BaseMongoMap<String, Object>> skuList, String priceConfigValue, String updType, String pNumIId) throws Exception {
        Double maxPrice = null;
        List<TmallItemPriceUpdateRequest.UpdateSkuPrice> list2 = new ArrayList<>(skuList.size());
        for (BaseMongoMap skuObj : skuList) {
            TmallItemPriceUpdateRequest.UpdateSkuPrice obj3 = new TmallItemPriceUpdateRequest.UpdateSkuPrice();
            obj3.setOuterId((String) skuObj.get("skuCode"));
            Double priceSale;
            if (priceConfigValue == null) {
                priceSale = skuObj.getDoubleAttribute("priceSale");
            } else {
                priceSale = skuObj.getDoubleAttribute(priceConfigValue);
            }
            if (maxPrice == null || (maxPrice != null && priceSale > maxPrice)) {
                maxPrice = priceSale;
            }
            obj3.setPrice(priceSale.toString());
            list2.add(obj3);
        }
        if ("p".equals(updType)) {
            // 更新商品价格
            list2 = null;
        }
        TmallItemPriceUpdateResponse response = tbItemService.updateSkuPrice(shopBean, pNumIId, maxPrice, list2);
        if (response != null) {
            logger.info("PriceService　更新商品SKU的价格 " + response.getBody());
        }
    }

    /**
     * 更新聚美deal商品的价格
     * @param shopBean
     * @param platObj
     * @param priceConfigValue
     * @param isUpdateJmDealPrice
     * @throws Exception
     */
    private void jmUpdateDealPriceBatch(ShopBean shopBean, CmsBtProductModel_Platform_Cart platObj, String priceConfigValue, boolean isUpdateJmDealPrice) throws Exception {

        HtDeal_UpdateDealPriceBatch_UpdateData updateData = null;
        String pNumIId = platObj.getpNumIId();
        List<BaseMongoMap<String, Object>> skuList = platObj.getSkus();
        List<HtDeal_UpdateDealPriceBatch_UpdateData> list = new ArrayList<>(skuList.size());
        for (BaseMongoMap skuObj : skuList) {
            updateData = new HtDeal_UpdateDealPriceBatch_UpdateData();
            String jmSkuNo = (String) skuObj.get("jmSkuNo");
            if (StringUtils.isEmpty(jmSkuNo)) {
                continue;
            }
            updateData.setJumei_sku_no(jmSkuNo);
            Double priceMsrp = skuObj.getDoubleAttribute("priceMsrp");
            updateData.setMarket_price(priceMsrp);
            updateData.setJumei_hash_id(pNumIId);
            list.add(updateData);
        }
        String errorMsg = "";
        if (list.size() == 0) {
            return;
        }
        HtDealUpdateDealPriceBatchRequest request = new HtDealUpdateDealPriceBatchRequest();
        List<List<HtDeal_UpdateDealPriceBatch_UpdateData>> pageList = CommonUtil.splitList(list, 10);
        for (List<HtDeal_UpdateDealPriceBatch_UpdateData> page : pageList) {
            request.setUpdate_data(page);
            HtDealUpdateDealPriceBatchResponse response = serviceJumeiHtDeal.updateDealPriceBatch(shopBean, request);
            if (!response.is_Success()) {
                //是否抛出错误
                boolean isThrowError = isThrowError(response);
                if (isThrowError) {
                    errorMsg += response.getErrorMsg();
                }
            }
        }
        if (!StringUtils.isEmpty(errorMsg)) {
            throw new BusinessException("jm_UpdateDealPriceBatch:" + errorMsg);
        }
    }

    /**
     * 更新聚美mall商品的价格
     * @param shopBean
     * @param skuList
     * @param priceConfigValue
     * @throws Exception
     */
    private void jmUpdateMallPriceBatch(ShopBean shopBean, List<BaseMongoMap<String, Object>> skuList, String priceConfigValue) throws Exception {
        List<HtMallSkuPriceUpdateInfo> list = new ArrayList<>(skuList.size());
        HtMallSkuPriceUpdateInfo updateData;

        // 因为聚美mall中的sku价格必须一致, 获取isSale的sku价格,随便取一个就行
        Double defaultSalePrice = 0.0D;
        for (BaseMongoMap<String, Object> sku : skuList) {
            if (Boolean.valueOf(sku.getStringAttribute("isSale")))
                defaultSalePrice = sku.getDoubleAttribute("priceSale");
        }

        for (BaseMongoMap skuObj : skuList) {
            updateData = new HtMallSkuPriceUpdateInfo();
            String skuCode = (String) skuObj.get("jmSkuNo");
            if (StringUtils.isEmpty(skuCode)) {
                continue;
            }
            updateData.setJumei_sku_no(skuCode);
            Double priceSale;
            if (priceConfigValue == null) {
                // 如果该sku不销售,则使用默认的sku的salePrice
                priceSale = Boolean.valueOf(skuObj.getStringAttribute("isSale"))
                        ? skuObj.getDoubleAttribute("priceSale")
                        : defaultSalePrice;
            } else {
                priceSale = skuObj.getDoubleAttribute(priceConfigValue);
            }
            Double priceMsrp = skuObj.getDoubleAttribute("priceMsrp");
            updateData.setMall_price(priceSale);
            updateData.setMarket_price(priceMsrp);
            list.add(updateData);
        }
        String errorMsg = "";
        if (list.size() == 0) return;
        List<List<HtMallSkuPriceUpdateInfo>> pageList = CommonUtil.splitList(list, 10);
        for (List<HtMallSkuPriceUpdateInfo> page : pageList) {
            StringBuffer sb = new StringBuffer();
            if (!jumeiHtMallService.updateMallSkuPrice(shopBean, page, sb)) {
                errorMsg += sb.toString();
            }
        }
        if (!StringUtils.isEmpty(errorMsg)) {
            throw new BusinessException("updateMallSkuPrice:" + errorMsg);
        }
    }

    /**
     * 京东更新商品价格
     * @param shopBean
     * @param skuList
     * @param priceConfigValue
     * @param updType
     * @throws Exception
     */
    private void jdUpdatePriceBatch(ShopBean shopBean, List<BaseMongoMap<String, Object>> skuList, String priceConfigValue, String updType) throws Exception {
        List<TmallItemPriceUpdateRequest.UpdateSkuPrice> list = new ArrayList<>(skuList.size());
        TmallItemPriceUpdateRequest.UpdateSkuPrice updateData = null;
        Double maxPrice = null;
        for (BaseMongoMap skuObj : skuList) {
            updateData = new TmallItemPriceUpdateRequest.UpdateSkuPrice();
            updateData.setOuterId((String) skuObj.get("skuCode"));
            Double priceSale = null;
            if (priceConfigValue == null) {
                priceSale = skuObj.getDoubleAttribute("priceSale");
            } else {
                priceSale = skuObj.getDoubleAttribute(priceConfigValue);
            }
            if (maxPrice == null || (maxPrice != null && priceSale > maxPrice)) {
                maxPrice = priceSale;
            }
            updateData.setPrice(priceSale.toString());
            list.add(updateData);
        }
        if (!"s".equals(updType)) {
            final Double skuPrice = maxPrice;
            // 更新商品价格
            list.forEach(f -> f.setPrice(skuPrice.toString()));
        }

        list.forEach(f -> {
            if (!StringUtils.isEmpty(f.getOuterId())) {
                jdSkuService.updateSkuPriceByOuterId(shopBean, f.getOuterId(), f.getPrice().toString());
            }
        });
    }

    /**
     * 是否抛出错误
     *
     * @param response
     * @return
     */
    private boolean isThrowError(HtDealUpdateDealPriceBatchResponse response) {
        boolean isThrowError = true;
        if (response.getErrorList() != null && response.getErrorList().size() > 0) {
            for (HtDealUpdateDealPriceBatchResponse.JuMeiSkuError error : response.getErrorList()) {
                //if错误码为505 且错误信息包含"不存在!" then 错误信息不抛出
                if ("505".equals(error.getError_code())) {
                    if (!StringUtils.isEmpty(error.getError_message()) && error.getError_message().indexOf("不存在!") >= 0) {
                        isThrowError = false;
                    }
                }
            }
        }
        return isThrowError;
    }

    /**
     * 根据输入的公式计算价格
     * @param baseVal
     * @param optionType
     * @param priceValueStr
     * @param roundType
     * @return
     */
    private Double getFinalSalePrice(BigDecimal baseVal, String optionType, String priceValueStr, int roundType) {
        BigDecimal priceValue = null;
        if (priceValueStr != null) {
            priceValue = new BigDecimal(priceValueStr);
        }
        BigDecimal rs = null;
        if ("=".equals(optionType)) {
            if (baseVal == null) {
                rs = priceValue;
            } else {
                rs = baseVal;
            }
        } else if ("+".equals(optionType)) {
            rs = baseVal.add(priceValue);
        } else if ("-".equals(optionType)) {
            rs = baseVal.subtract(priceValue);
        } else if ("*".equals(optionType)) {
            rs = baseVal.multiply(priceValue);
        } else if ("/".equals(optionType)) {
            rs = baseVal.divide(priceValue, 3, BigDecimal.ROUND_CEILING);
        }
        if (rs == null) {
            return null;
        } else {
            if (roundType == 1) {
                // 小数点向上取整
                return rs.setScale(0, BigDecimal.ROUND_CEILING).doubleValue();
            } else if (roundType == 2) {
                // 个位向下取整
                BigDecimal multyValue = new BigDecimal("10");
                if (rs.compareTo(multyValue) <= 0) {
                    // 少于10的直接返回
                    return rs.setScale(2, BigDecimal.ROUND_CEILING).doubleValue();
                }

                rs = rs.divide(multyValue);
                rs = rs.setScale(0, BigDecimal.ROUND_DOWN);
                rs = rs.multiply(multyValue);
                return rs.doubleValue();
            } else if (roundType == 3) {
                // 个位向上取整
                BigDecimal multyValue = new BigDecimal("10");
                rs = rs.divide(multyValue);
                rs = rs.setScale(1, BigDecimal.ROUND_UP);
                rs = rs.setScale(0, BigDecimal.ROUND_CEILING);
                rs = rs.multiply(multyValue);
                return rs.doubleValue();
            } else {
                return rs.setScale(2, BigDecimal.ROUND_CEILING).doubleValue();
            }
        }
    }
}
