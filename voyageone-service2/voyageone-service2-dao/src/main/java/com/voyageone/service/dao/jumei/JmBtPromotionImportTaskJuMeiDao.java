package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface JmBtPromotionImportTaskJuMeiDao {
    public List<JmBtPromotionImportTaskModel>  getList();
    public  JmBtPromotionImportTaskModel get(long id);
    public int create(JmBtPromotionImportTaskModel entity);
    public  int update(JmBtPromotionImportTaskModel entity);

    public  int delete(long id);
    }
