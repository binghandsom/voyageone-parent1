package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface CmsBtJmPromotionExportTaskDao {
    public List<CmsBtJmPromotionExportTaskModel>  getList();
    public  CmsBtJmPromotionExportTaskModel get(long id);
    public int create(CmsBtJmPromotionExportTaskModel entity);
    public  int update(CmsBtJmPromotionExportTaskModel entity);

    public  int delete(long id);
    }
