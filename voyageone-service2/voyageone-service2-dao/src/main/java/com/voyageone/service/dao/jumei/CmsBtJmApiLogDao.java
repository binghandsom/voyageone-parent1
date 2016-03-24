package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface CmsBtJmApiLogDao {
    public List<CmsBtJmApiLogModel>  getList();
    public  CmsBtJmApiLogModel get(long id);
    public int create(CmsBtJmApiLogModel entity);
    public  int update(CmsBtJmApiLogModel entity);

    public  int delete(long id);
    }
