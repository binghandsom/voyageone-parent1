package com.voyageone.common.components.jumei;

import com.voyageone.common.components.jumei.Bean.JmProductBean;
import com.voyageone.common.components.jumei.base.JmBase;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chuanyu.liang on 2016/1/25.
 */
@Service
public class JumeiProductService extends JmBase {

    private static String PRODUCT_NEW = "/v1/htProduct/addProductAndDeal";

    /**
     * 创建商品并同时创建Deal
     */
    public void productNewUpload(ShopBean shopBean, JmProductBean product) throws Exception {
        if (product == null) {
            throw new Exception("fileBean not found!");
        }

        product.check();

        Map<String, String> params = new HashMap<>();
        params.put("product", product.toJsonStr());
        params.put("spus", product.getSpusString());
        params.put("dealInfo", product.getDealInfoString());

        String reqResult = reqJmApi(shopBean, PRODUCT_NEW, params);
        Map<String, Object> resultMap = JsonUtil.jsonToMap(reqResult);
        product.setProduct_spec_number((String) getValue(resultMap, "product", "product_spec_number"));
        product.setJumei_product_id((String) getValue(resultMap, "product", "jumei_product_id"));

        product.getDealInfo().setPartner_deal_id((String) getValue(resultMap, "dealInfo", "partner_deal_id"));
        product.getDealInfo().setPartner_deal_id((String) getValue(resultMap, "dealInfo", "jumei_hash_id"));
    }

    private Object getValue(Map<String, Object> dataMap, String parentKey, String key) {
        Object result = null;
        if (dataMap != null && dataMap.containsKey(parentKey)) {
            Object parentObj = dataMap.get(parentKey);
            if (parentObj != null && parentObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> parentMap = (Map<String, Object>)parentObj;
                if (parentMap.containsKey(key)) {
                    result = parentMap.get(key);
                }
            }
        }
        return result;
    }

    private Object getValue(Map<String, Object> dataMap, String key) {
        Object result = null;
        if (dataMap != null && dataMap.containsKey(key)) {
            result = dataMap.get(key);
        }
        return result;
    }


    private static String PRODUCT_GET = "/v1/htProduct/getProductById";
    /**
     * 取得商品
     */
    public JmProductBean getProduct(ShopBean shopBean, String product_id) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("product_id", product_id);
        params.put("fields", "product_id,name,foreign_language_name,categorys,brand_id,brand_name,functions,normalImage,verticalImage,diaoxingImage");

        String reqResult = reqJmApi(shopBean, PRODUCT_NEW, params);
        Map<String, Object> resultMap = JsonUtil.jsonToMap(reqResult);

        JmProductBean resultBean = new JmProductBean();
        resultBean.setJumei_product_id((String) getValue(resultMap, "product_id"));
        resultBean.setName((String) getValue(resultMap, "name"));
        resultBean.setForeign_language_name((String) getValue(resultMap, "foreign_language_name"));
        resultBean.setBrand_id((int) getValue(resultMap, "brand_id"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> categorys = (List<Map<String, Object>>)getValue(resultMap, "categorys");
        if (categorys != null && categorys.size()>0) {
            Map<String, Object> categoryMap = categorys.get(categorys.size()-1);
            resultBean.setCategory_v3_4_id((int)categoryMap.get("category_id"));
        }

        //resultBean.setFunction_ids(function_ids);
        resultBean.setNormalImage((String) getValue(resultMap, "normalImage"));
        resultBean.setVerticalImage((String) getValue(resultMap, "verticalImage"));
        resultBean.setDiaoxingImage((String) getValue(resultMap, "diaoxingImage"));
        return resultBean;
    }
}
