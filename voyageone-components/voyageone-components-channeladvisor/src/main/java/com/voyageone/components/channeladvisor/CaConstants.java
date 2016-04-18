package com.voyageone.components.channeladvisor;

/**
 * Created by sn3 on 2015-08-14.
 */
public class CaConstants {

    //common
    public final static String NAMESPACE= "ca_url_namespace";
    public final static String ACCOUNTID = "ca_account_id";
    public final static String DEVELOPERKEY = "ca_developer_key";
    public final static String PASSWORD = "ca_password";

    //inventory
    public final static String URL = "ca_url_api_inventory";
    public final static String POSTACTION = "ca_inventory_action";
    public final static String URL_ORDER = "ca_url_api_order";

    //order
    public static final class OrderList {
        //public final static String ACTION = "ca_order_action";
        public final static String URL = "ca_url_api_order";
        public final static String DETAILLEVEL = "ca_orderList_detlLevel";
        public final static String SHIPPING_STATUS_FILTER = "ca_orderList_shippingStatusFilter";
        public final static String PAGE_SIZE = "ca_orderList_pageSize";
        public final static String TIME_INTERVAL =  "ca_orderList_timeInterval";
    }

}
