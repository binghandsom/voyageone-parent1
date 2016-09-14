package com.voyageone.service.daoext.vms;

import com.voyageone.service.model.vms.VmsBtInventoryFileModel;
import org.springframework.stereotype.Repository;

/**
 * 上传inventory记录表dao
 * Created by vantis on 16-9-12.
 */
@Repository
public interface VmsBtInventoryFileDaoExt {
    int updateStatus(VmsBtInventoryFileModel vmsBtInventoryFileModel);
}
