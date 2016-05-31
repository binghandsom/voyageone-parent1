package com.voyageone.components.jumei.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jumei.bean.JmGetProductInfoRes;
import com.voyageone.components.jumei.bean.JmProductBean;
import com.voyageone.components.jumei.bean.JmProductBean_Spus;
import com.voyageone.components.jumei.JmBase;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JumeiProductService
 *
 * @author chuanyu.laing on 2016/1/25.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class JumeiProductService extends JmBase {

    private static final String PRODUCT_NEW = "v1/htProduct/addProductAndDeal";

    /**
     * 创建商品并同时创建Deal
     */
    @SuppressWarnings("unchecked")
    public void productNewUpload(ShopBean shopBean, JmProductBean product) throws Exception {
        if (product == null) {
            throw new Exception("fileBean not found!");
        }

        product.check();

        Map<String, Object> params = new HashMap<>();
        params.put("product", product.toJsonStr());
        params.put("spus", product.getSpusString());
        params.put("dealInfo", product.getDealInfoString());
        logger.info("上新参数 product:" + JacksonUtil.bean2Json(product));
        logger.info("上新参数 spus:" + product.getSpusString());
        logger.info("上新参数 dealInfo:"+product.getDealInfoString());
        String reqResult = reqJmApi(shopBean, PRODUCT_NEW, params);
//        String reqResult = "{\"product\":{\"product_spec_number\":\"h523k6610\",\"jumei_product_id\":\"1427\"},\"spus\":[{\"partner_spu_no\":\"H523K6610-10\",\"jumei_spu_no\":\"12102\",\"skuinfo\":{\"partner_sku_no\":\"H523K6610-10\",\"jumei_sku_no\":\"1779\"}},{\"partner_spu_no\":\"H523K6610-10.5\",\"jumei_spu_no\":\"12103\",\"skuinfo\":{\"partner_sku_no\":\"H523K6610-10.5\",\"jumei_sku_no\":\"1780\"}},{\"partner_spu_no\":\"H523K6610-11\",\"jumei_spu_no\":\"12104\",\"skuinfo\":{\"partner_sku_no\":\"H523K6610-11\",\"jumei_sku_no\":\"1781\"}},{\"partner_spu_no\":\"H523K6610-11.5\",\"jumei_spu_no\":\"12105\",\"skuinfo\":{\"partner_sku_no\":\"H523K6610-11.5\",\"jumei_sku_no\":\"1782\"}},{\"partner_spu_no\":\"H523K6610-12\",\"jumei_spu_no\":\"12106\",\"skuinfo\":{\"partner_sku_no\":\"H523K6610-12\",\"jumei_sku_no\":\"1783\"}},{\"partner_spu_no\":\"H523K6610-13\",\"jumei_spu_no\":\"12107\",\"skuinfo\":{\"partner_sku_no\":\"H523K6610-13\",\"jumei_sku_no\":\"1784\"}},{\"partner_spu_no\":\"H523K6610-5\",\"jumei_spu_no\":\"12108\",\"skuinfo\":{\"partner_sku_no\":\"H523K6610-5\",\"jumei_sku_no\":\"1785\"}},{\"partner_spu_no\":\"H523K6610-5.5\",\"jumei_spu_no\":\"12109\",\"skuinfo\":{\"partner_sku_no\":\"H523K6610-5.5\",\"jumei_sku_no\":\"1786\"}},{\"partner_spu_no\":\"H523K6610-6\",\"jumei_spu_no\":\"12110\",\"skuinfo\":{\"partner_sku_no\":\"H523K6610-6\",\"jumei_sku_no\":\"1787\"}},{\"partner_spu_no\":\"H523K6610-6.5\",\"jumei_spu_no\":\"12111\",\"skuinfo\":{\"partner_sku_no\":\"H523K6610-6.5\",\"jumei_sku_no\":\"1788\"}},{\"partner_spu_no\":\"H523K6610-7\",\"jumei_spu_no\":\"12112\",\"skuinfo\":{\"partner_sku_no\":\"H523K6610-7\",\"jumei_sku_no\":\"1789\"}},{\"partner_spu_no\":\"H523K6610-7.5\",\"jumei_spu_no\":\"12113\",\"skuinfo\":{\"partner_sku_no\":\"H523K6610-7.5\",\"jumei_sku_no\":\"1790\"}},{\"partner_spu_no\":\"H523K6610-8\",\"jumei_spu_no\":\"12114\",\"skuinfo\":{\"partner_sku_no\":\"H523K6610-8\",\"jumei_sku_no\":\"1791\"}},{\"partner_spu_no\":\"H523K6610-8.5\",\"jumei_spu_no\":\"12115\",\"skuinfo\":{\"partner_sku_no\":\"H523K6610-8.5\",\"jumei_sku_no\":\"1792\"}},{\"partner_spu_no\":\"H523K6610-9\",\"jumei_spu_no\":\"12116\",\"skuinfo\":{\"partner_sku_no\":\"H523K6610-9\",\"jumei_sku_no\":\"1793\"}},{\"partner_spu_no\":\"H523K6610-9.5\",\"jumei_spu_no\":\"12117\",\"skuinfo\":{\"partner_sku_no\":\"H523K6610-9.5\",\"jumei_sku_no\":\"1794\"}}],\"dealInfo\":[{\"partner_deal_id\":\"h523k6610-JM0301\",\"jumei_hash_id\":\"100583\"}]}";
        logger.info("上新的返回结果：" + reqResult);
        Map<String, Object> resultMap = JacksonUtil.jsonToMap(reqResult);

        /**
         * set jumei_product_id from result
         */
        //product.setProduct_spec_number((String) getValue(resultMap, "product", "product_spec_number"));
        product.setJumei_product_id((String) getValue(resultMap, "product", "jumei_product_id"));

        if(StringUtils.isEmpty(product.getJumei_product_id())){
            logger.info("返回错误" + reqResult);
            throw new BusinessException(reqResult);
        }
        /**
         * set jumei_spu_no jumei_sku_no from result
         */
        List<Map<String, Object>> spusMapList = new ArrayList<>();
        if (resultMap.get("spus") != null && resultMap.get("spus") instanceof List) {
            spusMapList = (List<Map<String, Object>>)resultMap.get("spus");
        }
        List<JmProductBean_Spus> spusList = new ArrayList<>();
        if (product.getSpus() != null) {
            spusList = product.getSpus();
        }
        for (JmProductBean_Spus spu : spusList) {
            for (Map<String, Object> spusMap : spusMapList) {
                String partner_spu_no = (String) spusMap.get("partner_spu_no");
                if (partner_spu_no != null && partner_spu_no.equals(spu.getPartner_spu_no())) {
                    String jumei_spu_no = (String) spusMap.get("jumei_spu_no");
                    spu.setJumei_spu_no(jumei_spu_no);
                    if (spu.getSkuInfo() != null && spusMap.get("skuinfo") != null) {
                        Map<String, Object> skuinfoMap = (Map<String, Object>)spusMap.get("skuinfo");
                        String jumei_sku_no = (String)skuinfoMap.get("jumei_sku_no");
                        spu.getSkuInfo().setJumei_sku_no(jumei_sku_no);
                    }

                    break;
                }
            }
        }

        /**
         * set jumei_hash_id from result
         */
        List<Map<String, Object>> dealInfos = (List<Map<String, Object>>) resultMap.get("dealInfo");
        if(dealInfos != null){
            for(Map<String, Object> dealInfo : dealInfos){
                if(product.getDealInfo().getPartner_deal_id().equalsIgnoreCase((String) dealInfo.get("partner_deal_id"))){
                    product.getDealInfo().setJumei_hash_id((String) dealInfo.get("jumei_hash_id"));
                    String jumei_hash_id = product.getDealInfo().getJumei_hash_id();
                    if(StringUtils.isEmpty(jumei_hash_id) || "p0".equalsIgnoreCase(jumei_hash_id.substring(jumei_hash_id.length() - 2))){
                        throw new BusinessException("jumei_hash_id不正确："+jumei_hash_id);
                    }
                    break;
                }
            }
        }
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


    private static String PRODUCT_GET = "v1/htProduct/getProductByIdOrName";
    /**
     * 取得商品
     */
    public JmGetProductInfoRes getProductById(ShopBean shopBean, String product_id) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("product_id", product_id);
        params.put("fields", "product_id,name,foreign_language_name,categorys,brand_id,brand_name,functions,normalImage,verticalImage,diaoxingImage,hash_ids,category_v3_4,spus");

        String reqResult = reqJmApi(shopBean, PRODUCT_GET, params);
        return JsonUtil.jsonToBean(reqResult, JmGetProductInfoRes.class);

//        resultBean.setJumei_product_id((String) getValue(resultMap, "product_id"));
//        resultBean.setName((String) getValue(resultMap, "name"));
//        resultBean.setForeign_language_name((String) getValue(resultMap, "foreign_language_name"));
//        resultBean.setBrand_id((int) getValue(resultMap, "brand_id"));
//
//        @SuppressWarnings("unchecked")
//        List<Map<String, Object>> categorys = (List<Map<String, Object>>)getValue(resultMap, "categorys");
//        if (categorys != null && categorys.size()>0) {
//            Map<String, Object> categoryMap = categorys.get(categorys.size()-1);
//            resultBean.setCategory_v3_4_id((int)categoryMap.get("category_id"));
//        }
//
//        //resultBean.setFunction_ids(function_ids);
//        resultBean.setNormalImage((String) getValue(resultMap, "normalImage"));
//        resultBean.setVerticalImage((String) getValue(resultMap, "verticalImage"));
//        resultBean.setDiaoxingImage((String) getValue(resultMap, "diaoxingImage"));
//        return resultBean;
    }
    public JmGetProductInfoRes getProductByName(ShopBean shopBean, String productName) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("product_name", productName);
        params.put("fields", "product_id,name,foreign_language_name,categorys,brand_id,brand_name,functions,normalImage,verticalImage,diaoxingImage,hash_ids,category_v3_4,spus");

        String reqResult = reqJmApi(shopBean, PRODUCT_GET, params);
        return JsonUtil.jsonToBean(reqResult, JmGetProductInfoRes.class);

//        resultBean.setJumei_product_id((String) getValue(resultMap, "product_id"));
//        resultBean.setName((String) getValue(resultMap, "name"));
//        resultBean.setForeign_language_name((String) getValue(resultMap, "foreign_language_name"));
//        resultBean.setBrand_id( Integer.parseInt(getValue(resultMap, "brand_id").toString()));
//        resultBean.setHash_ids((String) getValue(resultMap, "hash_ids"));
//
//        @SuppressWarnings("unchecked")
//        List<Map<String, Object>> categorys = (List<Map<String, Object>>)getValue(resultMap, "categorys");
//        if (categorys != null && categorys.size()>0) {
//            Map<String, Object> categoryMap = categorys.get(categorys.size()-1);
//            resultBean.setCategory_v3_4_id((int)categoryMap.get("category_id"));
//        }
//
//        //resultBean.setFunction_ids(function_ids);
//        resultBean.setNormalImage((String) getValue(resultMap, "normalImage"));
//        resultBean.setVerticalImage((String) getValue(resultMap, "verticalImage"));
//        resultBean.setDiaoxingImage((String) getValue(resultMap, "diaoxingImage"));
//        return resultBean;
    }
}
