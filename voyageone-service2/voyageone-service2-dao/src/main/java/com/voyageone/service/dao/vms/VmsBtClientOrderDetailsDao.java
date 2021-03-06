/*
 * VmsBtClientOrderDetailsDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.vms;

import com.voyageone.service.model.vms.VmsBtClientOrderDetailsModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface VmsBtClientOrderDetailsDao {
    List<VmsBtClientOrderDetailsModel> selectList(Object map);

    VmsBtClientOrderDetailsModel selectOne(Object map);

    int selectCount(Object map);

    VmsBtClientOrderDetailsModel select(Integer id);

    int insert(VmsBtClientOrderDetailsModel record);

    int update(VmsBtClientOrderDetailsModel record);

    int delete(Integer id);
}