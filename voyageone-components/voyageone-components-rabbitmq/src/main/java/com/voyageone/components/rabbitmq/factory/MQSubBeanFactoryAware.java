package com.voyageone.components.rabbitmq.factory;

import com.voyageone.common.spring.SpringContext;
import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class MQSubBeanFactoryAware implements BeanFactoryAware, Ordered,  ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        SpringContext.getBeansWithAnnotationMap(VOSubRabbitListener.class);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
