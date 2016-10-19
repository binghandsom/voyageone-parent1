package com.voyageone.web2.cms.views.product;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductStatusHistoryService;
import com.voyageone.service.impl.cms.promotion.PromotionCodeService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.web2.base.BaseViewService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 移动Code和移动Sku
 * @author jeff.duan
 * @version 2.3.0
 * @since 2.3.0
 */
@Service
public class CmsProductMoveService extends BaseViewService {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductGroupService productGroupService;

    @Autowired
    private PromotionCodeService promotionCodeService;

    @Autowired
    private MongoSequenceService mongoSequenceService;

    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;

    /**********************************************************/
    /****************移动Code到Group***************************/
    /**********************************************************/

    /**
     * 移动Code-初始化前Check
     */
    public void moveCodeInitCheck(Map<String, Object> params, String channelId, String lang) {
        // 移动的Code
        String productCode = (String) params.get("productCode");
        // 相关的平台id
        Integer cartId = params.get("cartId") != null ? new Integer(String.valueOf(params.get("cartId"))) : null;
        // 相关的平台名称
        String cartName = (String) params.get("cartName");

        // 取得Group信息
        CmsBtProductGroupModel sourceGroupModel = productGroupService.selectProductGroupByCode(channelId, productCode, cartId);

        // 取得移动的Code的Product信息
        CmsBtProductModel productModel = productService.getProductByCode(channelId, productCode);

        // check移动信息是否匹配（源Group下是否包含移动的Code，源Group是否存在）
        if (sourceGroupModel == null
                || productModel == null
                || !sourceGroupModel.getProductCodes().contains(productCode)) {
            // 移动的数据不整合，请重新刷新画面
            throw new BusinessException("7000100");
        }

        // check业务上的移动条件是否满足
        // CheckGroup中如果包含多了Code，并且这个Code是主商品，那么不可以移动。（提示请切换其他商品为主商品后才可以移动）
        if (!checkIsMainProduct(sourceGroupModel, productCode)) {
            // 移动的Code在" + cartName + "平台下是主商品，请先切换其他商品为主商品后再进行移动Code操作
            throw new BusinessException("7000101", new Object[]{cartName});
        }

        // Check这个Code是不是Approved的状态，如果是的话，提示先下线。
        if (!checkCodeStatus(productModel, cartId)) {
            // 移动Code的状态是Approved，请先下线后再进行移动Code操作
            throw new BusinessException("7000102");
        }

        // Check这个Code是否存在于没有结束的活动中。
        String promotionNames = promotionCodeService.getExistCodeInActivePromotion(channelId, productCode, cartId);
        if (!StringUtil.isEmpty(promotionNames)) {
            //移动Code存在于没有结束的活动:" + promotionNames + "中，请从活动中移除，或者等活动结束后再进行移动Code操作
            throw new BusinessException("7000103", new Object[]{promotionNames});
        }

        // Check这个Code是否是锁定的状态。
        if (!checkCodeLocked(productModel)) {
            // 移动Code处于锁定的状态，请先解锁后再进行移动Code操作
            throw new BusinessException("7000104");
        }
    }

    /**
     * 移动Code-初始化
     */
    public Map<String, Object> moveCodeInit(Map<String, Object> params, String channelId) {

        Map<String, Object> returnMap = new HashMap<>();

        // 移动的Code
        String productCode = (String) params.get("productCode");
        // 相关的平台id
        Integer cartId = params.get("cartId") != null ? new Integer(String.valueOf(params.get("cartId"))) : null;

        // 取得Group信息
        CmsBtProductGroupModel groupModel = productGroupService.selectProductGroupByCode(channelId, productCode, cartId);

        // 取得Group下主产品的Product信息
        Long moveSourceGroupId = null;
        String moveSourceGroupName = "";
        if (groupModel != null) {
            moveSourceGroupId = groupModel.getGroupId();
            CmsBtProductModel productModel = productService.getProductByCode(channelId, groupModel.getMainProductCode());
            if (productModel != null) {
                moveSourceGroupName = productModel.getCommon().getFields().getOriginalTitleCn();
                if (StringUtils.isEmpty(moveSourceGroupName)) {
                    moveSourceGroupName = productModel.getCommon().getFields().getProductNameEn();
                }
            }
        }
        returnMap.put("sourceGroupId", moveSourceGroupId);
        returnMap.put("sourceGroupName", moveSourceGroupName);
        returnMap.put("sourceGroupProductsNum", groupModel == null ? 0 : groupModel.getProductCodes().size());
        return returnMap;
    }

    /**
     * 移动Code-根据Code检索Group信息
     */
    public Map<String, Object> moveCodeSearch(Map<String, Object> params, String channelId, String lang) {

        Map<String, Object> returnMap = new HashMap<>();
        // 查询的Group列表
        List<Map<String, Object>> groupList = new ArrayList<>();
        // 检索的Code
        String searchCode = (String) params.get("searchCode");
        // 相关的平台id
        Integer cartId = params.get("cartId") != null ? new Integer(String.valueOf(params.get("cartId"))) : null;
        // 移动源GroupId
        Long sourceGroupId = params.get("sourceGroupId") != null ? new Long(String.valueOf(params.get("sourceGroupId"))) : null;

        // Code如果不输入，那么没有检索结果
        if (StringUtils.isEmpty(searchCode)) {
            returnMap.put("groupList", groupList);
            return returnMap;
        }

        // 根据输入的Code和对应的CarId检索出对应的Group
        CmsBtProductGroupModel groupModel = productGroupService.selectProductGroupByCode(channelId, searchCode, cartId);
        if (groupModel != null) {
            Map<String, Object> groupInfo = new HashMap<>();
            groupInfo.put("groupInfo", groupModel);
            // 取得Group的主商品的Product信息
            CmsBtProductModel productModel = productService.getProductByCode(channelId, groupModel.getMainProductCode());
            if (productModel != null) {
                groupInfo.put("mainProductInfo", productModel);
            }
            groupList.add(groupInfo);
        }

        // 去除自己那条
        groupList = groupList.stream().filter(group->(!sourceGroupId.equals(((CmsBtProductGroupModel)group.get("groupInfo")).getGroupId()))).collect(Collectors.toList());

        returnMap.put("groupList", groupList);
        return returnMap;
    }

    /**
     * 移动Code-根据移动Code的源Group/目标Group取得移动前/移动后的Preview
     */
    public Map<String, Object> moveCodePreview(Map<String, Object> params, String channelId, String lang) {
        Map<String, Object> previewInfo = new HashMap<>();

        // 取得移动需要的信息
        // 目标Group类型 "new" or "select"
        String destGroupType = (String) params.get("destGroupType");
        // 移动的Code
        String productCode = (String) params.get("productCode");
        // 移动源GroupId
        Long sourceGroupId = params.get("sourceGroupId") != null ? new Long(String.valueOf(params.get("sourceGroupId"))) : null;
        // 移动目标GroupId
        Long destGroupId = params.get("destGroupId") != null ? new Long(String.valueOf(params.get("destGroupId"))) : null;
        // 移动源Group的主商品的名称
        String sourceGroupName = (String) params.get("sourceGroupName");
        // 移动目标Group的主商品的名称
        String destGroupName = (String) params.get("destGroupName");


        // 如果没有选择移动目的Group，出错
        if (StringUtils.isEmpty(destGroupType) || ("select".equals(destGroupType) && destGroupId == null)) {
            // 请选择移动目的Group
            throw new BusinessException("7000105");
        }

        // 取得源Group信息
        CmsBtProductGroupModel sourceGroupModel = productGroupService.getProductGroupByGroupId(channelId, sourceGroupId);
        // 取得目标Group信息
        CmsBtProductGroupModel destGroupModel = null;
        if ("select".equals(destGroupType)) {
            destGroupModel = productGroupService.getProductGroupByGroupId(channelId, destGroupId);
        }

        // 移动前-源Group信息
        previewInfo.put("sourceGroupInfoBefore", sourceGroupName);

        // 移动前-源Code信息
        List<Map<String, Object>> sourceCodeListBefore = new ArrayList<>();
        if (sourceGroupModel != null) {
            for (String code : sourceGroupModel.getProductCodes()) {
                Map<String, Object> sourceCodeInfoMapBefore = new HashMap<>();
                sourceCodeInfoMapBefore.put("code", code);
                // 如果这个Code是当前移动的Code,那么就高亮显示
                if (code.equals(productCode)) {
                    sourceCodeInfoMapBefore.put("current", true);
                }
                sourceCodeListBefore.add(sourceCodeInfoMapBefore);
            }
        }
        previewInfo.put("sourceCodeInfoBefore", sourceCodeListBefore);

        // 移动前-目标Group信息
        // 如果选择"移动到新的Group"，那么移动前-目标Group信息是没有的
        if ("new".equals(destGroupType)) {
            previewInfo.put("destGroupInfoBefore", "");
        } else {
            previewInfo.put("destGroupInfoBefore", destGroupName);
        }

        // 移动前-目标Code信息
        List<Map<String, Object>> destCodeListBefore = new ArrayList<>();
        // 如果选择"移动到新的Group"，那么移动前-目标Code信息是没有的
        if ("select".equals(destGroupType)) {
            if (destGroupModel != null) {
                for (String code : destGroupModel.getProductCodes()) {
                    Map<String, Object> destCodeInfoMapBefore = new HashMap<>();
                    destCodeInfoMapBefore.put("code", code);
                    destCodeListBefore.add(destCodeInfoMapBefore);
                }
            }
        }
        previewInfo.put("destCodeInfoBefore", destCodeListBefore);

        // 移动后-源Group信息
        previewInfo.put("sourceGroupInfoAfter", sourceGroupName);
        // 如果移动前-源Code个数为1，那么相当于移动后删除这个Group，那么就要加上删除线
        if (sourceCodeListBefore.size() == 1) {
            previewInfo.put("sourceGroupInfoAfterDeleted", true);
        }

        // 移动后-源Code信息
        List<Map<String, Object>> sourceCodeListAfter = new ArrayList<>();
        // 如果移动前-源Code个数为1，那么相当于移动后删除这个Group-Code，那么就要加上删除线
        if (sourceCodeListBefore.size() == 1) {
            Map<String, Object> sourceCodeInfoMapAfter = new HashMap<>();
            sourceCodeInfoMapAfter.put("code", (String) sourceCodeListBefore.get(0).get("code"));
            sourceCodeInfoMapAfter.put("current", true);
            sourceCodeInfoMapAfter.put("deleted", true);
            sourceCodeListAfter.add(sourceCodeInfoMapAfter);
        } else {
            if (sourceGroupModel != null) {
                for (String code : sourceGroupModel.getProductCodes()) {
                    // 去除当前移动的Code
                    if (!code.equals(productCode)) {
                        Map<String, Object> sourceCodeInfoMapAfter = new HashMap<>();
                        sourceCodeInfoMapAfter.put("code", code);
                        sourceCodeListAfter.add(sourceCodeInfoMapAfter);
                    }
                }
            }
        }
        previewInfo.put("sourceCodeInfoAfter", sourceCodeListAfter);

        // 移动后-目标Group信息
        if ("new".equals(destGroupType)) {
            previewInfo.put("destGroupInfoAfter", "");
        } else {
            previewInfo.put("destGroupInfoAfter", destGroupName);
        }

        // 移动后-目标Code信息
        List<Map<String, Object>> destCodeListAfter = new ArrayList<>();
        // 如果选择"移动到新的Group"，那么移动前-目标Code信息是没有的
        if ("new".equals(destGroupType)) {
            // 加入移动的Code，并且高亮显示
            Map<String, Object> destCodeInfoMapAfter = new HashMap<>();
            destCodeInfoMapAfter.put("code", productCode);
            destCodeInfoMapAfter.put("current", true);
            destCodeListAfter.add(destCodeInfoMapAfter);
        } else {
            if (destGroupModel != null) {
                // 加入原来目标Group下的Code
                for (String code : destGroupModel.getProductCodes()) {
                    Map<String, Object> destCodeInfoMapAfter = new HashMap<>();
                    destCodeInfoMapAfter.put("code", code);
                    destCodeListAfter.add(destCodeInfoMapAfter);
                }                // 再加入移动的Code，并且高亮显示
                Map<String, Object> destCodeInfoMapAfter = new HashMap<>();
                destCodeInfoMapAfter.put("code", productCode);
                destCodeInfoMapAfter.put("current", true);
                destCodeListAfter.add(destCodeInfoMapAfter);
            }
        }
        previewInfo.put("destCodeInfoAfter", destCodeListAfter);

        return previewInfo;
    }

    /**
     * 移动Code-从一个Group到另外一个Group
     */
    @VOTransactional
    public void moveCode(Map<String, Object> params, String channelId, String modifier, String lang) {
        // 取得移动需要的信息
        // 目标Group类型 "new" or "select"
        String destGroupType = (String) params.get("destGroupType");
        // 移动的Code
        String productCode = (String) params.get("productCode");
        // 移动源GroupId
        Long sourceGroupId = params.get("sourceGroupId") != null ? new Long(String.valueOf(params.get("sourceGroupId"))) : null;
        // 移动目标GroupId
        Long destGroupId = params.get("destGroupId") != null ? new Long(String.valueOf(params.get("destGroupId"))) : null;
        // 相关的平台id
        Integer cartId = params.get("cartId") != null ? new Integer(String.valueOf(params.get("cartId"))) : null;
        // 相关的平台名称
        String cartName = (String) params.get("cartName");

        // 如果没有选择移动目的Group，出错
        if (StringUtils.isEmpty(destGroupType) || ("select".equals(destGroupType) && destGroupId == null)) {
            // 请选择移动目的Group
            throw new BusinessException("7000105");
        }

        // 取得源Group信息
        CmsBtProductGroupModel sourceGroupModel = productGroupService.getProductGroupByGroupId(channelId, sourceGroupId);

        // 取得目标Group信息
        CmsBtProductGroupModel destGroupModel = null;
        if ("select".equals(destGroupType)) {
            destGroupModel = productGroupService.getProductGroupByGroupId(channelId, destGroupId);
        }

        // 取得移动的Code的Product信息
        CmsBtProductModel productModel = productService.getProductByCode(channelId, productCode);

        // check移动信息是否匹配（源Group下是否包含移动的Code，源Group和目标Group是否存在）
        if (cartId == null
                || sourceGroupModel == null
                || productModel == null
                || !sourceGroupModel.getProductCodes().contains(productCode)) {
            // 移动的数据不整合，移动Code失败
            throw new BusinessException("7000106");
        }

        // check业务上的移动条件是否满足
        // Check这个Code是否是锁定的状态。
        if (!checkCodeLocked(productModel)) {
            // 移动Code处于锁定的状态，请先解锁后再进行移动Code操作
            throw new BusinessException("7000104");
        }

        // Check这个Code的源Group下是否只有一个Code，并且目的Group是新建的，那么不可以移动。
        if ("new".equals(destGroupType) && sourceGroupModel.getProductCodes().size() == 1) {
            // 不能移动Code到新Group,因为源Group下是否只有一个Code
            throw new BusinessException("7000107");
        }

        // CheckGroup中如果包含多了Code，并且这个Code是主商品，那么不可以移动。（提示请切换其他商品为主商品后才可以移动）
        if (!checkIsMainProduct(sourceGroupModel, productCode)) {
            // 移动的Code在" + cartName + "平台下是主商品，请先切换其他商品为主商品后再进行移动Code操作
            throw new BusinessException("7000101", new Object[]{cartName});
        }

        // Check这个Code是不是Approved的状态，如果是的话，提示先下线。
        if (!checkCodeStatus(productModel, cartId)) {
            // 移动Code的状态是Approved，请先下线后再进行移动Code操作
            throw new BusinessException("7000102");
        }

        // Check这个Code是否存在于没有结束的活动中。
        String promotionNames = promotionCodeService.getExistCodeInActivePromotion(channelId, productCode, cartId);
        if (!StringUtil.isEmpty(promotionNames)) {
            // 移动Code存在于没有结束的活动:" + promotionNames + "中，请从活动中移除，或者等活动结束后再进行移动Code操作
            throw new BusinessException("7000103", new Object[]{promotionNames});
        }


        // ************这里开始正式移动*************
        // 处理目标Group
        // 目的Group如果不是一个新的Group，那么目的Group中加入这个Code（Group的productCodes中加入移动的Code），然后重算目的group价格区间。
        if ("select".equals(destGroupType)) {
            // 目的Group中加入这个Code
            destGroupModel.getProductCodes().add(productCode);
            // 重算目的group价格区间
            productGroupService.calculatePriceRange(destGroupModel);
            // 更新目标Group
            productGroupService.update(destGroupModel);

            // 设定移动Code的Product信息的相关平台下pIsMain=0
            productService.updateProductPlatformIsMain(0, destGroupModel.getMainProductCode(), channelId, productCode, cartId, modifier);
        } else {
            // 目的Group如果是一个新的Group，那么建立一个新的Group（Group的mainProductCode=移动的Code，productCodes中就一个元素：移动的Code）
            destGroupModel = createNewGroup(channelId, cartId, productCode);
            productGroupService.insert(destGroupModel);

            // 设定移动Code的Product信息的相关平台下pIsMain=1
            productService.updateProductPlatformIsMain(1, productCode, channelId, productCode, cartId, modifier);
        }

        // 处理源Group
        // 移动Code如果是源Group的最后一个Code，那么删除源Group信息
        if (sourceGroupModel.getProductCodes().size() == 1) {
            productGroupService.deleteGroup(sourceGroupModel);
        } else {
            // 移动Code如果不是源Group的最后一个Code，那么把这个Code从源Group信息中移除，然后重算源group价格区间。
            sourceGroupModel.setProductCodes(sourceGroupModel.getProductCodes().stream().filter(code->!code.equals(productCode)).collect(Collectors.toList()));
            productGroupService.calculatePriceRange(sourceGroupModel);
            productGroupService.update(sourceGroupModel);
        }

        // 插入商品操作履历
        productStatusHistoryService.insert(channelId, productCode,
                productModel.getPlatform(cartId).getStatus(), cartId, EnumProductOperationType.MoveCode,
                "从Group:" + sourceGroupModel.getGroupId() + "移动到Group:" + destGroupModel.getGroupId(), modifier);
    }


    /**********************************************************/
    /****************移动Sku到Code*****************************/
    /**********************************************************/
    /**
     * 移动Sku-初始化前Check
     */
    public void moveSkuInitCheck(Map<String, Object> params, String channelId, String lang) {
        // 移动的Sku列表
        List<Map<String, Object>> skuList = (List) params.get("skuList");
        // 源Code
        String sourceCode = (String) params.get("sourceCode");

        // 取得源Code的Product信息
        CmsBtProductModel sourceProductModel = productService.getProductByCode(channelId, sourceCode);

        // check移动信息是否匹配（是否存在选择的Sku列表，源Code是否存在, 源Code是否存在包含选择的Sku列表）
        if (skuList == null || sourceProductModel == null || !checkSkusInCode(sourceProductModel, skuList)) {
            // 移动的数据不整合，请重新刷新画面
            throw new BusinessException("7000100");
        }

        // check这个Code下Sku是否一个都没有被选择
        if (!checkNoSelectSku(skuList)) {
            // 请选择要移动的Sku
            throw new BusinessException("7000108");
        }

        //  check是否选择了这个Code下的所有Sku
        if (!checkSelectAllSkus(skuList)) {
            // 不能移动Code下的所有Sku,如果需要移动所有Sku,请到平台Tab页进行"移动到其他Group"操作
            throw new BusinessException("7000109");
        }

        // Check这个Code是否存在于没有结束的活动中。
        String promotionNames = promotionCodeService.getExistCodeInActivePromotion(channelId, sourceCode, null);
        if (!StringUtil.isEmpty(promotionNames)) {
            // 处理Code存在于没有结束的活动:" + promotionNames + "中，请从活动中移除，或者等活动结束后再进行移动Sku操作
            throw new BusinessException("7000110", new Object[]{promotionNames});
        }

        // Check这个Code是否是锁定的状态。
        if (!checkCodeLocked(sourceProductModel)) {
            // 处理Code正处于锁定的状态，请先解锁后再进行移动Sku操作
            throw new BusinessException("7000111");
        }
    }

    /**
     * 移动Sku-初始化
     */
    public Map<String, Object> moveSkuInit(Map<String, Object> params, String channelId) {

        Map<String, Object> returnMap = new HashMap<>();

        // 源Code
        String sourceCode = (String) params.get("sourceCode");

        // 取得源Code的Product信息
        CmsBtProductModel sourceProductModel = productService.getProductByCode(channelId, sourceCode);

        // 取得Code下是否包含聚美平台
        if (sourceProductModel != null) {
            for (Map.Entry<String, CmsBtProductModel_Platform_Cart> platform : sourceProductModel.getPlatforms().entrySet()) {
                if (platform.getValue().getCartId().equals(27)) {
                    returnMap.put("includeJM", true);
                    return returnMap;
                }
            }
        }
        returnMap.put("includeJM", false);
        return returnMap;
    }

    /**
     * 移动Sku-根据Code检索产品信息
     */
    public Map<String, Object> moveSkuSearch(Map<String, Object> params, String channelId, String lang) {

        Map<String, Object> returnMap = new HashMap<>();

        List<CmsBtProductModel> codeList = new ArrayList<>();

        // 检索的Code
        String searchCode = (String) params.get("searchCode");
        // 源Code
        String sourceCode = (String) params.get("sourceCode");

        // Code如果不输入，那么没有检索结果
        if (StringUtils.isEmpty(searchCode)) {
            returnMap.put("codeList", codeList);
            return returnMap;
        }

        // 根据输入的Code检索出对应的Product信息
        CmsBtProductModel productModel = productService.getProductByCode(channelId, searchCode);
        if (productModel != null) {
            codeList.add(productModel);
        }

        // 去除自己那条
        codeList = codeList.stream().filter(codeModel->!codeModel.getCommon().getFields().getCode().equals(sourceCode)).collect(Collectors.toList());

        returnMap.put("codeList", codeList);
        return returnMap;
    }

    /**
     * 移动Sku-根据移动Sku的源Code/目标Code取得移动前/移动后的Preview
     */
    public Map<String, Object> moveSkuPreview(Map<String, Object> params, String channelId, String lang) {
        Map<String, Object> previewInfo = new HashMap<>();

        // 取得移动需要的信息
        // 目标Group类型 "new" or "old" or "select"
        String destGroupType = (String) params.get("destGroupType");
        // 移动的Sku列表
        List<String> skuList = (List) params.get("skuList");
        // 源Code
        String sourceCode = (String) params.get("sourceCode");
        // 参照Code
        String refCode = (String) params.get("refCode");

        // 如果没有选择移动方式，出错
        if (StringUtils.isEmpty(destGroupType)) {
            // 请选择移动方式
            throw new BusinessException("7000112");
        }

        // 参照Code没有选择
        if ("select".equals(destGroupType) && StringUtils.isEmpty(refCode)) {
            // 请选择编辑Group时参照的Code
            throw new BusinessException("7000113");
        }


        // 取得源Code的Product信息
        CmsBtProductModel sourceProductModel = productService.getProductByCode(channelId, sourceCode);

        // check移动信息是否匹配（源Group下是否包含移动的Code，源Group是否存在）
        if (skuList == null || sourceProductModel == null) {
            // 移动的数据不整合
            throw new BusinessException("7000114");
        }

        // 移动前-源Code信息
        previewInfo.put("sourceCodeInfoBefore", sourceCode);

        // 移动前-源Sku信息
        List<Map<String, Object>> sourceSkuListBefore = new ArrayList<>();
        for (CmsBtProductModel_Sku sku : sourceProductModel.getCommon().getSkus()) {
            Map<String, Object> sourceSkuInfoMapBefore = new HashMap<>();
            sourceSkuInfoMapBefore.put("sku", sku.getSkuCode());
            // 如果这个Code是当前移动的Code,那么就高亮显示
            if (skuList.contains(sku.getSkuCode())) {
                sourceSkuInfoMapBefore.put("current", true);
            }
            sourceSkuListBefore.add(sourceSkuInfoMapBefore);
        }
        previewInfo.put("sourceSkuInfoBefore", sourceSkuListBefore);

        // 移动前-目标Code信息
        previewInfo.put("destCodeInfoBefore", "");

        // 移动前-目标Sku信息
        List<Map<String, Object>> destSkuListBefore = new ArrayList<>();
        previewInfo.put("destSkuInfoBefore", destSkuListBefore);

        // 移动后-源Code信息
        previewInfo.put("sourceCodeInfoAfter", sourceCode);

        // 移动后-源Code信息
        List<Map<String, Object>> sourceSkuListAfter = new ArrayList<>();
        for (CmsBtProductModel_Sku sku : sourceProductModel.getCommon().getSkus()) {
            // 去除当前移动的Sku
            if (!skuList.contains(sku.getSkuCode())) {
                Map<String, Object> sourceSkuInfoMapAfter = new HashMap<>();
                sourceSkuInfoMapAfter.put("sku", sku.getSkuCode());
                sourceSkuListAfter.add(sourceSkuInfoMapAfter);
            }
        }
        previewInfo.put("sourceSkuInfoAfter", sourceSkuListAfter);

        // 移动后-目标Code信息
        previewInfo.put("destCodeInfoAfter", skuList.get(0));

        // 移动后-目标Sku信息
        List<Map<String, Object>> destSkuListAfter = new ArrayList<>();
        // 加入移动的Sku，并且高亮显示
        for (String sku : skuList) {
            Map<String, Object> destSkuInfoMapAfter = new HashMap<>();
            destSkuInfoMapAfter.put("sku", sku);
            destSkuInfoMapAfter.put("current", true);
            destSkuListAfter.add(destSkuInfoMapAfter);
        }
        previewInfo.put("destSkuInfoAfter", destSkuListAfter);

        return previewInfo;
    }


    /**
     * 移动Sku-从一个Code到新Code
     */
    @VOTransactional
    public Map<String, Object> moveSku(Map<String, Object> params, String channelId, String modifier, String lang) {
        Map<String, Object> returnInfo = new HashMap<>();

        returnInfo.put("newProdId", new Integer(2513260));
        return returnInfo;
    }







    /**
     * Check是否选择了这个Code下的所有Sku
     */
    private boolean checkSelectAllSkus(List<Map<String, Object>> skuList) {
        for (Map<String, Object> sku : skuList) {
            Boolean isChecked = (Boolean) sku.get("isChecked");
            if (isChecked == null || !isChecked) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check这个Code下Sku是否一个都没有被选择
     */
    private boolean checkNoSelectSku(List<Map<String, Object>> skuList) {
        for (Map<String, Object> sku : skuList) {
            Boolean isChecked = (Boolean) sku.get("isChecked");
            if (isChecked != null && isChecked) {
                return true;
            }
        }
        return false;
    }



    /**
     * CheckGroup中如果包含多了Code，并且这个Code是主商品，那么不可以移动。（提示请切换其他商品为主商品后才可以移动）
     */
    private boolean checkIsMainProduct(CmsBtProductGroupModel groupModel, String productCode) {
        // Group中包含多了Code，并且这个Code是主商品
        if (groupModel.getProductCodes().size() > 1 && productCode.equals(groupModel.getMainProductCode())) {
            return false;
        }
        return true;
    }

    /**
     * Check这个Code是不是Approved的状态，如果是的话，提示先下线。
     */
    private boolean checkCodeStatus(CmsBtProductModel productModel, Integer cartId) {

        for (Map.Entry<String, CmsBtProductModel_Platform_Cart> platform : productModel.getPlatforms().entrySet()) {
            // 找到对应的平台信息，看看状态是不是Approved
            if (cartId.equals(platform.getValue().getCartId())) {
                if (CmsConstants.ProductStatus.Approved.name().equals(platform.getValue().getStatus())) {
                    return false;
                }
                break;
            }

        }
        return true;
    }

    /**
     * Check这个Code是否是锁定的状态。
     */
    private boolean checkCodeLocked(CmsBtProductModel productModel) {
        if ("1".equals(productModel.getLock())) {
            return false;
        }
        return true;
    }

    /**
     * 源Code是否存在包含选择的Sku列表。
     */
    private boolean checkSkusInCode(CmsBtProductModel productModel, List skuList) {
        for (Object sku : skuList) {
            String selectedSku = "";
            boolean find = false;
            if (sku instanceof Map) {
                Boolean isChecked = (Boolean) ((Map) sku).get("isChecked");
                if (isChecked != null && isChecked) {
                    selectedSku = (String) ((Map) sku).get("skuCode");
                }
            } else if (sku instanceof String) {
                selectedSku = (String) sku;
            }
            if (!StringUtils.isEmpty(selectedSku)) {
                for (CmsBtProductModel_Sku skuModel : productModel.getCommon().getSkus()) {
                    if (selectedSku.equals(skuModel.getSkuCode())) {
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    return false;
                }
            }
        }
        return true;
    }



    /**
     * 新建一个新的Group。
     */
    private CmsBtProductGroupModel createNewGroup(String channelId, Integer cartId, String productCode) {

        CmsBtProductGroupModel group = new CmsBtProductGroupModel();

        // 渠道id
        group.setChannelId(channelId);

        // cart id
        group.setCartId(cartId);

        // 获取唯一编号
        group.setGroupId(mongoSequenceService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_GROUP_ID));

        // 主商品Code
        group.setMainProductCode(productCode);

        // platform status:发布状态: 未上新 // Synship.com_mt_type : id = 45
        group.setPlatformStatus(CmsConstants.PlatformStatus.WaitingPublish);

        CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBean(channelId
                , CmsConstants.ChannelConfig.PLATFORM_ACTIVE
                , String.valueOf(group.getCartId()));
        if (cmsChannelConfigBean != null && !com.voyageone.common.util.StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
            if (CmsConstants.PlatformActive.ToOnSale.name().equals(cmsChannelConfigBean.getConfigValue1())) {
                group.setPlatformActive(CmsConstants.PlatformActive.ToOnSale);
            } else {
                // platform active:上新的动作: 暂时默认是放到:仓库中
                group.setPlatformActive(CmsConstants.PlatformActive.ToInStock);
            }
        } else {
            // platform active:上新的动作: 暂时默认是放到:仓库中
            group.setPlatformActive(CmsConstants.PlatformActive.ToInStock);
        }

        // ProductCodes
        List<String> codes = new ArrayList<>();
        codes.add(productCode);
        group.setProductCodes(codes);
        group.setCreater(getClass().getName());
        group.setModifier(getClass().getName());

        // 计算group价格区间
        productGroupService.calculatePriceRange(group);

        return group;
    }
}
