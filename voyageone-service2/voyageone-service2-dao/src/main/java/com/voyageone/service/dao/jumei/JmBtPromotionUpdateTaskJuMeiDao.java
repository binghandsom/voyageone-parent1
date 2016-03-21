package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface JmBtPromotionUpdateTaskJuMeiDao {
    public List<JmBtPromotionUpdateTaskModel>  getList();
    public  JmBtPromotionUpdateTaskModel get(long id);
    public int create(JmBtPromotionUpdateTaskModel entity);
    public  int update(JmBtPromotionUpdateTaskModel entity);

    public  int delete(long id);
    }
