package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.model.cms.mongo.CmsBtCAdProudctModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author james.li on 2016/9/12.
 * @version 2.0.0
 */
@Repository
public class CmsBtCAdProudctDao extends BaseMongoDao<CmsBtCAdProudctModel> {
    protected String collectionName = "cms_bt_ca_proudct_c";

    public void insert(String channelId, CmsBtCAdProudctModel cmsBtCAdProudctModel) {
        mongoTemplate.insert(cmsBtCAdProudctModel, collectionName + channelId);
    }

    public CmsBtCAdProudctModel getBySellerSku(String channelId, String sellerSku) {
        JongoQuery query = new JongoQuery();
        query.setQuery("{\"SellerSKU\":#}");
        query.setParameters(sellerSku);
        return mongoTemplate.findOne(query, CmsBtCAdProudctModel.class, collectionName + channelId);
    }

    public void update(String channelId, CmsBtCAdProudctModel cmsBtCAdProudctModel) {
        mongoTemplate.save(cmsBtCAdProudctModel, collectionName + channelId);
    }

    public List<CmsBtCAdProudctModel> getProduct(String channelId, String queryStr, Integer pageNum, Integer pageSize) {
        JongoQuery query = new JongoQuery();
        query.setQuery(queryStr);
        query.setSkip((pageNum - 1) * pageSize);
        query.setLimit(pageSize);
        return mongoTemplate.find(query, CmsBtCAdProudctModel.class, collectionName + channelId);
    }

    public List<CmsBtCAdProudctModel> getProduct(String channelId, List<String> skus) {
        JongoQuery query = new JongoQuery();
        query.setQuery("{\"SellerSKU\":{$in:#}}");
        query.setParameters(skus);
        return mongoTemplate.find(query, CmsBtCAdProudctModel.class, collectionName + channelId);
    }
}
