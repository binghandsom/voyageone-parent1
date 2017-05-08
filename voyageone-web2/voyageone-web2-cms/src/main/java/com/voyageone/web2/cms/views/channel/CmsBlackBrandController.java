package com.voyageone.web2.cms.views.channel;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants.CHANNEL.BLACK_BRAND;
import com.voyageone.web2.cms.bean.channel.CmsBlackBrandParamBean;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by jonas on 9/7/16.
 *
 * @author jonas
 * @version 2.6.0
 * @since 2.6.0
 */
@RestController
@RequestMapping(value = BLACK_BRAND.ROOT, method = RequestMethod.POST)
public class CmsBlackBrandController extends CmsController {

    private final CmsBlackBrandViewService blackBrandViewService;

    @Autowired
    public CmsBlackBrandController(CmsBlackBrandViewService blackBrandViewService) {
        this.blackBrandViewService = blackBrandViewService;
    }

    @RequestMapping(BLACK_BRAND.INIT)
    public AjaxResponse init(@RequestBody String channelId) {
        Map<String, Object> masterData = new HashMap();
        if(channelId.equalsIgnoreCase("928")) {
            List<TypeChannelBean> typeChannelBeenList = TypeChannels.getTypeChannelBeansByTypeValueLang(Constants.comMtTypeChannel.SKU_CARTS_53, channelId, "cn");
            if (typeChannelBeenList == null || typeChannelBeenList.isEmpty()) {
                $warn("高级检索:getMasterData 未取得供应商列表(Synship.com_mt_value_channel表中未配置) channelid=" + channelId);
            } else {
                List<OrderChannelBean> channelBeanList = new ArrayList<>();
                for (TypeChannelBean typeBean : typeChannelBeenList) {
                    OrderChannelBean channelBean = Channels.getChannel(typeBean.getChannel_id());
                    if (channelBean != null) {
                        channelBeanList.add(channelBean);
                    } else {
                        $warn("高级检索:getMasterData 取得供应商列表 channel不存在 channelid=" + typeBean.getChannel_id());
                    }
                }
                if (channelBeanList.isEmpty()) {
                    $warn("高级检索:getMasterData 取得供应商列表 channel不存在 " + channelBeanList.toString());
                } else {
                    masterData.put("channelList", channelBeanList);
                }
            }
        }else{
            OrderChannelBean channelBean = Channels.getChannel(channelId);
            masterData.put("channelList", Collections.singletonList(channelBean));
        }
        return success(masterData);
    }

    /**
     * 查询商品黑名单
     */
    @RequestMapping(BLACK_BRAND.SEARCH_BLACK_BRAND)
    public AjaxResponse searchBlackBrand(@RequestBody CmsBlackBrandParamBean blackBrandParamBean) {
        return success(blackBrandViewService.searchBrandListPage(blackBrandParamBean, getLang(), getUser()));
    }

    /**
     * 更新/批量更新 商品黑名单
     */
    @RequestMapping(BLACK_BRAND.UPDATE_BLACK_BRAND)
    public AjaxResponse updateBlackBrand(@RequestBody CmsBlackBrandParamBean blackBrandParamBean) throws IllegalAccessException {
        return success(blackBrandViewService.switchBrandBlock(blackBrandParamBean, getUser()));
    }
}
