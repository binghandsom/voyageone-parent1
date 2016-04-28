package com.voyageone.service.dao.cms;
import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;

@Repository
public interface CmsBtJmPromotionSkuDao {
     public List<CmsBtJmPromotionSkuModel> selectList(Map<String, Object> map);
    public CmsBtJmPromotionSkuModel selectOne(Map<String, Object> map);
    public CmsBtJmPromotionSkuModel select(long id);
    public int insert(CmsBtJmPromotionSkuModel entity);
    public int update(CmsBtJmPromotionSkuModel entity);
    public int delete(long id);
    }
