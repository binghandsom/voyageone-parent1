package com.voyageone.web2.cms.views.search;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.impl.cms.CmsBtCustomPropService;
import com.voyageone.service.impl.cms.CommonPropService;
import com.voyageone.service.impl.cms.feed.FeedCustomPropService;
import com.voyageone.service.model.cms.mongo.CmsBtCustomPropModel;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Edward
 * @version 2.0.0, 15/12/14
 */
@Service
public class CmsAdvSearchCustColumnService extends BaseViewService {

    @Autowired
    private CommonPropService commonPropService;
    @Autowired
    private FeedCustomPropService feedCustomPropService;
    @Autowired
    private CmsAdvSearchOtherService advSearchQueryService;

    @Autowired
    private CmsBtCustomPropService cmsBtCustomPropService;

    /**
     * 取得用户已选择的自定义显示列设置（一览画面用）
     *
     */
    public void getUserCustColumns(String channelId, int userId, CmsSessionBean cmsSession, String language) {
        Map<String, Object> rsMap = commonPropService.getCustColumnsByUserId(userId, "cms_prod_cust_col");
        String custAttrStr;
        String commStr;
        if (rsMap == null) {
            $debug("该用户还未设置自定义查询列 userId=" + userId + " channelId=" + channelId);
            custAttrStr = "";
            commStr = "";
        } else {
            custAttrStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) rsMap.get("cfg_val1"));
            commStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) rsMap.get("cfg_val2"));
        }

        // 设置自定义查询用的属性
        List<Map<String, String>> custAttsQueryList = new ArrayList<>();

        List<Map<String, Object>> customProps2 = new ArrayList<>();
        String[] custAttrList = custAttrStr.split(",");
        StringBuilder customPropsStr = new StringBuilder();
        if (custAttrList.length > 0) {
            List<Map<String, Object>> customProps = feedCustomPropService.getFeedCustomPropAttrs(channelId, "");
            for (Map<String, Object> props : customProps) {
                String propId = (String) props.get("feed_prop_original");
                Map<String, String> atts = new HashMap<>(2);
                atts.put("configCode", "feed.cnAtts." + propId);
                atts.put("configValue1", (String) props.get("feed_prop_translation"));
                custAttsQueryList.add(atts);

                if (ArrayUtils.contains(custAttrList, propId)) {
                    customProps2.add(props);
                    customPropsStr.append("feed.cnAtts.");
                    customPropsStr.append(propId);
                    customPropsStr.append(";");
                    customPropsStr.append("feed.orgAtts.");
                    customPropsStr.append(propId);
                    customPropsStr.append(";");
                }
            }
        }
        List<Map<String, Object>> commonProp2 = new ArrayList<>();
        String[] commList = commStr.split(",");
        StringBuilder commonPropsStr = new StringBuilder();
        if (commList.length > 0) {
            List<Map<String, Object>> commonProps = commonPropService.getCustColumns(1);
            for (Map<String, Object> props : commonProps) {
                String propId = (String) props.get("propId");
                if ("comment".equals(propId)) {
                    Map<String, String> atts = new HashMap<>(2);
                    atts.put("configCode", "common.comment");
                    atts.put("configValue1", (String) props.get("propName"));
                    atts.put("valType", (String) props.get("valType"));
                    custAttsQueryList.add(atts);

                    if (ArrayUtils.contains(commList, propId)) {
                        commonProp2.add(props);
                        commonPropsStr.append("common.comment");
                        commonPropsStr.append(";");
                    }
                } else {
                    Map<String, String> atts = new HashMap<>(2);
                    atts.put("configCode", "common.fields." + propId);
                    atts.put("configValue1", (String) props.get("propName"));
                    atts.put("valType", (String) props.get("valType"));
                    custAttsQueryList.add(atts);

                    if (ArrayUtils.contains(commList, propId)) {
                        commonProp2.add(props);
                        commonPropsStr.append("common.fields.");
                        commonPropsStr.append(propId);
                        commonPropsStr.append(";");
                    }
                }
            }
        }

        cmsSession.putAttribute("_adv_search_props_custAttsQueryList", custAttsQueryList);
        cmsSession.putAttribute("_adv_search_props_searchItems", customPropsStr.toString() + commonPropsStr.toString());
        cmsSession.putAttribute("_adv_search_customProps", customProps2);
        cmsSession.putAttribute("_adv_search_commonProps", commonProp2);

        rsMap = commonPropService.getCustColumnsByUserId(userId, "cms_prod_cust_col_salestype");
        List<String> itemList = new ArrayList<>();
        if (rsMap != null) {
            String itemVal = org.apache.commons.lang3.StringUtils.trimToNull((String) rsMap.get("cfg_val1"));
            if (itemVal != null) {
                Collections.addAll(itemList, itemVal.split(","));
            }
        }
        cmsSession.putAttribute("_adv_search_selSalesType", advSearchQueryService.getSalesTypeList(channelId, language, itemList));
    }

    /**
     * 取得用户自定义显示列设置(弹出画面用，用于确定那些项已被选择)
     */
    public Map<String, Object> getUserCustColumns(UserSessionBean userInfo, String language) {
        Map<String, Object> rsMap = new HashMap<>();

        // 取得所有销量数据显示列
        rsMap.put("salesTypeList", advSearchQueryService.getSalesTypeList(userInfo.getSelChannelId(), language, null));
        // 取得已选择的销量数据显示列
        Map<String, Object> colMap2 = commonPropService.getCustColumnsByUserId(userInfo.getUserId(), "cms_prod_cust_col_salestype");
        if (colMap2 == null || colMap2.isEmpty()) {
            rsMap.put("selSalesTypeList", new String[]{});
        } else {
            String selStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) colMap2.get("cfg_val1"));
            rsMap.put("selSalesTypeList", selStr.split(","));
        }

        // 取得所有BI数据显示列
        rsMap.put("biDataList", advSearchQueryService.getBiDataList(userInfo.getSelChannelId(), language, null));
        // 取得已选择的BI数据显示列
        colMap2 = commonPropService.getCustColumnsByUserId(userInfo.getUserId(), "cms_prod_cust_col_bidata");
        if (colMap2 == null || colMap2.isEmpty()) {
            rsMap.put("selBiDataList", new String[]{});
        } else {
            String selStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) colMap2.get("cfg_val1"));
            rsMap.put("selBiDataList", selStr.split(","));
        }

        // 取得所有自定义显示列
        List<Map<String, Object>> custommProps = feedCustomPropService.getFeedCustomPropAttrs(userInfo.getSelChannelId(), "");
        if(!ListUtils.isNull(custommProps)) {
            custommProps = custommProps.stream().filter(item -> !StringUtil.isEmpty((String) item.get("feed_prop_translation"))).collect(Collectors.toList());
        }
        rsMap.put("customProps", custommProps);
        rsMap.put("commonProps", commonPropService.getCustColumns(2));
        // 取得已选择的自定义显示列
        colMap2 = commonPropService.getCustColumnsByUserId(userInfo.getUserId(), "cms_prod_cust_col");
        if (colMap2 == null || colMap2.isEmpty()) {
            rsMap.put("custAttrList", new String[]{});
            rsMap.put("commList", new String[]{});
        } else {
            String custAttrStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) colMap2.get("cfg_val1"));
            String commStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) colMap2.get("cfg_val2"));
            rsMap.put("custAttrList", custAttrStr.split(","));
            rsMap.put("commList", commStr.split(","));
        }

        rsMap.put("platformDataList", advSearchQueryService.getPlatformList(userInfo.getSelChannelId(), language));
        return rsMap;
    }

    /**
     * 保存用户自定义显示列设置
     */
    public void saveCustColumnsInfo(UserSessionBean userInfo, CmsSessionBean cmsSessionBean, Map<String, Object> params, String language) {
        List<String> selCustomPropList = (List<String>) params.get("customProps");
        List<String> selCommonPropList = (List<String>) params.get("commonProps");
        String customStrs = org.apache.commons.lang3.StringUtils.trimToEmpty(org.apache.commons.lang3.StringUtils.join(selCustomPropList, ","));
        String commonStrs = org.apache.commons.lang3.StringUtils.trimToEmpty(org.apache.commons.lang3.StringUtils.join(selCommonPropList, ","));

        List<Map<String, Object>> customProps2 = new ArrayList<>();
        StringBuilder customPropsStr = new StringBuilder();
        if (selCustomPropList != null && selCustomPropList.size() > 0) {
            List<Map<String, Object>> customProps = feedCustomPropService.getFeedCustomPropAttrs(userInfo.getSelChannelId(), "");
            for (Map<String, Object> props : customProps) {
                String propId = (String) props.get("feed_prop_original");
                if (selCustomPropList.contains(propId)) {
                    customProps2.add(props);
                    customPropsStr.append("feed.cnAtts.");
                    customPropsStr.append(propId);
                    customPropsStr.append(";");
                    customPropsStr.append("feed.orgAtts.");
                    customPropsStr.append(propId);
                    customPropsStr.append(";");
                }
            }
        }

        List<Map<String, Object>> commonProp2 = new ArrayList<>();
        StringBuilder commonPropsStr = new StringBuilder();
        if (selCommonPropList != null && selCommonPropList.size() > 0) {
            List<Map<String, Object>> commonProps = commonPropService.getCustColumns(1);
            for (Map<String, Object> props : commonProps) {
                String propId = (String) props.get("propId");
                if (selCommonPropList.contains(propId)) {
                    commonProp2.add(props);
                    if ("comment".equals(propId)) {
                        commonPropsStr.append("common.comment");
                    } else {
                        commonPropsStr.append("common.fields.");
                        commonPropsStr.append(propId);
                    }
                    commonPropsStr.append(";");
                }
            }
        }
        cmsSessionBean.putAttribute("_adv_search_props_searchItems", customPropsStr.toString() + commonPropsStr.toString());
        cmsSessionBean.putAttribute("_adv_search_customProps", customProps2);
        cmsSessionBean.putAttribute("_adv_search_commonProps", commonProp2);

        // 保存feed自定义显示列选择和共通自定义显示列选择
        Map<String, Object> raMap = commonPropService.getCustColumnsByUserId(userInfo.getUserId(), "cms_prod_cust_col");
        int rs;
        if (raMap == null || raMap.isEmpty()) {
            rs = commonPropService.addUserCustColumn(userInfo.getUserId(), userInfo.getUserName(), "cms_prod_cust_col", customStrs, commonStrs);
        } else {
            rs = commonPropService.saveUserCustColumn(userInfo.getUserId(), userInfo.getUserName(), "cms_prod_cust_col", customStrs, commonStrs);
        }
        if (rs == 0) {
            $error("保存自定义显示列设置不成功 userid=" + userInfo.getUserId());
        }

        // 保存销量数据显示列选择
        List<String> selSalesTypeList = (List<String>) params.get("selSalesTypeList");
        raMap = commonPropService.getCustColumnsByUserId(userInfo.getUserId(), "cms_prod_cust_col_salestype");
        String selStr;
        if (selSalesTypeList == null || selSalesTypeList.isEmpty()) {
            selStr = "";
        } else {
            selStr = selSalesTypeList.stream().collect(Collectors.joining(","));
        }
        if (raMap == null || raMap.isEmpty()) {
            rs = commonPropService.addUserCustColumn(userInfo.getUserId(), userInfo.getUserName(), "cms_prod_cust_col_salestype", selStr, "");
        } else {
            rs = commonPropService.saveUserCustColumn(userInfo.getUserId(), userInfo.getUserName(), "cms_prod_cust_col_salestype", selStr, "");
        }
        if (rs == 0) {
            $error("保存自定义销售数据显示设置不成功 userid=" + userInfo.getUserId());
        }
        cmsSessionBean.putAttribute("_adv_search_selSalesType", advSearchQueryService.getSalesTypeList(userInfo.getSelChannelId(), language, selSalesTypeList));

        // 保存BI数据显示列选择
        List<String> selBiDataList = (List<String>) params.get("selBiDataList");
        raMap = commonPropService.getCustColumnsByUserId(userInfo.getUserId(), "cms_prod_cust_col_bidata");
        if (selBiDataList == null || selBiDataList.isEmpty()) {
            selStr = "";
        } else {
            selStr = selBiDataList.stream().collect(Collectors.joining(","));
        }
        if (raMap == null || raMap.isEmpty()) {
            rs = commonPropService.addUserCustColumn(userInfo.getUserId(), userInfo.getUserName(), "cms_prod_cust_col_bidata", selStr, "");
        } else {
            rs = commonPropService.saveUserCustColumn(userInfo.getUserId(), userInfo.getUserName(), "cms_prod_cust_col_bidata", selStr, "");
        }
        if (rs == 0) {
            $error("保存BI数据显示设置不成功 userid=" + userInfo.getUserId());
        }
        cmsSessionBean.putAttribute("_adv_search_selBiDataList", advSearchQueryService.getBiDataList(userInfo.getSelChannelId(), language, selBiDataList));
    }

}
