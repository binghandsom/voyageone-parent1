package com.voyageone.web2.cms.views.product_data_edit;

import com.voyageone.cms.service.model.CmsMtCategorySchemaWithValueModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lewis on 15-12-16.
 */
@RestController
@RequestMapping(method = RequestMethod.POST,value = CmsUrlConstants.PRODUCT.EDIT.ROOT)
public class ProductPropsEditController extends CmsController{

    @Autowired
    ProductPropsEditService productPropsEditService;

    @RequestMapping(CmsUrlConstants.PRODUCT.EDIT.INIT)
    public AjaxResponse doInit(int productId){

        CmsMtCategorySchemaWithValueModel valueModel = productPropsEditService.handleInit(getUser().getSelChannelId(),productId);

        return success(valueModel);
    }


}
