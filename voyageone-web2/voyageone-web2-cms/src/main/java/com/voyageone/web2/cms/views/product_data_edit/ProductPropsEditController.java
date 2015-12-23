package com.voyageone.web2.cms.views.product_data_edit;

import com.voyageone.cms.service.model.CmsMtCategorySchemaWithValueModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
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

        CmsMtCategorySchemaWithValueModel schemaModel = productPropsEditService.handleInit(getUser().getSelChannelId(),productId);
        Map<String,Object> categorySchema = new HashMap<>();
        categorySchema.put("categorySchema",schemaModel);
        return success(categorySchema);
//        Map<String,Object> response = new HashMap<>();

//        String word = "Hi, "+productId+", Hello World!!!";
//        response.put("sayHello",word);
//        return success(response);
    }


}
