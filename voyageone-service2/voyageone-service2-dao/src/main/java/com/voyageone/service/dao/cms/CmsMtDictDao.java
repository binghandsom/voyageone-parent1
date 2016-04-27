package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtDictModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtDictDao {
    List<CmsMtDictModel> selectList(Map<String, Object> map);

    CmsMtDictModel selectOne(Map<String, Object> map);

    CmsMtDictModel select(long id);

    int insert(CmsMtDictModel entity);

    int update(CmsMtDictModel entity);

    int delete(long id);
}
