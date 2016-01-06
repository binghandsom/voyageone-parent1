package com.voyageone.web2.cms.service;

import com.voyageone.cms.service.model.CmsBtProductModel_Field;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.ProductPriceModel;
import com.voyageone.web2.sdk.api.domain.ProductSkuPriceModel;
import com.voyageone.web2.sdk.api.request.ProductUpdatePriceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        List<BigDecimal> msrpPriceList = new ArrayList<>();
        List<BigDecimal> retailPriceList = new ArrayList<>();
        List<BigDecimal> salePriceList = new ArrayList<>();
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

            msrpPriceList.add(new BigDecimal(skuModel.getPriceMsrp()));
            retailPriceList.add(new BigDecimal(skuModel.getPriceRetail()));
            salePriceList.add(new BigDecimal(skuModel.getPriceSale()));
        }

        Map<String, Object> resultField = this.getNewFields(msrpPriceList, retailPriceList, salePriceList);
        CmsBtProductModel_Field field = (CmsBtProductModel_Field) resultField.get("field");

        productPriceModel.setProductCode(code);
        productPriceModel.setPriceChange(1);
        requestModel.addProductPrices(productPriceModel);

        voApiClient.execute(requestModel);
    }

    /**
     * 更新field价格
     */
    private Map<String, Object> getNewFields(List<BigDecimal> msrpPriceList,
                                             List<BigDecimal> retailPriceList,
                                             List<BigDecimal> salePriceList) {
        Map<String, Object> result = new HashMap<>();
//        Map<String, Object> msrpMap = this.getNewPriceScope(msrpPriceList, new BigDecimal(field.getMsrpStart()), new BigDecimal(field.getMsrpEnd()));
//        Map<String, Object> retailMap = this.getNewPriceScope(retailPriceList, new BigDecimal(field.getRetailPriceStart()), new BigDecimal(field.getRetailPriceEnd()));
//        Map<String, Object> saleMap = this.getNewPriceScope(salePriceList, new BigDecimal(field.getSalePriceStart()), new BigDecimal(field.getSalePriceEnd()));
//        if ((boolean) msrpMap.get("isChanged") || (boolean) retailMap.get("isChanged") || (boolean) saleMap.get("isChanged")) {
//            field.put("msrpStart", ((BigDecimal) msrpMap.get("start")).doubleValue());
//            field.put("msrpEnd", ((BigDecimal) msrpMap.get("end")).doubleValue());
//            field.put("retailPriceStart", ((BigDecimal) retailMap.get("start")).doubleValue());
//            field.put("retailPriceEnd", ((BigDecimal) retailMap.get("end")).doubleValue());
//            field.put("salePriceStart", ((BigDecimal) saleMap.get("start")).doubleValue());
//            field.put("salePriceEnd", ((BigDecimal) saleMap.get("end")).doubleValue());
//            result.put("isChanged", true);
//        } else {
//            result.put("isChanged", false);
//        }
//        result.put("field", field);
        return result;
    }

    /**
     * 获取价格范围
     */
    private Map<String, Object> getNewPriceScope(List<BigDecimal> priceList) {
        Map<String, Object> result = new HashMap<>();
//        BigDecimal start = priceStart;
//        BigDecimal end = priceEnd;
//        Boolean isChanged = false;
//        if (priceList != null && priceList.size() > 0) {
//            priceList.sort((o1, o2) -> o2.compareTo(o1));
//            start = priceList.get(priceList.size() - 1);
//            end = priceList.get(0);
//            if (start.compareTo(priceStart) != 0 || end.compareTo(priceEnd) != 0) {
//                isChanged = true;
//            }
//        }
//        result.put("start", start);
//        result.put("end", end);
//        result.put("isChanged", isChanged);
        return result;
    }

    /**
     * 判断价格是否变更
     */
    private boolean isPriceChanged(CmsBtProductModel_Sku model1, CmsBtProductModel_Sku model2) {
        BigDecimal msrp1 = new BigDecimal(model1.getPriceMsrp());
        BigDecimal retail1 = new BigDecimal(model1.getPriceRetail());
        BigDecimal sale1 = new BigDecimal(model1.getPriceSale());

        BigDecimal msrp2 = new BigDecimal(model2.getPriceMsrp());
        BigDecimal retail2 = new BigDecimal(model2.getPriceRetail());
        BigDecimal sale2 = new BigDecimal(model2.getPriceSale());

        return !(msrp1.compareTo(msrp2) == 0 && retail1.compareTo(retail2) == 0 && sale1.compareTo(sale2) == 0);
    }
}
