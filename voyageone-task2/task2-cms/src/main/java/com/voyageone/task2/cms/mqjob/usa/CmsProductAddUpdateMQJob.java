package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsProductAddUpdateMQMessageBody;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 给一个channel生成新的platform
 * Created by james on 2016/12/9.
 */
@Service
@RabbitListener()
public class CmsProductAddUpdateMQJob extends TBaseMQCmsService<CmsProductAddUpdateMQMessageBody> {

    private final FeedInfoService feedInfoService;

    private final ProductService productService;

    private final SxProductService sxProductService;

    @Autowired
    public CmsProductAddUpdateMQJob(FeedInfoService feedInfoService, ProductService productService, SxProductService sxProductService) {
        this.feedInfoService = feedInfoService;
        this.productService = productService;
        this.sxProductService = sxProductService;
    }


    @Override
    public void onStartup(CmsProductAddUpdateMQMessageBody messageBody) throws Exception {
        if (messageBody.getStatus() != 1) {
            CmsBtFeedInfoModel cmsBtFeedInfoModel = feedInfoService.getProductByCode(messageBody.getChannelId(), messageBody.getCode());
            if (cmsBtFeedInfoModel == null) {
                $info(String.format("追加 code：%s", messageBody.getCode()));
                addProduct(messageBody);
            } else {
                $info(String.format("更新 code：%s", messageBody.getCode()));
                updateProduct(cmsBtFeedInfoModel, messageBody);
            }
        } else {
            $info(String.format("删除 code：%s", messageBody.getCode()));
            delProduct(messageBody);
        }
    }

    //追加产品数据
    private void addProduct(CmsProductAddUpdateMQMessageBody messageBody) {
        CmsBtFeedInfoModel cmsBtFeedInfoModel = new CmsBtFeedInfoModel();
        cmsBtFeedInfoModel.setChannelId(messageBody.getChannelId());
        cmsBtFeedInfoModel.setCreater(messageBody.getSender());
        cmsBtFeedInfoModel.setModifier(messageBody.getSender());
        cmsBtFeedInfoModel.setCode(messageBody.getCode());
        cmsBtFeedInfoModel.setName(messageBody.getName());
        cmsBtFeedInfoModel.setColor(messageBody.getColor());
        cmsBtFeedInfoModel.setStatus(CmsConstants.UsaFeedStatus.New.name());
        cmsBtFeedInfoModel.setUpdFlg(9);
        cmsBtFeedInfoModel.setPriceClientMsrpMax(messageBody.getMsrp());
        cmsBtFeedInfoModel.setPriceClientMsrpMin(messageBody.getMsrp());
        cmsBtFeedInfoModel.setPriceClientRetailMin(messageBody.getMsrp() - 0.01);
        cmsBtFeedInfoModel.setPriceClientRetailMax(messageBody.getMsrp() - 0.01);
        List<CmsBtFeedInfoModel_Sku> skus = new ArrayList<>(messageBody.getSkuList().size());
        cmsBtFeedInfoModel.setSkus(skus);
        cmsBtFeedInfoModel.setApprovePricing("0");
        messageBody.getSkuList().forEach(item -> {
            CmsBtFeedInfoModel_Sku sku = new CmsBtFeedInfoModel_Sku();
            sku.setPriceClientMsrp(messageBody.getMsrp());
            sku.setPriceClientRetail(messageBody.getMsrp() - 0.01);
            sku.setPriceNet(messageBody.getMsrp() - 0.01);
            sku.setSku(item.getSku());
            sku.setClientSku(item.getSku());
            sku.setBarcode(item.getBarcode());
            sku.setIsSale(1);
            sku.setQty(item.getQty());
            sku.setSize(item.getSize());
            skus.add(sku);
        });
        feedInfoService.insertFeedInfo(cmsBtFeedInfoModel);
    }

    // 更新produt
    public void updateProduct(CmsBtFeedInfoModel feedInfo, CmsProductAddUpdateMQMessageBody messageBody) {

        List<CmsBtFeedInfoModel_Sku> delSkus = new ArrayList<>();
        messageBody.getSkuList().forEach(item -> {
            CmsBtFeedInfoModel_Sku feedSku = feedInfo.getSkus().stream().filter(sku -> sku.getSku().equals(item.getSku())).findFirst().orElse(null);
            // sku追加
            if (feedSku == null) {
                $info(String.format("新sku %s", item.getSku()));
                CmsBtFeedInfoModel_Sku newSku = new CmsBtFeedInfoModel_Sku();
                newSku.setSku(item.getSku());
                newSku.setSize(item.getSize());
                newSku.setBarcode(item.getBarcode());
                newSku.setQty(item.getQty());
                newSku.setClientSku(item.getSku());
                newSku.setIsSale(1);
                newSku.setImage(feedInfo.getImage());
                newSku.setPriceNet(feedInfo.getSkus().get(0).getPriceNet());
                newSku.setPriceClientMsrp(feedInfo.getSkus().get(0).getPriceClientMsrp());
                newSku.setPriceClientRetail(feedInfo.getSkus().get(0).getPriceClientRetail());
                newSku.setPriceMsrp(feedInfo.getSkus().get(0).getPriceMsrp());
                newSku.setPriceCurrent(feedInfo.getSkus().get(0).getPriceCurrent());
                feedInfo.getSkus().add(newSku);
            } else {
                $info(String.format("更新sku %s", item.getSku()));
                feedSku.setSize(item.getSize());
                feedSku.setBarcode(item.getBarcode());
                feedSku.setQty(item.getQty());
                feedSku.setIsSale(1);
            }
        });

        // 如果sku的数不一致说明有sku已经被删除了
        if (messageBody.getSkuList().size() != feedInfo.getSkus().size()) {
            List<String> skuNames = messageBody.getSkuList().stream().map(CmsProductAddUpdateMQMessageBody.SkuModel::getSku).collect(Collectors.toList());
            feedInfo.getSkus().forEach(item -> {
                if (!skuNames.contains(item.getSku())) {
                    $info(String.format("删除sku %s", item.getSku()));
                    delSkus.add(item);
                }
            });
            if (ListUtils.notNull(delSkus)) {
                if (feedInfo.getStatus().equals(CmsConstants.UsaFeedStatus.Approved.name()) || feedInfo.getUpdFlg() == 1) {
                    delSkus.forEach(item -> item.setIsSale(0));
                } else {
                    delSkus.forEach(item -> feedInfo.getSkus().remove(item));
                }
            }
        }
        feedInfoService.updateFeedInfo(feedInfo);

        if (feedInfo.getStatus().equals(CmsConstants.UsaFeedStatus.Approved.name()) || feedInfo.getUpdFlg() == 1) {
            updateMastProduct(messageBody, delSkus.stream().map(CmsBtFeedInfoModel_Sku::getSku).collect(Collectors.toList()));
        }
    }

    // 删除code
    private void delProduct(CmsProductAddUpdateMQMessageBody messageBody) {
        CmsBtProductModel product = productService.getProductByCode(messageBody.getChannelId(), messageBody.getCode());
        if (product == null) {
            CmsBtFeedInfoModel feedInfo = feedInfoService.getProductByCode(messageBody.getChannelId(), messageBody.getCode());
            if (feedInfo != null) {
                feedInfoService.delFeedInfo(feedInfo);
            }
        } else {
            $info(String.format("code:%s 已经进入mast了不能删除", messageBody.getCode()));
        }
    }

    // 更新mast product
    private void updateMastProduct(CmsProductAddUpdateMQMessageBody messageBody, List<String> delSkus) {
        CmsBtProductModel cmsBtProductModel = productService.getProductByCode(messageBody.getChannelId(), messageBody.getCode());
        List<String> newSkus = new ArrayList<>();

        //同步common里面的sku
        if (cmsBtProductModel != null) {
            messageBody.getSkuList().forEach(item -> {
                CmsBtProductModel_Sku productSku = cmsBtProductModel.getCommon().getSkus().stream().filter(sku -> sku.getSkuCode().equals(item.getSku())).findFirst().orElse(null);
                if (productSku == null) {
                    CmsBtProductModel_Sku newSku = new CmsBtProductModel_Sku();
                    newSku.setSkuCode(item.getSku());
                    newSku.setSize(item.getSize());
                    newSku.setBarcode(item.getBarcode());
                    newSku.setQty(item.getQty());
                    newSku.setClientSkuCode(item.getSku());
                    newSku.setIsSale(1);
                    newSku.setPriceMsrp(cmsBtProductModel.getCommon().getSkus().get(0).getPriceMsrp());
                    newSku.setPriceRetail(cmsBtProductModel.getCommon().getSkus().get(0).getPriceRetail());
                    newSku.setClientMsrpPrice(cmsBtProductModel.getCommon().getSkus().get(0).getClientMsrpPrice());
                    newSku.setClientMsrpPriceChgFlg(cmsBtProductModel.getCommon().getSkus().get(0).getClientMsrpPriceChgFlg());
                    newSku.setClientNetPrice(cmsBtProductModel.getCommon().getSkus().get(0).getClientNetPrice());
                    newSku.setClientRetailPrice(cmsBtProductModel.getCommon().getSkus().get(0).getClientRetailPrice());
                    cmsBtProductModel.getCommon().getSkus().add(newSku);
                    newSkus.add(item.getSku());
                } else {
                    productSku.setBarcode(item.getBarcode());
                    productSku.setIsSale(1);
                }
            });

            // 把platforms下的sku 在 issale设成1
            cmsBtProductModel.getPlatforms().forEach((cartId, platform) -> {
                if (platform.getCartId() > 0) {
                    messageBody.getSkuList().forEach(item -> {
                        BaseMongoMap<String, Object> pfSku = platform.getSkus().stream().filter(sku -> sku.getStringAttribute("skuCode").equals(item.getSku())).findFirst().orElse(null);
                        if (pfSku != null) {
                            pfSku.setAttribute(CmsBtProductConstants.Platform_SKU_COM.isSale.name(), true);
                        }
                    });
                }
            });
            // 在platforms追加新的sku
            if (ListUtils.notNull(newSkus)) {
                cmsBtProductModel.getPlatforms().forEach((cartId, platform) -> {
                    if (platform.getCartId() > 0) {
                        newSkus.forEach(item -> {
                            BaseMongoMap<String, Object> skuTemp = platform.getSkus().get(0);
                            BaseMongoMap<String, Object> newSku = new BaseMongoMap<>();
                            newSku.put(CmsBtProductConstants.Platform_SKU_COM.skuCode.name(), item);
                            newSku.put(CmsBtProductConstants.Platform_SKU_COM.isSale.name(), true);
                            newSku.put(CmsBtProductConstants.Platform_SKU_COM.priceChgFlg.name(), skuTemp.get(CmsBtProductConstants.Platform_SKU_COM.priceChgFlg.name()));
                            newSku.put(CmsBtProductConstants.Platform_SKU_COM.priceRetail.name(), skuTemp.get(CmsBtProductConstants.Platform_SKU_COM.priceRetail.name()));
                            newSku.put(CmsBtProductConstants.Platform_SKU_COM.priceSale.name(), skuTemp.get(CmsBtProductConstants.Platform_SKU_COM.priceSale.name()));
                            newSku.put(CmsBtProductConstants.Platform_SKU_COM.priceDiffFlg.name(), skuTemp.get(CmsBtProductConstants.Platform_SKU_COM.priceDiffFlg.name()));
                            newSku.put(CmsBtProductConstants.Platform_SKU_COM.priceMsrp.name(), skuTemp.get(CmsBtProductConstants.Platform_SKU_COM.priceMsrp.name()));
                            newSku.put(CmsBtProductConstants.Platform_SKU_COM.originalPriceMsrp.name(), skuTemp.get(CmsBtProductConstants.Platform_SKU_COM.originalPriceMsrp.name()));
                            newSku.put(CmsBtProductConstants.Platform_SKU_COM.priceMsrpFlg.name(), skuTemp.get(CmsBtProductConstants.Platform_SKU_COM.priceMsrpFlg.name()));
                            newSku.put(CmsBtProductConstants.Platform_SKU_COM.confPriceRetail.name(), skuTemp.get(CmsBtProductConstants.Platform_SKU_COM.confPriceRetail.name()));
                            platform.getSkus().add(newSku);
                        });
                    }
                });
            }

            // 在platforms把删除的sku issale设为false
            if (ListUtils.notNull(delSkus)) {
                cmsBtProductModel.getCommon().getSkus().forEach(sku -> {
                    if (delSkus.contains(sku.getSkuCode())) {
                        sku.setIsSale(0);
                    } else {
                        sku.setIsSale(1);
                    }
                });
                cmsBtProductModel.getPlatforms().forEach((cartId, platform) -> {
                    if (platform.getCartId() > 0) {
                        platform.getSkus().forEach(sku -> {
                            if (delSkus.contains(sku.getStringAttribute("skuCode"))) {
                                sku.setAttribute("isSale", false);
                            } else {
                                sku.setAttribute("isSale", true);
                            }
                        });
                    }
                });
            }
            productService.updateProductFeedToMaster(cmsBtProductModel.getChannelId(), cmsBtProductModel, getTaskName(), "wms new item");
            sxProductService.insertSxWorkLoad(cmsBtProductModel, getTaskName());
        }
    }
}
