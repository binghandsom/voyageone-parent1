/*
 * CmsMtPlatformSkusDao.java
 * Copyright(C) 20xx-2015 Voyageonone Group Inc.
 * All rights reserved.
 * This class is generated by code generator, please don't modify it.
 * -----------------------------------------------
 * 2016-05-06 Created
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtPlatformSkusModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtPlatformSkusDao {
    List<CmsMtPlatformSkusModel> selectList(Map<String, Object> map);

    CmsMtPlatformSkusModel selectOne(Map<String, Object> map);

    CmsMtPlatformSkusModel select(Integer id);

    int insert(CmsMtPlatformSkusModel record);

    int update(CmsMtPlatformSkusModel record);

    int delete(Integer id);
}