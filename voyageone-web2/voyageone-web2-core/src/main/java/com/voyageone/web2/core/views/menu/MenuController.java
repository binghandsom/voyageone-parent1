package com.voyageone.web2.core.views.menu;

import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.core.CoreUrlConstants;
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
 * @author Edward
 * @version 2.0.0, 15/12/01
 */
@RestController
@RequestMapping(
        value = CoreUrlConstants.MENU.ROOT,
        method = RequestMethod.POST
)
public class MenuController extends BaseController {
    @Autowired
    private MenuService menuService;

    @RequestMapping(CoreUrlConstants.MENU.GET_MENU_HEADER_INFO)
    public AjaxResponse getMenuHeaderInfo() {

        Map<String, Object> resultbean = new HashMap<>();

        //获取userId和channelId.
        Integer userId = getUser().getUserId();
        String channelId = getUser().getSelChannelId();

        // 获取menu列表.
        List<Map<String, Object>> menuList = menuService.getMenuList(userId, channelId);
        resultbean.put("menuList", menuList);

        // 获取language列表.
        List<TypeBean> languageList = menuService.getLanguageList();
        resultbean.put("languageList", languageList);

        // 获取用户相关信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userName", getUser().getUserName());
        userInfo.put("channelName", getUser().getSelChannel().getFullName());
        userInfo.put("language", getLang());
        resultbean.put("userInfo", userInfo);

        // 返回用户信息
        return success(resultbean);
    }

    @RequestMapping(CoreUrlConstants.MENU.SET_LANGUAGE)
    public AjaxResponse setLanguage(@RequestBody Map<String, Object> params) {

        menuService.setLanguage(getSession(), getUser(), params.get("language"));

        // 返回用户信息
        return success(true);
    }
}
