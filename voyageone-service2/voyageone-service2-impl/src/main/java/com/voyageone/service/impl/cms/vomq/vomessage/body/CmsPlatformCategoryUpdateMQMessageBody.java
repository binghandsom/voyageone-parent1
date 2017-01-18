package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 2017/1/13.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_PLATFORM_CATEGORY_UPDATE)
public class CmsPlatformCategoryUpdateMQMessageBody extends BaseMQMessageBody {
    String channelId;
    List<String> productCodes;
    Integer cartId;
    String pCatPath;
    String pCatId;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public List<String> getProductCodes() {
        return productCodes;
    }

    public void setProductCodes(List<String> productCodes) {
        this.productCodes = productCodes;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getpCatPath() {
        return pCatPath;
    }

    public void setpCatPath(String pCatPath) {
        this.pCatPath = pCatPath;
    }

    public String getpCatId() {
        return pCatId;
    }

    public void setpCatId(String pCatId) {
        this.pCatId = pCatId;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if(ListUtils.isNull(productCodes) || StringUtils.isAnyEmpty(pCatId, pCatPath)){
            throw new MQMessageRuleException("参数不对");
        }
    }
}
