package com.voyageone.web2.core.views.menu;

import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.service.bean.com.UserConfigBean;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.core.CoreUrlConstants;
import com.voyageone.web2.core.views.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

        // 返回用户信息
        return success(resultBean);
    }

    @RequestMapping(CoreUrlConstants.MENU.GET_VENDOR_MENU_HEADER_INFO)
    public AjaxResponse getVendorMenuHeaderInfo() {

        // 返回Map
        Map<String, Object> resultBean = new HashMap<>();

        // 取得Menu信息
        List<Map<String, Object>> menuInfo =menuService.getVendorMenuHInfo(getUser());
        resultBean.put("menuInfo", menuInfo);

        // 获取用户相关信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userName", getUser().getUserName());
        Map<String, String> channelInfo = new HashMap<>();
        channelInfo.put("id", getUser().getSelChannel().getId());
        channelInfo.put("name", getUser().getSelChannel().getFullName());
        userInfo.put("selChannel", channelInfo);
        userInfo.put("language", getLang());

        // 如果一个用户对应多了多了channel，把这些加进去
        List<UserConfigBean> channelList = getUser().getUserConfig().get("channel_id");
        if (channelList != null && channelList.size() > 1) {
            List<Map<String, String>> channels = new ArrayList<>();
            for (UserConfigBean channel : channelList) {
                if (!getUser().getSelChannel().getId().equals(channel.getCfg_val1())) {
                    Map<String, String> channelMapInfo = new HashMap<>();
                    channelMapInfo.put("id", channel.getCfg_val1());
                    channelMapInfo.put("name", ChannelConfigEnums.Channel.valueOfId(channel.getCfg_val1()).getFullName());
                    channels.add(channelMapInfo);
                }
            }
            userInfo.put("channelList", channels);
        }

        resultBean.put("userInfo", userInfo);

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

        userService.setSelectChannel(getUser(), (String) params.get("channelId"), "99", "vms");

        // 返回用户信息
        return success(true);
    }
}
