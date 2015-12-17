package com.voyageone.web2.cms.rest;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.cms.CmsRestController;
import com.voyageone.web2.sdk.api.request.PostProductSelectOneRequest;
import com.voyageone.web2.sdk.api.response.PostProductSelectOneResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class PostProductSelectOneController extends CmsRestController {

    @Autowired
    private PostProductSelectOneService productService;

    /**
     * 返回selectOne
     * @return
     */
    @RequestMapping("selectOne")
    public PostProductSelectOneResponse selectOne() {
        CmsBtProductModel model = productService.selectOne(null);

        PostProductSelectOneResponse result = new PostProductSelectOneResponse();
        result.setData(model);

        // 返回用户信息
        return result;
    }

}
