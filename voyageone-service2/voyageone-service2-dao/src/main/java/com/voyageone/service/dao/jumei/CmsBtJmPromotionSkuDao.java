package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface CmsBtJmPromotionSkuDao {
    public List<CmsBtJmPromotionSkuModel>  getList();
    public  CmsBtJmPromotionSkuModel get(long id);
    public int create(CmsBtJmPromotionSkuModel entity);
    public  int update(CmsBtJmPromotionSkuModel entity);

    public  int delete(long id);
    }
