package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface JmBtMasterInfoDao {
    public List<JmBtMasterInfoModel>  getList();
    public  JmBtMasterInfoModel get(long id);
    public int create(JmBtMasterInfoModel entity);
    public  int update(JmBtMasterInfoModel entity);

    public  int delete(long id);
    }
