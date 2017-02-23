package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.ProductPlatformService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductStatusHistoryService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * if 主类目人工设置FLG（common.catConf = 1 ）
    -》只修改主类目
    else if 主类目人工设置FLG（common.catConf 不存在 或 = 0）
    -》更新 主类目，产品分类，使用人群，税号个人，税号跨境申报
    -》设置 主类目人工设置FLG（common.catConf = 1 ）
 * Created by james on 2016/12/19.
 */
@Service
@RabbitListener(queues = CmsMqRoutingKey.CMS_BATCH_CmsBatchSetMainCategoryJob)
public class CmsBatchSetMainCategoryMqService extends BaseMQCmsService {

    final ProductService productService;

    final CmsBtProductDao cmsBtProductDao;

    private final ProductStatusHistoryService productStatusHistoryService;

    private final PriceService priceService;

    private final ProductPlatformService productPlatformService;



    @Autowired
    public CmsBatchSetMainCategoryMqService(ProductService productService, CmsBtProductDao cmsBtProductDao, ProductStatusHistoryService productStatusHistoryService, PriceService priceService, ProductPlatformService productPlatformService) {
        this.productService = productService;
        this.cmsBtProductDao = cmsBtProductDao;
        this.productStatusHistoryService = productStatusHistoryService;
        this.priceService = priceService;
        this.productPlatformService = productPlatformService;
    }

//    if 主类目人工设置FLG（common.catConf = 1 ）
//            -》只修改主类目
//else if 主类目人工设置FLG（common.catConf 不存在 或 = 0）
//            -》更新 主类目，产品分类，使用人群，税号个人，税号跨境申报
//-》设置 主类目人工设置FLG（common.catConf = 1 ）
    @Override
    protected void onStartup(Map<String, Object> requestMap) throws Exception {
        // 获取参数
        String mCatId = StringUtils.trimToNull((String) requestMap.get("catId"));
        String mCatPathCn = StringUtils.trimToNull((String) requestMap.get("catPath"));
        String mCatPathEn = StringUtils.trimToNull((String) requestMap.get("catPathEn"));
        List<String> productCodes = (List<String>) requestMap.get("prodIds");
        String userName = (String) requestMap.get("userName");
        String channelId = (String) requestMap.get("channelId");
        String productTypeEn = (String) requestMap.get("productType");
        String sizeTypeEn = (String) requestMap.get("sizeType");
        String productTypeCn = (String) requestMap.get("productTypeCn");
        String sizeTypeCn = (String) requestMap.get("sizeTypeCn");
        String hscodeName8 = (String) requestMap.get("hscodeName8");
        String hscodeName10 = (String) requestMap.get("hscodeName10");

        if (mCatId == null || mCatPathCn == null || ListUtils.isNull(productCodes)) {
            $warn("切换类目 缺少参数 params=" + requestMap.toString());
            return;
        }
        $info(String.format("channel=%s mCatPath=%s prodCodes size=%d", channelId, mCatPathCn, productCodes.size()));
        productCodes.forEach(code -> {
                    CmsBtProductModel cmsBtProductModel = productService.getProductByCode(channelId, code);
                    if (cmsBtProductModel != null) {

                        HashMap<String, Object> queryMap = new HashMap<>();
                        queryMap.put("common.fields.code", code);
                        List<BulkUpdateModel> bulkList = new ArrayList<>();
                        HashMap<String, Object> updateMap = new HashMap<>();

                        updateMap.put("common.catId", mCatId);
                        updateMap.put("common.catPath", mCatPathCn);
                        updateMap.put("common.catPathEn", mCatPathEn);
                        updateMap.put("common.catConf", "1");
                        updateMap.put("common.fields.categoryStatus", "1");
                        updateMap.put("common.fields.categorySetter", userName);
                        updateMap.put("common.fields.categorySetTime", DateTimeUtil.getNow());
                        if (StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getOriginalTitleCn()) || !"1".equalsIgnoreCase(cmsBtProductModel.getCommon().getFields().getTranslateStatus())) {
                            // 设置商品中文名称（品牌 + 空格 + Size Type中文 + 空格 + 主类目叶子级中文名称）
                            String[] temp = mCatPathCn.split(">");
                            String titleCn = String.format("%s %s %s",getString(cmsBtProductModel.getCommon().getFields().getBrand()), getString(sizeTypeCn), temp[temp.length-1]);
                            updateMap.put("common.fields.originalTitleCn", titleCn);
                        }
//                        if (StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getProductType())) {
//                            updateMap.put("common.fields.productType", productTypeEn);
//                        }
//                        if (StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getProductTypeCn())) {
//                            updateMap.put("common.fields.productTypeCn", productTypeCn);
//                        }
//                        if (StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getSizeType())) {
//                            updateMap.put("common.fields.sizeType", sizeTypeEn);
//                        }
//                        if (StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getSizeTypeCn())) {
//                            updateMap.put("common.fields.sizeTypeCn", sizeTypeCn);
//                        }
//                        if (!StringUtil.isEmpty(hscodeName8) && StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getHsCodePrivate())) {
//                            updateMap.put("common.fields.hsCodePrivate", hscodeName8);
//                        }
//                        if (!StringUtil.isEmpty(hscodeName10) && StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getHsCodeCross())) {
//                            updateMap.put("common.fields.hsCodeCross", hscodeName10);
//                        }
                        if("0".equalsIgnoreCase(cmsBtProductModel.getCommon().getCatConf())){
                            updateMap.put("common.fields.productType", getString(productTypeEn));
                            updateMap.put("common.fields.productTypeCn", getString(productTypeCn));
                            updateMap.put("common.fields.sizeType", getString(sizeTypeEn));
                            updateMap.put("common.fields.sizeTypeCn", getString(sizeTypeCn));
                            updateMap.put("common.fields.hsCodePrivate", getString(hscodeName8));
                            updateMap.put("common.fields.hsCodeCross", getString(hscodeName10));
                            if (!StringUtil.isEmpty(hscodeName8)) {
                                updateMap.put("common.fields.hsCodeStatus", "1");
                                updateMap.put("common.fields.hsCodeSetter", userName);
                                updateMap.put("common.fields.hsCodeSetTime", DateTimeUtil.getNow());
                            }else{
                                updateMap.put("common.fields.hsCodeStatus", "0");
                                updateMap.put("common.fields.hsCodeSetter", "");
                                updateMap.put("common.fields.hsCodeSetTime", "");
                            }
                        }else {
                            if(StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getHsCodePrivate()) && !StringUtil.isEmpty(hscodeName8)){
                                updateMap.put("common.fields.hsCodePrivate", getString(hscodeName8));
                                updateMap.put("common.fields.hsCodeStatus", "1");
                                updateMap.put("common.fields.hsCodeSetter", userName);
                                updateMap.put("common.fields.hsCodeSetTime", DateTimeUtil.getNow());
                            }
                            if(StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getHsCodeCross()) && !StringUtil.isEmpty(hscodeName10)){
                                updateMap.put("common.fields.hsCodeCross", getString(hscodeName10));
                            }
                        }
                        BulkUpdateModel model = new BulkUpdateModel();
                        model.setUpdateMap(updateMap);
                        model.setQueryMap(queryMap);
                        bulkList.add(model);
                        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, userName, "$set");
                        if(!productService.compareHsCode(hscodeName8, cmsBtProductModel.getCommon().getFields().getHsCodePrivate())) {
                            try{
                                CmsBtProductModel newProduct = productService.getProductByCode(channelId, code);
                                // 税号从无到有的场后同步最终售价
                                if (StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getHsCodePrivate())) {
                                    priceService.setPrice(newProduct, true);
                                } else {
                                    priceService.setPrice(newProduct, false);
                                }
                                newProduct.getPlatforms().forEach((s, platform) -> {
                                    if (platform.getCartId() != 0) {
                                        productPlatformService.updateProductPlatform(channelId, newProduct.getProdId(), platform, userName, false, EnumProductOperationType.changeMainCategory, "批量修改主类目税号变更", true);
                                    }
                                });
                            }catch (Exception e){
                                $error(e);
                            }
                        }
                        productStatusHistoryService.insert(channelId, code, "", 0, EnumProductOperationType.changeMainCategory, "修改主类目为" + mCatPathCn, userName);
                    }
                }
        );

//        List<Integer> cartList = null;
//        if (cartIdObj == null || cartIdObj == 0) {
//            // 表示全平台更新
//            // 店铺(cart/平台)列表
//            List<TypeChannelBean> cartTypeList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");
//            cartList = cartTypeList.stream().map((cartType) -> NumberUtils.toInt(cartType.getValue())).collect(Collectors.toList());
//        } else {
//            cartList = new ArrayList<>(1);
//            cartList.add(cartIdObj);
//        }
//        for (Integer cartId : cartList) {
//            updObj = new JongoUpdate();
//            updObj.setQuery("{'common.fields.code':{$in:#},'platforms.P#':{$exists:true},'platforms.P#.pAttributeStatus':{$in:[null,'','0']}}");
//            updObj.setQueryParameters(prodCodes, cartId, cartId);
//
//            boolean isInCatFlg = false;
//            String pCatId = null;
//            String pCatPath = null;
//            for (Map pCatObj : pCatList) {
//                if (cartId.toString().equals(pCatObj.get("cartId"))) {
//                    isInCatFlg = true;
//                    pCatId = StringUtils.trimToNull((String) pCatObj.get("catId"));
//                    pCatPath = StringUtils.trimToNull((String) pCatObj.get("catPath"));
//                    break;
//                }
//            }
//            if (isInCatFlg && (pCatId == null || pCatPath == null)) {
//                $debug(String.format("changeProductCategory 该平台未匹配此主类目 cartid=%d, 主类目path=%s, 主类目id=%s, platformCategory=%s", cartId, mCatPath, mCatId, pCatList.toString()));
//            } else if (!isInCatFlg) {
//                $debug(String.format("changeProductCategory 该平台未匹配此主类目 cartid=%d, 主类目path=%s, 主类目id=%s,", cartId, mCatPath, mCatId));
//            }
//            if (pCatId != null || pCatPath != null) {
//                updObj.setUpdate("{$set:{'platforms.P#.pCatId':#,'platforms.P#.pCatPath':#,'platforms.P#.pCatStatus':'1'}}");
//                updObj.setUpdateParameters(cartId, pCatId, cartId, pCatPath, cartId);
//                rs = productService.updateMulti(updObj, channelId);
//                $info("切换类目 product更新结果 " + rs.toString());
//            }
//
//        }
    }

    private String getString(String str){
        if(str == null) return "";
        return str;
    }
}
