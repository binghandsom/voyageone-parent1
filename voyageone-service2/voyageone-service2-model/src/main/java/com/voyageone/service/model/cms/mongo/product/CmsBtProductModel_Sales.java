package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CmsBtProductModel_Sales
 *
 * @author chuanyu.liang, 2016/06/03
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_Sales extends BaseMongoMap<String, Object> {

    public final static String CODE_SUM_7 = "code_sum_7";
    public final static String CODE_SUM_30 = "code_sum_30";
    public final static String CODE_SUM_ALL = "code_sum_30";
    public final static String CARTID = "cartId_";
    public final static String SKUS = "skus";

    public CmsBtProductModel_Sales() {
    }

    public CmsBtProductModel_Sales(Map<String, Object> map) {
        this.putAll(map);
    }

    //code_sum_7
    public Map getCodeSum7() {
        return getAttribute(CODE_SUM_7);
    }
    public void setCodeSum7(Map codeSum7) {
        setAttribute(CODE_SUM_7, codeSum7);
    }
    public int getCodeSum7(int cart) {
        Map<String, Object> codeSum = getAttribute(CODE_SUM_7);
        if (codeSum != null) {
            return (int) codeSum.get("cartId_" + cart);
        }
        return 0;
    }

    //code_sum_30
    public BaseMongoMap getCodeSum30() {
        return getAttribute(CODE_SUM_30);
    }
    public void setCodeSum30(BaseMongoMap codeSum30) {
        setAttribute(CODE_SUM_30, codeSum30);
    }
    public int getCodeSum30(int cart) {
        Map<String, Object> codeSum = getAttribute(CODE_SUM_30);
        if (codeSum != null) {
            return (int) codeSum.get(CARTID + cart);
        }
        return 0;
    }

    //code_sum_all
    public BaseMongoMap getCodeSumAll() {
        return getAttribute(CODE_SUM_ALL);
    }
    public void setCodeSumAll(BaseMongoMap codeSumAll) {
        setAttribute(CODE_SUM_ALL, codeSumAll);
    }
    public int getCodeSumAll(int cart) {
        Map<String, Object> codeSum = getAttribute(CODE_SUM_ALL);
        if (codeSum != null) {
            return (int) codeSum.get(CARTID + cart);
        }
        return 0;
    }

    //skus
    public List<CmsBtProductModel_Sales_Sku> getSkus() {
        return getAttribute("skus");
    }

    public void setSkus(List<CmsBtProductModel_Sales_Sku> skus) {
        setAttribute("skus", skus);
    }
    public CmsBtProductModel_Sales_Sku getSkuSum(int cart, String skuCode) {
        List<CmsBtProductModel_Sales_Sku> skus = getAttribute(SKUS);
        if (skus != null) {
            for (CmsBtProductModel_Sales_Sku sku : skus) {
                if (sku.getCartId() == cart && skuCode.equals(sku.getSkuCode())) {
                    return sku;
                }
            }
        }
        return null;
    }

    public int getSkuSum(int cart, String skuCode, Integer days) {
        List<CmsBtProductModel_Sales_Sku> skus = getAttribute(SKUS);
        if (skus != null) {
            for (CmsBtProductModel_Sales_Sku sku : skus) {
                if (sku.getCartId() == cart && skuCode.equals(sku.getSkuCode())) {
                    if (days == null) {
                        return sku.getSkuSumAll();
                    } else if (days == 7) {
                        return sku.getSkuSum7();
                    } else if (days == 30) {
                        return sku.getSkuSum30();
                    }
                    return 0;
                }
            }
        }
        return 0;
    }


    @Override
    public Object put(String key, Object value) {
        if (SKUS.equals(key)) {
            if (value != null) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> imageMaps = (List<Map<String, Object>>) value;
                List<CmsBtProductModel_Sales_Sku> skus = new ArrayList<>();
                for (Map<String, Object> map : imageMaps) {
                    if (map != null) {
                        CmsBtProductModel_Sales_Sku image;
                        if (map instanceof CmsBtProductModel_Sales_Sku) {
                            image = (CmsBtProductModel_Sales_Sku) map;
                        } else {
                            image = new CmsBtProductModel_Sales_Sku(map);
                        }
                        skus.add(image);
                    }
                }
                value = skus;
            }
        }
        return super.put(key, value);
    }
}
