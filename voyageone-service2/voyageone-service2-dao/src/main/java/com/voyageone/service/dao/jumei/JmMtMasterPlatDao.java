package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface JmMtMasterPlatDao {
    public List<JmMtMasterPlatModel>  selectList();
    public  JmMtMasterPlatModel select(long id);
    public int insert(JmMtMasterPlatModel entity);
    public  int update(JmMtMasterPlatModel entity);

    public  int delete(long id);
    }
