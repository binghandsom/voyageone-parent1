package com.voyageone.service.impl.cms.jumei;

import com.voyageone.service.dao.jumei.CmsBtJmProductDao;
import com.voyageone.service.model.jumei.CmsBtJmProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmProductService {
@Autowired
    CmsBtJmProductDao dao;

    public CmsBtJmProductModel select(int id)
    {
       return dao.select(id);
    }

    public int update(CmsBtJmProductModel entity)
    {
   return dao.update(entity);
    }
    public int create(CmsBtJmProductModel entity)
    {
                   return dao.insert(entity);
    }

    // 根据条件检索出product数据
    public CmsBtJmProductModel selectOne(Map<String, Object> param) {
            return dao.selectOne(param);
    }
}



