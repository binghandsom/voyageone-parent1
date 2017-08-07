package com.voyageone.service.impl.cms.usa;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.TypeChannelBean;
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
                String[] cartAttrs = ((String)attrMap.get("cfg_val1")).split(",");
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
        if (colMap2 == null || colMap2.isEmpty()) {
            rsMap.put("selPlatformSales", new ArrayList<Map<String, String>>());
        } else {
            List<Map<String, Object>> selPlatformSales = new ArrayList<>();
            for (Map<String, Object> map : platformSales) {
                Map<String, Object> cartMap = new HashMap<>();
                cartMap.put("cartId", map.get("cfg_val2"));
                cartMap.putAll(JacksonUtil.jsonToMap((String) map.get("cfg_val1")));
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
        // 保存用户自定义列 --->>> usa_cms_cust_col_platform_sale

        // 当前用户的自定义列 -> 平台动态日期销售数量, KV: cfg_val2-cfg_val1
        List<Map<String, Object>> platformSales = commonPropService.getMultiCustColumnsByUserId(userId, "usa_cms_cust_col_platform_sale");
        if (platformSales == null) {
            platformSales = Collections.emptyList();
        }
        // 方便起见, 直接删除用户‘usa_cms_cust_col_platform_sale’配置, 再重新初始化
        commonPropService.deleteUserCustColumns(userId, "usa_cms_cust_col_platform_sale");
        if (CollectionUtils.isNotEmpty(selPlatformSales)) {
            // 动态销售日期区间, 所有用户共享, 一人改变全部的用户自定义列对应时间区间都修改
            // group by cartId(cfg_val2), 看所有用户总共选择了多少cartId
            List<String> platformSaleCarts = commonPropService.getUsaPlatformSaleCarts("usa_cms_cust_col_platform_sale");
            if (platformSaleCarts == null) {
                platformSaleCarts = Collections.emptyList();
            }
            boolean saleTimeUpdFlag = false;
            String beginTime = "";
            String endTime = "";
            for (Map<String, Object> cartMap : selPlatformSales) {
                String cartId = (String) cartMap.get("cartId");
                beginTime = (String) cartMap.get("beginTime");
                endTime = (String) cartMap.get("endTime");
                if (StringUtils.isBlank(cartId) || StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)) {
                    throw new BusinessException("Platform sale parameter invalid");
                }

                if (!saleTimeUpdFlag) {
                    // 当前用户的配置记录已删除, 把其他所有用户相关配置的时间区间修改一致
                    cartMap.remove("cartId");
                    commonPropService.updateUsaPlatformSaleTime("usa_cms_cust_col_platform_sale", JacksonUtil.bean2Json(cartMap));
                    saleTimeUpdFlag = true;
                }

                // 逐一添加当前用户平台设置
                cartMap.put("cartId", cartId);
                commonPropService.addUserCustColumn(userId, username, "usa_cms_cust_col_platform_sale", JacksonUtil.bean2Json(cartMap), cartId);

                boolean mqFlag = true;
                for (Map<String, Object> saleMap : platformSales) {
                    String tempCartId = (String) saleMap.get("cfg_val2");
                    if (cartId.equals(tempCartId)) {
                        Map<String, Object> cartTimeMap = JacksonUtil.jsonToMap((String) saleMap.get("cfg_val1"));
                        if (beginTime.equals((String) cartTimeMap.get("beginTime")) && endTime.equals((String) cartTimeMap.get("endTime"))) {
                            mqFlag = false;
                            break;
                        }
                    }
                }

                if (mqFlag) {
                    CmsSaleDataStatisticsMQMessageBody mqMessageBody = new CmsSaleDataStatisticsMQMessageBody();
                    mqMessageBody.setChannelId(channelId);
                    mqMessageBody.setCartId(Integer.valueOf(cartId));
                    mqMessageBody.setStartDate(beginTime);
                    mqMessageBody.setEndDate(endTime);
                    mqMessageBody.setSender(username);
                    // cmsMqSenderService.sendMessage(mqMessageBody);
                    usaSaleDataStatisticsService.SaleDataStatistics(mqMessageBody);

                    platformSaleCarts.remove(cartId);
                }
            }

            if (saleTimeUpdFlag && platformSaleCarts.size() > 0) {
                for (String cartId : platformSaleCarts) {
                    CmsSaleDataStatisticsMQMessageBody mqMessageBody = new CmsSaleDataStatisticsMQMessageBody();
                    mqMessageBody.setChannelId(channelId);
                    mqMessageBody.setCartId(Integer.valueOf(cartId));
                    mqMessageBody.setStartDate(beginTime);
                    mqMessageBody.setEndDate(endTime);
                    mqMessageBody.setSender(username);
                    // cmsMqSenderService.sendMessage(mqMessageBody);
                    usaSaleDataStatisticsService.SaleDataStatistics(mqMessageBody);
                }
            }
        }
    }
}
