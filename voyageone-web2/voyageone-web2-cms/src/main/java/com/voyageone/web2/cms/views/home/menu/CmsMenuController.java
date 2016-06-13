package com.voyageone.web2.cms.views.home.menu;

import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.ImageTemplateService;
import com.voyageone.service.impl.cms.PlatformService;
import com.voyageone.service.model.cms.enums.CartType;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
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
        value = CmsUrlConstants.HOME.MENU.ROOT,
        method = RequestMethod.POST
)
public class CmsMenuController extends CmsController {

    @Autowired
    private CmsMenuService menuService;
    @Autowired
    ImageTemplateService imageTemplateService;
    @Autowired
    PlatformService platformService;

    /**
     * 返回categoryType, categoryList, categoryTreeList
     */
    @RequestMapping(CmsUrlConstants.HOME.MENU.GET_CATE_INFO)
    public AjaxResponse getCategoryInfo(){
        Map<String, Object> resultBean = new HashMap<>();

        String cTypeId = getCmsSession().getPlatformType().get("cTypeId").toString();
        int cartId =  Integer.parseInt(getCmsSession().getPlatformType().get("cartId").toString());
        String channelId = getUser().getSelChannelId();

        resultBean.put("platformType", getCmsSession().getPlatformType());

        // 获取CategoryList
        List<Map<String, Object>> categoryList = new ArrayList<>();
//        List<Map<String, Object>> categoryList = menuService.getCategoryList(cTypeId, channelId);
        resultBean.put("categoryList", categoryList);
        resultBean.put("imageUrl", imageTemplateService.getDefaultImageUrl());
        resultBean.put("productUrl", platformService.getPlatformProductUrl(getCmsSession().getPlatformType().get("cartId").toString()));

        //主数据类目+Feed类目
        if (cTypeId.equals(CartType.MASTER.getShortName()) || cTypeId.equals(CartType.FEED.getShortName())) {
            // 获取主数据类目CategoryTreeList

            List<CmsMtCategoryTreeModel> categoryTreeList = menuService.getCategoryTreeList(cTypeId, channelId);

            resultBean.put("categoryTreeList", categoryTreeList);
        }
        //店铺自定义类目
        else
        {
            List<CmsBtSellerCatModel> cmsBtSellerCatList = menuService.getSellerCatTreeList(channelId, cartId);
            resultBean.put("categoryTreeList", cmsBtSellerCatList);
        }



        // 判断是否是minimall用户
        boolean isMiniMall = channelId.equals(ChannelConfigEnums.Channel.VOYAGEONE.getId());
        resultBean.put("isminimall", isMiniMall ? 1 : 0);
        // 返回用户信息
        return success(resultBean);
    }

    /**
     * 返回categoryTypeList
     */
    @RequestMapping(CmsUrlConstants.HOME.MENU.GET_CATE_TYPE)
    public AjaxResponse getPlatformType() {

        // 返回用户信息
        return success(menuService.getPlatformTypeList(getUser().getSelChannelId(), getLang()));
    }

    /**
     * 设置当前用户的categoryType.
     */
    @RequestMapping(CmsUrlConstants.HOME.MENU.SET_CATE_TYPE)
    public AjaxResponse setPlatformType(@RequestBody Map<String, Object> params) {

        String cTypeId = (String) params.get("cTypeId");
        Integer cartId = Integer.valueOf(params.get("cartId").toString());

        // 如果cTypeId为空,设置成其默认值.
        if (StringUtils.isEmpty(cTypeId)) {
            params.put("cTypeId", CartType.MASTER.getShortName());
        }

        if (cartId == null) {
            params.put("cartId", CartType.MASTER.getCartId());
        }

        getCmsSession().setPlatformType(params);

        return getCategoryInfo();
    }

}
