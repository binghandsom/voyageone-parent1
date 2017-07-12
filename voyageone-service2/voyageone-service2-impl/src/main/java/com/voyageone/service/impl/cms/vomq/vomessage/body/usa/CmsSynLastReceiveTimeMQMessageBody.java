package com.voyageone.service.impl.cms.vomq.vomessage.body.usa;

import com.voyageone.common.util.ListUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import java.util.List;

/**
 * Created by james on 2017/7/12.
 * 同步LastReceiveTime 来自wms的MQ
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_SYN_LAST_RECEIVE_TIME)
public class CmsSynLastReceiveTimeMQMessageBody extends BaseMQMessageBody {

    private List<String> codes;

    private Long receiveTime;

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public Long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Long receiveTime) {
        this.receiveTime = receiveTime;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (ListUtils.isNull(codes) || receiveTime == null) throw new MQMessageRuleException("参数错误");
    }
}
