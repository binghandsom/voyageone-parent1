package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.CmsConstants;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * 记录上下架操作历史 Job
 *
 * @Author rex
 * @Create 2017-01-04 19:31
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_UPDATE_PRODUCT_PLATFORM_PSTATUS_OFF_OR_ON)
public class PlatformActiveLogMQMessageBody extends BaseMQMessageBody {

    private String channelId;
    private String userName;
    private List<Integer> cartList;
    private String activeStatus;
    private List<String> productCodes;
    private String comment;
    private CmsConstants.PlatformActive statusVal;


    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isBlank(channelId)) {
            throw new MQMessageRuleException("上下架PlatformActiveLogMQ参数channelId为空.");
        }
        if (CollectionUtils.isEmpty(productCodes)) {
            throw new MQMessageRuleException("上下架PlatformActiveLogMQ参数productCodes为空.");
        }
        if (CollectionUtils.isEmpty(cartList)) {
            throw new MQMessageRuleException("上下架PlatformActiveLogMQ参数cartList为空.");
        }
        if (StringUtils.isBlank(activeStatus)) {
            throw new MQMessageRuleException("上下架PlatformActiveLogMQ参数activeStatus为空.");
        }
        if (StringUtils.isBlank(userName)) {
            throw new MQMessageRuleException("上下架PlatformActiveLogMQ参数userName为空.");
        }
        if (StringUtils.isBlank(getSender())) {
            throw new MQMessageRuleException("sender(发送者)不能为空");
        }
        if (StringUtils.isBlank(String.valueOf(statusVal))) {
            throw new MQMessageRuleException("上下架PlatformActiveLogMQ参数statusVal为空.");
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

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public List<String> getProductCodes() {
        return productCodes;
    }

    public void setProductCodes(List<String> productCodes) {
        this.productCodes = productCodes;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CmsConstants.PlatformActive getStatusVal() {
        return statusVal;
    }

    public void setStatusVal(CmsConstants.PlatformActive statusVal) {
        this.statusVal = statusVal;
    }
}
