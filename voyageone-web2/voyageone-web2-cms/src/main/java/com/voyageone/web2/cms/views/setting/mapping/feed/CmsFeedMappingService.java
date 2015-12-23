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
import java.util.function.Predicate;
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

    /**
     * 为类目更新 Mapping
     *
     * @param setMappingBean 参数模型,包含 Feed 类目路径和匹配的主类目路径
     * @param user           变动用户信息
     * @return 变动后的 Feed 类目的 Mapping 关系
     */
    public List<CmsFeedMappingModel> setMapping(SetMappingBean setMappingBean, UserSessionBean user) {

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

        CmsFeedMappingModel defaultMapping = findMapping(cmsFeedCategoryModel, m -> m.getDefaultMapping() == 1);

        if (defaultMapping != null) {
            if (defaultMapping.getMainCategoryPath().equals(setMappingBean.getTo()))
                return cmsFeedCategoryModel.getMapping();
            else {
                cmsFeedCategoryModel.getMapping().remove(defaultMapping);
                feedMappingContext.removeMapping(defaultMapping);
            }
        }

        CmsFeedMappingModel mapping = findMapping(cmsFeedCategoryModel, m ->
                m.getMainCategoryPath().equals(setMappingBean.getTo()));

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

            setChildrenMapping(cmsFeedCategoryModel, mapping, user);

            cmsFeedCategoryModel.getMapping().add(mapping);

            feedMappingContext.addMapping(mapping, user.getUserName());
        }

        treeModel.setModifier(user.getUserName());
        feedToCmsService.save(treeModel);

        return cmsFeedCategoryModel.getMapping();
    }

    /**
     * 批量为 cmsFeedCategoryModel 的子类目追加 mapping
     *
     * @param cmsFeedCategoryModel Feed 类目
     * @param mapping              要更新的 Mapping
     * @param userSessionBean      变动的用户信息
     */
    private void setChildrenMapping(CmsFeedCategoryModel cmsFeedCategoryModel, CmsFeedMappingModel mapping, UserSessionBean userSessionBean) {

        feedToCmsService.flatten(cmsFeedCategoryModel.getChild()).forEach(c -> setChildMapping(c, mapping, userSessionBean));
    }

    /**
     * 为 feedCategoryModel 追加 parentMapping
     *
     * @param feedCategoryModel Feed 类目
     * @param parentMapping     来自父级的 Mapping
     * @param userSessionBean   变动的用户信息
     */
    private void setChildMapping(CmsFeedCategoryModel feedCategoryModel, CmsFeedMappingModel parentMapping, UserSessionBean userSessionBean) {

        // 搜索 DefaultFeed Mapping
        // 如果有,则该子类目不需要被设定 Mapping
        // 如果没有,尝试检查目标类目是否已经被匹配到当前类目
        // 如果有,则变更为 DefaultFeed
        // 如果没有,则新建

        // 同步新增和修改 Mapping 表的 DefaultFeed 设置

        CmsFeedMappingModel defaultMapping = findMapping(feedCategoryModel, m -> m.getDefaultMapping() == 1);

        if (defaultMapping != null) return;

        CmsFeedMappingModel mapping = findMapping(feedCategoryModel, m ->
                m.getMainCategoryPath().equals(parentMapping.getMainCategoryPath()));

        if (mapping == null) {
            mapping = new CmsFeedMappingModel();
            mapping.setMainCategoryPath(parentMapping.getMainCategoryPath());
            feedCategoryModel.getMapping().add(mapping);
        }

        mapping.setDefaultMapping(1);

        cmsFeedMappingService.new CategoryContext(feedCategoryModel, userSessionBean.getSelChannel()).addMapping(mapping, userSessionBean.getUserName());
    }

    /**
     * 在类目的 mapping 属性内查找符合 predicate 的 mapping 对象
     *
     * @param feedCategoryModel Feed 类目对象
     * @param predicate         查询条件
     * @return mapping 对象
     */
    private CmsFeedMappingModel findMapping(CmsFeedCategoryModel feedCategoryModel, Predicate<CmsFeedMappingModel> predicate) {
        return feedCategoryModel.getMapping()
                .stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据 path 路径在 treeModel 中查找指定的类目
     *
     * @param path      类目路径
     * @param treeModel 完整的类目树
     * @return 具体的 Feed 类目
     */
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
