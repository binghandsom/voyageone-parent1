package com.voyageone.web2.cms.openapi.control;

import com.voyageone.service.bean.openapi.image.AddListParameter;
import com.voyageone.service.bean.openapi.image.AddListResultBean;
import com.voyageone.web2.cms.openapi.service.CmsImageFileService;
//import com.voyageone.service.model.openapi.image.AddListResultBean;
import com.voyageone.service.bean.openapi.image.GetImageResultBean;
import com.voyageone.web2.cms.openapi.OpenAipBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(
        value = "/rest/product/image"
)
public class CmsImageFileController extends OpenAipBaseController {
    @Autowired
    CmsImageFileService service;

    ///http://localhost:8081/rest/product/getImage?cId=001&templateId=15&file=nike-air-penny-ii-333886005-1&vparam=file:bcbg/bcbg-sku.png,file:bcbg/bcbgtupian.jpg,Text String to be rendered
    @RequestMapping(value = "get")
    public GetImageResultBean get(HttpServletRequest request, @RequestParam String cId, @RequestParam int templateId, @RequestParam String file, @RequestParam String vparam) throws Exception {
        String queryString = request.getQueryString();
        return service.getImage(cId, templateId, file, vparam, queryString, "SystemCreateImage");
    }

    @RequestMapping(value = "addList", method = RequestMethod.POST)
    public AddListResultBean addList(@RequestParam AddListParameter parameter) {
        return service.addList(parameter);
    }
}
