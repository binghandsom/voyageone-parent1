package com.voyageone.web2.cms.openapi.control;

import com.voyageone.web2.sdk.api.request.ProductForOmsGetRequest;
import com.voyageone.web2.sdk.api.response.ProductForOmsGetResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@RestController
@RequestMapping(
        value  = "/rest",
        method = RequestMethod.GET
)
public class CmsImageFileController {

    @RequestMapping(value = "/product/getImage",method = RequestMethod.GET)
    public Object get(String channelId,int templateId,String file,String vparam) {
//        String url = "http://image.voyageone.net/product/getImage?cId=001&templateId=15&file=nike-air-penny-ii-333886005-1&vparam=file:bcbg/bcbg-sku.png,file:bcbg/bcbgtupian.jpg,Text String to be rendered";
//        String cId = "001";
//        int templateId = 15;
//        String file = "nike-air-penny-ii-333886005-1";//"test-test-1";//
//        String vparam = "file:bcbg/bcbg-sku.png,file:bcbg/bcbgtupian.jpg,Text String to be rendered";
//        String fullPath = getImage(url, cId, templateId, vparam);
//        putOSS(cId, "600", templateId, file + ".jpg", fullPath);
                 return  test4()+"afasdfasdf432323323233333rrrrr4";
    }

    public String  test1()
    {
        return "aaaaaa322222222222222a";
    }
    public String  test2()
    {
        return "b2aaaaaa32222222243222222b";
    }
    public String  test3()
    {
        return "3b2aaaaaa32222222243222222b3";
    }
    public String  test4()
    {
        return "44444444443b2aaaaaa32222222243222222b3";
    }
}
