package com.voyageone.common.components.sears;

/**
 * Created by jack.zhao on 2015-12-01.
 */
public class SerasConstants {

    //Status
    public static final class Status {
        //Status values: PickedUp, Shipped, Delivered, Returned and Cancelled.
        public final static String PickedUp = "PickedUp";
        public final static String Shipped = "Shipped";
        public final static String Delivered = "Delivered";
        public final static String Returned = "Returned";
        public final static String Cancelled =  "Cancelled";
    }

    //Notes
    public static final class Notes {
        //A note or text message that indicates the reason for certain status change.
        public final static String PickedUp = "Warehouse PickedUp";
        public final static String Shipped = "Warehouse Shipped";
        public final static String Delivered = "Customer Delivered";
        public final static String Returned = "Customer Returned";
        public final static String Cancelled =  "Customer Cancelled";
    }

}
