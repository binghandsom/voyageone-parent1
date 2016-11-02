package com.voyageone.web2.cms.views.product;

import com.google.common.base.Joiner;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.BeanUtils;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.impl.cms.prices.IllegalPriceConfigException;
import com.voyageone.service.impl.cms.prices.PriceCalculateException;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.*;
import com.voyageone.service.impl.cms.promotion.PromotionCodeService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.wms.InventoryCenterLogicService;
import com.voyageone.service.impl.wms.InventoryCenterService;
import com.voyageone.service.impl.wms.ItemDetailsService;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.web2.base.BaseViewService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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

    @Autowired
    private MongoSequenceService commSequenceMongoService;

    @Autowired
    private PriceService priceService;

    @Autowired
    private ItemDetailsService itemDetailsService;

    @Autowired
    private InventoryCenterService inventoryCenterService;

    @Autowired
    private InventoryCenterLogicService inventoryCenterLogicService;

    @Autowired
    private CmsBtPriceLogService cmsBtPriceLogService;

    @Autowired
    private CmsBtPriceConfirmLogService cmsBtPriceConfirmLogService;

    @Autowired
    private SxProductService sxProductService;


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
            sourceGroupModel.setModifier(modifier);
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
                if (String.valueOf(platform.getValue().getCartId()).equals(CartEnums.Cart.JM.getId())
                        || String.valueOf(platform.getValue().getCartId()).equals(CartEnums.Cart.CN.getId())) {
                    returnMap.put("includeJMCN", true);
                    return returnMap;
                }
            }
        }
        returnMap.put("includeJMCN", false);
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
        // 参照Code的Product信息
        CmsBtProductModel refProductModel = null;

        // 如果没有选择移动方式，出错
        if (StringUtils.isEmpty(destGroupType)) {
            // 请选择移动方式
            throw new BusinessException("7000112");
        }

        // 选择参照Code
        if ("select".equals(destGroupType)) {
            if (StringUtils.isEmpty(refCode)) {
                // 请选择编辑Group时参照的Code
                throw new BusinessException("7000113");
            } else {
                refProductModel = productService.getProductByCode(channelId, refCode);
                if (refProductModel == null) {
                    // 移动的数据不整合
                    throw new BusinessException("7000114");
                }
            }
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
        previewInfo.put("destCodeInfoAfter", this.makeNewCode(skuList, channelId));

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

        // 取得移动需要的信息
        // 目标Group类型 "new" or "old" or "select"
        String destGroupType = (String) params.get("destGroupType");
        // 移动的Sku列表
        List<String> skuList = (List) params.get("skuList");
        // 源Code
        String sourceCode = (String) params.get("sourceCode");
        // 参照Code
        String refCode = (String) params.get("refCode");
        // 参照Code的Product信息
        CmsBtProductModel refProductModel = null;

        // 如果没有选择移动方式，出错
        if (StringUtils.isEmpty(destGroupType)) {
            // 请选择移动方式
            throw new BusinessException("7000112");
        }

        // 选择参照Code
        if ("select".equals(destGroupType)) {
            if (StringUtils.isEmpty(refCode)) {
                // 请选择编辑Group时参照的Code
                throw new BusinessException("7000113");
            } else {
                refProductModel = productService.getProductByCode(channelId, refCode);
                if (refProductModel == null) {
                    // 移动的数据不整合,移动Sku失败
                    throw new BusinessException("7000116");
                }
            }
        }

        // 取得源Code的Product信息
        CmsBtProductModel sourceProductModel = productService.getProductByCode(channelId, sourceCode);

        // check移动信息是否匹配（源Group下是否包含移动的Code，源Group是否存在）
        if (skuList == null || sourceProductModel == null || skuList.size() == 0 || !checkSkusInCode(sourceProductModel, skuList)) {
            // 移动的数据不整合,移动Sku失败
            throw new BusinessException("7000116");
        }

        // Check源Code是否存在于没有结束的活动中。
        String promotionNames = promotionCodeService.getExistCodeInActivePromotion(channelId, sourceCode, null);
        if (!StringUtil.isEmpty(promotionNames)) {
            // 处理Code存在于没有结束的活动:" + promotionNames + "中，请从活动中移除，或者等活动结束后再进行移动Sku操作
            throw new BusinessException("7000110", new Object[]{promotionNames});
        }

        // Check源Code是否是锁定的状态。
        if (!checkCodeLocked(sourceProductModel)) {
            // 处理Code正处于锁定的状态，请先解锁后再进行移动Sku操作
            throw new BusinessException("7000111");
        }

        List<TypeChannelBean> typeChannelBeanListDisplay = TypeChannels.getTypeListSkuCarts(channelId, "D", "en"); // 取得展示用数据
        if (ListUtils.isNull(typeChannelBeanListDisplay)) {
            // 在com_mt_value_channel表中没有找到当前Channel允许售卖的Cart信息,移动Sku失败
            throw new BusinessException("7000119");
        }

        // ************这里开始正式移动*************
        // 处理目标Code（新Code）
        // Copy源Code为新Code
        CmsBtProductModel newProductModel = productService.getProductByCode(channelId, sourceCode);

        String now = DateTimeUtil.getNow();

        // 重新设置那些需要更新的属性
        newProductModel.setCreated(now);
        newProductModel.setModified(now);
        newProductModel.setCreater(modifier);
        newProductModel.setModifier(modifier);
        newProductModel.set_id(null);
        newProductModel.setBi(new BaseMongoMap());
        // 处理sales数据只保留移动的Sku
        CmsBtProductModel_Sales sales = newProductModel.getSales();
        if (sales != null) {
            sales.setCodeSum7(new BaseMongoMap());
            sales.setCodeSum30(new BaseMongoMap());
            sales.setCodeSumAll(new BaseMongoMap());
            List<CmsBtProductModel_Sales_Sku> saleSkus = sales.getSkus();
            List<CmsBtProductModel_Sales_Sku> newSaleSkus = new ArrayList<>();
            if (saleSkus != null) {
                for (CmsBtProductModel_Sales_Sku saleSku : saleSkus) {
                    if (skuList.contains(saleSku.getSkuCode())) {
                        newSaleSkus.add(saleSku);
                    }
                }
            }
            sales.setSkus(newSaleSkus);
        }
        // 处理common
        CmsBtProductModel_Common newCommonModel = newProductModel.getCommon();
        newCommonModel.setModifier(modifier);
        newCommonModel.setModified(now);

        // 处理common.fields
        CmsBtProductModel_Field newFieldModel = newCommonModel.getFields();
        newFieldModel.setCode(this.makeNewCode(skuList, channelId));
        newFieldModel.setIsMasterMain(0);

        // 处理common.sku
        List<CmsBtProductModel_Sku> skusModel = newCommonModel.getSkus();
        List<CmsBtProductModel_Sku> newSkusModel = new ArrayList<>();
        // 去除没有选择的Sku
        for (CmsBtProductModel_Sku skuModel : skusModel) {
            if (skuList.contains(skuModel.getSkuCode())) {
                newSkusModel.add(skuModel);
            }
        }
        newCommonModel.setSkus(newSkusModel);

        // 处理platforms.P0
        CmsBtProductModel_Platform_Cart newP0Model = newProductModel.getPlatform(0);
        if ("new".equals(destGroupType)) {
            // 新Group的场合，P0下的主商品=源Code
            newP0Model.setMainProductCode(sourceCode);
        } else {
            if ("old".equals(destGroupType)) {
                // 原Group的场合,P0下的主商品不变
            } else {
                // 选择参照Code的场合,P0下的主商品=参照Code对应Group(carId=0)的主商品
                CmsBtProductGroupModel group0 = productGroupService.selectProductGroupByCode(channelId, refCode, 0);
                if (group0 != null) {
                    newP0Model.setMainProductCode(group0.getMainProductCode());
                } else {
                    // 不能获取参照Code对应的Group信息,移动Sku失败
                    throw new BusinessException("7000118");
                }
            }
        }

        // 处理platforms.PXX
        for (Map.Entry<String, CmsBtProductModel_Platform_Cart> entry : newProductModel.getPlatforms().entrySet()) {

            CmsBtProductModel_Platform_Cart newPlatformModel = entry.getValue();
            // 跳过P0（主数据）
            if (entry.getValue().getCartId().equals(0)) {
                continue;
            }

            newPlatformModel.setStatus(CmsConstants.ProductStatus.Pending.toString());
            newPlatformModel.setpProductId(null);
            newPlatformModel.setpNumIId(null);
            newPlatformModel.setpPlatformMallId(null);
            newPlatformModel.setpPublishTime(null);
            newPlatformModel.setpPublishError(null);
            newPlatformModel.setAttribute("pStatus", null);
            newPlatformModel.setpReallyStatus(null);

            // 去除没有选择的Sku(platforms.skus)
            List<BaseMongoMap<String, Object>> platformSkusModel = newPlatformModel.getSkus();
            List<BaseMongoMap<String, Object>> newPlatformSkusModel = new ArrayList<>();
            for (BaseMongoMap<String, Object> platformSku : platformSkusModel) {
                if (skuList.contains(platformSku.get("skuCode"))) {
                    newPlatformSkusModel.add(platformSku);
                }
            }
            newPlatformModel.setSkus(newPlatformSkusModel);

            // 去除没有选择的Sku(platforms.fields.sku)
            if (newPlatformModel.getFields() != null && newPlatformModel.getFields().get("sku") != null) {
                if (newPlatformModel.getFields().get("sku") instanceof List) {
                    List platformFieldsSkusModel = (List) newPlatformModel.getFields().get("sku");
                    List<Map<String, Object>> newPlatformFieldsSkusModel = new ArrayList<>();
                    for (Object platformFieldSku : platformFieldsSkusModel) {
                        if (platformFieldSku instanceof  Map) {
                            if (((Map)platformFieldSku).get("sku_outerId") != null) {
                                if (skuList.contains(String.valueOf(((Map)platformFieldSku).get("sku_outerId")))) {
                                    newPlatformFieldsSkusModel.add((Map)platformFieldSku);
                                }
                            }
                        }
                    }
                    newPlatformModel.getFields().put("sku", newPlatformFieldsSkusModel);
                }
            }

            // 设置主商品
            if ("new".equals(destGroupType)) {
                newPlatformModel.setpIsMain(1);
                newPlatformModel.setMainProductCode(newFieldModel.getCode());
            } else {
                if ("old".equals(destGroupType)) {
                    // 原Group的场合,PXX下的主商品不变
                    newPlatformModel.setpIsMain(0);
                } else {
                    newPlatformModel.setpIsMain(0);
                    // 选择参照Code的场合,PXX下的主商品=参照Code对应Group的主商品
                    CmsBtProductGroupModel groupInfo = productGroupService.selectProductGroupByCode(channelId, refCode, entry.getValue().getCartId());
                    if (groupInfo != null) {
                        newPlatformModel.setMainProductCode(groupInfo.getMainProductCode());
                    } else {
                        // 不能获取参照Code对应的Group信息,移动Sku失败
                        throw new BusinessException("7000118");
                    }
                }
            }
        }

        // 重新计算价格
        try {
            priceService.setPrice(newProductModel, false);
        } catch (IllegalPriceConfigException ex) {
            // 新Code价格计算时出现异常(%s)
            throw new BusinessException("7000117", new Object[]{ex.getMessage()});
        } catch (PriceCalculateException ex) {
            // 新Code价格计算时出现异常(%s)
            throw new BusinessException("7000117", new Object[]{ex.getMessage()});
        }

        newProductModel.setProdId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_PROD_ID));

        // 计算商品价格区间
        productService.calculatePriceRange(newProductModel);
        // 插入DB
        productService.insert(newProductModel);


        // 处理源Code
        // common.skus中去除移动的sku
        List<CmsBtProductModel_Sku> sourceSkusModel = sourceProductModel.getCommon().getSkus();
        List<CmsBtProductModel_Sku> sourceSkusModelNew = new ArrayList<>();
        for (CmsBtProductModel_Sku skuModel : sourceSkusModel) {
            if (!skuList.contains(skuModel.getSkuCode())) {
                sourceSkusModelNew.add(skuModel);
            }
        }
        sourceProductModel.getCommon().setSkus(sourceSkusModelNew);
        sourceProductModel.getCommon().setModified(now);
        sourceProductModel.getCommon().setModifier(modifier);

        // 去除移动的sku
        for (Map.Entry<String, CmsBtProductModel_Platform_Cart> platform : sourceProductModel.getPlatforms().entrySet()) {
            // 跳过P0（主数据）
            if (platform.getValue().getCartId().equals(0)) {
                continue;
            }
            // platforms.skus中去除移动的sku
            List<BaseMongoMap<String, Object>> platformSkusNew = new ArrayList<>();
            for (BaseMongoMap<String, Object> platformSku : platform.getValue().getSkus()) {
                if (!skuList.contains((String) platformSku.get("skuCode"))) {
                    platformSkusNew.add(platformSku);
                }
            }
            platform.getValue().setSkus(platformSkusNew);

            // 去除没有选择的Sku(platforms.fields.sku)
            if (platform.getValue().getFields() != null && platform.getValue().getFields().get("sku") != null) {
                if (platform.getValue().getFields().get("sku") instanceof List) {
                    List platformFieldsSkusModel = (List) platform.getValue().getFields().get("sku");
                    List<Map<String, Object>> newPlatformFieldsSkusModel = new ArrayList<>();
                    for (Object platformFieldSku : platformFieldsSkusModel) {
                        if (platformFieldSku instanceof  Map) {
                            if (((Map)platformFieldSku).get("sku_outerId") != null) {
                                if (!skuList.contains(String.valueOf(((Map)platformFieldSku).get("sku_outerId")))) {
                                    newPlatformFieldsSkusModel.add((Map)platformFieldSku);
                                }
                            }
                        }
                    }
                    platform.getValue().getFields().put("sku", newPlatformFieldsSkusModel);
                }
            }

            platform.getValue().setModified(now);
        }

        // sales中去除移动的sku
        if (sourceProductModel.getSales().getSkus() != null) {
            List<CmsBtProductModel_Sales_Sku> salesSkusNew = new ArrayList<>();
            for (CmsBtProductModel_Sales_Sku salesSku : sourceProductModel.getSales().getSkus()) {
                if (!skuList.contains(salesSku.getSkuCode())) {
                    salesSkusNew.add(salesSku);
                }
            }
            sourceProductModel.getSales().setSkus(salesSkusNew);
        }

        // 计算商品价格区间
        productService.calculatePriceRange(sourceProductModel);
        // 更新源Code
        productService.updateProductForMove(channelId, sourceProductModel, modifier);

        // 处理目标Group
        for (TypeChannelBean shop : typeChannelBeanListDisplay) {
            // 新建Group(选择新Group或者聚美平台或者独立官网平台)
            if ("new".equals(destGroupType)
                    || shop.getValue().equals(CartEnums.Cart.JM.getId())
                    || shop.getValue().equals(CartEnums.Cart.CN.getId())) {
                productGroupService.insert(createNewGroup(channelId, Integer.parseInt(shop.getValue()), newFieldModel.getCode()));
            } else {
                // 取得Group信息
                CmsBtProductGroupModel groupModel = null;
                if ("old".equals(destGroupType)) {
                    groupModel = productGroupService.selectProductGroupByCode(channelId, sourceCode, Integer.parseInt(shop.getValue()));
                } else {
                    groupModel = productGroupService.selectProductGroupByCode(channelId, refCode, Integer.parseInt(shop.getValue()));
                }
                if (groupModel != null) {
                    groupModel.getProductCodes().add(newFieldModel.getCode());
                    groupModel.setModifier(modifier);
                    // 计算Group价格区间
                    productGroupService.calculatePriceRange(groupModel);
                    // 更新Group
                    productGroupService.update(groupModel);
                }
            }
        }

        // 处理源Group（如果是移动到新Group或者选择的Group，那么源Group下面的价格区间需要进行重新计算一下）
        if ("new".equals(destGroupType) || "select".equals(destGroupType)) {
            List<CmsBtProductGroupModel> sourceGroups = productGroupService.selectProductGroupListByCode(channelId, sourceCode);
            for (CmsBtProductGroupModel sourceGroup : sourceGroups) {
                sourceGroup.setModifier(modifier);
                // 计算Group价格区间
                productGroupService.calculatePriceRange(sourceGroup);
                // 更新Group
                productGroupService.update(sourceGroup);
            }
        }

        // 更新wms_bt_item_details表
        itemDetailsService.updateCodeForMove(channelId, sourceCode, skuList, newFieldModel.getCode(), modifier);
        // 更新wms_bt_inventory_center表
        inventoryCenterService.updateCodeForMove(channelId, sourceCode, skuList, newFieldModel.getCode(), modifier);
        // 更新wms_bt_inventory_center_logic表
        inventoryCenterLogicService.updateCodeForMove(channelId, sourceCode, skuList, newFieldModel.getCode(), modifier);
        // 更新价格履历cms_bt_price_log表
        cmsBtPriceLogService.updateCmsBtPriceLogForMove(channelId, sourceCode, skuList, newFieldModel.getCode(), modifier);
        // 更新价格确认履历cms_bt_price_confirm_log表
        cmsBtPriceConfirmLogService.updateCmsBtPriceLogForMove(channelId, sourceCode, skuList, newFieldModel.getCode(), modifier);
        // 更新商品操作履历：cms_bt_product_status_history表
        // 源Code
        for (Map.Entry<String, CmsBtProductModel_Platform_Cart> platform : sourceProductModel.getPlatforms().entrySet()) {
            // 跳过P0（主数据）
            if (platform.getValue().getCartId().equals(0)) {
                continue;
            }
            productStatusHistoryService.insert(channelId, sourceCode,
                    platform.getValue().getStatus(), platform.getValue().getCartId(), EnumProductOperationType.MoveSku,
                    "移除了下面的Sku:" + Joiner.on(",").skipNulls().join(skuList), modifier);
        }
        // 新Code
        for (Map.Entry<String, CmsBtProductModel_Platform_Cart> platform : newProductModel.getPlatforms().entrySet()) {
            // 跳过P0（主数据）
            if (platform.getValue().getCartId().equals(0)) {
                continue;
            }
            productStatusHistoryService.insert(channelId, newFieldModel.getCode(),
                    platform.getValue().getStatus(), platform.getValue().getCartId(), EnumProductOperationType.MoveSku,
                    "增加了下面的Sku:" + Joiner.on(",").skipNulls().join(skuList), modifier);
        }

        // cms_bt_sx_workload中插入个各个平台的元Code对应的Group。（如果已经上新的情况下）
        sxProductService.insertSxWorkLoad(sourceProductModel, modifier);


        returnInfo.put("newProdId", newProductModel.getProdId());
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

    /**
     * 新建一个新的Code。
     */
    private String makeNewCode(List<String> skuList, String channelId) {
        int i = 0;
        while (true) {
            String suffixName = "";
            if (i > 0) {
                suffixName = "-" + String.format("%03d", i);
            }
            CmsBtProductModel productModel = productService.getProductByCode(channelId, skuList.get(0) + suffixName);
            if (productModel == null) {
                return skuList.get(0) + suffixName;
            }
            i++;
        }
    }
}
