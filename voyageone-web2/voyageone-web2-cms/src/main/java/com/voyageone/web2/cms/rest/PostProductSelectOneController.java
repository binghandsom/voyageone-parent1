package com.voyageone.web2.cms.rest;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.cms.CmsRestController;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.PostProductSelectOneRequest;
import com.voyageone.web2.sdk.api.response.PostProductSelectOneResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public PostProductSelectOneResponse selectOne(@RequestBody PostProductSelectOneRequest responseMode) {
        CmsBtProductModel model = productService.selectOne(responseMode);

        PostProductSelectOneResponse result = new PostProductSelectOneResponse();
        result.setProduct(model);

        // 返回用户信息
        return result;
    }
}
