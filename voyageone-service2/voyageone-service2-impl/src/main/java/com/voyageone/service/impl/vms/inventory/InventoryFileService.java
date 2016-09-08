package com.voyageone.service.impl.vms.inventory;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Codes;
import com.voyageone.service.dao.vms.VmsBtInventoryFileDao;
import com.voyageone.service.daoext.vms.VmsBtFeedFileDaoExt;
import com.voyageone.service.model.vms.VmsBtFeedFileModel;
import com.voyageone.service.model.vms.VmsBtInventoryFileModel;
import org.apache.commons.io.FileUtils;
import com.voyageone.service.dao.vms.VmsBtFeedFileDao;
import com.voyageone.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InventoryFileService
 *
 * @author jeff.duan 16/9/8
 * @version 1.0.0
 */
@Service
public class InventoryFileService extends BaseService {

    @Autowired
    private VmsBtInventoryFileDao vmsBtInventoryFileDao;

    /**
     * 新建一条文件信息到vms_bt_inventory_file表
     *
     * @param channelId   渠道
     * @param fileName    文件名
     * @param newFileName 新文件名
     * @param uploadType 上传状态
     * @param status      状态
     * @param modifier    创建者
     * @return id
     */
    public Integer insertInventoryFileInfo(String channelId, String fileName, String newFileName, String uploadType, String status, String modifier) {
        VmsBtInventoryFileModel model = new VmsBtInventoryFileModel();
        model.setChannelId(channelId);
        model.setClientFileName(fileName);
        model.setFileName(newFileName);
        model.setUploadType(uploadType);
        model.setStatus(status);
        model.setCreater(modifier);
        model.setModifier(modifier);
        vmsBtInventoryFileDao.insert(model);
        return model.getId();

    }
}
