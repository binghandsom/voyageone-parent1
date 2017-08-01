package com.voyageone.web2.cms.views.home.menu;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.service.bean.com.AdminResourceBean;
import com.voyageone.service.daoext.com.UserRolePropertyDao;
import com.voyageone.service.impl.cms.SellerCatService;
import com.voyageone.service.impl.cms.TypeChannelsService;
import com.voyageone.service.impl.com.user.AdminResService;
import com.voyageone.service.model.cms.enums.CartType;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.service.impl.cms.ChannelCategoryService;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeModel;
import com.voyageone.web2.base.BaseViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 15/12/2
 */
@Service
public class CmsMenuService extends BaseViewService {

    @Autowired
    private ChannelCategoryService cmsBtChannelCategoryService;

    @Autowired
    private CmsFeedCategoriesService cmsFeedCategoriesService;

    @Autowired
    private SellerCatService sellerCatService;

    @Autowired
    TypeChannelsService typeChannelsService;
    /**
     * 获取该channel的category类型.
     * @param channelId
     * @return
     */
    public List<TypeChannelBean> getPlatformTypeList (String channelId, String language) {
        return typeChannelsService.getPlatformTypeList(channelId, language);
    }

    public List<TypeChannelBean> getUsPlatformTypeList (String channelId, String language) {
        return typeChannelsService.getUsPlatformTypeList(channelId, language);
    }

    /**
     * 根据userId和ChannelId获取Menu列表.
     */
    public List<CmsMtCategoryTreeModel> getCategoryTreeList (String cTypeId, String channelId){
        try {
            List<CmsMtCategoryTreeModel> categoryTreeList = null;
            if (CartType.FEED.getShortName().equals(cTypeId)) {
                // 获取Feed类目 (mongo:cms_mt_feed_category_tree)
                categoryTreeList = cmsFeedCategoriesService.getFeedCategoryMap(channelId);
            } else if (CartType.MASTER.getShortName().equals(cTypeId)) {
                // 取得主数据的类目树 (mongo:cms_mt_category_tree)
                categoryTreeList = cmsBtChannelCategoryService.getCategoriesByChannelId(channelId);
            }

            return categoryTreeList;
        } catch (IOException ex) {
            $error("获取类目失败", ex);
            throw new BusinessException("获取类目失败");
        }
    }

    /**
     * 根据userId和ChannelId获取Menu列表.
     */
    public List<CmsBtSellerCatModel> getSellerCatTreeList ( String channelId,  int cartId){
        return sellerCatService.getSellerCatsByChannelCart(channelId, cartId);
    }


    @Autowired
    private UserRolePropertyDao userRolePropertyDao;
    @Autowired
    AdminResService adminResService;

    public Map<String, Object> getMenuHeaderInfo(int userId, String channelId,String userName) throws IOException {

        Map<String, Object> resultbean = new HashMap<>();


        // 获取menu列表.
        List<Map<String, Object>> menuList = this.getApplicationList(userId, channelId);
        resultbean.put("applicationList", menuList);
        // 获取language列表.
        List<TypeBean> languageList = this.getLanguageList();
        resultbean.put("languageList", languageList);
        //feed分类
        List<CmsMtCategoryTreeModel> categoryTreeList = cmsFeedCategoriesService.getFeedCategoryTree(channelId);
        resultbean.put("feedCategoryTreeList", categoryTreeList);

        //菜单
        List<AdminResourceBean> list = adminResService.getMenu("cms", userName);
        resultbean.put("menuTree", list);

//        Object menuTree = getMenuTree(Integer.toString(userId), channelId, applicationId);
//        resultbean.put("menuTree", menuTree);
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
//    /**
//     * 根据userId,applicationId和ChannelId获取Menu列表.
//     */
//    public List<Map<String, Object>> getMenuTree(String userId,String channelId,String applicationId) {
//
//        List<Map<String, Object>> listModule = userRolePropertyDao.selectListModuleByWhere(userId, channelId, applicationId);
//        List<Map<String, Object>> listControl = userRolePropertyDao.selectListControllerByWhere(userId, channelId, applicationId);
//        List<Map<String, Object>> children = null;
//        for (Map<String, Object> map : listModule) {
//            children = getControlListByParentId(listControl, map.get("id"));
//            map.put("children", children);
//        }
//        return listModule;
//    }
//    public List<Map<String,Object>> getControlListByParentId( List<Map<String, Object>> list,Object parentId) {
//        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
//        for (Map<String, Object> map : list) {
//            if (map.get("parentId").equals(parentId)) {
//                result.add(map);
//            }
//        }
//        return result;
//    }
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
}
