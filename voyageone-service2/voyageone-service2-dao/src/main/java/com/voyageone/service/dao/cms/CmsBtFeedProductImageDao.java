package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtFeedProductImageModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtFeedProductImageDao {
    List<CmsBtFeedProductImageModel> selectList(Map<String, Object> map);

    CmsBtFeedProductImageModel selectOne(Map<String, Object> map);

    CmsBtFeedProductImageModel select(long id);

    int insert(CmsBtFeedProductImageModel entity);

    int update(CmsBtFeedProductImageModel entity);

    int delete(long id);
}
