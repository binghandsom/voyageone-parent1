package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;
import java.util.Map;
import java.util.List;

@Repository
public interface CmsBtJmPromotionUpdateTaskDao {
     public List<CmsBtJmPromotionUpdateTaskModel> selectList(Map<String, Object> map);
    public CmsBtJmPromotionUpdateTaskModel selectOne(Map<String, Object> map);
    public CmsBtJmPromotionUpdateTaskModel select(long id);
    public int insert(CmsBtJmPromotionUpdateTaskModel entity);
    public int update(CmsBtJmPromotionUpdateTaskModel entity);
    public int delete(long id);
    }
