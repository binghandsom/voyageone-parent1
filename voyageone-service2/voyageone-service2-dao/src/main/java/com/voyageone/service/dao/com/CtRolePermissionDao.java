/*
 * CtRolePermissionDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.com;

import com.voyageone.service.model.com.CtRolePermissionModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CtRolePermissionDao {
    List<CtRolePermissionModel> selectList(Object map);

    CtRolePermissionModel selectOne(Object map);

    int selectCount(Object map);

    CtRolePermissionModel select(Integer id);

    int insert(CtRolePermissionModel record);

    int update(CtRolePermissionModel record);

    int delete(Integer id);
}