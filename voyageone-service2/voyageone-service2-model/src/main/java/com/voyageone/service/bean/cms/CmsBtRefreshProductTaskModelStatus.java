package com.voyageone.service.bean.cms;

/**
 * 默认属性功能下，商品属性值强制重刷任务的状态
 * Created by jonas on 2016/11/4.
 *
 * @version 2.9.0
 * @since 2.9.0
 */
public interface CmsBtRefreshProductTaskModelStatus {
    /**
     * 初始状态，等待执行状态
     */
    int WAITING = 0;
    /**
     * 完成状态，所有子（商品）任务都完成
     */
    int COMPLETED = 1;
}
