package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtTagModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtTagDao {
    List<CmsBtTagModel> selectList(Map<String, Object> map);

    CmsBtTagModel selectOne(Map<String, Object> map);

    CmsBtTagModel select(long id);

    int insert(CmsBtTagModel entity);

    int update(CmsBtTagModel entity);

    int delete(long id);
}
