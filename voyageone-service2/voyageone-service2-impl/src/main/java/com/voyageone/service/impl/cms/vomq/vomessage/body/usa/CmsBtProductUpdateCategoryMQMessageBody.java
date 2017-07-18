package com.voyageone.service.impl.cms.vomq.vomessage.body.usa;

import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Created by dell on 2017/7/17.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_USA_PRODUCT_UPDATE_CATEGORY)
public class CmsBtProductUpdateCategoryMQMessageBody extends BaseMQMessageBody {

    List<String> productCodes;
    Integer cartId;
    String pCatPath;
    String pCatId;

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
        if (org.apache.commons.lang.StringUtils.isBlank(super.getChannelId())) {
            throw new MQMessageRuleException("高级检索-批量设置平台类目MQ发送异常, 参数channelId为空.");
        }
        if (CollectionUtils.isEmpty(productCodes)) {
            throw new MQMessageRuleException("高级检索-批量设置平台类目MQ发送异常, 参数productCodes为空.");
        }
        if (cartId == null) {
            throw new MQMessageRuleException("高级检索-批量设置平台类目MQ发送异常, 参数cartId为空.");
        }
        if (StringUtils.isAnyEmpty(pCatId, pCatPath)) {
            throw new MQMessageRuleException("高级检索-批量设置平台类目MQ发送异常, 参数pCatId或者pCatPath为空.");
        }
        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("高级检索-批量设置平台类目MQ发送异常, 发送者为空.");
        }
    }
}
