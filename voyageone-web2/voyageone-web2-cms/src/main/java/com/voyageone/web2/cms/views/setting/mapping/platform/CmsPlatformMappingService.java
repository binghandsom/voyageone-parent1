package com.voyageone.web2.cms.views.setting.mapping.platform;

import com.voyageone.cms.service.CmsBtChannelCategoryService;
import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformCategoryDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformMappingDao;
import com.voyageone.cms.service.model.CmsMtCategoryTreeModel;
import com.voyageone.cms.service.model.CmsMtPlatformCategoryTreeModel;
import com.voyageone.cms.service.model.CmsMtPlatformMappingModel;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * @author Jonas, 1/11/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service("web2.cms.CmsPlatformMappingService")
public class CmsPlatformMappingService extends BaseAppService {

    @Autowired
    private CmsMtPlatformCategoryDao platformCategoryDao;

    @Autowired
    private CmsMtPlatformMappingDao platformMappingDao;

    @Autowired
    private CmsBtChannelCategoryService cmsBtChannelCategoryService;

    /**
     * 获取拍平的叶子类目和类目Mapping对应主数据类目名称
     *
     * @param cartId 平台 ID
     * @param user   用户配置
     * @return Map, Key: categoryMap, mappings
     */
    public Map<String, Object> getMainFinalCategoryMap(Integer cartId, UserSessionBean user) {

        // 拍平

        List<CmsMtCategoryTreeModel> treeModelMap =
                cmsBtChannelCategoryService.getCategoriesByChannelId(user.getSelChannelId())
                        .stream()
                        .flatMap(this::flattenFinal)
                        .collect(toList());

        // 获取所有渠道和平台下的 mapping 关系, 画面需要显示

        Map<String, CmsMtPlatformCategoryTreeModel> platformMap = getPlatformMap(user.getSelChannel(), cartId);

        Map<String, String> mappings = platformMappingDao.selectMappings(user.getSelChannel(), cartId)
                .stream()
                .filter(m -> !platformMap.containsKey(m.getPlatformCategoryId()))
                .collect(toMap(
                        CmsMtPlatformMappingModel::getMainCategoryId,
                        m -> platformMap.get(m.getPlatformCategoryId()).getCatPath()));

        return new HashMap<String, Object>() {{
            put("categories", treeModelMap);
            put("mappings", mappings);
        }};
    }

    /**
     * 获取主数据类目在其他平台的关联数据
     *
     * @param mainCategoryId 主数据类目 ID
     * @param user           用户配置
     * @return Map 集合, 键 --> cartId, 值 --> platform category path
     */
    public List<Map<String, Object>> getOtherPlatformMapping(String mainCategoryId, UserSessionBean user) {

        return platformMappingDao.getMappingByMainCatId(user.getSelChannel(), mainCategoryId)
                .stream()
                .map(m -> new HashMap<String, Object>(2) {{
                    put("platform", m.getPlatformCartId());
                    put("categoryPath", getPlatformPath(m));
                }})
                .collect(toList());
    }

    /**
     * 获取平台所有类目
     *
     * @param user   用户配置
     * @param cartId 平台 ID
     * @return 类目集合
     */
    public List<CmsMtPlatformCategoryTreeModel> getPlatformCategories(UserSessionBean user, Integer cartId) {

        return platformCategoryDao.selectByChannel_CartId(user.getSelChannelId(), cartId);
    }

    /**
     * 查询平台类目的路径
     *
     * @param m CmsMtPlatformMappingModel 平台类目 Mapping 模型
     * @return 平台类目的路径
     */
    private String getPlatformPath(CmsMtPlatformMappingModel m) {

        CmsMtPlatformCategoryTreeModel platformCategoryTreeModel = platformCategoryDao.selectByChannel_CartId_CatId(
                m.getChannelId(),
                m.getPlatformCartId(),
                m.getPlatformCategoryId());

        return platformCategoryTreeModel == null ? "未找到平台类目" : platformCategoryTreeModel.getCatPath();
    }

    /**
     * 获取所有叶子类目的路径
     *
     * @param channel 渠道
     * @param cartId  平台 ID
     * @return Map 键 -> CategoryId, 值 -> CategoryPath
     */
    protected Map<String, CmsMtPlatformCategoryTreeModel> getPlatformMap(ChannelConfigEnums.Channel channel, Integer cartId) {

        // --> 取平台所有类目
        List<CmsMtPlatformCategoryTreeModel> platformCategoryTreeModels =
                platformCategoryDao.selectByChannel_CartId(channel.getId(), cartId);

        // --> 所有平台类目 --> 取所有叶子 --> 拍平
        Stream<CmsMtPlatformCategoryTreeModel> platformCategoryTreeModelStream =
                platformCategoryTreeModels.stream().flatMap(this::flattenFinal);

        // --> 所有平台类目 --> 取所有叶子 --> 拍平 --> 转 Map, id 为键, path 为值
        return platformCategoryTreeModelStream
                .collect(toMap(
                        CmsMtPlatformCategoryTreeModel::getCatId,
                        model -> model));
    }

    /**
     * 拍平叶子类目,不包含父级
     *
     * @param platformCategoryTreeModel 平台类目模型
     * @return 叶子类目数据流
     */
    private Stream<CmsMtPlatformCategoryTreeModel> flattenFinal(CmsMtPlatformCategoryTreeModel platformCategoryTreeModel) {

        if (platformCategoryTreeModel.getIsParent() == 0)
            return Stream.of(platformCategoryTreeModel);

        List<CmsMtPlatformCategoryTreeModel> children = platformCategoryTreeModel.getChildren();

        if (children == null) children = new ArrayList<>(0);

        return children.stream().flatMap(this::flattenFinal);
    }

    /**
     * 拍平主数据类目,不包含父级
     *
     * @param categoryTreeModel 主数据类目模型
     * @return 叶子类目数据流
     */
    private Stream<CmsMtCategoryTreeModel> flattenFinal(CmsMtCategoryTreeModel categoryTreeModel) {

        if (categoryTreeModel.getIsParent() == 0)
            return Stream.of(categoryTreeModel);

        List<CmsMtCategoryTreeModel> children = categoryTreeModel.getChildren();

        if (children == null) children = new ArrayList<>(0);

        return children.stream().flatMap(this::flattenFinal);
    }
}
