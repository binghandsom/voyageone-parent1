package com.voyageone.service.impl.cms;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.jd.open.api.sdk.domain.trip.JosProductService.ProductPrice;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.components.sears.bean.PriceBean;
import com.voyageone.service.bean.cms.product.ProductPriceBean;
import com.voyageone.service.bean.cms.product.ProductSkuPriceBean;
import com.voyageone.service.bean.cms.product.ProductUpdateBean;
import com.voyageone.service.dao.cms.CmsBtStoreOperationHistoryDao;
import com.voyageone.service.dao.cms.CmsBtSxWorkloadDao;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductPriceLogService;
import com.voyageone.service.impl.cms.product.ProductSkuService;
import com.voyageone.service.model.cms.CmsBtStoreOperationHistoryModel;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.apache.avro.generic.GenericData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 店面操作
 *
 * @description
 * @author: holysky.zhao
 * @date: 2016/4/26 14:53
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
@Service
public class StoreOperationService extends BaseService {

    Map<String, String> OPS = ImmutableMap.<String, String>builder()
            .put("rePublish", "重新上新所有商品")
            .put("reUpload1", "重新导入所有Feed商品(清空共通属性)")
            .put("reUpload0", "重新导入所有Feed商品(不清空共通属性)")
            .put("rePublishPrice", "价格同步")
            .build();


    @Resource
    CmsBtProductDao productDao;

    @Resource
    CmsBtFeedInfoDao feedInfoDao;

    @Resource
    CmsBtProductGroupDao productGroupDao;

    @Resource
    CmsBtSxWorkloadDao workloadDao;

    @Resource
    CmsBtStoreOperationHistoryDao historyDao;


    public long countProductsThatCanUploaded(String channelId) {

        return productDao.countByFieldStatusEqualApproved(channelId);
    }

    /**
     * 1. 从cms_bt_product_cxxx中获取素有的fields.status为approve的商品code
     * 2. 更新商品code在cms_bt_product_groups_cxxxx表中找到所有的groupId
     * 3. 插入cms_bt_sx_workload表
     *
     * @param channelId
     */
    public void rePublish(String channelId, String creater) {
        List<CmsBtProductModel> products = productDao.selectByFieldStatusEqualApproved(channelId);

//        productDao.updateSkusPriceSaleEqPriceRetail(channelId, products);


        List<String> productCodes = products.stream().map(p -> p.getFields().getCode()).collect(toList());
        List<CmsBtProductGroupModel> groupModels = productGroupDao.selectGroupIdsByProductCode(channelId, productCodes);
        insertWorkLoad(channelId, creater, groupModels);
        insertHistory(OPS.get("rePublish"), creater);
    }

    private void insertWorkLoad(String channelId, String creater, List<CmsBtProductGroupModel> groupModels) {
        List<CmsBtSxWorkloadModel> models = groupModels.stream().map(groupModel -> {
            CmsBtSxWorkloadModel model = new CmsBtSxWorkloadModel();
            model.setChannelId(channelId);
            model.setCreater(creater);
            model.setCartId(groupModel.getCartId());
            model.setGroup_id(groupModel.getGroupId());
            model.setModifier(creater);
            return model;
        }).collect(toList());
        workloadDao.insertSxWorkloadModels(models);
    }

    @Resource
    ProductPriceLogService priceLogService;

    /**
     * 价格同步
     *
     * @param channelId
     * @param channelId
     */
    public void rePublishPrice(String channelId, String creater) {
        //查询priceRetail和priceSale不相等的product
        List<CmsBtProductModel> products = productDao.selectByRetailSalePriceNonEqual(channelId);

        if (products.size() == 0) {
            $warn("[价格同步] 同步product数量为:0");
            insertHistory(OPS.get("rePublishPrice"), creater);
            return;
        }


        List<ProductPriceBean> productPrices = buildProductPricesFrom(products);
        if (productPrices.size() > 0) {
            skuService.updatePrices(channelId, productPrices, creater);
        }
//这里已有共通的改变价格的方法,直接调用
//        List<BulkUpdateModel> bulkList = Lists.newArrayListWithExpectedSize(products.size());
//        List<ProductPriceBean> productPrices = Lists.newArrayListWithExpectedSize(products.size());
//        products.forEach(product -> { //组合数据
//            productCodes.add(product.getFields().getCode());
//
//            List<CmsBtProductModel_Sku> skus = product.getSkus().stream().map(sku -> {
//                sku.setPriceSale(sku.getPriceRetail());
//                sku.setPriceChgFlg(null);
//                return sku;
//            }).collect(Collectors.toList());
//            BulkUpdateModel bulk = new BulkUpdateModel();
//            bulk.setQueryMap(ImmutableMap.of("prodId", product.getProdId()));
//            bulk.setUpdateMap(ImmutableMap.of("skus", skus));
//            bulkList.add(bulk);
//
//        });
//        productDao.bulkUpdateWithMap(channelId, bulkList, creater, "$set");

        List<String> productCodes = Lists.newArrayListWithExpectedSize(products.size());
        List<CmsBtProductGroupModel> groupModels = productGroupDao.selectGroupIdsByProductCode(channelId, productCodes);

        insertPriceLog(channelId, products, creater);

        //插入价格变更log
        insertWorkLoad(channelId, creater, groupModels);
        $info("[价格同步] 同步product数量为:" + products.size());
        insertHistory(OPS.get("rePublishPrice"), creater);
    }

    /**
     * 生成需要改变价格的ProductPriceBean,如果某个product的某个sku中priceSale和priceRetail不相同那么将其
     * priceSale设为priceRetail后updatePrice
     *
     * @param products
     * @return
     */
    private List<ProductPriceBean> buildProductPricesFrom(List<CmsBtProductModel> products) {
        List<ProductPriceBean> prices = new ArrayList<>();
        products.forEach(product -> {
            List<CmsBtProductModel_Sku> skus = product.getSkus();
            skus = skus == null ? Lists.newArrayList() : skus;

            List<ProductSkuPriceBean> skuPrices = skus.stream()
                    .filter(sku -> sku.getPriceRetail() != sku.getPriceSale())  //过滤价格不相等的
                    .map(sku -> {
                        sku.setPriceSale(sku.getPriceRetail()); //将价格转化为相等
                        return ProductSkuPriceBean.from(sku);
                    }).collect(Collectors.toList());
            if (skuPrices.size() > 0) {
                ProductPriceBean price = new ProductPriceBean();
                price.setProductId(product.getProdId());
                price.setProductCode(product.getFields().getCode());
                price.setSkuPrices(skuPrices);
                prices.add(price);
            }
        }); //end products for
        return prices;
    }

    @Resource
    ProductSkuService skuService;

    //插入priceLog
    private void insertPriceLog(String channelId, List<CmsBtProductModel> products, String creater) {

        products.forEach(product -> {
            boolean hasChanged = false;
            List<CmsBtProductModel_Sku> skus = product.getSkus();
            List<ProductSkuPriceBean> orginSkus = new ArrayList<ProductSkuPriceBean>();
            List<ProductSkuPriceBean> targetSkus = new ArrayList<ProductSkuPriceBean>();

            for (CmsBtProductModel_Sku sku : skus) {
                if (sku.getPriceRetail() != sku.getPriceSale()) {
                    hasChanged = true;
                    ProductSkuPriceBean skuPriceBefore = new ProductSkuPriceBean();
                    skuPriceBefore.setPriceRetail(sku.getPriceRetail());
                    skuPriceBefore.setPriceSale(sku.getPriceSale());
                    skuPriceBefore.setSkuCode(sku.getSkuCode());
                    orginSkus.add(skuPriceBefore);

                    ProductSkuPriceBean skuPriceEnd = new ProductSkuPriceBean();
                    skuPriceEnd.setPriceRetail(sku.getPriceRetail());
                    skuPriceEnd.setPriceSale(sku.getPriceRetail());
                    skuPriceEnd.setSkuCode(sku.getSkuCode());
                    targetSkus.add(skuPriceEnd);
                }
            }

            if (hasChanged) {
                ProductPriceBean priceBefore = new ProductPriceBean();
                priceBefore.setProductId(product.getProdId());
                priceBefore.setProductCode(product.getFields().getCode());
                priceBefore.setSkuPrices(orginSkus);

                ProductPriceBean priceAfter = new ProductPriceBean();
                priceAfter.setProductId(product.getProdId());
                priceAfter.setProductCode(product.getFields().getCode());
                priceAfter.setSkuPrices(targetSkus);
                priceLogService.insertPriceLog(channelId, priceAfter, priceBefore, "价格同步", creater);
            }
        }); //end products for
    }


    /**
     * 重新导入所有feed商品 这里仅仅只是更新标志位,其他逻辑由重新导入job进行
     *
     * @param channelId
     * @param cleanCommonProperties
     * @return
     */
    public boolean reUpload(String channelId, boolean cleanCommonProperties, String creater) {

        if (cleanCommonProperties) { //清除数据
            productDao.deleteAll(channelId);
            productGroupDao.deleteAll(channelId);
        }
        String operationType = cleanCommonProperties ? OPS.get("reUpload1") : OPS.get("reUpload0");
        feedInfoDao.updateAllUpdFlg(channelId, 0);
        insertHistory(operationType, creater);
        return true;
    }

    public List<CmsBtStoreOperationHistoryModel> getHistoryBy(Map<String, Object> params) {
        return historyDao.selectList(params);
    }

    /**
     * 插入操作log
     *
     * @param operationType
     * @param creater
     */
    private void insertHistory(String operationType, String creater) {
        CmsBtStoreOperationHistoryModel history = new CmsBtStoreOperationHistoryModel();
        history.setCreater(creater);
        history.setOperationType(operationType);
        historyDao.insert(history);
    }

}
