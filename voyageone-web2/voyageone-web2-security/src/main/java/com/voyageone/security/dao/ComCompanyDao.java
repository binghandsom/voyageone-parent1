/*
 * ComCompanyDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.security.dao;

import com.voyageone.security.model.ComCompanyModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface ComCompanyDao {
    List<ComCompanyModel> selectList(Map<String, Object> map);

    ComCompanyModel selectOne(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    ComCompanyModel select(Integer id);

    int insert(ComCompanyModel record);

    int update(ComCompanyModel record);

    int delete(Integer id);
}