package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtImageCreateTaskDetailModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtImageCreateTaskDetailDao {
    public List<CmsMtImageCreateTaskDetailModel> selectList(Map<String, Object> map);

    public CmsMtImageCreateTaskDetailModel selectOne(Map<String, Object> map);

    public CmsMtImageCreateTaskDetailModel select(long id);

    public int insert(CmsMtImageCreateTaskDetailModel entity);

    public int update(CmsMtImageCreateTaskDetailModel entity);

    public int delete(long id);
}
