package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformSxImageTemplateModel;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by james on 2017/6/29.
 *
 */
@Repository
public class CmsMtPlatformSxImageTemplateDao extends BaseMongoDao<CmsMtPlatformSxImageTemplateModel> {

    public List<CmsMtPlatformSxImageTemplateModel> selectSxImageTemplateByChannelAndCart(String channelId, Integer cartId){
        JongoQuery jongoQuery = new JongoQuery();
        jongoQuery.setQuery(new Criteria("channelId").is(channelId).and("cartId").is(cartId));
        return select(jongoQuery);
    }

    public CmsMtPlatformSxImageTemplateModel select(String channelId, Integer cartId, String imageTemplate){
        JongoQuery jongoQuery = new JongoQuery();
        jongoQuery.setQuery(new Criteria("channelId").is(channelId).and("cartId").is(cartId).and("imageTemplate").is(imageTemplate));
        return selectOneWithQuery(jongoQuery);
    }

    public void add(CmsMtPlatformSxImageTemplateModel cmsMtPlatformSxImageTemplateModel){
        CmsMtPlatformSxImageTemplateModel model = select(cmsMtPlatformSxImageTemplateModel.getChannelId(), cmsMtPlatformSxImageTemplateModel.getCartId(), cmsMtPlatformSxImageTemplateModel.getImageTemplate());
        if(model == null){
            super.insert(cmsMtPlatformSxImageTemplateModel);
        }
    }
}
