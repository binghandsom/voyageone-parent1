package com.voyageone.service.impl.jumei;
import com.voyageone.service.dao.jumei.*;
import com.voyageone.service.model.jumei.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class JmMtMasterService {
@Autowired
    JmMtMasterJuMeiDao dao;

    public JmMtMasterModel get(int id)
    {
       return dao.get(id);
    }
    public List<JmMtMasterModel>  getList()
    {
    return dao.getList();
    }
    public int update(JmMtMasterModel entity)
    {
   return dao.update(entity);
    }
    public int create(JmMtMasterModel entity)
    {
                   return dao.create(entity);
    }
    }

