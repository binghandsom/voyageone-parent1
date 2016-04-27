package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtSxWorkloadDao {
    List<CmsBtSxWorkloadModel> selectList(Map<String, Object> map);

    CmsBtSxWorkloadModel selectOne(Map<String, Object> map);

    CmsBtSxWorkloadModel select(long id);

    int insert(CmsBtSxWorkloadModel entity);

    int update(CmsBtSxWorkloadModel entity);

    int delete(long id);
}
