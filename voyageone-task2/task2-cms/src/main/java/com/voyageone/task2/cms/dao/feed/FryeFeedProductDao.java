package com.voyageone.task2.cms.dao.feed;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.service.model.cms.mongo.CmsBtCAdProductModel;
import org.springframework.stereotype.Repository;

/**
 * Created by gjl on 2016/12/16.
 */
@Repository
public class FryeFeedProductDao extends BaseMongoDao<CmsBtCAdProductModel> {
    protected String collectionName = "proudct_032";

}
