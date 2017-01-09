package com.voyageone.components.cnn.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ListUtils;
import com.voyageone.components.cnn.CnnBase;
import com.voyageone.components.cnn.enums.CnnConstants;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新独立域名商品类API调用服务
 * <p/>
 * Created by desmond on 2017/01/04.
 */
@Component
public class CnnWareService extends CnnBase {

    /**
     * 新独立域名新增商品
     *
     * @param shop 店铺信息
     * @param commonFields 商品共通级属性(必须)
     * @param customFields 产品级扩展属性(任意)
     * @param skuList      该商品的sku列表(必须) 列表中的第一个sku作为该商品的缺省sku，即默认显示该sku
     * @param optionsList  sku区分选项名称及值的一览(任意) 只有单个sku的情形时，不需要此项
     * @return String 返回结果JSON串
     */
    public String addProduct(ShopBean shop, Map<String, Object> commonFields, Map<String, Object> customFields,
                             List<Map<String, Object>> skuList, List<Map<String, Object>> optionsList) throws Exception {
        String result;

        Map<String, Object> request = new HashMap<>();
        // 商品共通级属性(必须)
        request.put("commonFields", commonFields);
        // 产品级扩展属性(任意)
        if (MapUtils.isNotEmpty(customFields)) request.put("customFields", customFields);
        // 该商品的sku列表(必须) 列表中的第一个sku作为该商品的缺省sku，即默认显示该sku
        request.put("skuList", skuList);
        // sku区分选项名称及值的一览(任意) 只有单个sku的情形时，不需要此项
        if (ListUtils.notNull(optionsList)) request.put("optionsList", optionsList);

        // 调用新独立域名新增商品API
        result = reqApi(shop, CnnConstants.CnnApiAction.PRODUCT_ADD, request);

        return result;
    }

    /**
     * 新独立域名更新商品
     *
     * @param shop 店铺信息
     * @param numIId 商品ID
     * @param commonFields 商品共通级属性(任意)
     * @param customFields 产品级扩展属性(任意)
     * @param skuList      该商品的sku列表(任意)
     * @param optionsList  sku区分选项名称及值的一览(任意) 该项目实质上是"skuList"中的skuOptions的归纳合并
     * @return String 返回结果JSON串
     */
    public String updateProduct(ShopBean shop, Long numIId, Map<String, Object> commonFields, Map<String, Object> customFields,
                             List<Map<String, Object>> skuList, List<Map<String, Object>> optionsList) throws Exception {
        String result;

        Map<String, Object> request = new HashMap<>();
        // 商品ID
        request.put("numIId", numIId);
        // 商品共通级属性(任意)
        if (MapUtils.isNotEmpty(commonFields)) request.put("commonFields", commonFields);
        // 产品级扩展属性(任意)
        if (MapUtils.isNotEmpty(customFields)) request.put("customFields", customFields);
        // 该商品的sku列表(任意)
        if (ListUtils.notNull(skuList)) request.put("skuList", skuList);
        // sku区分选项名称及值的一览(任意) 该项目实质上是"skuList"中的skuOptions的归纳合并
        if (ListUtils.notNull(optionsList)) request.put("optionsList", optionsList);

        // 调用新独立域名更新商品API
        result = reqApi(shop, CnnConstants.CnnApiAction.PRODUCT_UPDATE, request);

        return result;
    }

    /**
     * 新独立域名商品上架处理
     *
     * @param shop 店铺信息
     * @param numIId 商品ID
     * @return String 返回结果JSON串
     */
    public String doWareUpdateListing(ShopBean shop, Long numIId) throws Exception {
        String result;

        Map<String, Object> request = new HashMap<>();
        request.put("numIId", numIId);

        // 调用新独立域名上架商品API
        result = reqApi(shop, CnnConstants.CnnApiAction.PRODUCT_ADD2WARE, request);

        return result;
    }

    /**
     * 新独立域名商品下架处理
     *
     * @param shop 店铺信息
     * @param numIId 商品ID
     * @return String 返回结果JSON串
     */
    public String doWareUpdateDelisting(ShopBean shop, Long numIId) throws Exception {
        String result;

        Map<String, Object> request = new HashMap<>();
        request.put("numIId", numIId);

        // 调用新独立域名上架商品API
        result = reqApi(shop, CnnConstants.CnnApiAction.PRODUCT_DELFROMWARE, request);

        return result;
    }
}
