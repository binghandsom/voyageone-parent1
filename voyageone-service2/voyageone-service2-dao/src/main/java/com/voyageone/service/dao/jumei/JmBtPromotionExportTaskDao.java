package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface JmBtPromotionExportTaskDao {
    public List<JmBtPromotionExportTaskModel>  getList();
    public  JmBtPromotionExportTaskModel get(long id);
    public int create(JmBtPromotionExportTaskModel entity);
    public  int update(JmBtPromotionExportTaskModel entity);

    public  int delete(long id);
    }
