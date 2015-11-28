package com.voyageone.batch.cms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.dao.InventoryDao;
import com.voyageone.batch.cms.model.InventoryModel;
import com.voyageone.batch.cms.mongoDao.ProductDao;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.common.components.issueLog.enums.SubSystem;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class CmsSynInventoryService extends BaseTaskService {


    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private ProductDao productDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsSynInventoryJob";
    }

    @Override
    public boolean getLogWithThread() {
        return true;
    }
    static int i = 1;

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
            //获取本渠道所有code级别库存
            List<InventoryModel> codeInventoryList =  inventoryDao.getCodeInventory(orderChannelID);


            //更新本渠道mongodb中对应code的库存值
            productDao.updateCodeInventory(codeInventoryList);
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
        $info("mongodb 批量更新开始");
        runWithThreadPool(threads, taskControlList);
        $info("mongodb 批量更新结束");
    }





}
