package com.voyageone.web2.cms.views.tools.product;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants.PRODUCT_FIELDS_MAP;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 搜索, 获取和保存平台类目的属性匹配
 * <p>
 * Created by jonas on 8/13/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
@RestController
@RequestMapping(value = PRODUCT_FIELDS_MAP.ROOT, method = RequestMethod.POST)
public class FieldMapController extends CmsController {


    @RequestMapping(PRODUCT_FIELDS_MAP.LIST)
    public AjaxResponse list(@RequestBody Map a) {







        return success(null);
    }

    @RequestMapping(PRODUCT_FIELDS_MAP.GET)
    public AjaxResponse get(@RequestBody Map a) {
        return success(null);
    }

    @RequestMapping(PRODUCT_FIELDS_MAP.SAVE)
    public AjaxResponse save(@RequestBody Map a) {
        return success(null);
    }
}
