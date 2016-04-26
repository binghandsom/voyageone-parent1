package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtPlatformConfigModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtPlatformConfigDao {
    List<CmsMtPlatformConfigModel> selectList(Map<String, Object> map);

    CmsMtPlatformConfigModel selectOne(Map<String, Object> map);

    CmsMtPlatformConfigModel select(long id);

    int insert(CmsMtPlatformConfigModel entity);

    int update(CmsMtPlatformConfigModel entity);

    int delete(long id);
}
