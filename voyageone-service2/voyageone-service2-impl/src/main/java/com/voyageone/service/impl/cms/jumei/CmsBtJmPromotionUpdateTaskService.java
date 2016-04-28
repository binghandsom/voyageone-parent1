package com.voyageone.service.impl.cms.jumei;
import com.voyageone.service.dao.cms.CmsBtJmPromotionUpdateTaskDao;
import com.voyageone.service.model.cms.CmsBtJmPromotionUpdateTaskModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotionUpdateTaskService {
@Autowired
CmsBtJmPromotionUpdateTaskDao dao;

    public CmsBtJmPromotionUpdateTaskModel select(int id)
    {
       return dao.select(id);
    }

    public int update(CmsBtJmPromotionUpdateTaskModel entity)
    {
   return dao.update(entity);
    }
    public int create(CmsBtJmPromotionUpdateTaskModel entity)
    {
                   return dao.insert(entity);
    }
    }

