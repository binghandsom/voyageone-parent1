package com.voyageone.web2.cms.views.setting.mapping.feed;

import com.voyageone.cms.service.FeedToCmsService;
import com.voyageone.cms.service.model.CmsMtFeedCategoryTreeModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jonas, 12/8/15
 * @version 2.0.0
 */
@Service("web2.cms.CmsFeedMappingService")
public class CmsFeedMappingService extends BaseAppService {

    @Autowired
    private FeedToCmsService feedToCmsService;

    public CmsMtFeedCategoryTreeModel getFeedCategoriyTree(UserSessionBean user) {
        return feedToCmsService.getFeedCategory(user.getSelChannelId());
    }
}
