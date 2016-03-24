package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface CmsBtJmPromotionImportTaskDao {
    public List<CmsBtJmPromotionImportTaskModel>  getList();
    public  CmsBtJmPromotionImportTaskModel get(long id);
    public int create(CmsBtJmPromotionImportTaskModel entity);
    public  int update(CmsBtJmPromotionImportTaskModel entity);

    public  int delete(long id);
    }
