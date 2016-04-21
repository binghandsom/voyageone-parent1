package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtChannelCategoryModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtChannelCategoryDao {
    List<CmsBtChannelCategoryModel> selectList(Map<String, Object> map);

    CmsBtChannelCategoryModel selectOne(Map<String, Object> map);

    CmsBtChannelCategoryModel select(long id);

    int insert(CmsBtChannelCategoryModel entity);

    int update(CmsBtChannelCategoryModel entity);

    int delete(long id);
}
