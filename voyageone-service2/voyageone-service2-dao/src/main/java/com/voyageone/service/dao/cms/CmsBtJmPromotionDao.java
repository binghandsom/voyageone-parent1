package com.voyageone.service.dao.cms;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;

@Repository
public interface CmsBtJmPromotionDao {
     public List<CmsBtJmPromotionModel> selectList(Map<String, Object> map);
    public CmsBtJmPromotionModel selectOne(Map<String, Object> map);
    public CmsBtJmPromotionModel select(long id);
    public int insert(CmsBtJmPromotionModel entity);
    public int update(CmsBtJmPromotionModel entity);
    public int delete(long id);
    }
