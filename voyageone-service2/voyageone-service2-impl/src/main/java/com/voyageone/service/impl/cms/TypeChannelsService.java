package com.voyageone.service.impl.cms;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.service.impl.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dell on 2016/12/13.
 */
@Service
public class TypeChannelsService extends BaseService {

    /**
     * 获取该channel的PlatformType
     * @param channelId
     * @return
     */
    public List<TypeChannelBean> getPlatformTypeList (String channelId, String language) {
        return TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_D, language);
    }

    public List<TypeChannelBean> getUsPlatformTypeList (String channelId, String language) {
        return TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_O, language);
    }
}
