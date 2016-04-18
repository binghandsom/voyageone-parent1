package com.voyageone.task2.base;

import com.voyageone.common.util.JacksonUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.io.UnsupportedEncodingException;
import java.util.Map;

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
            if(service instanceof BaseMQTaskService) {
                String messageStr = new String(message.getBody(), "UTF-8");
                Map<String, Object> messageMap = JacksonUtil.jsonToMap(messageStr);
                ((BaseMQTaskService) service).startup(messageMap);
            } else {
                $error("请配置BaseMQTaskService");
            }
        } catch (RuntimeException rex) {
            throw rex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
