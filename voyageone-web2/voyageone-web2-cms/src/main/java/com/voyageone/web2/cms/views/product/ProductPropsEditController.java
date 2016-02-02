package com.voyageone.web2.cms.views.product;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.CmsCategoryInfoBean;
import com.voyageone.web2.cms.bean.CmsProductInfoBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lewis on 15-12-16.
 */
@RestController
@RequestMapping(method = RequestMethod.POST,value = CmsUrlConstants.PRODUCT.EDIT.ROOT)
public class ProductPropsEditController extends CmsController{

    @Autowired
    ProductPropsEditService productPropsEditService;

    @RequestMapping(CmsUrlConstants.PRODUCT.EDIT.GET_PRODUCT_INFO)
    public AjaxResponse doGetProductInfo(@RequestBody Map params){

        int productId = Integer.valueOf(params.get("productId").toString());

        String channelId = super.getUser().getSelChannelId();

        Map<String,Object> categoryInfo = new HashMap<>();

        CmsProductInfoBean productInfo = productPropsEditService.getProductInfo(channelId,productId);

        categoryInfo.put("productInfo",productInfo);

        return success(categoryInfo);

    }

    @RequestMapping(CmsUrlConstants.PRODUCT.EDIT.UPDATE_PRODUCT_MASTER_INFO)
    public AjaxResponse doUpdateProductMasterInfo(@RequestBody Map requestMap){

        String channelId = super.getUser().getSelChannelId();

        String user = super.getUser().getUserName();

        String updateTime = productPropsEditService.updateProductMasterInfo(channelId, user, requestMap);

        Map<String,Object> updateInfo = new HashMap<>();

        updateInfo.put("modified",updateTime);

        return success(updateInfo);

    }

    @RequestMapping(CmsUrlConstants.PRODUCT.EDIT.UPDATE_PRODUCT_SKU_INFO)
    public AjaxResponse doUpdateProductSkuInfo(@RequestBody Map requestMap){

        String channelId = super.getUser().getSelChannelId();
        String user = super.getUser().getUserName();
        String categoryId = requestMap.get("categoryId").toString();
        Long productId = Long.valueOf(requestMap.get("productId").toString());
        String categoryFullPath = requestMap.get("categoryFullPath").toString();
        Map skuMap = (Map) requestMap.get("skuFields");
        String modified = requestMap.get("modified").toString();

        String updateTime = productPropsEditService.updateProductSkuInfo(channelId, user, categoryId, productId, modified, categoryFullPath, skuMap);

        Map<String,Object> updateInfo = new HashMap<>();

        updateInfo.put("modified", updateTime);

        return success(updateInfo);

    }

    @RequestMapping(CmsUrlConstants.PRODUCT.EDIT.UPDATE_PRODUCT_ALL_INFO)
    public AjaxResponse doUpdateProductAllInfo(@RequestBody Map requestMap){

        String channelId = super.getUser().getSelChannelId();
        String userName = super.getUser().getUserName();

        String updateTime = productPropsEditService.updateProductAllInfo(channelId, userName,requestMap);

        Map<String,Object> updateInfo = new HashMap<>();

        updateInfo.put("modified", updateTime);

        return success(updateInfo);

    }

    @RequestMapping(CmsUrlConstants.PRODUCT.EDIT.CHANGE_CATEGORY)
    public AjaxResponse doChangeCategory(@RequestBody Map requestMap){

//        String categoryId = requestMap.get("catId").toString();
//
//        CmsCategoryInfoBean categoryInfoBean = productPropsEditService.getCategoryInfo(categoryId);
//
//        Map<String,Object> categoryInfo = new HashMap<>();
//
//        categoryInfo.put("categoryInfo",categoryInfoBean);
//
//        return success(categoryInfo);

        UserSessionBean userSession = super.getUser();

        Map<String,Object> resultMap = productPropsEditService.confirmChangeCategory(requestMap,userSession);

        return success(resultMap);

    }

    /**
     * confirm change category
     * @param requestMap
     * @return
     */
    @RequestMapping(CmsUrlConstants.PRODUCT.EDIT.CONFIRM_CHANGE)
    public AjaxResponse doConfirmChange(@RequestBody Map requestMap){

        UserSessionBean userSession = super.getUser();

        Map<String,Object> resultMap = productPropsEditService.confirmChangeCategory(requestMap,userSession);

        return success(resultMap);

    }

}
