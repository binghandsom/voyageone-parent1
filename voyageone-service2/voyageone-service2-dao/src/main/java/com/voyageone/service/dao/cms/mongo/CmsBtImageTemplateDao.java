package com.voyageone.service.dao.cms.mongo;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CmsBtImageTemplateDao extends BaseMongoDao<CmsBtImageTemplateModel> {

    public CmsBtImageTemplateModel selectByTemplateId(long imageTemplateId) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{\"imageTemplateId\":" + imageTemplateId + "}");
        return selectOneWithQuery(queryObject);
    }


    /**
     * 根据banrd和productType和sizeType取得对应的所有Template列表
     */
    public List<CmsBtImageTemplateModel> selectTemplateForImageUpload(String channelId, String brandName, String productType, String sizeType) {

        StringBuffer sbQuery = new StringBuffer();

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

}
