package com.voyageone.service.dao.cms;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.service.model.cms.mongo.CmsMtChangeHistoryModel;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: holysky.zhao
 * @date: 2016/4/28 11:19
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
@Repository
public class CmsMtChangeHistoryDao extends BaseMongoChannelDao<CmsMtChangeHistoryModel> {

    public WriteResult insert(CmsMtChangeHistoryModel model) {
        return super.insert(model);
    }
}
