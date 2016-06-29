package com.voyageone.common.idsnowflake;

/**
 * Created by admin on 2014/8/25.
 */
public class FactoryIdWorker {
    public static final IdWorker idWorker = new IdWorker(16, 18);

    //  配置分布式获取id的地址   重写该方法  方案待定
    public static long nextId() {
        return idWorker.NextId();
    }
}
