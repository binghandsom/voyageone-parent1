package com.voyageone.batch.wms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.dao.InventoryDao;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WmsSetLogicInventoryService  extends BaseTaskService {

    @Autowired
    InventoryDao inventoryDao;

    @Autowired
    private TransactionRunner transactionRunner;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "WmsSetLogicInventoryJob";
    }

    public void onStartup(List<TaskControlBean> taskControlList)  throws Exception {

        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList,TaskControlEnums.Name.order_channel_id);

        // 线程
        List<Runnable> threads = new ArrayList<>();

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {

            threads.add(new Runnable() {
                @Override
                public void run() {
                    new setLogicInventory(orderChannelID).doRun();
                }
            });

        }

        runWithThreadPool(threads, taskControlList);

    }

    /**
     * 按渠道进行逻辑库存计算
     */
    public class setLogicInventory {
        private OrderChannelBean channel;

        public setLogicInventory(String orderChannelId) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
        }

        public void doRun() {

            logger.info(channel.getFull_name()+"逻辑库存计算开始" );

            transactionRunner.runWithTran(new Runnable() {
                @Override
                public void run() {

                    try {
                        inventoryDao.setLogicInventory(channel.getOrder_channel_id());

                    } catch (Exception e) {
                        logIssue(e, channel.getFull_name() + "逻辑库存计算错误");

                        throw new RuntimeException(e);
                    }
                }
            });

            logger.info(channel.getFull_name() + "逻辑库存计算结束");

        }
    }

}
