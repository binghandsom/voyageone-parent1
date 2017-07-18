package com.voyageone.service.impl.cms.usa;

import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CommonPropService;
import com.voyageone.service.impl.cms.feed.FeedCustomPropService;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * USA CMS2 高级检索
 *
 * @Author rex.wu
 * @Create 2017-07-17 13:20
 */
@Service
public class UsaAdvanceSearchService extends BaseService {

    @Autowired
    private CommonPropService commonPropService;
    @Autowired
    private FeedCustomPropService feedCustomPropService;
//    @Autowired
//    private CmsAdvSearchOtherService advSearchQueryService;



    /**
     * 取得用户已选择的自定义显示列设置（一览画面用）
     *
     */
    public void getUserCustColumns(String channelId, int userId, String language) {
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
                if(props.get("feed_prop_translation") == null) continue;
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

//        cmsSession.putAttribute("_adv_search_props_custAttsQueryList", custAttsQueryList);
//        cmsSession.putAttribute("_adv_search_props_searchItems", customPropsStr.toString() + commonPropsStr.toString());
//        cmsSession.putAttribute("_adv_search_customProps", customProps2);
//        cmsSession.putAttribute("_adv_search_commonProps", commonProp2);

        rsMap = commonPropService.getCustColumnsByUserId(userId, "cms_prod_cust_col_salestype");
        List<String> itemList = new ArrayList<>();
        if (rsMap != null) {
            String itemVal = org.apache.commons.lang3.StringUtils.trimToNull((String) rsMap.get("cfg_val1"));
            if (itemVal != null) {
                Collections.addAll(itemList, itemVal.split(","));
            }
        }
//        cmsSession.putAttribute("_adv_search_selSalesType", advSearchQueryService.getSalesTypeList(channelId, language, itemList));
    }

}
