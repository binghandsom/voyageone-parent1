package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Created by james on 2017/1/16.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_TM_TE_JIA_BAO_DEL)
public class CmsTeJiaBaoDelMQMessageBody extends BaseMQMessageBody {

    private Long tejiabaoId;
    private Integer cartId;
    private List<String> numIId;

    public Long getTejiabaoId() {
        return tejiabaoId;
    }

    public void setTejiabaoId(Long tejiabaoId) {
        this.tejiabaoId = tejiabaoId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public List<String> getNumIId() {
        return numIId;
    }

    public void setNumIId(List<String> numIId) {
        this.numIId = numIId;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isEmpty(super.getChannelId())) {
            throw new MQMessageRuleException("天猫活动-天猫特价宝刷新MQ发送异常, 参数channelId为空.");
        }
        if(tejiabaoId == null){
            throw new MQMessageRuleException("天猫活动-天猫特价宝刷新MQ发送异常, 参数tejiabaoId为空.");
        }
        if(cartId == null){
            throw new MQMessageRuleException("天猫活动-天猫特价宝刷新MQ发送异常, 参数cartId为空.");
        }
        if (CollectionUtils.isEmpty(numIId)) {
            throw new MQMessageRuleException("天猫活动-天猫特价宝刷新MQ发送异常, 参数numIId为空.");
        }
    }
}
