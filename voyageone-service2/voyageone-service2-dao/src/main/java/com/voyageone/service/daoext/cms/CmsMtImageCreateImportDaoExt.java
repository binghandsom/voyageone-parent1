/*
 * CmsMtImageCreateImportMapper.java
 * Copyright(C) 20xx-2015 Voyageonone Group Inc.
 * All rights reserved.
 * -----------------------------------------------
 * 2016-05-04 Created
 */
package com.voyageone.service.daoext.cms;


import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtImageCreateImportDaoExt {
    public List getPageByWhere(Map<String, Object> map);

    public int getCountByWhere(Map<String, Object> ma);
}