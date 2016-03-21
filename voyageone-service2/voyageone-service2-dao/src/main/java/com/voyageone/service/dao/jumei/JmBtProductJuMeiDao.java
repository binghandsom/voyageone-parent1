package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface JmBtProductJuMeiDao {
    public List<JmBtProductModel>  getList();
    public  JmBtProductModel get(long id);
    public int create(JmBtProductModel entity);
    public  int update(JmBtProductModel entity);

    public  int delete(long id);
    }
