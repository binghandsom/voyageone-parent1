package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface CmsBtJmApiLogDao {
    public List<CmsBtJmApiLogModel>  selectList();
    public  CmsBtJmApiLogModel select(long id);
    public int insert(CmsBtJmApiLogModel entity);
    public  int update(CmsBtJmApiLogModel entity);

    public  int delete(long id);
    }
