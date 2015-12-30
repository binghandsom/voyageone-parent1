package com.voyageone.web2.cms.views.system;

import com.voyageone.cms.service.model.CmsMtCategorySchemaModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants.SYSTEM;
import com.voyageone.web2.cms.model.CmsBtPromotionModel;
import com.voyageone.web2.cms.views.promotion.CmsPromotionService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/12/11.
 */
@RestController
@RequestMapping(
        value = SYSTEM.CATEGORY.ROOT,
        method = RequestMethod.POST
)
public class CmsCategoryListController extends CmsController {

    @Autowired
    private CmsCategoryListService cmsCategoryListService;

    @RequestMapping(SYSTEM.CATEGORY.GET_CATEGORY_LIST)
    public AjaxResponse getCategoryList(@RequestBody Map params) {

        Long total = cmsCategoryListService.getCategoryCount(params);
        List<JSONObject> resultData = cmsCategoryListService.getCategoryList(params);
        Map<String,Object> resultBean = new HashMap<>();
        resultBean.put("total",total);
        resultBean.put("resultData",resultData);

        // 返回用户信息
        return success(resultBean);
    }

    @RequestMapping(SYSTEM.CATEGORY.GET_CATEGORY_DETAIL)
    public AjaxResponse getCategoryById(@RequestBody String id) {

//        JSONObject resultBean = cmsCategoryListService.getMasterSchemaJsonObjectByCatId(id);
        CmsMtCategorySchemaModel resultBean = cmsCategoryListService.getMasterSchemaModelByCatId(id);

        // 返回用户信息
        return success(resultBean);
    }

    @RequestMapping(SYSTEM.CATEGORY.UPDATE_CATEGORY_SCHEMA)
    public  AjaxResponse updateCategorySchema(@RequestBody Map categorySchema) {

        CmsMtCategorySchemaModel resultBean = cmsCategoryListService.updateCategorySchema(categorySchema, getUser().getUserName());
        // 返回用户信息
        return success(resultBean.getModified());
    }

}
