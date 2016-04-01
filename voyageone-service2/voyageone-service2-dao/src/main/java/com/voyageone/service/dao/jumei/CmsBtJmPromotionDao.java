package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;
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
