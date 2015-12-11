package com.voyageone.web2.cms.rest;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.sdk.api.VoApiResponse;
import com.voyageone.web2.sdk.api.response.ProductGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * product Controller
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since. 2.0.0
 */

@RestController
@RequestMapping(
        value  = "/rest/puroduct",
        method = RequestMethod.POST
)
public class ProductController extends CmsController{

    @Autowired
    private ProductService productService;

//    /**
//     * 返回selectOne
//     * @return
//     */
//    @RequestMapping("selectOne")
//    public AjaxResponse selectOne(HttpServletRequest request) {
//        System.out.println(getBody(request));
//        Map<String, Object> resultBean = new HashMap<>();
//
//        String cTypeId = getCmsSession().getCategoryType();
//        //String channelId = getUser().getSelChannelId();
//
//        CmsBtProductModel model = productService.selectOne("");
//        resultBean.put("product", model);
//
//        // 返回用户信息
//        return success(model);
//    }

    private String getBody(HttpServletRequest req) {
        String body = "";
        if (req.getMethod().equals("POST") )
        {
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = null;

            try {
                bufferedReader =  req.getReader();
                char[] charBuffer = new char[128];
                int bytesRead;
                while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
                    sb.append(charBuffer, 0, bytesRead);
                }
            } catch (IOException ex) {
                // swallow silently -- can't get body, won't
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException ex) {
                        // swallow silently -- can't get body, won't
                    }
                }
            }
            body = sb.toString();
        }
        return body;
    }



    /**
     * 返回selectOne
     * @return
     */
    @RequestMapping("selectOne.json")
    public ProductGetResponse selectOne(@RequestBody Map<String, Object> params) {

        ProductGetResponse resultBean = new ProductGetResponse();

        String cTypeId = getCmsSession().getCategoryType();
        //String channelId = getUser().getSelChannelId();

        CmsBtProductModel model = productService.selectOne("");
        resultBean.setProduct(model);

        // 返回用户信息
        return resultBean;
    }

}
