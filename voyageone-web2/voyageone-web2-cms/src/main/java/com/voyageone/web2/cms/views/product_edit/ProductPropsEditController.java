package com.voyageone.web2.cms.views.product_edit;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.ProductInfoBean;
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

//       String channelId = super.getUser().getSelChannelId();

        String channelId = "200";

        Map<String,Object> categoryInfo = new HashMap<>();

        try {

           ProductInfoBean productInfo = productPropsEditService.getProductInfo(channelId,productId);

           categoryInfo.put("productInfo",productInfo);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return success(categoryInfo);

    }


}
