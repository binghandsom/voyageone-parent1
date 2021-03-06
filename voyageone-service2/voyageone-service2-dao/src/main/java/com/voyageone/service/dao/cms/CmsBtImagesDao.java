/*
 * CmsBtImagesDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtImagesModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtImagesDao {
    List<CmsBtImagesModel> selectList(Object map);

    CmsBtImagesModel selectOne(Object map);

    int selectCount(Object map);

    CmsBtImagesModel select(Integer id);

    int insert(CmsBtImagesModel record);

    int update(CmsBtImagesModel record);

    int delete(Integer id);
}