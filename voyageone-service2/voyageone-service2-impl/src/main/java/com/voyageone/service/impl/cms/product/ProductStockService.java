package com.voyageone.service.impl.cms.product;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.bean.cms.stock.CartChangedStockBean;
import com.voyageone.service.dao.cms.mongo.CmsBtOperationLogDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author piao
 * @description 处理WMS =》 CMS 的库存
 * @date 2017-3-24
 */
@Service
public class ProductStockService extends BaseService {

    @Autowired
    ProductService productService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private ProductPlatformService productPlatformService;
    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;
    @Autowired
    CmsBtProductDao cmsBtProductDao;

    /**
     * WMS->CMS 库存更新
     * @param stockList 如果cartId=0是渠道库存更新，否则是店铺库存更新
     * @param modifier 更新人
     */
    public List<CmsBtOperationLogModel_Msg> updateProductStock(List<CartChangedStockBean> stockList, String modifier) {
        List<CmsBtOperationLogModel_Msg> failList = null;
        if (CollectionUtils.isNotEmpty(stockList)) {

            Map<String, List<BulkUpdateModel>> bulkUpdateModelMap = new HashMap<>();
            failList = new ArrayList<>();
            Map<String, CmsBtProductModel> productModelMap = new HashMap<>();

            for (CartChangedStockBean stockBean : stockList) {
                // 当前渠道ID、平台ID
                String channelId = stockBean.getChannelId();
                Integer cartId = stockBean.getCartId();

                if("001".equals(channelId)){
                    stockBean.setItemCode(stockBean.getItemCode().toLowerCase());
                    stockBean.setSku(stockBean.getSku().toLowerCase());
                }

                CmsBtProductModel productModel = productService.getProductByCode(channelId, stockBean.getItemCode());
                if (productModel != null) {
                    $info(String.format("channelId=%s cartId=%d code=%s sku=%s qty=%d", channelId, cartId==null?0:cartId, stockBean.getItemCode(), stockBean.getSku(), stockBean.getQty()));
                    HashMap<String, Object> updateMap = new HashMap<>();
                    HashMap<String, Object> queryMap = new HashMap<>();

                    Integer quantity = 0;
                    if (cartId == null || cartId.intValue() == 0) {
                        // cartId为0，则表示按渠道更新库存
                        for (CmsBtProductModel_Sku skuModel : productModel.getCommon().getSkus()) {
                            if (skuModel.getSkuCode().equals(stockBean.getSku())) {
                                skuModel.setQty(stockBean.getQty());
                                quantity += stockBean.getQty();
                            } else {
                                quantity += skuModel.getQty() == null ? 0 : skuModel.getQty();
                            }
                        }

                        // 更新数据
                        updateMap.put("common.skus.$.qty", stockBean.getQty());
                        updateMap.put("common.fields.quantity", quantity);
                        queryMap.put("common.skus.skuCode", stockBean.getSku());

                    } else {
                        CmsBtProductModel_Platform_Cart platformCart = productModel.getPlatform(stockBean.getCartId());
                        if(platformCart == null || platformCart.getSkus() == null){
                            continue;
                        }
                        // cartId不为0，表示更新具体某个平台某个店铺的库存
                        for (BaseMongoMap<String, Object> skuModel : productModel.getPlatform(stockBean.getCartId()).getSkus()) {
                            if (skuModel.getStringAttribute("skuCode").equals(stockBean.getSku())) {
                                skuModel.setAttribute("qty", stockBean.getQty());
                                quantity += stockBean.getQty();
                            } else {
                                quantity += skuModel.getIntAttribute("qty");
                            }
                        }

                        // 更新数据
                        updateMap.put(String.format("platforms.P%s.skus.$.qty", stockBean.getCartId()), stockBean.getQty());
                        updateMap.put(String.format("platforms.P%s.quantity", stockBean.getCartId()), quantity);
                        queryMap.put(String.format("platforms.P%s.skus.skuCode", stockBean.getCartId()), stockBean.getSku());
                    }
                    try {

                        BulkWriteResult writeResult = cmsBtProductDao.bulkUpdateWithMap(channelId, Collections.singletonList(createBulkUpdateModel(updateMap, queryMap)), modifier, "$set");
//                        $info(String.format("(channelId=%s, cartId=%d, code=%s, sku=%s)库存更新结果：%s",
//                                stockBean.getChannelId(), stockBean.getCartId(), stockBean.getItemCode(), stockBean.getSku(), JacksonUtil.bean2Json(writeResult)));
                        CmsBtProductModel_Platform_Cart platform = null;
                        if (cartId != null && cartId != 0
                                && CmsConstants.ProductStatus.Approved.name().equalsIgnoreCase((platform = productModel.getPlatform(cartId)).getStatus())
                                && stockBean.getItemCode().equalsIgnoreCase(platform.getMainProductCode())
                                && quantity == 0) {
                            // 从cms_mt_channel_config查询AUTO_SWITCH_MASTER_PRODUCT设置，如果config_value1为1则自动切换平台主商品
                            CmsChannelConfigBean channelConfig = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.AUTO_SWITCH_MASTER_PRODUCT, String.valueOf(cartId));
                            if(channelConfig == null){
                                channelConfig = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.AUTO_SWITCH_MASTER_PRODUCT);
                            }
                            //CmsChannelConfigBean channelConfig = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.AUTO_SWITCH_MASTER_PRODUCT);
                            if(channelConfig != null && "1".equals(channelConfig.getConfigValue1())) {
                                // 查询当前Code所在的Group下cartId库存最大值且Approved
                                CmsBtProductGroupModel cmsBtProductGroup = productGroupService.selectProductGroupByCode(channelId, stockBean.getItemCode(), cartId);
                                if (cmsBtProductGroup == null) {
                                    continue;
                                }

                                // Group下非当前Code的其他产品Code集合
                                List<String> codes = cmsBtProductGroup.getProductCodes().stream().filter(code -> !code.equalsIgnoreCase(stockBean.getItemCode())).collect(Collectors.toList());
                                // 当前Group下只有一个Code
                                if (codes.isEmpty()) {
                                    continue;
                                }

                                // Group下非当前Code的其他产品Map集合,KV分别是Code和产品Model
                                Map<String, CmsBtProductModel> productMap = new HashMap<>();
                                // 待切换成的主商品Code
                                String newMastCode = null;
                                CmsBtProductModel newMastProduct = null;
                                CmsBtProductModel_Platform_Cart newPlatFormCart = null;
                                int platformQuantity = 0;
                                for (String code : codes) {
                                    CmsBtProductModel product = productService.getProductByCode(channelId, code);
                                    if (product != null) {
                                        productMap.put(code, product);

                                        if ((newPlatFormCart = product.getPlatform(cartId)) != null
                                                && CmsConstants.ProductStatus.Approved.name().equals(newPlatFormCart.getStatus())
                                                && newPlatFormCart.getIntAttribute("quantity") > platformQuantity) {
                                            newMastCode = code;
                                            newMastProduct = product;
                                            platformQuantity = newPlatFormCart.getIntAttribute("quantity");
                                        }
                                    }
                                }

                                if (newMastCode != null) {

                                    // 当前平台原来的主商品Code
                                    String oldMastCode = platform.getMainProductCode();
                                    platform.setpIsMain(0);// 把product表中对应的平台的pIsMain设0
                                    platform.setMainProductCode(newMastCode);
                                    productPlatformService.updateProductPlatformWithSx(channelId, productModel.getProdId(), platform, modifier, "切换主商品", false);
                                    String comment = "WMS->CMS推送平台库存 主商品发生变化 主商品：" + newMastCode;
                                    productStatusHistoryService.insert(channelId, stockBean.getItemCode(), platform.getStatus(), cartId, EnumProductOperationType.ChangeMastProduct, comment, modifier);
                                    $info(String.format("(Code=%s, CartId=%d切换主商品(原%s->新%s))", stockBean.getItemCode(), cartId, oldMastCode, newMastCode));


                                    newPlatFormCart.setpIsMain(1);//把productCode的所对应的product表中对应的平台的pIsMain设1
                                    newPlatFormCart.setMainProductCode(newMastCode);
                                    productPlatformService.updateProductPlatformWithSx(channelId, newMastProduct.getProdId(), newPlatFormCart, modifier, "切换主商品", false);
                                    String newComment = "WMS->CMS推送平台库存 设置为主商品 主商品：" + newMastCode;
                                    productStatusHistoryService.insert(channelId, newMastCode, newPlatFormCart.getStatus(), cartId, EnumProductOperationType.ChangeMastProduct, newComment, modifier);
                                    $info(String.format("(Code=%s, CartId=%d设置新主商品(原%s->新%s))", newMastCode, cartId, oldMastCode, newMastCode));

                                    cmsBtProductGroup.setMainProductCode(newMastCode);//把group表中的mainProduct替换成productCode
                                    cmsBtProductGroup.setModifier(modifier);
                                    cmsBtProductGroup.setModified(DateTimeUtil.getNowTimeStamp());
                                    productGroupService.update(cmsBtProductGroup);
                                    $info(String.format("(Code=%s, CartId=%d切换主商品时更新Group主商品Code(原%s->新%s))", stockBean.getItemCode(), cartId, oldMastCode, newMastCode));

                                    // 新主商品Code已经处理过了
                                    codes.remove(newMastCode);

                                    if (!codes.isEmpty()) {
                                        for (String code : codes) {
                                            CmsBtProductModel product = productMap.get(code);
                                            CmsBtProductModel_Platform_Cart pform = product.getPlatform(cartId);
                                            pform.setMainProductCode(newMastCode);
                                            productPlatformService.updateProductPlatformWithSx(channelId, product.getProdId(), pform, modifier, "切换主商品", false);

                                            comment = "WMS->CMS推送平台库存 切换同Group对应平台主商品：" + newMastCode;
                                            productStatusHistoryService.insert(channelId, code, pform.getStatus(), cartId, EnumProductOperationType.ChangeMastProduct, comment, modifier);

                                            $info(String.format("(Code=%s, CartId=%d切换同Group商品对应平台主商品(原%s->新%s))",
                                                    code, cartId, StringUtils.isBlank(pform.getMainProductCode()) ? " " : platform.getMainProductCode(), newMastCode));
                                        }
                                    }
                                }
                            }
                        }

                    } catch (Exception e) {
                        CmsBtOperationLogModel_Msg failMsg = new CmsBtOperationLogModel_Msg();
                        failMsg.setSkuCode(stockBean.getSku());
                        failMsg.setMsg("WMS->CMS渠道/店铺库存更新Mongodb异常");
                        failList.add(failMsg);
                        $error(String.format("(channelId=%s, cartId=%d, code=%s, sku=%s)库存更新差异",
                                stockBean.getChannelId(), stockBean.getCartId(), stockBean.getItemCode(), stockBean.getSku()));
                    }
                } else {
                    CmsBtOperationLogModel_Msg failMsg = new CmsBtOperationLogModel_Msg();
                    failMsg.setSkuCode(stockBean.getSku());
                    failMsg.setMsg("WMS->CMS渠道/店铺库存更新时根据产品Code在CMS查不到商品");
                    failList.add(failMsg);
                    $info(String.format("channelId=%s cartId=%d code=%s 在CMS查不到商品", channelId, cartId==null?0:cartId, stockBean.getItemCode()));
                }
            }
//            if (!bulkUpdateModelMap.isEmpty()) {
//                for (Map.Entry<String, List<BulkUpdateModel>> entry : bulkUpdateModelMap.entrySet()) {
//                    BulkWriteResult writeResult = cmsBtProductDao.bulkUpdateWithMap(entry.getKey(), entry.getValue(), "CmsStockCartChangedStockMQJob", "$set");
//                    $info(String.format("渠道(chanelId=%s)库存更新结果: %s", entry.getKey(), JacksonUtil.bean2Json(writeResult)));
//                }
//            }
        }
        return failList;
    }

    private BulkUpdateModel createBulkUpdateModel(HashMap<String, Object> updateMap,
                                                  HashMap<String, Object> queryMap) {
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        return model;
    }

}
