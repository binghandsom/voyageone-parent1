package com.voyageone.web2.core.views.menu;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.base.BaseConstants;
import com.voyageone.web2.core.CoreConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.service.daoext.com.UserRolePropertyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Index 路径
 * Created on 12/01/15.
 *
 * @author Edward
 * @version 2.0.0
 */
@Service
public class MenuService extends BaseViewService {

    @Autowired
    private UserRolePropertyDao userRolePropertyDao;

    public Map<String, Object> getMenuHeaderInfo(int userId,String channelId,String applicationId) {

        Map<String, Object> resultbean = new HashMap<>();


        // 获取menu列表.
        List<Map<String, Object>> menuList = this.getApplicationList(userId, channelId);
        resultbean.put("applicationList", menuList);
        // 获取language列表.
        List<TypeBean> languageList = this.getLanguageList();
        resultbean.put("languageList", languageList);
        Object menuTree = getMenuTree(Integer.toString(userId), channelId, applicationId);
        resultbean.put("menuTree", menuTree);
        // TODO 临时对应翻译人员对应的权限
        Map<String, Object> param = new HashMap<>();
        param.put("userId", Integer.toString(userId));
        param.put("channelId", channelId);
        param.put("active", 1);
        param.put("roleId", "12");
        int roleList = userRolePropertyDao.selectUserRoleProperties(param);
        resultbean.put("isTranslator", roleList == 1);

        return resultbean;
    }
    /**
     * 根据userId,applicationId和ChannelId获取Menu列表.
     */
    public List<Map<String, Object>> getMenuTree(String userId,String channelId,String applicationId) {

        List<Map<String, Object>> listModule = userRolePropertyDao.selectListModuleByWhere(userId, channelId, applicationId);
        List<Map<String, Object>> listControl = userRolePropertyDao.selectListControllerByWhere(userId, channelId, applicationId);
        List<Map<String, Object>> children = null;
        for (Map<String, Object> map : listModule) {
            children = getControlListByParentId(listControl, map.get("id"));
            map.put("children", children);
        }
        return listModule;
    }
    public List<Map<String,Object>> getControlListByParentId( List<Map<String, Object>> list,Object parentId) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : list) {
            if (map.get("parentId").equals(parentId)) {
                result.add(map);
            }
        }
        return result;
    }
    /**
     * 根据userId和ChannelId获取Application列表.
     */
    public List<Map<String, Object>> getApplicationList (Integer userId, String channelId) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("channelId", channelId);

        return userRolePropertyDao.selectUserApplication(data);
    }

    /**
     * 获取language列表.
     */
    public List<TypeBean> getLanguageList() {
        return TypeConfigEnums.MastType.languageType.getList(Constants.LANGUAGE.EN);
    }

    /**
     * 设定用户选择的语言
     */
    public void setLanguage(HttpSession session, UserSessionBean userInfo, Object lang) {

        String language = Constants.LANGUAGE.EN;

        if (lang != null &&
                Arrays.asList(Constants.LANGUAGE.ALL).contains(lang))
            language = lang.toString();

        // 设置session中的language
        session.setAttribute(BaseConstants.SESSION_LANG, language);

        // 设置用户的默认语言
        if (userInfo.getUserConfig().get(CoreConstants.USER_CONFIG_LANGUAGE_ID) != null)
            userInfo.getUserConfig().get(CoreConstants.USER_CONFIG_LANGUAGE_ID).get(0).setCfg_val1(language);
    }

    /**
     * 取得用的的Menu信息
     */
    public List<Map<String, Object>> getVendorMenuHInfo(UserSessionBean user) {
        // MenuList
        List<Map<String, Object>> menuList = new ArrayList<>();

        // Feed
        Map<String, Object> feedMenu = new HashMap<>();
        feedMenu.put("name", "Feed");
        List<Map<String, String>> feedMenuItems = new ArrayList<>();
        // Feed->Feed File Upload
        Map<String, String> feedFileUpload = new HashMap<>();
        feedFileUpload.put("name", "Product Feed Upload");
        feedFileUpload.put("url", "#/feed/product_feed_upload");
        feedMenuItems.add(feedFileUpload);
        // Feed->Feed Import Result
        Map<String, String> feedImportResult = new HashMap<>();
        feedImportResult.put("name", "Product Feed Import Status");
        feedImportResult.put("url", "#/feed/product_feed_import_status");
        feedMenuItems.add(feedImportResult);
        // Feed->Feed Info Search
        Map<String, String> feedInfoSearch = new HashMap<>();
        feedInfoSearch.put("name", "Product Feed Search");
        feedInfoSearch.put("url", "#/feed/product_feed_search");
        feedMenuItems.add(feedInfoSearch);
        feedMenu.put("items", feedMenuItems);

        // Order
        Map<String, Object> orderMenu = new HashMap<>();
        orderMenu.put("name", "Order");
        List<Map<String, String>> orderMenuItems = new ArrayList<>();
        // Order->Order Info
        Map<String, String> orderInfo = new HashMap<>();
        orderInfo.put("name", "Order Info");
        orderInfo.put("url", "#/order/order_info");
        orderMenuItems.add(orderInfo);
        orderMenu.put("items", orderMenuItems);


        // Reports
        Map<String, Object> reportsMenu = new HashMap<>();
        reportsMenu.put("name", "Reports");
        List<Map<String, String>> reportsMenuItems = new ArrayList<>();
        // Reports->Financial Report
        Map<String, String> financialReport = new HashMap<>();
        financialReport.put("name", "Financial Report");
        financialReport.put("url", "#/reports/financial_report");
        reportsMenuItems.add(financialReport);
        reportsMenu.put("items", reportsMenuItems);

        // Inventory
        Map<String, Object> inventoryMenu = new HashMap<>();
        inventoryMenu.put("name", "Inventory");
        List<Map<String, String>> inventoryMenuItems = new ArrayList<>();
        // Inventory->Inventory Upload
        Map<String, String> inventoryFileUpload = new HashMap<>();
        inventoryFileUpload.put("name", "Inventory&Price Feed Upload");
        inventoryFileUpload.put("url", "#/inventory/inventory_feed_upload");
        inventoryMenuItems.add(inventoryFileUpload);
        inventoryMenu.put("items", inventoryMenuItems);

        // Shipment
        Map<String, Object> shipmentMenu = new HashMap<>();
        shipmentMenu.put("name", "Shipment");
        List<Map<String, String>> shipmentMenuItems = new ArrayList<>();
        // Shipment->Shipment Info
        Map<String, String> shipmentInfo = new HashMap<>();
        shipmentInfo.put("name", "Shipment Info");
        shipmentInfo.put("url", "#/shipment/shipment_info");
        shipmentMenuItems.add(shipmentInfo);
        shipmentMenu.put("items", shipmentMenuItems);

        // Settings
        Map<String, Object> settingsMenu = new HashMap<>();
        settingsMenu.put("name", "Settings");
        List<Map<String, String>> settingsItems = new ArrayList<>();
        // Settings->Vendor Settings
        Map<String, String> vendorSettings = new HashMap<>();
        vendorSettings.put("name", "Vendor Settings");
        vendorSettings.put("url", "#/settings/vendor_settings");
        settingsItems.add(vendorSettings);
        settingsMenu.put("items", settingsItems);


        // TODO 在这里加Menu权限控制
        menuList.add(feedMenu);
        menuList.add(orderMenu);
        menuList.add(reportsMenu);
        menuList.add(inventoryMenu);
        menuList.add(shipmentMenu);
        menuList.add(settingsMenu);

        return menuList;
    }
}
