package com.voyageone.components.eexpress;

/**
 * Created by sn3 on 2015-08-14.
 */
public class EtkConstants {

    //所在地
    public final static String Location = "香港";

    //结果
    public final static class Result {
        public final static String T ="T";
        public final static String F ="F";
    }

    //状态Code
    public final static class StatusCode {
        public final static String Created ="C";
        public final static String Exported ="E";
        public final static String Inscanned ="I";
        public final static String Clearance ="R";
        public final static String Submitted ="S";
        public final static String Despatch ="B";
        public final static String Warehouse_reject ="";
        public final static String HK_reject ="";
        public final static String Cancel ="D";
        public final static String Returned ="";
        public final static String Delivered ="";
    }

    //状态描述
    public final static class StatusDescription {
        public final static String Created ="Shipment Created";
        public final static String Exported ="shipment exported by agent";
        public final static String Inscanned ="Shipment inscanned in warehouse";
        public final static String Clearance ="HK Customs clearance";
        public final static String Submitted ="Shipment data submitted to HKPOST";
        public final static String Despatch ="Shipment despatch by hkpost,Shipment accepted by hkpost,Arrived and is being processed";
        public final static String Warehouse_reject ="";
        public final static String HK_reject ="";
        public final static String Cancel ="Shipment Cancelled by Customer";
        public final static String Returned ="";
        public final static String Delivered ="Delivered";
    }

}
