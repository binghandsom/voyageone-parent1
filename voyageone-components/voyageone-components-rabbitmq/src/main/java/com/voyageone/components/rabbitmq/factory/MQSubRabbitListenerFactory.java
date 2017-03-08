package com.voyageone.components.rabbitmq.factory;

import com.athaydes.javanna.Javanna;
import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * MQSub RabbitListener Factory
 * Created on 2017/01/11
 *
 * @author chuanyu.liang
 * @version 1.0.0
 */
public class MQSubRabbitListenerFactory {

    public static RabbitListener create(VOSubRabbitListener voSubRabbitListener) {
        // create new Reference
        return Javanna.createAnnotation(RabbitListener.class, Javanna.getAnnotationValues(voSubRabbitListener));
    }
}
