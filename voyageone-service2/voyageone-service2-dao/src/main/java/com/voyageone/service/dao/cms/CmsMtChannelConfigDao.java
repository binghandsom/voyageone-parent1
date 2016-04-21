package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtChannelConfigModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtChannelConfigDao {
    List<CmsMtChannelConfigModel> selectList(Map<String, Object> map);

    CmsMtChannelConfigModel selectOne(Map<String, Object> map);

    CmsMtChannelConfigModel select(long id);

    int insert(CmsMtChannelConfigModel entity);

    int update(CmsMtChannelConfigModel entity);

    int delete(long id);
    }
