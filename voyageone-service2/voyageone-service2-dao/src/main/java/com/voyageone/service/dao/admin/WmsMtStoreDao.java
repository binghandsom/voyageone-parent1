/*
 * WmsMtStoreDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.admin;

import com.voyageone.service.model.admin.WmsMtStoreModel;
import org.springframework.stereotype.Repository;

@Repository
public interface WmsMtStoreDao {
    int insert(WmsMtStoreModel record);
}