package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface JmBtApiLogDao {
    public List<JmBtApiLogModel>  getList();
    public  JmBtApiLogModel get(long id);
    public int create(JmBtApiLogModel entity);
    public  int update(JmBtApiLogModel entity);

    public  int delete(long id);
    }
