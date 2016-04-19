package com.voyageone.service.impl.cms.feed;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedAttributesModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
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
public class FeedInfoService extends BaseService {

    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;

    /**
     * getList
     */
    public List<CmsBtFeedInfoModel> getList(String channelId, JomgoQuery queryObject) {
        return cmsBtFeedInfoDao.select(queryObject, channelId);
    }

    /**
     * getCnt
     */
    public long getCnt(String channelId, String queryStr) {
        return cmsBtFeedInfoDao.countByQuery(queryStr, channelId);
    }

    /**
     * getProductByCode
     */
    public CmsBtFeedInfoModel getProductByCode(String channelId, String productCode) {
        return cmsBtFeedInfoDao.selectProductByCode(channelId, productCode);
    }

    /**
     * 更新feed的产品信息
     * @param cmsBtFeedInfoModel feed的产品信息
     * @return     WriteResult
     */
    public WriteResult updateFeedInfo(CmsBtFeedInfoModel cmsBtFeedInfoModel){
        return cmsBtFeedInfoDao.update(cmsBtFeedInfoModel);
    }

}
