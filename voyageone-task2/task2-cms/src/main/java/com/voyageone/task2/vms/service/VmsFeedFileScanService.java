package com.voyageone.task2.vms.service;

import com.csvreader.CsvReader;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.VmsChannelConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.VmsChannelConfigBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.MD5;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.com.MessageBean;
import com.voyageone.service.dao.vms.VmsBtFeedFileDao;
import com.voyageone.service.dao.vms.VmsBtFeedInfoTempDao;
import com.voyageone.service.daoext.vms.VmsBtFeedFileDaoExt;
import com.voyageone.service.daoext.vms.VmsBtFeedInfoTempDaoExt;
import com.voyageone.service.impl.cms.feed.FeedToCmsService;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.service.model.vms.VmsBtFeedFileModel;
import com.voyageone.service.model.vms.VmsBtFeedInfoTempModel;
import com.voyageone.task2.base.BaseMQCmsService;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.vms.VmsConstants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 扫描Vendor通过ftp上传的Csv文件，并加入文件管理表（vms_bt_feed_file）
 * Created on 16/06/29.
 * @author jeff.duan
 * @version 1.0
 */
@Service
public class VmsFeedFileScanService extends BaseTaskService {

    @Autowired
    private VmsBtFeedFileDao vmsBtFeedFileDao;

    @Autowired
    private MqSender sender;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.VMS;
    }

    @Override
    public String getTaskName() {
        return "VmsFeedFileScanJob";
    }


    /**
     * 扫描Vendor通过ftp上传的Csv文件，并加入文件管理表（vms_bt_feed_file）
     *
     * @param taskControlList job 配置
     * @throws Exception
     */
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 允许运行的渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        if (orderChannelIdList != null && orderChannelIdList.size() > 0) {

            // 检测Feed文件是否在vms_bt_feed_file表中存在，如果不存在那么新建一条文件管理信息
            // 按渠道进行处理
            for (final String orderChannelID : orderChannelIdList) {
                checkFeedFileDBInfo(orderChannelID);
            }
        }
    }

    /**
     * 检测Feed文件是否在vms_bt_feed_file表中存在，如果不存在那么新建一条文件管理信息
     *
     * @param channelId 渠道
     */
    public void checkFeedFileDBInfo(String channelId) {

        // 如果在vms_bt_feed_file表有存在状态为1：等待导入的数据，那么不进行Scan
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", channelId);
        param.put("status", VmsConstants.FeedFileStatus.WAITING_IMPORT);
        List<VmsBtFeedFileModel> waitingImportFeedFileList = vmsBtFeedFileDao.selectList(param);
        if (waitingImportFeedFileList != null && waitingImportFeedFileList.size() > 0) {
            return;
        }

        // 取得Feed文件上传路径
        String feedFilePath = com.voyageone.common.configs.Properties.readValue("vms.feed.upload");

        // 这个渠道的Feed文件的根目录
        File root = new File(feedFilePath + "/" + channelId + "/");
        // 扫描根目录下面的所有文件（不包含子目录）
        File[] files = root.listFiles();
        // 如果存在文件，那么逐个处理
        if (files != null && files.length > 0) {
            for (File file : files) {
                // 只处理文件，跳过目录
                if (!file.isDirectory()) {
                    // 只处理扩展名为.csv的文件
                    String fileName = file.getName();
                    // 先转成小写再比较
                    fileName = fileName.toLowerCase();
                    if (fileName.lastIndexOf(".csv") > -1) {
                        if (".csv".equals(fileName.substring(fileName.length() - 4))) {
                            // 看看文件信息是否在vms_bt_feed_file表中存在
                            Map<String, Object> param1 = new HashMap<>();
                            param1.put("channelId", channelId);
                            param1.put("fileName", file.getPath());
                            List<VmsBtFeedFileModel> feedFileList = vmsBtFeedFileDao.selectList(param1);
                            // 不存在说明是客户通过FTP直接传的，需要新建文件信息
                            if (feedFileList == null || feedFileList.size() == 0) {
                                // 先更改下文件名为标准格式，Feed_[channel名称]_年月日_时分秒.csv
                                OrderChannelBean channel = Channels.getChannel(channelId);
                                File newFile = new File(feedFilePath + "/" + channelId + "/"
                                        + "Feed_" + channel.getFull_name() + DateTimeUtil.getNow("_yyyyMMdd_HHmmss") + ".csv");
                                boolean result = file.renameTo(newFile);
                                if (result) {
                                    // 更新状态为1：等待导入
                                    VmsBtFeedFileModel model = new VmsBtFeedFileModel();
                                    model.setChannelId(channelId);
                                    model.setFileName(newFile.getAbsolutePath());
                                    model.setStatus(VmsConstants.FeedFileStatus.WAITING_IMPORT);
                                    model.setCreater(getTaskName());
                                    model.setModifier(getTaskName());
                                    vmsBtFeedFileDao.insert(model);
                                    // 发MQ
                                    Map<String, Object> message = new HashMap<>();
                                    message.put("channelId", channelId);
                                    message.put("fileName", newFile.getAbsolutePath());
                                    sender.sendMessage("voyageone_mq_vms_feed_file_import", message);
                                    // 只处理一个文件
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}