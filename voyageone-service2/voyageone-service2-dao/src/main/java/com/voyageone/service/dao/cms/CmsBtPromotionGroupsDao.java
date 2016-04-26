package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtPromotionGroupsModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtPromotionGroupsDao {
    List<CmsBtPromotionGroupsModel> selectList(Map<String, Object> map);

    CmsBtPromotionGroupsModel selectOne(Map<String, Object> map);

    CmsBtPromotionGroupsModel select(long id);

    int insert(CmsBtPromotionGroupsModel entity);

    int update(CmsBtPromotionGroupsModel entity);

    int delete(long id);
}
