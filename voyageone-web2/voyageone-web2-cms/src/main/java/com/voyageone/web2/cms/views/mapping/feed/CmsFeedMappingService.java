package com.voyageone.web2.cms.views.mapping.feed;

import com.mongodb.WriteResult;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.service.dao.cms.mongo.CmsMtFeedCategoryTreeDao;
import com.voyageone.service.impl.cms.CmsBtChannelCategoryService;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedMappingModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryTreeModelx;
import com.voyageone.service.model.cms.mongo.feed.mapping.Scope;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.setting.mapping.feed.FeedCategoryBean;
import com.voyageone.web2.cms.bean.setting.mapping.feed.SetMappingBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private com.voyageone.service.impl.cms.CmsFeedMappingService cmsFeedMappingService;

    public List<CmsMtFeedCategoryModel> getTopCategories(UserSessionBean user) {

        CmsMtFeedCategoryTreeModelx treeModelx = cmsMtFeedCategoryTreeDao.findTopCategories(user.getSelChannelId());

        return treeModelx.getCategoryTree();
    }

    public Map<String, FeedCategoryBean> getFeedCategoryMap(String topCategoryId, UserSessionBean user) {

        CmsMtFeedCategoryTreeModelx treeModelx = cmsMtFeedCategoryTreeDao.findTopCategory(user.getSelChannelId(), topCategoryId);

        if (treeModelx.getCategoryTree().isEmpty())
            throw new BusinessException("未找到类目");

        CmsMtFeedCategoryModel topCategory = treeModelx.getCategoryTree().get(0);

        // 查询 Mapping 信息

        String topCategoryPath = topCategory.getPath();

        List<CmsBtFeedMappingModel> feedMappingModels =
                cmsFeedMappingService.getMappingWithoutProps(user.getSelChannelId(), topCategoryPath);

        // 转 Map 供前台查询

        Map<String, CmsBtFeedMappingModel> feedMappingModelMap = new HashMap<>();

        for (CmsBtFeedMappingModel feedMappingModel : feedMappingModels)
            feedMappingModelMap.put(feedMappingModel.getScope().getFeedCategoryPath(), feedMappingModel);

        // 拍平, 转 Map, 供前台查询方便, 和显示方便

        final int[] seq = {0};

        Stream<FeedCategoryBean> feedCategoryBeanStream = buildFeedCategoryBean(topCategory, feedMappingModelMap);

        // 返回组装的结果

        return feedCategoryBeanStream
                .map(f -> {
                    f.setSeq(seq[0]);
                    seq[0]++;
                    return f;
                })
                .collect(toMap(f -> f.getModel().getPath(), f -> f));
    }

    private Stream<FeedCategoryBean> buildFeedCategoryBean(CmsMtFeedCategoryModel feedCategoryModel, Map<String, CmsBtFeedMappingModel> feedMappingModelMap) {

        // 先取出暂时保存
        List<CmsMtFeedCategoryModel> children = feedCategoryModel.getChild();

        // 创建新模型
        FeedCategoryBean feedCategoryBean = new FeedCategoryBean();
        feedCategoryBean.setLevel(StringUtils.countMatches(feedCategoryModel.getPath(), "-"));
        feedCategoryBean.setModel(feedCategoryModel);

        if (feedMappingModelMap.containsKey(feedCategoryModel.getPath()))
            feedCategoryBean.setMapping(feedMappingModelMap.get(feedCategoryModel.getPath()));

        // 输出流
        Stream<FeedCategoryBean> feedCategoryBeanStream = Stream.of(feedCategoryBean);
        // 如果有子,则继续输出子
        if (children != null && !children.isEmpty())
            feedCategoryBeanStream = Stream.concat(feedCategoryBeanStream,
                    children.stream().flatMap(m -> buildFeedCategoryBean(m, feedMappingModelMap)));

        // 减少 JSON 重复输出
        feedCategoryModel.setChild(null);
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
    public CmsBtFeedMappingModel setMapping(SetMappingBean setMappingBean, UserSessionBean user) {

        if (StringUtils.isAnyEmpty(setMappingBean.getFrom(), setMappingBean.getTo()))
            throw new BusinessException("木有参数");

        // 尝试查询 Mapping

        CmsBtFeedMappingModel defaultMapping =
                cmsFeedMappingService.getDefault(user.getSelChannel(), setMappingBean.getFrom());

        CmsBtFeedMappingModel defaultMainMapping =
                cmsFeedMappingService.getDefaultMain(user.getSelChannel(), setMappingBean.getTo());

        boolean canBeDefaultMain =
                cmsFeedMappingService.isCanBeDefaultMain(user.getSelChannel(), setMappingBean.getTo());

        if (defaultMapping != null) {

            if (!defaultMapping.getScope().getMainCategoryPath().equals(setMappingBean.getTo())) {
                defaultMapping.getScope().setMainCategoryPath(setMappingBean.getTo());
                defaultMapping.setDefaultMain(defaultMainMapping != null && canBeDefaultMain ? 0 : 1);
                defaultMapping.setMatchOver(0);
                defaultMapping.setProps(new ArrayList<>());

                cmsFeedMappingService.setMapping(defaultMapping);
            }

            return defaultMapping;
        }

        CmsBtFeedMappingModel mainCategoryMapping =
                cmsFeedMappingService.getMapping(user.getSelChannel(), setMappingBean.getFrom(), setMappingBean.getTo());

        if (mainCategoryMapping != null) {
            mainCategoryMapping.setDefaultMapping(1);
            cmsFeedMappingService.setMapping(defaultMainMapping);

            return mainCategoryMapping;
        }

        Scope scope = new Scope();
        scope.setChannelId(user.getSelChannelId());
        scope.setFeedCategoryPath(setMappingBean.getFrom());
        scope.setMainCategoryPath(setMappingBean.getTo());

        defaultMapping = new CmsBtFeedMappingModel();

        defaultMapping.setScope(scope);
        defaultMapping.setMatchOver(0);
        defaultMapping.setDefaultMapping(1);
        defaultMapping.setDefaultMain(defaultMainMapping != null && canBeDefaultMain ? 0 : 1);

        cmsFeedMappingService.setMapping(defaultMapping);

        return defaultMapping;
    }

    /**
     * 自动继承 feedCategoryModel 类目父级类目的 Mapping 关系
     *
     * @param feedCategoryModel Feed 类目
     * @param user              变动用户
     * @return 更新后的 Mapping 关系
     */
    public CmsBtFeedMappingModel extendsMapping(CmsMtFeedCategoryModel feedCategoryModel, UserSessionBean user) {

        CmsBtFeedMappingModel feedMappingModel = findParentDefaultMapping(user.getSelChannel(), feedCategoryModel.getPath());

        if (feedMappingModel == null) return null;

        SetMappingBean setMappingBean = new SetMappingBean(feedCategoryModel.getPath(), feedMappingModel.getScope().getMainCategoryPath());

        return setMapping(setMappingBean, user);
    }

    /**
     * 从下往上在类目树中查找已匹配的 Mapping
     */
    private CmsBtFeedMappingModel findParentDefaultMapping(ChannelConfigEnums.Channel channel, String feedCategoryPath) {

        // 当前类目没父级了.
        if (!feedCategoryPath.contains("-")) return null;

        String parentPath = feedCategoryPath.substring(0, feedCategoryPath.lastIndexOf("-"));

        CmsBtFeedMappingModel parentDefaultMapping = cmsFeedMappingService.getDefault(channel, parentPath, false);

        if (parentDefaultMapping == null)
            return findParentDefaultMapping(channel, parentPath);

        return parentDefaultMapping;
    }

    /**
     * 切换 MatchOver
     */
    public boolean switchMatchOver(SetMappingBean setMappingBean, UserSessionBean user) {

        CmsBtFeedMappingModel feedMappingModel = getMapping(setMappingBean, user);

        feedMappingModel.setMatchOver(feedMappingModel.getMatchOver() == 1 ? 0 : 1);

        WriteResult result = cmsFeedMappingService.setMapping(feedMappingModel);

        return result.getN() > 0;
    }

    public int getMatchOver(SetMappingBean setMappingBean, UserSessionBean user) {

        return getMapping(setMappingBean, user).getMatchOver();
    }

    /**
     * 获取 Default Mapping 或 DefaultMain Mapping
     *
     * @param setMappingBean 参数模型, isCommon 用于控制获取的是 Default 还是 DefaultMain
     * @param user           包含渠道的用户信息
     * @return Mapping 模型
     */
    protected CmsBtFeedMappingModel getMapping(SetMappingBean setMappingBean, UserSessionBean user) {

        CmsBtFeedMappingModel feedMappingModel = cmsFeedMappingService.getDefault(user.getSelChannel(),
                setMappingBean.getFrom());

        if (feedMappingModel == null)
            throw new BusinessException("没找到 Mapping");

        if (setMappingBean.isCommon())
            feedMappingModel = cmsFeedMappingService.getDefaultMain(user.getSelChannel(),
                    feedMappingModel.getScope().getMainCategoryPath());

        if (feedMappingModel == null)
            throw new BusinessException("没找到主类目 Mapping");

        return feedMappingModel;
    }
}
