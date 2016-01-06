package com.voyageone.web2.cms.service;

import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.ProductPriceModel;
import com.voyageone.web2.sdk.api.domain.ProductSkuPriceModel;
import com.voyageone.web2.sdk.api.request.ProductUpdatePriceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gubuchun 15/12/28
 * @version 2.0.0
 */
@Service
public class CmsPriceUpdateService {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    /**
     * 更新价格,group价格暂不计算，以主商品为准
     */
    public void updatePriceByCode(String channelId, String code, List<CmsBtProductModel_Sku> skuModelList, String modifier) {
        ProductUpdatePriceRequest requestModel = new ProductUpdatePriceRequest(channelId);
        ProductPriceModel productPriceModel = new ProductPriceModel();

        for (CmsBtProductModel_Sku skuModel : skuModelList) {
            //设置修改后的价格
            ProductSkuPriceModel skuPriceModel = new ProductSkuPriceModel();
            skuPriceModel.setSkuCode(skuModel.getSkuCode());
            skuPriceModel.setPriceMsrp(skuModel.getPriceMsrp());
            skuPriceModel.setPriceRetail(skuModel.getPriceRetail());
            skuPriceModel.setPriceSale(skuModel.getPriceSale());
            productPriceModel.addSkuPrice(skuPriceModel);
        }

        productPriceModel.setProductCode(code);
        requestModel.addProductPrices(productPriceModel);
        productPriceModel.setPriceChange(1);

        voApiClient.execute(requestModel);
    }

}
