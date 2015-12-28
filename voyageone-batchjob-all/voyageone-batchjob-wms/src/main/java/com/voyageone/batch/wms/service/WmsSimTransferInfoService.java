package com.voyageone.batch.wms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.dao.ClientInventoryDao;
import com.voyageone.batch.wms.dao.TransferDao;
import com.voyageone.batch.wms.modelbean.SimTransferBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WmsSimTransferInfoService  extends BaseTaskService {

    @Autowired
    TransferDao transferDao;

    @Autowired
    ClientInventoryDao clientInventoryDao;

    @Autowired
    private TransactionRunner transactionRunner;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "WmsSimTransferInfoJob";
    }

    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 线程
        List<Runnable> threads = new ArrayList<>();

        String process_time = DateTimeUtil.getLocalTime(DateTimeUtil.getDate(), -4);

        int transferCount = transferDao.deleteTransferHistory( process_time);

        logger.info("处理过期TransferItem数据日期："+process_time + "，处理件数："+transferCount);

        transferCount = transferDao.deleteTransferDetail(process_time);

        logger.info("处理过期TransferDetail数据日期："+process_time + "，处理件数："+transferCount);

        transferCount = transferDao.deleteTransfer( process_time);

        logger.info("处理过期Transfer数据日期："+process_time + "，处理件数："+transferCount);

        int inventoryCount = clientInventoryDao.deleteClientInventoryHistory(process_time);

        logger.info("处理过期InventoryHistory数据日期："+process_time + "，处理件数："+inventoryCount);

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {

            threads.add(new Runnable() {
                @Override
                public void run() {
                    new simTransferInfo(orderChannelID).doRun();
                }
            });

        }

        runWithThreadPool(threads, taskControlList);

    }

    /**
     * 按渠道进行模拟入出库
     */
    public class simTransferInfo  {
        private OrderChannelBean channel;

        public simTransferInfo(String orderChannelId) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
        }

        public void doRun() {
            logger.info(channel.getFull_name()+"入出库模拟开始" );

            // 需要模拟入出库的记录取得
            final List<SimTransferBean> simTransferList = transferDao.getSimTransferInfo(channel.getOrder_channel_id());

            logger.info(channel.getFull_name()+"入出库模拟件数："+simTransferList.size());

            // 获取当前日期
            final String dateFormat = DateTimeUtil.format(DateTimeUtil.getDate(), DateTimeUtil.DEFAULT_DATE_FORMAT);

            for (SimTransferBean simTransfer : simTransferList) {

                transactionRunner.runWithTran(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            logger.info(channel.getFull_name() + "，Origin：" + simTransfer.getTransfer_origin() + "，Store：" + simTransfer.getStore_id() + "，Process_ID：" + simTransfer.getProcess_id());

                            // 订单渠道
                            simTransfer.setOrder_channel_id(channel.getOrder_channel_id());

                            // 入出库状态
                            simTransfer.setTransfer_status(CodeConstants.TransferStatus.ClOSE);

                            // 根据来源编辑数据
                            switch (simTransfer.getTransfer_origin()) {

                                // TransferOut的场合，模拟TransferIn
                                case CodeConstants.TransferOrigin.TRANSFER:

                                    // 入出库类型
                                    simTransfer.setTransfer_type(CodeConstants.TransferType.IN);

                                    // 入出库名称
                                    simTransfer.setTransfer_name(dateFormat + "_" + simTransfer.getTransfer_origin() + "_" + simTransfer.getStore_id() + "_" + simTransfer.getProcess_id());

                                    // 备注
                                    simTransfer.setComment("Origin（TransferOut）：" + simTransfer.getProcess_id());

                                    // 来源ID
                                    simTransfer.setOrigin_id(simTransfer.getProcess_id());

                                    break;

                                // CloseDay的场合，模拟TransferOut
                                case CodeConstants.TransferOrigin.RESERVED:

                                    // 入出库类型
                                    simTransfer.setTransfer_type(CodeConstants.TransferType.OUT);

                                    // 入出库名称
                                    simTransfer.setTransfer_name(dateFormat + "_" + simTransfer.getTransfer_origin() + "_" + simTransfer.getStore_id());

                                    // 备注
                                    simTransfer.setComment("Origin（RESERVED）");

                                    // 来源ID
                                    simTransfer.setOrigin_id("0");

                                    break;

                                // 退货（退回仓库）的场合，模拟TransferIn
                                case CodeConstants.TransferOrigin.RETURNED:

                                    // 入出库类型
                                    simTransfer.setTransfer_type(CodeConstants.TransferType.IN);

                                    // 入出库名称
                                    simTransfer.setTransfer_name(dateFormat + "_" + simTransfer.getTransfer_origin() + "_" + simTransfer.getStore_id() + "_" + simTransfer.getProcess_id());

                                    // 备注
                                    simTransfer.setComment("Origin（RETURNED：0）：" + simTransfer.getProcess_id());

                                    // 来源ID
                                    simTransfer.setOrigin_id(simTransfer.getProcess_id());

                                    break;

                                // 退货（退回品牌方）的场合，模拟TransferIn、Withdrawal
                                case CodeConstants.TransferOrigin.WITHDRAWAL:

                                    // 入出库类型
                                    simTransfer.setTransfer_type(CodeConstants.TransferType.IN);

                                    // 入出库名称
                                    simTransfer.setTransfer_name(dateFormat + "_" + simTransfer.getTransfer_origin() + "_" + simTransfer.getStore_id() + "_" + simTransfer.getProcess_id());

                                    // 备注
                                    simTransfer.setComment("Origin（RETURNED：1）：" + simTransfer.getProcess_id());

                                    // 来源ID
                                    simTransfer.setOrigin_id(simTransfer.getProcess_id());

                                    break;

                                // 盘盈的场合，模拟TransferIn
                                case CodeConstants.TransferOrigin.PLUS:

                                    // 入出库类型
                                    simTransfer.setTransfer_type(CodeConstants.TransferType.IN);

                                    // 入出库名称
                                    simTransfer.setTransfer_name(dateFormat + "_" + simTransfer.getTransfer_origin() + "_" + simTransfer.getStore_id() + "_" + simTransfer.getProcess_id());

                                    // 备注
                                    simTransfer.setComment("Origin（Stock Plus）：" + simTransfer.getProcess_id());

                                    // 来源ID
                                    simTransfer.setOrigin_id(simTransfer.getProcess_id());

                                    break;

                                // 盘亏的场合，模拟TransferOut
                                case CodeConstants.TransferOrigin.MINUS:

                                    // 入出库类型
                                    simTransfer.setTransfer_type(CodeConstants.TransferType.OUT);

                                    // 入出库名称
                                    simTransfer.setTransfer_name(dateFormat + "_" + simTransfer.getTransfer_origin() + "_" + simTransfer.getStore_id() + "_" + simTransfer.getProcess_id());

                                    // 备注
                                    simTransfer.setComment("Origin（Stock Minus）：" + simTransfer.getProcess_id());

                                    // 来源ID
                                    simTransfer.setOrigin_id(simTransfer.getProcess_id());

                                    break;

                                // 第三方渠道的场合，模拟刷新
                                case CodeConstants.TransferOrigin.REFRESH:

                                    logger.info(channel.getFull_name() + "删除第三方库存表中处理完毕的记录开始");
                                    // 删除处理完毕的记录
                                    clientInventoryDao.delProcessedClientInventor(channel.getOrder_channel_id(), "1", "1");
                                    logger.info(channel.getFull_name() + "删除第三方库存表中处理完毕的记录结束");

                                    logger.info(channel.getFull_name() + "删除第三方库存表中处理忽略的记录开始");
                                    // 删除处理完毕的记录
                                    clientInventoryDao.delProcessedClientInventor(channel.getOrder_channel_id(), "4", "4");
                                    logger.info(channel.getFull_name() + "删除第三方库存表中处理忽略的记录结束");

                                    // 入出库类型
                                    simTransfer.setTransfer_type(CodeConstants.TransferType.REFRESH);

                                    // 入出库名称
                                    simTransfer.setTransfer_name(dateFormat + "_" + simTransfer.getTransfer_origin() + "_" + simTransfer.getStore_id() + "_" + simTransfer.getProcess_id());

                                    // 备注
                                    simTransfer.setComment("Origin（Refresh）：" + simTransfer.getProcess_id());

                                    // 来源ID
                                    simTransfer.setOrigin_id(simTransfer.getProcess_id());

                                    break;

                                // 上述来源以外的场合，作为错误记录
                                default:
                                    simTransfer.setErrorInfo("来源不明");
                                    break;

                            }

                            if (StringUtils.isNullOrBlank2(simTransfer.getErrorInfo()) && !StringUtils.isNullOrBlank2(simTransfer.getProcess_id())) {
                                transferDao.setSimTransferInfo(simTransfer);
                            }

                        } catch (Exception e) {
                            logIssue(e, channel.getFull_name() + "入出库模拟错误");

                            throw new RuntimeException(e);
                        }

                    }
                });
            }

            logger.info(channel.getFull_name() + "入出库模拟结束");

        }
    }

}
