package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtPromotionModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtPromotionDao {
    List<CmsBtPromotionModel> selectList(Map<String, Object> map);

    CmsBtPromotionModel selectOne(Map<String, Object> map);

    CmsBtPromotionModel select(long id);

    int insert(CmsBtPromotionModel entity);

    int update(CmsBtPromotionModel entity);

    int delete(long id);
}
