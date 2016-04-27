package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtTaskJiagepiluModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtTaskJiagepiluDao {
    List<CmsBtTaskJiagepiluModel> selectList(Map<String, Object> map);

    CmsBtTaskJiagepiluModel selectOne(Map<String, Object> map);

    CmsBtTaskJiagepiluModel select(long id);

    int insert(CmsBtTaskJiagepiluModel entity);

    int update(CmsBtTaskJiagepiluModel entity);

    int delete(long id);
}
