package com.voyageone.service.dao.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtFeedCustomPropModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * for voyageone_ims.ims_mt_custom_word and voyageone_ims.ims_mt_custom_word_param
 *
 * Created by Jonas on 9/11/15.
 */
@Repository
public class CmsBtFeedCustomPropDao extends ServiceBaseDao {

    // 根据类目路径查询自定义属性信息(包含共通属性)
    public List<CmsBtFeedCustomPropModel> selectWithCategory(Map<String, Object> params) {
        return selectList("cms_bt_feed_custom_prop_selectWithCatPath", params);
    }

    // 根据类目路径查询属性信息
    public List<Map<String, Object>> selectAllAttr(Map<String, Object> params) {
        return selectList("cms_bt_feed_custom_prop_selectAllAttr", params);
    }

    // 根据类目路径查询自定义已翻译属性信息(不包含共通属性)
    public List<Map<String, Object>> selectTransProp(Map<String, Object> params) {
        return selectList("cms_bt_feed_custom_prop_selectWithCat2", params);
    }

    // 根据类目路径查询自定义未翻译属性信息(不包含共通属性)
    public List<Map<String, Object>> selectOrigProp(Map<String, Object> params) {
        return selectList("cms_bt_feed_custom_prop_selectWithCat1", params);
    }

    // 查询指定类目属性是否存在
    public boolean isAttrExist(Map<String, Object> params) {
        List<Map<String, Object>> rslt = selectList("cms_bt_feed_custom_prop_isexist", params);
        if (rslt != null && rslt.size() > 0) {
            return true;
        }
        return false;
    }

    // 查询是否全店铺共通属性
    public String selectSameAttr(Map<String, Object> params) {
        List<Map<String, Object>> rslt = selectList("cms_bt_feed_custom_prop_getSameAttr", params);
        if (rslt != null && rslt.size() > 0) {
            return (String) rslt.get(0).get("config_code");
        }
        return "";
    }

    // 根据类目路径查询自定义已翻译属性信息(不包含共通属性)
    public int insertAttr(Map<String, Object> params) {
        return insert("cms_bt_feed_custom_prop_add", params);
    }

    // 根据类目路径查询自定义未翻译属性信息(不包含共通属性)
    public int updateAttr(Map<String, Object> params) {
        return update("cms_bt_feed_custom_prop_update", params);
    }

    // 查询指定属性值是否存在
    public boolean isPropValueExist(Map<String, Object> params) {
        List<Map<String, Object>> rslt = selectList("cms_bt_feed_custom_prop_value_isexist", params);
        if (rslt != null && rslt.size() > 0) {
            return true;
        }
        return false;
    }

    // 查询指定属性值是否存在
    public boolean isPropValueExistById(Map<String, Object> params) {
        List<Map<String, Object>> rslt = selectList("cms_bt_feed_custom_prop_value_isexistbyid", params);
        if (rslt != null && rslt.size() > 0) {
            return true;
        }
        return false;
    }

    // 添加属性值
    public int insertPropValue(Map<String, Object> params) {
        return insert("cms_bt_feed_custom_prop_value_add", params);
    }

    // 添加属性值
    public int updatePropValue(Map<String, Object> params) {
        return update("cms_bt_feed_custom_prop_value_update", params);
    }

    // 查询属性值
    public List<Map<String, Object>> selectPropValue(Map<String, Object> params) {
        return selectList("cms_bt_feed_custom_prop_value_selectById", params);
    }
}
