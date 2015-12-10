package com.voyageone.web2.cms.views;

import com.voyageone.cms.service.CmsBtChannelCategoryService;
import com.voyageone.cms.service.FeedToCmsService;
import com.voyageone.cms.service.model.CmsBtFeedMappingModel;
import com.voyageone.cms.service.model.CmsMtCategoryTreeModel;
import com.voyageone.cms.service.model.CmsMtFeedCategoryTreeModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jonas, 12/8/15
 * @version 2.0.0
 */
@Service("web2.cms.CmsFeedMappingService")
public class CmsFeedMappingService extends BaseAppService {

    @Autowired
    private FeedToCmsService feedToCmsService;

    @Autowired
    private CmsBtChannelCategoryService cmsBtChannelCategoryService;

    @Autowired
    private com.voyageone.cms.service.CmsFeedMappingService cmsFeedMappingService;

    public CmsMtFeedCategoryTreeModel getFeedCategories(UserSessionBean user) {
        return feedToCmsService.getFeedCategory(user.getSelChannelId());
    }

    public List<CmsMtCategoryTreeModel> getMainCategoryTree(UserSessionBean user) {
        return cmsBtChannelCategoryService.getCategorysByChannelId(user.getSelChannelId());
    }

    public List<CmsBtFeedMappingModel> getFeedMapping(UserSessionBean user) {
        return cmsFeedMappingService.getFeedMapping(user.getSelChannel());
    }
}
