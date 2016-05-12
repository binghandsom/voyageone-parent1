package com.voyageone.web2.cms.views.system.category;

import com.voyageone.service.impl.cms.CategoryTreeService;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JiangJusheng on 2016/05/09
 */
@RestController
@RequestMapping( value = "/cms/system/category/setting", method = RequestMethod.POST )
public class CmsCategorySettingController extends CmsController {

    @Autowired
    private CategoryTreeService categoryTreeService;
    @Autowired
    private PlatformCategoryService platformCategoryService;

    // 取得主数据指定类目的子类目,没有指定时取一级类目
    @RequestMapping("getMasterSubCategoryList")
    public AjaxResponse getMasterSubCategoryList(@RequestBody Map param) {
        int catLvl = (Integer) param.get("catLevel");
        String catId = StringUtils.trimToNull((String) param.get("catId"));

        Map<String, Object> resultBean = new HashMap<>();
        if (catLvl == 0 && catId == null) {
            List<CmsMtCategoryTreeModel> rsList = categoryTreeService.getFstLvlMasterCategory();
            resultBean.put("catList", rsList);
        } else if (catId != null) {
            String rootCatId = StringUtils.trimToNull((String) param.get("rootCatId"));
            List<CmsMtCategoryTreeModel> rsList = categoryTreeService.findCategoryListByCatId(rootCatId, catLvl, catId);
            resultBean.put("catList", rsList);
        }

        // 返回类目信息
        return success(resultBean);
    }

    // 取得平台数据指定类目的子类目,没有指定时取一级类目
    @RequestMapping("getPlatformSubCategoryList")
    public AjaxResponse getPlatformSubCategoryList(@RequestBody Map param) {
        UserSessionBean user = getUser();
        String channelId = user.getSelChannelId();

        int catLvl = (Integer) param.get("catLevel");
        String catId = StringUtils.trimToNull((String) param.get("catId"));
        int cartId = NumberUtils.toInt((String) param.get("cartId"));

        Map<String, Object> resultBean = new HashMap<>();
        if (catLvl == 0 && catId == null) {
            List<CmsMtPlatformCategoryTreeModel> rsList = platformCategoryService.getFstLvlCategory(channelId, cartId);
            resultBean.put("catList", rsList);
        } else if (catId != null) {
            String rootCatId = StringUtils.trimToNull((String) param.get("rootCatId"));
            List<CmsMtPlatformCategoryTreeModel> rsList = platformCategoryService.findCategoryListByCatId(channelId, cartId, rootCatId, catLvl, catId);
            resultBean.put("catList", rsList);
        }

        // 返回类目信息
        return success(resultBean);
    }
}
