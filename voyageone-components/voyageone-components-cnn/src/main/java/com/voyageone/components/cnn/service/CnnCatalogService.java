package com.voyageone.components.cnn.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.cnn.CnnBase;
import com.voyageone.components.cnn.enums.CnnConstants;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新独立域名Liking店铺内分类API调用服务
 * <p/>
 * Created by desmond on 2017/01/11.
 */
@Component
public class CnnCatalogService extends CnnBase {

    /**
     * 添加店铺内分类
     *
     * @param shop 店铺信息
     * @param name 店铺内分类名称(必须)
     * @param id   店铺内分类ID(必须)
     * @param parentCatalogId 该分类的父节点ID(为空时表示是顶级节点)(任意)
     * @return String 返回结果JSON串
     */
    public Map<String, Object>  addCatalog(ShopBean shop, String name, String id, String parentCatalogId) throws Exception {
        String result;

        Map<String, Object> request = new HashMap<>();
        request.put("name", name);
        request.put("id", id);
        if (!StringUtils.isEmpty(parentCatalogId)) request.put("parentCatalogId", parentCatalogId);

        // 调用新独立域名添加店铺内分类API
        result = reqApi(shop, CnnConstants.CnnApiAction.CATALOG_ADD, request);
        if(!StringUtil.isEmpty(result)){
            Map<String, Object> ret = JacksonUtil.jsonToMap(result);
            if(CnnConstants.C_CNN_RETURN_SUCCESS_0 != (Integer)ret.get("code")){
                throw new BusinessException("创建类目失败， 请再尝试一下。 "+ result);
            }
            return ret;
        }else{
            throw new BusinessException("创建类目失败， 请再尝试一下。 ");
        }
    }

    /**
     * 修改店铺内分类名称
     *
     * @param shop 店铺信息
     * @param id   店铺内分类ID(必须)
     * @param name 店铺内分类名称(必须)
     * @return String 返回结果JSON串
     */
    public Map<String, Object> updateCatalog(ShopBean shop, String id, String name) throws Exception {
        String result;

        Map<String, Object> request = new HashMap<>();
        request.put("id", id);
        request.put("name", name);

        // 调用新独立域名修改店铺内分类名称API
        result = reqApi(shop, CnnConstants.CnnApiAction.CATALOG_UPDATE, request);
        if(!StringUtil.isEmpty(result)){
            Map<String, Object> ret = JacksonUtil.jsonToMap(result);
            if(CnnConstants.C_CNN_RETURN_SUCCESS_0 != (Integer)ret.get("code")){
                throw new BusinessException("创建类目失败， 请再尝试一下。 "+ result);
            }
            return ret;
        }else{
            throw new BusinessException("创建类目失败， 请再尝试一下。 ");
        }
    }

    /**
     * 删除店铺内分类
     *
     * @param shop 店铺信息
     * @param id   店铺内分类ID(必须)
     * @return String 返回结果JSON串
     */
    public Map<String, Object> deleteCatalog(ShopBean shop, String id) throws Exception {
        String result;

        Map<String, Object> request = new HashMap<>();
        request.put("id", id);

        // 调用新独立域名修改店铺内分类名称API
        result = reqApi(shop, CnnConstants.CnnApiAction.CATALOG_DELETE, request);
        if(!StringUtil.isEmpty(result)){
            Map<String, Object> ret = JacksonUtil.jsonToMap(result);
            if(CnnConstants.C_CNN_RETURN_SUCCESS_0 != (Integer)ret.get("code")){
                throw new BusinessException("创建类目失败， 请再尝试一下。 "+ result);
            }
            return ret;
        }else{
            throw new BusinessException("创建类目失败， 请再尝试一下。 ");
        }
    }

    /**
     * 设置商品的店铺内分类
     *
     * @param shop 店铺信息
     * @param numIId 商品id
     * @param idList 店铺内分类ID(必须) 店铺内分类ID列表，最多10个，目前必须是叶子节点
     * @return String 返回结果JSON串
     */
    public String setProductCatalog(ShopBean shop, String numIId, List<String> idList) throws Exception {
        String result;

        Map<String, Object> request = new HashMap<>();
        request.put("numIId", numIId);
        request.put("idList", idList);

        // 调用新独立域名修改店铺内分类名称API
        result = reqApi(shop, CnnConstants.CnnApiAction.CATALOG_PRODUCT_SET, request);

        return result;
    }

    /**
     * 设置商品的店铺内分类
     * 查询店铺内分类信息
     * catalogId=='0'时表示查询第一级分类
     * 只返回有效的分类节点
     * 只返回指定的分类信息及其下级分类信息，（不包括再下一级的分类，若需要请重新指定父节点查询）
     *
     * @param shop 店铺信息
     * @param catalogId 节点ID(必须)
     * @return String 返回结果JSON串
     */
    public String getCatalog(ShopBean shop, String catalogId) throws Exception {
        String result;

        Map<String, Object> request = new HashMap<>();
        // catalogId=='0'时表示查询第一级分类
        request.put("catalogId", catalogId);

        // 调用新独立域名修改店铺内分类名称API
        result = reqApi(shop, CnnConstants.CnnApiAction.CATALOG_GET, request);

        // 只返回有效的分类节点
        // 只返回指定的分类信息及其下级分类信息，（不包括再下一级的分类，若需要请重新指定父节点查询）
        return result;
    }

    /**
     * 重置所有店铺内分类
     *
     * @param shop 店铺信息
     * @param numIId 商品id
     * @param idList 店铺内分类ID(必须) 店铺内分类ID列表，最多10个，目前必须是叶子节点
     * @return String 返回结果JSON串
     */
    public String resetAllCatalog(ShopBean shop, String numIId, List<String> idList) throws Exception {
        String result;

        Map<String, Object> request = new HashMap<>();
//        request.put("numIId", numIId);// 这里要改 TODO
//        request.put("idList", idList);

        // 调用新独立域名修改店铺内分类名称API
        result = reqApi(shop, CnnConstants.CnnApiAction.CATALOG_ADD, request);  // 这里要改 TODO

        return result;
    }

    /**
     * 重置所有商品的店铺内分类
     *
     * @param shop 店铺信息
     * @param numIId 商品id
     * @param idList 店铺内分类ID(必须) 店铺内分类ID列表，最多10个，目前必须是叶子节点
     * @return String 返回结果JSON串
     */
    public String resetAllProductCatalog(ShopBean shop, String numIId, List<String> idList) throws Exception {
        String result;

        Map<String, Object> request = new HashMap<>();
//        request.put("numIId", numIId); // 这里要改 TODO
//        request.put("idList", idList);

        // 调用新独立域名修改店铺内分类名称API
        result = reqApi(shop, CnnConstants.CnnApiAction.CATALOG_PRODUCT_RESET, request);

        return result;
    }

}
