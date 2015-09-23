package com.voyageone.batch.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.wms.modelbean.AllotInventoryDetailBean;
import com.voyageone.batch.wms.modelbean.InventorySynLogBean;
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
     * 库存分配
     *
     * @param orderChannelID 订单渠道
     * @param rowCount       处理件数
     */
    public void setAllotInventory(String orderChannelID, int rowCount) {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("order_channel_id", orderChannelID);
        dataMap.put("row_count", rowCount);

        updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_setAllotInventory", dataMap);
    }

    /**
     * 取得分配库存后SKU错误的记录
     *
     * @return List<AllotInventoryDetailBean>
     */
    public List<AllotInventoryDetailBean> getErrorSkuInfo() {

        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_WMS + "wms_selectErrorSkuInfo");

    }

    /**
     * 获取不需要进行的库存同步记录（赠品SKU等）
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
}
