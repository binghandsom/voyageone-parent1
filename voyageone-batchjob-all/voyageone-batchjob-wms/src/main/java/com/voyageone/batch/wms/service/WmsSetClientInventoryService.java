package com.voyageone.batch.wms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.dao.InventoryDao;
import com.voyageone.batch.wms.dao.ReservationDao;
import com.voyageone.batch.wms.dao.TransferDao;
import com.voyageone.batch.wms.modelbean.TransferBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.StoreConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.StoreBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WmsSetClientInventoryService extends BaseTaskService {

    @Autowired
    TransferDao transferDao;

    @Autowired
    InventoryDao inventoryDao;

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    private TransactionRunner transactionRunner;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "WmsSetClientInventoryJob";
    }

    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 线程
        List<Runnable> threads = new ArrayList<>();

        // 在北京时间零点三十分，清除现有逻辑库存，重新计算最新的库存（全量推送）
        if (DateTimeUtil.getCurrentHour() == 16 && DateTimeUtil.getCurrentMinute() == 30) {
            int logicyCount = inventoryDao.deleteLogicInventory();
            logger.info("清除逻辑库存的件数："+logicyCount);
        }

        // 初始化临时表
        inventoryDao.truncateInventorySynTable();

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {

            threads.add(new Runnable() {
                @Override
                public void run() {
                    new setClientInventory(orderChannelID).doRun();
                }
            });

        }

        runWithThreadPool(threads, taskControlList);

    }

    /**
     * 按渠道进行库存计算
     */
    public class setClientInventory  {
        private OrderChannelBean channel;

        public setClientInventory(String orderChannelId) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
        }

        public void doRun() {
            logger.info(channel.getFull_name()+"库存计算开始" );

            transactionRunner.runWithTran(new Runnable() {
                @Override
                public void run() {

                    // 需要第三方渠道库存计算的记录取得
                    final List<TransferBean> transferList = transferDao.getClientInventory(channel.getOrder_channel_id());

                    logger.info(channel.getFull_name()+"-----库存计算件数："+transferList.size());

                    // 取得该渠道下所属的真实仓库
                    List<StoreBean> storeList  = StoreConfigs.getChannelStoreList(channel.getOrder_channel_id(), false, false);
                    // 判断是否有【库存由品牌方管理】的仓库
                    boolean inventory_manager = true;
                    for (StoreBean storeBean : storeList) {
                        if (storeBean.getInventory_manager().equals(StoreConfigEnums.Manager.NO.getId()) && storeBean.getIs_sale().equals(StoreConfigEnums.Sale.YES.getId())) {
                            inventory_manager = false;
                            break;
                        }
                    }

                    //分配仓库取得（如果能够取得相应的值，则说明只是模拟分配而已，不再进行逻辑库存计算）
                    String allot_store = ChannelConfigs.getVal1(channel.getOrder_channel_id(), ChannelConfigEnums.Name.allot_store);
                    if (!StringUtils.isNullOrBlank2(allot_store)) {
                        inventory_manager = false;
                    }

                    try {
                        for (TransferBean transfer : transferList) {
                            logger.info(channel.getFull_name() + "，transfer_id：" + transfer.getTransfer_id() + "，Store：" + transfer.getStore_id() + "，Origin：" + transfer.getTransfer_origin()+ "，Item_id：" + transfer.getTransfer_item_id());

                            // 所属仓库
                            StoreBean storebean = StoreConfigs.getStore(transfer.getStore_id());

                            // 如果是品牌方仓库引起的变化，则允许进行逻辑库存计算（这是为了防止品牌方库存推送延迟导致库存不一致的问题）
                            if (storebean.getInventory_manager().equals(StoreConfigEnums.Manager.NO.getId()) && StringUtils.isNullOrBlank2(allot_store)) {
                                inventory_manager = true;
                            }

                            // 客户库存保留量
                            String  client_inventory_hold = storebean.getInventory_hold();
                            if (StringUtils.isNullOrBlank2(client_inventory_hold)) {
                                client_inventory_hold = "0";
                            }

                            // 客户库存同步类型
                            String inventory_syn_type = storebean.getInventory_syn_type();

                            // 全量更新时，将原先物理库存记录清零
                            if (StoreConfigEnums.SynType.FULL.getId().equals(inventory_syn_type)) {
                                logger.info(channel.getFull_name() + "-----所属渠道仓库的物理库存值全部清零" );
                                inventoryDao.clearPhysicsInventoryByAll(transfer.getOrder_channel_id(), transfer.getStore_id(), getTaskName());
                            }
                            // 刷新时，将涉及到变化的物理库存记录清零
                            else  if (StoreConfigEnums.SynType.REFLUSH.getId().equals(inventory_syn_type)) {
                                logger.info(channel.getFull_name() + "-----所属渠道仓库的相关物理库存值清零" );
                                inventoryDao.clearPhysicsInventoryBySku(transfer.getOrder_channel_id(), transfer.getStore_id(), transfer.getTransfer_id(), transfer.getTransfer_item_id(), getTaskName());
                            }

                            logger.info(channel.getFull_name() + "-----物理库存计算(库存保留量 = " + client_inventory_hold + ",库存同步类型 = " + inventory_syn_type + " )");

                            // 物理库存计算
                            if (StoreConfigEnums.SynType.INCREMENT.getId().equals(inventory_syn_type)) {
                                inventoryDao.calcPhysicsInventoryByIncrement(transfer.getOrder_channel_id(), transfer.getStore_id(), transfer.getTransfer_id(), transfer.getTransfer_item_id(), Integer.valueOf(client_inventory_hold), getTaskName());
                            } else {
                                inventoryDao.calcPhysicsInventory(transfer.getOrder_channel_id(), transfer.getStore_id(), transfer.getTransfer_id(), transfer.getTransfer_item_id(), Integer.valueOf(client_inventory_hold), getTaskName());
                            }

                            // 如果是捡货引起的库存变化，需要将CloseDay标志位设置为2
                            if (transfer.getTransfer_origin().equals(CodeConstants.TransferOrigin.RESERVED)) {
                                logger.info(channel.getFull_name() + "-----Reservation记录更新CloseDay" );

                                // 捡货记录更新
                                reservationDao.updateReservationCloseDay(transfer.getTransfer_id(), transfer.getTransfer_item_id(), getTaskName());
                            }

                            // 如果是真实的Transfer,并且是入库或者采购时，判断BackOrder记录是否删除
                            if (transfer.getTransfer_origin().equals(CodeConstants.TransferOrigin.TRANSFER) &&
                                    (transfer.getTransfer_type().equals(CodeConstants.TransferType.IN)  || transfer.getTransfer_type().equals(CodeConstants.TransferType.PURCHASE))) {
                                logger.info(channel.getFull_name() + "-----BackOrder记录更新" );

                                // 刷新记录更新
                                inventoryDao.updateBackOrder(transfer.getTransfer_id(), transfer.getTransfer_item_id(), getTaskName());
                            }

                            logger.info(channel.getFull_name() + "-----Transfer记录更新" );

                            // 刷新记录更新
                            transferDao.updateTransfer(transfer.getTransfer_id(), transfer.getTransfer_item_id(), getTaskName());

                            // 刷新（库存由品牌方管理）时，将记录插入历史表后删除相关记录
                            if (transfer.getTransfer_origin().equals(CodeConstants.TransferOrigin.REFRESH)) {
                                logger.info(channel.getFull_name() + "-----Transfer记录移到历史表" );
                                transferDao.insertTransferItemHistory(transfer.getTransfer_id(), transfer.getTransfer_item_id(), getTaskName());

                                transferDao.deleteTransferItem(transfer.getTransfer_id(), transfer.getTransfer_item_id(), getTaskName());
                            }
                        }

                        // 如果库存需要管理 或者物理库存发生过变动，进行逻辑库存计算
                        if ( inventory_manager == true ) {
                            logger.info(channel.getFull_name() + "-----逻辑库存计算" );

                            inventoryDao.setLogicInventory(channel.getOrder_channel_id());
                        }
                    } catch (Exception e) {
                        logIssue(e, channel.getFull_name() + "库存计算错误");

                        throw new RuntimeException(e);
                    }
                }
            });

            logger.info(channel.getFull_name() + "库存计算结束");

        }
    }

}
