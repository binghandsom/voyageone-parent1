package com.voyageone.web2.cms.views.product;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lewis on 15-12-16.
 *
 * @author lewis
 */
@RestController
@RequestMapping(method = RequestMethod.POST, value = CmsUrlConstants.PRODUCT.DETAIL.ROOT)
public class CmsProductPlatformDetailController extends CmsController {

    @Autowired
    private CmsProductPlatformDetailService cmsProductPlatformDetailService;

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.GET_PRODUCT_PLATFORM)
    public AjaxResponse doGetProductPlatform(@RequestBody Map params) {
        Long prodId = Long.parseLong(String.valueOf(params.get("prodId")));

        String channelId = getUser().getSelChannelId();
        int cartId = Integer.parseInt(String.valueOf(params.get("cartId")));
        Map<String, Object> result = new HashMap<>();

        result.put("mastData", cmsProductPlatformDetailService.getProductMastData(channelId, prodId, cartId));
        result.put("platform", cmsProductPlatformDetailService.getProductPlatform(channelId, prodId, cartId));

        return success(result);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.CHANGE_PLATFORM_CATEGORY)
    public AjaxResponse doChangePlatformCategory(@RequestBody Map params) {
        Long prodId = Long.parseLong(String.valueOf(params.get("prodId")));

        String channelId = getUser().getSelChannelId();
        int cartId = Integer.parseInt(String.valueOf(params.get("cartId")));
        String catId = String.valueOf(params.get("catId"));
        Map<String, Object> result = new HashMap<>();

        result.put("platform", cmsProductPlatformDetailService.changePlatformCategory(channelId, prodId, cartId, catId));

        return success(result);
    }
    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.UPDATE_PRODUCT_PLATFORM)
    public AjaxResponse doUpdateProductPlatform(@RequestBody Map params) {
        Long prodId = Long.parseLong(String.valueOf(params.get("prodId")));

        String channelId = getUser().getSelChannelId();

        Map<String, Object> result = new HashMap<>();

        Map<String,Object> platform = (Map<String, Object>) params.get("platform");

        result.put("platform", cmsProductPlatformDetailService.updateProductPlatform(channelId, prodId, platform));

        return success(result);
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.UPDATE_PRODUCT_PLATFORM_CHK)
    public AjaxResponse doUpdateProductPlatformChk(@RequestBody Map params) {

        Long prodId = Long.parseLong(String.valueOf(params.get("prodId")));

        String channelId = getUser().getSelChannelId();

        Map<String, Object> result = new HashMap<>();

        Map<String,Object> platform = (Map<String, Object>) params.get("platform");
        String errcode = cmsProductPlatformDetailService.priceChk(channelId, prodId, platform);

        if(errcode != null){
            throw new BusinessException(errcode);
        }else{
            return doUpdateProductPlatform(params);
        }
    }
}

