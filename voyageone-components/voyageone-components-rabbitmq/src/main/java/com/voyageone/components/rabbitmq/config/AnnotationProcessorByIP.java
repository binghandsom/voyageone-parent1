/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.voyageone.components.rabbitmq.config;

import com.voyageone.common.spring.SpringContext;
import com.voyageone.common.util.GenericSuperclassUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
import com.voyageone.components.rabbitmq.namesub.IMQSubBeanName;
import com.voyageone.components.rabbitmq.namesub.IMQSubBeanNameAll;
import com.voyageone.components.rabbitmq.factory.MQSubRabbitListenerFactory;
import com.voyageone.components.rabbitmq.service.IVOMQOnStartup;
import com.voyageone.components.rabbitmq.utils.MQConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.RabbitListenerConfigUtils;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.*;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AnnotationProcessorByIP
        implements BeanPostProcessor, Ordered, BeanFactoryAware, SmartInitializingSingleton {

    private final static Logger logger = LoggerFactory.getLogger(AnnotationProcessorByIP.class);

    /**
     * The bean name of the default {@link RabbitListenerContainerFactory}.
     */
    static final String DEFAULT_RABBIT_LISTENER_CONTAINER_FACTORY_BEAN_NAME = "rabbitListenerContainerFactory";

    private RabbitListenerEndpointRegistry endpointRegistry;

    private String containerFactoryBeanName = DEFAULT_RABBIT_LISTENER_CONTAINER_FACTORY_BEAN_NAME;

    private BeanFactory beanFactory;

    private final RabbitHandlerMethodFactoryAdapter messageHandlerMethodFactory =
            new RabbitHandlerMethodFactoryAdapter();

    private final RabbitListenerEndpointRegistrar registrar = new RabbitListenerEndpointRegistrar();

    private final AtomicInteger counter = new AtomicInteger();

    private BeanExpressionResolver resolver = new StandardBeanExpressionResolver();

    private BeanExpressionContext expressionContext;

    private int increment;

    private boolean local = false;

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }


    private AmqpAdmin amqpAdmin;

    public AmqpAdmin getAmqpAdmin() {
        return amqpAdmin;
    }

    public void setAmqpAdmin(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    /**
     * Set the {@link RabbitListenerEndpointRegistry} that will hold the created
     * endpoint and manage the lifecycle of the related listener container.
     *
     * @param endpointRegistry the {@link RabbitListenerEndpointRegistry} to set.
     */
    public void setEndpointRegistry(RabbitListenerEndpointRegistry endpointRegistry) {
        this.endpointRegistry = endpointRegistry;
    }

    /**
     * Set the name of the {@link RabbitListenerContainerFactory} to use by default.
     * <p>If none is specified, "rabbitListenerContainerFactory" is assumed to be defined.
     *
     * @param containerFactoryBeanName the {@link RabbitListenerContainerFactory} bean name.
     */
    public void setContainerFactoryBeanName(String containerFactoryBeanName) {
        this.containerFactoryBeanName = containerFactoryBeanName;
    }

    /**
     * Set the {@link MessageHandlerMethodFactory} to use to configure the message
     * listener responsible to serve an endpoint detected by this processor.
     * <p>By default, {@link DefaultMessageHandlerMethodFactory} is used and it
     * can be configured further to support additional method arguments
     * or to customize conversion and validation support. See
     * {@link DefaultMessageHandlerMethodFactory} Javadoc for more details.
     *
     * @param messageHandlerMethodFactory the {@link MessageHandlerMethodFactory} instance.
     */
    public void setMessageHandlerMethodFactory(MessageHandlerMethodFactory messageHandlerMethodFactory) {
        this.messageHandlerMethodFactory.setMessageHandlerMethodFactory(messageHandlerMethodFactory);
    }

    /**
     * Making a {@link BeanFactory} available is optional; if not set,
     * {@link RabbitListenerConfigurer} beans won't get autodetected and an
     * {@link #setEndpointRegistry endpoint registry} has to be explicitly configured.
     *
     * @param beanFactory the {@link BeanFactory} to be used.
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        if (beanFactory instanceof ConfigurableListableBeanFactory) {
            this.resolver = ((ConfigurableListableBeanFactory) beanFactory).getBeanExpressionResolver();
            this.expressionContext = new BeanExpressionContext((ConfigurableListableBeanFactory) beanFactory, null);
        }
    }


    @Override
    public void afterSingletonsInstantiated() {
        this.registrar.setBeanFactory(this.beanFactory);

        if (this.beanFactory instanceof ListableBeanFactory) {
            Map<String, RabbitListenerConfigurer> instances =
                    ((ListableBeanFactory) this.beanFactory).getBeansOfType(RabbitListenerConfigurer.class);
            for (RabbitListenerConfigurer configurer : instances.values()) {
                configurer.configureRabbitListeners(this.registrar);
            }
        }

        if (this.registrar.getEndpointRegistry() == null) {
            if (this.endpointRegistry == null) {
                Assert.state(this.beanFactory != null,
                        "BeanFactory must be set to find endpoint registry by bean name");
                this.endpointRegistry = this.beanFactory.getBean(
                        RabbitListenerConfigUtils.RABBIT_LISTENER_ENDPOINT_REGISTRY_BEAN_NAME,
                        RabbitListenerEndpointRegistry.class);
            }
            this.registrar.setEndpointRegistry(this.endpointRegistry);
        }

        if (this.containerFactoryBeanName != null) {
            this.registrar.setContainerFactoryBeanName(this.containerFactoryBeanName);
        }

        // Set the custom handler method factory once resolved by the configurer
        MessageHandlerMethodFactory handlerMethodFactory = this.registrar.getMessageHandlerMethodFactory();
        if (handlerMethodFactory != null) {
            this.messageHandlerMethodFactory.setMessageHandlerMethodFactory(handlerMethodFactory);
        }

        // Actually register all listeners
        this.registrar.afterPropertiesSet();
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof IMQSubBeanNameAll && bean instanceof IMQSubBeanName) {
            if (((IMQSubBeanName) bean).getSubBeanName() != null) {
                return bean;
            }

            String[] subBeanNames = ((IMQSubBeanNameAll) bean).getAllSubBeanName();
            if (subBeanNames == null || subBeanNames.length == 0) {
                return bean;
            }

            String newBeanName = MQConfigUtils.getNewBeanName(beanName, subBeanNames[0]);
            ((IMQSubBeanName) bean).setSubBeanName(subBeanNames[0]);
            logger.info(String.format("create new bean %s", newBeanName));

            for (int i = 1; i < subBeanNames.length; i++) {
                newBeanName = registerMQNewBean(beanName, subBeanNames[i], bean.getClass());
                logger.info(String.format("create new bean %s", newBeanName));
                IMQSubBeanName subBeanName = (IMQSubBeanName) SpringContext.getBean(newBeanName);
                //noinspection ConstantConditions
                subBeanName.setSubBeanName(subBeanNames[i]);
            }
        }
        return bean;
    }

    private String registerMQNewBean(String beanName, String subBeanName, Class<?> beanClass) {
        String newBeanName = MQConfigUtils.getNewBeanName(beanName, subBeanName);
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);
        beanDefinitionBuilder.addPropertyValue("subBeanName", subBeanName);
        ((DefaultListableBeanFactory) beanFactory).registerBeanDefinition(newBeanName, beanDefinitionBuilder.getRawBeanDefinition());
        return newBeanName;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);

        /**
         * RabbitListener
         */
        RabbitListener classLevelListener = AnnotationUtils.findAnnotation(targetClass, RabbitListener.class);
        if (classLevelListener == null) {
            VOSubRabbitListener vosubRabbitListener = AnnotationUtils.findAnnotation(targetClass, VOSubRabbitListener.class);
            if (vosubRabbitListener != null) {
                classLevelListener = MQSubRabbitListenerFactory.create(vosubRabbitListener);
            }
        }
        if (classLevelListener != null) {
            final List<Method> multiMethods = new ArrayList<>();
            ReflectionUtils.doWithMethods(targetClass, new ReflectionUtils.MethodCallback() {

                @Override
                public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                    RabbitListener rabbitListener = AnnotationUtils.findAnnotation(method, RabbitListener.class);
                    if (rabbitListener != null) {
                        processAmqpListener(rabbitListener, method, bean, beanName);
                    }
                    RabbitHandler rabbitHandler = AnnotationUtils.findAnnotation(method, RabbitHandler.class);
                    if (rabbitHandler != null) {
                        multiMethods.add(method);
                    }
                }
            });
            processMultiMethodListener(classLevelListener, multiMethods, bean, beanName);
        }

        return bean;
    }

    private void processMultiMethodListener(RabbitListener classLevelListener, List<Method> multiMethods, Object bean,
                                            String beanName) {
        List<Method> checkedMethods = new ArrayList<>();
        for (Method method : multiMethods) {
            checkedMethods.add(checkProxy(method, bean));
        }
        MultiMethodRabbitListenerEndpoint endpoint = new MultiMethodRabbitListenerEndpoint(checkedMethods, bean);
        endpoint.setBeanFactory(this.beanFactory);
        processListener(endpoint, classLevelListener, bean, bean.getClass(), beanName);
    }

    protected void processAmqpListener(RabbitListener rabbitListener, Method method, Object bean, String beanName) {
        Method methodToUse = checkProxy(method, bean);
        MethodRabbitListenerEndpoint endpoint = new MethodRabbitListenerEndpoint();
        endpoint.setMethod(methodToUse);
        endpoint.setBeanFactory(this.beanFactory);
        processListener(endpoint, rabbitListener, bean, methodToUse, beanName);
    }

    private Method checkProxy(Method method, Object bean) {
        if (AopUtils.isJdkDynamicProxy(bean)) {
            try {
                // Found a @RabbitListener method on the target class for this JDK proxy ->
                // is it also present on the proxy itself?
                method = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
                Class<?>[] proxiedInterfaces = ((Advised) bean).getProxiedInterfaces();
                for (Class<?> iface : proxiedInterfaces) {
                    try {
                        method = iface.getMethod(method.getName(), method.getParameterTypes());
                        break;
                    } catch (NoSuchMethodException ignored) {
                    }
                }
            } catch (SecurityException ex) {
                ReflectionUtils.handleReflectionException(ex);
            } catch (NoSuchMethodException ex) {
                throw new IllegalStateException(String.format(
                        "@RabbitListener method '%s' found on bean target class '%s', " +
                                "but not found in any interface(s) for bean JDK proxy. Either " +
                                "pull the method up to an interface or switch to subclass (CGLIB) " +
                                "proxies by setting proxy-target-class/proxyTargetClass " +
                                "attribute to 'true'", method.getName(), method.getDeclaringClass().getSimpleName()));
            }
        }
        return method;
    }

    private void processListener(MethodRabbitListenerEndpoint endpoint, RabbitListener rabbitListener, Object bean,
                                 Object adminTarget, String beanName) {
        endpoint.setBean(bean);
        endpoint.setMessageHandlerMethodFactory(this.messageHandlerMethodFactory);

        /**
         * add ip to quenes key aooer start
         */
        endpoint.setId(MQConfigUtils.getEndPointName(AopUtils.getTargetClass(bean).getName(), beanName));


        String[] queues = resolveQueues(rabbitListener, bean, beanName);

        if (local) {
            for (int i = 0; i < queues.length; i++) {
                queues[i] = MQConfigUtils.getAddStrQueneName(queues[i]);
            }
        }
        for (String queue : queues) {
            amqpAdmin.declareQueue(new Queue(queue, true, false, local));
        }
        endpoint.setQueueNames(queues);
        /**
         * add ip to quenes key aooer end
         */

        String group = rabbitListener.group();
        if (StringUtils.hasText(group)) {
            Object resolvedGroup = resolveExpression(group);
            if (resolvedGroup instanceof String) {
                endpoint.setGroup((String) resolvedGroup);
            }
        }

        endpoint.setExclusive(rabbitListener.exclusive());
        String priority = resolve(rabbitListener.priority());
        if (StringUtils.hasText(priority)) {
            try {
                endpoint.setPriority(Integer.valueOf(priority));
            } catch (NumberFormatException ex) {
                throw new BeanInitializationException("Invalid priority value for " +
                        rabbitListener + " (must be an integer)", ex);
            }
        }

        String rabbitAdmin = resolve(rabbitListener.admin());
        if (StringUtils.hasText(rabbitAdmin)) {
            Assert.state(this.beanFactory != null, "BeanFactory must be set to resolve RabbitAdmin by bean name");
            try {
                endpoint.setAdmin(this.beanFactory.getBean(rabbitAdmin, RabbitAdmin.class));
            } catch (NoSuchBeanDefinitionException ex) {
                throw new BeanInitializationException("Could not register rabbit listener endpoint on [" +
                        adminTarget + "], no " + RabbitAdmin.class.getSimpleName() + " with id '" +
                        rabbitAdmin + "' was found in the application context", ex);
            }
        }

        RabbitListenerContainerFactory<?> factory = null;
        String containerFactoryBeanName = resolve(rabbitListener.containerFactory());
        if (StringUtils.hasText(containerFactoryBeanName)) {
            Assert.state(this.beanFactory != null, "BeanFactory must be set to obtain container factory by bean name");
            try {
                factory = this.beanFactory.getBean(containerFactoryBeanName, RabbitListenerContainerFactory.class);
            } catch (NoSuchBeanDefinitionException ex) {
                throw new BeanInitializationException("Could not register rabbit listener endpoint on [" +
                        adminTarget + "] for bean " + beanName + ", no " + RabbitListenerContainerFactory.class.getSimpleName() + " with id '" +
                        containerFactoryBeanName + "' was found in the application context", ex);
            }
        }

        this.registrar.registerEndpoint(endpoint, factory);
    }

    private String getEndpointId(RabbitListener rabbitListener) {
        if (StringUtils.hasText(rabbitListener.id())) {
            return resolve(rabbitListener.id());
        } else {
            return "org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#" + counter.getAndIncrement();
        }
    }

    // new add begin
    private String[] resolveQueues(RabbitListener rabbitListener, Object bean, String beanName) {
        String[] queues = getQueues(rabbitListener, bean, beanName);
        QueueBinding[] bindings = rabbitListener.bindings();
        if (queues.length > 0 && bindings.length > 0) {
            throw new BeanInitializationException("@RabbitListener can have 'queues' or 'bindings' but not both");
        }
        List<String> result = new ArrayList<>();
        if (queues.length > 0) {
            for (String queue : queues) {
                Object resolvedValue = resolveExpression(queue);
                resolveAsString(resolvedValue, result);
            }
        } else {
            return registerBeansForDeclaration(rabbitListener);
        }
        return result.toArray(new String[result.size()]);
    }

    private String[] getQueues(RabbitListener rabbitListener, Object bean, String beanName) {
        String[] queues = rabbitListener.queues();
        if (queues.length > 0) return queues;
        if (bean instanceof IVOMQOnStartup) {
            //获取消息体类class
            Class<? extends IMQMessageBody> messageBodyClass = GenericSuperclassUtils.getGenericActualTypeClass(bean);
            //获取队列的注解
            final VOMQQueue voQueue = AnnotationUtils.findAnnotation(messageBodyClass, VOMQQueue.class);
            if (voQueue != null) {
                if (bean instanceof IMQSubBeanName) {
                    String subBeanName = ((IMQSubBeanName) bean).getSubBeanName();
                    if (subBeanName != null) {
                        return new String[]{MQConfigUtils.getNewQueueName(voQueue.value(), subBeanName)};
                    } else {
                        return new String[0];
                    }
                }
                return new String[]{voQueue.value()};
            }
        }
        return new String[0];
    }
    // new add end

//    private String[] resolveQueues(RabbitListener rabbitListener) {
//        String[] queues = rabbitListener.queues();
//        QueueBinding[] bindings = rabbitListener.bindings();
//        if (queues.length > 0 && bindings.length > 0) {
//            throw new BeanInitializationException("@RabbitListener can have 'queues' or 'bindings' but not both");
//        }
//        List<String> result = new ArrayList<String>();
//        if (queues.length > 0) {
//            for (String queue : queues) {
//                Object resolvedValue = resolveExpression(queue);
//                resolveAsString(resolvedValue, result);
//            }
//        } else {
//            return registerBeansForDeclaration(rabbitListener);
//        }
//        return result.toArray(new String[result.size()]);
//    }

    @SuppressWarnings("unchecked")
    private void resolveAsString(Object resolvedValue, List<String> result) {
        Object resolvedValueToUse = resolvedValue;
        if (resolvedValue instanceof String[]) {
            resolvedValueToUse = Arrays.asList((String[]) resolvedValue);
        }
        if (resolvedValueToUse instanceof Queue) {
            result.add(((Queue) resolvedValueToUse).getName());
        } else if (resolvedValueToUse instanceof String) {
            result.add((String) resolvedValueToUse);
        } else if (resolvedValueToUse instanceof Iterable) {
            for (Object object : (Iterable<Object>) resolvedValueToUse) {
                resolveAsString(object, result);
            }
        } else {
            throw new IllegalArgumentException(String.format(
                    "@RabbitListener can't resolve '%s' as either a String or a Queue",
                    resolvedValue));
        }
    }

    private Object resolveExpression(String value) {
        String resolvedValue = resolve(value);

        if (!(resolvedValue.startsWith("#{") && value.endsWith("}"))) {
            return resolvedValue;
        }

        return this.resolver.evaluate(resolvedValue, this.expressionContext);
    }

    /**
     * Resolve the specified value if possible.
     *
     * @see ConfigurableBeanFactory#resolveEmbeddedValue
     */
    private String resolve(String value) {
        if (this.beanFactory != null && this.beanFactory instanceof ConfigurableBeanFactory) {
            return ((ConfigurableBeanFactory) this.beanFactory).resolveEmbeddedValue(value);
        }
        return value;
    }

    private String[] registerBeansForDeclaration(RabbitListener rabbitListener) {
        List<String> queues = new ArrayList<String>();
        if (this.beanFactory instanceof ConfigurableBeanFactory) {
            for (QueueBinding binding : rabbitListener.bindings()) {
                org.springframework.amqp.rabbit.annotation.Queue bindingQueue = binding.value();
                String queueName = (String) resolveExpression(bindingQueue.value());
                boolean exclusive = false;
                boolean autoDelete = false;
                if (!StringUtils.hasText(queueName)) {
                    queueName = UUID.randomUUID().toString();
                    // default exclusive/autodelete to true when anonymous
                    if (!StringUtils.hasText(bindingQueue.exclusive())
                            || resolveExpressionAsBoolean(bindingQueue.exclusive())) {
                        exclusive = true;
                    }
                    if (!StringUtils.hasText(bindingQueue.autoDelete())
                            || resolveExpressionAsBoolean(bindingQueue.autoDelete())) {
                        autoDelete = true;
                    }
                } else {
                    exclusive = resolveExpressionAsBoolean(bindingQueue.exclusive());
                    autoDelete = resolveExpressionAsBoolean(bindingQueue.autoDelete());
                }
                Queue queue = new Queue(queueName,
                        resolveExpressionAsBoolean(bindingQueue.durable()),
                        exclusive,
                        autoDelete);
                ((ConfigurableBeanFactory) this.beanFactory).registerSingleton(queueName + ++this.increment, queue);
                queues.add(queueName);
                Exchange exchange = null;
                org.springframework.amqp.rabbit.annotation.Exchange bindingExchange = binding.exchange();
                String exchangeName = (String) resolveExpression(bindingExchange.value());
                String exchangeType = bindingExchange.type();
                Binding actualBinding = null;
                Object key = resolveExpression(binding.key());
                if (!(key instanceof String)) {
                    throw new BeanInitializationException("key must resolved to a String, not: " + key.getClass().toString());
                }
                String resolvedKey = (String) key;
                if (exchangeType.equals(ExchangeTypes.DIRECT)) {
                    exchange = new DirectExchange(exchangeName,
                            resolveExpressionAsBoolean(bindingExchange.durable()),
                            resolveExpressionAsBoolean(bindingExchange.autoDelete()));
                    actualBinding = new Binding(queueName, DestinationType.QUEUE, exchangeName, resolvedKey, null);
                } else if (exchangeType.equals(ExchangeTypes.FANOUT)) {
                    exchange = new FanoutExchange(exchangeName,
                            resolveExpressionAsBoolean(bindingExchange.durable()),
                            resolveExpressionAsBoolean(bindingExchange.autoDelete()));
                    actualBinding = new Binding(queueName, DestinationType.QUEUE, exchangeName, "", null);
                } else if (exchangeType.equals(ExchangeTypes.TOPIC)) {
                    exchange = new TopicExchange(exchangeName,
                            resolveExpressionAsBoolean(bindingExchange.durable()),
                            resolveExpressionAsBoolean(bindingExchange.autoDelete()));
                    actualBinding = new Binding(queueName, DestinationType.QUEUE, exchangeName, resolvedKey, null);
                } else {
                    throw new BeanInitializationException("Unexpected exchange type: " + exchangeType);
                }
                ((ConfigurableBeanFactory) this.beanFactory).registerSingleton(exchangeName + ++this.increment, exchange);
                ((ConfigurableBeanFactory) this.beanFactory).registerSingleton(exchangeName + ++this.increment, actualBinding);
            }
        }
        return queues.toArray(new String[queues.size()]);
    }

    private boolean resolveExpressionAsBoolean(String value) {
        Object resolved = resolveExpression(value);
        if (resolved instanceof Boolean) {
            return (Boolean) resolved;
        } else if (resolved instanceof String) {
            return Boolean.valueOf((String) resolved);
        } else {
            return false;
        }
    }

    /**
     * An {@link MessageHandlerMethodFactory} adapter that offers a configurable underlying
     * instance to use. Useful if the factory to use is determined once the endpoints
     * have been registered but not created yet.
     *
     * @see RabbitListenerEndpointRegistrar#setMessageHandlerMethodFactory
     */
    private class RabbitHandlerMethodFactoryAdapter implements MessageHandlerMethodFactory {

        private MessageHandlerMethodFactory messageHandlerMethodFactory;

        public void setMessageHandlerMethodFactory(MessageHandlerMethodFactory rabbitHandlerMethodFactory1) {
            this.messageHandlerMethodFactory = rabbitHandlerMethodFactory1;
        }

        @Override
        public InvocableHandlerMethod createInvocableHandlerMethod(Object bean, Method method) {
            return getMessageHandlerMethodFactory().createInvocableHandlerMethod(bean, method);
        }

        private MessageHandlerMethodFactory getMessageHandlerMethodFactory() {
            if (this.messageHandlerMethodFactory == null) {
                this.messageHandlerMethodFactory = createDefaultMessageHandlerMethodFactory();
            }
            return this.messageHandlerMethodFactory;
        }

        private MessageHandlerMethodFactory createDefaultMessageHandlerMethodFactory() {
            DefaultMessageHandlerMethodFactory defaultFactory = new DefaultMessageHandlerMethodFactory();
            defaultFactory.setBeanFactory(beanFactory);
            defaultFactory.afterPropertiesSet();
            return defaultFactory;
        }

    }

}
