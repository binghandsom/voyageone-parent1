package com.voyageone.task2.vms.service;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.base.exception.SystemException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Codes;
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
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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

        FileInfo fileInfo = new FileInfo();
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
            $error("channelId: " + channelId + ", fileName: " + fileName + "-> " + e);
            // 把文件管理的状态变为 异常
            VmsBtInventoryFileModel vmsBtInventoryFileModel = new VmsBtInventoryFileModel();
            // 更新条件
            vmsBtInventoryFileModel.setChannelId(channelId);
            vmsBtInventoryFileModel.setFileName(fileName);
            // 更新内容
            vmsBtInventoryFileModel.setErrorMsg(e.getMessage());
            vmsBtInventoryFileModel.setStatus(status);
            vmsBtInventoryFileModel.setModifier(getTaskName());
            vmsBtInventoryFileDaoExt.updateStatus(vmsBtInventoryFileModel);
        }
    }

    private void commitFile(String channelId, FileInfo pendingFileInfo, PrcInvFileErrorMessage prcInvFileErrorMessage)
            throws IOException {

        CsvReader csvReader = new CsvReader(
                new FileInputStream(pendingFileInfo.getFile()), COMMA, Charset.forName(UTF_8));
        csvReader.readHeaders();
        int lineNumber = 1;

        // 分析结果
        while (csvReader.readRecord()) {
            lineNumber++;
            // 检查该行内容

            // SKU
            String sku = csvReader.get(pendingFileInfo.getSkuColumnNumber());
            if (StringUtils.isEmpty(sku)) {
                prcInvFileErrorMessage.add(sku, pendingFileInfo.getSkuColumnNumber(), lineNumber,
                        "Missing required column: %s", new Object[]{"SKU"});
                continue;
            }

            // Price
            String price = csvReader.get(pendingFileInfo.getPriceColumnNumber());
            if (null != pendingFileInfo.getPriceColumnNumber()) {

                // 空值检查
                if (StringUtils.isEmpty(price)) {
                    prcInvFileErrorMessage.add(sku, pendingFileInfo.getSkuColumnNumber(), lineNumber,
                            "Missing required column: %s", new Object[]{"Price"});
                    continue;
                }

                if (!StringUtils.isNumeric(price) || Float.valueOf(price) <= 0) {
                    prcInvFileErrorMessage.add(sku, pendingFileInfo.getSkuColumnNumber(), lineNumber,
                            "%s must be a positive number", new Object[]{"Price"});
                    continue;
                }
            }

            // MSRP
            String msrp = csvReader.get(pendingFileInfo.getMsrpColumnNumber());
            if (null != pendingFileInfo.getMsrpColumnNumber()
                    && null != csvReader.get(pendingFileInfo.getMsrpColumnNumber())) {

                // 数值检查
                if (!StringUtils.isNumeric(msrp) || Float.valueOf(msrp) <= 0) {
                    prcInvFileErrorMessage.add(sku, pendingFileInfo.getMsrpColumnNumber(), lineNumber,
                            "%s must be a positive number", new Object[]{"MSRP"});
                    continue;
                }
            }

            // Inventory
            String inventory = csvReader.get(pendingFileInfo.getInventoryColumnNumber());
            if (null != pendingFileInfo.getInventoryColumnNumber()) {

                // 空值检查
                if (StringUtils.isEmpty(inventory)) {
                    prcInvFileErrorMessage.add(sku, pendingFileInfo.getInventoryColumnNumber(), lineNumber,
                            "Missing required column: %s", new Object[]{"Price"});
                    continue;
                }
                if (!StringUtils.isDigit(inventory)) {
                    prcInvFileErrorMessage.add(sku, pendingFileInfo.getInventoryColumnNumber(), lineNumber,
                            "%s must be a positive number", new Object[]{"MSRP"});
                    continue;
                }
            }

            CmsBtFeedInfoModel cmsBtFeedInfoModel = cmsBtFeedInfoDao.selectProductByClientSku(channelId, sku);
            cmsBtFeedInfoModel.getSkus().stream()
                    .filter(cmsBtFeedInfoModel_sku -> cmsBtFeedInfoModel_sku.getClientSku().equals(sku))
                    .forEach(cmsBtFeedInfoModel_sku -> {
                        if (null != price) cmsBtFeedInfoModel_sku.setPriceClientRetail(Double.valueOf(price));
                        if (null != msrp) cmsBtFeedInfoModel_sku.setPriceClientMsrp(Double.valueOf(msrp));
                        if (null != inventory) cmsBtFeedInfoModel_sku.setQty(Integer.valueOf(inventory));
                    });

            // 更新feed表
            feedToCmsService.updateProduct(channelId, new ArrayList<CmsBtFeedInfoModel>() {{
                        add(cmsBtFeedInfoModel);
                    }},
                    getTaskName(), CmsConstants.FeedProductUpdateType.VMS_FEED);

            // 推送库存
            if (null != inventory)
                clientInventoryService.insertClientInventory(channelId, sku, Integer.valueOf(inventory));

            $debug(cmsBtFeedInfoModel.toString());
        }

        csvReader.close();
    }

    private void saveSuccessfulRecord(String channelId, FileInfo pendingFileInfo) {

        // 把文件管理的状态变为 异常
        VmsBtInventoryFileModel vmsBtInventoryFileModel = new VmsBtInventoryFileModel();
        File pendingFile = pendingFileInfo.getFile();
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
                new FileInputStream(pendingFileInfo.getFile()), COMMA, Charset.forName(UTF_8));

        // 错误信息
        PrcInvFileErrorMessage prcInvFileErrorMessage = new PrcInvFileErrorMessage(channelId,
                pendingFileInfo.getFile().getParent(), pendingFileInfo.getFile().getName());

        if (!csvReader.readHeaders()) {
            prcInvFileErrorMessage.add("header", 0, 0, "8000040");
            prcInvFileErrorMessage.close();
            throw new BusinessException(pendingFileInfo.getFile().getName() + " 未通过标题检查");
        }

        String[] headers = csvReader.getHeaders();

        if (null == headers || headers.length <= 0) {
            prcInvFileErrorMessage.add("header", 0, 0, "8000040");
            prcInvFileErrorMessage.close();
            throw new BusinessException(pendingFileInfo.getFile().getName() + " 未通过标题检查");
        }

        for (int i = 0; i < headers.length; i++) {
            switch (headers[i].toLowerCase()) {
                case "sku": {
                    pendingFileInfo.setSkuColumnNumber(i);
                    break;
                }
                case "price": {
                    pendingFileInfo.setPriceColumnNumber(i);
                    break;
                }
                case "msrp": {
                    pendingFileInfo.setMsrpColumnNumber(i);
                    break;
                }
                case "inventory": {
                    pendingFileInfo.setInventoryColumnNumber(i);
                    break;
                }
                default:
                    break;
            }
        }

        // 缺少需要的列
        if (null == pendingFileInfo.getSkuColumnNumber() ||
                (null == pendingFileInfo.getPriceColumnNumber()
                        && null == pendingFileInfo.getInventoryColumnNumber())) {
            prcInvFileErrorMessage.add("header", 0, 0, "8000040");
            prcInvFileErrorMessage.close();
            throw new BusinessException(pendingFileInfo.getFile().getName() + " failed to pass the checking");
        }

        csvReader.close();

        return prcInvFileErrorMessage;
    }

    private FileInfo constructFilePathAndCheckIt(String channelId, String fileName, String uploadType) {

        FileInfo fileInfo = new FileInfo();

        String rootPath;
        if (PrcInvUploadType.FTP.equals(uploadType)) {
            rootPath = Codes.getCodeName(VMS_PROPERTY, "vms.inventory.ftp.upload");
        } else {
            rootPath = Codes.getCodeName(VMS_PROPERTY, "vms.inventory.online.upload");
        }

        if (null == rootPath) {
            throw new SystemException("缺少上传路径配置: vms.inventory.ftp.upload 或者 vms.inventory.online.upload");
        }

        if (!rootPath.endsWith("/")) rootPath = rootPath + "/";

        File file = new File(rootPath + fileName);

        if (!file.exists())
            throw new SystemException("指定路径 " + rootPath + channelId + "/" + fileName + " 未找到");

        if (file.isDirectory())
            throw new SystemException("指定路径 " + rootPath + channelId + "/" + fileName + " 是一个目录");

        fileInfo.setFile(file);
        return fileInfo;
    }

    private class FileInfo {

        private File file;
        private Integer skuColumnNumber = null;
        private Integer priceColumnNumber = null;
        private Integer msrpColumnNumber = null;
        private Integer inventoryColumnNumber = null;

        private File getFile() {
            return file;
        }

        private void setFile(File file) {
            this.file = file;
        }

        private Integer getSkuColumnNumber() {
            return skuColumnNumber;
        }

        private void setSkuColumnNumber(Integer skuColumnNumber) {
            this.skuColumnNumber = skuColumnNumber;
        }

        private Integer getPriceColumnNumber() {
            return priceColumnNumber;
        }

        private void setPriceColumnNumber(Integer priceColumnNumber) {
            this.priceColumnNumber = priceColumnNumber;
        }

        private Integer getMsrpColumnNumber() {
            return msrpColumnNumber;
        }

        private void setMsrpColumnNumber(Integer msrpColumnNumber) {
            this.msrpColumnNumber = msrpColumnNumber;
        }

        private Integer getInventoryColumnNumber() {
            return inventoryColumnNumber;
        }

        private void setInventoryColumnNumber(Integer inventoryColumnNumber) {
            this.inventoryColumnNumber = inventoryColumnNumber;
        }
    }

    private class PrcInvFileErrorMessage {
        private String channelId;
        private String path;
        private String fileName;

        private CsvWriter csvWriter;

        List<String[]> errorMessageListBuffer = new Vector<>();

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
                this.csvWriter = new CsvWriter(path + "/errorMessage_" + fileName, COMMA, Charset.forName(UTF_8));
                try {
                    this.csvWriter.writeRecord(new String[]{"sku", "columnNumber", "lineNumber", "errorMessage"});
                    this.csvWriter.endRecord();
                } catch (IOException e) {
                    throw new SystemException("8000041");
                }
            }

            try {
                String errorMessage = errorCode;
                MessageBean messageBean = messageService.getMessage("en", errorCode);
                if (messageBean != null) {
                    errorMessage = messageBean.getMessage();
                }
                if (null != args)
                    errorMessage = String.format(errorMessage, args);

                errorMessageListBuffer.add(
                        new String[]{sku, String.valueOf(columnNumber), String.valueOf(lineNumber), errorMessage});

                this.csvWriter.endRecord();
                $debug(channelId + "-> lineNumber: " + lineNumber + ", error: " + errorMessage);
            } catch (IOException e) {
                throw new SystemException("8000041");
            }
            this.csvWriter.flush();
        }

        private void save() throws IOException {

            for (String[] errorMessage : errorMessageListBuffer) {
                this.csvWriter.writeRecord(errorMessage);
                this.csvWriter.endRecord();
            }
            this.csvWriter.flush();

        }

        private void close() throws IOException {
            this.save();
            this.csvWriter.close();
            VmsBtInventoryFileModel vmsBtInventoryFileModel = new VmsBtInventoryFileModel();
            vmsBtInventoryFileModel.setChannelId(channelId);
            vmsBtInventoryFileModel.setFileName(fileName);
            vmsBtInventoryFileModel.setErrorFileName("errorMessage_" + fileName);
            vmsBtInventoryFileDaoExt.updateStatus(vmsBtInventoryFileModel);
        }
    }

    private class SkuInfo {
        @NotNull
        private String sku;
        private Integer lineNumber;
        private Double price = null;
        private Double msrp = null;
        private Integer inventory = null;

        public SkuInfo(@NotNull String sku, Double price, Double msrp, Integer inventory, Integer lineNumber) {
            this.sku = sku;
            this.price = price;
            this.msrp = msrp;
            this.inventory = inventory;
            this.lineNumber = lineNumber;
        }
    }
}
