package com.voyageone.service.daoext.vms;

import com.voyageone.service.model.vms.VmsBtFeedFileModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface VmsBtFeedFileDaoExt {
    List<VmsBtFeedFileModel> selectListOrderByCreateTime(Map<String, Object> map);
}
