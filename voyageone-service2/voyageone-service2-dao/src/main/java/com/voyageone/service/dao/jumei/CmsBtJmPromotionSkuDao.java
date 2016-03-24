package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface CmsBtJmPromotionSkuDao {
    public List<CmsBtJmPromotionSkuModel>  selectList();
    public  CmsBtJmPromotionSkuModel select(long id);
    public int insert(CmsBtJmPromotionSkuModel entity);
    public  int update(CmsBtJmPromotionSkuModel entity);

    public  int delete(long id);
    }
