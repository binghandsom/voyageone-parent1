package com.voyageone.task2.mq;

import com.voyageone.common.mq.config.MQConfigInit;
import com.voyageone.common.spring.SpringContext;
import com.voyageone.common.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * VOMQServiceControlListener
 * @author chuanyu.liang 2016/4/18.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class VOMQServiceControlListener implements MessageListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String QUEUE_NAME = "VOMQServiceControlQueue" + System.currentTimeMillis();

    @Autowired
    private ConnectionFactory rabbitConnectionFactory;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private TopicExchange voTopicExchange;


    @PostConstruct
    public void init() {
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
    }

    public void onMessage(Message message) {

        try {
            String messageBody = new String(message.getBody());
            logger.info(String.format("VOMQServiceControlListener message:%s", messageBody));

            //mqService
            Map<String, Object> messageBodyMap = JacksonUtil.jsonToMap(messageBody);
            if (!messageBodyMap.containsKey("mqService")) {
                logger.error("mqService not found in message");
                return;
            }
            String mqService = (String) messageBodyMap.get("mqService");
            Class mqServiceClass = ClassUtils.forName(mqService, Thread.currentThread().getContextClassLoader());
            Object mqServiceObj = SpringContext.getBean(mqServiceClass);
            if (mqServiceObj == null) {
                logger.error("mqService not found");
                return;
            }

            // active
            if (!messageBodyMap.containsKey("active")) {
                logger.error("active not found in message");
                return;
            }
            String active = (String) messageBodyMap.get("active");
            switch (active){
                case "start":
                    MQConfigInit.checkStartMq(mqServiceObj);
                    logger.info(String.format("MQAnnoService %s is start", mqService));
                    break;
                case "stop":
                    MQConfigInit.stopMQ(mqServiceObj);
                    logger.info(String.format("MQAnnoService %s is stop", mqService));
                    break;
                default:
                    logger.error("active not found");
                    break;
            }
        } catch (Exception ex) {
            logger.error("VOMQServiceControlListener error", ex);
        }
    }
}
