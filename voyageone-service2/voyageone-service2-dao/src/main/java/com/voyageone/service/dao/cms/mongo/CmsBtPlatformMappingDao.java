package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.model.cms.mongo.CmsBtPlatformMappingModel;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public CmsBtPlatformMappingModel selectCommon(int cartId, String channelId) {

        return selectOneWithQuery(new JongoQuery(
                new Criteria("cartId").is(cartId)
                        .and("categoryType").is(1)), channelId);
    }

    public CmsBtPlatformMappingModel selectOne(int cartId, int categoryType, String categoryPath, String channelId) {

        return selectOneWithQuery(new JongoQuery(
                new Criteria("cartId").is(cartId)
                        .and("categoryType").is(categoryType)
                        .and("categoryPath").is(categoryPath)), channelId);
    }

    public boolean exists(CmsBtPlatformMappingModel fieldMapsModel) {

        JongoQuery query = new JongoQuery(
                new Criteria("cartId").is(fieldMapsModel.getCartId())
                        .and("categoryType").is(fieldMapsModel.getCategoryType())
                        .and("categoryPath").is(fieldMapsModel.getCategoryPath()));

        return countByQuery(query.getQuery(), fieldMapsModel.getChannelId()) > 0;
    }

    public List<CmsBtPlatformMappingModel> selectPage(String channelId, int cartId, int offset, int limit) {
        return select(new JongoQuery(new Criteria("cartId").is(cartId)).setSkip(offset).setLimit(limit), channelId);
    }

    public long count(String channelId, int cartId) {
        return countByQuery(new JongoQuery(new Criteria("cartId").is(cartId)).getQuery(), channelId);
    }

    public List<CmsBtPlatformMappingModel> selectPage(String channelId, int cartId, String categoryPath, int offset, int limit) {
        return select(new JongoQuery(new Criteria("cartId").is(cartId).and("categoryPath").regex(categoryPath)).setSkip(offset).setLimit(limit), channelId);
    }

    public long count(String channelId, int cartId, String categoryPath) {
        return countByQuery(new JongoQuery(new Criteria("cartId").is(cartId).and("categoryPath").regex(categoryPath)).getQuery(), channelId);
    }
}
