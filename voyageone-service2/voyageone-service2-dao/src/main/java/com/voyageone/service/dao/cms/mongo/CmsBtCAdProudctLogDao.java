package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.service.model.cms.mongo.CmsBtCAdProudctModel;
import org.springframework.stereotype.Repository;

/**
 * @author james.li on 2016/9/12.
 * @version 2.0.0
 */
@Repository
public class CmsBtCAdProudctLogDao extends BaseMongoDao<CmsBtCAdProudctModel> {
    protected String collectionName = "cms_bt_ca_proudct_log_c";

    public void insert(String channelId, CmsBtCAdProudctModel cmsBtCAdProudctModel) {
        mongoTemplate.insert(cmsBtCAdProudctModel, collectionName + channelId);
    }
}
