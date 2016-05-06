package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtPlatformDictModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtPlatformDictDao {
    List<CmsMtPlatformDictModel> selectList(Map<String, Object> map);

    CmsMtPlatformDictModel selectOne(Map<String, Object> map);

    CmsMtPlatformDictModel select(long id);

    int insert(CmsMtPlatformDictModel entity);

    int update(CmsMtPlatformDictModel entity);

    int delete(long id);
}
