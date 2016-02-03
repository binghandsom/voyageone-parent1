package com.voyageone.batch.ims.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.ims.dao.SendProductDao;
import com.voyageone.batch.ims.modelbean.SendProductBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.*;
import com.voyageone.ws.client.getOrderService.GetOrderService;
import com.voyageone.ws.client.getOrderService.GetOrderServicePortType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by simin on 2016/1/26.
 */
@Repository
public class ImsJumeiSendProductService extends BaseTaskService {
    private static Log logger = LogFactory.getLog(ImsJumeiSendProductService.class);
    @Autowired
    SendProductDao sendProductDao;
    @Autowired
    private TransactionRunner transactionRunner;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.IMS;
    }

    public String getTaskName() {
        return "ImsJumeiSendProductJob";
    }

    @Override
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

            final String postURI = TaskControlUtils.getVal2(taskControlList, TaskControlEnums.Name.order_channel_id, orderChannelID);
            final int finalIntRowCount = intRowCount;
            threads.add(() -> new SendProduct(orderChannelID, postURI, finalIntRowCount).dorun());
        }
        runWithThreadPool(threads, taskControlList);
    }


    public class SendProduct {

        private OrderChannelBean channel;
        private String postURI;
        private int rowCount;

        public SendProduct(String orderChannelId, String postURI, int rowCount) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
            this.postURI = postURI;
            this.rowCount = rowCount;
        }

        private List<SendProductBean> sendProductList(String channel_id, int rowCount) {
            return sendProductDao.selectSendProduct(channel_id, rowCount);
        }


        public void dorun() {
            logger.info(channel.getFull_name() + "开始推送数据");
            List<SendProductBean> sendproductList = sendProductList(channel.getOrder_channel_id(), rowCount);
            logger.info(channel.getFull_name() + "推送数据件数：" + sendproductList.size());
            for (SendProductBean productBean : sendproductList) {
                try {
                    String result = getSendCode(productBean.getChannel_id(), productBean.getProduct_code());
                    Date now = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if (result.contains("Success")) {
                        String time = dateFormat.format(now);
                        sendProductDao.updateSendProductSend_flg(time, getTaskName().toString(), productBean);
                        logger.info(channel.getOrder_channel_id() + "-----" + productBean.getProduct_code() + "-----" + time + "-----推送成功");
                    } else {
                        logger.info(channel.getFull_name() + productBean.getProduct_code() + result);
                        String time = dateFormat.format(now);
                        logIssue(getTaskName() + "---" + getSubSystem().toString() + "---推送数据：---" + productBean.getProduct_code() + "--失败--" + time);
                    }
                } catch (Exception e) {
                    logger.info(channel.getFull_name() + productBean.getProduct_code() + e);
                    Date now = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = dateFormat.format(now);
                    logIssue(getTaskName() + "---" + getSubSystem().toString() + "---推送数据：---" + productBean.getProduct_code() + "---异常---" + time);
                }
            }
            logger.info(channel.getFull_name() + "推送数据结束");
        }

        private String getSendCode(String orderChannel_id, String product_code) throws Exception {

            GetOrderService service = new GetOrderService();

            GetOrderServicePortType soap = service.getGetOrderServiceHttpPort();

            String result = soap.getJMOnlieCode(orderChannel_id, product_code);

            return result;
        }
    }
}
