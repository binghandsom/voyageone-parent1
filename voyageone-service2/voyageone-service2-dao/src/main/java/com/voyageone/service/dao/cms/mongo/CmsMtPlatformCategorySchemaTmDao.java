package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaTmModel;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

/**
 * Created by tom on 16-07-26.
 */
@Repository
public class CmsMtPlatformCategorySchemaTmDao extends BaseMongoChannelDao<CmsMtPlatformCategorySchemaTmModel> {

    /**
     * 删除参数指定的schema
     */
    public WriteResult deletePlatformCategorySchemaByChannnelCart(String channelId, Integer cartId) {
        String queryStr = String.format("{channelId: '%s', cartId:%s}", channelId, cartId);
        return deleteWithQuery(queryStr, channelId);
    }

    /**
     * 删除参数指定的schema: channel, cart, category
     */
    public WriteResult deletePlatformCategorySchemaByChannnelCartCategory(String channelId, Integer cartId, String categoryId){
        String queryStr = String.format("{channelId: '%s', cartId:%s, catId:'%s'}", channelId, cartId, categoryId);
        return deleteWithQuery(queryStr, channelId);
    }

    public CmsMtPlatformCategorySchemaTmModel selectPlatformCatSchemaTmModel(String catId, String channelId, int cartId){
        String queryStr = "{" +
                "cartId: " + cartId +
                ", catId: '" + catId + "'" +
                "}";
        return selectOneWithQuery(queryStr, channelId);
    }

    public CmsMtPlatformCategorySchemaTmModel selectByCategoryPath(String categoryPath, String channelId, int cartId) {
        return selectOneWithQuery(new JongoQuery(
                new Criteria("catFullPath").is(categoryPath)
                        .and("cartId").is(cartId)
                        .and("channelId").is(channelId)
        ), channelId);
    }
}
