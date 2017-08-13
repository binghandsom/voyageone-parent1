package com.voyageone.task2.cms.mqjob;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.dao.cms.mongo.CmsBtTempProductCategoryDao;
import com.voyageone.service.dao.cms.mongo.CmsBtTempProductDao;
import com.voyageone.service.impl.cms.SellerCatService;
import com.voyageone.service.impl.cms.prices.IllegalPriceConfigException;
import com.voyageone.service.impl.cms.prices.PriceCalculateException;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductPlatformService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsCartAddMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 给一个channel生成新的platform
 * Created by james on 2016/12/9.
 */
@Service
@RabbitListener()
public class CmsCartAddMQJob extends TBaseMQCmsService<CmsCartAddMQMessageBody> {

    private final static int pageSize = 200;
    private final ProductService productService;
    private final ProductGroupService productGroupService;
    private final PriceService priceService;
    private final ProductPlatformService productPlatformService;
    private final CmsBtTempProductDao cmsBtTempProductDao;
    private final CmsBtTempProductCategoryDao cmsBtTempProductCategoryDao;
    private final SellerCatService sellerCatService;
    private final Integer[] usCarts = {5, 6, 7, 8, 9, 10, 11, 12};


    @Autowired
    public CmsCartAddMQJob(ProductService productService,
                           ProductGroupService productGroupService,
                           PriceService priceService,
                           ProductPlatformService productPlatformService,
                           CmsBtTempProductDao cmsBtTempProductDao,
                           CmsBtTempProductCategoryDao cmsBtTempProductCategoryDao,
                           SellerCatService sellerCatService) {
        this.productService = productService;
        this.productGroupService = productGroupService;
        this.priceService = priceService;
        this.productPlatformService = productPlatformService;
        this.cmsBtTempProductDao = cmsBtTempProductDao;
        this.cmsBtTempProductCategoryDao = cmsBtTempProductCategoryDao;
        this.sellerCatService = sellerCatService;
    }

    @Override
    public void onStartup(CmsCartAddMQMessageBody messageBody) {
        String channelId = messageBody.getChannelId();
        Integer cartId =  messageBody.getCartId();
        Boolean isSingle = messageBody.getSingle() == null ? false : messageBody.getSingle();

        Assert.isTrue(channelId != null && cartId != null, "参数错误!");

        long sumCnt = productService.countByQuery("{}", null, channelId);
        super.count = sumCnt;

        long pageCnt = sumCnt / pageSize + (sumCnt % pageSize > 0 ? 1 : 0);
        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();

        for (int pageNum = 1; pageNum <= pageCnt; pageNum++) {
            JongoQuery jongoQuery = new JongoQuery();

            jongoQuery.setSkip((pageNum - 1) * pageSize);
            jongoQuery.setLimit(pageSize);
            List<CmsBtProductModel> cmsBtProductModels = productService.getList(channelId, jongoQuery);
            for (int i = 0; i < cmsBtProductModels.size(); i++) {
                $info(String.format("%d/%d  code:%s", (pageNum - 1) * pageSize + i + 1, sumCnt, cmsBtProductModels.get(i).getCommon().getFields().getCode()));
                if(Arrays.asList(usCarts).contains(cartId))
                    createUsPlatform(cmsBtProductModels.get(i), cartId, isSingle, failList);
                else
                    createPlatform(cmsBtProductModels.get(i), cartId, isSingle, failList);
            }
        }
        if (failList.size() > 0) {
            //写业务错误日志
            String comment = String.format("处理总件数(%s), 处理失败件数(%s)", sumCnt, failList.size());
            cmsSuccessIncludeFailLog(messageBody, comment, failList);
        }
    }

    private void createPlatform(CmsBtProductModel cmsBtProductModel, Integer cartId, Boolean isSingle, List<CmsBtOperationLogModel_Msg> failMap) {

        if (cmsBtProductModel.getPlatform(cartId) != null) return;

        String code = cmsBtProductModel.getCommon().getFields().getCode();

        CmsBtProductModel_Platform_Cart platform = new CmsBtProductModel_Platform_Cart();
        // cartId
        platform.setCartId(cartId);
        // 设定是否主商品
        // 如果是聚美或者独立官网的话，那么就是一个Code对应一个Group
        CmsBtProductGroupModel group;


        if (isSingle) {
            group = productGroupService.createNewGroup(cmsBtProductModel.getChannelId(), cartId, code, false);
            group.setModifier(getTaskName());
            group.setCreater(getTaskName());
            platform.setpIsMain(1);
            platform.setMainProductCode(cmsBtProductModel.getCommon().getFields().getCode());
        } else {
            group = productGroupService.selectProductGroupByModelCodeAndCartId(cmsBtProductModel.getChannelId(), cmsBtProductModel.getCommon().getFields().getModel(), cartId.toString());
            if (group != null) {
                group.getProductCodes().add(code);
                group.setModifier(getTaskName());
                platform.setpIsMain(0);
            } else {
                group = productGroupService.createNewGroup(cmsBtProductModel.getChannelId(), cartId, code, false);
                group.setModifier(getTaskName());
                group.setCreater(getTaskName());
                platform.setpIsMain(1);
            }
            platform.setMainProductCode(group.getMainProductCode());
        }


        // 平台类目状态(新增时)
        platform.setpCatStatus("0");  // add desmond 2016/07/05

        // 商品状态
        if (928 != cartId)
            platform.setStatus(CmsConstants.ProductStatus.Pending.toString());
        else {
            platform.setStatus(CmsConstants.ProductStatus.Approved.toString());
        }
        // 平台属性状态(新增时)
        platform.setpAttributeStatus("0");    // add desmond 2016/07/05

        // 平台sku
        List<BaseMongoMap<String, Object>> skuList = new ArrayList<>();
        for (CmsBtProductModel_Sku sku : cmsBtProductModel.getCommon().getSkus()) {
            BaseMongoMap<String, Object> skuInfo = new BaseMongoMap<>();
            skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.skuCode.name(), sku.getSkuCode());
            skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.isSale.name(), true);
            skuList.add(skuInfo);
        }

        // 设置lock状态
        platform.setLock(cmsBtProductModel.getLock());

        platform.setSkus(skuList);
        cmsBtProductModel.setPlatform(cartId, platform);
        try {
            priceService.setPrice(cmsBtProductModel, cartId, true);
        } catch (IllegalPriceConfigException | PriceCalculateException e) {
            $error(e);
            CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
            errorInfo.setSkuCode(code);
            errorInfo.setMsg(String.format("调用PriceService.setPrice异常, code=%s, cartId=%d, errmsg=%s", code, cartId, e.getMessage()));
            failMap.add(errorInfo);
        }
        productGroupService.update(group);
        productPlatformService.updateProductPlatform(cmsBtProductModel.getChannelId(), cmsBtProductModel.getProdId(), platform, getTaskName(), false, EnumProductOperationType.CreateNewCart, EnumProductOperationType.CreateNewCart.getName(), false, 3);
    }


    private void createUsPlatform(CmsBtProductModel cmsBtProductModel, Integer cartId, Boolean isSingle, List<CmsBtOperationLogModel_Msg> failMap) {

        JongoQuery query = new JongoQuery();
        query.setQuery("{\"code\": #}");
        query.setParameters(cmsBtProductModel.getCommon().getFields().getCode());
        CmsBtTempProductModel tempProductModel = cmsBtTempProductDao.selectOneWithQuery(query, cmsBtProductModel.getChannelId());

        if (cmsBtProductModel.getUsPlatform(cartId) != null) return;

        String code = cmsBtProductModel.getCommon().getFields().getCode();

        CmsBtProductModel_Platform_Cart platform = new CmsBtProductModel_Platform_Cart();

        platform.setCartId(cartId);
        platform.setLock("0");
        platform.setIsSale("1");
        Double msrp = 0.0D;
        Double price = 0.0D;
        Double thridPrice = 0.0D;
        if (tempProductModel != null ) {

            BaseMongoMap<String, Object> newFileds = new BaseMongoMap<>();


            if (!StringUtils.isEmpty(tempProductModel.getMSRP()))
                msrp = Double.valueOf(tempProductModel.getMSRP());
            if (!StringUtils.isEmpty(tempProductModel.getPrice()))
                price = Double.valueOf(tempProductModel.getPrice());
            if (!StringUtils.isEmpty(tempProductModel.getThirdPrice()))
                thridPrice = Double.valueOf(tempProductModel.getThirdPrice());
            else
                thridPrice = price;

            platform.setpPriceMsrpSt(msrp);
            platform.setpPriceMsrpEd(msrp);
            if (cartId == 8 || cartId == 9 || cartId == 12 || cartId == 10) {
                platform.setpPriceRetailSt(price);
                platform.setpPriceRetailEd(price);
                platform.setpPriceSaleSt(price);
                platform.setpPriceSaleEd(price);
            } else if (cartId == 11) {
                platform.setpPriceRetailSt(msrp);
                platform.setpPriceRetailEd(msrp);
                platform.setpPriceSaleSt(msrp);
                platform.setpPriceSaleEd(msrp);
            } else {
                platform.setpPriceRetailSt(thridPrice);
                platform.setpPriceRetailEd(thridPrice);
                platform.setpPriceSaleSt(thridPrice);
                platform.setpPriceSaleEd(thridPrice);
            }


            cmsBtProductModel.getCommon().getFields().setAbstract(tempProductModel.getAbstract());
            cmsBtProductModel.getCommon().getFields().setAccessory(tempProductModel.getAccessory());
            cmsBtProductModel.getCommon().getFields().setGoogleCategory(tempProductModel.getGoogleCategoryPath());
            cmsBtProductModel.getCommon().getFields().setGoogleDepartment(tempProductModel.getGoogleDepartmentPath());
            cmsBtProductModel.getCommon().getFields().setPriceGrabberCategory(tempProductModel.getPriceGrabberCategory());
            cmsBtProductModel.getCommon().getFields().setUrlKey(tempProductModel.getUrlKey());
            if (!StringUtils.isEmpty(tempProductModel.getColorMap()))
                cmsBtProductModel.getCommon().getFields().setColorMap(tempProductModel.getColorMap().toLowerCase());
            cmsBtProductModel.getCommon().getFields().setTaxable(tempProductModel.isTaxable());

            if (cartId == 6
                    || cartId == 7
                    || cartId == 8
                    || cartId == 9
                    || cartId == 11
                    || cartId == 12) {
                newFileds.setAttribute("newArrival", tempProductModel.getIsNewArrival());
                newFileds.setAttribute("orderLimitCount", String.valueOf(Double.valueOf(tempProductModel.getOrderLimitCount()).intValue()));
                newFileds.setAttribute("phoneOrderOnly", tempProductModel.getPhoneOrderOnlyMessage());


                // 设置类目相关内容
                JongoQuery productCategoryQuery = new JongoQuery();
                productCategoryQuery.setQuery("{\"code\": #}");
                productCategoryQuery.setParameters(cmsBtProductModel.getCommon().getFields().getCode());
                List<CmsBtTempProductCategoryModel> productCategories = cmsBtTempProductCategoryDao.select(productCategoryQuery, cmsBtProductModel.getChannelId());
//                if (8 == cartId)
                setUsPlatformSellerCats(platform, productCategories);

                if (cartId == 8 || cartId == 9) {
                    newFileds.setAttribute("seoTitle", tempProductModel.getSeoTitle());
                    newFileds.setAttribute("seoDescription", tempProductModel.getSeoDescription());
                    newFileds.setAttribute("seoKeywords", tempProductModel.getSeoKeywords());
                    newFileds.setAttribute("freeShipping", String.valueOf(Double.valueOf(tempProductModel.getFreeShippingType()).intValue()));
                    newFileds.setAttribute("rewardEligible", String.valueOf(Double.valueOf(tempProductModel.getRewardEligible()).intValue()));
                    newFileds.setAttribute("discountEligible", String.valueOf(Double.valueOf(tempProductModel.getDiscountEligible()).intValue()));
                    newFileds.setAttribute("sneakerheadPlus", "0");
                    newFileds.setAttribute("sneakerfolio", "1".equalsIgnoreCase(String.valueOf(Double.valueOf(tempProductModel.getOnsale()).intValue())) ? "0" : "1");
                    if ("1".equals(tempProductModel.getMagento()) || "2".equals(tempProductModel.getMagento())) {
                        platform.setStatus("Approved");
                        platform.setpStatus("OnSale");
                        platform.setpReallyStatus("OnSale");
                    } else if ("4".equals(tempProductModel.getMagento())) {
                        platform.setStatus("Pending");
                        platform.setpStatus("");
                        platform.setpReallyStatus("");
                        platform.setLock("1");
                        platform.setIsSale("0");
                    } else {
                        platform.setStatus("Pending");
                        platform.setpStatus("");
                        platform.setpReallyStatus("");
                    }
                } else {
                    newFileds.setAttribute("seoTitle", null);
                    newFileds.setAttribute("seoDescription", null);
                    newFileds.setAttribute("seoKeywords", null);
                }

                if (6 == cartId || 7 == cartId) {

                    if ("1".equals(tempProductModel.getAmazon())) {
                        platform.setStatus("Approved");
                        platform.setpStatus("OnSale");
                        platform.setpReallyStatus("OnSale");
                    } else if ("4".equals(tempProductModel.getMagento())) {
                        platform.setLock("1");
                        platform.setIsSale("0");
                        platform.setStatus("Pending");
                        platform.setpStatus("");
                        platform.setpReallyStatus("");
                    } else {
                        platform.setStatus("Pending");
                        platform.setpStatus("");
                        platform.setpReallyStatus("");
                    }
                }

                if (cartId == 11 ) {
                    if ("1".equals(tempProductModel.getApprovedForIkicks())) {
                        platform.setStatus("Approved");
                        platform.setpStatus("OnSale");
                        platform.setpReallyStatus("OnSale");

                    } else {
                        platform.setStatus("Pending");
                        platform.setpStatus("");
                        platform.setpReallyStatus("");
                    }
                }
            }

            if (cartId == 5) {

                if (!StringUtils.isEmpty(tempProductModel.getAmazonPath())) {
                    platform.setpCatId(tempProductModel.getAmazonId());
                    platform.setpCatPath(tempProductModel.getAmazonPath());
                    platform.setpCatStatus("1");
                } else {
                    platform.setpCatId("");
                    platform.setpCatPath("");
                    platform.setpCatStatus("0");
                }

                newFileds.setAttribute("sellerFulfilledPrime", "0");
                if ("1".equals(tempProductModel.getAmazon())) {
                    platform.setStatus("Approved");
                    platform.setpStatus("OnSale");
                    platform.setpReallyStatus("OnSale");
                } else if ("4".equals(tempProductModel.getMagento())) {
                    platform.setLock("1");
                    platform.setIsSale("0");
                    platform.setStatus("Pending");
                    platform.setpStatus("");
                    platform.setpReallyStatus("");
                } else {
                    platform.setStatus("Pending");
                    platform.setpStatus("");
                    platform.setpReallyStatus("");
                }
            }
            platform.setFields(newFileds);
        }

        List<BaseMongoMap<String, Object>> skuList = new ArrayList<>();
        for (CmsBtProductModel_Sku sku : cmsBtProductModel.getCommon().getSkus()) {
            BaseMongoMap<String, Object> skuInfo = new BaseMongoMap<>();
            skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.skuCode.name(), sku.getSkuCode());
            skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.isSale.name(), true);
            skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.clientMsrpPrice.name(), msrp);
            skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.barcode.name(), sku.getBarcode());
            skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.size.name(), sku.getSize());
            skuInfo.put("clientSize", sku.getClientSize());
            skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.weight.name(), sku.getWeight());
            skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.weightUnit.name(), StringUtils.isEmpty(sku.getWeightUnit()) ? "lb": sku.getWeightUnit());
            skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.clientMsrpPrice.name(), msrp);

            if (cartId == 8 || cartId == 9 || cartId == 12 || cartId == 10) {
                skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.clientNetPrice.name(), price);
                skuInfo.put("clientRetailPrice", price);
            } else if (cartId == 11) {
                skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.clientNetPrice.name(), msrp);
                skuInfo.put("clientRetailPrice", msrp);
            } else {
                skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.clientNetPrice.name(), thridPrice);
                skuInfo.put("clientRetailPrice", thridPrice);
            }
            skuList.add(skuInfo);
        }
        platform.setSkus(skuList);

        // todo 美国不需要生成group数据
        // 设定是否主商品
        // 如果是聚美或者独立官网的话，那么就是一个Code对应一个Group
//        CmsBtProductGroupModel group;
//
//        if (isSingle) {
//            group = productGroupService.createNewGroup(cmsBtProductModel.getChannelId(), cartId, code, false);
//            group.setModifier(getTaskName());
//            group.setCreater(getTaskName());
//            platform.setpIsMain(1);
//            platform.setMainProductCode(cmsBtProductModel.getCommon().getFields().getCode());
//        } else {
//            group = productGroupService.selectProductGroupByModelCodeAndCartId(cmsBtProductModel.getChannelId(), cmsBtProductModel.getCommon().getFields().getModel(), cartId.toString());
//            if (group != null) {
//                group.getProductCodes().add(code);
//                group.setModifier(getTaskName());
//                platform.setpIsMain(0);
//            } else {
//                group = productGroupService.createNewGroup(cmsBtProductModel.getChannelId(), cartId, code, false);
//                group.setModifier(getTaskName());
//                group.setCreater(getTaskName());
//                platform.setpIsMain(1);
//            }
//            platform.setMainProductCode(group.getMainProductCode());
//        }

        cmsBtProductModel.setUsPlatform(cartId, platform);
//        productGroupService.update(group);

        JongoUpdate updateQuery = new JongoUpdate();
        updateQuery.setQuery("{\"prodId\": #}");
        updateQuery.setQueryParameters(cmsBtProductModel.getProdId());
        updateQuery.setUpdate("{$set: {\"common.fields\": #, \"usPlatforms.P#\": #}}");
        updateQuery.setUpdateParameters(cmsBtProductModel.getCommon().getFields(), cartId, platform);

        productService.updateFirstProduct(updateQuery, cmsBtProductModel.getChannelId());
//        productPlatformService.updateProductPlatform(cmsBtProductModel.getChannelId(), cmsBtProductModel.getProdId(), platform, getTaskName(), false, EnumProductOperationType.CreateNewCart, EnumProductOperationType.CreateNewCart.getName(), false, 3);
    }

    private void setUsPlatformSellerCats (CmsBtProductModel_Platform_Cart platform, List<CmsBtTempProductCategoryModel> productCategories) {

        platform.setpCatId("");
        platform.setpCatPath("");
        platform.setpCatStatus("0");

        List<String> categories = productCategories.stream().map((pc) -> pc.getCatIdPath()).collect(Collectors.toList());
        List<String> newCategories = new ArrayList<>();
        categories.sort((pc1, pc2) -> pc1.compareTo(pc2) * -1);

        categories.forEach(item -> {

            if (newCategories.stream().filter(cate -> cate.indexOf(item + "-") == 0).findFirst().orElse(null) == null )
                newCategories.add(item);

        });

        List<CmsBtProductModel_SellerCat> sellerCats = new ArrayList<>();
        for (CmsBtTempProductCategoryModel productCategory : productCategories) {

            if (newCategories.contains(productCategory.getCatIdPath())) {

                if (productCategory.isMainCategory()) {
                    platform.setpCatId(productCategory.getCatId());
                    platform.setpCatPath(productCategory.getCatPath());
                    platform.setpCatStatus("1");
                }
                CmsBtProductModel_SellerCat sellerCat = new CmsBtProductModel_SellerCat();
                sellerCat.setcId(productCategory.getCatId());
                sellerCat.setcName(productCategory.getCatName());
                sellerCat.setcIds(Arrays.asList(productCategory.getCatIdPath().split("-")));
                sellerCat.setcNames(Arrays.asList(productCategory.getCatPath().split(">")));
                sellerCats.add(sellerCat);
            }
        }
        if (8 == platform.getCartId())
            platform.setSellerCats(sellerCats);
    }
}
