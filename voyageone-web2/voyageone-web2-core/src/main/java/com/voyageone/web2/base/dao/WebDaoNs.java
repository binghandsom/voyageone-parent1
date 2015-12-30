package com.voyageone.web2.base.dao;

/**
 * 定义各 Web 系统 Dao Mapper 所属的命名空间
 * Created on 11/26/15.
 *
 * @author Jonas
 * @version 2.0.0
 */
public enum WebDaoNs {
    CORE("com.voyageone.web2.core.sql"),
    CMS("com.voyageone.web2.cms.sql"),
    WSDL("com.voyageone.web2.wsdl.sql");

    private final String ns;

    WebDaoNs(String ns) {
        this.ns = ns;
    }

    public String getNs() {
        return ns;
    }
}
