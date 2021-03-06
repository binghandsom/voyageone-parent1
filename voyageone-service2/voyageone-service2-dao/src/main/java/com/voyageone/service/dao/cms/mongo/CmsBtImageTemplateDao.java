package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CmsBtImageTemplateDao extends BaseMongoDao<CmsBtImageTemplateModel> {

    public CmsBtImageTemplateModel selectByTemplateId(long imageTemplateId) {
        JongoQuery queryObject = new JongoQuery();
        queryObject.setQuery("{\"imageTemplateId\":" + imageTemplateId + "}");
        return selectOneWithQuery(queryObject);
    }

    public void update(CmsBtImageTemplateModel model) {
        super.update(model);
    }

    /**
     * 根据banrd和productType和sizeType取得对应的所有Template列表
     */
    public List<CmsBtImageTemplateModel> selectTemplateForImageUpload(String channelId, String brandName, String productType, String sizeType) {

        StringBuilder sbQuery = new StringBuilder();

        sbQuery.append(MongoUtils.splicingValue("channelId", channelId));

        List<String> tempOrQuery = new ArrayList<>();

        if (!StringUtils.isEmpty(brandName)) {
            tempOrQuery.add(MongoUtils.splicingValue("brandName", new String[]{"all", brandName}, "$in"));
        }

        if (!StringUtils.isEmpty(productType)) {
            tempOrQuery.add(MongoUtils.splicingValue("productType", new String[]{"all", productType}, "$in"));
        }

        if (!StringUtils.isEmpty(sizeType)) {
            tempOrQuery.add(MongoUtils.splicingValue("sizeType", new String[]{"all", sizeType}, "$in"));
        }

        if (tempOrQuery.size() > 0) {
            sbQuery.append(",");
            sbQuery.append(MongoUtils.splicingValue(null, tempOrQuery.toArray(), "$or"));
        }

        String query = "{" + sbQuery.toString() + "}";

        return select(query);
    }

    /**
     * 查询店铺的所有可用模板, 不分类型
     */
    public List<CmsBtImageTemplateModel> selectAll(String channel, Integer cart) {

        return select(String.format("{'channelId':'%s', 'cartId':%s}", channel, cart));
    }
}
