package com.voyageone.task2.vms.service;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.base.exception.SystemException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.com.MessageBean;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.daoext.vms.VmsBtInventoryFileDaoExt;
import com.voyageone.service.impl.cms.feed.FeedToCmsService;
import com.voyageone.service.impl.wms.ClientInventoryService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.vms.VmsBtInventoryFileModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.voyageone.task2.vms.VmsConstants.*;

/**
 * 价格和库存同步文件的处理Service
 * Created by vantis on 16-9-8.
 */
@Service
@RabbitListener(queues = "voyageone_mq_vms_prc_inv_file_import")
public class VmsPrcInvImportService extends BaseMQCmsService {

    private MessageService messageService;
    private VmsBtInventoryFileDaoExt vmsBtInventoryFileDaoExt;
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;
    private FeedToCmsService feedToCmsService;
    private ClientInventoryService clientInventoryService;

    @Autowired
    public VmsPrcInvImportService(MessageService messageService,
                                  VmsBtInventoryFileDaoExt vmsBtInventoryFileDaoExt,
                                  CmsBtFeedInfoDao cmsBtFeedInfoDao,
                                  FeedToCmsService feedToCmsService,
                                  ClientInventoryService clientInventoryService) {
        this.messageService = messageService;
        this.vmsBtInventoryFileDaoExt = vmsBtInventoryFileDaoExt;
        this.cmsBtFeedInfoDao = cmsBtFeedInfoDao;
        this.feedToCmsService = feedToCmsService;
        this.clientInventoryService = clientInventoryService;
    }

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {

        // 渠道
        String channelId = (String) messageMap.get("channelId");
        // 处理的csv文件名
        String fileName = (String) messageMap.get("fileName");
        // 文件上传类型
        String uploadType = (String) messageMap.get("uploadType");

        // 传入参数类的错误只能抛出去了
        $info("message received -> channelId: " + channelId + ", fileName: " + fileName + ", uploadType: " +
                uploadType);
        if (StringUtils.isEmpty(channelId) || null == fileName) {
            $error("参数channelId或者fileName为空");
            throw new BusinessException("参数channelId或者fileName为空: " + JsonUtil.bean2Json(messageMap));
        }

        try {
            // 获取文件
            FileInfo pendingFileInfo = this.constructFilePathAndCheckIt(channelId, fileName, uploadType);

            // 检查文件头并初始化错误信息
            PrcInvFileErrorMessage prcInvFileErrorMessage = this.checkCsvFileHeader(channelId, pendingFileInfo);

            // 导入
            this.commitFile(channelId, pendingFileInfo, prcInvFileErrorMessage);

            // 保存处理结果
            this.saveSuccessfulRecord(channelId, pendingFileInfo);

            prcInvFileErrorMessage.close();

        } catch (Exception e) {
            String status = PrcInvFileStatus.IMPORT_SYSTEM_ERROR;
            if (e instanceof BusinessException) {
                status = PrcInvFileStatus.IMPORT_ERROR;
            }
            $error("channelId: " + channelId + ", fileName: " + fileName + " -> " + e);
            // 把文件管理的状态变为 异常
            VmsBtInventoryFileModel vmsBtInventoryFileModel = new VmsBtInventoryFileModel();
            // 更新条件
            vmsBtInventoryFileModel.setChannelId(channelId);
            vmsBtInventoryFileModel.setFileName(fileName);
            // 更新内容
            if (!(e instanceof BusinessException)) vmsBtInventoryFileModel.setErrorMsg(e.getMessage());
            vmsBtInventoryFileModel.setStatus(status);
            vmsBtInventoryFileModel.setModifier(getTaskName());
            vmsBtInventoryFileDaoExt.updateStatus(vmsBtInventoryFileModel);
        }

        // 尝试移动文件
        this.tryToBackupFile(channelId, fileName, uploadType);

        $info("finished -> channelId: " + channelId + ", fileName: " + fileName + ", uploadType: " +
                uploadType);
    }

    private void tryToBackupFile(String channelId, String fileName, String uploadType) {
        String rootPath;
        if (PrcInvUploadType.FTP.equals(uploadType)) {
            rootPath = Codes.getCodeName(VMS_PROPERTY, "vms.inventory.ftp.upload");
            if (null == rootPath) {
                return;
            }
            if (!rootPath.endsWith("/")) rootPath = rootPath + "/";
            rootPath += channelId + "/inventory/";
        } else {
            rootPath = Codes.getCodeName(VMS_PROPERTY, "vms.inventory.online.upload");
            if (null == rootPath) {
                return;
            }
            if (!rootPath.endsWith("/")) rootPath = rootPath + "/";
            rootPath += channelId + "/";
        }

        // bak目录没有的话自动新建
        File backupFolder = new File(rootPath + "bak");
        if (!backupFolder.exists()) {
            backupFolder.mkdirs();
        }

        File file = new File(rootPath + fileName);
        if (file.exists() && !file.isDirectory())
            FileUtils.moveFile(rootPath + fileName, rootPath + "bak/" + fileName);
    }

    private void commitFile(String channelId, FileInfo pendingFileInfo, PrcInvFileErrorMessage prcInvFileErrorMessage)
            throws IOException {

        CsvReader csvReader = new CsvReader(
                new FileInputStream(pendingFileInfo.file), COMMA, Charset.forName(UTF_8));
        csvReader.readHeaders();
        int lineNumber = 1;

        // 分析结果
        while (csvReader.readRecord()) {
            Date date = new Date();
            boolean haveError = false;
            lineNumber++;
            // 检查该行内容

            // SKU
            String sku = csvReader.get(pendingFileInfo.skuColumnNumber);
            if (StringUtils.isEmpty(sku)) {
                prcInvFileErrorMessage.add(sku, pendingFileInfo.skuColumnNumber, lineNumber,
                        "Missing required column: %s", new Object[]{"sku"});
                haveError = true;
            }

            $debug(channelId + "->SKU: " + sku + ", SKU检查 " + (new Date().getTime() - date.getTime()) + "毫秒.");

            // Price
            String tempPrice = null;
            if (null != pendingFileInfo.priceColumnNumber) {
                tempPrice = csvReader.get(pendingFileInfo.priceColumnNumber);
                // 空值检查
                if (StringUtils.isEmpty(tempPrice)) {
                    prcInvFileErrorMessage.add(sku, pendingFileInfo.priceColumnNumber, lineNumber,
                            "Missing required column: %s", new Object[]{"price"});
                    haveError = true;
                }

                if (!StringUtils.isNumeric(tempPrice) || Float.valueOf(tempPrice) <= 0) {
                    prcInvFileErrorMessage.add(sku, pendingFileInfo.priceColumnNumber, lineNumber,
                            "%s must be a positive number", new Object[]{"price"});
                    haveError = true;
                }
            }
            final String price = tempPrice;

            $debug(channelId + "->SKU: " + sku + ", Price检查 " + (new Date().getTime() - date.getTime()) + "毫秒.");

            // MSRP
            String tempMsrp = null;
            if (null != pendingFileInfo.msrpColumnNumber
                    && null != csvReader.get(pendingFileInfo.msrpColumnNumber)) {
                tempMsrp = csvReader.get(pendingFileInfo.msrpColumnNumber);
                // 数值检查
                if (!StringUtils.isNumeric(tempMsrp) || Float.valueOf(tempMsrp) <= 0) {
                    prcInvFileErrorMessage.add(sku, pendingFileInfo.msrpColumnNumber, lineNumber,
                            "%s must be a positive number", new Object[]{"msrp"});
                    haveError = true;
                }
            }

            final String msrp = tempMsrp;

            $debug(channelId + "->SKU: " + sku + ", MSRP检查 " + (new Date().getTime() - date.getTime()) + "毫秒.");

            // Inventory
            String tempInventory = null;
            if (null != pendingFileInfo.inventoryColumnNumber) {
                tempInventory = csvReader.get(pendingFileInfo.inventoryColumnNumber);
                // 空值检查
                if (StringUtils.isEmpty(tempInventory)) {
                    prcInvFileErrorMessage.add(sku, pendingFileInfo.inventoryColumnNumber, lineNumber,
                            "Missing required column: %s", new Object[]{"quantity"});
                    haveError = true;
                }
                if (!StringUtils.isDigit(tempInventory)) {
                    prcInvFileErrorMessage.add(sku, pendingFileInfo.inventoryColumnNumber, lineNumber,
                            "%s must be a positive number", new Object[]{"quantity"});
                    haveError = true;
                }
            }

            final String inventory = tempInventory;

            if (StringUtils.isEmpty(sku)) continue;

            CmsBtFeedInfoModel cmsBtFeedInfoModel = cmsBtFeedInfoDao.selectProductByClientSku(channelId, sku);

            if (null == cmsBtFeedInfoModel) {
                prcInvFileErrorMessage.add(sku, pendingFileInfo.skuColumnNumber, lineNumber,
                        "SKU not found");
                haveError = true;
            }

            if (haveError) continue;

            cmsBtFeedInfoModel.getSkus().stream()
                    .filter(cmsBtFeedInfoModel_sku -> cmsBtFeedInfoModel_sku.getClientSku().equals(sku))
                    .forEach(cmsBtFeedInfoModel_sku -> {
                        if (null != price) {
                            cmsBtFeedInfoModel_sku.setPriceClientRetail(Double.valueOf(price));
                            cmsBtFeedInfoModel_sku.setPriceCurrent(Double.valueOf(price));
                            cmsBtFeedInfoModel_sku.setPriceNet(Double.valueOf(price));
                        }
                        if (null != msrp) {
                            cmsBtFeedInfoModel_sku.setPriceClientMsrp(Double.valueOf(msrp));
                            cmsBtFeedInfoModel_sku.setPriceMsrp(Double.valueOf(msrp));
                        }
                        if (null != inventory) cmsBtFeedInfoModel_sku.setQty(Integer.valueOf(inventory));
                    });


            // 更新feed表
            feedToCmsService.updateProduct(channelId, new ArrayList<CmsBtFeedInfoModel>() {{
                        add(cmsBtFeedInfoModel);
                    }},
                    getTaskName());


            // 推送库存
            if (null != inventory)
                clientInventoryService.insertClientInventory(channelId, sku, Integer.valueOf(inventory));

            $debug(channelId + "->SKU: " + sku + ", 时间共计" + (new Date().getTime() - date.getTime()) + "毫秒.");
            $debug(cmsBtFeedInfoModel.toString());
        }

        csvReader.close();

        // 导入完成后处理更新库存的标志位
        if (null == prcInvFileErrorMessage.csvWriter)
            clientInventoryService.updateClientInventorySynFlag(channelId);
        else
            // 有错误的情况下抛出
            throw new BusinessException("文件处理中发生部分错误");

    }

    private void saveSuccessfulRecord(String channelId, FileInfo pendingFileInfo) {

        // 把文件管理的状态变为 异常
        VmsBtInventoryFileModel vmsBtInventoryFileModel = new VmsBtInventoryFileModel();
        File pendingFile = pendingFileInfo.file;
        // 更新条件
        vmsBtInventoryFileModel.setChannelId(channelId);
        vmsBtInventoryFileModel.setFileName(pendingFile.getName());
        // 更新内容
        vmsBtInventoryFileModel.setStatus(PrcInvFileStatus.IMPORT_COMPLETED);
        vmsBtInventoryFileModel.setModifier(this.getTaskName());
        vmsBtInventoryFileDaoExt.updateStatus(vmsBtInventoryFileModel);

        $info("channelId: " + channelId + ", fileName: " + pendingFile.getName() + "-> 处理完毕");
    }

    private PrcInvFileErrorMessage checkCsvFileHeader(String channelId, FileInfo pendingFileInfo) throws IOException {
        CsvReader csvReader = new CsvReader(
                new FileInputStream(pendingFileInfo.file), COMMA, Charset.forName(UTF_8));

        // 错误信息
        PrcInvFileErrorMessage prcInvFileErrorMessage = new PrcInvFileErrorMessage(channelId,
                Codes.getCodeName(VMS_PROPERTY, "vms.inventory.check") + "/" + channelId,
                pendingFileInfo.file.getName());

        if (!csvReader.readHeaders()) {
            prcInvFileErrorMessage.add("header", 0, 0, "8000040");
            prcInvFileErrorMessage.close();
            throw new BusinessException(pendingFileInfo.file.getName() + " 未通过标题检查");
        }

        String[] headers = csvReader.getHeaders();

        if (null == headers || headers.length <= 0) {
            prcInvFileErrorMessage.add("header", 0, 0, "8000040");
            prcInvFileErrorMessage.close();
            throw new BusinessException(pendingFileInfo.file.getName() + " 未通过标题检查");
        }

        for (int i = 0; i < headers.length; i++) {
            switch (headers[i].toLowerCase()) {
                case "sku": {
                    pendingFileInfo.skuColumnNumber = i;
                    break;
                }
                case "price": {
                    pendingFileInfo.priceColumnNumber = i;
                    break;
                }
                case "msrp": {
                    pendingFileInfo.msrpColumnNumber = i;
                    break;
                }
                case "quantity": {
                    pendingFileInfo.inventoryColumnNumber = i;
                    break;
                }
                default:
                    break;
            }
        }

        // 缺少需要的列
        if (null == pendingFileInfo.skuColumnNumber ||
                (null == pendingFileInfo.priceColumnNumber
                        && null == pendingFileInfo.inventoryColumnNumber)) {
            prcInvFileErrorMessage.add("header", 0, 0, "8000040");
            prcInvFileErrorMessage.close();
            csvReader.close();
            throw new BusinessException(pendingFileInfo.file.getName() + " failed to pass the checking");
        }

        csvReader.close();

        return prcInvFileErrorMessage;
    }

    private FileInfo constructFilePathAndCheckIt(String channelId, String fileName, String uploadType) {

        FileInfo fileInfo = new FileInfo();

        String rootPath;
        if (PrcInvUploadType.FTP.equals(uploadType)) {
            rootPath = Codes.getCodeName(VMS_PROPERTY, "vms.inventory.ftp.upload");
            if (null == rootPath) {
                throw new SystemException("缺少上传路径配置: vms.inventory.ftp.upload 或者 vms.inventory.online.upload");
            }
            if (!rootPath.endsWith("/")) rootPath = rootPath + "/";
            rootPath += channelId + "/inventory/";
        } else {
            rootPath = Codes.getCodeName(VMS_PROPERTY, "vms.inventory.online.upload");
            if (null == rootPath) {
                throw new SystemException("缺少上传路径配置: vms.inventory.ftp.upload 或者 vms.inventory.online.upload");
            }
            if (!rootPath.endsWith("/")) rootPath = rootPath + "/";
            rootPath += channelId + "/";
        }

        File file = new File(rootPath + fileName);

        if (!file.exists())
            throw new SystemException("指定路径 " + rootPath + fileName + " 未找到");

        if (file.isDirectory())
            throw new SystemException("指定路径 " + rootPath + fileName + " 是一个目录");

        fileInfo.file = file;
        return fileInfo;
    }

    private class FileInfo {

        private File file;
        private Integer skuColumnNumber = null;
        private Integer priceColumnNumber = null;
        private Integer msrpColumnNumber = null;
        private Integer inventoryColumnNumber = null;
    }

    private class PrcInvFileErrorMessage {
        private String channelId;
        private String path;
        private String fileName;

        private CsvWriter csvWriter;

        private PrcInvFileErrorMessage(String channelId, String path, String fileName) {
            this.channelId = channelId;
            this.path = path;
            this.fileName = fileName;

        }

        private void add(String sku, int columnNumber, int lineNumber, String errorCode) {
            this.add(sku, columnNumber, lineNumber, errorCode, null);
        }

        private void add(String sku, int columnNumber, int lineNumber, String errorCode, Object[] args) {

            // 在第一次添加内容时初始化错误文件
            if (null == this.csvWriter) {
                File errorPathFile = new File(path);
                if (!errorPathFile.exists()) errorPathFile.mkdirs();
                String errorFileName = path + "/" +
                        fileName.replace("Inventory&Price_", "Inventory&Price_Check_Result_");
                this.csvWriter = new CsvWriter(errorFileName, COMMA, Charset.forName(UTF_8));
                try {
                    this.csvWriter.writeRecord(new String[]{"sku", "columnNumber", "rowNumber", "errorMessage"});
                } catch (IOException e) {
                    throw new SystemException("8000041");
                }

                VmsBtInventoryFileModel vmsBtInventoryFileModel = new VmsBtInventoryFileModel();
                vmsBtInventoryFileModel.setChannelId(channelId);
                vmsBtInventoryFileModel.setFileName(fileName);
                vmsBtInventoryFileModel.setErrorFileName(fileName.replace("Inventory&Price_", "Inventory&Price_Check_Result_"));
                vmsBtInventoryFileDaoExt.updateStatus(vmsBtInventoryFileModel);
            }

            String errorMessage = errorCode;
            MessageBean messageBean = messageService.getMessage("en", errorCode);
            if (messageBean != null) {
                errorMessage = messageBean.getMessage();
            }
            if (null != args)
                errorMessage = String.format(errorMessage, args);
            try {
                this.csvWriter.writeRecord(
                        new String[]{sku, String.valueOf(columnNumber + 1), String.valueOf(lineNumber), errorMessage});
                $debug(channelId + "-> lineNumber: " + lineNumber + ", error: " + errorMessage);
            } catch (IOException e) {
                throw new SystemException("8000041");
            }
            this.csvWriter.flush();
        }

        private void close() throws IOException {
            if (null == this.csvWriter) return;
            this.csvWriter.close();
        }
    }
}
