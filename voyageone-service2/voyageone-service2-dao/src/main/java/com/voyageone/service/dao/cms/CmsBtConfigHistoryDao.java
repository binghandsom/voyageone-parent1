package com.voyageone.service.dao.cms;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.service.model.cms.mongo.CmsBtConfigHistory;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: holysky.zhao
 * @date: 2016/4/28 11:19
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
@Repository
public class CmsBtConfigHistoryDao extends BaseMongoChannelDao<CmsBtConfigHistory> {

    public WriteResult insert(CmsBtConfigHistory model) {
        return super.insert(model);
    }
}
