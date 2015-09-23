package com.voyageone.batch.wms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.dao.InventoryDao;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author  sky
 * @CreateDate 20150715
 */
@Service
@Transactional
public class WmsInvHisCalcService extends BaseTaskService {

    @Autowired
    InventoryDao inventoryDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "WmsInvHisCalcJob";
    }

    protected void onStartup(final List<TaskControlBean> taskControlList) throws Exception {

        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 线程
        List<Runnable> threads = new ArrayList<>();

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {
            threads.add(() -> new InvHisCalc(orderChannelID).doRun());
        }

        runWithThreadPool(threads, taskControlList);
    }

    /**
     * 按渠道进行库存数据记录
     */
    public class InvHisCalc {

        private OrderChannelBean channel;

        public InvHisCalc(String orderChannelId) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
        }

        public void doRun() {
            log("渠道 " + channel.getFull_name() + " 月库存记录开始" );
            Map<String, String> paramMap = setParamMap();
            proNonexistentSku(paramMap);
            insertInvHis(paramMap);
            log("渠道 " + channel.getFull_name() + " 月库存记录结束" );
        }

        private Map<String,String> setParamMap() {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("orderChannelId", channel.getOrder_channel_id());
            paramMap.put("calTimeE", getCalcGMTTime(-1));
            paramMap.put("calTimeS", getCalcGMTTime(-2));
            paramMap.put("taskName", getTaskName());
            return paramMap;
        }

        /**
         * @description 获取到针对于本地时间来说的第一天零时，对应格林威治时间;如本地时间为 2015-07-01 00:00:00,那对应时间为 2015-06-30 16:00:00
         * @param offsetMonth 获得每月的第一个日期
         * @return 计算开始时间
         */
        private String getCalcGMTTime(int offsetMonth) {
            String localTime = DateTimeUtil.format(DateTimeUtil.addDays(DateTimeUtil.getMonthLastDay(DateTimeUtil.addMonths(offsetMonth)), 1), DateTimeUtil.DEFAULT_DATE_FORMAT) + " 00:00:00";
            String GMTTime = DateTimeUtil.getGMTTimeFrom(localTime, getTimeZone());
            if(offsetMonth == -1) {
                log( "计算截止时间为：" + "本地时间：" + localTime + "；格林威治时间：" + GMTTime);
            }else{
                log( "计算开始时间为：" + "本地时间：" + localTime + "；格林威治时间：" + GMTTime);
            }
            return GMTTime;
        }

        private int getTimeZone() {
            String timeZoneStr = ChannelConfigs.getVal1(channel.getOrder_channel_id(), ChannelConfigEnums.Name.channel_time_zone);
            if(StringUtils.isEmpty(timeZoneStr)){
                log("没有配置渠道时区，按默认时区处理！");
                return 0;
            }else {
                return Integer.parseInt(timeZoneStr);
            }
        }

        private void proNonexistentSku(Map<String, String> paramMap) {
            log("处理wms_bt_inventory_history表中没有记录的sku开始");
            log(inventoryDao.insertNonexistentSku(paramMap) + "个sku已经处理");
            log("处理wms_bt_inventory_history表中没有记录的sku结束");
        }

        private void insertInvHis(Map<String, String> paramMap) {
            log("插入库存数据到wms_bt_inventory_history表开始");
            log(inventoryDao.insertInvHis(paramMap) + "记录成功插入 wms_bt_inventory_history");
            log("插入库存数据到wms_bt_inventory_history表结束");
        }

        private void log(String msg){
            logger.info("===============" + msg + "===============");
        }
    }

}