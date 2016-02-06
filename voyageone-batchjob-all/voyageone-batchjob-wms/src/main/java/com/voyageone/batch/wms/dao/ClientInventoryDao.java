package com.voyageone.batch.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.batch.wms.modelbean.ClientInventoryBean;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ClientInventoryDao extends BaseDao {

    /**
     * 删除临时库存表数据
     *
     * @param orderChannelID 订单渠道
     */
    public void delClientInventory(String orderChannelID) {

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("order_channel_id", orderChannelID);
        updateTemplate.delete(Constants.DAO_NAME_SPACE_WMS + "wms_deleteClientInventory", dataMap);
    }

    /**
     * 删除临时库存表所有数据
     *
     */
    public void delClientInventoryByAll() {

        updateTemplate.delete(Constants.DAO_NAME_SPACE_WMS + "wms_deleteClientInventoryByAll");
    }

    /**
     * 删除临时库存表中已经处理完毕的数据
     *
     * @param order_channel_id 订单渠道
     */
    public void delProcessedClientInventor(String order_channel_id, String syn_flg, String sim_flg) {

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("order_channel_id", order_channel_id);
        dataMap.put("syn_flg", syn_flg);
        dataMap.put("sim_flg", sim_flg);

        updateTemplate.delete(Constants.DAO_NAME_SPACE_WMS + "wms_deleteProcessedClientInventor", dataMap);
    }

    /**
     * 插入新的临时库存表数据
     */
    public int insertClientInventory(String clientInventoryValues) {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("values", clientInventoryValues);
        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_insertClientInventory", dataMap);
    }

    /**
     * 更新临时库存表的SKU
     * @param orderChannelID 运行渠道
     * @param takeName 任务名
     * @return int
     */
    public int updateSKUBySizeMapping(String orderChannelID, String takeName) {
        Map<String, String> params = new HashMap<>();
        params.put("order_channel_id", orderChannelID);
        params.put("task_id", takeName);
        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_updateSKUBySizeMapping", params);
    }

    /**
     * 更新临时库存表的SKU
     * @param orderChannelID 运行渠道
     * @param takeName 任务名
     * @return int
     */
    public int updateItemDetailsSizeBarcode(String orderChannelID, String takeName) {

        Map<String, String> params = new HashMap<>();
        params.put("order_channel_id", orderChannelID);
        params.put("task_id", takeName);
        params.put("active", WmsConstants.ACTIVE.USABLE);
        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_updateItemDetailsSizeBarcode", params);
    }

    /**
     * 获取临时库存表中在SizeMapping表中不存在的记录
     *
     * @param order_channel_id 渠道
     * @return ViwLogicInventoryBean 集合
     */
//    public List<ClientInventoryBean> getNotExistsSizeMapping(String order_channel_id) {
//
//        Map<String, String> params = new HashMap<>();
//        params.put("order_channel_id", order_channel_id);
//
//        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_WMS + "wms_getNotExistsSizeMapping", params);
//    }

    /**
     * 获取临时库存表中在wms_bt_item_details表中不存在的记录
     *
     * @param order_channel_id 渠道
     * @return ViwLogicInventoryBean 集合
     */
    public List<ClientInventoryBean> getNotExistsItemDetails(String order_channel_id) {

        Map<String, String> params = new HashMap<>();
        params.put("order_channel_id", order_channel_id);

        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_WMS + "wms_getNotExistsItemDetails", params);
    }

    /**
     * 临时库存表中在SizeMapping表中不存在的记录，更新临时库存表的syn_flg（更新忽略）
     * @param orderChannelID 运行渠道
     * @param takeName 任务名
     * @return int
     */
    public int updateClientInventorySynflgIgnore(String orderChannelID, String takeName) {
        Map<String, String> params = new HashMap<>();
        params.put("order_channel_id", orderChannelID);
        params.put("task_id", takeName);
        params.put("syn_flg", WmsConstants.SYN_FLG.IGONRE);
        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_updateClientInventorySynflgIgnore", params);
    }

    /**
     * 对wms_bt_item_details表中更新成功的记录，更新临时库存表的syn_flg（更新成功）
     * @param orderChannelID 运行渠道
     * @param takeName 任务名
     * @return int
     */
    public int updateClientInventorySynflgSucc(String orderChannelID, String takeName) {
        Map<String, String> params = new HashMap<>();
        params.put("order_channel_id", orderChannelID);
        params.put("task_id", takeName);
        params.put("syn_flg", WmsConstants.SYN_FLG.SUCCESS);
        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_updateClientInventorySynflgSucc", params);
    }

    /**
     * 根据参数获取指定任务的上一次运行时间，和本次运行时间
     * @param paramMap 运行渠道
     * @return int
     */
    public String getTaskRunTime(Map<String, String> paramMap) {
        return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_getTaskRunTime", paramMap);
    }

    /**
     * 用webService获取的数据关联detail表获取相关的要插入Client_inventory表的数据
     * @param tempTable 从webservice中取出的对应数据
     * @return int
     */
    public List<ClientInventoryBean> getInsertClientInvData(String tempTable) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("tempTable", tempTable);
        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_getInsertClientInvData", paramMap);
    }

    public ThirdPartyConfigBean getFullUpdtConfig(HashMap<String, String> paramMap) {
        return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_getFullUpdtConfig", paramMap);
    }

    public int setLastFullUpdateTime(Map<String, String> params) {
       return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_getClientInv_setLastFullUpdateTime", params);
    }

    public int setLastSalesUpdateTime(Map<String, String> params) {
        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_getClientInv_setLastSalesUpdateTime", params);
    }

    public int setFaildConfig(Map<String, String> params) {
        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_getClientInv_setFaildConfig", params);
    }

    public int insertClientInventoryCommonly(String tempTable) {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("values", tempTable);
        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_insertClientInventory_commonly", dataMap);
    }

    /**
     * 将一段时间之前的ClientInventoryHistory删除
     *
     * @param process_time 出力时间
     */
    public int deleteClientInventoryHistory(String process_time) {

        Map<String, Object> params = new HashMap<>();

        params.put("process_time", process_time);

        return updateTemplate.delete(Constants.DAO_NAME_SPACE_WMS + "wms_deleteClientInventoryHistory", process_time);
    }
}
