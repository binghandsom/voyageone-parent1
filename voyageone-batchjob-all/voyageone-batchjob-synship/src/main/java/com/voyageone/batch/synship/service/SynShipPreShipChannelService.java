package com.voyageone.batch.synship.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.synship.dao.ReservationDao;
import com.voyageone.batch.synship.dao.TrackingDao;
import com.voyageone.batch.synship.modelbean.ReservationBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SynShipPreShipChannelService extends BaseTaskService {

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    private TransactionRunner transactionRunner;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.SYNSHIP;
    }

    @Override
    public String getTaskName() {
        return "SynShipPreShipChannelJob";
    }

    /**
     * 获取打印的日志是否需要包含线程
     */
    @Override
    public boolean getLogWithThread() {
        return true;
    }

    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 线程
        List<Runnable> threads = new ArrayList<>();

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {

            final String exceptShipChannel = TaskControlUtils.getVal2(taskControlList, TaskControlEnums.Name.order_channel_id, orderChannelID);

            threads.add(new Runnable() {
                @Override
                public void run() {
                    new preShipChannel(orderChannelID, exceptShipChannel).doRun();
                }
            });

        }

        runWithThreadPool(threads, taskControlList);

    }

    /**
     * 预处理发货渠道，当发货渠道发生变化时，重新设置发货渠道
     */
    public class preShipChannel  {
        private OrderChannelBean channel;
        List<String> exceptShipChannelList = new ArrayList<>();

        public preShipChannel(String orderChannelId, String exceptShipChannel) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
            if (!StringUtils.isNullOrBlank2(exceptShipChannel)) {
                this.exceptShipChannelList = Arrays.asList(exceptShipChannel.split(","));
            }

        }

        public void doRun() {
            $info(channel.getFull_name() + " 预处理发货渠道开始");

            // 取得发货渠道有变化的记录
            List<ReservationBean> ReservationList = reservationDao.getPreShipChannel(channel.getOrder_channel_id(), CodeConstants.Reservation_Status.Open, exceptShipChannelList);

            $info(channel.getFull_name() + "----------预处理发货渠道的物品件数：" + ReservationList.size());

            for  (ReservationBean reservationBean : ReservationList) {

                transactionRunner.runWithTran(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            // 更新发货渠道
                            int result = reservationDao.updateReservationByShipChannel(reservationBean.getSyn_ship_no(),reservationBean.getId(),reservationBean.getStatus(),reservationBean.getShip_channel(),getTaskName());

                            if (result > 0) {
                                $info(channel.getFull_name() + "----------SynShipNo：" + reservationBean.getSyn_ship_no() + "，id:"+ reservationBean.getSyn_ship_no() + "，id:"+ reservationBean.getId() + "，ChangeShipChannel:"+ reservationBean.getShip_channel());
                                reservationDao.insertReservationLogByID(reservationBean.getSyn_ship_no(), reservationBean.getId(), "ShipChannel change to " + reservationBean.getShip_channel(), getTaskName());
                            }
                        } catch(Exception e) {
                            logIssue(e, channel.getFull_name() + " 预处理发货渠道错误，SynShipNo：" + reservationBean.getSyn_ship_no() + "，id:"+ reservationBean.getSyn_ship_no() + "，id:"+ reservationBean.getId() + "，ChangeShipChannel:"+ reservationBean.getShip_channel());

                            throw new RuntimeException(e);
                        }
                    }
                });

            }

            $info(channel.getFull_name() + " 预处理发货渠道结束");

        }
    }

}
