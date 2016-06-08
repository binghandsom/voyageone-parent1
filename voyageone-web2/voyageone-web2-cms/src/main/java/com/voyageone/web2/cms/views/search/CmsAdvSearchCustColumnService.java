package com.voyageone.web2.cms.views.search;

import com.voyageone.service.daoext.cms.CmsMtCommonPropDaoExt;
import com.voyageone.service.impl.cms.CommonPropService;
import com.voyageone.service.impl.cms.feed.FeedCustomPropService;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.core.bean.UserSessionBean;
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
public class CmsAdvSearchCustColumnService extends BaseAppService {

    @Autowired
    private CommonPropService commonPropService;
    @Autowired
    private FeedCustomPropService feedCustomPropService;
    @Autowired
    private CmsMtCommonPropDaoExt cmsMtCommonPropDaoExt;
    @Autowired
    private CmsAdvSearchQueryService advSearchQueryService;

    /**
     * 取得用户自定义显示列设置
     *
     */
    public void getUserCustColumns(String channelId, int userId, CmsSessionBean cmsSession, String language) {
        List<Map<String, Object>> rsList = commonPropService.getCustColumnsByUserId(userId);
        String custAttrStr;
        String commStr;
        if (rsList == null || rsList.isEmpty()) {
            $debug("该用户还未设置自定义查询列 userId=" + userId + " channelId=" + channelId);
            custAttrStr = "";
            commStr = "";
        } else {
            custAttrStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) rsList.get(0).get("cfg_val1"));
            commStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) rsList.get(0).get("cfg_val2"));
        }

        // 设置自定义查询用的属性
        List<Map<String, String>> custAttsQueryList = new ArrayList<>();

        List<Map<String, Object>> customProps2 = new ArrayList<>();
        String[] custAttrList = custAttrStr.split(",");
        StringBuilder customPropsStr = new StringBuilder();
        if (custAttrList.length > 0) {
            List<Map<String, Object>> customProps = feedCustomPropService.getFeedCustomPropAttrs(channelId, "0");
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
            List<Map<String, Object>> commonProps = commonPropService.getCustColumns();
            for (Map<String, Object> props : commonProps) {
                String propId = (String) props.get("propId");
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

        cmsSession.putAttribute("_adv_search_props_custAttsQueryList", custAttsQueryList);
        cmsSession.putAttribute("_adv_search_props_searchItems", customPropsStr.toString() + commonPropsStr.toString());
        cmsSession.putAttribute("_adv_search_customProps", customProps2);
        cmsSession.putAttribute("_adv_search_commonProps", commonProp2);

        List<Map<String, Object>> rsList2 = cmsMtCommonPropDaoExt.selectUserCustColumnsSalesType(userId);
        List<String> itemList = new ArrayList<>();
        if (!rsList2.isEmpty()) {
            String itemVal = org.apache.commons.lang3.StringUtils.trimToNull((String) rsList2.get(0).get("cfg_val2"));
            if (itemVal != null) {
                Collections.addAll(itemList, itemVal.split(","));
            }
        }
        cmsSession.putAttribute("_adv_search_selSalesType", advSearchQueryService.getSalesTypeList(channelId, language, itemList));
    }


    /**
     * 取得用户自定义显示列设置
     *
     */
    public Map<String, Object> getUserCustColumns(UserSessionBean userInfo, String language) {
        Map<String, Object> rsMap = new HashMap<>();
        rsMap.put("salesTypeList", advSearchQueryService.getSalesTypeList(userInfo.getSelChannelId(), language, null));

        List<Map<String, Object>> rsList2 = cmsMtCommonPropDaoExt.selectUserCustColumnsSalesType(userInfo.getUserId());
        if (rsList2 == null || rsList2.isEmpty()) {
            rsMap.put("selSalesTypeList", new String[]{});
        } else {
            String selStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) rsList2.get(0).get("cfg_val2"));
            rsMap.put("selSalesTypeList", selStr.split(","));
        }

        List<Map<String, Object>> rsList = commonPropService.getCustColumnsByUserId(userInfo.getUserId());
        if (rsList == null || rsList.isEmpty()) {
            rsMap.put("custAttrList", new String[]{});
            rsMap.put("commList", new String[]{});
            return rsMap;
        }
        String custAttrStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) rsList.get(0).get("cfg_val1"));
        String commStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) rsList.get(0).get("cfg_val2"));
        rsMap.put("custAttrList", custAttrStr.split(","));
        rsMap.put("commList", commStr.split(","));
        return rsMap;
    }

    /**
     * 保存用户自定义显示列设置
     */
    public void saveCustColumnsInfo(UserSessionBean userInfo, CmsSessionBean cmsSessionBean, List<String> param1, List<String> param2, String language, List<String> selSalesTypeList) {
        String customStrs = org.apache.commons.lang3.StringUtils.trimToEmpty(org.apache.commons.lang3.StringUtils.join(param1, ","));
        String commonStrs = org.apache.commons.lang3.StringUtils.trimToEmpty(org.apache.commons.lang3.StringUtils.join(param2, ","));

        List<Map<String, Object>> customProps2 = new ArrayList<>();
        StringBuilder customPropsStr = new StringBuilder();
        if (param1 != null && param1.size() > 0) {
            List<Map<String, Object>> customProps = feedCustomPropService.getFeedCustomPropAttrs(userInfo.getSelChannelId(), "0");
            for (Map<String, Object> props : customProps) {
                String propId = (String) props.get("feed_prop_original");
                if (param1.contains(propId)) {
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
        if (param2 != null && param2.size() > 0) {
            List<Map<String, Object>> commonProps = commonPropService.getCustColumns();
            for (Map<String, Object> props : commonProps) {
                String propId = (String) props.get("propId");
                if (param2.contains(propId)) {
                    commonProp2.add(props);
                    commonPropsStr.append("common.fields.");
                    commonPropsStr.append(propId);
                    commonPropsStr.append(";");
                }
            }
        }
        cmsSessionBean.putAttribute("_adv_search_props_searchItems", customPropsStr.toString() + commonPropsStr.toString());
        cmsSessionBean.putAttribute("_adv_search_customProps", customProps2);
        cmsSessionBean.putAttribute("_adv_search_commonProps", commonProp2);

        List<Map<String, Object>> rsList = commonPropService.getCustColumnsByUserId(userInfo.getUserId());
        int rs;
        if (rsList == null || rsList.isEmpty()) {
            rs = commonPropService.addUserCustColumn(userInfo.getUserId(), userInfo.getUserName(), customStrs, commonStrs);
        } else {
            rs = commonPropService.saveUserCustColumn(userInfo.getUserId(), userInfo.getUserName(), customStrs, commonStrs);
        }
        if (rs == 0) {
            $error("保存自定义显示列设置不成功 userid=" + userInfo.getUserId());
        }

        rsList = cmsMtCommonPropDaoExt.selectUserCustColumnsSalesType(userInfo.getUserId());
        String selStr;
        if (selSalesTypeList == null || selSalesTypeList.isEmpty()) {
            selStr = "";
        } else {
            selStr = selSalesTypeList.stream().collect(Collectors.joining(","));
        }
        if (rsList == null || rsList.isEmpty()) {
            rs = cmsMtCommonPropDaoExt.insertUserCustColumnsSalesType(userInfo.getUserId(), userInfo.getUserName(), selStr);
        } else {
            rs = cmsMtCommonPropDaoExt.updateUserCustColumnsSalesType(userInfo.getUserId(), userInfo.getUserName(), selStr);
        }
        if (selSalesTypeList == null) {
            selSalesTypeList = new ArrayList<>(0);
        }
        cmsSessionBean.putAttribute("_adv_search_selSalesType", advSearchQueryService.getSalesTypeList(userInfo.getSelChannelId(), language, selSalesTypeList));
        if (rs == 0) {
            $error("保存自定义销售数据显示设置不成功 userid=" + userInfo.getUserId());
        }
    }

}
