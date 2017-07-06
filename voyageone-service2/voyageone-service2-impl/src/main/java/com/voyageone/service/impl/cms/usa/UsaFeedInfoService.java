package com.voyageone.service.impl.cms.usa;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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


        String columnResult="{_id:1,code:1,model:1}";
        String query = String.format("{\"channelId\":#,\"model\":#}");
        JongoQuery jongoQuery = new JongoQuery(columnResult, query, null, 5, 0);
        jongoQuery.setParameters(channelId, model);
        List<CmsBtFeedInfoModel> feedInfoModelList = cmsBtFeedInfoDao.select(jongoQuery, channelId);
        if (!feedInfoModelList.isEmpty()) {
            for (CmsBtFeedInfoModel feed : feedInfoModelList) {
                System.out.println(feed.getModel());
            }
        }
    }
}
