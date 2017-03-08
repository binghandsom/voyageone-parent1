package com.voyageone.task2.cms.mqjob.test;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.components.rabbitmq.namesub.IMQSubBeanNameAll;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.stereotype.Service;

@Service
@VOSubRabbitListener
public class MqSubChannelTask extends TBaseMQCmsService<MqSubChannelMQMessageBody> implements IMQSubBeanNameAll {

    /**
     * 取得所有SubBeanName
     */
    @Override
    public String[] getAllSubBeanName() {
        return new String[]{"001", "002"};
    }

    @Override
    public void onStartup(MqSubChannelMQMessageBody messageBody) throws Exception {

        System.out.println(this + ",body:[" + JacksonUtil.bean2Json(messageBody) + "]");

    }
}
