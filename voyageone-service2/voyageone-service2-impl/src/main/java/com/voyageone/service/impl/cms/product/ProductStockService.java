package com.voyageone.service.impl.cms.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.service.bean.cms.stock.CartChangedStockBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author piao
 * @description 处理WMS =》 CMS 的库存
 * @date 2017-3-24
 */
public class ProductStockService extends BaseService {

    @Autowired
    ProductService productService;
    @Autowired
    CmsBtProductDao cmsBtProductDao;

    /**
     * 批量更新产品库存 方法待优化
     * 不着急发布   added by piao
     */
    public List<CmsBtOperationLogModel_Msg> updateProductStock(List<CartChangedStockBean> stockList) {

        List<BulkUpdateModel> bulkList = new ArrayList<>();
        CartChangedStockBean firstStock = stockList.get(0);
        List<CmsBtOperationLogModel_Msg> result = new ArrayList<>();

        for (CartChangedStockBean stockInfo : stockList) {

            CmsBtProductModel productInfo = productService.getProductByCode(stockInfo.getChannelId(), stockInfo.getItemCode());

            if (productInfo != null) {

                Integer quantity = null;

                if (stockInfo.getCartId() == 0) {

                    for (CmsBtProductModel_Sku skuModel : productInfo.getCommon().getSkus()) {
                        if (skuModel.getSkuCode().equals(stockInfo.getSku())) {
                            skuModel.setQty(stockInfo.getQty());
                            break;
                        }
                    }

                    for (CmsBtProductModel_Sku skuModel : productInfo.getCommon().getSkus()) {
                        quantity += skuModel.getQty();
                    }

                    HashMap<String, Object> updateMap = new HashMap<>();
                    updateMap.put("common.skus.$.qty", stockInfo.getQty());
                    updateMap.put("common.fields.quantity", quantity);

                    HashMap<String, Object> queryMap = new HashMap<>();
                    queryMap.put("channelId", stockInfo.getChannelId());
                    queryMap.put("common.skus.skuCode", stockInfo.getSku());

                    bulkList.add(createBulkUpdateModel(updateMap, queryMap));

                } else {

                    for (BaseMongoMap<String, Object> skuModel : productInfo.getPlatform(stockInfo.getCartId()).getSkus()) {
                        if (skuModel.getStringAttribute("skuCode").equals(stockInfo.getSku())) {
                            skuModel.setAttribute("qty", stockInfo.getQty());
                            break;
                        }
                    }

                    for (BaseMongoMap<String, Object> skuModel : productInfo.getPlatform(stockInfo.getCartId()).getSkus()) {
                        quantity += Integer.parseInt(skuModel.getAttribute("qty"));
                    }

                    HashMap<String, Object> updateMap = new HashMap<>();
                    updateMap.put(String.format("platforms.P%s.skus.$.qty", stockInfo.getCartId()), stockInfo.getQty());
                    updateMap.put(String.format("platforms.P%s.fields.quantity", stockInfo.getCartId()), quantity);

                    HashMap<String, Object> queryMap = new HashMap<>();
                    queryMap.put("channelId", stockInfo.getChannelId());
                    queryMap.put(String.format("platforms.P%s.skus.skuCode", stockInfo.getCartId()), stockInfo.getSku());

                    bulkList.add(createBulkUpdateModel(updateMap, queryMap));

                }

                //记录操作结果
                CmsBtOperationLogModel_Msg msgInfo = new CmsBtOperationLogModel_Msg();
                msgInfo.setSkuCode(stockInfo.getSku());
                msgInfo.setMsg("产品库存更新成功！");
                result.add(msgInfo);
            }

        }

        cmsBtProductDao.bulkUpdateWithMap(firstStock.getChannelId(), bulkList, "库存更新MQ", "$set");

        return result;
    }

    private BulkUpdateModel createBulkUpdateModel(HashMap<String, Object> updateMap,
                                                  HashMap<String, Object> queryMap) {
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        return model;
    }

}
