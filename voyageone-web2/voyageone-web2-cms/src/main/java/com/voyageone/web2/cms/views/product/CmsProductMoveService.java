package com.voyageone.web2.cms.views.product;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.Types;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtPlatformActiveLogDao;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtPlatformActiveLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 移动Code-初始化
     */
    public Map<String, Object> moveCodeInit(Map<String, Object> params, String channelId) {

        Map<String, Object> returnMap = new HashMap<>();

        String productCode = (String) params.get("productCode");
        Integer cartId = (Integer) params.get("cartId");

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
        return returnMap;
    }

    /**
     * 移动Code-根据Code检索Group信息
     */
    public List<Map<String, Object>> moveCodeSearch(Map<String, Object> params, String channelId, String lang) {

        List<Map<String, Object>> groupList = new ArrayList<>();

        String searchCode = (String) params.get("searchCode");
        Integer cartId = (Integer) params.get("cartId");
        // Code如果不输入，那么没有检索结果
        if (StringUtils.isEmpty(searchCode)) {
            return groupList;
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
        return groupList;
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
        Long sourceGroupId = (Long) params.get("sourceGroupId");
        // 移动目标GroupId
        Long destGroupId = (Long) params.get("destGroupId");
        // 移动源Group的主商品的名称
        String sourceGroupName = (String) params.get("sourceGroupName");
        // 移动目标Group的主商品的名称
        String destGroupName = (String) params.get("destGroupName");


        // 如果没有选择移动目的Group，出错
        if (StringUtils.isEmpty(destGroupType) || ("select".equals(destGroupType) && destGroupId == null)) {
            throw new BusinessException("请选择移动目的Group");
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
        previewInfo.put("destGroupInfoAfter", sourceGroupName);
        // 如果移动前-源Code个数为1，那么相当于移动后删除这个Group，那么就要加上删除线
        if (sourceCodeListBefore.size() == 1) {
            previewInfo.put("destGroupInfoAfterDeleted", true);
        }

        // 移动后-源Code信息
        List<Map<String, Object>> sourceCodeListAfter = new ArrayList<>();
        // 如果移动前-源Code个数为1，那么相当于移动后删除这个Group-Code，那么就要加上删除线
        if (sourceCodeListBefore.size() == 1) {
            Map<String, Object> sourceCodeInfoMapAfter = new HashMap<>();
            sourceCodeInfoMapAfter.put("code", (String) destCodeListBefore.get(0).get("code"));
            sourceCodeInfoMapAfter.put("current", true);
            sourceCodeInfoMapAfter.put("deleted", true);
            sourceCodeListAfter.add(sourceCodeInfoMapAfter);
        } else {
            for (String code : sourceGroupModel.getProductCodes()) {
                // 去除当前移动的Code
                if (!code.equals(productCode)) {
                    Map<String, Object> sourceCodeInfoMapAfter = new HashMap<>();
                    sourceCodeInfoMapAfter.put("code", code);
                    sourceCodeListAfter.add(sourceCodeInfoMapAfter);
                }
            }
        }

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

        } else {
            if (destGroupModel != null) {
                // 加入原来目标Group下的Code
                for (String code : destGroupModel.getProductCodes()) {
                    Map<String, Object> destCodeInfoMapAfter = new HashMap<>();
                    destCodeInfoMapAfter.put("code", code);
                    destCodeListAfter.add(destCodeInfoMapAfter);
                }
                // 再加入移动的Code，并且高亮显示
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
    public void moveCode(Map<String, Object> params, String channelId, String lang) {
        // 取得移动需要的信息
        // 目标Group类型 "new" or "select"
        String destGroupType = (String) params.get("destGroupType");
        // 移动的Code
        String productCode = (String) params.get("productCode");
        // 移动源GroupId
        Long sourceGroupId = (Long) params.get("sourceGroupId");
        // 移动目标GroupId
        Long destGroupId = (Long) params.get("destGroupId");
        // 相关的平台
        Integer cartId = (Integer) params.get("cartId");

        // 如果没有选择移动目的Group，出错
        if (StringUtils.isEmpty(destGroupType) || ("select".equals(destGroupType) && destGroupId == null)) {
            throw new BusinessException("请选择移动目的Group");
        }

        // 取得源Group信息
        CmsBtProductGroupModel sourceGroupModel = productGroupService.getProductGroupByGroupId(channelId, sourceGroupId);
        // 取得目标Group信息
        CmsBtProductGroupModel destGroupModel = null;
        if ("select".equals(destGroupType)) {
            destGroupModel = productGroupService.getProductGroupByGroupId(channelId, destGroupId);
        }

        // check移动信息是否匹配（源Group下是否包含移动的Code，源Group和目标Group是否存在）



        // check业务上的移动条件是否满足

    }

    /**
     * CheckGroup中如果包含多了Code，并且这个Code是主商品，那么不可以移动。（提示请切换其他商品为主商品后才可以移动）
     */
    public boolean checkIsMainProduct(CmsBtProductGroupModel groupModel, String productCode, Integer cartId) {
        return true;
    }

    /**
     * Check这个Code是不是Approved的状态，如果是的话，提示先下线。
     */
    public boolean checkCodeStatus(CmsBtProductModel productModel, String productCode, Integer cartId) {
        return true;
    }

    /**
     * Check这个Code是否存在于没有结束的活动中。
     */
    public boolean checkCodeInActivePromotion(CmsBtProductModel productModel, String productCode, Integer cartId) {
        return true;
    }

    /**
     * Check这个Code是否存在于没有结束的活动中。
     */
    public boolean checkCodeLocked(CmsBtProductModel productModel, String productCode, Integer cartId) {
        return true;
    }
}
