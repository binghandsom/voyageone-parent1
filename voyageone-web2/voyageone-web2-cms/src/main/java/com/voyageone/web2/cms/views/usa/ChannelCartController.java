package com.voyageone.web2.cms.views.usa;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 渠道-平台共通控制器
 *
 * @Author rex.wu
 * @Create 2017-07-10 17:59
 */
@RestController
@RequestMapping(value = UsaCmsUrlConstants.COMMON.ROOT)
public class ChannelCartController extends BaseController {

    /**
     * 获取当前Channel所有的平台，包括en,cn
     * @return
     */
    @RequestMapping(value = UsaCmsUrlConstants.COMMON.GET_CHANNEL_CART)
    public AjaxResponse getChannelCarts() {
        return success(TypeChannels.getTypeList(Constants.comMtTypeChannel.SKU_CARTS_53, getUser().getSelChannelId()));
    }


}
