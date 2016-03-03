package com.voyageone.web2.cms.views.home.menu;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.service.dao.mongodb.CmsBtFeedMappingDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtFeedCategoryTreeDao;
import com.voyageone.cms.service.model.CmsBtFeedMappingModel;
import com.voyageone.cms.service.model.CmsFeedCategoryModel;
import com.voyageone.cms.service.model.CmsMtCategoryTreeModel;
import com.voyageone.cms.service.model.CmsMtFeedCategoryTreeModelx;
import com.voyageone.web2.base.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private CmsMtFeedCategoryTreeDao cmsMtFeedCategoryTreeDao;

    @Autowired
    private CmsBtFeedMappingDao cmsBtFeedMappingDao;

    /**
     * 获取feed类目树形结构
     * @param channelId
     * @return
     */
    public List<CmsMtCategoryTreeModel> getFeedCategoryMap(String channelId) {

        // 获取整个类目树
        CmsMtFeedCategoryTreeModelx treeModelX = cmsMtFeedCategoryTreeDao.findFeedCategoryx(channelId);

        if (treeModelX.getCategoryTree().isEmpty())
            throw new BusinessException("未找到类目");

        // 获取已经绑定的类目
        List<CmsBtFeedMappingModel> feedMappingModels =
                cmsBtFeedMappingDao.findMappingByChannelId(channelId);

        // 将绑定好的类目转成map
        Map<String, String> feedMappingModelMap = new HashMap<>();
        for (CmsBtFeedMappingModel feedMappingModel : feedMappingModels)
            feedMappingModelMap.put(feedMappingModel.getScope().getFeedCategoryPath(), feedMappingModel.getScope().getMainCategoryPath());

        List<CmsMtCategoryTreeModel> result = new ArrayList<>();
        for(CmsFeedCategoryModel feedCategory : treeModelX.getCategoryTree()) {
            result.add(buildFeedCategoryBean(feedCategory));
        }

        return result;
    }

    /**
     * 递归重新给Feed类目赋值 并转换成CmsMtCategoryTreeModel.
     * @param feedCategoryModel
     * @return
     */
    private CmsMtCategoryTreeModel buildFeedCategoryBean(CmsFeedCategoryModel feedCategoryModel) {

        CmsMtCategoryTreeModel cmsMtCategoryTreeModel = new CmsMtCategoryTreeModel();

        cmsMtCategoryTreeModel.setCatId(feedCategoryModel.getCid());
        cmsMtCategoryTreeModel.setCatName(feedCategoryModel.getName());
        cmsMtCategoryTreeModel.setCatPath(feedCategoryModel.getPath());
        cmsMtCategoryTreeModel.setIsParent(feedCategoryModel.getIsChild() == 1 ? 0 : 1);

        // 先取出暂时保存
        List<CmsFeedCategoryModel> children = feedCategoryModel.getChild();
        List<CmsMtCategoryTreeModel> newChild = new ArrayList<>();

        if (children != null && !children.isEmpty())
            for (CmsFeedCategoryModel child : children) {
                newChild.add(buildFeedCategoryBean(child));
            }
        cmsMtCategoryTreeModel.setChildren(newChild);

        return cmsMtCategoryTreeModel;
    }
}
