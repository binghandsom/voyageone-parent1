package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;
import java.util.Map;
import java.util.List;

@Repository
public interface CmsBtJmPromotionExportTaskDao {
     public List<CmsBtJmPromotionExportTaskModel> selectList(Map<String, Object> map);
    public CmsBtJmPromotionExportTaskModel selectOne(Map<String, Object> map);
    public CmsBtJmPromotionExportTaskModel select(long id);
    public int insert(CmsBtJmPromotionExportTaskModel entity);
    public int update(CmsBtJmPromotionExportTaskModel entity);
    public int delete(long id);
    }
