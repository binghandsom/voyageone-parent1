package com.voyageone.web2.cms.views.setting.mapping.feed;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.service.CmsBtChannelCategoryService;
import com.voyageone.cms.service.CmsFeedMappingService.CategoryContext;
import com.voyageone.cms.service.dao.mongodb.CmsMtFeedCategoryTreeDao;
import com.voyageone.cms.service.model.CmsFeedCategoryModel;
import com.voyageone.cms.service.model.CmsFeedMappingModel;
import com.voyageone.cms.service.model.CmsMtCategoryTreeModel;
import com.voyageone.cms.service.model.CmsMtFeedCategoryTreeModelx;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.setting.mapping.feed.FeedCategoryBean;
import com.voyageone.web2.cms.bean.setting.mapping.feed.SetMappingBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * @author Jonas, 12/8/15
 * @version 2.0.0
 */
@Service("web2.cms.CmsFeedMappingService")
public class CmsFeedMappingService extends BaseAppService {

    @Autowired
    private CmsMtFeedCategoryTreeDao cmsMtFeedCategoryTreeDao;

    @Autowired
    private CmsBtChannelCategoryService cmsBtChannelCategoryService;

    @Autowired
    private com.voyageone.cms.service.CmsFeedMappingService cmsFeedMappingService;

    public Map<String, Map<String, FeedCategoryBean>> getFeedCategoryMap(UserSessionBean user) {

        CmsMtFeedCategoryTreeModelx treeModelx = cmsMtFeedCategoryTreeDao.selectFeedCategoryx(user.getSelChannelId());

        // 将树形转化为利于画面查询数据的键值结构
        Map<String, Map<String, FeedCategoryBean>> map = new HashMap<>();

        for (CmsFeedCategoryModel feedCategoryModel : treeModelx.getCategoryTree()) {

            final int[] seq = {0};

            Stream<FeedCategoryBean> feedCategoryBeanStream = buildFeedCategoryBean(feedCategoryModel);

            Map<String, FeedCategoryBean> children = feedCategoryBeanStream
                    .map(f -> {
                        f.setSeq(seq[0]);
                        seq[0]++;
                        return f;
                    })
                    .collect(toMap(f -> f.getModel().getPath(), f -> f));

            map.put(feedCategoryModel.getPath(), children);
        }

        return map;
    }

    private Stream<FeedCategoryBean> buildFeedCategoryBean(CmsFeedCategoryModel feedCategoryModel) {
        // 先取出暂时保存
        List<CmsFeedCategoryModel> children = feedCategoryModel.getChild();
        // 创建新模型
        FeedCategoryBean feedCategoryBean = new FeedCategoryBean();
        feedCategoryBean.setLevel(StringUtils.countMatches(feedCategoryModel.getPath(), "-"));
        feedCategoryBean.setModel(feedCategoryModel);
        feedCategoryBean.setMapping(findMapping(feedCategoryModel, m -> m.getDefaultMapping() == 1));
        // 输出流
        Stream<FeedCategoryBean> feedCategoryBeanStream = Stream.of(feedCategoryBean);
        // 如果有子,则继续输出子
        if (children != null && !children.isEmpty())
            feedCategoryBeanStream = Stream.concat(feedCategoryBeanStream,
                    children.stream().flatMap(this::buildFeedCategoryBean));

        return feedCategoryBeanStream;
    }

    public List<CmsMtCategoryTreeModel> getMainCategories(UserSessionBean user) {
        return cmsBtChannelCategoryService.getCategoriesByChannelId(user.getSelChannelId());
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

        CmsMtFeedCategoryTreeModelx treeModelx = cmsMtFeedCategoryTreeDao.selectFeedCategoryx(user.getSelChannelId());

        // 按 Path 查 FeedCategory
        CmsFeedCategoryModel cmsFeedCategoryModel = findByPath(setMappingBean.getFrom(), treeModelx);

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

            boolean hasDefaultMain = flatten(treeModelx)
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

        treeModelx.setModifier(user.getUserName());
        cmsMtFeedCategoryTreeDao.update(treeModelx);

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

        flatten(cmsFeedCategoryModel.getChild()).forEach(c -> setChildMapping(c, mapping, userSessionBean));
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
    protected CmsFeedMappingModel findMapping(CmsFeedCategoryModel feedCategoryModel, Predicate<CmsFeedMappingModel> predicate) {
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
    protected CmsFeedCategoryModel findByPath(String path, CmsMtFeedCategoryTreeModelx treeModel) {

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

    /**
     * 自动继承 feedCategoryModel 类目父级类目的 Mapping 关系
     *
     * @param feedCategoryModel Feed 类目
     * @param user              变动用户
     * @return 更新后的 Mapping 关系
     */
    public List<CmsFeedMappingModel> extendsMapping(CmsFeedCategoryModel feedCategoryModel, UserSessionBean user) {

        CmsMtFeedCategoryTreeModelx treeModelx = cmsMtFeedCategoryTreeDao.selectFeedCategoryx(user.getSelChannelId());

        CmsFeedMappingModel feedMappingModel = findParentDefaultMapping(feedCategoryModel, treeModelx);

        if (feedMappingModel == null) return null;

        SetMappingBean setMappingBean = new SetMappingBean(feedCategoryModel.getPath(), feedMappingModel.getMainCategoryPath());

        return setMapping(setMappingBean, user);
    }

    /**
     * 从下往上在类目树中查找已匹配的 Mapping
     *
     * @param feedCategoryModel 当前类目节点
     * @param treeModel         完整的树
     * @return Mapping 对象
     */
    protected CmsFeedMappingModel findParentDefaultMapping(CmsFeedCategoryModel feedCategoryModel, CmsMtFeedCategoryTreeModelx treeModel) {

        // 当前类目没父级了.
        if (!feedCategoryModel.getPath().contains("-")) return null;

        String parentPath = feedCategoryModel.getPath().substring(0, feedCategoryModel.getPath().lastIndexOf("-"));

        CmsFeedCategoryModel parent = findByPath(parentPath, treeModel);

        CmsFeedMappingModel parentDefaultMapping = findMapping(parent, m -> m.getDefaultMapping() == 1);

        if (parentDefaultMapping == null) {
            return findParentDefaultMapping(parent, treeModel);
        }

        return parentDefaultMapping;
    }

    /**
     * 将类目和其子类目全部转化为扁平的数据流
     *
     * @param feedCategoryTreeModel Feed 类目树
     * @return 扁平化后的类目数据
     */
    public Stream<CmsFeedCategoryModel> flatten(CmsMtFeedCategoryTreeModelx feedCategoryTreeModel) {

        return feedCategoryTreeModel.getCategoryTree().stream().flatMap(this::flatten);
    }

    /**
     * 将类目和其子类目全部转化为扁平的数据流
     *
     * @param feedCategoryModels 多个类目
     * @return 扁平化后的类目数据
     */
    public Stream<CmsFeedCategoryModel> flatten(List<CmsFeedCategoryModel> feedCategoryModels) {

        return feedCategoryModels.stream().flatMap(this::flatten);
    }

    /**
     * 将类目和其子类目全部转化为扁平的数据流
     *
     * @param feedCategoryModel 某 Feed 类目
     * @return 扁平化后的类目数据
     */
    public Stream<CmsFeedCategoryModel> flatten(CmsFeedCategoryModel feedCategoryModel) {

        Stream<CmsFeedCategoryModel> feedCategoryModelStream = Stream.of(feedCategoryModel);

        List<CmsFeedCategoryModel> children = feedCategoryModel.getChild();

        if (children != null && children.size() > 0)
            feedCategoryModelStream = Stream.concat(feedCategoryModelStream, children.stream().flatMap(this::flatten));

        return feedCategoryModelStream;
    }
}
