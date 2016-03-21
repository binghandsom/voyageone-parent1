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
public class JmBtImagesService {
@Autowired
    JmBtImagesJuMeiDao dao;

    public JmBtImagesModel get(int id)
    {
       return dao.get(id);
    }
    public List<JmBtImagesModel>  getList()
    {
    return dao.getList();
    }
    public int update(JmBtImagesModel entity)
    {
   return dao.update(entity);
    }
    public int create(JmBtImagesModel entity)
    {
                   return dao.create(entity);
    }
    }

