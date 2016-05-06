/*
 * CmsMtFeedCustomOptionDao.java
 * Copyright(C) 20xx-2015 Voyageonone Group Inc.
 * All rights reserved.
 * This class is generated by code generator, please don't modify it.
 * -----------------------------------------------
 * 2016-05-06 Created
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtFeedCustomOptionModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtFeedCustomOptionDao {
    List<CmsMtFeedCustomOptionModel> selectList(Map<String, Object> map);

    CmsMtFeedCustomOptionModel selectOne(Map<String, Object> map);

    CmsMtFeedCustomOptionModel select(Integer id);

    int insert(CmsMtFeedCustomOptionModel record);

    int update(CmsMtFeedCustomOptionModel record);

    int delete(Integer id);
}