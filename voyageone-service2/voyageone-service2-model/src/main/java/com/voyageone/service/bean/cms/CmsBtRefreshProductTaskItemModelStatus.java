package com.voyageone.service.bean.cms;

/**
 * 默认属性功能下，商品属性值强制重刷任务，单个商品任务的状态
 * Created by jonas on 2016/11/4.
 *
 * @version 2.9.0
 * @since 2.9.0
 */
public interface CmsBtRefreshProductTaskItemModelStatus {
    /**
     * 初始状态，等待执行状态
     */
    int WAITING = 0;
    /**
     * 完成状态
     */
    int COMPLETED = 1;
}
