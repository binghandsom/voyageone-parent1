package com.voyageone.service.impl.cms.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.common.CmsConstants;
import com.voyageone.service.bean.cms.stock.CartChangedStockBean;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author piao
 * @description 处理WMS =》 CMS 的库存
 * @date 2017-3-24
 */
public class ProductStockService extends BaseService {

    @Autowired
    ProductService productService;

    /**
     * channelId
     * cartId
     * sku
     * itemCode
     * qty
     */
    public void updateProductStock(List<CartChangedStockBean> stockList) {

        for (CartChangedStockBean stockInfo : stockList) {

            CmsBtProductModel productInfo = productService.getProductByCode(stockInfo.getChannelId(), stockInfo.getItemCode());

            if(productInfo != null){

                StringBuffer updateStr = new StringBuffer("{$set:{");

                /**cartId:0  表示全平台更新,
                    cartId :!0 表示更新具体平台*/
                if(stockInfo.getCartId() == 0){

                    List<CmsBtProductModel_Sku> skus = productInfo.getCommon().getSkus();

                    for(CmsBtProductModel_Sku skuModel : skus){
                        if(skuModel.getSkuCode().equals(stockInfo.getSku())){
                            skuModel.setQty(stockInfo.getQty());
                            updateStr.append("{'common.sku':#}");
                            break;
                        }
                    }

                }else{

                    //productInfo.getPlatform(stockInfo.getCartId()).getFields().getAttribute("");

                }

            }

        }

    }

    public List<CmsBtOperationLogModel_Msg> batchUpdateCommonQty(BulkJongoUpdateList productBulkList,List<CartChangedStockBean> stockList){

        for(CartChangedStockBean stockModel : stockList){

        }

        return null;
    }

    public List<CmsBtOperationLogModel_Msg> batchUpdatePlatFormQty(BulkJongoUpdateList productBulkList,List<CartChangedStockBean> stockList){


        return null;
    }

}
