/**
 * SDKService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.voyageone.common.components.yimei.eucp;

public interface SDKService extends javax.xml.rpc.Service {
    public String getSDKServiceAddress();

    public com.voyageone.common.components.yimei.eucp.SDKClient getSDKService() throws javax.xml.rpc.ServiceException;

    public com.voyageone.common.components.yimei.eucp.SDKClient getSDKService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
