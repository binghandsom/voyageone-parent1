package com.voyageone.service.impl.cms;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformSxImageTemplateDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformSxImageTemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

/**
 * Created by james on 2017/6/29.
 */
public class CmsMtPlatformSxImageTemplateService extends BaseService {

    final
    CmsMtPlatformSxImageTemplateDao cmsMtPlatformSxImageTemplateDao;

    @Autowired
    public CmsMtPlatformSxImageTemplateService(CmsMtPlatformSxImageTemplateDao cmsMtPlatformSxImageTemplateDao) {
        this.cmsMtPlatformSxImageTemplateDao = cmsMtPlatformSxImageTemplateDao;
    }

    public List<CmsMtPlatformSxImageTemplateModel> getSxImageTemplateByChannelAndCart(String channelId, Integer cartId){
        return cmsMtPlatformSxImageTemplateDao.selectSxImageTemplateByChannelAndCart(channelId, cartId);
    }

    public void add(CmsMtPlatformSxImageTemplateModel cmsMtPlatformSxImageTemplateModel){
        cmsMtPlatformSxImageTemplateDao.insert(cmsMtPlatformSxImageTemplateModel);
    }
}
