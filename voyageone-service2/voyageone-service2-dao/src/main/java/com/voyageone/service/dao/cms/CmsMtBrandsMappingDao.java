/*
 * CmsMtBrandsMappingDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtBrandsMappingModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtBrandsMappingDao {
    List<CmsMtBrandsMappingModel> selectList(Map<String, Object> map);

    CmsMtBrandsMappingModel selectOne(Map<String, Object> map);

    CmsMtBrandsMappingModel select(Integer seq);

    int insert(CmsMtBrandsMappingModel record);

    int update(CmsMtBrandsMappingModel record);

    int delete(Integer seq);
}