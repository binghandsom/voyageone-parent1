/*
 * CmsMtImageCreateImportMapper.java
 * Copyright(C) 20xx-2015 Voyageonone Group Inc.
 * All rights reserved.
 * -----------------------------------------------
 * 2016-05-04 Created
 */
package com.voyageone.service.dao.cms;


import com.voyageone.service.model.cms.CmsMtImageCreateImport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtImageCreateImportDao {
    List<CmsMtImageCreateImport> selectList(Map<String, Object> map);

    CmsMtImageCreateImport selectOne(Map<String, Object> map);

    CmsMtImageCreateImport select(Integer id);

    int insert(CmsMtImageCreateImport record);

    int update(CmsMtImageCreateImport record);

    int delete(Integer id);
}