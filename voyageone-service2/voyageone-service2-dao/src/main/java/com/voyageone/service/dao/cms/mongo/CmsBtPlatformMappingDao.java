package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.model.cms.mongo.CmsBtPlatformMappingModel;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

/**
 * 查询平台类目的属性匹配
 * <p>
 * Created by jonas on 8/13/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
@Repository
public class CmsBtPlatformMappingDao extends BaseMongoChannelDao<CmsBtPlatformMappingModel> {

    public CmsBtPlatformMappingModel selectOne(int cartId, int categoryType, String categoryId, String channelId) {

        return selectOneWithQuery(new JongoQuery(
                new Criteria("cartId").is(cartId)
                        .and("categoryType").is(categoryType)
                        .and("categoryId").is(categoryId)), channelId);
    }

    public boolean exists(CmsBtPlatformMappingModel fieldMapsModel) {

        JongoQuery query = new JongoQuery(
                new Criteria("cartId").is(fieldMapsModel.getCartId())
                        .and("categoryType").is(fieldMapsModel.getCategoryType())
                        .and("categoryId").is(fieldMapsModel.getCategoryId()));

        return countByQuery(query.getQuery(), fieldMapsModel.getChannelId()) > 0;
    }
}
