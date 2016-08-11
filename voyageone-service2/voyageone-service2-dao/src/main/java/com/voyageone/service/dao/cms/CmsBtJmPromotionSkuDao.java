/*
 * CmsBtJmPromotionSkuDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtJmPromotionSkuDao {
    List<CmsBtJmPromotionSkuModel> selectList(Object map);

    CmsBtJmPromotionSkuModel selectOne(Object map);

    int selectCount(Object map);

    CmsBtJmPromotionSkuModel select(Integer id);

    int insert(CmsBtJmPromotionSkuModel record);

    int update(CmsBtJmPromotionSkuModel record);

    int delete(Integer id);
}