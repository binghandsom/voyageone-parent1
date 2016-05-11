package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.service.dao.cms.CmsMtImageCreateTaskDao;
import com.voyageone.service.model.cms.CmsMtImageCreateTaskModel;
import org.codehaus.jackson.map.deser.ValueInstantiators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CmsMtImageCreateTaskService extends ValueInstantiators.Base {
    @Autowired
    CmsMtImageCreateTaskDao dao;

    public CmsMtImageCreateTaskModel get(int id) {
        return dao.select(id);
    }

    public int save(CmsMtImageCreateTaskModel model) {
        return dao.update(model);
    }
}
