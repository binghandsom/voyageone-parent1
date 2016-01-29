package com.voyageone.web2.cms.wsdl.control;

import com.voyageone.web2.cms.wsdl.BaseController;
import com.voyageone.web2.cms.wsdl.service.CategorySchemaService;
import com.voyageone.web2.sdk.api.request.*;
import com.voyageone.web2.sdk.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * categorySchema Controller
 *
 * @author lewis 2016/01/28
 * @version 2.0.1
 * @since. 2.0.0
 */

@RestController
@RequestMapping(
        value  = "/rest/categorySchema",
        method = RequestMethod.POST
)
public class CategorySchemaController extends BaseController {

    @Autowired
    private CategorySchemaService categorySchemaService;

    /**
     * selectOne
     *
     * @return ProductGetResponse
     */
    @RequestMapping("selectOne")
    public CategorySchemaGetResponse selectOne(@RequestBody CategorySchemaGetRequest request) {
        return categorySchemaService.getCategorySchemaByCatId(request.getCategoryId());
    }

}