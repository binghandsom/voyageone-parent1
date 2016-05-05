/*
 * CmsMtCommonPropDao.java
 * Copyright(C) 20xx-2015 Voyageonone Group Inc.
 * All rights reserved.
 * -----------------------------------------------
 * 2016-05-05 Created
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtCommonPropModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtCommonPropDao {
    List<CmsMtCommonPropModel> selectList(Map<String, Object> map);

    CmsMtCommonPropModel selectOne(Map<String, Object> map);

    CmsMtCommonPropModel select(Integer id);

    int insert(CmsMtCommonPropModel record);

    int update(CmsMtCommonPropModel record);

    int delete(Integer id);
}