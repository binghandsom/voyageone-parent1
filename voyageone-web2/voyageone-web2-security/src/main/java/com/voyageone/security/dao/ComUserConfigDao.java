/*
 * ComUserConfigDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.security.dao;

import com.voyageone.security.model.ComUserConfigModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ComUserConfigDao {
    List<ComUserConfigModel> selectList(Object map);

    ComUserConfigModel selectOne(Object map);

    int selectCount(Object map);

    ComUserConfigModel select(Integer id);

    int insert(ComUserConfigModel record);

    int update(ComUserConfigModel record);

    int delete(Integer id);
}