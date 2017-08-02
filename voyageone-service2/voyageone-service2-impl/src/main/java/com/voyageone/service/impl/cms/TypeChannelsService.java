package com.voyageone.service.impl.cms;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.service.impl.BaseService;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/12/13.
 */
@Service
public class TypeChannelsService extends BaseService {

    /**
     * 获取该channel的PlatformType
     */
    public List<TypeChannelBean> getPlatformTypeList(String channelId, String language) {
        return TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_D, language);
    }

    public List<TypeChannelBean> getUsPlatformTypeList(String channelId, String language) {
        return TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_O, language);
    }

    /**
     * 值查询USA 平台(cartId在0~20之间的平台)
     *
     * @return 美国平台
     */
    public List<TypeChannelBean> getOnlyUsPlatformTypeList(String channelId, String language) {
        List<TypeChannelBean> platformTypeList = TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.SKU_CARTS_53, channelId, language);
        List<TypeChannelBean> onlyUsPlatformTypeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(platformTypeList)) {
            for (TypeChannelBean typeChannelBean : platformTypeList) {
                int cartId = NumberUtils.toInt(typeChannelBean.getValue(), -1);
                if (cartId > 0 && cartId < 20) {
                    onlyUsPlatformTypeList.add(typeChannelBean);
                }
            }
        }
        return onlyUsPlatformTypeList;
    }
}
