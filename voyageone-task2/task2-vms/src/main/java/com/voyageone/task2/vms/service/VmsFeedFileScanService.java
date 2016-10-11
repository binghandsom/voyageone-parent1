package com.voyageone.task2.vms.service;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.vms.VmsBtFeedFileDao;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.model.vms.VmsBtFeedFileModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.vms.VmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
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
            for (String orderChannelID : orderChannelIdList) {

                // 在vms_bt_feed_file表有状态为2：正在导入的数据，那么略过
                Map<String, Object> param = new HashMap<>();
                param.put("channelId", orderChannelID);
                param.put("status", VmsConstants.FeedFileStatus.IMPORTING);
                List<VmsBtFeedFileModel> importingFeedFileList = vmsBtFeedFileDao.selectList(param);
                if (importingFeedFileList.size() > 0) {
                    $info("存在正在导入的Feed文件,略过channel：" + orderChannelID);
                    continue;
                }

                // 检测Feed文件,并且在vms_bt_feed_file表中新建一条文件管理信息
                checkFeedFileDBInfo(orderChannelID);

                // 取出在vms_bt_feed_file表有状态为1：等待导入的一条创建时间最早的Feed文件信息，发MQ进行导入
                Map<String, Object> param1 = new HashMap<>();
                param1.put("channelId", orderChannelID);
                param1.put("status", VmsConstants.FeedFileStatus.WAITING_IMPORT);
                // Order 条件
                String sortString = "created asc";
                Map<String, Object> newMap = MySqlPageHelper.build(param1).sort(sortString).toMap();
                List<VmsBtFeedFileModel> waitingImportingFeedFileList = vmsBtFeedFileDao.selectList(newMap);
                if (waitingImportingFeedFileList.size() > 0) {
                    $info("存在准备导入的Feed文件,发MQ,channel：" + orderChannelID + ",文件名"
                            + waitingImportingFeedFileList.get(0).getClientFileName() + ",新文件名"
                            + waitingImportingFeedFileList.get(0).getFileName());
                    // 发MQ
                    Map<String, Object> message = new HashMap<>();
                    message.put("channelId", orderChannelID);
                    message.put("fileName", waitingImportingFeedFileList.get(0).getFileName());
                    message.put("uploadType", waitingImportingFeedFileList.get(0).getUploadType());
                    sender.sendMessage("voyageone_mq_vms_feed_file_import", message);

                    // 把文件管理的状态变为2：导入中
                    VmsBtFeedFileModel feedFileModel = waitingImportingFeedFileList.get(0);
                    feedFileModel.setStatus(VmsConstants.FeedFileStatus.IMPORTING);
                    feedFileModel.setModifier(getTaskName());
                    vmsBtFeedFileDao.update(feedFileModel);
                }
            }
        }
    }

    /**
     * 检测Feed文件,并且在vms_bt_feed_file表中新建一条文件管理信息
     *
     * @param channelId 渠道
     */
    public void checkFeedFileDBInfo(String channelId) {

        // 如果在vms_bt_feed_file表有存在状态为1：等待导入和2：导入中 的数据，那么不进行Scan
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", channelId);
        param.put("status", VmsConstants.FeedFileStatus.WAITING_IMPORT);
        List<VmsBtFeedFileModel> waitingImportFeedFileList = vmsBtFeedFileDao.selectList(param);
        if (waitingImportFeedFileList != null && waitingImportFeedFileList.size() > 0) {
            $info("存在正在准备导入的Feed文件,ScanFeed文件略过channel：" + channelId);
            return;
        }

        // 取得FTP测Feed文件上传路径
        String feedFilePath = Codes.getCodeName(VmsConstants.VMS_PROPERTY, "vms.feed.ftp.upload");
        feedFilePath += "/" + channelId + "/feed/";

        // 这个渠道的Feed文件的根目录
        File root = new File(feedFilePath);
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
                            // 存在csv文件，肯定是用户ftp上传的，加入文件管理表
                            // 先更改下文件名为标准格式，Feed_[channelId]_年月日_时分秒.csv
                            File newFile = new File(feedFilePath + "Feed_" + channelId + DateTimeUtil.getNow("_yyyyMMdd_HHmmss") + ".csv");
                            // 进行改名，改名失败的情况说明这个文件正被占用，所以略过处理
                            boolean result = file.renameTo(newFile);
                            if (result) {
                                $info("Scan到Feed文件,channel：" + channelId + ",文件名" + file.getName() + ",新文件名" + newFile.getName());
                                // 更新状态为1：等待导入
                                VmsBtFeedFileModel model = new VmsBtFeedFileModel();
                                model.setChannelId(channelId);
                                model.setClientFileName(file.getName());
                                model.setFileName(newFile.getName());
                                model.setUploadType(VmsConstants.FeedFileUploadType.FTP);
                                model.setStatus(VmsConstants.FeedFileStatus.WAITING_IMPORT);
                                model.setCreater("ftp user");
                                model.setModifier(getTaskName());
                                vmsBtFeedFileDao.insert(model);
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