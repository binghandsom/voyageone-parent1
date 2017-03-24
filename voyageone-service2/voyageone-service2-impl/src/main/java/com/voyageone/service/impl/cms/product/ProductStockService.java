package com.voyageone.service.impl.cms.product;

import com.voyageone.common.CmsConstants;
import com.voyageone.service.bean.cms.stock.CartChangedStockBean;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
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

                StringBuffer updateStr = new StringBuffer();

                /**cartId = 0 : 表示全平台更新,
                 * cartId != 0 : 表示更新具体平台*/
                if(stockInfo.getCartId() == 0){

                    productInfo.getPlatforms().forEach((_cart, platform) -> {
                        Integer cartId = platform.getCartId();

                        if (cartId < CmsConstants.ACTIVE_CARTID_MIN)
                            return;

                        //updateStr.append(String.format("platforms.P%s.lock", cartId), lock);

                    });

                }else{

                }

            }

        }

    }

}
