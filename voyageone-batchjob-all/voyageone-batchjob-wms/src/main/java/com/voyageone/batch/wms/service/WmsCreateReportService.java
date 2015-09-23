package com.voyageone.batch.wms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.service.createReport.CreateReportBaseService;
import com.voyageone.batch.wms.service.createReport.SpaldingReportService;
import com.voyageone.batch.wms.service.thirdFilePro.ThirdFileProBaseService;
import com.voyageone.batch.wms.service.thirdFilePro.WmsThirdInventoryFilePro;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2015/8/4.
 */
@Service
public class WmsCreateReportService extends BaseTaskService {

    @Autowired
    SpaldingReportService spaldingReportService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "WmsCreateReportJob";
    }
    private final Log logger = LogFactory.getLog(getClass());


    public void onStartup(List<TaskControlBean> taskControlList) throws IOException, MessagingException, InterruptedException {
        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);
        // 线程
        List<Runnable> threads = new ArrayList<>();
        final String taskNameSPD = TaskControlUtils.getTaskName(taskControlList);
        // 根据订单渠道、店铺生成日报文件
        for (final String orderChannelID : orderChannelIdList) {
            threads.add(new Runnable() {
                @Override
                public void run()  {
                    new createReport(orderChannelID,taskNameSPD).doRun();
                }
            });
        }
        runWithThreadPool(threads, taskControlList);
    }

    /**
     * 根据订单渠道、店铺生成日报文件
     */
    public class createReport {
        private OrderChannelBean channel;

        private String taskName;

        public createReport(String orderChannelId,String taskNameSPD) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
            this.taskName = taskNameSPD;
        }

        public void doRun() {
            //JCFtp下载 ， 判断下载文件时间间隔的文件路径设定
            //String filePath = Properties.readValue(WmsConstants.JCFTPCheckTimePathSetting.WMS_FTP_JC_CHECK_STOCK_PATH);
            logger.info(channel.getFull_name() + " WmsCreateReportJob 开始");
            CreateReportBaseService createReportBaseService;
            //设定订单渠道
            String channelId = channel.getOrder_channel_id();
            //for (final ShopBean shopBean : shopBeans){
                //销售记录日报数据取得
                logger.info("斯伯丁日报生成处理开始");
                //确定使用哪个模板
                createReportBaseService = spaldingReportService;

                try {
                    //生成日报文件
                    createReport(createReportBaseService, channelId);
                    logger.info("斯伯丁日报生成处理结束");
                }catch (Exception e){
                    //if(proName.equals(PRO_INVFILE_CONFIG)) {
                    logger.error( "斯伯丁日报生成处理出现错误");
//                    }else if(proName.equals(PRO_ORDFILE_CONFIG)) {
//                        logger.error( "解析订单文件出现错误");
//                    }
                    //break;
                }

            //}
            logger.info(channel.getFull_name() +  " WmsCreateReportJob 结束");
        }


        /**
         * @description 生成日报文件
         * @param channelId 渠道ID
         */
        private void createReport(final CreateReportBaseService createReportBaseService, final String channelId){
            OrderChannelBean channel = ChannelConfigs.getChannel(channelId);
            //if(thirdPartyConfigBeans.size() > 0){
                createReportBaseService.doRun(channelId,taskName);
//            }else {
//                assert channel != null;
//                logger.info("渠道" + channel.getFull_name() + "无需要处理的文件！");
//            }
        }
    }
}
