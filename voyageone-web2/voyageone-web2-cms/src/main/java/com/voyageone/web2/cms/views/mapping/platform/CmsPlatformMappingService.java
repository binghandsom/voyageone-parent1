package com.voyageone.web2.cms.views.mapping.platform;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.service.impl.cms.ChannelCategoryService;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.PlatformMappingDeprecatedService;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
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
class CmsPlatformMappingService extends BaseAppService {

    @Autowired
    private PlatformCategoryService platformCategoryService;

    @Autowired
    private PlatformMappingDeprecatedService platformMappingDeprecatedService;

    @Autowired
    private ChannelCategoryService channelCategoryService;

    List<TypeChannelBean> getCarts(UserSessionBean user, String lang) {
        return TypeChannels.getTypeListSkuCarts(user.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, lang);
    }

    /**
     * 获取拍平的叶子类目和类目Mapping对应主数据类目名称
     *
     * @param cartId 平台 ID
     * @param user   用户配置
     * @return Map, Key: categoryMap, mappings
     */
    Map<String, Object> getMainFinalCategoryMap(Integer cartId, UserSessionBean user) {

        // 拍平
        List<CmsMtCategoryTreeModel> treeModelMap =
                channelCategoryService.getCategoriesByChannelId(user.getSelChannelId())
                        .stream()
                        .flatMap(this::flattenFinal)
                        .collect(toList());

        // 获取所有渠道和平台下的 mapping 关系, 画面需要显示
        Map<String, CmsMtPlatformCategoryTreeModel> platformMap = getPlatformMap(user.getSelChannel(), cartId);

        Map<String, Object> mappings = platformMappingDeprecatedService.getMappings(user.getSelChannel().getId(), cartId)
                .stream()
                .filter(m -> platformMap.containsKey(m.getPlatformCategoryId()))
                .collect(toMap(
                        CmsMtPlatformMappingModel::getMainCategoryId,
                        m -> new HashMap<String, Object>() {{
                            put("matched", m.getMatchOver());
                            put("path", platformMap.get(m.getPlatformCategoryId()).getCatPath());
                        }}));

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
    List<Map<String, Object>> getOtherPlatformMapping(String mainCategoryId, UserSessionBean user) {

        // 20160506 tom 这个功能不需要, 删掉 START
//        return platformMappingService.getMappingsByMainCatId(user.getSelChannel().getId(), mainCategoryId)
//                .stream()
//                .map(m -> new HashMap<String, Object>(2) {{
//                    put("platform", m.getPlatformCartId());
//                    put("categoryPath", getPlatformPath(m));
//                }})
//                .collect(toList());

        return null;
        // 20160506 tom 这个功能不需要, 删掉 END
    }

    /**
     * 获取平台所有类目
     *
     * @param user   用户配置
     * @param cartId 平台 ID
     * @return 类目集合
     */
    List<CmsMtPlatformCategoryTreeModel> getPlatformCategories(UserSessionBean user, Integer cartId) {
        return platformCategoryService.getPlatformCategories(user.getSelChannelId(), cartId);
    }

    /**
     * 保存平台类目的 Mapping
     *
     * @param from   主数据类目 ID
     * @param to     平台类目 ID
     * @param cartId 平台 ID
     * @param user   用户配置
     * @return 新/老 mapping 的 matchOver
     */
    boolean setPlatformMapping(String from, String to, Integer cartId, UserSessionBean user) {

        // 检查下数据
        if (StringUtils.isEmpty(from)) {
            throw new BusinessException("木有参数");
        }

        // 取老数据
        CmsMtPlatformMappingModel platformMappingModel = platformMappingDeprecatedService.getMappingByMainCatId(user.getSelChannelId(), cartId, from);

        if (platformMappingModel == null) {
            // 如果没有, 那就新建数据
            platformMappingModel = new CmsMtPlatformMappingModel();
            platformMappingModel.setChannelId(user.getSelChannelId());
            platformMappingModel.setMainCategoryId(from);
            platformMappingModel.setPlatformCartId(cartId);

        } else if (platformMappingModel.getPlatformCategoryId().equals(to)) {
            // 如果有老数据
            // 老数据是不是和 to 一样, 一样就不用折腾了
            return platformMappingModel.getMatchOver() == 1;
        }

        // 重置老数据, 然后更改为 to 的新数据
        platformMappingModel.setPlatformCategoryId(to);
        platformMappingModel.setMatchOver(0);
        platformMappingModel.setProps(new ArrayList<>(0));

        platformMappingDeprecatedService.savePlatformMapping(platformMappingModel);

        return false;
    }

    /**
     * 查询平台类目的路径
     *
     * @param m CmsMtPlatformMappingModel 平台类目 Mapping 模型
     * @return 平台类目的路径
     */
    private String getPlatformPath(CmsMtPlatformMappingModel m) {
        CmsMtPlatformCategorySchemaModel platformCatSchemaModel = platformCategoryService.getPlatformCatSchema(m.getPlatformCategoryId(), m.getPlatformCartId());
        return platformCatSchemaModel == null ? "未找到平台类目" : platformCatSchemaModel.getCatFullPath();
    }

    /**
     * 获取所有叶子类目的路径
     *
     * @param channel 渠道
     * @param cartId  平台 ID
     * @return Map 键 -> CategoryId, 值 -> CategoryPath
     */
    private Map<String, CmsMtPlatformCategoryTreeModel> getPlatformMap(ChannelConfigEnums.Channel channel, Integer cartId) {

        // --> 取平台所有类目
        List<CmsMtPlatformCategoryTreeModel> platformCategoryTreeModels = platformCategoryService.getPlatformCategories(channel.getId(), cartId);

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
