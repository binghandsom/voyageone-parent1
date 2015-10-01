package com.voyageone.batch.synship.service;

import com.google.gson.Gson;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Enums.TaskControlEnums.Name;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.synship.dao.OrderDao;
import com.voyageone.batch.synship.dao.ReservationDao;
import com.voyageone.batch.synship.dao.TrackingDao;
import com.voyageone.batch.synship.modelbean.EtkTrackingBean;
import com.voyageone.common.components.eExpress.EtkConstants;
import com.voyageone.common.components.eExpress.EtkService;
import com.voyageone.common.components.eExpress.bean.ExpressShipmentTrackingReq;
import com.voyageone.common.components.eExpress.bean.ExpressTrackingDetail;
import com.voyageone.common.components.eExpress.bean.ExpressTrackingRes;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.CarrierConfigs;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.CarrierEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.Type;
import com.voyageone.common.configs.beans.CarrierBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 轮询获得ＥＴＫ面单的状态
 * <p>
 * Created by Jack on 9/30/15.
 */
@Service
public class SynShipGetEtkStatusService extends BaseTaskService {

    @Autowired
    TrackingDao trackingDao;

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    private TransactionRunner transactionRunner;

    /**
     * 获取子系统
     */
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.SYNSHIP;
    }

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "SynShipGetEtkStatusJob";
    }

    /**
     * 获取打印的日志是否需要包含线程
     */
    @Override
    public boolean getLogWithThread() {
        return true;
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        String thread_count = TaskControlUtils.getVal1(taskControlList, Name.thread_count);
        final int THREAD_COUNT = Integer.valueOf(thread_count);

        // 获取所有需要轮询数据
        List<EtkTrackingBean> etkTrackingBeans = trackingDao.getEtkTracking();

        // 如果木有数据，那自然就结束掉
        if (etkTrackingBeans == null || etkTrackingBeans.size() == 0) {
            $info("没有找到需要轮询的ETK记录");
            return;
        }

        List<Runnable> runnable = new ArrayList<>();

        List<Exception> exceptions = new ArrayList<>();

        int total = etkTrackingBeans.size();

        //按线程平分件数
        int split = total/THREAD_COUNT;
        split = split + THREAD_COUNT;

        $info("需要轮询的ETK记录件数：" + total + "，线程数：" + THREAD_COUNT + "，平分件数：" + split);

        // 将集合拆分到线程
        for (int i = 0; i < THREAD_COUNT; i++) {
            int start = i * split;
            int end = start + split;

            if (end >= total) {
                end = total;
                // 如果已经不足够了。说明就算后续还可以开线程，但是已经不需要了。
                // 标记 i 为最大，结束 for 循环
                i = THREAD_COUNT;
            }

            List<EtkTrackingBean> subList = etkTrackingBeans.subList(start, end);

            runnable.add(() -> {
                try {
                    getStatusOnThread(subList);
                } catch (Exception e) {
                    exceptions.add(e);
                }
            });
        }

        $info("生成的线程任务数：" + runnable.size());

        runWithThreadPool(runnable, taskControlList);

        // 任务结束后统一生成 issueLog
        exceptions.forEach((e) -> {
            logIssue(e.getMessage(), e.getStackTrace());
        });
    }

    protected void getStatusOnThread(List<EtkTrackingBean> etkTrackingBeans) throws Exception {

        $info("ETK提单状态追踪开始");

        $info("ETK提单状态追踪件数："+ etkTrackingBeans.size());

        for (EtkTrackingBean etkTrackingBean : etkTrackingBeans) {

            OrderChannelBean channel = ChannelConfigs.getChannel(etkTrackingBean.getOrder_channel_id());

            $info(channel.getFull_name() + "--OrderNumber：" + etkTrackingBean.getOrder_number() + "，TrackingNo：" + etkTrackingBean.getTracking_no() + "，Status：" + etkTrackingBean.getStatus());

            // 取得相关快递信息
            CarrierBean carrierBean = CarrierConfigs.getCarrier(etkTrackingBean.getOrder_channel_id(), CarrierEnums.Name.ETK);

            // 取得发货地
            String location = ChannelConfigs.getVal1(etkTrackingBean.getOrder_channel_id(), ChannelConfigEnums.Name.location);

            if (carrierBean == null) {
                // 渠道的数据不对，无法继续
                $info(channel.getFull_name() + "--渠道对应的ETK快递配置信息取得失败。");

                throw new BusinessException(channel.getFull_name() + "渠道对应的ETK快递配置信息取得失败。");
            }

            transactionRunner.runWithTran(new Runnable() {
                @Override
                public void run() {

                    try {

                        EtkService etkService = new EtkService();

                        ExpressShipmentTrackingReq expressShipmentTrackingReq = new ExpressShipmentTrackingReq();
                        expressShipmentTrackingReq.setShipment_number(etkTrackingBean.getTracking_no());
                        ExpressTrackingRes expressTrackingRes = etkService.eExpressShipmentTracking(expressShipmentTrackingReq, carrierBean);

                        $info(channel.getFull_name() + "--OrderNumber：" + etkTrackingBean.getOrder_number() + "，ETK返回结果：" + expressTrackingRes.getResult());

                        // 结果是正确时，处理此条记录，否则忽略此条记录，继续处理其他
                        if (EtkConstants.Result.T.equals(expressTrackingRes.getResult())) {

                            int index =0 ;
                            expressTrackingRes.getTrackingDetails().get(index).setEntry_datetime(DateTimeUtil.getGMTTime(DateTimeUtil.parse(expressTrackingRes.getTrackingDetails().get(index).getEntry_datetime(), DateTimeUtil.DATE_TIME_FORMAT_5), 8));
                            String entryDatetime = expressTrackingRes.getTrackingDetails().get(index).getEntry_datetime();

                            // 取得处理日期最大的记录
                            for(int i=1;i<expressTrackingRes.getTrackingDetails().size();i++) {

                                expressTrackingRes.getTrackingDetails().get(i).setEntry_datetime(DateTimeUtil.getGMTTime(DateTimeUtil.parse(expressTrackingRes.getTrackingDetails().get(i).getEntry_datetime(), DateTimeUtil.DATE_TIME_FORMAT_5), 8));

                                if (expressTrackingRes.getTrackingDetails().get(i).getEntry_datetime().compareTo(entryDatetime) > 0) {
                                    entryDatetime = expressTrackingRes.getTrackingDetails().get(i).getEntry_datetime();
                                    index = i;
                                }
                            }

                            ExpressTrackingDetail trackingDetail =expressTrackingRes.getTrackingDetails().get(index);

                            String json = trackingDetail == null ? "" : new Gson().toJson(trackingDetail);
                            $info(channel.getFull_name() + "--OrderNumber：" + etkTrackingBean.getOrder_number() + "，ETK返回最新状态：" + json);

                            switch (trackingDetail.getStatus_code()) {

                                case EtkConstants.StatusCode.Created:
                                case EtkConstants.StatusCode.Exported:
                                    break;
                                case EtkConstants.StatusCode.Inscanned:
                                case EtkConstants.StatusCode.Clearance:
                                case EtkConstants.StatusCode.Submitted:
                                    // ShippedUS状态时，变为Arrived
                                    if (CodeConstants.Reservation_Status.ShippedUS.equals(etkTrackingBean.getStatus())) {
                                        etkTrackingBean.setBefore_status(etkTrackingBean.getStatus());
                                        etkTrackingBean.setStatus(CodeConstants.Reservation_Status.Arrived);

                                        // 判断发货地是否是香港
                                        if (location.equals(EtkConstants.Location)) {
                                            etkTrackingBean.setTracking_status(CodeConstants.TRACKING.INFO_062);
                                        } else {
                                            etkTrackingBean.setTracking_status(CodeConstants.TRACKING.INFO_061);
                                        }
                                    }
                                    break;
                                case EtkConstants.StatusCode.Despatch:
                                    // ShippedUS状态时，变为Arrived
                                    if (CodeConstants.Reservation_Status.ShippedUS.equals(etkTrackingBean.getStatus())) {
                                        etkTrackingBean.setBefore_status(etkTrackingBean.getStatus());
                                        etkTrackingBean.setStatus(CodeConstants.Reservation_Status.Arrived);
                                        // 判断发货地是否是香港
                                        if (location.equals(EtkConstants.Location)) {
                                            etkTrackingBean.setTracking_status(CodeConstants.TRACKING.INFO_062);
                                        } else {
                                            etkTrackingBean.setTracking_status(CodeConstants.TRACKING.INFO_061);
                                        }
                                    }
                                    // Arrived状态时，变为Clearance
                                    else if (CodeConstants.Reservation_Status.Arrived.equals(etkTrackingBean.getStatus())) {
                                        etkTrackingBean.setBefore_status(etkTrackingBean.getStatus());
                                        etkTrackingBean.setStatus(CodeConstants.Reservation_Status.Clearance);
                                        etkTrackingBean.setTracking_status(CodeConstants.TRACKING.INFO_072);
                                    }
                                    break;
                                default:
                                    $info(channel.getFull_name() + "--OrderNumber：" + etkTrackingBean.getOrder_number() + "当前ETK提单状态无法处理");
                                    logIssue(channel.getFull_name() + "--当前ETK提单状态无法处理", "Order_Number：" + etkTrackingBean.getOrder_number() + "，TrackingNo：" + etkTrackingBean.getTracking_no() + "，Message：" + expressTrackingRes.getMsg());
                                    break;
                            }

                            // 物流状态有变化时进行更新
                            if (StringUtils.isNullOrBlank2(etkTrackingBean.getTracking_status())) {
                                $info(channel.getFull_name() + "--Order_Number：" + etkTrackingBean.getOrder_number() + "'s Status no change");
                            }
                            else {

                                String notes = "Status changed to："+ Type.getTypeName(TypeConfigEnums.MastType.reservationStatus.getId(), etkTrackingBean.getStatus());

                                $info(channel.getFull_name() + "--Order_Number：" + etkTrackingBean.getOrder_number() + "'s" + notes);

                                // 订单状态变更
                                orderDao.updateOrderByStatus(etkTrackingBean.getSyn_ship_no(), etkTrackingBean.getStatus(), etkTrackingBean.getBefore_status(), getTaskName());

                                // Reservation状态变更
                                reservationDao.updateReservationByStatus(etkTrackingBean.getSyn_ship_no(), etkTrackingBean.getStatus(), etkTrackingBean.getBefore_status(), getTaskName());

                                // 插入物品日志
                                reservationDao.insertReservationLogByStatus(etkTrackingBean.getSyn_ship_no(), notes, etkTrackingBean.getStatus() , getTaskName());

                                // 物流信息的追加
                                trackingDao.insertTrackingInfo(etkTrackingBean.getSyn_ship_no(),etkTrackingBean.getTracking_no(),etkTrackingBean.getTracking_status(),trackingDetail.getEntry_datetime(),getTaskName());

                                // 快递100未订阅时，订阅快递100
                                if (CodeConstants.KD100_POLL.NO.equals(etkTrackingBean.getSent_kd100_poll_flg())){
                                    trackingDao.updateKD100Poll(etkTrackingBean.getTracking_type(), etkTrackingBean.getTracking_no(), CodeConstants.KD100_POLL.YES, getTaskName());
                                }

                            }

                        }
                        else {
                            // 结果是错误时，忽略此条记录，继续处理其他
                            $info(channel.getFull_name() + "--Order_Number：" + etkTrackingBean.getOrder_number()+"，ETK提单状态取得错误，无法更新");
                            logIssue(channel.getFull_name() + "--ETK提单状态取得错误，无法更新", "Order_Number：" + etkTrackingBean.getOrder_number() + "，TrackingNo：" + etkTrackingBean.getTracking_no() +  "，Message：" + expressTrackingRes.getMsg());
                        }

                    } catch (Exception e) {
                        $info(channel.getFull_name() + "--ETK提单状态追踪失败" + e);
                        //logIssue(e, channel.getFull_name() + "--ETK提单状态追踪错误");

                        throw new RuntimeException(e);
                    }
                }

            });
        }

        $info("ETK提单状态追踪结束");


    }


}
