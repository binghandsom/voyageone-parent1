package com.voyageone.batch.synship.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.synship.dao.TrackingDao;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SynShipSetTakeFlgService extends BaseTaskService {

    @Autowired
    TrackingDao trackingDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.SYNSHIP;
    }

    @Override
    public String getTaskName() {
        return "SynShipSetTakeFlgJob";
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

            final String timeInterval = TaskControlUtils.getVal2(taskControlList, TaskControlEnums.Name.order_channel_id, orderChannelID);

            threads.add(new Runnable() {
                @Override
                public void run() {
                    new setTakeFlg(orderChannelID, timeInterval).doRun();
                }
            });

        }

        runWithThreadPool(threads, taskControlList);

    }

    /**
     * 按渠道轮询获得几个小时后的订单的物流信息，如果无任何物流信息的话，则自动插入揽收标志
     */
    public class setTakeFlg  {
        private OrderChannelBean channel;
        private int timeInterval = 24;

        public setTakeFlg(String orderChannelId, String timeInterval) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
            if (!StringUtils.isNullOrBlank2(timeInterval)) {
                this.timeInterval = Integer.parseInt(timeInterval);
            }

        }

        public void doRun() {
            $info(channel.getFull_name() + " 物流揽收信息设置开始");

            //处理时间
            String processTime = DateTimeUtil.getLocalTime(timeInterval * -1);

            // 模拟 Shipment 失败的记录取得
            List<String> synShipNoList = trackingDao.getNotTrackingList(channel.getOrder_channel_id(), processTime);

            $info(channel.getFull_name() + "----------没有任何物流信息的订单件数：" + synShipNoList.size());

            for  (String synShipNo : synShipNoList) {
                $info(channel.getFull_name() + "----------SynShipNo：" + synShipNo);
                // 是否存在的检查，存在的场合不插入记录
                int trackingExists = trackingDao.selectTrackingByStatus(synShipNo, CodeConstants.TRACKING.INFO_010);

                if (trackingExists == 0) {
                    trackingDao.insertTrackingByStatus(synShipNo, CodeConstants.TRACKING.INFO_010, 0, getTaskName());
                }

            }

            $info(channel.getFull_name() + " 物流揽收信息设置结束");

        }
    }

}
