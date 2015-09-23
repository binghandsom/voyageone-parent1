package com.voyageone.batch.wms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.batch.wms.dao.InventoryDao;
import com.voyageone.batch.wms.modelbean.AllotInventoryDetailBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.mail.Mail;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WmsSetAllotInventoryService extends BaseTaskService {

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
        return "WmsSetAllotInventoryJob";
    }

    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList,TaskControlEnums.Name.order_channel_id);

        // 抽出件数
        String row_count = TaskControlUtils.getVal1(taskControlList,TaskControlEnums.Name.row_count);

        int intRowCount = 1;
        if (!StringUtils.isNullOrBlank2(row_count)) {
            intRowCount = Integer.valueOf(row_count);
        }

        // 线程
        List<Runnable> threads = new ArrayList<>();

        // 根据订单渠道、店铺运行库存分配
        for (final String orderChannelID : orderChannelIdList) {

            final int finalIntRowCount = intRowCount;

            threads.add(new Runnable() {
                @Override
                public void run() {
                    new setAllotInventory(orderChannelID, finalIntRowCount).doRun();
                }
            });
        }

        runWithThreadPool(threads, taskControlList);

        // 存在错误的SKU时，业务通知邮件出力
        List<AllotInventoryDetailBean> errorSkuList = inventoryDao.getErrorSkuInfo();

        String errorSkuMail = sendErrorSkuMail(errorSkuList);

        if (!StringUtils.isNullOrBlank2(errorSkuMail)) {
            logger.info("SKU错误邮件出力");
            Mail.sendAlert(CodeConstants.EmailReceiver.VOYAGEONE_ERROR, WmsConstants.EmailSetAllotInventoryErrorSku.SUBJECT, errorSkuMail);
        }

    }

    /**
     * 按渠道进行库存分配
     */
    public class setAllotInventory {
        private OrderChannelBean channel;
        private int rowCount;

        public setAllotInventory(String orderChannelId,int rowCount) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
            this.rowCount = rowCount;
        }

        public void doRun() {

            logger.info( channel.getFull_name()+"库存分配开始" );

            transactionRunner.runWithTran(new Runnable() {
                @Override
                public void run() {

                    try {
                        inventoryDao.setAllotInventory(channel.getOrder_channel_id(), rowCount);
                    } catch (Exception e) {
                        logIssue(e, channel.getFull_name() + "库存分配发生错误");

                        throw new RuntimeException(e);
                    }

                }
            });

            logger.info(channel.getFull_name() + "库存分配结束");

        }
    }

    /**
     * 错误SKU邮件出力
     * @param errorSkuList 错误SKU一览
     * @return 错误SKU邮件内容
     */
    private String sendErrorSkuMail(List<AllotInventoryDetailBean> errorSkuList) {

        StringBuilder builderContent = new StringBuilder();

        if (errorSkuList.size() > 0) {

            StringBuilder builderDetail = new StringBuilder();

            int index = 0;
            for (AllotInventoryDetailBean errorSku : errorSkuList) {

                index = index+1;
                builderDetail.append(String.format(WmsConstants.EmailSetAllotInventoryErrorSku.ROW,
                        index,
                        ShopConfigs.getShopNameDis(errorSku.getOrder_channel_id(),errorSku.getCart_id()),
                        errorSku.getOrder_number(),
                        errorSku.getSource_order_id(),
                        errorSku.getItem_number(),
                        errorSku.getRes_id(),
                        errorSku.getProduct(),
                        errorSku.getSku()));
            }

            String count = String.format(WmsConstants.EmailSetAllotInventoryErrorSku.COUNT, errorSkuList.size());

            String detail = String.format(WmsConstants.EmailSetAllotInventoryErrorSku.TABLE, count, builderDetail.toString());

            builderContent
                    .append(Constants.EMAIL_STYLE_STRING)
                    .append(WmsConstants.EmailSetAllotInventoryErrorSku.HEAD)
                    .append(detail);


        }

        return builderContent.toString();
    }
}
