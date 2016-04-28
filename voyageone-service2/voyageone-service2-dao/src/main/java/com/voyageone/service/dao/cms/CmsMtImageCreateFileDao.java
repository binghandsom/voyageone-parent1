package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtImageCreateFileDao {
    List<CmsMtImageCreateFileModel> selectList(Map<String, Object> map);

    CmsMtImageCreateFileModel selectOne(Map<String, Object> map);

    CmsMtImageCreateFileModel select(long id);

    int insert(CmsMtImageCreateFileModel entity);

    int update(CmsMtImageCreateFileModel entity);

    int delete(long id);
}
