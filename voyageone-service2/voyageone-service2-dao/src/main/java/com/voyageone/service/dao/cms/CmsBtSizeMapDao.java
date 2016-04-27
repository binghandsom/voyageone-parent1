package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtSizeMapModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtSizeMapDao {
    List<CmsBtSizeMapModel> selectList(Map<String, Object> map);

    CmsBtSizeMapModel selectOne(Map<String, Object> map);

    CmsBtSizeMapModel select(long id);

    int insert(CmsBtSizeMapModel entity);

    int update(CmsBtSizeMapModel entity);

    int delete(long id);
}
