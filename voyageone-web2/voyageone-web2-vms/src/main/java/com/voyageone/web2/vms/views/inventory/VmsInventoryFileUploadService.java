package com.voyageone.web2.vms.views.inventory;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.impl.vms.feed.FeedFileService;
import com.voyageone.service.impl.vms.inventory.InventoryFileService;
import com.voyageone.service.model.vms.VmsBtFeedFileModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.vms.VmsConstants;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * VmsInventoryFileUploadService
 * Created on 2016/5/5.
 * @author jeff.duan
 * @version 1.0
 */
@Service
public class VmsInventoryFileUploadService extends BaseAppService {

    private final static int FILE_LIMIT_SIZE = 52428800;

    private final static String CSV_TYPE = "csv";

    @Autowired
    private InventoryFileService inventoryFileService;

    /**
     * 保存上传的InventoryFile
     *
     * @param channelId 渠道
     * @param userName 用户名
     * @param file  上传的文件
     */
    public void saveInventoryFile(String channelId, String userName, MultipartFile file) {

        // check
        doSaveInventoryFileCheck(channelId, file);

        // 上传文件流
        try (InputStream inputStream = file.getInputStream();) {

            String fileName = "Inventory&Price_" + channelId + DateTimeUtil.getNow("_yyyyMMdd_HHmmss") + ".csv";

            // 保存文件
            // 取得Inventory文件上传路径
            String inventoryFilePath = Codes.getCodeName(VmsConstants.VMS_PROPERTY, "vms.inventory.online.upload");
            inventoryFilePath +=  "/" + channelId + "/";
            FileUtils.copyInputStreamToFile(inputStream, new File(inventoryFilePath  + fileName));

            // 往vms_bt_inventory_file表插入数据
            inventoryFileService.insertInventoryFileInfo(channelId, file.getOriginalFilename(), fileName,
                    VmsConstants.InventoryFileUploadType.ONLINE, VmsConstants.InventoryFileStatus.WAITING_IMPORT, userName);

        } catch (Exception ex) {
            $error(ex);
            // Failed to upload file.
            throw new BusinessException("8000016");
        }
    }

    /**
     * checkInventoryFile
     *
     * @param channelId 渠道
     * @param uploadFile 上传的InventoryFile文件
     */
    private void doSaveInventoryFileCheck(String channelId, MultipartFile uploadFile) {

        // 取得Inventory文件Ftp的上传路径
        String inventoryFilePath = Codes.getCodeName(VmsConstants.VMS_PROPERTY, "vms.inventory.ftp.upload");
        inventoryFilePath +=  "/" + channelId + "/inventory/";

        // 目录下有文件存在的话不允许上传（FTP有上传的情况下）
        File root = new File(inventoryFilePath);
        // 扫描根目录下面的所有文件（不包含子目录）
        File[] files = root.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                // 只处理文件，跳过目录
                if (!file.isDirectory()) {
                    String fileName = file.getName().toLowerCase();
                    if (fileName.lastIndexOf(".csv") > -1) {
                        if (".csv".equals(fileName.substring(fileName.length() - 4))) {
                            // Have inventory file is processing, please upload later.
                            throw new BusinessException("8000013", new Object[]{"inventory&price"});
                        }
                    }
                }
            }
        }

        // 文件大小判断
        if (uploadFile.getSize() >= FILE_LIMIT_SIZE) {
            // The size of inventory file exceeds the limit.
            throw new BusinessException("8000014", new Object[]{"inventory&price"});
        }

        // 文件名
        String fileName = uploadFile.getOriginalFilename();

        // 获取文件后缀
        String suffix = null;
        if (fileName.lastIndexOf(".") > -1) {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        // 判断后缀是否合法（csv）
        if (suffix == null || !CSV_TYPE.toLowerCase().contains(suffix.toLowerCase())) {
            // Please upload a inventory file with csv format.
            throw new BusinessException("8000015", new Object[]{"inventory&price"});
        }
    }
}