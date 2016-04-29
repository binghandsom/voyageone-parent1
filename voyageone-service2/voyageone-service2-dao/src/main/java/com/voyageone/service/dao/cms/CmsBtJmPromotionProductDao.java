package com.voyageone.service.dao.cms;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;

@Repository
public interface CmsBtJmPromotionProductDao {
     public List<CmsBtJmPromotionProductModel> selectList(Map<String, Object> map);
    public CmsBtJmPromotionProductModel selectOne(Map<String, Object> map);
    public CmsBtJmPromotionProductModel select(long id);
    public int insert(CmsBtJmPromotionProductModel entity);
    public int update(CmsBtJmPromotionProductModel entity);
    public int delete(long id);
    }
