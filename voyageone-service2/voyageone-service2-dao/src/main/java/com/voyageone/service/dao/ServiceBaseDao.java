package com.voyageone.service.dao;

import com.voyageone.base.dao.BaseDao;

/**
 * 预定义了 Service Dao 命名空间的基础类
 * Created on 11/26/15.
 *
 * @author chuanyu.liang
 * @version 2.0.0
 */
public abstract class ServiceBaseDao extends BaseDao {
    /**
     * 获取 mapper 的 namespace，只在初始化时调用
     */
    @Override
    protected String namespace() {
        return serviceNs().getNs();
    }

    protected ServiceDaoNs serviceNs() {
        return ServiceDaoNs.SERVICE_DAO_NS;
    }
}
