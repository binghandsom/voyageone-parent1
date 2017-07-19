package com.voyageone.service.impl.cms.usa;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CommonPropService;
import com.voyageone.service.impl.cms.PropService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sales;
import org.apache.commons.collections4.CollectionUtils;
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

    /**
     * 获取用户勾选的自定义列
     *
     * @param channelId 渠道
     * @param userId    用户ID
     * @param language  语言
     */
    public Map<String, Object> getUserCustomColumns(String channelId, Integer userId, String language) {
        Map<String, Object> rsMap = new HashMap<>();
        // 取得已选择Platform Attributes
        Map<String, Object> colMap2 = commonPropService.getCustColumnsByUserId(userId, "usa_cms_cust_col_platform_attr");
        if (colMap2 == null || colMap2.isEmpty()) {
            rsMap.put("selPlatformAttributes", new String[]{});
        } else {
            String selStr = StringUtils.trimToEmpty((String) colMap2.get("cfg_val2"));
            rsMap.put("selPlatformAttributes", selStr.split(","));
        }
        // 取得已选择Common Attributes
        colMap2 = commonPropService.getCustColumnsByUserId(userId, "usa_cms_cust_col_common_attr");
        if (colMap2 == null || colMap2.isEmpty()) {
            rsMap.put("selCommonProps", new String[]{});
        } else {
            String custAttrStr = StringUtils.trimToEmpty((String) colMap2.get("cfg_val2"));
            rsMap.put("selCommonProps", custAttrStr.split(","));
        }
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
            keyPublishTimeMap.put("value", String.format("usPlatforms.P%d.publishTime", cartId));
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
        List<TypeChannelBean> cartBeanList = TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.SKU_CARTS_53, channelId, language);
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
            commonPropService.addUserCustColumn(userId, username, "usa_cms_cust_col_common_attr", "", sb.toString());
        }
        // 保存用户自定义列 --->>> usa_cms_cust_col_platform_attr
        commonPropService.deleteUserCustColumns(userId, "usa_cms_cust_col_platform_attr");
        if (CollectionUtils.isNotEmpty(selPlatformAttributes)) {
            int size = selPlatformAttributes.size();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < size; i++) {
                sb.append(selPlatformAttributes.get(i));
                if (i != size - 1) {
                    sb.append(",");
                }
            }
            commonPropService.addUserCustColumn(userId, username, "usa_cms_cust_col_platform_attr", "", sb.toString());
        }
        // 保存用户自定义列 --->>> usa_cms_cust_col_platform_sale
        commonPropService.deleteUserCustColumns(userId, "usa_cms_cust_col_platform_sale");
        if (CollectionUtils.isNotEmpty(selPlatformSales)) {
            for (Map<String, Object> cartMap : selPlatformSales) {
                String cartId = (String) cartMap.get("cartId");
                String beginTime = (String) cartMap.get("beginTime");
                String endTime = (String) cartMap.get("endTime");
                if (StringUtils.isBlank(cartId) || StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)) {
                    throw new BusinessException("Platform sale parameter invalid");
                }
                commonPropService.addUserCustColumn(userId, username, "usa_cms_cust_col_platform_sale", cartId, JacksonUtil.bean2Json(cartMap));
            }
        }
    }
}
