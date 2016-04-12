package com.voyageone.web2.cms.views.home.menu;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.impl.cms.ChannelCategoryService;
import com.voyageone.service.impl.cms.feed.FeedCategoryTreeService;
import com.voyageone.service.impl.cms.feed.FeedMappingService;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedMappingModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryTreeModelx;
import com.voyageone.web2.base.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 共通Util:获取feed类目相关的各种处理
 * @author Edward
 * @version 2.0.0, 16/3/3
 */
@Service
public final class CmsFeedCategoriesService extends BaseAppService {

    @Autowired
    private FeedCategoryTreeService feedCategoryTreeService;

    @Autowired
    private FeedMappingService feedMappingService;

    @Autowired
    private ChannelCategoryService cmsBtChannelCategoryService;

    /**
     * 获取feed类目树形结构
     */
    public List<CmsMtCategoryTreeModel> getFeedCategoryMap(String channelId) throws IOException {

        // 获取整个类目树
        CmsMtFeedCategoryTreeModelx treeModelX = feedCategoryTreeService.getFeedCategory(channelId);

        if (treeModelX.getCategoryTree().isEmpty())
            throw new BusinessException("未找到类目");

        // 获取已经绑定的类目
        List<CmsBtFeedMappingModel> feedMappingModels = feedMappingService.getFeedMappings(channelId);

        // 生成主类目的全路径和类目Id关联关系
        List<CmsMtCategoryTreeModel> mtCategoryList = cmsBtChannelCategoryService.getAllCategoriesByChannelId(channelId);
        Map<String, String> mtCategoryMap = new HashMap<>();
        for(CmsMtCategoryTreeModel mtCategory : mtCategoryList)
            mtCategoryMap.put(mtCategory.getCatPath(), mtCategory.getCatId());

        // 将绑定好的类目转成map
        Map<String, String> feedMappingModelMap = new HashMap<>();
        for (CmsBtFeedMappingModel feedMappingModel : feedMappingModels)
            feedMappingModelMap.put(feedMappingModel.getScope().getFeedCategoryPath(), mtCategoryMap.get(feedMappingModel.getScope().getMainCategoryPath()));

        List<CmsMtCategoryTreeModel> result = new ArrayList<>();
        for(CmsMtFeedCategoryModel feedCategory : treeModelX.getCategoryTree()) {
            result.add(buildFeedCategoryBean(feedCategory, feedMappingModelMap, true));
        }

        return result;
    }

    /**
     * 递归重新给Feed类目赋值 并转换成CmsMtCategoryTreeModel.
     */
    private CmsMtCategoryTreeModel buildFeedCategoryBean(CmsMtFeedCategoryModel feedCategoryModel, Map<String, String> feedMappingModelMap, Boolean setMainFlag) {

        CmsMtCategoryTreeModel cmsMtCategoryTreeModel = new CmsMtCategoryTreeModel();

        cmsMtCategoryTreeModel.setCatId(setMainFlag ? feedMappingModelMap.get(feedCategoryModel.getPath()) : feedCategoryModel.getCid());
        cmsMtCategoryTreeModel.setCatName(feedCategoryModel.getName());
        cmsMtCategoryTreeModel.setCatPath(feedCategoryModel.getPath());
        cmsMtCategoryTreeModel.setIsParent(feedCategoryModel.getIsChild() == 1 ? 0 : 1);

        // 先取出暂时保存
        List<CmsMtFeedCategoryModel> children = feedCategoryModel.getChild();
        List<CmsMtCategoryTreeModel> newChild = new ArrayList<>();

        if (children != null && !children.isEmpty()) {
            for (CmsMtFeedCategoryModel child : children) {
                newChild.add(buildFeedCategoryBean(child, feedMappingModelMap, setMainFlag));
            }
        }
        cmsMtCategoryTreeModel.setChildren(newChild);

        return cmsMtCategoryTreeModel;
    }


}
