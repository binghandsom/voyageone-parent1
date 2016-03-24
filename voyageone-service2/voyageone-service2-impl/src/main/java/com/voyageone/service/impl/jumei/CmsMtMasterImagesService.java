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
public class CmsMtMasterImagesService {
@Autowired
    CmsMtMasterImagesDao dao;

    public CmsMtMasterImagesModel select(int id)
    {
       return dao.select(id);
    }
    public List<CmsMtMasterImagesModel>  selectList()
    {
    return dao.selectList();
    }
    public int update(CmsMtMasterImagesModel entity)
    {
   return dao.update(entity);
    }
    public int create(CmsMtMasterImagesModel entity)
    {
                   return dao.insert(entity);
    }
    }

