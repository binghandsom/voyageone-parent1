package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface CmsBtJmPromotionProductDao {
    public List<CmsBtJmPromotionProductModel>  getList();
    public  CmsBtJmPromotionProductModel get(long id);
    public int create(CmsBtJmPromotionProductModel entity);
    public  int update(CmsBtJmPromotionProductModel entity);

    public  int delete(long id);
    }
