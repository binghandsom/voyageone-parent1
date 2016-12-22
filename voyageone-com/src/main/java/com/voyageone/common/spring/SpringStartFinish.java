package com.voyageone.common.spring;

import com.voyageone.common.configs.VOTestContext;
import com.voyageone.common.mq.config.MQConfigInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * SpringStartFinish
 *
 * @author chuanyu.liang 2016/5/5.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component()
@Lazy(value = false)
public class SpringStartFinish implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired(required=false)
    VOTestContext voTestContext;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(voTestContext!=null&&!voTestContext.isInitMQRabbitListenerConfig()) return;
        //root application context 没有parent，他就是老大.
        if(event.getApplicationContext().getParent() == null){
            // MQ Service 初始化
            MQConfigInit.init();
        }
    }

}
