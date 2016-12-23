package com.voyageone.common.configs;


import com.voyageone.components.rabbitmq.utils.MQConfigInit;

/**
 * Created by dell on 2016/12/22.
 */
public class MQConfigInitTestUtil {
    public static void startMQ(Object service) throws InterruptedException {

        System.out.print("begin:"+service.getClass().getName());
        //启动监听服务
        MQConfigInit.checkStartMq(service);

        System.out.print("end:"+service.getClass().getName());
        //线程休眠
        Thread.sleep(999999999999999999L);
    }
}
