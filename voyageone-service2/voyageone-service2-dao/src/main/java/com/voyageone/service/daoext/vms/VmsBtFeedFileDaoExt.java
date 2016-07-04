package com.voyageone.service.daoext.vms;

import com.voyageone.service.model.vms.VmsBtFeedFileModel;
import com.voyageone.service.model.vms.VmsBtFeedInfoTempModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface VmsBtFeedFileDaoExt {
    List<VmsBtFeedFileModel> selectListOrderByCreateTime(Map<String, Object> map);

    int updateErrorFileInfo(VmsBtFeedFileModel record);

}
