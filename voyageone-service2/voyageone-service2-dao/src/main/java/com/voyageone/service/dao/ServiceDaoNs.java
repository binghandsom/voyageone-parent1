package com.voyageone.service.dao;

/**
 * 定义各 WSDL 系统 Dao Mapper 所属的命名空间
 * Created on 11/26/15.
 *
 * @author chuanyu.liang
 * @version 2.0.0
 */
public enum ServiceDaoNs {
    SERVICE_DAO_NS("com.voyageone.service.dao.sql");

    private final String ns;

    ServiceDaoNs(String ns) {
        this.ns = ns;
    }

    public String getNs() {
        return ns;
    }
}
