package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.bean.*;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SuperFeedDao extends BaseDao {

    /**
     * 插入Jewelry产品信息
     */
    public int insertSuperfeedJEInfo(SuperFeedJEBean superfeedjebean) {
        return updateTemplate.insert(Constants.DAO_NAME_SPACE_CMS + "cms_superfeed_insertSuperfeedJEInfo", superfeedjebean);
    }

    /**
     * 插入LOCONDO产品信息
     */
    public int insertSuperfeedLCInfo(SuperFeedLCBean superfeedlcbean) {
        return updateTemplate.insert(Constants.DAO_NAME_SPACE_CMS + "cms_superfeed_insertSuperfeedLCInfo", superfeedlcbean);
    }

    /**
     * 取得Attribute数据 Attribute
     *
     * @return List/ AttributeBean
     */
    public List<AttributeBean> selectAttribute(AttributeBean attributebean, String tablename , String keyword) {

        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_CMS + "cms_superfeed_selectAttribute", parameters("attributebean", attributebean, "tablename", tablename, "keyword", keyword));
    }

    /**
     * 清表
     *
     * @param tableName
     * @return int
     */
    public int deleteTableInfo(String tableName) {

        Map<String, Object> params = new HashMap<>();

        params.put("tableName",tableName);

        return updateTemplate.delete(Constants.DAO_NAME_SPACE_CMS + "cms_deletetableInfo", params);
    }

    /**
     * 取得异常数据 model,product
     *
     * @return List/ String
     */
    public List<String> selectErrData(String sql) {
        Map<String, Object> params = new HashMap<>();
        params.put("sql", sql);

        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_CMS + "cms_superfeed_selectErrData", params);
    }

    /**
     * 清除异常数据 category
     *
     * @param sql
     * @return
     */
    public int deleteData(String sql) {
        Map<String, Object> params = new HashMap<>();
        params.put("sql", sql);

        return updateTemplate.delete(Constants.DAO_NAME_SPACE_CMS + "cms_deletData", params);
    }

    /**
     * 清除异常数据 model product
     *
     * @param tableName
     * @return
     */
    public int deleteErrData(String tableName, String keyword) {
        Map<String, Object> params = new HashMap<>();

        params.put("tableName",tableName);
        params.put("keyword",keyword);

        return updateTemplate.delete(Constants.DAO_NAME_SPACE_CMS + "cms_deletErrData", params);
    }

    /**
     * insert date change UpdateFlag
     *
     * @param tableName code
     * @return
     */
    public int updateInsertData(String tableName,  String code, String codes) {
        Map<String, Object> params = new HashMap<>();

        params.put("tableName",tableName);
        params.put("code",code);
        params.put("codes",codes);

        return updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "cms_updateInsertData", params);
    }

    /**
     * insert model date change UpdateFlag
     *
     * @param tableName tableName model code codes keyword
     * @return
     */
    public int updateInsertModelData(String tableName, String model ,String code, String codes, String keyword) {
        Map<String, Object> params = new HashMap<>();

        params.put("tableName",tableName);
        params.put("model",model);
        params.put("code",code);
        params.put("codes",codes);
        params.put("keyword",keyword.replace(model,"a." + model));

        return updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "cms_updateInsertModelData", params);
    }

    /**
     * update date change UpdateFlag
     *
     * @param tableName code codes
     * @return
     */
    public int updateUpdateData(String tableName, String code, String codes) {
        Map<String, Object> params = new HashMap<>();

        params.put("tableName",tableName);
        params.put("code",code);
        params.put("codes",codes);

        return updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "cms_updateUpdateData", params);
    }

    /**
     * 取得 CategoryValue信息 CategoryValue <> ''
     *
     * @return List/ String
     */
    public List<String> selectSuperfeedCategory(String columnname,String tableName,String keyword) {
        Map<String, Object> params = new HashMap<>();
        params.put("column_name", columnname);
        params.put("tableName",tableName);
        params.put("keyword",keyword);

        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_CMS + "cms_superfeed_selectSuperfeedCategory", params);
    }

    /**
     * 取得  Model 信息
     *
     * @return List/ ModelBean
     */
    public List<ModelBean> selectSuperfeedModel(String keyword, ModelBean modelbean,String tableName) {
        Map<String, Object> params = new HashMap<>();

        params.put("url_key", modelbean.getUrl_key());
        params.put("category_url_key", modelbean.getCategory_url_key());
        params.put("m_product_type", modelbean.getM_product_type());
        params.put("m_brand", modelbean.getM_brand());
        params.put("m_model", modelbean.getM_model());
        params.put("m_name", modelbean.getM_name());
        params.put("m_short_description", modelbean.getM_short_description());
        params.put("m_long_description", modelbean.getM_long_description());
        params.put("m_size_type", modelbean.getM_size_type());
        params.put("m_is_unisex", modelbean.getM_is_unisex());
        params.put("m_weight", modelbean.getM_weight());
        params.put("m_is_taxable", modelbean.getM_is_taxable());
        params.put("m_is_effective", modelbean.getM_is_effective());
        params.put("keyword", keyword);
        params.put("tableName",tableName);

        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_superfeed_selectSuperfeedModel",params);
    }

    /**
     * 取得 Product 信息
     *
     * @return List/ ProductBean
     */
    public List<ProductBean> selectSuperfeedProduct(String keyword, ProductBean  productbean,String tableName) {
        Map<String, Object> params = new HashMap<>();

        params.put("url_key", productbean.getUrl_key());
        params.put("model_url_key", productbean.getModel_url_key());
        params.put("category_url_key", productbean.getCategory_url_key());
        params.put("p_code", productbean.getP_code());
        params.put("p_name", productbean.getP_name());
        params.put("p_color", productbean.getP_color());
        params.put("p_msrp", productbean.getP_msrp());
        params.put("p_made_in_country", productbean.getP_made_in_country());
        params.put("pe_short_description", productbean.getPe_short_description());
        params.put("pe_long_description", productbean.getPe_long_description());
        params.put("ps_price", productbean.getPs_price());
        params.put("cps_cn_price_rmb", productbean.getCps_cn_price_rmb());

        params.put("tableName",tableName);
        params.put("keyword", keyword);

        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_superfeed_selectSuperfeedProduct", params);
    }

    /**
     * 取得 Image 信息
     *
     * @return List/ String
     */
    public List<String> selectSuperfeedImage(String keyword, String  image,String tableName) {
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("image", image);
        params.put("tableName",tableName);

        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_superfeed_selectSuperfeedImage", params);
    }

    /**
     * 取得 Item 信息
     *
     * @return List/ ItemBean
     */
    public List<ItemBean> selectSuperfeedItem(String keyword, ItemBean itembean,String tableName) {
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);

        params.put("code",itembean.getCode());
        params.put("i_sku",itembean.getI_sku());
        params.put("i_itemcode",itembean.getI_itemcode());
        params.put("i_size",itembean.getI_size());
        params.put("i_barcode",itembean.getI_barcode());
        params.put("tableName",tableName);

        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_superfeed_selectSuperfeedItem", params);
    }

    /**
     * 取得attributelist信息
     *
     * @return List/ ItemBean
     */
    public List<String> selectSuperfeedAttributeList(String channelId, String isAttribute,String attributeType) {
        Map<String, Object> params = new HashMap<>();
        params.put("channelId",channelId);
        params.put("isAttribute", isAttribute);
        params.put("attributeType",attributeType);

        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_superfeed_selectAttributeList", params);
    }
//    /** 太慢了不用
//     * 插入Attribute产品信息
//     */
//    public int insertFeedAttribute(String channelId, String attributeName,String tableName) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("channelId",  "'" +channelId + "'");
//        params.put("attributeName", attributeName);
//        params.put("attributeName_value", "'" + attributeName + "'");
//        params.put("tableName", tableName);
//
//        return updateTemplate.insert(Constants.DAO_NAME_SPACE_CMS + "cms_superfeed_insertFeedAttribute", params);
//    }

    /**
     * 插入ZZ_Work_Superfeed_Full产品信息
     */
    public int inertSuperfeedFull(String keyword,String tableName,String tableNameFull) {
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", tableName);
        params.put("tableNameFull", tableNameFull);
        params.put("keyword",   keyword);

        return updateTemplate.insert(Constants.DAO_NAME_SPACE_CMS + "cms_superfeed_inertSuperfeedFull", params);
    }


    /**
     * 取得 Insert 数据
     *
     * @return List/ String
     */
    public List<String> inertSuperfeedInsertData(String category, String  model, String code, String tableName, String tableNameFull) {
        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("model", model);
        params.put("code", code);
        params.put("tableName",tableName);
        params.put("tableNameFull", tableNameFull);

        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_superfeed_selectInsertData", params);
    }

    /**
     * 取得 Update 数据
     *
     * @return List/ String
     */
    public List<String> inertSuperfeedUpdateData(String code, String tableName, String tableNameFull) {
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("tableName",tableName);
        params.put("tableNameFull", tableNameFull);

        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_superfeed_selectUpdateData", params);
    }

    /**
     * 取得Attribute数据 All Attribute
     *
     * @return List/ String
     */
    public List<String> selectAllAttribute(String attributeName,String tableName) {
        Map<String, Object> params = new HashMap<>();
        params.put("attributeName", attributeName);
        params.put("tableName", tableName);

        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_CMS + "cms_superfeed_selectAllAttribute", params);
    }


    /**
     * 取得Attribute数据 FeedAttribute
     *
     * @return List/ String
     */
    public String selectFeedAttribute(String channelId, String attributeName,String attributeValue) {
        Map<String, Object> params = new HashMap<>();
        params.put("channelId", channelId);
        params.put("attributeName", attributeName);
        params.put("attributeValue", attributeValue);

        return updateTemplate.selectOne(Constants.DAO_NAME_SPACE_CMS + "cms_superfeed_selectFeedAttribute", params);
    }
    /**
     * 插入Attribute产品信息
     */
    public int insertFeedAttributeNew(String channelId, String attributeName,String attributeValue) {
        Map<String, Object> params = new HashMap<>();
        params.put("channelId", channelId);
        params.put("attributeName", attributeName);
        params.put("attributeValue", attributeValue);

        return updateTemplate.insert(Constants.DAO_NAME_SPACE_CMS + "cms_superfeed_insertFeedAttributeNew", params);
    }
}