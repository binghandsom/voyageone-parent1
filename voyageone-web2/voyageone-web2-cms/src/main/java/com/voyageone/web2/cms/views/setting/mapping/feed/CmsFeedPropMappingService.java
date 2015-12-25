package com.voyageone.web2.cms.views.setting.mapping.feed;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.service.FeedToCmsService;
import com.voyageone.cms.service.dao.mongodb.CmsMtCategorySchemaDao;
import com.voyageone.cms.service.model.CmsFeedCategoryModel;
import com.voyageone.cms.service.model.CmsFeedMappingModel;
import com.voyageone.cms.service.model.CmsMtCategorySchemaModel;
import com.voyageone.cms.service.model.CmsMtFeedCategoryTreeModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 为属性匹配画面提供功能
 * @author Jonas, 12/24/15.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CmsFeedPropMappingService extends BaseAppService {

    @Autowired
    private FeedToCmsService feedToCmsService;

    @Autowired
    private CmsMtCategorySchemaDao categorySchemaDao;

    @Autowired
    private CmsFeedMappingService feedMappingService;

    public CmsMtCategorySchemaModel getCategoryPropsByFeed(String feedCategoryPath, UserSessionBean userSessionBean) {

        CmsMtFeedCategoryTreeModel treeModel = feedToCmsService.getFeedCategory(userSessionBean.getSelChannelId());

        CmsFeedCategoryModel feedCategoryModel = feedMappingService.findByPath(feedCategoryPath, treeModel);

        if (feedCategoryModel == null)
            throw new BusinessException("根据路径没找到类目");

        CmsFeedMappingModel feedMappingModel = feedMappingService.findMapping(feedCategoryModel, m -> m.getDefaultMapping() == 1);

        if (feedMappingModel == null)
            throw new BusinessException("类目没有默认的类目匹配");

        String categoryId = convertPathToId(feedMappingModel.getMainCategoryPath());

        return categorySchemaDao.getMasterSchemaModelByCatId(categoryId);
    }

    /**
     * 通过 categoryPath 转换获取 categoryId
     * @param categoryPath 类目路径
     * @return String
     */
    public String convertPathToId(String categoryPath) {

        // 当前为 Path 的 Base64 码
        // 有可能未来更改为 MD5
        return new String(Base64.encodeBase64(categoryPath.getBytes()));
    }
}
