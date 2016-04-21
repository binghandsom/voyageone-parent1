package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtChannelCategoryConfigModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtChannelCategoryConfigDao {
    List<CmsMtChannelCategoryConfigModel> selectList(Map<String, Object> map);

    CmsMtChannelCategoryConfigModel selectOne(Map<String, Object> map);

    CmsMtChannelCategoryConfigModel select(long id);

    int insert(CmsMtChannelCategoryConfigModel entity);

    int update(CmsMtChannelCategoryConfigModel entity);

    int delete(long id);
}
