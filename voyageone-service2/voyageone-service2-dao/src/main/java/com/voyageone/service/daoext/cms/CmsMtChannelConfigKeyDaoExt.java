package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsMtChannelConfigKeyModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/12/13.
 */
@Repository
public interface CmsMtChannelConfigKeyDaoExt {
    List<CmsMtChannelConfigKeyModel> selectList(Map<String, Object> map);
}
