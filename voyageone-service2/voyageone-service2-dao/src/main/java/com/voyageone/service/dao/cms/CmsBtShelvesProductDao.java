/*
 * CmsBtShelvesProductDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtShelvesProductDao {
    List<CmsBtShelvesProductModel> selectList(Object map);

    CmsBtShelvesProductModel selectOne(Object map);

    int selectCount(Object map);

    CmsBtShelvesProductModel select(Integer id);

    int insert(CmsBtShelvesProductModel record);

    int update(CmsBtShelvesProductModel record);

    int delete(Integer id);
}