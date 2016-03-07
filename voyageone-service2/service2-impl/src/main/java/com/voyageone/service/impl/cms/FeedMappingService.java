package com.voyageone.service.impl.cms;

import com.voyageone.cms.service.dao.mongodb.CmsBtFeedMappingDao;
import com.voyageone.cms.service.model.CmsBtFeedMappingModel;
import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.sdk.api.request.FeedMappingsGetRequest;
import com.voyageone.web2.sdk.api.response.FeedMappingsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * FeedMapping Service
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since. 2.0.0
 */
@Service
public class FeedMappingService extends BaseService {

    @Autowired
    private CmsBtFeedMappingDao cmsBtFeedMappingDao;

    /**
     * getList
     * @param request FeedMappingsGetRequest
     * @return FeedMappingsGetResponse
     */
    public FeedMappingsGetResponse getList(FeedMappingsGetRequest request) {
        checkCommRequest(request);
        request.check();

        FeedMappingsGetResponse result = new FeedMappingsGetResponse();
        String channelId = request.getChannelId();

        List<CmsBtFeedMappingModel> list = new ArrayList<>();
        if (request.getFeedCategoryPath() != null && request.getMainCategoryPath() != null) {
            CmsBtFeedMappingModel model = cmsBtFeedMappingDao.selectByKey(channelId, request.getFeedCategoryPath(), request.getMainCategoryPath());
            list.add(model);
        } else {
            List<CmsBtFeedMappingModel> feedMappingList = cmsBtFeedMappingDao.selectCategoryMappingByChannel(channelId);
            if (feedMappingList != null) {
                list = feedMappingList;
            }
        }
        result.setFeedMappings(list);
        result.setTotalCount((long)list.size());

        return result;
    }

}
