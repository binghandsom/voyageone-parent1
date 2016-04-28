package com.voyageone.service.dao.cms;
import com.voyageone.service.model.cms.CmsBtJmPromotionExportTaskModel;
import org.springframework.stereotype.Repository;

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
