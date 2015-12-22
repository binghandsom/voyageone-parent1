package com.voyageone.web2.cms.views.setting.mapping.feed;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.service.CmsBtChannelCategoryService;
import com.voyageone.cms.service.CmsFeedMappingService.CategoryContext;
import com.voyageone.cms.service.FeedToCmsService;
import com.voyageone.cms.service.model.CmsFeedCategoryModel;
import com.voyageone.cms.service.model.CmsFeedMappingModel;
import com.voyageone.cms.service.model.CmsMtCategoryTreeModel;
import com.voyageone.cms.service.model.CmsMtFeedCategoryTreeModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.setting.mapping.feed.SetMappingBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

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

    public CmsMtFeedCategoryTreeModel getFeedCategoriyTree(UserSessionBean user) {
        return feedToCmsService.getFeedCategory(user.getSelChannelId());
    }

    public List<CmsMtCategoryTreeModel> getMainCategories(UserSessionBean user) {
        return cmsBtChannelCategoryService.getCategorysByChannelId(user.getSelChannelId());
    }

    public Object setMapping(SetMappingBean setMappingBean, UserSessionBean user) {

        if (StringUtils.isAnyEmpty(setMappingBean.getFrom(), setMappingBean.getTo()))
            throw new BusinessException("木有参数");

        CmsMtFeedCategoryTreeModel treeModel = getFeedCategoriyTree(user);

        // 按 Path 查 FeedCategory
        CmsFeedCategoryModel cmsFeedCategoryModel = findByPath(setMappingBean.getFrom(), treeModel);

        // 准备同步操作 Mapping 数据
        CategoryContext feedMappingContext = cmsFeedMappingService.new CategoryContext(cmsFeedCategoryModel, user.getSelChannel());

        // 检查是否设置过 FeedDefault 的 Mapping
        // 如果有,是否和当前要设定的是同一类目,如果相同,则跳过
        // 如果不是,删除原 FeedDefault Mapping
        // 检查将要被绑定的主类目是否已经绑定,只不过不是 FeedDefault
        // 如果是,则直接变更 FeedDefault 属性
        // 如果不是,则检查主类目是否为第一次被绑定到当前渠道的相关类目
        // 如果是,则需要标记为 MainDefault

        // 记录需要删除的 Mapping,同步删除 Mapping 表的数据
        // 记录需要新增的 Mapping,同步新增的 Mapping 表

        CmsFeedMappingModel defaultMapping = cmsFeedCategoryModel.getMapping()
                .stream()
                .filter(m -> m.getDefaultMapping() == 1)
                .findFirst()
                .orElse(null);

        if (defaultMapping != null) {
            if (defaultMapping.getMainCategoryPath().equals(setMappingBean.getTo()))
                return cmsFeedCategoryModel.getMapping();
            else {
                cmsFeedCategoryModel.getMapping().remove(defaultMapping);
                feedMappingContext.removeMapping(defaultMapping);
            }
        }

        CmsFeedMappingModel mapping = cmsFeedCategoryModel.getMapping()
                .stream()
                .filter(m -> m.getMainCategoryPath().equals(setMappingBean.getTo()))
                .findFirst()
                .orElse(null);

        if (mapping != null) {
            mapping.setDefaultMapping(1);
            feedMappingContext.addMapping(mapping, user.getUserName());
        } else {

            boolean hasDefaultMain = feedToCmsService
                    .flatten(treeModel)
                    .flatMap(c -> c.getMapping().stream())
                    .anyMatch(m -> m.getMainCategoryPath().equals(setMappingBean.getTo()));

            mapping = new CmsFeedMappingModel();
            mapping.setMainCategoryPath(setMappingBean.getTo());
            mapping.setMatchOver(0);
            mapping.setDefaultMapping(1);
            mapping.setDefaultMain(hasDefaultMain ? 0 : 1);

            cmsFeedCategoryModel.getMapping().add(mapping);
            feedMappingContext.addMapping(mapping, user.getUserName());
        }

        treeModel.setModifier(user.getUserName());
        feedToCmsService.save(treeModel);

        return cmsFeedCategoryModel.getMapping();
    }

    private CmsFeedCategoryModel findByPath(String path, CmsMtFeedCategoryTreeModel treeModel) {

        String[] fromPath = path.split("-");

        Stream<CmsFeedCategoryModel> feedCategoryModelStream = treeModel.getCategoryTree().stream();

        for (int i = 0; i < fromPath.length; i++) {

            String name = fromPath[i];

            feedCategoryModelStream = feedCategoryModelStream
                    .filter(c -> c.getName().equals(name));

            if (i == fromPath.length - 1) {
                break;
            }

            feedCategoryModelStream = feedCategoryModelStream
                    .map(CmsFeedCategoryModel::getChild)
                    .flatMap(Collection::stream);
        }

        return feedCategoryModelStream.findFirst().orElse(null);
    }
}
