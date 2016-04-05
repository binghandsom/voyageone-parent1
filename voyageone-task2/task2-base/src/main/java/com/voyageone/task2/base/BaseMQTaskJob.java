package com.voyageone.task2.base;

import com.voyageone.common.util.JsonUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * RabbitMq触发任务基类
 * @author aooer 2016/2/29.
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class BaseMQTaskJob extends BaseTaskJob implements MessageListener {

    /**
     * 监听通知消息，执行任务
     * @param message 接受到的消息体
     */
    public void onMessage(Message message) {
        try {
            BaseTaskService service = getTaskService();
            if(service instanceof BaseMQTaskService)
                ((BaseMQTaskService) service).startup(JsonUtil.jsonToMap(new String(message.getBody(),"UTF-8")));
            else
                $error("请配置BaseMQTaskService");
        } catch (Exception e) {
            $error("onMessage error:", e);
        }
    }
}
