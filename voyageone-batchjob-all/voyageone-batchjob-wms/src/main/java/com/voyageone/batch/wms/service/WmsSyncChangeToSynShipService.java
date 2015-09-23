package com.voyageone.batch.wms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.batch.wms.dao.ReservationLogDao;
import com.voyageone.batch.wms.modelbean.PostSynshipData;
import com.voyageone.batch.wms.modelbean.SyncReservationBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WmsSyncChangeToSynShipService extends BaseTaskService {

    @Autowired
    ReservationLogDao reservationLogDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "WmsSyncChangeToSynShipJob";
    }

    // 更新错误列表（如果Synship中的Order表还没有生成，不计入错误，留待下次处理）
    List<String> errorList = new ArrayList<>();

    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        errorList.clear();

        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 抽出件数
        String row_count = TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.row_count);

        int intRowCount = 1;
        if (!StringUtils.isNullOrBlank2(row_count)) {
            intRowCount = Integer.valueOf(row_count);
        }

        // 线程
        List<Runnable> threads = new ArrayList<>();

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {

            final String postURI = TaskControlUtils.getVal2(taskControlList, TaskControlEnums.Name.order_channel_id, orderChannelID);

            final int finalIntRowCount = intRowCount;

            threads.add(new Runnable() {
                @Override
                public void run() {
                    new syncChangeToSynShip(orderChannelID, postURI, finalIntRowCount).doRun();
                }
            });
        }

        runWithThreadPool(threads, taskControlList);

        // 存在错误的记录时,抛出错误
        if (errorList.size() > 0) {
            throw new RuntimeException(WmsConstants.EmailWmsSyncChangeToSynShipJob.SUBJECT + "（ReservationID：" + errorList.toString() + "）");
        }
    }

    /**
     * 按渠道进行同步
     */
    public class syncChangeToSynShip {
        private OrderChannelBean channel;
        private String postURI;
        private int rowCount;

        public syncChangeToSynShip(String orderChannelId, String postURI, int rowCount) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
            this.postURI = postURI;
            this.rowCount = rowCount;
        }

        public void doRun() {
            // channel 不存在，说明传入的 channel id 是错误的。找不到对应的 channel
            if (channel == null) return;

            String orderChannelId = channel.getOrder_channel_id();

            logger.info(channel.getFull_name() + "同步Synship开始");

            // 处理更新忽略的记录
            int updateIgnoreSize = reservationLogDao.updateSyncIgnoreLog(orderChannelId, getTaskName());

            logger.info(channel.getFull_name() + "'s syn_flg --> 2 update success：" + updateIgnoreSize);

            // 需要同步的记录取得
            List<SyncReservationBean> syncReservationList = reservationLogDao.getSyncReservationInfo(orderChannelId, rowCount);

            logger.info(channel.getFull_name() + "同步Synship件数：" + syncReservationList.size());

            try {
                if (syncReservationList.size() < 1) return;

                PostSynshipData postSynshipData = new PostSynshipData();
                postSynshipData.setChannelId(orderChannelId);
                postSynshipData.setScrectKey(channel.getScrect_key());
                postSynshipData.setSessionKey(channel.getSession_key());
                postSynshipData.setOrderDetailDatas(syncReservationList);

                String strParam = JsonUtil.getJsonString(postSynshipData);

                logger.info(channel.getFull_name() + "'s call webservice is start at:" + DateTimeUtil.getNow());
                String strResponse = HttpUtils.post(postURI, strParam);
                logger.info(channel.getFull_name() + "'s call webservice is start end at:" + DateTimeUtil.getNow());

                // 处理返回数据
                if (StringUtils.isNullOrBlank2(strResponse)) {

                    logger.error(channel.getFull_name() + "同步Synship发生错误：调用WSDL没有返回值");

                    logIssue(channel.getFull_name() + "同步Synship发生错误：调用WSDL没有返回值");

                    return;
                }

                JSONObject paraJson = new JSONObject(strResponse);

                if (Constants.SynshipWebService.RESULT_OK.equals(paraJson.get("result").toString())) {

                    logger.info(channel.getFull_name() + "this time all ReservationId is pushed success!");

                } else {
                    if (!StringUtils.isNullOrBlank2(String.valueOf(paraJson.get("errorInfo")))) {

                        JSONObject errorInfo = paraJson.getJSONObject("errorInfo");

                        if (errorInfo != null) logger.info(errorInfo.get("errorMessageCn").toString());

                        syncReservationList.clear();

                    } else if (!StringUtils.isNullOrBlank2(String.valueOf(paraJson.get("errorReservationList")))) {

                        JSONArray errorReservationList = paraJson.getJSONArray("errorReservationList");

                        if (errorReservationList != null && errorReservationList.length() > 0) {

                            int size = errorReservationList.length();

                            for (int i = 0; i < size; i++) {
                                JSONObject obj = errorReservationList.getJSONObject(i);
                                String reservationIdFailure = obj.get("key").toString();
                                logger.info(channel.getFull_name() + " fail ReservationId：" + reservationIdFailure);
                                logger.info(channel.getFull_name() + " fail reason：" + obj.get("errorMessageCn").toString());

                                for (SyncReservationBean detail : syncReservationList) {
                                    if (detail.getReservationId().equals(reservationIdFailure)) {

                                        // 如果Synship中的Order表还没有生成，不计入错误，留待下次处理
                                        if (!Constants.SynshipWebService.ERR_CODE_RESERVATIONID_NOT_EXIST.equals(obj.get("errorCode").toString())) {
                                            detail.setErrorMessage(obj.get("errorMessageCn").toString());
                                            // 错误记录计入列表
                                            errorList.add(detail.getReservationId());
                                        }

                                        // 将错误记录从处理列表中移出
                                        syncReservationList.remove(detail);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                // 将更新成功记录的标志位设置为1
                if (syncReservationList.size() > 0) {
                    int updateSuccessSize = reservationLogDao.updateSyncSuccessLog(orderChannelId, getTaskName(), syncReservationList);

                    logger.info(channel.getFull_name() + "'s syn_flg --> 1 update success：" + updateSuccessSize);
                }

            } catch (Exception e) {
                logger.error(channel.getFull_name() + "同步Synship发生错误：", e);
                logIssue(channel.getFull_name() + "同步Synship发生错误：" + e);
            }

            logger.info(channel.getFull_name() + "同步Synship结束");
        }
    }
}
