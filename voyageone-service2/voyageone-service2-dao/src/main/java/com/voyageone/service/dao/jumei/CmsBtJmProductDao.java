package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface CmsBtJmProductDao {
    public List<CmsBtJmProductModel>  getList();
    public  CmsBtJmProductModel get(long id);
    public int create(CmsBtJmProductModel entity);
    public  int update(CmsBtJmProductModel entity);

    public  int delete(long id);
    }
