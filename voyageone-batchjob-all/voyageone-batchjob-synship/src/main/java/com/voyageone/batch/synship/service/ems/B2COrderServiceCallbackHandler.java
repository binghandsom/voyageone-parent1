/**
 * B2COrderServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */
package com.voyageone.batch.synship.service.ems;


/**
 *  B2COrderServiceCallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
public abstract class B2COrderServiceCallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public B2COrderServiceCallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public B2COrderServiceCallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for getOrders method
     * override this method for handling normal response from getOrders operation
     */
    public void receiveResultgetOrders(
        com.voyageone.batch.synship.service.ems.B2COrderServiceStub.GetOrdersResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getOrders operation
     */
    public void receiveErrorgetOrders(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for validCardNoFormat method
     * override this method for handling normal response from validCardNoFormat operation
     */
    public void receiveResultvalidCardNoFormat(
        com.voyageone.batch.synship.service.ems.B2COrderServiceStub.ValidCardNoFormatResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from validCardNoFormat operation
     */
    public void receiveErrorvalidCardNoFormat(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for validCarNo method
     * override this method for handling normal response from validCarNo operation
     */
    public void receiveResultvalidCarNo(
        com.voyageone.batch.synship.service.ems.B2COrderServiceStub.ValidCarNoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from validCarNo operation
     */
    public void receiveErrorvalidCarNo(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for isCardExistByOrder method
     * override this method for handling normal response from isCardExistByOrder operation
     */
    public void receiveResultisCardExistByOrder(
        com.voyageone.batch.synship.service.ems.B2COrderServiceStub.IsCardExistByOrderResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from isCardExistByOrder operation
     */
    public void receiveErrorisCardExistByOrder(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for addB2COrder method
     * override this method for handling normal response from addB2COrder operation
     */
    public void receiveResultaddB2COrder(
        com.voyageone.batch.synship.service.ems.B2COrderServiceStub.AddB2COrderResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from addB2COrder operation
     */
    public void receiveErroraddB2COrder(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getOrderByExpress method
     * override this method for handling normal response from getOrderByExpress operation
     */
    public void receiveResultgetOrderByExpress(
        com.voyageone.batch.synship.service.ems.B2COrderServiceStub.GetOrderByExpressResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getOrderByExpress operation
     */
    public void receiveErrorgetOrderByExpress(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for addB2COrderCD method
     * override this method for handling normal response from addB2COrderCD operation
     */
    public void receiveResultaddB2COrderCD(
        com.voyageone.batch.synship.service.ems.B2COrderServiceStub.AddB2COrderCDResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from addB2COrderCD operation
     */
    public void receiveErroraddB2COrderCD(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for newValidCarNo method
     * override this method for handling normal response from newValidCarNo operation
     */
    public void receiveResultnewValidCarNo(
        com.voyageone.batch.synship.service.ems.B2COrderServiceStub.NewValidCarNoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from newValidCarNo operation
     */
    public void receiveErrornewValidCarNo(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getPrintCount method
     * override this method for handling normal response from getPrintCount operation
     */
    public void receiveResultgetPrintCount(
        com.voyageone.batch.synship.service.ems.B2COrderServiceStub.GetPrintCountResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getPrintCount operation
     */
    public void receiveErrorgetPrintCount(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for isCardExist method
     * override this method for handling normal response from isCardExist operation
     */
    public void receiveResultisCardExist(
        com.voyageone.batch.synship.service.ems.B2COrderServiceStub.IsCardExistResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from isCardExist operation
     */
    public void receiveErrorisCardExist(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getOrderByCode method
     * override this method for handling normal response from getOrderByCode operation
     */
    public void receiveResultgetOrderByCode(
        com.voyageone.batch.synship.service.ems.B2COrderServiceStub.GetOrderByCodeResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getOrderByCode operation
     */
    public void receiveErrorgetOrderByCode(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getWaybillNo method
     * override this method for handling normal response from getWaybillNo operation
     */
    public void receiveResultgetWaybillNo(
        com.voyageone.batch.synship.service.ems.B2COrderServiceStub.GetWaybillNoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getWaybillNo operation
     */
    public void receiveErrorgetWaybillNo(Exception e) {
    }
}
