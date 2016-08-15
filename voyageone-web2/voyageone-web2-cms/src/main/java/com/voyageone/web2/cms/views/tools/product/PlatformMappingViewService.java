package com.voyageone.web2.cms.views.tools.product;

import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.tools.PlatformMappingService;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.tools.product.PlatformMappingBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas on 8/15/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
@Service
class PlatformMappingViewService extends BaseAppService {

    private final PlatformMappingService platformMappingService;

    private final PlatformCategoryService platformCategoryService;

    @Autowired
    public PlatformMappingViewService(PlatformMappingService platformMappingService, PlatformCategoryService platformCategoryService) {
        this.platformMappingService = platformMappingService;
        this.platformCategoryService = platformCategoryService;
    }

    public void list(PlatformMappingBean platformMappingBean, UserSessionBean userSessionBean) {

        String channelId = userSessionBean.getSelChannelId();

        String categoryPath = platformMappingBean.getCategoryPath();

        Integer cartId = platformMappingBean.getCartId();

        CmsMtPlatformCategoryTreeModel categoryTreeModel = platformCategoryService.getCategoryByCatPath(channelId, categoryPath, cartId);

        List<CmsMtPlatformCategoryTreeModel> categoryTreeModelList = new ArrayList<>();

        addCategoryAndChildren(categoryTreeModelList, categoryTreeModel);

        // 转换拍平的类目, 把匹配的关系放入类目对象
    }

    private void addCategoryAndChildren(List<CmsMtPlatformCategoryTreeModel> categoryTreeModelList, CmsMtPlatformCategoryTreeModel categoryTreeModel) {

        categoryTreeModelList.add(categoryTreeModel);

        List<CmsMtPlatformCategoryTreeModel> children = categoryTreeModel.getChildren();

        categoryTreeModel.setChildren(null);

        if (children == null || children.isEmpty())
            return;

        for (CmsMtPlatformCategoryTreeModel child: children)
            addCategoryAndChildren(categoryTreeModelList, child);
    }
}
