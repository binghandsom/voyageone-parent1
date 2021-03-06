/*
 * CmsMtPlatformCategoryExtendInfoDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtPlatformCategoryExtendInfoModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtPlatformCategoryExtendInfoDao {
    List<CmsMtPlatformCategoryExtendInfoModel> selectList(Object map);

    CmsMtPlatformCategoryExtendInfoModel selectOne(Object map);

    int selectCount(Object map);

    CmsMtPlatformCategoryExtendInfoModel select(Integer id);

    int insert(CmsMtPlatformCategoryExtendInfoModel record);

    int update(CmsMtPlatformCategoryExtendInfoModel record);

    int delete(Integer id);
}