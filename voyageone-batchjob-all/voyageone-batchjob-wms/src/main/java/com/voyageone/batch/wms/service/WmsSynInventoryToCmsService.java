package com.voyageone.batch.wms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.dao.InventoryDao;
import com.voyageone.batch.wms.modelbean.InventoryForCmsBean;
import com.voyageone.batch.wms.mongoDao.InventoryTmpDao;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
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

        //删除wms_bt_inventory_aggregate TODO

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {

            $info("channel_id=" + orderChannelID);

            //获取本渠道所有code级别库存
            List<InventoryForCmsBean> codeInventoryList =  inventoryDao.selectInventoryCode(orderChannelID,this.getTaskName());
            $info("orderChannelID:" + orderChannelID + "    库存记录数:" + codeInventoryList.size());

            //批量更新code级库存 TODO
//            bulkUpdateCodeQty(codeInventoryList);

            //获取本渠道的cart
            List<ShopBean> cartList = ShopConfigs.getChannelShopList(orderChannelID);


            for (int j = 0; j < cartList.size(); j++) {
                //获取本Cart下所有group TODO

                //批量插入group级记录到mysql 10000条插入一次 TODO


                //获取group级别库存 TODO


                //批量更新mongodb的group级别库存 TODO
            }

        }
//        $info("mongodb 批量更新开始");
//        runWithThreadPool(threads, taskControlList);
//        $info("mongodb 批量更新结束");
    }





}
