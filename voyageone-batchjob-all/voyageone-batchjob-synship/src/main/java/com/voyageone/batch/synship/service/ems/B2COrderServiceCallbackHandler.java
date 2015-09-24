
/**
 * B2COrderServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */

    package com.voyageone.batch.synship.service.ems;

    /**
     *  B2COrderServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class B2COrderServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public B2COrderServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public B2COrderServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for GetOrderByExpress method
            * override this method for handling normal response from GetOrderByExpress operation
            */
           public void receiveResultGetOrderByExpress(
                    com.voyageone.batch.synship.service.ems.B2COrderServiceStub.GetOrderByExpressResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetOrderByExpress operation
           */
            public void receiveErrorGetOrderByExpress(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for NewValidCarNo method
            * override this method for handling normal response from NewValidCarNo operation
            */
           public void receiveResultNewValidCarNo(
                    com.voyageone.batch.synship.service.ems.B2COrderServiceStub.NewValidCarNoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from NewValidCarNo operation
           */
            public void receiveErrorNewValidCarNo(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ValidCardNoFormat method
            * override this method for handling normal response from ValidCardNoFormat operation
            */
           public void receiveResultValidCardNoFormat(
                    com.voyageone.batch.synship.service.ems.B2COrderServiceStub.ValidCardNoFormatResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ValidCardNoFormat operation
           */
            public void receiveErrorValidCardNoFormat(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetOrderByCode method
            * override this method for handling normal response from GetOrderByCode operation
            */
           public void receiveResultGetOrderByCode(
                    com.voyageone.batch.synship.service.ems.B2COrderServiceStub.GetOrderByCodeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetOrderByCode operation
           */
            public void receiveErrorGetOrderByCode(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetWaybillNo method
            * override this method for handling normal response from GetWaybillNo operation
            */
           public void receiveResultGetWaybillNo(
                    com.voyageone.batch.synship.service.ems.B2COrderServiceStub.GetWaybillNoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetWaybillNo operation
           */
            public void receiveErrorGetWaybillNo(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetPrintCount method
            * override this method for handling normal response from GetPrintCount operation
            */
           public void receiveResultGetPrintCount(
                    com.voyageone.batch.synship.service.ems.B2COrderServiceStub.GetPrintCountResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetPrintCount operation
           */
            public void receiveErrorGetPrintCount(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for AddB2COrder method
            * override this method for handling normal response from AddB2COrder operation
            */
           public void receiveResultAddB2COrder(
                    com.voyageone.batch.synship.service.ems.B2COrderServiceStub.AddB2COrderResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from AddB2COrder operation
           */
            public void receiveErrorAddB2COrder(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetOrders method
            * override this method for handling normal response from GetOrders operation
            */
           public void receiveResultGetOrders(
                    com.voyageone.batch.synship.service.ems.B2COrderServiceStub.GetOrdersResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetOrders operation
           */
            public void receiveErrorGetOrders(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ValidCarNo method
            * override this method for handling normal response from ValidCarNo operation
            */
           public void receiveResultValidCarNo(
                    com.voyageone.batch.synship.service.ems.B2COrderServiceStub.ValidCarNoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ValidCarNo operation
           */
            public void receiveErrorValidCarNo(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for IsCardExist method
            * override this method for handling normal response from IsCardExist operation
            */
           public void receiveResultIsCardExist(
                    com.voyageone.batch.synship.service.ems.B2COrderServiceStub.IsCardExistResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from IsCardExist operation
           */
            public void receiveErrorIsCardExist(Exception e) {
            }
                


    }
    