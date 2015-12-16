package com.voyageone.web2.cms.rest;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.cms.CmsRestController;
import com.voyageone.web2.sdk.api.response.VoResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
public class ProductController extends CmsRestController {

    @Autowired
    private ProductService productService;

    /**
     * 返回selectOne
     * @return
     */
    @RequestMapping("selectOne")
    public VoResponseEntity selectOne(@RequestBody Map<String, Object> params) {

        CmsBtProductModel model = null;
//        try {
            String aa = null;
            aa.startsWith("");
            model = productService.selectOne("");
//        } catch (Exception ex) {
//            return new ResponseEntity<CmsBtProductModel>(HttpStatus.BAD_REQUEST);
//        }
        // 返回用户信息
        return successEntity(model);
    }
}
