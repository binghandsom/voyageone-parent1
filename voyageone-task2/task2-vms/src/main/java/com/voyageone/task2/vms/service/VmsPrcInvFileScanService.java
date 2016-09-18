package com.voyageone.task2.vms.service;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.base.exception.SystemException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MapUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.vms.VmsBtInventoryFileDao;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.model.vms.VmsBtInventoryFileModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.vms.VmsConstants;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.voyageone.task2.vms.VmsConstants.PrcInvFileStatus;
import static com.voyageone.task2.vms.VmsConstants.VMS_PROPERTY;

/**
 * vms价格和库存文件扫描service
 * Created by vantis on 16-9-8.
 */
@Service
public class VmsPrcInvFileScanService extends BaseTaskService {

    private VmsBtInventoryFileDao vmsBtInventoryFileDao;
    private MqSender mqSender;

    @Autowired
    public VmsPrcInvFileScanService(VmsBtInventoryFileDao vmsBtInventoryFileDao, MqSender mqsender) {
        this.vmsBtInventoryFileDao = vmsBtInventoryFileDao;
        this.mqSender = mqsender;
    }

    private static String PRICE_INVENTORY_DIRECTORY = "/inventory";

    private int skuColumnNumber;
    private int priceColumnNumber;
    private int msrpColumnNumber;
    private int inventoryColumnNumber;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.VMS;
    }

    @Override
    public String getTaskName() {
        return "VmsPrcInvFileScanJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 获取开启的渠道
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);
        if (null == orderChannelIdList || orderChannelIdList.size() == 0) {
            $info("没有启用的channel 本次执行跳过");
            return;
        }

        // 对开启的渠道进行文件扫描和导入操作

        orderChannelIdList.parallelStream().forEach(orderChannelId -> {
            $info(orderChannelId + ": 文件扫描处理开始");
            try {
                // 检查是否有可处理的内容
                VmsBtInventoryFileModel vmsBtInventoryFileModel = checkReadyFile(orderChannelId);
                if (vmsBtInventoryFileModel == null) return;

                // 无正在处理内容 开始进行文件检查
                VmsBtInventoryFileModel pendingFile = initializeFtpPendingFile(orderChannelId, vmsBtInventoryFileModel);

                if (pendingFile == null) return;

                // 发起更新请求MQ
                sendPriceAndInventoryUpdateMq(orderChannelId, pendingFile);

                // 更新DB状态
                updateFileStatus(pendingFile);
            } catch (Exception e) {
                $error(e.getMessage());
                return;
            }
            $info(orderChannelId + ": 文件扫描处理结束");
        });
    }

    private void updateFileStatus(VmsBtInventoryFileModel pendingFile) {
        pendingFile.setStatus(PrcInvFileStatus.IMPORTING);
        pendingFile.setModifier(this.getTaskName());
        pendingFile.setModified(new Date());
        vmsBtInventoryFileDao.update(pendingFile);
    }

    private void sendPriceAndInventoryUpdateMq(String orderChannelId, VmsBtInventoryFileModel pendingFile) {
        $info(orderChannelId + ": 文件 " + pendingFile.getFileName() + "处理中 发起MQ...");
        Map<String, Object> mqMessage = new HashMap<>();
        mqMessage.put("channelId", orderChannelId);
        mqMessage.put("fileName", pendingFile.getFileName());
        mqMessage.put("uploadType", pendingFile.getUploadType());
        mqSender.sendMessage("voyageone_mq_vms_prc_inv_file_import", mqMessage);
    }

    @Nullable
    private VmsBtInventoryFileModel initializeFtpPendingFile(
            String orderChannelId, VmsBtInventoryFileModel vmsBtInventoryFileModel) {
        this.checkWhetherTheFTPUploadedFileExistsThenRenameItAndInsertIntoDB(orderChannelId);

        vmsBtInventoryFileModel.setStatus(PrcInvFileStatus.WAITING_IMPORT);
        Map<String, Object> param;
        try {
            param = MapUtil.toMap(vmsBtInventoryFileModel);
        } catch (IllegalAccessException e) {
            BusinessException businessException = new BusinessException("参数转换异常");
            businessException.setStackTrace(e.getStackTrace());
            throw businessException;
        }
        param = MySqlPageHelper.build(param)
                .addSort("created", Order.Direction.ASC)
                .limit(1)
                .toMap();

        List<VmsBtInventoryFileModel> pendingFiles = vmsBtInventoryFileDao.selectList(param);
        if (null == pendingFiles || pendingFiles.size() == 0) {
            $info(orderChannelId + ": 没有需要处理的文件 本渠道执行结束");
            return null;
        }
        return pendingFiles.get(0);
    }

    @Nullable
    private VmsBtInventoryFileModel checkReadyFile(String orderChannelId) {
        VmsBtInventoryFileModel vmsBtInventoryFileModel = new VmsBtInventoryFileModel();
        vmsBtInventoryFileModel.setChannelId(orderChannelId);
        vmsBtInventoryFileModel.setStatus(PrcInvFileStatus.IMPORTING);
        List<VmsBtInventoryFileModel> importingList = vmsBtInventoryFileDao.selectList(vmsBtInventoryFileModel);
        // 存在正在处理的内容
        if (null != importingList && importingList.size() > 0) {
            List<String> importingIdList = importingList.stream()
                    .map(VmsBtInventoryFileModel::getId)
                    .map(String::valueOf)
                    .collect(Collectors.toList());
            $info(orderChannelId + ": 存在正在导入的价格/库存文件(编号: " + String.join(", ", importingIdList) + ")");
            return null;
        }
        $debug(orderChannelId + ": 当前没有正在导入的文件, 开始进行文件扫描");
        return vmsBtInventoryFileModel;
    }

    private boolean checkWhetherTheFTPUploadedFileExistsThenRenameItAndInsertIntoDB(String orderChannelId) {
        String prcInvFilePath = Codes.getCodeName(VMS_PROPERTY, "vms.inventory.ftp.upload");
        if (StringUtils.isEmpty(prcInvFilePath)) {
            throw new SystemException(orderChannelId + ": 未配置vms.inventory.ftp.upload, 请检查相关配置表");
        }

        if (!prcInvFilePath.endsWith("/")) prcInvFilePath = prcInvFilePath + "/";

        File ftpRoot = new File(prcInvFilePath + orderChannelId + PRICE_INVENTORY_DIRECTORY);
        if (!ftpRoot.exists() || !ftpRoot.isDirectory()) {
            throw new SystemException(orderChannelId + ": FTP上传目录 " + prcInvFilePath + orderChannelId +
                    PRICE_INVENTORY_DIRECTORY + " 不存在或者不是目录, 请检查服务器配置");
        }

        File[] files = ftpRoot.listFiles();
        if (null == files || files.length == 0) {
            $info(orderChannelId + ": 目录为空, 不存在FTP上传内容");
            return false;
        }

        for (File file : files) {
            if (file.isDirectory()) continue;
            if (!file.getName().toLowerCase().endsWith(".csv")) continue;

            String originFileName = file.getName();
            String finalFileName = "Inventory&Price_" + orderChannelId +
                    DateTimeUtil.getNow("_yyyyMMdd_HHmmss") + ".csv";

            /*
             * FIXME: 这里使用了renameTo方法进行重命名和移动 但是该命令在不同文件格式的磁盘/分区间移动会引起问题 vantis
             *
             * 相应应该封装一个copy和delete方法来达到真正移动文件的目的从而避免不同文件格式磁盘间移动的问题
             * 但由于目标目录和源目录理论上在同一个磁盘/分区 故此问题暂不需要做更多处理
             */
            if (!file.renameTo(new File(finalFileName))) {
                $info(orderChannelId + ": 文件 " + file.getName() + "正在被占用 跳过");
                continue;
            }

            createFtpFileDataInDB(orderChannelId, originFileName, finalFileName, prcInvFilePath);

            $info(orderChannelId + ": FTP上传文件 " + originFileName + " 已改名为" + finalFileName);
            return true;
        }

        $info(orderChannelId + ": 未发现对应的.csv文件");
        return false;
    }

    private void createFtpFileDataInDB(String orderChannelId, String originFileName, String finalFileName, String
            finalPath) {
        VmsBtInventoryFileModel vmsBtInventoryFileModel = new VmsBtInventoryFileModel();
        vmsBtInventoryFileModel.setChannelId(orderChannelId);
        vmsBtInventoryFileModel.setStatus(PrcInvFileStatus.WAITING_IMPORT);
        vmsBtInventoryFileModel.setClientFileName(originFileName);
        vmsBtInventoryFileModel.setFileName(finalFileName);
        vmsBtInventoryFileModel.setUploadType(VmsConstants.PrcInvUploadType.FTP);
        vmsBtInventoryFileModel.setCreater("ftp user");
        vmsBtInventoryFileModel.setModifier(this.getTaskName());
        vmsBtInventoryFileDao.insert(vmsBtInventoryFileModel);
    }
}
