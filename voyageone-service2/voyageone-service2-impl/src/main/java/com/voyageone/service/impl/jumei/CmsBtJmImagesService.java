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
public class CmsBtJmImagesService {
@Autowired
    CmsBtJmImagesDao dao;

    public CmsBtJmImagesModel select(int id)
    {
       return dao.select(id);
    }
    public List<CmsBtJmImagesModel>  selectList()
    {
    return dao.selectList();
    }
    public int update(CmsBtJmImagesModel entity)
    {
   return dao.update(entity);
    }
    public int create(CmsBtJmImagesModel entity)
    {
                   return dao.insert(entity);
    }
    }

