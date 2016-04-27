package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtPlatformSkusModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtPlatformSkusDao {
    List<CmsMtPlatformSkusModel> selectList(Map<String, Object> map);

    CmsMtPlatformSkusModel selectOne(Map<String, Object> map);

    CmsMtPlatformSkusModel select(long id);

    int insert(CmsMtPlatformSkusModel entity);

    int update(CmsMtPlatformSkusModel entity);

    int delete(long id);
}
