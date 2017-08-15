package com.voyageone.service.impl.cms.usa;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CommonPropService;
import com.voyageone.service.impl.cms.PropService;
import com.voyageone.service.impl.cms.TypeChannelsService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsSaleDataStatisticsMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sales;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private TypeChannelsService typeChannelsService;
    @Autowired
    private CmsMqSenderService cmsMqSenderService;
    @Autowired
    private UsaSaleDataStatisticsService usaSaleDataStatisticsService;

    /**
     * 获取用户勾选的自定义列
     *
     * @param channelId 渠道
     * @param userId    用户ID
     * @param language  语言
     */
    public Map<String, Object> getUserCustomColumns(String channelId, Integer userId, String language) {
        Map<String, Object> rsMap = new HashMap<>();
        // 取得已选择Common Attributes
        Map<String, Object> colMap2 = commonPropService.getCustColumnsByUserId(userId, "usa_cms_cust_col_common_attr");
        if (colMap2 == null || colMap2.isEmpty()) {
            rsMap.put("selCommonProps", new String[]{});
        } else {
            String custAttrStr = StringUtils.trimToEmpty((String) colMap2.get("cfg_val1"));
            rsMap.put("selCommonProps", custAttrStr.split(","));
        }

        // 取得已选择Platform Attributes
        List<Map<String, Object>> platformAttrMapList = commonPropService.getMultiCustColumnsByUserId(userId, "usa_cms_cust_col_platform_attr");
        if (platformAttrMapList == null || platformAttrMapList.isEmpty()) {
            rsMap.put("selPlatformAttributes", new String[]{});
        } else {
            List<Map<String, String>> selPlatformAttributes = new ArrayList<>();
            for (Map<String, Object> attrMap : platformAttrMapList) {
                String cartId = (String) attrMap.get("cfg_val2");
                String[] cartAttrs = ((String) attrMap.get("cfg_val1")).split(",");
                if (cartAttrs != null && cartAttrs.length > 0) {
                    for (String attr : cartAttrs) {
                        Map<String, String> platformAttrMap = new HashMap<>();
                        platformAttrMap.put("cartId", cartId);
                        platformAttrMap.put("value", attr);
                        selPlatformAttributes.add(platformAttrMap);
                    }
                }
            }
            rsMap.put("selPlatformAttributes", selPlatformAttributes);
        }
        List<Map<String, Object>> platformSales = commonPropService.getMultiCustColumnsByUserId(userId, "usa_cms_cust_col_platform_sale");
        if (platformSales == null || platformSales.isEmpty()) {
            rsMap.put("selPlatformSales", new ArrayList<Map<String, String>>());
            // 所有用户Platform Sale共享一个销量日期区间
            // 当前用户为勾选Platform Sale时, 找一条其他用户的usa_cms_cust_col_platform_sale记录, 用以显示日期区间
            Map<String, Object> shareTimeInterval = commonPropService.getOnePlatformSaleWithoutUserId("usa_cms_cust_col_platform_sale");
            if (MapUtils.isNotEmpty(shareTimeInterval)) {
                rsMap.put("shareTimeInterval", JacksonUtil.jsonToMap((String) shareTimeInterval.get("cfg_val1")));
            }
        } else {
            List<Map<String, Object>> selPlatformSales = new ArrayList<>();
            for (Map<String, Object> map : platformSales) {
                Map<String, Object> cartMap = new HashMap<>();
                String cartId = (String) map.get("cfg_val2");
                cartMap.put("cartId", cartId);
                cartMap.putAll(JacksonUtil.jsonToMap((String) map.get("cfg_val1")));
                // 当前平台Platform Sale是否在计算中
                cartMap.put("calculateFlag", CacheHelper.getValueOperation().get("P" + cartId + "_customSale"));
                selPlatformSales.add(cartMap);
            }
            rsMap.put("selPlatformSales", selPlatformSales);
        }
        return rsMap;
    }

    /**
     * 显示用户自定义列,并获取当前用户已勾选的自定义列
     *
     * @param channelId 渠道ID
     * @param userId    用户ID
     * @param language  语言环境
     */
    public Map<String, Object> getCustomColumnsWithChecked(String channelId, Integer userId, String language) {
        Map<String, Object> rsMap = new HashMap<>();

        // 用户勾选的自定义列
        rsMap.putAll(this.getUserCustomColumns(channelId, userId, language));

        // 取得所有Common Attributes
        List<Map<String, String>> commonProps = propService.getPropByType(0);
        commonProps.sort((o1, o2) -> o1.get("propId").toString().compareTo(o2.get("propId").toString()));
        rsMap.put("commonProps", commonProps);

        // 获取所有Platform Attributes
        rsMap.put("platformAttributes", this.getPlatformAttributesCustColumns(channelId, language, null));

        // Platform  Sales
        rsMap.put("platformSales", this.getPlatformSalesCustColumns(channelId, language));
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
        List<TypeChannelBean> cartList = typeChannelsService.getOnlyUsPlatformTypeList(channelId, language);

        List<Map<String, String>> platformAttrList = new ArrayList<>();

        for (TypeChannelBean cartObj : cartList) {
            Integer cartId = Integer.valueOf(cartObj.getValue());
            String cartName = cartObj.getName();
            // 自定义列 --->>> 各平台销量
            Map<String, String> keySum7Map = new HashMap<>();
            keySum7Map.put("cartId", String.valueOf(cartId));
            keySum7Map.put("name", String.format("%s (Last 7)", cartName));
            keySum7Map.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_7 + "." + CmsBtProductModel_Sales.CARTID + cartId);
            // Solr中暂无平台库存, 所以不能排序
            keySum7Map.put("sortFlag", "0");
            platformAttrList.add(keySum7Map);

            Map<String, String> keySum30Map = new HashMap<>();
            keySum30Map.put("cartId", String.valueOf(cartId));
            keySum30Map.put("name", String.format("%s (Last 30)", cartName));
            keySum30Map.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_30 + "." + CmsBtProductModel_Sales.CARTID + cartId);
            // Solr中暂无平台库存, 所以不能排序
            keySum30Map.put("sortFlag", "0");
            platformAttrList.add(keySum30Map);

            Map<String, String> keySumYearMap = new HashMap<>();
            keySumYearMap.put("cartId", String.valueOf(cartId));
            keySumYearMap.put("name", String.format("%s (Period)", cartName));
            keySumYearMap.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_YEAR + "." + CmsBtProductModel_Sales.CARTID + cartId);
            // Solr中暂无平台库存, 所以不能排序
            keySumYearMap.put("sortFlag", "0");
            platformAttrList.add(keySumYearMap);

            Map<String, String> keySumAllMap = new HashMap<>();
            keySumAllMap.put("cartId", String.valueOf(cartId));
            keySumAllMap.put("name", String.format("%s (All)", cartName));
            keySumAllMap.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_ALL + "." + CmsBtProductModel_Sales.CARTID + cartId);
            // Solr中暂无平台库存, 所以不能排序
            keySumAllMap.put("sortFlag", "0");
            platformAttrList.add(keySumAllMap);

            // 各平台属性 >>> 'Publish Time' && 'Price' && 'Msrp' && 'QTY'
            Map<String, String> keyPublishTimeMap = new HashMap<>();
            keyPublishTimeMap.put("cartId", String.valueOf(cartId));
            keyPublishTimeMap.put("name", String.format("%s Publish Time", cartName));
            keyPublishTimeMap.put("value", String.format("usPlatforms.P%d.publishTime", cartId));
            // Solr中暂无平台库存, 所以不能排序
            keyPublishTimeMap.put("sortFlag", "0");
            platformAttrList.add(keyPublishTimeMap);

            Map<String, String> keyMsrpMap = new HashMap<>();
            keyMsrpMap.put("cartId", String.valueOf(cartId));
            keyMsrpMap.put("name", String.format("%s Msrp", cartName));
            keyMsrpMap.put("value", String.format("usPlatforms.P%d.pPriceMsrpSt,usPlatforms.P%d.pPriceMsrpEd", cartId, cartId));
            platformAttrList.add(keyMsrpMap);

            Map<String, String> keyPriceMap = new HashMap<>();
            keyPriceMap.put("cartId", String.valueOf(cartId));
            keyPriceMap.put("name", String.format("%s Price", cartName));
            keyPriceMap.put("value", String.format("usPlatforms.P%d.pPriceSaleSt,usPlatforms.P%d.pPriceSaleEd", cartId, cartId));
            platformAttrList.add(keyPriceMap);

            Map<String, String> keyQtyMap = new HashMap<>();
            keyQtyMap.put("cartId", String.valueOf(cartId));
            keyQtyMap.put("name", String.format("%s Qty", cartName));
            keyQtyMap.put("value", String.format("usPlatforms.P%d.quantity", cartId));
            // Solr中暂无平台库存, 所以不能排序
            keyQtyMap.put("sortFlag", "0");
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
        //  List<TypeChannelBean> cartList = typeChannelsService.getOnlyUsPlatformTypeList(channelId, language);
        // USA CMS 动态销量统计, 追加中国平台
        List<TypeChannelBean> cartList = typeChannelsService.getUsPlatformTypeList(channelId, language);
        List<Map<String, String>> platformsSales = new ArrayList<>();
        for (TypeChannelBean cartObj : cartList) {
            int cartId = NumberUtils.toInt(cartObj.getValue(), -1);
            if (cartId <= 0) continue;
            Map<String, String> cartSaleMap = new HashMap<>();
            cartSaleMap.put("cartId", cartObj.getValue());
            cartSaleMap.put("cartName", cartId < 20 ? cartObj.getName() : cartObj.getAdd_name2());
            cartSaleMap.put("beginTime", "");
            cartSaleMap.put("endTime", "");
            platformsSales.add(cartSaleMap);
        }

        // 追加一个Total
        Map<String, String> totalSaleMap = new HashMap<>();
        totalSaleMap.put("cartId", "-1");
        totalSaleMap.put("cartName", "Total");
        totalSaleMap.put("beginTime", "");
        totalSaleMap.put("endTime", "");
        platformsSales.add(totalSaleMap);
        return platformsSales;
    }

    public void saveUserCustomColumns(String channelId, Integer userId, String username, Map<String, Object> params) {
        List<String> selCommonProps = (List<String>) params.get("selCommonProps");
        Map<String, List<String>> selPlatformAttributes = (Map<String, List<String>>) params.get("selPlatformAttributes");
        List<Map<String, Object>> selPlatformSales = (List<Map<String, Object>>) params.get("selPlatformSales");
        // 保存用户自定义列 --->>> usa_cms_cust_col_common_attr
        commonPropService.deleteUserCustColumns(userId, "usa_cms_cust_col_common_attr");
        if (CollectionUtils.isNotEmpty(selCommonProps)) {
            int size = selCommonProps.size();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < size; i++) {
                sb.append(selCommonProps.get(i));
                if (i != size - 1) {
                    sb.append(",");
                }
            }
            commonPropService.addUserCustColumn(userId, username, "usa_cms_cust_col_common_attr", sb.toString(), "");
        }
        // 保存用户自定义列 --->>> usa_cms_cust_col_platform_attr
        commonPropService.deleteUserCustColumns(userId, "usa_cms_cust_col_platform_attr");
        if (MapUtils.isNotEmpty(selPlatformAttributes)) {
            for (Map.Entry<String, List<String>> platformEntry : selPlatformAttributes.entrySet()) {
                String cartId = platformEntry.getKey();
                List<String> cartAttributes = platformEntry.getValue();
                if (CollectionUtils.isEmpty(cartAttributes)) {
                    continue;
                }
                int size = cartAttributes.size();
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < size; i++) {
                    sb.append(cartAttributes.get(i));
                    if (i != size - 1) {
                        sb.append(",");
                    }
                }
                commonPropService.addUserCustColumn(userId, username, "usa_cms_cust_col_platform_attr", sb.toString(), cartId);
            }
        }

        // 当前用户勾选了Platform Sale
        if (CollectionUtils.isNotEmpty(selPlatformSales)) {
            List<CmsSaleDataStatisticsMQMessageBody> mqMessageBodies = new ArrayList<>();
            // 所有用户勾选的Platform Sale
            List<Map<String, Object>> platformSaleCarts = commonPropService.getUsaPlatformSaleCarts("usa_cms_cust_col_platform_sale");
            // 所有用户勾选的Platform Sale -> List<Map> 转化为 Map
            Map<String, String> platformSaleInfoMap = new HashMap<>();
            // 所有用户勾选的Platform Sale 共享同一个时间区间, 记录之前的时间区间
            String beginTimeVal = "";
            String endTimeVal = "";
            if (CollectionUtils.isNotEmpty(platformSaleCarts)) {
                for (Map<String, Object> platformSaleMap : platformSaleCarts) {
                    platformSaleInfoMap.put((String) platformSaleMap.get("cfg_val2"), (String) platformSaleMap.get("cfg_val1"));
                    // 记录一下之前的时间区间
                    if (StringUtils.isBlank(beginTimeVal) || StringUtils.isBlank(endTimeVal)) {
                        Map<String, Object> tempSaleTimeMap = JacksonUtil.jsonToMap((String) platformSaleMap.get("cfg_val1"));
                        beginTimeVal = (String) tempSaleTimeMap.get("beginTime");
                        endTimeVal = (String) tempSaleTimeMap.get("endTime");
                    }
                }
            }

            // 当前用户勾选的Platform Sale
            List<Map<String, Object>> platformSales = commonPropService.getMultiCustColumnsByUserId(userId, "usa_cms_cust_col_platform_sale");
            if (platformSales == null) {
                platformSales = Collections.emptyList();
            }

            // 方便起见, 直接删除当前用户 Platform Sale
            commonPropService.deleteUserCustColumns(userId, "usa_cms_cust_col_platform_sale");
            // 本次勾选时间和之前时间区间是否发生变化了
            boolean saleTimeUpdFlag = false;
            for (Map<String, Object> selPlatformSaleMap : selPlatformSales) {
                String cartId = (String) selPlatformSaleMap.get("cartId");
                String beginTime = (String) selPlatformSaleMap.get("beginTime");
                String endTime = (String) selPlatformSaleMap.get("endTime");
                if (StringUtils.isBlank(cartId) || StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)) {
                    throw new BusinessException("Platform sale parameter invalid");
                }
                selPlatformSaleMap.remove("cartId");
                boolean mqFlag = true;
                if (platformSaleInfoMap.containsKey(cartId)) {
                    Map<String, Object> tempCartTimeMap = JacksonUtil.jsonToMap(platformSaleInfoMap.get(cartId));
                    if (tempCartTimeMap != null && beginTime.equals((String) tempCartTimeMap.get("beginTime")) && endTime.equals((String) tempCartTimeMap.get("endTime"))) {
                        mqFlag = false;
                    }
                    platformSaleInfoMap.remove(cartId);
                }
                if (mqFlag) {
                    CmsSaleDataStatisticsMQMessageBody messageBody = new CmsSaleDataStatisticsMQMessageBody();
                    messageBody.setCartId(Integer.valueOf(cartId));
                    messageBody.setStartDate(beginTime);
                    messageBody.setEndDate(endTime);
                    messageBody.setSender(username);
                    messageBody.setChannelId(channelId);
                    mqMessageBodies.add(messageBody);
                }

                commonPropService.addUserCustColumn(userId, username, "usa_cms_cust_col_platform_sale", JacksonUtil.bean2Json(selPlatformSaleMap), cartId);

                if ((!beginTimeVal.equals(beginTime) || !endTimeVal.equals(endTime)) && !saleTimeUpdFlag) {
                    beginTimeVal = beginTime;
                    endTimeVal = endTime;
                    // 把其他用户勾选的Platform Sale 的时间区间更改为一致
                    commonPropService.updateUsaPlatformSaleTime("usa_cms_cust_col_platform_sale", JacksonUtil.bean2Json(selPlatformSaleMap), username);
                    saleTimeUpdFlag = true;
                }
            }

            if (saleTimeUpdFlag && MapUtils.isNotEmpty(platformSaleInfoMap)) {
                for (Map.Entry<String, String> platformSaleInfoEntry : platformSaleInfoMap.entrySet()) {
                    CmsSaleDataStatisticsMQMessageBody messageBody = new CmsSaleDataStatisticsMQMessageBody();
                    messageBody.setCartId(Integer.valueOf(platformSaleInfoEntry.getKey()));
                    messageBody.setStartDate(beginTimeVal);
                    messageBody.setEndDate(endTimeVal);
                    messageBody.setSender(username);
                    messageBody.setChannelId(channelId);
                    mqMessageBodies.add(messageBody);
                }
            }

            if (!mqMessageBodies.isEmpty()) {
                // mqMessageBodies.forEach(messageBody -> usaSaleDataStatisticsService.SaleDataStatistics(messageBody));

                mqMessageBodies.forEach(messageBody -> {
                    CacheHelper.getValueOperation().set("P" + messageBody.getCartId() + "_customSale", messageBody.getStartDate() + messageBody.getEndDate());
                    cmsMqSenderService.sendMessage(messageBody);
                });
            }

        } else {
            // 当前用户此次未勾选Platform Sale, 尝试删除之前勾选的usa_cms_cust_col_platform_sale的记录
            commonPropService.deleteUserCustColumns(userId, "usa_cms_cust_col_platform_sale");
        }
    }
}
