package com.voyageone.service.impl.cms.imagecreate;
import com.voyageone.service.dao.cms.CmsMtImageCreateTemplateDao;
import com.voyageone.service.dao.jumei.*;
import com.voyageone.service.model.cms.CmsMtImageCreateTemplateModel;
import com.voyageone.service.model.jumei.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsMtImageCreateTemplateService {
    @Autowired
    CmsMtImageCreateTemplateDao dao;
    public CmsMtImageCreateTemplateModel select(int id) {
        return dao.select(id);
    }
    public int update(CmsMtImageCreateTemplateModel entity) {
        return dao.update(entity);
    }
    public int create(CmsMtImageCreateTemplateModel entity) {
        return dao.insert(entity);
    }
}

