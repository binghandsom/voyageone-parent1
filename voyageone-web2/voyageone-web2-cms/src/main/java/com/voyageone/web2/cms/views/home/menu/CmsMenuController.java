package com.voyageone.web2.cms.views.home.menu;

import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.ImageServer;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.ChannelCategoryService;
import com.voyageone.service.impl.cms.CmsBtDataAmountService;
import com.voyageone.service.impl.cms.ImageTemplateService;
import com.voyageone.service.impl.cms.PlatformService;
import com.voyageone.service.model.cms.enums.CartType;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

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
    @Autowired
    CmsBtDataAmountService serviceCmsBtDataAmount;
    @Autowired
    ChannelCategoryService channelCategoryService;

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
        resultBean.put("imageUrl", ImageServer.frontendImageUrlTemplate());
        resultBean.put("productUrl", platformService.getPlatformProductUrl(getCmsSession().getPlatformType().get("cartId").toString()));

        //主数据类目+Feed类目
        if (cTypeId.equals(CartType.MASTER.getShortName())) {
            resultBean.put("categoryTreeList", new ArrayList<>());
        }
        else if (cTypeId.equals(CartType.FEED.getShortName())) {
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
        boolean isMiniMall = Channels.isUsJoi(channelId);
        resultBean.put("isminimall", isMiniMall ? 1 : 0);

        // 判断是否只在聚美销售
        List<TypeChannelBean> channelList = TypeChannels.getTypeListSkuCarts(getUser().getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, getLang());
        if (channelList != null && channelList.size() > 0) {
            if (channelList.size() == 1 && "27".equals(channelList.get(0).getValue())) {
                // 只在聚美销售
                resultBean.put("only4jumei", 1);
            } else {
                boolean isJumei = false;
                for (TypeChannelBean channelBean : channelList) {
                    if ("27".equals(channelBean.getValue())) {
                        isJumei = true;
                        break;
                    }
                }
                resultBean.put("only4jumei", isJumei ? 2 : 0);
            }
        } else {
            resultBean.put("only4jumei", "");
        }

        // 返回用户信息
        return success(resultBean);
    }

    /**
     * 返回categoryTypeList
     */
    @RequestMapping(CmsUrlConstants.HOME.MENU.GET_CATE_TYPE)
    public AjaxResponse getPlatformType(@RequestBody Map<String, Object> params) {

        if(params.get("isUsa") != null && Objects.equals(params.get("isUsa"), true))
        {
            // 返回用户信息
            return success(menuService.getUsPlatformTypeList(getUser().getSelChannelId(), getLang()));

        }else{
            // 返回用户信息
            return success(menuService.getPlatformTypeList(getUser().getSelChannelId(), getLang()));
        }
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

    @RequestMapping(CmsUrlConstants.HOME.MENU.GetHomeSumData)
    public AjaxResponse getHomeSumData()
    {
        return success(serviceCmsBtDataAmount.getHomeSumData(getUser().getSelChannelId(), getLang()));
    }

    @RequestMapping(value = CmsUrlConstants.HOME.MENU.SumHome,method = RequestMethod.GET)
    public AjaxResponse SumHome(@RequestParam String channelId) {
        if (StringUtils.isEmpty(channelId)) {
            return success("channelId不能为空");
        }
        serviceCmsBtDataAmount.sumByChannelId(channelId);
        return success("完成");
    }
    @RequestMapping(CmsUrlConstants.HOME.MENU.GET_CMS_CONFIG)
    public AjaxResponse getCmsConfig()
    {
        Map<String,Object> response = new HashMap<>();
        response.put("autoApprovePrice",CmsChannelConfigs.getConfigBeans(getUser().getSelChannelId(), CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_SALE, ""));
        return success(response);
    }

    @RequestMapping(CmsUrlConstants.HOME.MENU.GET_MAIN_CATEGORIES)
    public AjaxResponse getMainCategories() {
        return success(channelCategoryService.getCategoriesByChannelId(getUser().getSelChannelId()));
    }

    @RequestMapping(CmsUrlConstants.HOME.MENU.GET_CARTS)
    public AjaxResponse getCarts() {
        return success(TypeChannels.getTypeListSkuCarts(getUser().getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, getLang()));
    }


    @RequestMapping(CmsUrlConstants.HOME.MENU.GetMenuHeaderInfo)
    public AjaxResponse getMenuHeaderInfo() throws IOException {
        //获取userId和channelId.
        UserSessionBean userSessionBean=getUser();
       String userName= userSessionBean.getUserName();
        Integer userId =userSessionBean.getUserId();
        String channelId = userSessionBean.getSelChannelId();
        Map<String, Object> resultBean =menuService.getMenuHeaderInfo(userId,channelId,userName);


        // 获取用户相关信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userName", getUser().getUserName());
        userInfo.put("application",getUser().getApplication());
        userInfo.put("channelName", getUser().getSelChannel().getFullName());
        userInfo.put("language", getLang());

        resultBean.put("userInfo", userInfo);

        // 返回用户信息
        return success(resultBean);
    }
}
