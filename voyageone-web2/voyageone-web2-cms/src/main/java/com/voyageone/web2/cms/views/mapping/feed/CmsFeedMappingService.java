package com.voyageone.web2.cms.views.mapping.feed;

import com.mongodb.WriteResult;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.service.dao.cms.mongo.CmsMtFeedCategoryTreeDao;
import com.voyageone.service.impl.cms.ChannelCategoryService;
import com.voyageone.service.impl.cms.feed.FeedMappingService;
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
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

/**
 * @author Jonas, 12/8/15
 * @version 2.0.0
 */
@Service("web2.cms.CmsFeedMappingService")
public class CmsFeedMappingService extends BaseAppService {

    @Autowired
    private CmsMtFeedCategoryTreeDao cmsMtFeedCategoryTreeDao;

    @Autowired
    private ChannelCategoryService cmsBtChannelCategoryService;

    @Autowired
    private FeedMappingService feedMappingService;

    List<CmsMtFeedCategoryModel> getTopCategories(UserSessionBean user) {

        CmsMtFeedCategoryTreeModelx treeModelx = cmsMtFeedCategoryTreeDao.selectTopCategories(user.getSelChannelId());

        return treeModelx.getCategoryTree();
    }

    List<FeedCategoryBean> getFeedCategoryMap(String topCategoryId, UserSessionBean user) {

        CmsMtFeedCategoryTreeModelx treeModelx = cmsMtFeedCategoryTreeDao.selectTopCategory(user.getSelChannelId(), topCategoryId);

        if (treeModelx.getCategoryTree().isEmpty())
            throw new BusinessException("未找到类目");

        CmsMtFeedCategoryModel topCategory = treeModelx.getCategoryTree().get(0);

        // 查询 Mapping 信息
        // 当前渠道的所有所有所有 Mapping, 所以所有的 DefaultMain 都可以在这 List 里找到
        List<CmsBtFeedMappingModel> feedMappingModels = feedMappingService.getMappingWithoutProps(user.getSelChannelId());

        // 第一波, 先筛选出所有 DefaultMain
        Map<String, CmsBtFeedMappingModel> defaultMainMap = feedMappingModels.stream()
                .filter(i -> i.getDefaultMain() == 1)
                .collect(toMap(i -> i.getScope().getMainCategoryPath(), i -> i));

        Map<String, List<FeedCategoryBean.MappingBean>> mappingMap = feedMappingModels.stream()
                .map(i -> new FeedCategoryBean.MappingBean(i, defaultMainMap.get(i.getScope().getMainCategoryPath())))
                .collect(groupingBy(FeedCategoryBean.MappingBean::getFeedPath, toList()));

        // 拍平, 转 Map, 供前台查询方便, 和显示方便
        List<FeedCategoryBean> result = new ArrayList<>();
        buildFeedCategoryBean(topCategory, mappingMap, new int[]{0}, result);
        return result;
    }

    List<CmsMtCategoryTreeModel> getMainCategories(UserSessionBean user) {
        return cmsBtChannelCategoryService.getCategoriesByChannelId(user.getSelChannelId());
    }

    List<CmsBtFeedMappingModel> getMappings(SetMappingBean param, UserSessionBean user) {

        if (!param.isCommon())
            return feedMappingService.getMappingWithoutProps(param.getFrom(), user.getSelChannelId());

        CmsBtFeedMappingModel feedMappingModel = getMapping(param, user);

        List<CmsBtFeedMappingModel> mappings = new ArrayList<>();

        mappings.add(feedMappingModel);

        return mappings;
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
                feedMappingService.getDefault(user.getSelChannel(), setMappingBean.getFrom());

        CmsBtFeedMappingModel defaultMainMapping =
                feedMappingService.getDefaultMain(user.getSelChannel(), setMappingBean.getTo());

        boolean canBeDefaultMain =
                feedMappingService.isCanBeDefaultMain(user.getSelChannel(), setMappingBean.getFrom());

        // 如果当前 Feed 类目已经有了默认的 Mapping
        // 如果当前的默认 Mapping 就是这次给的主类目的话, 就不用继续了
        // 如果不是当前的主类目, 就清空属性的 Mapping, 更换主类目路径
        if (defaultMapping != null) {

            if (!defaultMapping.getScope().getMainCategoryPath().equals(setMappingBean.getTo())) {
                defaultMapping.getScope().setMainCategoryPath(setMappingBean.getTo());
                defaultMapping.setDefaultMain(defaultMainMapping == null && canBeDefaultMain ? 1 : 0);
                defaultMapping.setMatchOver(0);
                defaultMapping.setProps(new ArrayList<>());

                feedMappingService.setMapping(defaultMapping);
            }

            return defaultMapping;
        }

        // 如果当前 Feed 类目已经 Mapping 到这个主类目, 只是不是默认 Mapping, 则只更新默认标识位
        CmsBtFeedMappingModel mainCategoryMapping =
                feedMappingService.getMapping(user.getSelChannel(), setMappingBean.getFrom(), setMappingBean.getTo());

        if (mainCategoryMapping != null) {
            mainCategoryMapping.setDefaultMapping(1);
            feedMappingService.setMapping(defaultMainMapping);

            return mainCategoryMapping;
        }

        // 如果上面的全部不成立, 就需要全新创建 Mapping
        Scope scope = new Scope();
        scope.setChannelId(user.getSelChannelId());
        scope.setFeedCategoryPath(setMappingBean.getFrom());
        scope.setMainCategoryPath(setMappingBean.getTo());

        defaultMapping = new CmsBtFeedMappingModel();

        defaultMapping.setScope(scope);
        defaultMapping.setMatchOver(0);
        defaultMapping.setDefaultMapping(1);
        defaultMapping.setDefaultMain(defaultMainMapping == null && canBeDefaultMain ? 1 : 0);

        feedMappingService.setMapping(defaultMapping);

        return defaultMapping;
    }

    /**
     * 自动继承 feedCategoryModel 类目父级类目的 Mapping 关系
     *
     * @param feedCategoryModel Feed 类目
     * @param user              变动用户
     * @return 更新后的 Mapping 关系
     */
    CmsBtFeedMappingModel extendsMapping(CmsMtFeedCategoryModel feedCategoryModel, UserSessionBean user) {

        CmsBtFeedMappingModel feedMappingModel = findParentDefaultMapping(user.getSelChannel(), feedCategoryModel.getPath());

        if (feedMappingModel == null) return null;

        SetMappingBean setMappingBean = new SetMappingBean(feedCategoryModel.getPath(), feedMappingModel.getScope().getMainCategoryPath());

        return setMapping(setMappingBean, user);
    }

    /**
     * 切换 MatchOver
     */
    boolean switchMatchOver(SetMappingBean setMappingBean, UserSessionBean user) {

        CmsBtFeedMappingModel feedMappingModel = getMapping(setMappingBean, user);

        feedMappingModel.setMatchOver(feedMappingModel.getMatchOver() == 1 ? 0 : 1);

        WriteResult result = feedMappingService.setMapping(feedMappingModel);

        return result.getN() > 0;
    }

    /**
     * 获取 Default Mapping 或 DefaultMain Mapping
     *
     * @param setMappingBean 参数模型, isCommon 用于控制获取的是 Default 还是 DefaultMain
     * @param user           包含渠道的用户信息
     * @return Mapping 模型
     */
    private CmsBtFeedMappingModel getMapping(SetMappingBean setMappingBean, UserSessionBean user) {

        CmsBtFeedMappingModel feedMappingModel = feedMappingService.getDefault(user.getSelChannel(),
                setMappingBean.getFrom());

        if (feedMappingModel == null)
            throw new BusinessException("没找到 Mapping");

        if (setMappingBean.isCommon())
            feedMappingModel = feedMappingService.getDefaultMain(user.getSelChannel(),
                    feedMappingModel.getScope().getMainCategoryPath());

        if (feedMappingModel == null)
            throw new BusinessException("没找到主类目 Mapping");

        return feedMappingModel;
    }

    /**
     * 根据 params 查找其主数据类目的默认 mapping, 不包含属性 Mapping 信息
     *
     * @param params 带有 to (主数据类目路径) 的参数模型
     * @param user   带有 selChannel 的用户信息
     * @return mapping 模型
     */
    CmsBtFeedMappingModel getMainMapping(SetMappingBean params, UserSessionBean user) {
        CmsBtFeedMappingModel model = feedMappingService.getDefaultMain(user.getSelChannel(), params.getTo());
        if (model == null)
            return null;
        model.setProps(null); // 减小 Json 大小
        return model;
    }

    /**
     * 从下往上在类目树中查找已匹配的 Mapping
     */
    private CmsBtFeedMappingModel findParentDefaultMapping(ChannelConfigEnums.Channel channel, String feedCategoryPath) {

        // 当前类目没父级了.
        if (!feedCategoryPath.contains("-")) return null;

        String parentPath = feedCategoryPath.substring(0, feedCategoryPath.lastIndexOf("-"));

        CmsBtFeedMappingModel parentDefaultMapping = feedMappingService.getDefault(channel, parentPath, false);

        if (parentDefaultMapping == null)
            return findParentDefaultMapping(channel, parentPath);

        return parentDefaultMapping;
    }

    private void buildFeedCategoryBean(CmsMtFeedCategoryModel feedCategoryModel, Map<String, List<FeedCategoryBean.MappingBean>> feedMappingModelMap, int[] seq, List<FeedCategoryBean> result) {

        FeedCategoryBean feedCategoryBean = new FeedCategoryBean();
        feedCategoryBean.setSeq(seq[0]++);
        feedCategoryBean.setLevel(StringUtils.countMatches(feedCategoryModel.getPath(), "-"));
        feedCategoryBean.setIsChild(feedCategoryModel.getIsChild());
        feedCategoryBean.setPath(feedCategoryModel.getPath());
        if (feedMappingModelMap.containsKey(feedCategoryModel.getPath())) {
            List<FeedCategoryBean.MappingBean> mappings = feedMappingModelMap.get(feedCategoryModel.getPath());
            feedCategoryBean.setMappings(mappings);
        }

        result.add(feedCategoryBean);

        for (CmsMtFeedCategoryModel child : feedCategoryModel.getChild())
            buildFeedCategoryBean(child, feedMappingModelMap, seq, result);
    }
}
