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
 * 批量更新商品Job消息实体
 *
 * @Author rex
 * @Create 2017-01-03 13:59
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_BATCH_UPDATE_PRODUCT)
public class BatchUpdateProductMQMessageBody extends BaseMQMessageBody {

    private String channelId;
    private List<String> productCodes;
    private String userNmme;
    private Map<String, Object> params;

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

    public String getUserNmme() {
        return userNmme;
    }

    public void setUserNmme(String userNmme) {
        this.userNmme = userNmme;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isBlank(channelId)) {
            throw new MQMessageRuleException("BatchUpdateProduct参数channelId为空.");
        }
        if (CollectionUtils.isEmpty(productCodes)) {
            throw new MQMessageRuleException("BatchUpdateProduct参数productCodes为空.");
        }
        if (StringUtils.isBlank(userNmme)) {
            throw new MQMessageRuleException("BatchUpdateProduct参数userNmme为空.");
        }

        if (params == null || params.size() <= 0) {
            throw new MQMessageRuleException("BatchUpdateProduct更新参数为空.");
        }
        if (StringUtils.isBlank(getSender())) {
            throw new MQMessageRuleException("sender(发送者)不能为空");
        }
    }
}
