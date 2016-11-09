package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.model.cms.mongo.CmsBtPlatformMappingModel;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;

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
        return selectCommon(cartId, channelId, null);
    }

    /**
     * @since 2.9.0
     */
    public CmsBtPlatformMappingModel selectCommon(int cartId, String channelId, String fieldId) {
        Criteria criteria = new Criteria("categoryType").is(1).and("cartId").is(cartId);
        JongoQuery jongoQuery = new JongoQuery(criteria);

        if (!StringUtils.isEmpty(fieldId)) {
            jongoQuery.setProjectionExt(
                    "created",
                    "creater",
                    "modified",
                    "modifier",
                    "channelId",
                    "cartId",
                    "categoryType",
                    "categoryPath",
                    "mappings." + fieldId
            );
        }

        return selectOneWithQuery(jongoQuery, channelId);
    }

    public CmsBtPlatformMappingModel selectOne(int cartId, int categoryType, String categoryPath, String channelId) {
        return selectOne(cartId, categoryType, categoryPath, channelId, null);
    }

    /**
     * @since 2.9.0
     */
    public CmsBtPlatformMappingModel selectOne(int cartId, int categoryType, String categoryPath, String channelId, String fieldId) {

        JongoQuery jongoQuery = new JongoQuery(
                new Criteria("cartId").is(cartId)
                        .and("categoryType").is(categoryType)
                        .and("categoryPath").is(categoryPath));

        if (!StringUtils.isEmpty(fieldId)) {
            jongoQuery.setProjectionExt(
                    "created",
                    "creater",
                    "modified",
                    "modifier",
                    "channelId",
                    "cartId",
                    "categoryType",
                    "categoryPath",
                    "mappings." + fieldId
            );
        }

        return selectOneWithQuery(jongoQuery, channelId);
    }

    public boolean exists(CmsBtPlatformMappingModel fieldMapsModel) {

        JongoQuery query = new JongoQuery(
                new Criteria("cartId").is(fieldMapsModel.getCartId())
                        .and("categoryType").is(fieldMapsModel.getCategoryType())
                        .and("categoryPath").is(fieldMapsModel.getCategoryPath()));

        return countByQuery(query.getQuery(), fieldMapsModel.getChannelId()) > 0;
    }

    public List<CmsBtPlatformMappingModel> selectPage(String channelId, Integer categoryType, Integer cartId, String categoryPathPrefix, int offset, int limit) {

        Criteria criteria = new Criteria("channelId").is(channelId);

        if (categoryType != null)
            criteria.and("categoryType").is(categoryType);

        if (cartId != null)
            criteria.and("cartId").is(cartId);

        if (!StringUtils.isEmpty(categoryPathPrefix))
            criteria.and("categoryPath").regex(Pattern.quote(categoryPathPrefix) + ".*");

        return select(new JongoQuery(criteria)
                .setProjection("{\"mappings\":0}")
                .setSort("{\"modified\":-1}")
                .setSkip(offset)
                .setLimit(limit), channelId);
    }

    public long count(String channelId, Integer categoryType, Integer cartId, String categoryPath) {

        Criteria criteria = new Criteria("channelId").is(channelId);

        if (categoryType != null)
            criteria.and("categoryType").is(categoryType);

        if (cartId != null)
            criteria.and("cartId").is(cartId);

        if (!StringUtils.isEmpty(categoryPath))
            criteria.and("categoryPath").is(categoryPath);

        return countByQuery(new JongoQuery(criteria).getQuery(), channelId);
    }

    public String selectModified(CmsBtPlatformMappingModel fieldMapsModel) {

        String channelId = fieldMapsModel.getChannelId();
        Integer type = fieldMapsModel.getCategoryType();

        Criteria criteria = new Criteria("channelId").is(channelId)
                .and("categoryType").is(type)
                .and("cartId").is(fieldMapsModel.getCartId());

        if (type != 1)
            criteria.and("categoryPath").is(fieldMapsModel.getCategoryPath());

        JongoQuery query = new JongoQuery(criteria);

        query.setProjectionExt("modified");

        CmsBtPlatformMappingModel model = selectOneWithQuery(query, channelId);

        return model.getModified();
    }
}
