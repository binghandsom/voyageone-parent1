package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface JmBtDealOldJuMeiDao {
    public List<JmBtDealOldModel>  getList();
    public  JmBtDealOldModel get(long id);
    public int create(JmBtDealOldModel entity);
    public  int update(JmBtDealOldModel entity);

    public  int delete(long id);
    }
