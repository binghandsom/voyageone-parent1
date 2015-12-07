package com.voyageone.cms.service.dao.mongodb;

import com.voyageone.base.dao.mongodb.BaseJomgoTemplate;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsMtPlatformMappingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by james.li on 2015/12/7.
 */
@Repository
public class CmsMtPlatformMappingDao extends BaseMongoDao {

    @Autowired
    BaseJomgoTemplate mongoTemplate;

    @Override
    public Class getEntityClass() {
        return CmsMtPlatformMappingModel.class;
    }

    public CmsMtPlatformMappingModel getMapping(String channelId, int cartId, String mainCategoryId) {
        String queryStrTemp = "{" +
                "channelId:'%s'" +
                ",platformCartId:%s" +
                ",mainCategoryId:'%s'" +
                "}";
        String queryStr = String.format(queryStrTemp, channelId, cartId, mainCategoryId);
        return selectOneWithQuery(queryStr);
    }

    public void insertPlatformMapping(CmsMtPlatformMappingModel cmsMtPlatformMappingModel){
        insert(cmsMtPlatformMappingModel);
    }
}
