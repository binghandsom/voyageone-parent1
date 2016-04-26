package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtPromotionCodesModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtPromotionCodesDao {
    List<CmsBtPromotionCodesModel> selectList(Map<String, Object> map);

    CmsBtPromotionCodesModel selectOne(Map<String, Object> map);

    CmsBtPromotionCodesModel select(long id);

    int insert(CmsBtPromotionCodesModel entity);

    int update(CmsBtPromotionCodesModel entity);

    int delete(long id);
}
