package com.voyageone.service.impl.cms;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.service.dao.cms.mongo.CmsBtImageGroupDao;
import com.voyageone.service.dao.cms.mongo.CmsBtImageTemplateDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ImageGroup Service
 *
 * @author jeff.duan 16/6/6
 * @version 2.0.0
 */
@Service
public class ImageTemplateService extends BaseService {
    @Autowired
    private CmsBtImageTemplateDao dao;

    public void save(CmsBtImageTemplateModel model) {
        dao.insert(model);
    }

    public void update(CmsBtImageTemplateModel model) {
        dao.update(model);
    }

    public List<CmsBtImageTemplateModel> getList(JomgoQuery queryObject) {
        return dao.select(queryObject);
    }

    public CmsBtImageTemplateModel getOne(JomgoQuery queryObject) {
        return dao.selectOneWithQuery(queryObject);
    }
}
