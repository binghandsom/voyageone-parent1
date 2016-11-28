package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductTopModel;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class CmsBtProductTopDao extends BaseMongoDao<CmsBtProductTopModel> {

    public CmsBtProductTopModel selectByTemplateId(long productTopId) {
        JongoQuery queryObject = new JongoQuery();
        queryObject.setQuery("{\"productTopId\":" + productTopId + "}");
        return selectOneWithQuery(queryObject);
    }

    public void update(CmsBtProductTopModel model) {
        super.update(model);
    }

    /**
     * 根据channelId和catId列表
     */
    public List<CmsBtProductTopModel> selectTemplateForImageUpload(String channelId, String catId) {

        StringBuilder sbQuery = new StringBuilder();

        sbQuery.append(MongoUtils.splicingValue("channelId", channelId));

        sbQuery.append(",");
        sbQuery.append(MongoUtils.splicingValue("catId", catId));

        String query = "{" + sbQuery.toString() + "}";

        return select(query);
    }
}
