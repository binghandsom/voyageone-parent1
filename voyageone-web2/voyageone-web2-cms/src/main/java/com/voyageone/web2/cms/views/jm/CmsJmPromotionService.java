package com.voyageone.web2.cms.views.jm;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.service.bean.cms.jumei.ProductImportBean;
import com.voyageone.service.bean.cms.jumei.SkuImportBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.daoext.cms.CmsBtJmProductDaoExt;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionImportTask3Service;
import com.voyageone.service.model.cms.CmsBtJmProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagJmModuleExtensionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * 聚美活动新增商品
 * Created by dell on 2016/3/18.
 */
@Service
class CmsJmPromotionService extends BaseViewService {
    private final CmsBtProductDao productDao;  //用于获取mongo中的产品信息
    private final CmsBtJmProductDaoExt cmsBtJmProductDaoExt;
    private final CmsBtJmPromotionImportTask3Service cmsBtJmPromotionImportTask3Service;
    private final CmsAdvanceSearchService advanceSearchService;
    private final TagService tagService;
    private final CmsBtJmPromotionService jmPromotionService;

    @Autowired
    public CmsJmPromotionService(CmsBtProductDao productDao, CmsAdvanceSearchService advanceSearchService,
                                 CmsBtJmProductDaoExt cmsBtJmProductDaoExt,
                                 CmsBtJmPromotionImportTask3Service cmsBtJmPromotionImportTask3Service,
                                 TagService tagService, CmsBtJmPromotionService jmPromotionService) {
        this.productDao = productDao;
        this.advanceSearchService = advanceSearchService;
        this.cmsBtJmProductDaoExt = cmsBtJmProductDaoExt;
        this.cmsBtJmPromotionImportTask3Service = cmsBtJmPromotionImportTask3Service;
        this.tagService = tagService;
        this.jmPromotionService = jmPromotionService;
    }

    /**
     * 新增产品列表到聚美的产品项目中
     */
    Map<String, Object> addProductionToPromotion(List<Long> productIds, CmsBtJmPromotionModel promotion, String channelId,
                                                 Double discount, Integer priceType, String tagName, String tagId,
                                                 Integer isSelAll, CmsSessionBean cmsSession) {
        if (isSelAll == null) {
            isSelAll = 0;
        }
        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            productIds = advanceSearchService.getProductIdList(channelId, cmsSession);
        }
        Map<String, Object> rsMap = new HashMap<>();
        if (productIds == null || productIds.size() == 0) {
            $warn("LOG00010:no product for adding to jumei promotion");
            rsMap.put("ecd", 1);
            return rsMap;
        }
        List<CmsBtProductModel> orginProducts = productDao.selectProductByIds(productIds, channelId);
        if (orginProducts == null || orginProducts.size() == 0) {
            $warn(String.format("addJMPromotion 根据prodid查询不到商品 channelId=%s, prodids=%s", channelId, productIds.toString()));
            rsMap.put("ecd", 2);
            return rsMap;
        }
        List<CmsBtProductModel> products = new ArrayList<>();

        // 检查之前有没有上新到聚美上面
        List<String> errCodes = new ArrayList<>();
        List<String> productCodes = new ArrayList<>();
        orginProducts.forEach(item -> productCodes.add(item.getCommon().getFields().getCode()));
        List<CmsBtJmProductModel> cmsBtJmProductModels = cmsBtJmProductDaoExt.selectByProductCodeListChannelId(productCodes, channelId);
        if (cmsBtJmProductModels == null || orginProducts.size() != cmsBtJmProductModels.size()) {
            for (CmsBtProductModel orginProduct : orginProducts) {
                if (orginProduct.getCommon() == null || orginProduct.getCommon().getFields() == null || orginProduct.getCommon().getFields().getCode() == null) {
                    $warn("addJMPromotion 商品数据不完整 " + orginProduct.toString());
                    continue;
                }

                boolean flg = false;
                if (cmsBtJmProductModels != null) {
                    for (CmsBtJmProductModel cmsBtJmProductModel : cmsBtJmProductModels) {
                        if (orginProduct.getCommon().getFields().getCode().equalsIgnoreCase(cmsBtJmProductModel.getProductCode())) {
                            flg = true;
                            products.add(orginProduct);
                            break;
                        }
                    }
                }
                if (!flg) {
                    errCodes.add(orginProduct.getCommon().getFields().getCode());
                }
            }
        } else {
            products = orginProducts;
        }

        if (products.size() == 0) {
            $warn(String.format("addJMPromotion 没有商品可以加入活动 channelId=%s, prodids=%s", channelId, productIds.toString()));
            rsMap.put("ecd", 3);
            rsMap.put("errlist", errCodes);
            return rsMap;
        }

        List<ProductImportBean> listProductImport = new ArrayList<>();
        List<SkuImportBean> listSkuImport = new ArrayList<>();

        // 设置批量更新product的tag
        List<BulkUpdateModel> bulkList = new ArrayList<>();

        products.forEach(product -> { //pal
            ProductImportBean productImportBean = buildProductFrom(product, promotion);
            productImportBean.setPromotionTag(tagName);
            productImportBean.setDiscount(discount);
            listProductImport.add(productImportBean);
            listSkuImport.addAll(buildSkusFrom(product, discount, priceType));

            if (!product.getTags().contains(tagId))
                bulkList.add(buildBulkUpdateTag(product, tagId));
        });
        List<Map<String, Object>> listSkuErrorMap = new ArrayList<>();//;错误行集合
        List<Map<String, Object>> listProducctErrorMap = new ArrayList<>();//错误行集合
        // 插入jm的promotion信息
        try {
            cmsBtJmPromotionImportTask3Service.saveImport(promotion, listProductImport, listSkuImport, listProducctErrorMap, listSkuErrorMap, promotion.getModifier(), false);
        } catch (IllegalAccessException ex) {
            $error("添加商品到聚美活动一览失败", ex);
            rsMap.put("ecd", 4);
            return rsMap;
        }

        // 批量更新product表
        if (bulkList.size() > 0) {
            BulkWriteResult rs = productDao.bulkUpdateWithMap(channelId, bulkList, null, "$set", true);
            $debug("addJMPromotion 批量更新结果 " + rs.toString());
        }
        rsMap.put("ecd", 0);
        rsMap.put("cnt", products.size());
        rsMap.put("errlist", errCodes);
        return rsMap;
    }

    /**
     * 获取活动聚美模块数据
     */
    List<Map<String, Object>> getPromotionTagModules(int jmPromotionId) {

        CmsBtJmPromotionModel jmPromotionModel = jmPromotionService.select(jmPromotionId);

        List<CmsBtTagModel> tagModelList = tagService.getListByParentTagId(jmPromotionModel.getRefTagId());

        return tagModelList.stream().map(tagModel -> {

            CmsBtTagJmModuleExtensionModel jmModuleExtensionModel = tagService.getJmModule(tagModel);

            if (jmModuleExtensionModel == null) {
                jmModuleExtensionModel = tagService.createJmModuleExtension(tagModel);
                tagService.addJmModule(jmModuleExtensionModel);
            }

            Map<String, Object> tagMap = new HashMap<>();

            tagMap.put("tag", tagModel);
            tagMap.put("module", jmModuleExtensionModel);

            return tagMap;
        }).collect(toList());
    }

    private ProductImportBean buildProductFrom(CmsBtProductModel model, CmsBtJmPromotionModel promotion) {
        CmsBtProductModel_Field fields = model.getCommon().getFields();
        ProductImportBean bean = new ProductImportBean();
        bean.setAppId(promotion.getActivityAppId());
        bean.setPcId(promotion.getActivityPcId());
        bean.setLimit(0);
        bean.setProductCode(fields.getCode());
        return bean;
    }

    /**
     * buildSkusFrom
     *
     * @param model     CmsBtProductModel
     * @param discount  折扣,这里是正折扣,即直接计算而不是用减法,如 10元,discount为0.7那么 就是7元,而不是3元
     * @param priceType 1 表示用官方价(Msrp)打折,2表示用销售价(Sale Price)
     * @return List
     */
    private List<SkuImportBean> buildSkusFrom(CmsBtProductModel model, Double discount, Integer priceType) {
        final Integer priceTypeCopy = priceType;

        return model.getPlatform(CartEnums.Cart.JM).getSkus().stream().map(oldSku -> {

            Double priceMsrp = oldSku.getDoubleAttribute("priceMsrp");
            Double priceSale = oldSku.getDoubleAttribute("priceSale");
            Double priceRetail = oldSku.getDoubleAttribute("priceRetail");
            SkuImportBean bean = new SkuImportBean();
            bean.setProductCode(model.getCommon().getFields().getCode());
            bean.setSkuCode(oldSku.getStringAttribute("skuCode"));
            bean.setMarketPrice(priceMsrp);
            double finalPrice = 0;
            if (discount != null) {
                final Double discountCopy = discount > 1 || discount < 0 ? 1 : discount;
                if (priceTypeCopy == 1)//MSRP价格
                {
                    finalPrice = priceMsrp * discountCopy;
                } else if (priceTypeCopy == 2)//销售价
                {
                    finalPrice = priceSale * discountCopy;
                } else if (priceTypeCopy == 3)//指导价格
                {
                    finalPrice = priceRetail * discountCopy;
                }
                finalPrice = Math.ceil(finalPrice);
            } else {
                finalPrice = priceSale;
            }
            bean.setDealPrice(finalPrice);
            bean.setDiscount(discount);
            return bean;
        }).collect(toList());
    }

    /**
     * 设置批量更新product的tags标签
     */
    private BulkUpdateModel buildBulkUpdateTag(CmsBtProductModel model, String tagId) {
        HashMap<String, Object> bulkQueryMap = new HashMap<>();
        if (model.getCommon() != null && model.getCommon().size() > 0) {
            bulkQueryMap.put("common.fields.code", model.getCommon().getFields().getCode());
        } else {
            bulkQueryMap.put("common.fields.code", model.getCommon().getFields().getCode());
        }

        // 设置更新值
        HashMap<String, Object> bulkUpdateMap = new HashMap<>();
        List<String> newTags = model.getTags();
        newTags.add(tagId);

        bulkUpdateMap.put("tags", newTags);

        // 设定批量更新条件和值
        BulkUpdateModel bulkUpdateModel = new BulkUpdateModel();
        bulkUpdateModel.setUpdateMap(bulkUpdateMap);
        bulkUpdateModel.setQueryMap(bulkQueryMap);
        return bulkUpdateModel;
    }

    //------------------聚美活动新增商品end-------------------------------------------------------------------------------
}

