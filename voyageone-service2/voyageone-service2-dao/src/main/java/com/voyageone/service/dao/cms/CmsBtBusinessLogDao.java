package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtBusinessLogModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtBusinessLogDao {
    List<CmsBtBusinessLogModel> selectList(Map<String, Object> map);

    CmsBtBusinessLogModel selectOne(Map<String, Object> map);

    CmsBtBusinessLogModel select(long id);

    int insert(CmsBtBusinessLogModel entity);

    int update(CmsBtBusinessLogModel entity);

    int delete(long id);
}
