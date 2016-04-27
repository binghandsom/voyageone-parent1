package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtPromotionSkusModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtPromotionSkusDao {
    List<CmsBtPromotionSkusModel> selectList(Map<String, Object> map);

    CmsBtPromotionSkusModel selectOne(Map<String, Object> map);

    CmsBtPromotionSkusModel select(long id);

    int insert(CmsBtPromotionSkusModel entity);

    int update(CmsBtPromotionSkusModel entity);

    int delete(long id);
}
