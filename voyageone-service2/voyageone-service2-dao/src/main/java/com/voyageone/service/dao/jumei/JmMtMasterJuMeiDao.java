package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface JmMtMasterJuMeiDao {
    public List<JmMtMasterModel>  getList();
    public  JmMtMasterModel get(long id);
    public int create(JmMtMasterModel entity);
    public  int update(JmMtMasterModel entity);

    public  int delete(long id);
    }
