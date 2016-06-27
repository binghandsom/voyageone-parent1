package com.voyageone.web2.core.views.menu;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.core.CoreUrlConstants;
import com.voyageone.web2.core.views.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
    @Autowired
    private UserService userService;

    @RequestMapping(CoreUrlConstants.MENU.GET_MENU_HEADER_INFO)
    public AjaxResponse getMenuHeaderInfo() {
        //获取userId和channelId.
        Integer userId = getUser().getUserId();
        String channelId = getUser().getSelChannelId();
        String applicationId=getUser().getApplicationId();
        Map<String, Object> resultBean =menuService.getMenuHeaderInfo(userId,channelId,applicationId);


        // 获取用户相关信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userName", getUser().getUserName());
        userInfo.put("channelName", getUser().getSelChannel().getFullName());
        userInfo.put("language", getLang());
        userInfo.put("application",getUser().getApplication());
        resultBean.put("userInfo", userInfo);
        // vendor系统用
        userInfo.put("channelInfo", getUser().getUserConfig().get("channel_id"));

        // 返回用户信息
        return success(resultBean);
    }

    @RequestMapping(CoreUrlConstants.MENU.SET_LANGUAGE)
    public AjaxResponse setLanguage(@RequestBody Map<String, Object> params) {

        menuService.setLanguage(getSession(), getUser(), params.get("language"));

        // 返回用户信息
        return success(true);
    }

    @RequestMapping(CoreUrlConstants.MENU.SET_CHANNEL)
    public AjaxResponse setChannel(@RequestBody Map<String, Object> params) {

        userService.setSelectChannel(getUser(), (String) params.get("channelId"), "", "vms");

        // 返回用户信息
        return success(true);
    }
}
