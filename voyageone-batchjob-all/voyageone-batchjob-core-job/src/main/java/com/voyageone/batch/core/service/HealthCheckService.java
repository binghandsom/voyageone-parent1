package com.voyageone.batch.core.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.common.components.gilt.GiltHealthcheckService;
import com.voyageone.common.components.gilt.bean.GiltHealthcheck;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.OrderChannelBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HealthCheckService extends BaseTaskService {

    @Autowired
    private GiltHealthcheckService giltHealthcheckService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CORE;
    }

    @Override
    public String getTaskName() {
        return "HealthCheckJob";
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
            threads.add(new Runnable() {
                @Override
                public void run() {
                    new HealthCheck(orderChannelID).doRun();
                }
            });

        }

        runWithThreadPool(threads, taskControlList);

    }

    /**
     * 心跳检查
     */
    public class HealthCheck  {
        private OrderChannelBean channel;

        public HealthCheck(String orderChannelId) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
        }

        public void doRun() {
            $info(channel.getFull_name() + " 心跳检查开始");

            if (ChannelConfigEnums.Channel.GILT.getId().equals(channel.getOrder_channel_id())) {
                try {
                    GiltHealthcheck giltHealthcheck = giltHealthcheckService.ping();
                    if (giltHealthcheck != null) {
                        String status = giltHealthcheck.getStatus();
                        if ("pong".equals(status)) {
                            $info("Gilt Api is alive!");
                        } else {
                            logIssue("Gilt Api health check response's status is " + status);
                        }
                    } else {
                        logIssue("Gilt Api health check response is null");
                    }

                } catch (Exception ex) {
                    logIssue("Gilt Api health check have exception", ex);
                }

            }

            $info(channel.getFull_name() + " 心跳检查结束");
        }
    }

}
