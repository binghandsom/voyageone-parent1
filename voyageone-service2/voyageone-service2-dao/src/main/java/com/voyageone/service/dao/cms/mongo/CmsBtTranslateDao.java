package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.model.cms.mongo.CmsBtCustomPropModel;
import com.voyageone.service.model.cms.mongo.CmsBtTranslateModel;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by james on 2017/2/27.
 */
@Repository
public class CmsBtTranslateDao extends BaseMongoDao<CmsBtTranslateModel> {

    public CmsBtTranslateModel get(String channelId, Integer customPropType, String name, String valueEn) {
        JongoQuery jongoQuery = new JongoQuery();
        jongoQuery.setQuery(new Criteria("channelId").is(channelId).and("type").is(customPropType).and("name").is(name).and("valueEn").is(valueEn));
        return selectOneWithQuery(jongoQuery);
    }

    public void save(CmsBtTranslateModel cmsBtTranslateModel) {
        mongoTemplate.save(cmsBtTranslateModel);
    }

    public List<CmsBtTranslateModel> select(String channelId, Integer customPropType, String name, String valueEn, Integer skip, Integer limit) {

        JongoQuery jongoQuery = new JongoQuery();
        Criteria criteria = new Criteria("channelId").is(channelId);
        if (customPropType != null) {
            criteria.and("type").is(customPropType);
        }
        if (name != null) {
            criteria.and("name").regex(name);
        }
        if (valueEn != null) {
            criteria.and("valueEn").regex(valueEn);
        }
        jongoQuery.setQuery(criteria);
        jongoQuery.setSkip((skip - 1) * limit);
        jongoQuery.setLimit(limit);
        return select(jongoQuery);
    }

    public Long selectCnt(String channelId, Integer customPropType, String name, String valueEn) {
        JongoQuery jongoQuery = new JongoQuery();
        Criteria criteria = new Criteria("channelId").is(channelId);
        if (customPropType != null) {
            criteria.and("type").is(customPropType);
        }
        if (name != null) {
            criteria.and("name").regex(name);
        }
        if (valueEn != null) {
            criteria.and("valueEn").regex(valueEn);
        }
        jongoQuery.setQuery(criteria);
        return countByQuery(jongoQuery);
    }
}
