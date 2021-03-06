/*
 * CmsMtFeedCustomOptionDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtFeedCustomOptionModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtFeedCustomOptionDao {
    List<CmsMtFeedCustomOptionModel> selectList(Object map);

    CmsMtFeedCustomOptionModel selectOne(Object map);

    int selectCount(Object map);

    CmsMtFeedCustomOptionModel select(Integer id);

    int insert(CmsMtFeedCustomOptionModel record);

    int update(CmsMtFeedCustomOptionModel record);

    int delete(Integer id);
}