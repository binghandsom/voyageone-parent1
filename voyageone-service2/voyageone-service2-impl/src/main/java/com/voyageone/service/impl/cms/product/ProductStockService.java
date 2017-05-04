package com.voyageone.service.impl.cms.product;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.stock.CartChangedStockBean;
import com.voyageone.service.dao.cms.mongo.CmsBtOperationLogDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    CmsBtProductDao cmsBtProductDao;

    /**
     * WMS->CMS 库存更新
     * @param stockList 如果cartId=0是渠道库存更新，否则是店铺库存更新
     */
    public List<CmsBtOperationLogModel_Msg> updateProductStock(List<CartChangedStockBean> stockList) {
        List<CmsBtOperationLogModel_Msg> failList = null;
        if (CollectionUtils.isNotEmpty(stockList)) {

            Map<String, List<BulkUpdateModel>> bulkUpdateModelMap = new HashMap<>();
            failList = new ArrayList<>();

            for (CartChangedStockBean stockBean : stockList) {
                // 当前渠道ID、平台ID
                String channelId = stockBean.getChannelId();
                Integer cartId = stockBean.getCartId();

                // 根据产品Code获取产品
                CmsBtProductModel productModel = productService.getProductByCode(channelId, stockBean.getItemCode());
                if (productModel != null) {

                    HashMap<String, Object> updateMap = new HashMap<>();
                    HashMap<String, Object> queryMap = new HashMap<>();

                    Integer quantity = 0;
                    if (cartId == null || cartId.intValue() == 0) {
                        // cartId为0，则表示按渠道更新库存
                        for (CmsBtProductModel_Sku skuModel : productModel.getCommon().getSkus()) {
                            if (skuModel.getSkuCode().equals(stockBean.getSku())) {
                                quantity += stockBean.getQty();
                            } else {
                                quantity += skuModel.getQty() == null ? 0 : skuModel.getQty();
                            }
                        }

                        // 更新数据
                        updateMap.put("common.skus.$.qty", stockBean.getQty());
                        updateMap.put("common.fields.quantity", quantity);
                        queryMap.put("channelId", stockBean.getChannelId());
                        queryMap.put("common.skus.skuCode", stockBean.getSku());
                        List<CmsBtProductModel> productModel1 = productService.getProductBySkuCode(stockBean.getChannelId(),stockBean.getSku());
                        $info("");

                    } else {
                        // cartId不为0，表示更新具体某个平台某个店铺的库存
                        for (BaseMongoMap<String, Object> skuModel : productModel.getPlatform(stockBean.getCartId()).getSkus()) {
                            if (skuModel.getStringAttribute("skuCode").equals(stockBean.getSku())) {
                                quantity += stockBean.getQty();
                            } else {
                                quantity += skuModel.getIntAttribute("qty");
                            }
                        }

                        // 更新数据
                        updateMap.put(String.format("platforms.P%s.skus.$.qty", stockBean.getCartId()), stockBean.getQty());
                        updateMap.put(String.format("platforms.P%s.fields.quantity", stockBean.getCartId()), quantity);
                        queryMap.put("channelId", stockBean.getChannelId());
                        queryMap.put(String.format("platforms.P%s.skus.skuCode", stockBean.getCartId()), stockBean.getSku());

                    }
                    if (bulkUpdateModelMap.containsKey(channelId)) {
                        bulkUpdateModelMap.get(channelId).add(createBulkUpdateModel(updateMap, queryMap));
                    } else {
                        List<BulkUpdateModel> tempBulkUpdateModelList = new ArrayList<>();
                        tempBulkUpdateModelList.add(createBulkUpdateModel(updateMap, queryMap));
                        bulkUpdateModelMap.put(channelId, tempBulkUpdateModelList);

                    }
                } else {
                    CmsBtOperationLogModel_Msg failMsg = new CmsBtOperationLogModel_Msg();
                    failMsg.setMsg("WMS->CMS渠道/店铺库存更新时根据产品Code在CMS查不到商品");
                    failList.add(failMsg);
                }
            }
            if (!bulkUpdateModelMap.isEmpty()) {
                for (Map.Entry<String, List<BulkUpdateModel>> entry : bulkUpdateModelMap.entrySet()) {
                    BulkWriteResult writeResult = cmsBtProductDao.bulkUpdateWithMap(entry.getKey(), entry.getValue(), "CmsStockCartChangedStockMQJob", "$set");
                    $info(String.format("渠道(chanelId=%s)库存更新结果: %s", entry.getKey(), JacksonUtil.bean2Json(writeResult)));
                }
            }

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
