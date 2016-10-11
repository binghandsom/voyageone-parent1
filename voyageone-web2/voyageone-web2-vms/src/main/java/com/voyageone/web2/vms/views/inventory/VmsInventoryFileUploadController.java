package com.voyageone.web2.vms.views.inventory;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Codes;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.vms.VmsConstants;
import com.voyageone.web2.vms.VmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * VmsInventoryFileUploadController
 * Created on 16/07/11.
 * @author jeff.duan
 * @version 1.0
 */
@RestController
@RequestMapping(
        value = VmsUrlConstants.INVENTORY.INVENTORY_FILE_UPLOAD.ROOT,
        method = RequestMethod.POST
)
public class VmsInventoryFileUploadController extends BaseController {

    @Autowired
    private VmsInventoryFileUploadService vmsInventoryFileUploadService;


    /**
     *  下载Inventory文件模板
     *
     * @return Inventory文件模板
     */
    @RequestMapping(VmsUrlConstants.INVENTORY.INVENTORY_FILE_UPLOAD.DOWNLOAD_SAMPLE_INVENTORY_FILE)
    public ResponseEntity downSampleInventoryFile() throws IOException {
        // Feed文件模板的路径
        String sampleFilePath = Codes.getCodeName(VmsConstants.VMS_PROPERTY, "vms.inventory.sample.file");

        try(FileInputStream file = new FileInputStream(sampleFilePath)) {
            return genResponseEntityFromStream("inventory&price_file_sample.csv", file);
        } catch (FileNotFoundException ex) {
            // Lost VoyageOneInventoryTemplate.
            throw new BusinessException("8000031");
        }
    }


    /**
     *  保存Inventory文件
     *
     * @param param 客户端参数
     * @param file 导入文件
     * @return 结果
     */
    @RequestMapping(VmsUrlConstants.INVENTORY.INVENTORY_FILE_UPLOAD.UPLOAD_INVENTORY_FILE)
    public AjaxResponse uploadInventoryFile(@RequestParam Map<String, Object> param, @RequestParam MultipartFile file) {
        vmsInventoryFileUploadService.saveInventoryFile(getUser().getSelChannelId(), getUser().getUserName(), file);
        return success(null);
    }
}
