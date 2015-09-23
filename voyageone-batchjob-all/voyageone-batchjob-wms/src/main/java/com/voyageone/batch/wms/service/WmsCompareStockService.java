package com.voyageone.batch.wms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.dao.TakeStockDao;
import com.voyageone.batch.wms.modelbean.TakeStockBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WmsCompareStockService extends BaseTaskService {

    @Autowired
    private TakeStockDao takeStockDao;

    @Autowired
    private TransactionRunner transactionRunner;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "WmsCompareStockJob";
    }

    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

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

            final int finalIntRowCount = intRowCount;

            threads.add(new Runnable() {
                @Override
                public void run() {
                    new compareStock(orderChannelID, finalIntRowCount).doRun();
                }
            });

        }

        runWithThreadPool(threads, taskControlList);

    }

    /**
     * 按渠道进行模拟入出库
     */
    public class compareStock {
        private OrderChannelBean channel;
        private int rowCount;

        public compareStock(String orderChannelId, int rowCount) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
            this.rowCount = rowCount;
        }

        public void doRun() {
            logger.info(channel.getFull_name() + "盘点比较开始");

            // 需要盘点比较的记录取得
            final List<TakeStockBean> takeStockList = takeStockDao.getTakeStockInfo(channel.getOrder_channel_id(), rowCount);

            logger.info(channel.getFull_name() + "盘点比较件数：" + takeStockList.size());

            transactionRunner.runWithTran(new Runnable() {
                @Override
                public void run() {

                    try {
                        // 调用存储过程顺序进行盘点比较
                        for (TakeStockBean takeStockBean : takeStockList) {
                            logger.info(channel.getFull_name() + "，Store：" + takeStockBean.getStore_id() + "，Take_Stock_ID：" + takeStockBean.getTake_stock_id());

                            takeStockDao.compareStock(takeStockBean);
                        }
                    } catch (Exception e) {

                        logIssue(e, channel.getFull_name() + "盘点比较发生错误");

                        throw new RuntimeException(e);
                    }

                }
            });

            logger.info(channel.getFull_name() + "盘点比较结束");
        }
    }

}
