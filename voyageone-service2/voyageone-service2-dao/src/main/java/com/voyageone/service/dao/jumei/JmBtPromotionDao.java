package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface JmBtPromotionDao {
    public List<JmBtPromotionModel>  getList();
    public  JmBtPromotionModel get(long id);
    public int create(JmBtPromotionModel entity);
    public  int update(JmBtPromotionModel entity);

    public  int delete(long id);
    }
