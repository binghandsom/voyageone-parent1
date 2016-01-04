package com.voyageone.batch.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.wms.modelbean.ReceiptConfirmationBean;
import com.voyageone.batch.wms.modelbean.SimTransferBean;
import com.voyageone.batch.wms.modelbean.TransferBean;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TransferDao extends BaseDao {

    /**
     * 取得需要模拟入出库的记录（TransferOut、CloseDay、Return、Stock_Plus、Stock_Minus）
     * @param OrderChannelID 订单渠道
     * @return List<SimTransferBean>
     */
    public List<SimTransferBean> getSimTransferInfo(String OrderChannelID) {

        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_selectSimTransferInfo", OrderChannelID);

    }

    /**
     * 进行入出库模拟
     * @param simTransfer 模拟入出库记录
     */
    public void setSimTransferInfo(SimTransferBean simTransfer) {
        updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_simTransferInfo", simTransfer);
    }

    /**
     * 取得需要第三方渠道库存计算的记录
     * @param orderChannelID 订单渠道
     * @return List<TransferBean>
     */
    public List<TransferBean> getClientInventory(String orderChannelID) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", orderChannelID);

        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_selectClientInventory", params);

    }

    /**
     * 更新处理完毕的Transfer
     *
     * @param transfer_id 处理的transfer_id
     * @param transfer_item_id 处理的transfer_item_id
     * @param taskName 任务名
     */
    public int updateTransfer(long transfer_id, long transfer_item_id, String taskName) {

        Map<String, Object> params = new HashMap<>();

        params.put("transfer_id", transfer_id);
        params.put("transfer_item_id", transfer_item_id);
        params.put("task_name", taskName);

        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_updateTransfer", params);
    }

    /**
     * 将更新处理完毕的Transfer插入历史表
     *
     * @param transfer_id 处理的transfer_id
     * @param transfer_item_id 处理的transfer_item_id
     * @param taskName 任务名
     */
    public int insertTransferItemHistory(long transfer_id, long transfer_item_id, String taskName) {

        Map<String, Object> params = new HashMap<>();

        params.put("transfer_id", transfer_id);
        params.put("transfer_item_id", transfer_item_id);
        params.put("task_name", taskName);

        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_insertTransferItemHistory", params);
    }

    /**
     * 将更新处理完毕的Transfer删除
     *
     * @param transfer_id 处理的transfer_id
     * @param transfer_item_id 处理的transfer_item_id
     * @param taskName 任务名
     */
    public int deleteTransferItem(long transfer_id, long transfer_item_id, String taskName) {

        Map<String, Object> params = new HashMap<>();

        params.put("transfer_id", transfer_id);
        params.put("transfer_item_id", transfer_item_id);
        params.put("task_name", taskName);

        return updateTemplate.delete(Constants.DAO_NAME_SPACE_WMS + "wms_deleteTransferItem", params);
    }

    /**
     * 将一段时间之前的TransferHistory删除(刷新库存的记录删除)
     *
     * @param process_time 出力时间
     */
    public int deleteTransferHistory(String process_time) {

        Map<String, Object> params = new HashMap<>();

        params.put("process_time", process_time);

        return updateTemplate.delete(Constants.DAO_NAME_SPACE_WMS + "wms_deleteTransferHistory", process_time);
    }

    /**
     * 将一段时间之前的TransferDetail删除(刷新库存的记录删除)
     *
     * @param process_time 出力时间
     */
    public int deleteTransferDetail(String process_time) {

        Map<String, Object> params = new HashMap<>();

        params.put("process_time", process_time);

        return updateTemplate.delete(Constants.DAO_NAME_SPACE_WMS + "wms_deleteTransferDetail", process_time);
    }

    /**
     * 将一段时间之前的Transfer删除(刷新库存的记录删除)
     *
     * @param process_time 出力时间
     */
    public int deleteTransfer(String process_time) {

        Map<String, Object> params = new HashMap<>();

        params.put("process_time", process_time);

        return updateTemplate.delete(Constants.DAO_NAME_SPACE_WMS + "wms_deleteTransfer", process_time);
    }

    /**
     * 取得Receipt Confirmation记录
     * @param OrderChannelID 订单渠道
     * @return List<ReceiptConfirmationBean>
     */
    public List<ReceiptConfirmationBean> getReceiptConfirmation(String OrderChannelID) {

        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_bt_client_shipment_getReceiptConfirmation", OrderChannelID);

    }

    /**
     * 置位已经生成文件的记录
     *
     * @param successConfirm 出力时间
     */
    public int updateReceiptConfirmInfo(Map<String, String> successConfirm) {

        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_bt_client_shipment_updateReceiptConfirmInfo", successConfirm);
    }
}
