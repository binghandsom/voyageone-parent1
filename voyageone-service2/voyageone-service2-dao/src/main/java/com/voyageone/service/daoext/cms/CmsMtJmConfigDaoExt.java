package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsMtJmConfigModel;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtJmConfigDaoExt {
    public CmsMtJmConfigModel selectByKey(String channelId, String key);
}
