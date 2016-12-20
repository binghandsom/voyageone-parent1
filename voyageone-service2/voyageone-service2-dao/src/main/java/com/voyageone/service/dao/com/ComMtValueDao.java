/*
 * ComMtValueDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.com;

import com.voyageone.service.model.com.ComMtValueModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface ComMtValueDao {
    List<ComMtValueModel> selectList(Map<String, Object> map);

    ComMtValueModel selectOne(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    ComMtValueModel select(Integer id);

    int insert(ComMtValueModel record);

    int update(ComMtValueModel record);

    int delete(Integer id);
}