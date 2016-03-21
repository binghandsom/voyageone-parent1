package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface JmBtPromotionProductJuMeiDao {
    public List<JmBtPromotionProductModel>  getList();
    public  JmBtPromotionProductModel get(long id);
    public int create(JmBtPromotionProductModel entity);
    public  int update(JmBtPromotionProductModel entity);

    public  int delete(long id);
    }
