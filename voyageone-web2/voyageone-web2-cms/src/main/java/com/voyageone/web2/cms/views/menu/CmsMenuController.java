package com.voyageone.web2.cms.views.menu;

import com.voyageone.cms.service.model.CmsMtCategoryTreeModel;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants.MENU;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Edward, 15/12/2
 * @version 2.0.0
 */
@RestController
@RequestMapping(
        value = MENU.ROOT,
        method = RequestMethod.POST
)
public class CmsMenuController extends CmsController {

    @Autowired
    private CmsMenuService menuService;

    /**
     * 返回categoryType, categoryList, categoryTreeList
     */
    @RequestMapping(MENU.GET_CATE_INFO)
    public AjaxResponse getCategoryInfo() {

        Map<String, Object> resultBean = new HashMap<>();

        String cTypeId = getCmsSession().getCategoryType().get("cTypeId").toString();
        String channelId = getUser().getSelChannelId();

        resultBean.put("categoryType", getCmsSession().getCategoryType());

        // 获取CategoryList
        List<Map<String, Object>> categoryList = new ArrayList<>();
//        List<Map<String, Object>> categoryList = menuService.getCategoryList(cTypeId, channelId);
        resultBean.put("categoryList", categoryList);

        // 获取CategoryTreeList
        List<CmsMtCategoryTreeModel> categoryTreeList = menuService.getCategoryTreeList(cTypeId, channelId);
        resultBean.put("categoryTreeList", categoryTreeList);

        // 返回用户信息
        return success(resultBean);
    }

    /**
     * 返回categoryTypeList
     */
    @RequestMapping(MENU.GET_CATE_TYPE)
    public AjaxResponse getCategoryType() {

        String channelId = getUser().getSelChannelId();

        // 获取CategoryTypeList
        List<Map<String, Object>> categoryTypeList = menuService.getCategoryTypeList(channelId);

        // 返回用户信息
        return success(categoryTypeList);
    }

    /**
     * 设置当前用户的categoryType.
     */
    @RequestMapping(MENU.SET_CATE_TYPE)
    public AjaxResponse setCategoryType(@RequestBody Map<String, Object> params) {

        String cTypeId = (String) params.get("cTypeId");
        Integer cartId = (Integer) params.get("cartId");

        // 如果cTypeId为空,设置成其默认值.
        if (StringUtils.isEmpty(cTypeId)) {
            params.put("cTypeId", CmsConstants.DEFAULT_CATEGORY_TYPE);
        }

        if (cartId == null) {
            params.put("cartId", CmsConstants.DEFAULT_CART_ID);
        }

        getCmsSession().setCategoryType(params);

        return getCategoryInfo();
    }

}
