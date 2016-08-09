package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.common.util.BeanUtils;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoLogModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import org.springframework.stereotype.Repository;

/**
 * Created by james.li on 2015/11/27.
 *
 * @author james.li
 * @version 2.4.0
 * @since 2.0.0
 */
@Repository
public class CmsBtFeedInfoLogDao extends BaseMongoChannelDao<CmsBtFeedInfoLogModel> {

    public WriteResult insertCmsBtFeedInfoLog(CmsBtFeedInfoModel cmsBtFeedInfoModel) {

        CmsBtFeedInfoLogModel cmsBtFeedInfoLogModel = new CmsBtFeedInfoLogModel();

        BeanUtils.copy(cmsBtFeedInfoModel, cmsBtFeedInfoLogModel);

        return insert(cmsBtFeedInfoLogModel);
    }
}
