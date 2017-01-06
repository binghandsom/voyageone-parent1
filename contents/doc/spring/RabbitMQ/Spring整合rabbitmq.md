https://my.oschina.net/superise/blog/692330

Spring整合rabbitmq
摘要: 主要记录下Rabbitmq通过配置文件的方式整合到Spring的方法
发送者 spring配置文件
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context" 
    xmlns:rabbit="http://www.springframework.org/schema/rabbit"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd
    http://www.springframework.org/schema/rabbit
    http://www.springframework.org/schema/rabbit/spring-rabbit-1.4.xsd">

    <!-- 连接服务配置  -->
    <rabbit:connection-factory id="connectionFactory" addresses="${mq.addresses}"  username="${mq.username}" password="${mq.password}"/>

    <rabbit:admin connection-factory="connectionFactory"/>

    <!-- queue 队列声明-->
    <rabbit:queue id="queue_one" durable="true" auto-delete="false" exclusive="false" name="queue_one"/>
    <rabbit:queue id="queue_topic" durable="true" auto-delete="false" exclusive="false" name="queue_topic"/>

    <!-- exchange queue binging key 绑定，作为点对点模式使用 -->
    <rabbit:direct-exchange name="direct-exchange" durable="true" auto-delete="false" id="exchange-redirect">
        <rabbit:bindings>
            <rabbit:binding queue="queue_one" key="queue_one_key"/>
        </rabbit:bindings>
    </rabbit:direct-exchange>

    <!-- fanout-exchange，作为发布-订阅模式使用。
        由于RabbitMQ的发布订阅模型是根据多个queue，多个消费者订阅实现的。此处声明的exchange不必预先绑定queue，
        在消费者声明queue并绑定到该exchange即可。
     -->
    <rabbit:fanout-exchange name="fanout-exchange" durable="true" auto-delete="false" id="fanout-exchange">
    </rabbit:fanout-exchange>

    <!-- topic-exchange，作为主题模式使用。
        匹配routingkey的模式，这里匹配两个queue
        queue_topic准备匹配key为zhu.q1
        queue_topic2准备匹配key为zhu.q2
     -->
    <rabbit:topic-exchange name="topic-exchange">  
        <rabbit:bindings>  
            <rabbit:binding queue="queue_topic" pattern="zhu.*" />  
            <rabbit:binding queue="queue_topic2" pattern="zhu.*" />  
        </rabbit:bindings>  
    </rabbit:topic-exchange>  

    <!-- spring amqp默认的是jackson 的一个插件,目的将生产者生产的数据转换为json存入消息队列，由于fastjson的速度快于jackson,这里替换为fastjson的一个实现 -->
    <bean id="jsonMessageConverter" class="uap.web.mq.rabbit.FastJsonMessageConverter"></bean>
    <!--
    <bean id="jsonMessageConverter" class="org.springframework.amqp.support.converter.JsonMessageConverter"></bean>
    -->

    <!-- spring template声明 (点对点) -->
    <rabbit:template exchange="direct-exchange" id="amqpTemplate" retry-template="retryConnTemplate" connection-factory="connectionFactory"  message-converter="jsonMessageConverter"/>

    <!-- spring template声明（发布，订阅） -->
    <rabbit:template exchange="fanout-exchange" id="fanoutTemplate"  retry-template="retryConnTemplate"  connection-factory="connectionFactory"  message-converter="jsonMessageConverter"/>

    <!-- 通用 template声明 -->
    <rabbit:template id="rabbitTemplate"  retry-template="retryConnTemplate"  connection-factory="connectionFactory"  message-converter="jsonMessageConverter"/>

    <!-- 增加失败重试机制，发送失败之后，会尝试重发三次，重发间隔(ms)为 
        第一次 initialInterval 
        此后：initialInterval*multiplier > maxInterval ? maxInterval : initialInterval*multiplier。
        配合集群使用的时候，当mq集群中一个down掉之后，重试机制尝试其他可用的mq。
     -->
    <bean id="retryConnTemplate" class="org.springframework.retry.support.RetryTemplate">
        <property name="backOffPolicy">
            <bean class="org.springframework.retry.backoff.ExponentialBackOffPolicy">
                <property name="initialInterval" value="500"/>
                <property name="multiplier" value="10.0"/>
                <property name="maxInterval" value="5000"/>
            </bean>
        </property>
    </bean>
</beans>
消息发送代码
package com.zhu.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class RabbitMqSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private AmqpTemplate fanoutTemplate;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    /**
     * 点对点
     */
    public void sendDataToCrQueue(Object obj) {
        amqpTemplate.convertAndSend("queue_one_key", obj);
    }

    /**
     * 发送 发布--订阅消息
     */
    public void sendFanoutMsg(Object obj) {
        fanoutTemplate.convertAndSend(obj);
    }

    /**
     * 主题
     */
    public void sendTopicMsg(String topic,Object obj) {
        rabbitTemplate.convertAndSend(topic,obj);
    }

}
发送消息
RabbitMqSender sender = new RabbitMqSender();
String str="test1";
Object obj = (Object)str;
sender.sendDataToCrQueue(obj);
sender.sendFanoutMsg(obj);
sender.sendTopicMsg("zhu.p1",obj);
sender.sendTopicMsg("zhu.p2",obj);
接受者
spring配置文件
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context" 
    xmlns:rabbit="http://www.springframework.org/schema/rabbit"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd
    http://www.springframework.org/schema/rabbit
    http://www.springframework.org/schema/rabbit/spring-rabbit-1.4.xsd">

    <!-- 连接服务配置  -->
    <rabbit:connection-factory id="connectionFactory" addresses="${mq.addresses}"  username="${mq.username}" password="${mq.password}"/>

    <rabbit:admin connection-factory="connectionFactory"/>

    <!-- queue 队列声明(direct)  -->
    <rabbit:queue id="queue_one" durable="true" auto-delete="false" exclusive="false" name="queue_one"/>
    <!-- 声明的订阅模型的queue(fanout) -->
    <rabbit:queue id="fanout-subscribe" durable="true" auto-delete="false" exclusive="false" name="fanout-subscribe"/>
    <rabbit:queue id="fanout-subscribe2" durable="true" auto-delete="false" exclusive="false" name="fanout-subscribe2"/>
    <!-- 声明的订阅模型的queue(topic) -->
    <rabbit:queue id="queue_topic" durable="true" auto-delete="false" exclusive="false" name="queue_topic"/>
    <rabbit:queue id="queue_topic2" durable="true" auto-delete="false" exclusive="false" name="queue_topic2"/>

    <!-- 将订阅的queue绑定到fanout-exchange上 -->
    <rabbit:fanout-exchange name="fanout-exchange" durable="true" auto-delete="false" id="fanout-exchange">
        <rabbit:bindings>
            <rabbit:binding queue="fanout-subscribe"></rabbit:binding>
            <rabbit:binding queue="fanout-subscribe2"></rabbit:binding>
        </rabbit:bindings>
    </rabbit:fanout-exchange>

    <!-- 定义queue监听器 -->
    <bean id="DirectListener" class="com.zhu.mq.DirectListener"></bean>
    <bean id="FanoutLitener" class="com.zhu.mq.FanoutLitener"></bean>
    <bean id="TopicLitener" class="com.zhu.mq.TopicLitener"></bean>

    <!-- direct监听器绑定-->
    <rabbit:listener-container connection-factory="connectionFactory" acknowledge="auto">
        <rabbit:listener queues="queue_one" ref="DirectListener"/>
    </rabbit:listener-container>

    <!-- fanout监听器绑定 -->
    <rabbit:listener-container connection-factory="connectionFactory" acknowledge="auto">
        <rabbit:listener queues="fanout-subscribe" ref="FanoutLitener"/>
    </rabbit:listener-container>

    <rabbit:listener-container connection-factory="connectionFactory" acknowledge="auto">
        <rabbit:listener queues="fanout-subscribe2" ref="FanoutLitener"/>
    </rabbit:listener-container>

    <!-- topic监听器绑定-->
    <rabbit:listener-container connection-factory="connectionFactory" acknowledge="auto">
        <rabbit:listener queues="queue_topic" ref="TopicLitener"/>
    </rabbit:listener-container>

    <rabbit:listener-container connection-factory="connectionFactory" acknowledge="auto">
        <rabbit:listener queues="queue_topic2" ref="TopicLitener"/>
    </rabbit:listener-container>

</beans>
监听器代码 direct监听器
package com.zhu.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class DirectListener implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(DirectListener.class);

    @Override
    public void onMessage(Message message) {
        logger.info("Direct queue data:" + new String(message.getBody()));
    }
}
fanout监听器
package com.zhu.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class FanoutLitener implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(FanoutLitener.class);

    @Override
    public void onMessage(Message message) {
        logger.info("Fanout queue data:" + new String(message.getBody()));
    }
}
topic监听器
package com.zhu.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class TopicLitener implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(TopicLitener.class);

    @Override
    public void onMessage(Message message) {
        logger.info("Topic queue data:" + new String(message.getBody()));
    }
}