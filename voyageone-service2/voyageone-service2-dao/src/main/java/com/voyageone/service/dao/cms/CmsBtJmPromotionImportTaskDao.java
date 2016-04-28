package com.voyageone.service.dao.cms;
import com.voyageone.service.model.cms.CmsBtJmPromotionImportTaskModel;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;

@Repository
public interface CmsBtJmPromotionImportTaskDao {
     public List<CmsBtJmPromotionImportTaskModel> selectList(Map<String, Object> map);
    public CmsBtJmPromotionImportTaskModel selectOne(Map<String, Object> map);
    public CmsBtJmPromotionImportTaskModel select(long id);
    public int insert(CmsBtJmPromotionImportTaskModel entity);
    public int update(CmsBtJmPromotionImportTaskModel entity);
    public int delete(long id);
    }
