package com.voyageone.batch.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.wms.modelbean.SyncReservationBean;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReservationLogDao extends BaseDao {

    /**
     * 取得需要进行同步的记录
     * @param OrderChannelID 订单渠道
     * @param row_count 抽出件数
     * @return List<SyncReservationBean>
     */
    public List<SyncReservationBean> getSyncReservationInfo(String OrderChannelID,int row_count) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", OrderChannelID);
        params.put("type_id", TypeConfigEnums.MastType.reservationStatus.getId());
        params.put("limit", row_count);

        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_selectSyncReservationInfo", params);

    }

    /**
     * 处理Log表中不需要同步的纪录
     * @param orderChannelID 运行渠道
     * @param takeName 任务名
     * @return int
     */
    public int updateSyncIgnoreLog(String orderChannelID, String takeName) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", orderChannelID);
        params.put("task_id", takeName);
        params.put("cfg_name", TaskControlEnums.Name.sync_ignore_status.toString());

        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_updateSyncIgnoreLog", params);
    }

    /**
     * 处理Log表中同步成功的纪录
     * @param orderChannelID 运行渠道
     * @param takeName 任务名
     * @param reservationList 同步成功列表
     * @return int
     */
    public int updateSyncSuccessLog(String orderChannelID, String takeName, List<SyncReservationBean>reservationList) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", orderChannelID);
        params.put("task_id", takeName);
        params.put("reservationList", reservationList);

        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_updateSyncSuccessLog", params);
    }

    /**
     * 插入ReservationLog
     * @param reservationList reservation一览
     * @param resNote 备注
     * @param takeName 更新者
     * @return int
     */
    public int insertReservationLog(List<Long> reservationList, String resNote, String takeName) {

        Map<String, Object> params = new HashMap<>();

        params.put("reservationList", reservationList);
        params.put("resNote", resNote);
        params.put("takeName", takeName);

        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_bt_reservation_log_insert", params);
    }

}
