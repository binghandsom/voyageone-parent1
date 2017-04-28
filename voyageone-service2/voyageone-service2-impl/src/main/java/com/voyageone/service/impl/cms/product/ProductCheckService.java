package com.voyageone.service.impl.cms.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductErrorDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.model.cms.mongo.product.*;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * check各个channel的product数据是否正确,主要用于检测产品的状态和sku数据
 *
 * @author edward.lin 2017/03/06
 * @version 2.15.0
 */
@Service
public class ProductCheckService extends BaseService {

    @Autowired
    FeedInfoService feedInfoService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductGroupService productGroupService;
    @Autowired
    CmsBtProductErrorDao cmsBtProductErrorDao;
    @Autowired
    CmsBtProductDao cmsBtProductDao;


    /**
     *
     * @param productModel
     * @return
     */
    public void checkProductIsRight(CmsBtProductModel productModel, List<TypeChannelBean> cartTypeList, String modifer) throws Exception {
        CmsBtProductErrorModel errorModel = new CmsBtProductErrorModel();
        errorModel.setChannelId(productModel.getChannelId());
        errorModel.setProduct_Id(productModel.get_id());

        try {

            // 检测产品的基础属性是否存在
            checkCommonIsExists(productModel);

            // 检测product产品的common.skus是否正确
            checkCommonSkus(productModel, errorModel);

            // 检测product的P0平台的mainProductCode是否正确
            checkProductP0(productModel, errorModel);

            // 检测product的P0以外平台的mainProductCode是否正确
            cartTypeList.forEach(cartInfo -> {
                Integer cartId = Integer.valueOf(cartInfo.getValue());
                if (cartId !=0)
                    checkProductPlatform(productModel, errorModel, cartId);
            });

        } catch (BusinessException ex) {
            $error(ex.getMessage());
            errorModel.setChannelId(productModel.getChannelId());
            errorModel.setProduct_Id(productModel.get_id());
            errorModel.setErrorInfo(ex.getMessage());
        }

        if (errorModel.getErrors().size() > 0 || !StringUtils.isEmpty(errorModel.getErrorInfo())) {

            // 更新group表
            // TODO: 2017/3/7 暂时不更新数据库
//            productModel.setChannelId("999");
            productService.updateForCheckRight(productModel.getChannelId(), productModel, modifer);
            errorModel.getErrors().stream().distinct().collect(Collectors.toList());
            cmsBtProductErrorDao.insert(errorModel);
        }
    }

    /**
     * 检测产品的基础属性是否存在
     * @param productModel
     */
    private void checkCommonIsExists (CmsBtProductModel productModel) {
        if (productModel.getCommon() == null) {
            throw new BusinessException(String.format("该产品_id:%s的common属性为空", productModel.get_id()));
        } else if (productModel.getCommon().getFields() == null ) {
            throw new BusinessException(String.format("该产品_id:%s的common.fields属性为空", productModel.get_id()));
        } else if (productModel.getCommon().getSkus() == null) {
            throw new BusinessException(String.format("该产品_id:%s的common.skus属性为空", productModel.get_id()));
        } else if (StringUtils.isAnyEmpty(productModel.getChannelId()
                , productModel.getOrgChannelId()
                , String.valueOf(productModel.getProdId())
                , productModel.getCommon().getFields().getCode())) {
            throw new BusinessException(String.format("该产品_id:%s的channelId/orgChannelId/prodId/code属性为空", productModel.get_id()));
        }
    }

    /**
     * 检测common.skus的价格是否正确
     * @param productModel
     * @return
     */
    private void checkCommonSkus(CmsBtProductModel productModel, CmsBtProductErrorModel errorModel) {

        for (CmsBtProductModel_Sku sku : productModel.getCommon().getSkus()) {
            Double clientNetPrice = sku.getClientNetPrice();
            Double clientRetailPrice = sku.getClientRetailPrice();
            Double clientMsrpPrice = sku.getClientMsrpPrice();

            if (StringUtils.isEmpty(sku.getBarcode())
                    || StringUtils.isEmpty(sku.getSkuCode())
//                    || StringUtils.isEmpty(sku.getSize())
                    || StringUtils.isEmpty(sku.getClientSkuCode())
//                    || StringUtils.isEmpty(sku.getClientSize())
                    ) {
                errorModel.getErrors().add("_id: "+productModel.get_id()+"sku:" + sku.getSkuCode() + "的基础数据未空");
            }

            if (NumberUtils.compare(clientMsrpPrice, 0.0D) <= 0) {
                errorModel.getErrors().add("_id: "+productModel.get_id()+"sku:" + sku.getSkuCode() + "的clientMsrpPrice为空或者为0");
            }

            if (NumberUtils.compare(clientMsrpPrice, 0.0D) > 0
                    && NumberUtils.compare(clientRetailPrice, 0.0D) <= 0) {
                errorModel.getErrors().add("_id: "+productModel.get_id()+"sku:" + sku.getSkuCode() + "的clientRetailPrice为空或者为0");
                sku.setClientRetailPrice(clientMsrpPrice);
            }

            // 如果clientNetPrice为空或者为0, 设置成clientMsrpPrice
            if (NumberUtils.compare(clientNetPrice, 0.0D) <= 0
                    && (NumberUtils.compare(clientMsrpPrice, 0.0D) > 0
                    || NumberUtils.compare(clientRetailPrice, 0.0D) > 0)) {
                errorModel.getErrors().add("_id: "+productModel.get_id()+"sku:" + sku.getSkuCode() + "的clientNetPrice为空或者为0");
                sku.setClientNetPrice(NumberUtils.compare(clientRetailPrice, 0.0D) > 0
                        ? clientRetailPrice
                        : clientMsrpPrice);
            }

            // TODO: 2017/3/6 其他有些地方还有用处,暂时不处理,以后设置为0(因为人民币价格无用处)
//            sku.setPriceMsrp(0.0D);
//            sku.setPriceRetail(0.0D);
        }
    }

    /**
     * 检测platforms.P0.mainProductCode是否正确
     * @param productModel
     * @param errorModel
     */
    private void checkProductP0(CmsBtProductModel productModel, CmsBtProductErrorModel errorModel) {
        Boolean flg = false;

        if (productModel.getPlatform(0) == null) {
            throw new BusinessException(String.format("该产品_id:%s的platforms.P0为空", productModel.get_id()));
        }

        // 获取P0的主商品code
        List<CmsBtProductModel> productList = productService.getProductListByModel(productModel.getChannelId(), productModel.getCommon().getFields().getModel(), productModel.getOrgChannelId());
        String poMainCode = getOrgMainCode(productList);
        List<String> productCodes = productList.stream().map(product -> product.getCommon().getFields().getCode()).collect(Collectors.toList());

        if (StringUtils.isEmpty(poMainCode))
            throw new BusinessException(String.format("该产品_id:%s的获取原始主商品code为空", productModel.get_id()));

        // check默认设置是否正确,并且修改数据
        String mainProductCode = productModel.getPlatform(0).getMainProductCode();
        String productCode = productModel.getCommon().getFields().getCode();
        Integer pCommonIsMain = productModel.getCommon().getFields().getIsMasterMain();

        if (StringUtils.isEmpty(mainProductCode)) {
            flg = true;
            errorModel.getErrors().add(String.format("platforms(_id: %s):p0的主商品为空", productModel.get_id()));
            productModel.getPlatform(0).setMainProductCode(poMainCode);
        } else if (!poMainCode.equals(mainProductCode)){
            flg = true;
            errorModel.getErrors().add("platforms(_id: " + productModel.get_id() + "):p0的主商品不匹配:" + mainProductCode + ",修改后:" + poMainCode);
            productModel.getPlatform(0).setMainProductCode(poMainCode);

            if (productCode.equals(mainProductCode) && pCommonIsMain != 1) {
                errorModel.getErrors().add("platforms(_id: %s):common.fields.isMasterMain标志不正确:0->1");
                productModel.getCommon().getFields().setIsMasterMain(1);
            } else if (!productCode.equals(mainProductCode) && pCommonIsMain != 0) {
                errorModel.getErrors().add("platforms(_id: %s):common.fields.isMasterMain标志不正确:1->0");
                productModel.getCommon().getFields().setIsMasterMain(0);
            }
        }
        productModel.getPlatform(0).setCartId(0);

        // 检测和设置group级别的主商品
        CmsBtProductGroupModel groupInfo = productGroupService.selectProductGroupByCode(productModel.getChannelId(), productCode, 0);

        if (groupInfo == null || StringUtils.isEmpty(groupInfo.getMainProductCode())) {
            errorModel.getErrors().add(String.format("platforms(_id: %s):group(cartId:0)的不存在,或者mainProductCode不存在", productModel.get_id()));
        } else {
            if (!groupInfo.getMainProductCode().equals(poMainCode)) {
                flg = true;
                errorModel.getErrors().add(String.format("platforms(_id: %s):group(cartId:0)的主商品设置不正确:%s -> %s", productModel.get_id(), groupInfo.getMainProductCode(), poMainCode));
                groupInfo.setMainProductCode(poMainCode);
            }

            if (!productCodes.containsAll(groupInfo.getProductCodes())) {
                flg = true;
                errorModel.getErrors().add(String.format("platforms(_id: %s):group(cartId:0)的同group的产品设置不正确:%s -> %s", productModel.get_id(), groupInfo.getProductCodes().toArray().toString(), productCodes.toArray().toString()));
                groupInfo.setProductCodes(productCodes);
                groupInfo.setMainProductCode(poMainCode);
            }
        }

        // 更新group表
        // TODO: 2017/3/7 暂时不更新数据库
        if (flg) {
//            groupInfo.setChannelId("999");
            productGroupService.update(groupInfo);
        }
    }

    /**
     *
     * @param productModel
     * @param errorModel
     * @param cartId
     */
    private void checkProductPlatform(CmsBtProductModel productModel, CmsBtProductErrorModel errorModel, Integer cartId) {

        Boolean flg = false;

        // 检测产品的平台级别的mainProductCode是否正确(默认已group级别的mainProduct为准)
        String productCode = productModel.getCommon().getFields().getCode();

        if (productModel.getPlatform(cartId) == null || productModel.getPlatform(cartId).getSkus() == null) {
            if (productModel.getPlatform(cartId) != null && productModel.getPlatform(cartId).getSkus() == null
                    && 928 != cartId)
                productModel.getPlatforms().remove("P"+ cartId);
            errorModel.getErrors().add(String.format("该产品_id:%s的platforms.P%s为空", productModel.get_id(), cartId));
        } else {
            String mainProductCode = productModel.getPlatform(cartId).getMainProductCode();

            // 检测和设置group级别的主商品
            CmsBtProductGroupModel groupInfo = productGroupService.selectProductGroupByCode(productModel.getChannelId(), productCode, cartId);
            if (groupInfo == null) {
                errorModel.getErrors().add(String.format("该产品对应的group不存在, code:%s, cartId:%s", productCode, cartId));
                try {
                    groupInfo = productGroupService.creatOrUpdateGroup(productModel.getChannelId(), cartId, productModel.getCommon().getFields().getCode(), productModel.getPlatform(cartId).getMainProductCode(), false);
//                    groupInfo.setChannelId("999");
                    productGroupService.update(groupInfo);
                } catch (BusinessException ex) {
                    throw new BusinessException(ex.getMessage());
                }
            }

            if (StringUtils.isEmpty(groupInfo.getMainProductCode())) {
                throw new BusinessException(String.format("该产品对应的group的_id:%s的获取原始主商品code为空", groupInfo.get_id()));
            }

            // 设置platforms的主商品
            if (!groupInfo.getMainProductCode().equals(mainProductCode)) {
                errorModel.getErrors().add(String.format("platforms:mainProductCode(_id:%s,cartId:%s)的主商品设置不正确:%s -> %s", productModel.get_id(), cartId, mainProductCode, groupInfo.getMainProductCode()));
                productModel.getPlatform(cartId).setMainProductCode(groupInfo.getMainProductCode());
                productModel.getPlatform(cartId).setpIsMain(groupInfo.getMainProductCode().equals(productModel.getPlatform(cartId).getMainProductCode())? 1: 0);
            }

            // 检测product的P0以外平台的状态是否正确
            CmsBtProductModel mainProduct = productService.getProductByCode(productModel.getChannelId(), groupInfo.getMainProductCode());
//        JongoQuery query = new JongoQuery();
//        query.setQuery("{\"cartId\": #, \"productCodes\": {$in: #}}");
//        query.setParameters(cartId, groupInfo.getProductCodes());
//        List<CmsBtProductModel> productList = productService.getList(productModel.getChannelId(), query);
//        String numberIid;
//        String pProductId;
//        String pMallId;
//        for (CmsBtProductModel model : productList) {
//            if (StringUtils.isEmpty(model.getPlatform(cartId).getpNumIId()))
//                numberIid = model.getPlatform(cartId).getpNumIId();
//            if (StringUtils.isEmpty(model.getPlatform(cartId).getpProductId()))
//                pProductId = model.getPlatform(cartId).getpProductId();
//            if (StringUtils.isEmpty(model.getPlatform(cartId).getpPlatformMallId()))
//                pMallId = model.getPlatform(cartId).getpPlatformMallId();
//        }



            // 检测group的状态(已group的信息为正确依据)
            if (StringUtils.isEmpty(groupInfo.getNumIId())) {

                if (((CartEnums.Cart.JM.getValue() == cartId)
                        && !StringUtils.isEmpty(groupInfo.getPlatformPid()))
                        || (CartEnums.Cart.JM.getValue() == cartId
                        && !StringUtils.isEmpty(groupInfo.getPlatformMallId()))
                        || !StringUtils.isEmpty(groupInfo.getPublishTime())
                        || !StringUtils.isEmpty(groupInfo.getOnSaleTime())
                        || !StringUtils.isEmpty(groupInfo.getInStockTime())
                        || groupInfo.getPlatformStatus() != null) {
                    flg = true;
                    errorModel.getErrors().add(String.format("group(grup_id: %s),该group未上新,但是平台相关属性有值", groupInfo.get_id()));
                }
                groupInfo.setNumIId("");
                groupInfo.setPlatformPid("");
                groupInfo.setPlatformMallId("");
                groupInfo.setPublishTime("");
                groupInfo.setOnSaleTime("");
                groupInfo.setInStockTime("");
                groupInfo.setPlatformStatus(null);

//                if (!CmsConstants.ProductStatus.Approved.toString().equals(mainProduct.getPlatform(cartId).getStatus())) {
//                    groupInfo.setPlatformActive(null);
//                }
            } else {
                groupInfo.setNumIId(mainProduct.getPlatform(cartId).getpNumIId());
                if (CartEnums.Cart.JM.getValue() == cartId) {
                    if ((StringUtils.isEmpty(groupInfo.getPlatformPid())
                            || (!StringUtils.isEmpty(mainProduct.getPlatform(cartId).getpProductId())
                            && !mainProduct.getPlatform(cartId).getpProductId().equals(groupInfo.getPlatformPid())))) {
                        flg = true;
                        errorModel.getErrors().add(String.format("group(grup_id: %s, cartId: %s),该group已上新,但是平台PlatformPid为空或不正确(%s)", groupInfo.get_id(), cartId, mainProduct.getPlatform(cartId).getpProductId()));
                        groupInfo.setPlatformPid(mainProduct.getPlatform(cartId).getpProductId());
                    }
                } else {

                    flg = true;
                    groupInfo.setPlatformPid("");
                }

                if (CartEnums.Cart.JM.getValue() == cartId
                        && (StringUtils.isEmpty(groupInfo.getPlatformMallId())
                        || (!StringUtils.isEmpty(mainProduct.getPlatform(cartId).getpPlatformMallId())
                        && !mainProduct.getPlatform(cartId).getpPlatformMallId().equals(groupInfo.getPlatformMallId())))) {
                    flg = true;
                    errorModel.getErrors().add(String.format("group(grup_id: %s, cartId: %s),该group已上新,但是平台PlatformMallId为空或不正确(%s)", groupInfo.get_id(), cartId, mainProduct.getPlatform(cartId).getpPlatformMallId()));
                    groupInfo.setPlatformMallId(mainProduct.getPlatform(cartId).getpPlatformMallId());
                } else if (CartEnums.Cart.JM.getValue() != cartId && !StringUtils.isEmpty(groupInfo.getPlatformMallId())) {
                    flg = true;
                    groupInfo.setPlatformMallId("");
                }

                groupInfo.setPublishTime(mainProduct.getPlatform(cartId).getpPublishTime());
                groupInfo.setPlatformStatus(mainProduct.getPlatform(cartId).getpStatus());
            }

            // 更新group表
            // TODO: 2017/3/7 暂时不更新数据库
            if (flg) {
//                groupInfo.setChannelId("999");
                productGroupService.update(groupInfo);
            }

            // 判断该产品的状态是否正确
            // 该产品未上新过
            CmsBtProductModel_Platform_Cart cartInfo = productModel.getPlatform(cartId);

            // 检测status
            if (!CmsConstants.ProductStatus.Approved.name().equals(cartInfo.getStatus())
                    && !CmsConstants.ProductStatus.Pending.name().equals(cartInfo.getStatus())
                    && !CmsConstants.ProductStatus.Ready.name().equals(cartInfo.getStatus())) {
                errorModel.getErrors().add(String.format("platform(_id: %s, cartId: %s), 该商品平台状态不在正确的状态值内(%s)", productModel.get_id(), cartId, cartInfo.getStatus()));
                productModel.getPlatform(cartId).setStatus(CmsConstants.ProductStatus.Pending);
            }

            // 产品未Approve的时候状态check
            if (CmsConstants.ProductStatus.Pending.name().equals(cartInfo.getStatus())
                    || CmsConstants.ProductStatus.Ready.name().equals(cartInfo.getStatus())) {

                // 确认平台未上新过
                if (StringUtils.isEmpty(cartInfo.getpNumIId())) {
                    if(!StringUtils.isEmpty(cartInfo.getpPublishTime())
                            || cartInfo.getpStatus() != null
                            || !StringUtils.isEmpty(cartInfo.getpReallyStatus())
                            || ((CartEnums.Cart.TM.getValue() == cartId
                            || CartEnums.Cart.TG.getValue() == cartId
                            || CartEnums.Cart.TB.getValue() == cartId
                            || CartEnums.Cart.JM.getValue() == cartId)
                            && !StringUtils.isEmpty(cartInfo.getpProductId()))
                            || (CartEnums.Cart.JM.getValue() == cartId
                            && !StringUtils.isEmpty(cartInfo.getpPlatformMallId()))) {
                        errorModel.getErrors().add(String.format("platform(_id: %s, cartId: %s), 该商品平台状态不为Approved的时候,商品相关状态属性不在正确的值内(%s)", productModel.get_id(), cartId, cartInfo.getStatus()));
                        cartInfo.setpProductId("");
                        cartInfo.setpPlatformMallId("");
                        cartInfo.setpPublishTime("");
                        cartInfo.setpPublishError("");
                        cartInfo.setpPublishMessage("");
                        cartInfo.setpReallyStatus("");
                        cartInfo.setpStatus(null);
                    }
                }
                // 平台上新过,但是numberIId不为空
                else {
                    //
                    if(StringUtils.isEmpty(cartInfo.getpPublishTime())
                            || cartInfo.getpStatus() == null
                            || StringUtils.isEmpty(cartInfo.getpReallyStatus())
                            || ((CartEnums.Cart.TM.getValue() == cartId
                            || CartEnums.Cart.TG.getValue() == cartId
                            || CartEnums.Cart.TB.getValue() == cartId
                            || CartEnums.Cart.JM.getValue() == cartId)
                            && StringUtils.isEmpty(cartInfo.getpProductId()))
                            || (CartEnums.Cart.JM.getValue() == cartId
                            && StringUtils.isEmpty(cartInfo.getpPlatformMallId()))) {
                        errorModel.getErrors().add(String.format("platform(_id: %s, cartId: %s), 该商品平台状态不为Approved,但是已经有numIId,商品相关状态属性不在正确的值内(%s)", productModel.get_id(), cartId, cartInfo.getStatus()));
                        cartInfo.setStatus(CmsConstants.ProductStatus.Approved);

                        if (CartEnums.Cart.TM.getValue() == cartId
                                || CartEnums.Cart.TG.getValue() == cartId
                                || CartEnums.Cart.TB.getValue() == cartId
                                || CartEnums.Cart.JM.getValue() == cartId)
                            cartInfo.setpProductId(mainProduct.getPlatform(cartId).getpProductId() != null ? mainProduct.getPlatform(cartId).getpProductId() : groupInfo.getPlatformPid());
                        else
                            cartInfo.setpProductId("");

                        if (CartEnums.Cart.JM.getValue() == cartId)
                            cartInfo.setpPlatformMallId(mainProduct.getPlatform(cartId).getpPlatformMallId() != null ? mainProduct.getPlatform(cartId).getpPlatformMallId() : groupInfo.getPlatformMallId());
                        else
                            cartInfo.setpPlatformMallId("");
//                    cartInfo.setpPublishTime(mainProduct.getPlatform(cartId).getpPublishTime());
//                    cartInfo.setpPublishError("");
//                    cartInfo.setpPublishMessage("");
                        cartInfo.setpReallyStatus(mainProduct.getPlatform(cartId).getpReallyStatus());
                        cartInfo.setpStatus(mainProduct.getPlatform(cartId).getpStatus());
                    }
                }

            }
            // Approve的时候
            else {
                //
                if (StringUtils.isEmpty(cartInfo.getpPublishTime())
                        || (cartInfo.getpStatus() == null && !"928".equals(String.valueOf(cartId)))
                        || (StringUtils.isEmpty(cartInfo.getpNumIId()) && !"928".equals(String.valueOf(cartId)))
                        || (StringUtils.isEmpty(cartInfo.getpReallyStatus()) && !"928".equals(String.valueOf(cartId)))
                        || ((CartEnums.Cart.JM.getValue() == cartId) && StringUtils.isEmpty(cartInfo.getpProductId()))
                        || (CartEnums.Cart.JM.getValue() == cartId && StringUtils.isEmpty(cartInfo.getpPlatformMallId()))) {

                    if (!"Error".equals(cartInfo.getpPublishError())) {

                        errorModel.getErrors().add(String.format("platform(_id: %s, cartId: %s), 该商品平台状态为Approved,但是商品相关状态属性不在正确的值内(%s)", productModel.get_id(), cartId, cartInfo.getStatus()));
//                cartInfo.setStatus(CmsConstants.ProductStatus.Approved);
                        cartInfo.setpNumIId(mainProduct.getPlatform(cartId).getpNumIId());

                        if (CartEnums.Cart.JM.getValue() == cartId)
                            cartInfo.setpProductId(!StringUtils.isEmpty(mainProduct.getPlatform(cartId).getpProductId()) ? mainProduct.getPlatform(cartId).getpProductId() : groupInfo.getPlatformPid());
                        else
                            cartInfo.setpProductId("");

                        if (CartEnums.Cart.JM.getValue() == cartId)
                            cartInfo.setpPlatformMallId(!StringUtils.isEmpty(mainProduct.getPlatform(cartId).getpPlatformMallId()) ? mainProduct.getPlatform(cartId).getpPlatformMallId() : groupInfo.getPlatformMallId());
                        else
                            cartInfo.setpPlatformMallId("");
//                    cartInfo.setpPublishTime(mainProduct.getPlatform(cartId).getpPublishTime());
//                cartInfo.setpPublishError("");
//                cartInfo.setpPublishMessage("");
                        cartInfo.setpReallyStatus(mainProduct.getPlatform(cartId).getpReallyStatus());
                        cartInfo.setpStatus(mainProduct.getPlatform(cartId).getpStatus());
                    }
                }
            }


            // 检测product的P0以外平台的价格范围是否正确

            // 检测product的各平台Sku的数量是否和common.skus的数量是否一致
            if (productModel.getCommon().getSkus().size() != cartInfo.getSkus().size()) {
                errorModel.getErrors().add(String.format("platform:(_id: %s, cartId: %s)的sku数量和common里面的数量不一致", productModel.get_id(), cartId));
                cartInfo.setSkus(addPlatformSkus(productModel.getCommon().getSkus(), cartInfo.getSkus()));
            }

            // 检测product的各平台Sku的数量是否和common.skus的价格是否正确
        }
    }

    /**
     * 获取platforms.P0的主商品
     * @param productList
     * @return
     */
    private String  getOrgMainCode(List<CmsBtProductModel> productList) {
        Map<String, Integer> mainCodes = new HashMap<>();
        ValueComparator bvc =  new ValueComparator(mainCodes);
        productList.forEach(productModel -> {
            if (mainCodes.containsKey(productModel.getPlatform(0).getMainProductCode()))
                mainCodes.put(productModel.getPlatform(0).getMainProductCode(), mainCodes.get(productModel.getPlatform(0).getMainProductCode()) + 1);
            else
                mainCodes.put(productModel.getPlatform(0).getMainProductCode(), 1);
        });

        if (!mainCodes.isEmpty()) {
            TreeMap<String,Integer> sortMap = new TreeMap<>(bvc);
            sortMap.putAll(mainCodes);
            return sortMap.firstEntry().getKey().toString();
        }
        return "";
    }

    /**
     * 补全sku
     * @param commonSkus
     * @param cartSkus
     * @return
     */
    private List<BaseMongoMap<String, Object>> addPlatformSkus(List<CmsBtProductModel_Sku> commonSkus, List<BaseMongoMap<String, Object>> cartSkus) {

        Map<String, BaseMongoMap<String, Object>> commonSkuMap = new HashMap<>();
        cartSkus.forEach(skuInfo -> {
            String skuCode = skuInfo.getStringAttribute("skuCode");
            commonSkuMap.put(skuCode, skuInfo);
        });

        commonSkus.forEach(cmsBtProductModel_sku -> {
            if (!commonSkuMap.containsKey(cmsBtProductModel_sku.getSkuCode())) {
                BaseMongoMap<String, Object> newSku = new BaseMongoMap<String, Object>();
                newSku.put("skuCode", cmsBtProductModel_sku.getSkuCode());
                newSku.put("isSale", false);
                newSku.put("priceChgFlg", "");
                newSku.put("priceRetail", cmsBtProductModel_sku.getPriceRetail());
                newSku.put("priceSale", cmsBtProductModel_sku.getPriceRetail());
                newSku.put("priceMsrp", cmsBtProductModel_sku.getPriceMsrp());
                newSku.put("originalPriceMsrp", cmsBtProductModel_sku.getPriceMsrp());
                newSku.put("priceMsrpFlg", "");
                newSku.put("priceDiffFlg", "");
                cartSkus.add(newSku);
            }
        });

        return cartSkus;
    }

    class ValueComparator implements Comparator<String> {

        Map<String, Integer> base;
        public ValueComparator(Map<String, Integer> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with equals.
        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }
}