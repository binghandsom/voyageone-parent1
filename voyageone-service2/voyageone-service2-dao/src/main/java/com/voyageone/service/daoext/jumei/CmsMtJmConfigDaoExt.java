package com.voyageone.service.daoext.jumei;

import com.voyageone.service.model.jumei.CmsMtJmConfigModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtJmConfigDaoExt {
    public CmsMtJmConfigModel getByKey(String channelId,String key);
    }
