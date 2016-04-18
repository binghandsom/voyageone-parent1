package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.common.util.BeanUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoLogModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by james.li on 2015/11/27.
 */

@Repository
public class CmsBtFeedInfoLogDao extends BaseMongoChannelDao<CmsBtFeedInfoLogModel> {
    public WriteResult insertCmsBtFeedInfoLog (CmsBtFeedInfoModel cmsBtFeedInfoModel){
        CmsBtFeedInfoLogModel cmsBtFeedInfoLogModel = new CmsBtFeedInfoLogModel();
        cmsBtFeedInfoLogModel = JacksonUtil.json2Bean(JacksonUtil.bean2Json(cmsBtFeedInfoModel),CmsBtFeedInfoLogModel.class);
        return insert(cmsBtFeedInfoLogModel);
    }
}
