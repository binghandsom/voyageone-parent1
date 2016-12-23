package com.voyageone.task2.mq;

import com.voyageone.common.spring.SpringContext;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.utils.MQConfigInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * VOMQServiceControlListener
 *
 * @author chuanyu.liang 2016/4/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class VOMQServiceControlListener implements MessageListener, ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String QUEUE_NAME = "VOMQServiceControlQueue" + System.currentTimeMillis();

    private ConnectionFactory rabbitConnectionFactory;

    private RabbitAdmin rabbitAdmin;

    private TopicExchange voTopicExchange;

    @PostConstruct
    public void init() {
        if (!check()) {
            logger.warn(String.format("%s, %s, %s is null", "rabbitConnectionFactory", "rabbitAdmin", "voTopicExchange"));
            return;
        }

        //@RabbitListener(
        //        bindings = @QueueBinding(
        //                value = @Queue(value = QUEUE_NAME, autoDelete = "false"),
        //                exchange = @Exchange(value = EXCHANGE_NAME,  type = ExchangeTypes.TOPIC),
        //                key = "my.#.*"
        //        )
        //)
        /*
         * <rabbit:queue name="myAnonymousQueue" auto-delete="false" durable="true"/>
         * <rabbit:fanout-exchange name="TUTORIAL-EXCHANGE">
         *     <rabbit:bindings>
         *         <rabbit:binding queue="myAnonymousQueue"/>
         *     </rabbit:bindings>
         * </rabbit:fanout-exchange>
         */
        Queue queue = new Queue(QUEUE_NAME, true, false, true);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(voTopicExchange).with("VOMQServiceControlQueue.#.*"));

        /*
         * <rabbit:listener-container connection-factory="rabbitConnFactory">
         *   <rabbit:listener ref="aListener" queues="myAnonymousQueue" />
         * </rabbit:listener-container>
         */
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitConnectionFactory);
        container.setQueueNames(QUEUE_NAME);
        MessageListenerAdapter adapter = new MessageListenerAdapter(this);
        container.setMessageListener(adapter);
        container.start();

        logger.info("VOMQServiceControlListener started");
    }

    private boolean check() {
        return rabbitConnectionFactory != null && rabbitAdmin != null && voTopicExchange != null;
    }

    public void onMessage(Message message) {
        if (!check()) {
            logger.warn(String.format("%s, %s, %s is null", "rabbitConnectionFactory", "rabbitAdmin", "voTopicExchange"));
            return;
        }

        try {
            String messageBody = new String(message.getBody());
            logger.info(String.format("VOMQServiceControlListener message:%s", messageBody));

            //mqService
            Map<String, Object> messageBodyMap = JacksonUtil.jsonToMap(messageBody);
            if (!messageBodyMap.containsKey("mqService")) {
                logger.error("mqService not found in message");
                return;
            }

            String mqServiceName = (String) messageBodyMap.get("mqService");
            if (StringUtils.isEmpty(mqServiceName)) {
                return;
            }

            String mqServiceBeanName = mqServiceName.substring(0, 1).toLowerCase() + mqServiceName.substring(1);
            Object mqServiceObj = getApplicationContextBean(mqServiceBeanName);
            if (mqServiceObj == null) {
                logger.warn(String.format("VOMQServiceControlListener %s not found", mqServiceName));
                return;
            }

            // active
            if (!messageBodyMap.containsKey("active")) {
                logger.error("active not found in message");
                return;
            }
            String active = (String) messageBodyMap.get("active");
            switch (active) {
                case "start":
                    MQConfigInit.checkStartMq(mqServiceObj);
                    logger.info(String.format("MQAnnoService %s is start", mqServiceName));
                    break;
                case "stop":
                    MQConfigInit.stopMQ(mqServiceObj);
                    logger.info(String.format("MQAnnoService %s is stop", mqServiceName));
                    break;
                default:
                    logger.error("active not found");
                    break;
            }
        } catch (Exception ex) {
            logger.error("VOMQServiceControlListener error", ex);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.rabbitConnectionFactory = getApplicationContextBean(applicationContext, ConnectionFactory.class);
        this.rabbitAdmin = getApplicationContextBean(applicationContext, RabbitAdmin.class);
        this.voTopicExchange = getApplicationContextBean(applicationContext, TopicExchange.class);
    }

    private <T> T getApplicationContextBean(ApplicationContext applicationContext, Class<T> requiredType) {
        try {
            return applicationContext.getBean(requiredType);
        } catch (Exception ignored) {
        }
        return null;
    }

    private Object getApplicationContextBean(String mqServiceBeanName) {
        try {
            return SpringContext.getBean(mqServiceBeanName);
        } catch (Exception ignored) {
        }
        return null;
    }
}
