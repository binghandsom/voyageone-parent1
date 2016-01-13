package com.voyageone.batch.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.wms.modelbean.InventoryForCmsBean;
import com.voyageone.batch.wms.modelbean.InventorySynLogBean;
import com.voyageone.batch.wms.modelbean.SumInventoryBean;
import com.voyageone.batch.wms.modelbean.ViwLogicInventoryBean;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InventoryDao extends BaseDao {

    /**
     * 物理库存计算设定
     *
     * @param orderChannelID 订单渠道
     */
    public void setPhysicsInventory(String orderChannelID) {
        updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_setPhysicsInventory", orderChannelID);
    }

    /**
     * 逻辑库存计算设定
     *
     * @param orderChannelID 订单渠道
     */
    public void setLogicInventory(String orderChannelID) {
        updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_setLogicInventory", orderChannelID);
    }

    /**
     * 获取不需要进行的库存同步记录（增量更新时\强行同步时例外）
     *
     * @param order_channel_id 渠道
     * @param cart_id          店铺
     * @return InventorySynLogBean
     */
    public List<InventorySynLogBean> getInventoryExceptSynLog(String task_name, String order_channel_id, String cart_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("task_name", task_name);
        params.put("order_channel_id", order_channel_id);
        params.put("cart_id", cart_id);
        params.put("syn_flg", "0");
        params.put("syn_type", "1");

        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_selectInventoryExceptSynLogs", params);
    }

    /**
     * 获取需要进行的库存同步记录
     *
     * @param order_channel_id 渠道
     * @param cart_id          店铺
     * @return InventorySynLogBean
     */
    public List<InventorySynLogBean> getInventorySynLog(String task_name, String order_channel_id, String cart_id, int row_count) {
        Map<String, Object> params = new HashMap<>();

        params.put("task_name", task_name);
        params.put("order_channel_id", order_channel_id);
        params.put("cart_id", cart_id);
        params.put("syn_flg", "0");
        params.put("limit", row_count);

        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_selectInventorySynLogs", params);
    }

    /**
     * 获取需要进行的库存同步记录(淘宝)
     *
     * @param order_channel_id 渠道
     * @param cart_id          店铺
     * @return InventorySynLogBean
     */
    public List<InventorySynLogBean> getInventorySynLogForTM(String task_name, String order_channel_id, String cart_id, int row_count) {
        Map<String, Object> params = new HashMap<>();

        params.put("task_name", task_name);
        params.put("order_channel_id", order_channel_id);
        params.put("cart_id", cart_id);
        params.put("syn_flg", "0");
        params.put("limit", row_count);

        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_selectInventorySynLogsForTM", params);
    }

    /**
     * 删除库存同步记录
     */
    public int deleteInventorySynLog(InventorySynLogBean inventorySynLogBean) {
        return updateTemplate.delete(Constants.DAO_NAME_SPACE_WMS + "wms_deleteInventorySynLog", inventorySynLogBean);
    }

    /**
     * 插入新的库存同步记录历史
     */
    public int insertInventorySynLogHistory(InventorySynLogBean inventorySynLogBean) {
        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_insertInventorySynLogHistory", inventorySynLogBean);
    }

    /**
     * 获取渠道下，所有逻辑库存
     *
     * @param taskName 任务名
     * @param order_channel_id 渠道
     * @param cart_id 店铺
     * @return ViwLogicInventoryBean 集合
     */
    public List<ViwLogicInventoryBean> getLogicInventory(String taskName, String order_channel_id, String cart_id) {

        Map<String, Object> params = new HashMap<>();

        params.put("task_name", taskName);
        params.put("order_channel_id", order_channel_id);
        params.put("cart_id", cart_id);

        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_getLogicInventory", params);
    }


    /**
     * 清空所属渠道、仓库的物理库存
     *
     * @param order_channel_id 渠道
     * @param store_id 仓库
     * @param taskName 任务名
     */
    public int clearPhysicsInventoryByAll(String order_channel_id, long store_id, String taskName) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("store_id", store_id);
        params.put("task_name", taskName);

        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_clearPhysicsInventoryByAll", params);
    }

    /**
     * 清空所属渠道、仓库的相关SKU的物理库存
     *
     * @param order_channel_id 渠道
     * @param store_id 仓库
     * @param transfer_id transferId
     * @param taskName 任务名
     */
    public int clearPhysicsInventoryBySku(String order_channel_id, long store_id, long transfer_id, long transfer_item_id, String taskName) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("store_id", store_id);
        params.put("transfer_id", transfer_id);
        params.put("transfer_item_id", transfer_item_id);
        params.put("task_name", taskName);

        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_clearPhysicsInventoryBySku", params);
    }

    /**
     * 物理库存计算(全量或刷新)
     *
     * @param order_channel_id 渠道
     * @param store_id 仓库
     * @param transfer_id transferId
     * @param transfer_item_id transfer_item_id
     * @param client_inventory_hold 库存保留量
     * @param taskName 任务名
     */
    public int calcPhysicsInventory(String order_channel_id, long store_id, long transfer_id, long transfer_item_id, int client_inventory_hold, String taskName) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("store_id", store_id);
        params.put("transfer_id", transfer_id);
        params.put("transfer_item_id", transfer_item_id);
        params.put("client_inventory_hold", client_inventory_hold);
        params.put("task_name", taskName);

        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_calcPhysicsInventory", params);
    }

    /**
     * 物理库存计算（增量）
     *
     * @param order_channel_id 渠道
     * @param store_id 仓库
     * @param transfer_id transferId
     * @param transfer_item_id transfer_item_id
     * @param client_inventory_hold 库存保留量
     * @param taskName 任务名
     */
    public int calcPhysicsInventoryByIncrement(String order_channel_id, long store_id, long transfer_id, long transfer_item_id, int client_inventory_hold, String taskName) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("store_id", store_id);
        params.put("transfer_id", transfer_id);
        params.put("transfer_item_id", transfer_item_id);
        params.put("client_inventory_hold", client_inventory_hold);
        params.put("task_name", taskName);

        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_calcPhysicsInventoryByIncrement", params);
    }

    public int insertInvHis(Map<String, String> paramMap) {
        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_insertInvHis", paramMap);
    }

    public int insertNonexistentSku(Map<String, String> paramMap) {
        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_insertNonexistentSku", paramMap);
    }

    /**
     * 将wms_bt_inventory_center_logic_syn初始化
     * @return
     */
    public int truncateInventorySynTable() {
        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_truncateInventorySynTable");
    }

    /**
     * 将wms_bt_inventory_center_logic初始化
     * @return
     */
    public int deleteLogicInventory() {
        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_deleteLogicInventory");
    }

    /**
     * 更新处理完毕的Reservation记录
     *
     * @param transfer_id 处理的transfer_id
     * * @param transfer_item_id 处理的transfer_item_id
     * @param taskName 任务名
     */
    public int updateBackOrder(long transfer_id, long transfer_item_id, String taskName) {

        Map<String, Object> params = new HashMap<>();

        params.put("transfer_id", transfer_id);
        params.put("transfer_item_id", transfer_item_id);
        params.put("task_name", taskName);

        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_updateBackOrder", params);
    }

    /**
     * 清除历史库存记录
     * @param order_channel_id
     * @param cart_id
     * @return
     */
    public List<InventorySynLogBean> getInventoryHistorySynLog(String order_channel_id, String cart_id, int row_count) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("cart_id", cart_id);
        params.put("syn_flg", "0");
        params.put("limit", row_count);

        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_selectInventoryHistorySynLog", params);
    }


    /**
     * 以SKU集计物理库存
     * @param order_channel_id
     * @param status
     * @return
     */
    public List<SumInventoryBean> getSumInventoryBySKU(String order_channel_id,String status) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("status", status);
        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_selectSumInventoryBySKU", params);
    }

    /**
     * 以仓库SKU集计物理库存
     * @param order_channel_id
     * @return
     */
    public List<SumInventoryBean> getSumInventoryByStoreSKU(String order_channel_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_selectSumInventoryByStoreSKU", params);
    }

    /**
     * 获取code级别的库存值
     * @param order_channel_id
     * @return
     */
    public List<InventoryForCmsBean> selectInventoryCode(String order_channel_id, String modified) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("modified", modified);
        params.put("creater", modified);

        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_selectInventoryCode", params);
    }


}
