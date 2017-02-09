package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 高级检索-商品审批Job消息实体
 *
 * @Author rex
 * @Create 2017-01-13 11:29
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_UPDATE_PRODUCT_PLATFORM_STATUS_TO_APPROVE)
public class AdvSearchProductApprovalMQMessageBody extends BaseMQMessageBody {

    private String channelId;
    private String userName;
    private List<Integer> cartList;
    private List<String> productCodes;
    private Map<String, Object> params;
    private Map<String, Object> cmsSessionParams;

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isBlank(channelId)) {
            throw new MQMessageRuleException("高级检索-商品审批MQ发送异常, 参数channelId为空.");
        }
        if (StringUtils.isBlank(userName)) {
            throw new MQMessageRuleException("高级检索-商品审批MQ发送异常, 参数userName为空.");
        }
        if (CollectionUtils.isEmpty(cartList)) {
            throw new MQMessageRuleException("高级检索-商品审批MQ发送异常, 参数cartList为空.");
        }
        if (CollectionUtils.isEmpty(productCodes)) {
            throw new MQMessageRuleException("高级检索-商品审批MQ发送异常, 参数productCodes为空.");
        }
        if (params == null || params.size() <= 0) {
            throw new MQMessageRuleException("高级检索-商品审批MQ发送异常, 参数params为空.");
        }

        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("sender(发送者)不能为空");
        }
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Integer> getCartList() {
        return cartList;
    }

    public void setCartList(List<Integer> cartList) {
        this.cartList = cartList;
    }

    public List<String> getProductCodes() {
        return productCodes;
    }

    public void setProductCodes(List<String> productCodes) {
        this.productCodes = productCodes;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, Object> getCmsSessionParams() {
        return cmsSessionParams;
    }

    public void setCmsSessionParams(Map<String, Object> cmsSessionParams) {
        this.cmsSessionParams = cmsSessionParams;
    }
}
