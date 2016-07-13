package com.voyageone.service.daoext.vms;

import com.voyageone.service.model.vms.VmsBtFeedFileModel;
import com.voyageone.service.model.vms.VmsBtFeedInfoTempModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface VmsBtFeedFileDaoExt {

    int updateErrorInfo(VmsBtFeedFileModel record);

    List<Map<String, Object>> selectList(Map<String, Object> map);

    long selectListCount(Map<String, Object> map);

}
