package com.voyageone.web2.cms.wsdl.dao;

import com.voyageone.base.dao.BaseDao;

/**
 * 预定义了 WSDL Dao 命名空间的基础类
 * Created on 11/26/15.
 *
 * @author chuanyu.liang
 * @version 2.0.0
 */
public abstract class WsdlBaseDao extends BaseDao {
    /**
     * 获取 mapper 的 namespace，只在初始化时调用
     */
    @Override
    protected String namespace() {
        return webNs().getNs();
    }

    protected WsdlDaoNs webNs() {
        return WsdlDaoNs.WSDL;
    }
}
