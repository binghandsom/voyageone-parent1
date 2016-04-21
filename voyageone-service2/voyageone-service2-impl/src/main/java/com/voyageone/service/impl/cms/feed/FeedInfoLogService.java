package com.voyageone.service.impl.cms.feed;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoLogDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * feed info Service
 *
 * @author JiangJusheng 2016/04/06
 * @version 2.0.0
 */
@Service
public class FeedInfoLogService extends BaseService {

    @Autowired
    private CmsBtFeedInfoLogDao CmsBtFeedInfoLogDao;

    /**
     * insertCmsBtFeedInfoLog
     */
    public WriteResult insertCmsBtFeedInfoLog(CmsBtFeedInfoModel cmsBtFeedInfoModel) {
        return CmsBtFeedInfoLogDao.insertCmsBtFeedInfoLog(cmsBtFeedInfoModel);
    }

}
