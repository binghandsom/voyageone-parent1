package com.voyageone.web2.core.views;

import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class description
 *
 * @author         Edward
 * @version        2.0.0, 15/12/01
 */
@RestController
@RequestMapping(
    value  = "/core/home/menu/",
    method = RequestMethod.POST
)
public class MenuController extends BaseController {
    @Autowired
    private MenuService menuService;

    @RequestMapping("getMenuHeaderInfo")
    public AjaxResponse getMenuHeaderInfo(@RequestBody Map<String, Object> params) {

        Map<String, Object> menuBean = new HashMap<>();

        //获取userId和channelId.
        Integer userId    = getUser().getUserId();
        String  channelId = getUser().getSelChannelId();

        // 获取menu列表.
        List<Map<String, Object>> menuList = menuService.getMenuList(userId, channelId);
        menuBean.put("menuList", menuList);

        // 获取language列表.
        List<TypeBean> languageList = menuService.getLanguageList();
        menuBean.put("languageList", languageList);

        // 获取用户相关信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userName", getUser().getUserName());
        userInfo.put("channelName", getUser().getSelChannel().getFullName());
        menuBean.put("userInfo", userInfo);

        // 返回用户信息
        return success(menuBean);
    }
}
