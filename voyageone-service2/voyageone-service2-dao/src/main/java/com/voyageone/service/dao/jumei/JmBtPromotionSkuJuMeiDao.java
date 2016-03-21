package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface JmBtPromotionSkuJuMeiDao {
    public List<JmBtPromotionSkuModel>  getList();
    public  JmBtPromotionSkuModel get(long id);
    public int create(JmBtPromotionSkuModel entity);
    public  int update(JmBtPromotionSkuModel entity);

    public  int delete(long id);
    }
