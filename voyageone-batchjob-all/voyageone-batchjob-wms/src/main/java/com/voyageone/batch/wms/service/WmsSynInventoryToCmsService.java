package com.voyageone.batch.wms.service;

import com.mongodb.WriteResult;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.dao.InventoryDao;
import com.voyageone.batch.wms.modelbean.InventoryForCmsBean;
import com.voyageone.batch.wms.mongoDao.InventoryTmpDao;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WmsSynInventoryToCmsService extends BaseTaskService {


    @Autowired
    private InventoryTmpDao inventoryTmpDao;

    @Autowired
    private InventoryDao inventoryDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "wmsSynInventoryToCmsJob";
    }

    @Override
    public boolean getLogWithThread() {
        return true;
    }
    static int i = 1;

    /**
     * 批量插入code级别的库存数据到mongdodb，以便db端的定时任务进行处理
     * @param taskControlList job 配置
     * @throws Exception
     */
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 获取允许运行的渠道
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        $info("channel_id=" + TaskControlEnums.Name.order_channel_id);
        $info("orderChannelIdList=" + orderChannelIdList.size());
        // 线程
        List<Runnable> threads = new ArrayList<>();

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {

            $info("channel_id=" + orderChannelID);
//            threads.add(() -> new getSuperFeed(orderChannelID).doRun());
            //如果 dbserver的定时任务运行中，本任务就跳过。或者临时表记录 > 0
            if (inventoryTmpDao.countAll() != 0 ){
                continue;
            }
            //获取本渠道所有code级别库存
            List<InventoryForCmsBean> codeInventoryList =  inventoryDao.selectInventoryCode(orderChannelID,this.getTaskName());
            $info("orderChannelID:" + orderChannelID + "    库存记录数:" + codeInventoryList.size());
            //将库存插入mongodb的临时表以便，dbserver端的cron程序进行批量处理
            WriteResult result = inventoryTmpDao.insertAll(codeInventoryList);


            $info("result:" + result.toString());

            //更新本渠道mongodb中对应code的库存值
//            productDao.updateCodeInventory(codeInventoryList);
//            for (Inventory item:codeInventoryList){
//                threads.add(() -> {
//
//                    try {
//                        $info(i + "/" +codeInventoryList.size() );
//                        i++;
//                        productDao.updateCodeInventory(item);
//
//                    }catch (Exception e){
//                        $info("mongodb有错误啦："+e.getMessage());
//                    }
//
//                });
//            }
        }
//        $info("mongodb 批量更新开始");
//        runWithThreadPool(threads, taskControlList);
//        $info("mongodb 批量更新结束");
    }





}
