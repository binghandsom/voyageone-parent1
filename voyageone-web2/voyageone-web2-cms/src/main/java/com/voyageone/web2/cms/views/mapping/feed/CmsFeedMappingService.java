package com.voyageone.web2.cms.views.mapping.feed;

import com.mongodb.WriteResult;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.service.impl.cms.ChannelCategoryService;
import com.voyageone.service.impl.cms.feed.FeedCategoryTreeService;
import com.voyageone.service.impl.cms.feed.FeedMappingService;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedMappingModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryTreeModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.setting.mapping.feed.FeedCategoryBean;
import com.voyageone.web2.cms.bean.setting.mapping.feed.SetMappingBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
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
    private FeedCategoryTreeService feedCategoryTreeService;

    @Autowired
    private ChannelCategoryService cmsBtChannelCategoryService;

    @Autowired
    private FeedMappingService feedMappingService;

    List<CmsMtFeedCategoryTreeModel> getTopCategories(UserSessionBean user) {
        return feedCategoryTreeService.getOnlyTopFeedCategories(user.getSelChannelId());
    }

    List<FeedCategoryBean> getFeedCategoryMap(String topCategoryId, UserSessionBean user) {

        CmsMtFeedCategoryTreeModel topCategory = feedCategoryTreeService.getFeedCategoryByCategoryId(user.getSelChannelId(), topCategoryId);

        if (topCategory == null)
            throw new BusinessException("未找到类目");

        // 查询 Mapping 信息
        // 当前渠道的所有所有所有 Mapping, 所以所有的 DefaultMain 都可以在这 List 里找到
        List<CmsBtFeedMappingModel> feedMappingModels = feedMappingService.getMappingWithoutProps(user.getSelChannelId());

        // 第一波, 先筛选出所有 DefaultMain
        Map<String, CmsBtFeedMappingModel> defaultMainMap = feedMappingModels.stream()
                .filter(i -> i.getDefaultMain() == 1)
                .collect(toMap(CmsBtFeedMappingModel::getMainCategoryPath, i -> i));

        Map<String, List<FeedCategoryBean.MappingBean>> mappingMap = feedMappingModels.stream()
                .map(i -> new FeedCategoryBean.MappingBean(i, defaultMainMap.get(i.getMainCategoryPath())))
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

        if (!param.isCommon()) {
            return feedMappingService.getMappingsWithoutProps(param.getFrom(), user.getSelChannelId());
        }

        List<CmsBtFeedMappingModel> mappings = new ArrayList<>();

        CmsBtFeedMappingModel feedMappingModel = feedMappingService.getMappingWithoutProps(user.getSelChannel(),new ObjectId(param.getMappingId()));

        mappings.add(feedMappingModel);

        return mappings;
    }

    /**
     * 自动继承 feedCategoryModel 类目父级类目的 Mapping 关系
     *
     * @param path Feed 类目路径
     * @param user 变动用户
     * @return 更新后的 Mapping 关系
     */
    CmsBtFeedMappingModel extendsMapping(String path, UserSessionBean user) {

        CmsBtFeedMappingModel feedMappingModel = findParentDefaultMapping(user.getSelChannel(), path);

        if (feedMappingModel == null) return null;

        SetMappingBean setMappingBean = new SetMappingBean(path, feedMappingModel.getMainCategoryPath());

        return feedMappingService.setMapping(setMappingBean.getFrom(), setMappingBean.getTo(), user.getSelChannel());
    }

    /**
     * 切换 MatchOver
     */
    boolean switchMatchOver(String mappingId, UserSessionBean user) {

        CmsBtFeedMappingModel feedMappingModel = feedMappingService.getMapping(user.getSelChannel(),new ObjectId(mappingId));

        feedMappingModel.setMatchOver(feedMappingModel.getMatchOver() == 1 ? 0 : 1);

        WriteResult result = feedMappingService.updateMapping(feedMappingModel);

        return result.getN() > 0;
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

    private void buildFeedCategoryBean(CmsMtFeedCategoryTreeModel feedCategoryModel, Map<String, List<FeedCategoryBean.MappingBean>> feedMappingModelMap, int[] seq, List<FeedCategoryBean> result) {

        FeedCategoryBean feedCategoryBean = new FeedCategoryBean();
        feedCategoryBean.setSeq(seq[0]++);
        feedCategoryBean.setLevel(StringUtils.countMatches(feedCategoryModel.getCatPath(), "-"));
        feedCategoryBean.setIsChild(feedCategoryModel.getIsParent() == 1?0:1);
        feedCategoryBean.setPath(feedCategoryModel.getCatPath());
        if (feedMappingModelMap.containsKey(feedCategoryModel.getCatPath())) {
            List<FeedCategoryBean.MappingBean> mappings = feedMappingModelMap.get(feedCategoryModel.getCatPath());
            feedCategoryBean.setMappings(mappings);
        }

        result.add(feedCategoryBean);

        for (CmsMtFeedCategoryTreeModel child : feedCategoryModel.getChildren())
            buildFeedCategoryBean(child, feedMappingModelMap, seq, result);
    }
}
