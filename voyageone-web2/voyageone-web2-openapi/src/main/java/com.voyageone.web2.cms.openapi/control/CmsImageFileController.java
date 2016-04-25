package com.voyageone.web2.cms.openapi.control;

import com.voyageone.service.impl.cms.imagecreate.CmsMtImageCreateFileService;
import com.voyageone.service.model.openapi.ProductGetImageRespone;
import com.voyageone.web2.cms.openapi.OpenAipBaseController;
import com.voyageone.web2.sdk.api.request.ProductForOmsGetRequest;
import com.voyageone.web2.sdk.api.response.ProductForOmsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@RestController
@RequestMapping(
        value  = "/rest",
        method = RequestMethod.GET
)
public class CmsImageFileController extends OpenAipBaseController {
    @Autowired
    CmsMtImageCreateFileService service;
    ///http://localhost:8081/rest/product/getImage?cId=001&templateId=15&file=nike-air-penny-ii-333886005-1&vparam=file:bcbg/bcbg-sku.png,file:bcbg/bcbgtupian.jpg,Text String to be rendered
    @RequestMapping(value = "/product/getImage", method = RequestMethod.GET)
    public ProductGetImageRespone get(HttpServletRequest request, @RequestParam String cId, @RequestParam int templateId, @RequestParam String file, @RequestParam String vparam) throws Exception {
        String queryString = request.getQueryString();
        return service.getImage(cId, templateId, file, vparam, queryString, "SystemCreateImage");
    }
}
