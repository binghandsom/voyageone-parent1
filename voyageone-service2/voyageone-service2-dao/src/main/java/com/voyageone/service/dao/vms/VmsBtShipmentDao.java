/*
 * VmsBtShipmentDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.vms;

import com.voyageone.service.model.vms.VmsBtShipmentModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface VmsBtShipmentDao {
    List<VmsBtShipmentModel> selectList(Object map);

    VmsBtShipmentModel selectOne(Object map);

    int selectCount(Object map);

    VmsBtShipmentModel select(Integer id);

    int insert(VmsBtShipmentModel record);

    int update(VmsBtShipmentModel record);

    int delete(Integer id);
}