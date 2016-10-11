package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.model.cms.mongo.CmsBtCAdProductModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author james.li on 2016/9/12.
 * @version 2.0.0
 */
@Repository
public class CmsBtCAdProductDao extends BaseMongoDao<CmsBtCAdProductModel> {
    protected String collectionName = "cms_bt_ca_proudct_c";

    public void insert(String channelId, CmsBtCAdProductModel cmsBtCAdProductModel) {
        mongoTemplate.insert(cmsBtCAdProductModel, collectionName + channelId);
    }

    public CmsBtCAdProductModel getBySellerSku(String channelId, String sellerSku) {
        JongoQuery query = new JongoQuery();
        query.setQuery("{\"SellerSKU\":#}");
        query.setParameters(sellerSku);
        return mongoTemplate.findOne(query, CmsBtCAdProductModel.class, collectionName + channelId);
    }

    public void update(String channelId, CmsBtCAdProductModel cmsBtCAdProductModel) {
        mongoTemplate.save(cmsBtCAdProductModel, collectionName + channelId);
    }

    public List<CmsBtCAdProductModel> getProduct(String channelId, String queryStr, Integer pageNum, Integer pageSize) {
        JongoQuery query = new JongoQuery();
        query.setQuery(queryStr);
        query.setSkip((pageNum - 1) * pageSize);
        query.setLimit(pageSize);
        return mongoTemplate.find(query, CmsBtCAdProductModel.class, collectionName + channelId);
    }

    public List<CmsBtCAdProductModel> getProduct(String channelId, List<String> skus) {
        JongoQuery query = new JongoQuery();
        query.setQuery("{\"SellerSKU\":{$in:#}}");
        query.setParameters(skus);
        return mongoTemplate.find(query, CmsBtCAdProductModel.class, collectionName + channelId);
    }
}
