package com.voyageone.web2.cms.wsdl.dao;

/**
 * 定义各 WSDL 系统 Dao Mapper 所属的命名空间
 * Created on 11/26/15.
 *
 * @author chuanyu.liang
 * @version 2.0.0
 */
public enum WsdlDaoNs {
    WSDL("com.voyageone.web2.wsdl.sql");

    private final String ns;

    WsdlDaoNs(String ns) {
        this.ns = ns;
    }

    public String getNs() {
        return ns;
    }
}
