package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.namesub.IMQMessageSubBeanName;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by james on 2017/1/3.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_UPDATE_PRODUCT_PLATFORM_FIELDS)
public class CmsBatchPlatformFieldsMQMessageBody extends BaseMQMessageBody  implements IMQMessageSubBeanName {

    private List<String> productCodes;
    private Integer cartId;
    private String fieldsId;
    private String fieldsName;
    private Object fieldsValue;

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

    public String getFieldsId() {
        return fieldsId;
    }

    public void setFieldsId(String fieldsId) {
        this.fieldsId = fieldsId;
    }

    public String getFieldsName() {
        return fieldsName;
    }

    public void setFieldsName(String fieldsName) {
        this.fieldsName = fieldsName;
    }

    public Object getFieldsValue() {
        return fieldsValue;
    }

    public void setFieldsValue(Object fieldsValue) {
        this.fieldsValue = fieldsValue;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isBlank(super.getChannelId())) {
            throw new MQMessageRuleException("高级检索-批量设置平台属性MQ发送异常, 参数channelId为空.");
        }
        if (CollectionUtils.isEmpty(productCodes)) {
            throw new MQMessageRuleException("高级检索-批量设置平台属性MQ发送异常, 参数productCodes为空.");
        }
        if (cartId == null) {
            throw new MQMessageRuleException("高级检索-批量设置平台属性MQ发送异常, 参数cartId为空.");
        }
        if (StringUtils.isBlank(fieldsId)) {
            throw new MQMessageRuleException("高级检索-批量设置平台属性MQ发送异常, 参数fieldsId为空.");
        }
        if (StringUtils.isBlank(fieldsName)) {
            throw new MQMessageRuleException("高级检索-批量设置平台属性MQ发送异常, 参数fieldsName为空.");
        }
        if (fieldsValue == null) {
            throw new MQMessageRuleException("高级检索-批量设置平台属性MQ发送异常, 参数fieldsValue为空.");
        }
        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("高级检索-批量设置平台属性MQ发送异常, 发送者为空.");
        }
    }

    @Override
    public String getSubBeanName() {
        return getChannelId();
    }
}
