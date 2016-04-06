package com.voyageone.common.components.yimei.eucp;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;

public class SDKClientProxy implements SDKClient {
  private String _endpoint = null;
  private com.voyageone.common.components.yimei.eucp.SDKClient sDKClient = null;
  
  public SDKClientProxy() {
    _initSDKClientProxy();
  }
  
  public SDKClientProxy(String endpoint) {
    _endpoint = endpoint;
    _initSDKClientProxy();
  }
  
  private void _initSDKClientProxy() {
    try {
      sDKClient = (new com.voyageone.common.components.yimei.eucp.SDKServiceLocator()).getSDKService();
      if (sDKClient != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)sDKClient)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)sDKClient)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
    }
    catch (ServiceException ignored) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (sDKClient != null)
      ((javax.xml.rpc.Stub)sDKClient)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public SDKClient getSDKClient() {
    if (sDKClient == null)
      _initSDKClientProxy();
    return sDKClient;
  }

  @Override
  public String getVersion() throws RemoteException{
    if (sDKClient == null)
      _initSDKClientProxy();
    return sDKClient.getVersion();
  }

  @Override
  public StatusReport[] getReport(String arg0, String arg1) throws RemoteException {
    if (sDKClient == null)
      _initSDKClientProxy();
    return sDKClient.getReport(arg0, arg1);
  }

  @Override
  public int cancelMOForward(String arg0, String arg1) throws RemoteException{
    if (sDKClient == null)
      _initSDKClientProxy();
    return sDKClient.cancelMOForward(arg0, arg1);
  }

  @Override
  public int chargeUp(String arg0, String arg1, String arg2, String arg3) throws RemoteException{
    if (sDKClient == null)
      _initSDKClientProxy();
    return sDKClient.chargeUp(arg0, arg1, arg2, arg3);
  }

  @Override
  public double getBalance(String arg0, String arg1) throws RemoteException{
    if (sDKClient == null)
      _initSDKClientProxy();
    return sDKClient.getBalance(arg0, arg1);
  }

  @Override
  public double getEachFee(String arg0, String arg1) throws RemoteException{
    if (sDKClient == null)
      _initSDKClientProxy();
    return sDKClient.getEachFee(arg0, arg1);
  }
  
  public com.voyageone.common.components.yimei.eucp.Mo[] getMO(String arg0, String arg1) throws RemoteException{
    if (sDKClient == null)
      _initSDKClientProxy();
    return sDKClient.getMO(arg0, arg1);
  }

  @Override
  public int logout(String arg0, String arg1) throws RemoteException{
    if (sDKClient == null)
      _initSDKClientProxy();
    return sDKClient.logout(arg0, arg1);
  }

  @Override
  public int registDetailInfo(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9) throws RemoteException{
    if (sDKClient == null)
      _initSDKClientProxy();
    return sDKClient.registDetailInfo(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
  }

  @Override
  public int registEx(String arg0, String arg1, String arg2) throws RemoteException{
    if (sDKClient == null)
      _initSDKClientProxy();
    return sDKClient.registEx(arg0, arg1, arg2);
  }

  @Override
  public int sendSMS(String arg0, String arg1, String arg2, String[] arg3, String arg4, String arg5, String arg6, int arg7, long arg8) throws RemoteException{
    if (sDKClient == null)
      _initSDKClientProxy();
    return sDKClient.sendSMS(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
  }

  @Override
  public String sendVoice(String arg0, String arg1, String arg2, String[] arg3, String arg4, String arg5, String arg6, int arg7, long arg8) throws RemoteException{
    if (sDKClient == null)
      _initSDKClientProxy();
    return sDKClient.sendVoice(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
  }

  @Override
  public int serialPwdUpd(String arg0, String arg1, String arg2, String arg3) throws RemoteException{
    if (sDKClient == null)
      _initSDKClientProxy();
    return sDKClient.serialPwdUpd(arg0, arg1, arg2, arg3);
  }

  @Override
  public int setMOForward(String arg0, String arg1, String arg2) throws RemoteException{
    if (sDKClient == null)
      _initSDKClientProxy();
    return sDKClient.setMOForward(arg0, arg1, arg2);
  }

  @Override
  public int setMOForwardEx(String arg0, String arg1, String[] arg2) throws RemoteException{
    if (sDKClient == null)
      _initSDKClientProxy();
    return sDKClient.setMOForwardEx(arg0, arg1, arg2);
  }
  
  
}