package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface JmBtSkuJuMeiDao {
    public List<JmBtSkuModel>  getList();
    public  JmBtSkuModel get(long id);
    public int create(JmBtSkuModel entity);
    public  int update(JmBtSkuModel entity);

    public  int delete(long id);
    }
