package com.voyageone.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.wms.formbean.ItemLocationFormBean;
import com.voyageone.wms.formbean.ItemLocationLogFormBean;
import com.voyageone.wms.formbean.LocationFormBean;
import com.voyageone.wms.modelbean.ItemLocationBean;
import com.voyageone.wms.modelbean.ItemLocationLogBean;
import com.voyageone.wms.modelbean.LocationBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tester on 5/14/2015.
 */
@Repository
public class LocationDao extends BaseDao {
    @Override
    protected String namespace() {
        return "com.voyageone.wms.sql";
    }

    public List<LocationFormBean> searchByName(String name, int store_id, int offset, int size, UserSessionBean user) {
        Map<String, Object> params = new HashMap<>();

        params.put("location_name", StringUtils.isEmpty(name) ? name : "%" + name + "%");
        params.put("store_id", store_id);
        params.put("offset", offset);
        params.put("size", size);
        params.put("storeList", user.getCompanyRealStoreList());
        params.put("orderChannelList", user.getChannelList());

        return selectList("wms_mt_location_search_by_name", params);
    }

    public int searchCountByName(String name, int store_id, UserSessionBean user) {
        Map<String, Object> params = new HashMap<>();

        params.put("location_name", StringUtils.isEmpty(name) ? name : "%" + name + "%");
        params.put("store_id", store_id);
        params.put("storeList", user.getCompanyRealStoreList());
        params.put("orderChannelList", user.getChannelList());

        return selectOne("wms_mt_location_search_by_name_count", params);
    }

    public int deleteLocation(int location_id, int store_id, String modified) {
        Map<String, Object> params = new HashMap<>();

        params.put("location_id", location_id);
        params.put("store_id", store_id);
        params.put("modified", modified);

        return updateTemplate.delete("wms_mt_location_delete", params);
    }


    public boolean isNameExists(String location_name, int store_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("location_name", location_name);
        params.put("store_id", store_id);

        int count = selectOne("wms_mt_location_isNameExists", params);

        return count > 0;
    }

    public int insertLocation(LocationBean location) {
        return updateTemplate.insert("wms_mt_location_insert", location);
    }

    public int itemCountInLocation(int location_id, int store_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("location_id", location_id);
        params.put("store_id", store_id);

        return selectOne("wms_bt_item_location_item_count_in_location", params);
    }

    /**
     * 根据 code 查询商品的所有所在位置
     *
     * @param code     商品的 Code
     * @param store_id 目标仓库
     * @return List
     */
    public List<ItemLocationFormBean> selectItemLocations(String code, int store_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("code", code);
        params.put("store_id", store_id);

        return selectList("wms_bt_item_location_select_item_locations", params);
    }

    /**
     * 根据 code 查询商品的位置变更日志
     *
     * @param code     商品的 Code
     * @param store_id 目标仓库
     * @return List
     */
    public List<ItemLocationLogFormBean> selectItemLocationLogs(String code, int store_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("code", code);
        params.put("store_id", store_id);

        return selectList("wms_bt_item_location_logs_select_item_location_logs", params);
    }

    /**
     * 插入一行 ItemLocationBean
     *
     * @param itemLocation ItemLocationBean
     * @return int
     */
    public int insertItemLocation(ItemLocationBean itemLocation) {
        return updateTemplate.insert("wms_bt_item_location_insert", itemLocation);
    }

    /**
     * 根据 store_id、code、location_id 定位获取一行 ItemLocationBean
     *
     * @param itemLocation ItemLocationBean
     * @return ItemLocationBean
     */
    public ItemLocationFormBean getItemLocationByLocation(ItemLocationBean itemLocation) {
        return selectOne("wms_bt_item_location_get_by_location", itemLocation);
    }

    /**
     * 删除 ItemLocation
     *
     * @param item_location_id id 主键
     * @param modified         最后的修改时间
     * @return int
     */
    public int deleteItemLocation(int item_location_id, String modified) {
        Map<String, Object> params = new HashMap<>();

        params.put("item_location_id", item_location_id);
        params.put("modified", modified);

        return updateTemplate.delete("wms_bt_item_location_delete", params);
    }

    /**
     * 删除 ItemLocationExt
     *
     * @param itemLocationBean
     * @return int
     */
    public int deleteItemLocationExt(ItemLocationBean itemLocationBean) {

        return updateTemplate.delete("wms_bt_item_location_ext_delete", itemLocationBean);
    }

    /**
     * 根据主键检索 ItemLocation
     *
     * @param item_location_id 主键
     * @return ItemLocationBean
     */
    public ItemLocationBean getItemLocation(int item_location_id) {
        return selectOne("wms_bt_item_location_get", item_location_id);
    }

    /**
     * 插入 ItemLocationLogBean
     *
     * @param itemLocationLog ItemLocationLogBean
     * @return int
     */
    public int insertItemLocationLog(ItemLocationLogBean itemLocationLog) {
        return updateTemplate.insert("wms_bt_item_location_log_insert", itemLocationLog);
    }

    /**
     * 通过 location_name 货架名称，匹配货架，获取货架信息
     * @param location_name 货架名称
     * @param store_id 仓库
     * @return LocationBean
     */
    public LocationFormBean getLocationByName(String location_name, int store_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("location_name", location_name);
        params.put("store_id", store_id);

        return selectOne("wms_mt_location_getLocationByName", params);
    }

    public LocationFormBean getLocation(int location_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("location_id", location_id);

        return selectOne("wms_mt_location_getLocation", params);
    }

    /**
     * 获取刚插入的 ItemLocationLogBean，返回 ItemLocationLogFormBean
     * @param itemLocationLog ItemLocationLogBean
     * @return ItemLocationLogFormBean
     */
    public ItemLocationLogFormBean getInsertedLog(ItemLocationLogBean itemLocationLog) {
        return selectOne("wms_bt_item_location_log_getInsertedLog", itemLocationLog);
    }
}
