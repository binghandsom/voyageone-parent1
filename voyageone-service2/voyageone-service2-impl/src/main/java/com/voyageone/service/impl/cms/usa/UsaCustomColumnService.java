package com.voyageone.service.impl.cms.usa;

import com.sun.tools.corba.se.idl.StringGen;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CommonPropService;
import com.voyageone.service.impl.cms.PropService;
import com.voyageone.service.model.cms.enums.CartType;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sales;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * USA CMS 高级自定义
 *
 * @Author rex.wu
 * @Create 2017-07-17 14:59
 */
@Service
public class UsaCustomColumnService extends BaseService {

    @Autowired
    private PropService propService;
    @Autowired
    private CommonPropService commonPropService;


    /**
     * 取得用户已选择的自定义显示列设置（一览画面用）
     *
     */
//    public void getUserCustColumns(String channelId, int userId, String language) {
//        Map<String, Object> rsMap = commonPropService.getCustColumnsByUserId(userId, "cms_prod_cust_col");
//        String custAttrStr;
//        String commStr;
//        if (rsMap == null) {
//            $debug("该用户还未设置自定义查询列 userId=" + userId + " channelId=" + channelId);
//            custAttrStr = "";
//            commStr = "";
//        } else {
//            custAttrStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) rsMap.get("cfg_val1"));
//            commStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) rsMap.get("cfg_val2"));
//        }
//
//        // 设置自定义查询用的属性
//        List<Map<String, String>> custAttsQueryList = new ArrayList<>();
//
//        List<Map<String, Object>> customProps2 = new ArrayList<>();
//        String[] custAttrList = custAttrStr.split(",");
//        StringBuilder customPropsStr = new StringBuilder();
//        if (custAttrList.length > 0) {
//            List<Map<String, Object>> customProps = feedCustomPropService.getFeedCustomPropAttrs(channelId, "");
//            for (Map<String, Object> props : customProps) {
//                if(props.get("feed_prop_translation") == null) continue;
//                String propId = (String) props.get("feed_prop_original");
//                Map<String, String> atts = new HashMap<>(2);
//                atts.put("configCode", "feed.cnAtts." + propId);
//                atts.put("configValue1", (String) props.get("feed_prop_translation"));
//                custAttsQueryList.add(atts);
//
//                if (ArrayUtils.contains(custAttrList, propId)) {
//                    customProps2.add(props);
//                    customPropsStr.append("feed.cnAtts.");
//                    customPropsStr.append(propId);
//                    customPropsStr.append(";");
//                    customPropsStr.append("feed.orgAtts.");
//                    customPropsStr.append(propId);
//                    customPropsStr.append(";");
//                }
//            }
//        }
//        List<Map<String, Object>> commonProp2 = new ArrayList<>();
//        String[] commList = commStr.split(",");
//        StringBuilder commonPropsStr = new StringBuilder();
//        if (commList.length > 0) {
//            List<Map<String, Object>> commonProps = commonPropService.getCustColumns(1);
//            for (Map<String, Object> props : commonProps) {
//                String propId = (String) props.get("propId");
//                if ("comment".equals(propId)) {
//                    Map<String, String> atts = new HashMap<>(2);
//                    atts.put("configCode", "common.comment");
//                    atts.put("configValue1", (String) props.get("propName"));
//                    atts.put("valType", (String) props.get("valType"));
//                    custAttsQueryList.add(atts);
//
//                    if (ArrayUtils.contains(commList, propId)) {
//                        commonProp2.add(props);
//                        commonPropsStr.append("common.comment");
//                        commonPropsStr.append(";");
//                    }
//                } else {
//                    Map<String, String> atts = new HashMap<>(2);
//                    atts.put("configCode", "common.fields." + propId);
//                    atts.put("configValue1", (String) props.get("propName"));
//                    atts.put("valType", (String) props.get("valType"));
//                    custAttsQueryList.add(atts);
//
//                    if (ArrayUtils.contains(commList, propId)) {
//                        commonProp2.add(props);
//                        commonPropsStr.append("common.fields.");
//                        commonPropsStr.append(propId);
//                        commonPropsStr.append(";");
//                    }
//                }
//            }
//        }
//
//        cmsSession.putAttribute("_adv_search_props_custAttsQueryList", custAttsQueryList);
//        cmsSession.putAttribute("_adv_search_props_searchItems", customPropsStr.toString() + commonPropsStr.toString());
//        cmsSession.putAttribute("_adv_search_customProps", customProps2);
//        cmsSession.putAttribute("_adv_search_commonProps", commonProp2);
//
//        rsMap = commonPropService.getCustColumnsByUserId(userId, "cms_prod_cust_col_salestype");
//        List<String> itemList = new ArrayList<>();
//        if (rsMap != null) {
//            String itemVal = org.apache.commons.lang3.StringUtils.trimToNull((String) rsMap.get("cfg_val1"));
//            if (itemVal != null) {
//                Collections.addAll(itemList, itemVal.split(","));
//            }
//        }
//        cmsSession.putAttribute("_adv_search_selSalesType", advSearchQueryService.getSalesTypeList(channelId, language, itemList));
//    }
//
//
//    /**
//     * 获取检索页面初始化的master data数据
//     */
//    public Map<String, Object> getMasterData(UserSessionBean userInfo, CmsSessionBean cmsSession, String language) throws IOException {
//
//        Map<String, Object> masterData = new HashMap<>();
//
//        // 获取product status
//        masterData.put("productStatusList", TypeConfigEnums.MastType.productStatus.getList(language));
//
//        // 获取publish status
//        masterData.put("platformStatusList", TypeConfigEnums.MastType.platformStatus.getList(language));
//
//        // 获取自定义标签列表
//        Map<String, Object> param = new HashMap<>(2);
//        param.put("channelId", userInfo.getSelChannelId());
//        param.put("tagTypeSelectValue", "4");
//        masterData.put("freetagList", cmsChannelTagService.getTagInfoByChannelId(param));
//
//        // 获取price type
//        masterData.put("priceTypeList", TypeConfigEnums.MastType.priceType.getList(language));
//
//        // 获取compare type
//        masterData.put("compareTypeList", TypeConfigEnums.MastType.compareType.getList(language));
//
//        // 获取brand list
//        masterData.put("brandList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, userInfo.getSelChannelId(), language));
//
//        // 取得产品类型
//        masterData.put("productTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_57, userInfo.getSelChannelId(), language));
//        // 取得尺寸类型
//        masterData.put("sizeTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_58, userInfo.getSelChannelId(), language));
//
//        // 取得销量类型
//        List<Map<String, String>> salesTypeList = advSearchOtherService.getSalesTypeList(userInfo.getSelChannelId(), language, null);
//        List<Map<String, String>> allSortList = new ArrayList<>(salesTypeList);
//        // 获取sort list
//        List<Map<String, Object>> sortList = commonPropService.getCustColumns(3);
//        List<Map<String, String>> biDataList = advSearchOtherService.getBiDataList(userInfo.getSelChannelId(), language, null);
//        allSortList.addAll(biDataList);
//        for (Map<String, String> sortData : allSortList) {
//            Map<String, Object> keySumMap = new HashMap<>();
//            keySumMap.put("propId", sortData.get("value"));
//            keySumMap.put("propName", sortData.get("name"));
//            sortList.add(keySumMap);
//        }
//        masterData.put("sortList", sortList);
//
//        // 店铺(cart/平台)列表
//        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(userInfo.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, language);
//        // 按cart获取promotion list，只加载有效的活动(活动期内/未关闭/有标签)
//        Map<String, List> promotionMap = new HashMap<>();
//        Map<String, List> shelvesMap = new HashMap<>();
//        Map<String, String> confirmPrice = new HashMap<>();
//        param = new HashMap<>();
//        for (TypeChannelBean cartBean : cartList) {
//            if (CartEnums.Cart.JM.getId().equals(cartBean.getValue())) {
//                // 聚美促销活动预加载
//                promotionMap.put(CartEnums.Cart.JM.getId(), jmPromotionService.getJMActivePromotions(CartEnums.Cart.JM.getValue(), userInfo.getSelChannelId()));
//            } else {
//                param.put("cartId", Integer.parseInt(cartBean.getValue()));
//                promotionMap.put(cartBean.getValue(), promotionService.getPromotions4AdvSearch(userInfo.getSelChannelId(), param));
//            }
//
//            shelvesMap.put(cartBean.getValue(),cmsBtShelvesService.selectByChannelIdCart(userInfo.getSelChannelId(), Integer.parseInt(cartBean.getValue())));
//
//            // 是否是使用价格公式
//            CmsChannelConfigBean priceCalculatorConfig = CmsChannelConfigs.getConfigBeanWithDefault(userInfo.getSelChannelId(), CmsConstants.ChannelConfig.PRICE_CALCULATOR, cartBean.getValue());
//            if (priceCalculatorConfig == null) {
//                priceCalculatorConfig = new CmsChannelConfigBean(CmsConstants.ChannelConfig.PRICE_CALCULATOR_FORMULA, "0", "0");
//            }
//            confirmPrice.put(cartBean.getValue(), priceCalculatorConfig.getConfigValue2());
//        }
//        masterData.put("promotionMap", promotionMap);
//
//        masterData.put("shelvesMap", shelvesMap);
//
//        masterData.put("confirmPrice", confirmPrice);
//
//        // 获取自定义查询用的属性
//        masterData.put("custAttsList", cmsSession.getAttribute("_adv_search_props_custAttsQueryList"));
//
//        //标签type
//        masterData.put("tagTypeList", Types.getTypeList(TypeConfigEnums.MastType.tagType.getId(), language));
//
//        // 翻译状态
//        masterData.put("transStatusList", Types.getTypeList(TypeConfigEnums.MastType.translationStatus.getId(), language));
//
//        // 设置按销量排序的选择列表
//        masterData.put("salesTypeList", salesTypeList);
//
//        // 设置BI数据显示的选择列表
//        masterData.put("biDataList", biDataList);
//
//        // 判断是否是minimall/usjoi用户
//        boolean isMiniMall = Channels.isUsJoi(userInfo.getSelChannelId());
//        masterData.put("isminimall", isMiniMall ? 1 : 0);
//        if (isMiniMall) {
//            List<TypeChannelBean> typeChannelBeenList = TypeChannels.getTypeChannelBeansByTypeValueLang(Constants.comMtTypeChannel.SKU_CARTS_53, userInfo.getSelChannelId(), "cn");
//            if (typeChannelBeenList == null || typeChannelBeenList.isEmpty()) {
//                $warn("高级检索:getMasterData 未取得供应商列表(Synship.com_mt_value_channel表中未配置) channelid=" + userInfo.getSelChannelId());
//            } else {
//                List<OrderChannelBean> channelBeanList = new ArrayList<>();
//                for (TypeChannelBean typeBean : typeChannelBeenList) {
//                    OrderChannelBean channelBean = Channels.getChannel(typeBean.getChannel_id());
//                    if (channelBean != null) {
//                        channelBeanList.add(channelBean);
//                    } else {
//                        $warn("高级检索:getMasterData 取得供应商列表 channel不存在 channelid=" + typeBean.getChannel_id());
//                    }
//                }
//                if (channelBeanList.isEmpty()) {
//                    $warn("高级检索:getMasterData 取得供应商列表 channel不存在 " + channelBeanList.toString());
//                } else {
//                    masterData.put("channelList", channelBeanList);
//                }
//            }
//        }
//
//        // 获取店铺列表
//        masterData.put("cartList", cartList);
//
//        // 是否自动最终售价同步指导价格
//        List<CmsChannelConfigBean> autoPriceCfg = CmsChannelConfigs.getConfigBeans(userInfo.getSelChannelId(), CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_SALE, "");
////        String autoApprovePrice = "0"; // 缺省不自动同步
////        if (autoPriceCfg != null && "1".equals(autoPriceCfg.getConfigValue1())) {
////             autoApprovePrice = "1"; // 自动同步
////        }
////        ;
//        if (!ListUtils.isNull(autoPriceCfg)) {
//            masterData.put("autoApprovePrice", autoPriceCfg.stream().collect(Collectors.toMap(CmsChannelConfigBean::getConfigCode, o -> o)));
//        } else {
//            masterData.put("autoApprovePrice", new HashMap<>());
//        }
//
//        // 是否是使用价格公式
//        CmsChannelConfigBean priceCalculatorConfig = CmsChannelConfigs.getConfigBeanNoCode(userInfo.getSelChannelId(), CmsConstants.ChannelConfig.PRICE_CALCULATOR);
////        String isPriceFormula = "0";
//        if (priceCalculatorConfig == null) {
//            priceCalculatorConfig = new CmsChannelConfigBean(CmsConstants.ChannelConfig.PRICE_CALCULATOR_FORMULA, "0", "0");
//        }
//        masterData.put("isPriceFormula", priceCalculatorConfig);
//
//        // 取得渠道的通用配置，动态按钮或配置可以直接在此外添加。
//        masterData.put("channelConfig", getChannelConfig(userInfo.getSelChannelId(), cartList, language));
//
//        return masterData;
//    }
//
//


    /**
     * 显示用户自定义列,并去人当前用户已勾选的自定义列
     *
     * @param channelId 渠道ID
     * @param userId    用户ID
     * @param language  语言环境
     */
    public Map<String, Object> getCustomColumnsWithChecked(String channelId, Integer userId, String language) {
        Map<String, Object> rsMap = new HashMap<>();

        // 获取所有Platform Attributes
        rsMap.put("platformAttributes", this.getPlatformAttributesCustColumns(channelId, language, null));
        // 取得已选择Platform Attributes
        Map<String, Object> colMap2 = commonPropService.getCustColumnsByUserId(userId, "usa_cms_cust_col_platform_attr");
        if (colMap2 == null || colMap2.isEmpty()) {
            rsMap.put("selPlatformAttributes", new String[]{});
        } else {
            String selStr = StringUtils.trimToEmpty((String) colMap2.get("cfg_val2"));
            rsMap.put("selPlatformAttributes", selStr.split(","));
        }


        // 取得所有Common Attributes
        List<Map<String, String>> commonProps = propService.getPropByType(0);
        commonProps.sort((o1, o2) -> o1.get("propId").toString().compareTo(o2.get("propId").toString()));
        rsMap.put("commonProps", commonProps);
        // 取得已选择Common Attributes
        colMap2 = commonPropService.getCustColumnsByUserId(userId, "usa_cms_cust_col_common_attr");
        if (colMap2 == null || colMap2.isEmpty()) {
            rsMap.put("selCommonProps", new String[]{});
        } else {
            String custAttrStr = StringUtils.trimToEmpty((String) colMap2.get("cfg_val2"));
            rsMap.put("selCommonProps", custAttrStr.split(","));
        }

        // Platform  Sales
        rsMap.put("platformSales", this.getPlatformSalesCustColumns(channelId, language));
        // 获取已勾选Platform Sales
        List<Map<String, Object>> platformSales = commonPropService.getMultiCustColumnsByUserId(userId, "usa_cms_cust_col_platform_sale");
        if (colMap2 == null || colMap2.isEmpty()) {
            rsMap.put("selPlatformSales", new ArrayList<Map<String, String>>());
        } else {
            List<Map<String, Object>> selPlatformSales = new ArrayList<>();
            for (Map<String, Object> map : platformSales) {
                Map<String, Object> cartMap = new HashMap<>();
                cartMap.put("cartId", map.get("cfg_val1"));
                cartMap.putAll(JacksonUtil.jsonToMap((String) map.get("cfg_val2")));
                selPlatformSales.add(cartMap);
            }
            rsMap.put("selPlatformSales", selPlatformSales);
        }
        return rsMap;
    }


    /**
     * 获取<<<Platform Attributes>>>自定义列
     * <p>1、各平台销售统计数据</p>
     * <p>2、各平台Publish Time、Msrp、Price和Qty</p>
     *
     * @param channelId  渠道ID
     * @param language   语言
     * @param filterList 过滤
     */
    public List<Map<String, String>> getPlatformAttributesCustColumns(String channelId, String language, List<String> filterList) {
        // 设置按销量排序的选择列表
        List<TypeChannelBean> cartList = this.getUsaCartTypeBean(channelId, language);

        List<Map<String, String>> platformAttrList = new ArrayList<>(0);

        for (TypeChannelBean cartObj : cartList) {
            Integer cartId = Integer.valueOf(cartObj.getValue());
            String cartName = cartObj.getName();
            // 自定义列 --->>> 各平台销量
            Map<String, String> keySum7Map = new HashMap<>(2);
            keySum7Map.put("name", String.format("%s (Last 7)", cartName));
            keySum7Map.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_7 + "." + CmsBtProductModel_Sales.CARTID + cartId);
            platformAttrList.add(keySum7Map);

            Map<String, String> keySum30Map = new HashMap<>(2);
            keySum30Map.put("name", String.format("%s (Last 30)", cartName));
            keySum30Map.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_30 + "." + CmsBtProductModel_Sales.CARTID + cartId);
            platformAttrList.add(keySum30Map);

            Map<String, String> keySumYearMap = new HashMap<>(2);
            keySumYearMap.put("name", String.format("%s (Period)", cartName));
            keySumYearMap.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_YEAR + "." + CmsBtProductModel_Sales.CARTID + cartId);
            platformAttrList.add(keySumYearMap);

            Map<String, String> keySumAllMap = new HashMap<>(2);
            keySumAllMap.put("name", String.format("%s (All)", cartName));
            keySumAllMap.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_ALL + "." + CmsBtProductModel_Sales.CARTID + cartId);
            platformAttrList.add(keySumAllMap);

            // 各平台属性 >>> 'Publish Time' && 'Price' && 'Msrp' && 'QTY'
            // TODO: 2017/7/17 rex.wu
            Map<String, String> keyPublishTimeMap = new HashMap<>(2);
            keyPublishTimeMap.put("name", String.format("%s Publish Time", cartName));
            keyPublishTimeMap.put("value", String.format("usPlatforms.P%d.pulishTime", cartId));
            platformAttrList.add(keyPublishTimeMap);

            Map<String, String> keyMarpMap = new HashMap<>(2);
            keyMarpMap.put("name", String.format("%s Msrp", cartName));
            keyMarpMap.put("value", String.format("usPlatforms.P%d.msrp", cartId));
            platformAttrList.add(keyMarpMap);

            Map<String, String> keyPriceMap = new HashMap<>(2);
            keyPriceMap.put("name", String.format("%s Msrp", cartName));
            keyPriceMap.put("value", String.format("usPlatforms.P%d.price", cartId));
            platformAttrList.add(keyPriceMap);

            Map<String, String> keyQtyMap = new HashMap<>(2);
            keyQtyMap.put("name", String.format("%s Msrp", cartName));
            keyQtyMap.put("value", String.format("usPlatforms.P%d.quantity", cartId));
            platformAttrList.add(keyQtyMap);
        }
        return platformAttrList;
    }

    /**
     * 获取Platform Sales
     *
     * @param channelId 渠道ID
     * @param language  语言
     */
    public List<Map<String, String>> getPlatformSalesCustColumns(String channelId, String language) {
        // 设置按销量排序的选择列表
        List<TypeChannelBean> cartList = this.getUsaCartTypeBean(channelId, language);
        List<Map<String, String>> platformsSales = new ArrayList<>();
        for (TypeChannelBean cartObj : cartList) {
            Map<String, String> cartSaleMap = new HashMap<>();
            cartSaleMap.put("cartId", cartObj.getValue());
            cartSaleMap.put("cartName", cartObj.getName());
            cartSaleMap.put("beginTime", "");
            cartSaleMap.put("endTime", "");
            platformsSales.add(cartSaleMap);
        }
        return platformsSales;
    }

    /**
     * 获取USA Cart
     *
     * @param channelId 渠道ID
     * @param language  语言
     */
    private List<TypeChannelBean> getUsaCartTypeBean(String channelId, String language) {
        List<TypeChannelBean> cartBeanList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_D, language);
        if (cartBeanList == null) {
            return Collections.emptyList();
        }
        List<TypeChannelBean> resultCartBeanList = new ArrayList<>();
        for (TypeChannelBean cartBean : cartBeanList) {
            int cartId = NumberUtils.toInt(cartBean.getValue(), -1);
            if (cartId <= 0 || cartId >= 20) continue;
            resultCartBeanList.add(cartBean);
        }
        return resultCartBeanList;
    }

    public void saveUserCustomColumns(String channelId, Integer userId, String username, Map<String, Object> params) {
        List<String> selCommonProps = (List<String>) params.get("selCommonProps");
        List<String> selPlatformAttributes = (List<String>) params.get("selPlatformAttributes");
        List<Map<String, Object>> selPlatformSales = (List<Map<String, Object>>) params.get("selPlatformSales");
        // 保存用户自定义列 --->>> usa_cms_cust_col_common_attr
        if (CollectionUtils.isNotEmpty(selCommonProps)) {
            commonPropService.deleteUserCustColumns(userId, "usa_cms_cust_col_common_attr");
            int size = selCommonProps.size();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < size; i++) {
                sb.append(selCommonProps.get(i));
                if (i != size - 1) {
                    sb.append(",");
                }
            }
            commonPropService.addUserCustColumn(userId, username,"usa_cms_cust_col_common_attr", "", sb.toString());
        }
        // 保存用户自定义列 --->>> usa_cms_cust_col_platform_attr
        if (CollectionUtils.isNotEmpty(selPlatformAttributes)) {
            commonPropService.deleteUserCustColumns(userId, "usa_cms_cust_col_platform_attr");
            int size = selPlatformAttributes.size();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < size; i++) {
                sb.append(selPlatformAttributes.get(i));
                if (i != size - 1) {
                    sb.append(",");
                }
            }
            commonPropService.addUserCustColumn(userId, username,"usa_cms_cust_col_platform_attr", "", sb.toString());
        }
        // 保存用户自定义列 --->>> usa_cms_cust_col_platform_sale
        if (CollectionUtils.isNotEmpty(selPlatformSales)) {
            commonPropService.deleteUserCustColumns(userId, "usa_cms_cust_col_platform_sale");

            for (Map<String, Object> cartMap : selPlatformSales) {
                String cartId = (String) cartMap.get("cartId");
                String beginTime = (String) cartMap.get("beginTime");
                String endTime = (String) cartMap.get("endTime");
                if (StringUtils.isBlank(cartId) || StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)) {
                    throw new BusinessException("Platform sale parameter invalid");
                }
                commonPropService.addUserCustColumn(userId, username,"usa_cms_cust_col_platform_sale", cartId, JacksonUtil.bean2Json(cartMap));
            }
        }
    }
}
