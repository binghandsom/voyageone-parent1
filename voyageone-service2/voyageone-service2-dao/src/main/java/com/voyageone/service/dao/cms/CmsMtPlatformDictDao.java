package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtPlatFormDictModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtPlatformDictDao {
    List<CmsMtPlatFormDictModel> selectList(Map<String, Object> map);

    CmsMtPlatFormDictModel selectOne(Map<String, Object> map);

    CmsMtPlatFormDictModel select(long id);

    int insert(CmsMtPlatFormDictModel entity);

    int update(CmsMtPlatFormDictModel entity);

    int delete(long id);
}
