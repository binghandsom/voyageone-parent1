package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtTaskTejiabaoModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtTaskTejiabaoDao {
    List<CmsBtTaskTejiabaoModel> selectList(Map<String, Object> map);

    CmsBtTaskTejiabaoModel selectOne(Map<String, Object> map);

    CmsBtTaskTejiabaoModel select(long id);

    int insert(CmsBtTaskTejiabaoModel entity);

    int update(CmsBtTaskTejiabaoModel entity);

    int delete(long id);
}
