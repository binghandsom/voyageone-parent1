package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtPlatformSpecialFieldModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtPlatformSpecialFieldDao {
    List<CmsMtPlatformSpecialFieldModel> selectList(Map<String, Object> map);

    CmsMtPlatformSpecialFieldModel selectOne(Map<String, Object> map);

    CmsMtPlatformSpecialFieldModel select(long id);

    int insert(CmsMtPlatformSpecialFieldModel entity);

    int update(CmsMtPlatformSpecialFieldModel entity);

    int delete(long id);
}
