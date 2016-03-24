package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface CmsBtJmPromotionUpdateTaskDao {
    public List<CmsBtJmPromotionUpdateTaskModel>  getList();
    public  CmsBtJmPromotionUpdateTaskModel get(long id);
    public int create(CmsBtJmPromotionUpdateTaskModel entity);
    public  int update(CmsBtJmPromotionUpdateTaskModel entity);

    public  int delete(long id);
    }
