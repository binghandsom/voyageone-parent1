package com.voyageone.batch.wms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.dao.ClientInventoryDao;
import com.voyageone.batch.wms.service.thirdFilePro.*;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fred on 2015/6/26.
 * Modified by sky at 20150707
 */
@Service
@Transactional
public class WmsThirdFileDataProcessingService extends BaseTaskService {

    @Autowired
    ClientInventoryDao clientInventoryDao;

    @Autowired
    WmsThirdInventoryFilePro wmsThirdInvFilePro;

    @Autowired
    WmsThirdOrdFilePro wmsThirdOrdFilePro;

    @Autowired
    WmsThirdUpdtOrdStaPro wmsThirdUpdtOrdStaPro;

    @Autowired
    WmsInventoryIncomingPro wmsInventoryIncomingPro;

    private static String PRO_INVFILE_CONFIG = "pro_invFile_config";

    private static String PRO_ORDFILE_CONFIG = "pro_ordFile_config";

    private static String PRO_ORDSTAFILE_CONFIG = "pro_ordStaFile_config";

    private static String PRO_INVENTORY_INCOMING = "pro_inventory_incoming";

    //第三方文件配置prop_namme
    private static String[] PRO_FILE_CONFIG = {PRO_INVFILE_CONFIG, PRO_ORDFILE_CONFIG, PRO_ORDSTAFILE_CONFIG, PRO_INVENTORY_INCOMING};

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
            return "WmsThirdFileProcessJob";
    }

    /**
     * @description 相关渠道下需要处理的相关文件的配置获取
     * @param orderChannelId 渠道
     * @return proFileConfigs 配置信息集合
     */
    private List<ThirdPartyConfigBean> getProFileConfig(String orderChannelId, String proName) {
        return ThirdPartyConfigs.getThirdPartyConfigList(orderChannelId, proName);
    }

    protected void onStartup(final List<TaskControlBean> taskControlList) throws Exception {
        logger.info("----------" + getTaskName() +"----------开始");
        //允许运行的渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList,TaskControlEnums.Name.order_channel_id);
        //线程
        List<Runnable> threads = new ArrayList<>();
        ThirdFileProBaseService thirdFileProBaseService;

        //找到各渠道对应需要处理的文件
        for(String channelId : orderChannelIdList){
            OrderChannelBean channel = ChannelConfigs.getChannel(channelId);
            for(String proName : PRO_FILE_CONFIG) {
                //获得渠道下需要处理的文件配置列表
                List<ThirdPartyConfigBean> thirdConfigBeans = getProFileConfig(channelId, proName);
                if (thirdConfigBeans.size() > 0) {
                    if(proName.equals(PRO_INVFILE_CONFIG)) {
                        logger.info(channel.getFull_name()+"解析库存文件");
                        thirdFileProBaseService = wmsThirdInvFilePro;
                    }else if (proName.equals(PRO_ORDFILE_CONFIG)){
                        logger.info(channel.getFull_name()+"解析订单文件");
                        thirdFileProBaseService = wmsThirdOrdFilePro;
                    }else if (proName.equals(PRO_ORDSTAFILE_CONFIG)){
                        logger.info(channel.getFull_name()+"解析订单文件(JCTR_OrdersData_*.txt)");
                        thirdFileProBaseService = wmsThirdUpdtOrdStaPro;
                    }else if (proName.equals(PRO_INVENTORY_INCOMING)){
                        logger.info(channel.getFull_name()+"解析Shipment文件(ASN_*.dat)");
                        thirdFileProBaseService = wmsInventoryIncomingPro;
                    }else {
                        logger.info(channel.getFull_name()+"无相关需要解析的文件");
                        break;
                    }
                    try {
                        processFile(thirdConfigBeans, thirdFileProBaseService, channelId, taskControlList, threads);
                    }catch (Exception e){
                        if(proName.equals(PRO_INVFILE_CONFIG)) {
                            logger.error(channel.getFull_name()+"解析库存文件出现错误："+e);
                        }else if(proName.equals(PRO_ORDFILE_CONFIG)) {
                            logger.error(channel.getFull_name()+"解析订单文件出现错误"+e);
                        }else if(proName.equals(PRO_ORDSTAFILE_CONFIG)) {
                            logger.error(channel.getFull_name()+"解析订单文件出现错误"+e);
                        }else if(proName.equals(PRO_INVENTORY_INCOMING)) {
                            logger.error(channel.getFull_name()+"解析Shipment文件出现错误"+e);
                        }

                        break;
                    }

                }
            }
        }
        runWithThreadPool(threads, taskControlList);
        logger.info("----------" + getTaskName() +"----------结束");
    }

    /**
     * @description 库存文件处理
     * @param channelId 渠道ID
     * @param taskControlList 任务列表
     * @param threads 线程集合
     */
    private void processFile(final List<ThirdPartyConfigBean> thirdPartyConfigBeans, final ThirdFileProBaseService thirdFileProBaseService, final String channelId, final List<TaskControlBean> taskControlList,  List<Runnable> threads){
        OrderChannelBean channel = ChannelConfigs.getChannel(channelId);
        if(thirdPartyConfigBeans.size() > 0){
            threads.add(new Runnable() {
                @Override
                public void run() {
                    thirdFileProBaseService.doRun(channelId, taskControlList, thirdPartyConfigBeans);
                }
            });
        }else {
            assert channel != null;
            logger.info("渠道" + channel.getFull_name() + "无需要处理的文件！");
        }
    }
}
