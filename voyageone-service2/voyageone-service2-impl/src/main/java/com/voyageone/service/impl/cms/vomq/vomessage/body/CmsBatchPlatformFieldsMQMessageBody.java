package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import java.util.List;

/**
 * Created by james on 2017/1/3.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_UPDATE_PRODUCT_PLATFORM_FIELDS)
public class CmsBatchPlatformFieldsMQMessageBody extends BaseMQMessageBody {

    private List<String> productCodes;
    private String channelId;
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

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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

    }
}
