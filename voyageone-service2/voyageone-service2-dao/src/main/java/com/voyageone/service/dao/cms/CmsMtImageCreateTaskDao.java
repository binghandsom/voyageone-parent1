package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtImageCreateTaskModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtImageCreateTaskDao {
    List<CmsMtImageCreateTaskModel> selectList(Map<String, Object> map);

    CmsMtImageCreateTaskModel selectOne(Map<String, Object> map);

    CmsMtImageCreateTaskModel select(long id);

    int insert(CmsMtImageCreateTaskModel entity);

    int update(CmsMtImageCreateTaskModel entity);

    int delete(long id);
}
