package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtTasksModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtTasksDao {
    List<CmsBtTasksModel> selectList(Map<String, Object> map);

    CmsBtTasksModel selectOne(Map<String, Object> map);

    CmsBtTasksModel select(long id);

    int insert(CmsBtTasksModel entity);

    int update(CmsBtTasksModel entity);

    int delete(long id);
}
