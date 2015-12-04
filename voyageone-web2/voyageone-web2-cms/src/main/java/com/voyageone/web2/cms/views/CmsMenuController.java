package com.voyageone.web2.cms.views;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 15/12/2
 */
@RestController
@RequestMapping(
        value  = "/cms/home/menu/",
        method = RequestMethod.POST
)
public class CmsMenuController extends CmsController{

    @Autowired
    private CmsMenuService menuService;

    /**
     * 返回categoryType, categoryList, categoryTreeList
     * @return
     */
    @RequestMapping("getCategoryInfo")
    public AjaxResponse getCategoryInfo() {

        Map<String, Object> resultbean = new HashMap<>();

        String cTypeId = getCmsSession().getCategoryType();
        String channelId = getUser().getSelChannelId();

        resultbean.put("categoryType", cTypeId);

        // 获取CategoryList
        List<Map<String, Object>> categoryList = new ArrayList<>();
//        List<Map<String, Object>> categoryList = menuService.getCategoryList(cTypeId, channelId);
        resultbean.put("categoryList", categoryList);

        // 获取CategoryTreeList
        List<Map> categoryTreeList = menuService.getCategoryTreeList(cTypeId, channelId);
        resultbean.put("categoryTreeList", categoryTreeList);

        // 返回用户信息
        return success(resultbean);
    }

    /**
     * 返回categoryTypeList
     * @return
     */
    @RequestMapping("getCategoryType")
    public AjaxResponse getCategoryType() {

        String channelId = getUser().getSelChannelId();

        // 获取CategoryTypeList
        List<Map<String, Object>> categoryTypeList = menuService.getCategoryTypeList(channelId);

        // 返回用户信息
        return success(categoryTypeList);
    }

}
