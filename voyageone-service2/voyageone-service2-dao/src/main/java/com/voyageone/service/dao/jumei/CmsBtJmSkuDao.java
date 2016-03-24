package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface CmsBtJmSkuDao {
    public List<CmsBtJmSkuModel>  getList();
    public  CmsBtJmSkuModel get(long id);
    public int create(CmsBtJmSkuModel entity);
    public  int update(CmsBtJmSkuModel entity);

    public  int delete(long id);
    }
