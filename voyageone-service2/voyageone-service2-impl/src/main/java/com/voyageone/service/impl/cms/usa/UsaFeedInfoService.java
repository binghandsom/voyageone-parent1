package com.voyageone.service.impl.cms.usa;

import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by james on 2017/7/5.
 * 美国用 feed info数据service
 */
@Service
public class UsaFeedInfoService extends BaseService {

    @Autowired
    FeedInfoService feedInfoService;

    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;


    //---------------------^^^^-------------------------------

    /**
     * 根据model查询符合特定条件的特定个数(暂定5)的code
     * <p>先查询product.platforms.Pxx.status in[Approved->Ready->Pending]</p>
     * <p>  其中xx为U.S.Official对应的cartId, 平台状态有优先级</p>
     * <p>---------------------------分割线------------------------------</p>
     * <p>如果product查不到满足条件的model信息，则从feed中查询model且status in[Approved->Ready->Pending->New]</p>
     * <p>  feed状态有优先级</p>
     *
     * @param channelId 渠道ID
     * @param model     feed->model
     */
    public void getTopModelsByModel(String channelId, String model) {
        // TODO: 2017/7/5 rex.wu 
    }


}
