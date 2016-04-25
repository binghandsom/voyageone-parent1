package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtCustomWordModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtCustomWordDao {
    List<CmsMtCustomWordModel> selectList(Map<String, Object> map);

    CmsMtCustomWordModel selectOne(Map<String, Object> map);

    CmsMtCustomWordModel select(long id);

    int insert(CmsMtCustomWordModel entity);

    int update(CmsMtCustomWordModel entity);

    int delete(long id);
}
