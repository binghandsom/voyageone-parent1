package com.voyageone.wms;

public final class WmsUrlConstants {

    // 仓储管理系统--默认页URL
    public static final String URL_WMS_DEFAULT_INDEX = "/wms/default/index";

    /**
     * Transfer 系列页面
     */
    public static final class TransferUrls {
        /**
         * List 页面。搜索指定的 Transfer
         */
        public static final String SEARCH = "/wms/transfer/list/search";

        public static final String INIT = "/wms/transfer/list/init";

        public static final String DELETE = "/wms/transfer/list/delete";

        public static final String DOWNLOAD = "/wms/transfer/list/download";

        public static final String GET = "/wms/transfer/edit/getTransfer";

        public static final String SAVE = "/wms/transfer/edit/saveTransfer";

        public static final String SUBMIT = "/wms/transfer/edit/submitTransfer";

        public static final String COMPARE = "/wms/transfer/edit/compareTransfer";

        public static final String DOWNLOAD_CLIENT_SHIPMENT = "/wms/transfer/edit/downloadClientShipment";

        public static final String DOWNLOAD_TRANSFER_COMPARE = "/wms/transfer/edit/downloadTransferCompare";

        public static final class Configs {
            public static final String ALL = "/wms/transfer/edit/getAllConfigs";
        }

        public static final class Package {
            public static final String GET = "/wms/transfer/edit/getPackage";
            public static final String CREATE = "/wms/transfer/edit/createPackage";
            public static final String DELETE = "/wms/transfer/edit/deletePackage";
            public static final String CLOSE = "/wms/transfer/edit/closePackage";
            public static final String REOPEN = "/wms/transfer/edit/reOpenPackage";
        }

        public static final class Item {
            public static final String ADD = "/wms/transfer/edit/addItem";
            public static final String COMPARE = "/wms/transfer/edit/compare";
            public static final String SELECT = "/wms/transfer/edit/getPackageItems";
            public static final String GETSKU = "/wms/transfer/edit/getSku";
        }
    }

    public static final class LocationUrls {
        /**
         * Location List 页面下的 Action 地址
         */
        public static final class List {
            public static final String INIT = "/wms/location/list/init";
            public static final String ADD = "/wms/location/list/addLocation";
            public static final String SEARCH = "/wms/location/list/searchLocation";
            public static final String DELETE = "/wms/location/list/deleteLocation";
        }

        public static final class Bind {
            public static final String INIT = "/wms/location/bind/init";
            public static final String SEARCH = "/wms/location/bind/searchItemLocation";
            public static final String SEARCH_BY_SKU = "/wms/location/bind/searchItemLocationBySku";
            public static final String SEARCH_BY_LOCATION_ID = "/wms/location/bind/searchItemLocationByLocationId";
            public static final String ADD = "/wms/location/bind/addItemLocation";
            public static final String DELETE = "/wms/location/bind/deleteItemLocation";
        }
    }

    /**
     * 捡货 系列页面
     */
    public static final class PickupUrls {

        /**
         * List 页面。初始化
         */
        public static final String INIT = "/wms/pickup/list/init";

        /**
         * List 页面。搜索指定条件的 Order
         */
        public static final String SEARCH = "/wms/pickup/list/search";

        /**
         * List 页面。扫描指定条件的 Order
         */
        public static final String SCAN = "/wms/pickup/list/scan";

        /**
         * List 页面。下载
         */
        public static final String DOWNLOAD = "/wms/pickup/list/download";

        /**
         * 已拣货报告。下载
         */
        public static final String REPORTDOWNLOAD = "/wms/pickup/report/download";

    }

    /**
     * 收货 系列页面
     */
    public static final class ReceiveUrls {

        /**
         * List 页面。初始化
         */
        public static final String INIT = "/wms/receive/list/init";

        /**
         * List 页面。搜索指定条件的 Order
         */
        public static final String SEARCH = "/wms/receive/list/search";

        /**
         * List 页面。扫描指定条件的 Order
         */
        public static final String SCAN = "/wms/receive/list/scan";

        /**
         * 已拣货报告。下载
         */
        public static final String REPORTDOWNLOAD = "/wms/receive/report/download";

    }

    /**
     * Upc 管理页面需要的操作地址
     */
    public static final class UpcUrls {
        public static final String GET_PRODUCT = "/wms/upc/manage/getProduct";
        public static final String SAVE_PRODUCT = "/wms/upc/manage/saveProduct";
        public static final String GET_ALL_SIZE = "/wms/upc/manage/getAllSize";
        public static final String SAVE_ITEM_DETAIL = "/wms/upc/manage/saveItemDetail";
        public static final String GET_ALL_PRODUCT_TYPE = "/wms/upc/manage/getAllProductTypes";
    }

    //reservation管理
    public static final class ReservationUrls {
        /**
         * ReservationLog 页面下的 Action 地址
         */
        public static final class ReservationLog {
            public static final String SEARCH = "/wms/reservation/reservationLog/doReservationLogSearch";
        }

        /**
         * Reservation List 页面下的 Action 地址
         */
        public static final class ReservationList {
            public static final String SEARCH = "/wms/reservation/list/doReservationListSearch";
            public static final String INIT = "/wms/reservation/list/doInit";
        }

        /**
         * PopInventory 页面下的 Action 地址
         */
        public static final class PopInventory {
            public static final String INIT = "/wms/reservation/popInventory/doPopInventoryInit";
            public static final String SEARCH = "/wms/reservation/popInventory/doPopInventorySearch";
            public static final String RESET = "/wms/reservation/popInventory/doPopInventoryReset";
        }

        /**
         * PopSkuHisList 页面下的 Action 地址
         */
        public static final class PopSkuHisList {
            public static final String INIT = "/wms/reservation/popSkuHisList/doPopSkuHisInit";
            public static final String SEARCH = "/wms/reservation/popSkuHisList/doPopSkuHisSearch";
        }

        /**
         * PopChangeReservation 页面下的 Action 地址
         */
        public static final class PopChangeReservation {
            public static final String INIT = "/wms/reservation/popChangeReservation/doInit";
            public static final String CHANGE = "/wms/reservation/popChangeReservation/doChange";
        }

    }

    //订单return管理
    public static final class ReturnUrls {

        /**
         * Return List 页面下的 Action 地址
         */
        public static final class ReturnList {
            public static final String INIT = "/wms/return/list/doInit";
            public static final String SEARCH = "/wms/return/list/doReturnListSearch";
            public static final String CHANGESTATUS = "/wms/return/list/doChangeStatus";
            public static final String DOWNLOAD = "/wms/return/list/doReturnListDownload";
        }

        /**
         * newSession 页面下的 Action 地址
         */
        public static final class newSession {
            public static final String INIT = "/wms/return/newSession/doNewSessionInit";
            public static final String GETSESSIONINFO = "/wms/return/newSession/doGetSessionInfo";
            public static final String GETORDERINFOBYORDNO = "/wms/return/newSession/getOrderInfoByOrdNo";
            public static final String INSERTRETURNINFO = "/wms/return/newSession/insertReturnInfo";
            public static final String CREATERETURNSESSION = "/wms/return/newSession/createReturnSession";
            public static final String CLOSERETURNSESSION = "/wms/return/newSession/closeReturnSession";
            public static final String GETRECEIVEDFROMITEMBYSTOREID = "/wms/return/newSession/doGetReceivedFromItemByStoreId";
            public static final String GETRETURNTYPE = "/wms/return/newSession/doGetReturnType";
        }

        /**
         * ItemEdit 页面下的 Action 地址
         */
        public static final class ItemEdit {
            public static final String INIT = "/wms/return/itemEdit/doItemEditInit";
            public static final String SAVE = "/wms/return/itemEdit/doItemEditSave";
            public static final String REMOVE = "/wms/return/itemEdit/doReturnInfoRemove";
        }

        /**
         * SessionList 页面下的 Action 地址
         */
        public static final class SessionList {
            public static final String INIT = "/wms/return/sessionList/doSessionListInit";
            public static final String SEARCH = "/wms/return/sessionList/doSessionListSearch";
        }

        /**
         * SessionDetail 页面下的 Action 地址
         */
        public static final class SessionDetail {
            public static final String INIT = "/wms/return/sessionDetail/doSessionDetailInit";
        }

        public static final class PopChange {
            public static final String CHANGE = "/wms/return/popchange/doChange";
        }
    }

    //盘点管理
    public static final class StockTakeUrls {
        /**
         * Session List 页面下的 Action 地址
         */
        public static final class SessionList {
            public static final String INIT = "/wms/stockTake/list/doSessionListInit";
            public static final String SEARCH = "/wms/stockTake/list/doSessionListSearch";
            public static final String NEWSESSION = "/wms/stockTake/list/doNewSession";
            public static final String DELETE = "/wms/stockTake/list/doSessionDelete";
            public static final String CLOSE = "/wms/stockTake/list/doSessionStock";
        }

        /**
         * Section List 页面下的 Action 地址
         */
        public static final class SectionList {
            public static final String INIT = "/wms/stockTake/sectionList/doSectionListInit";
            public static final String NEWSECTION = "/wms/stockTake/sectionList/doNewSection";
            public static final String DELETESECTION = "/wms/stockTake/sectionList/doDeleteSection";
        }

        /**
         * Inventory 页面下的 Action 地址
         */
        public static final class Inventory {
            public static final String INIT = "/wms/stockTake/inventory/doInventoryInit";
            public static final String SCAN = "/wms/stockTake/inventory/doUpcScan";
            public static final String GETSKU = "/wms/stockTake/inventory/getSku";
            public static final String CHECKSECTIONSTATUS = "/wms/stockTake/inventory/doCheckSectionStatus";
        }

        /**
         * SectionDetail 页面下的 Action 地址
         */
        public static final class SectionDetail {
            public static final String INIT = "/wms/stockTake/sectionDetail/doSectionDetailInit";
            public static final String CLOSESECTION = "/wms/stockTake/sectionDetail/doCloseSection";
            public static final String RESCAN = "/wms/stockTake/sectionDetail/doReScan";
        }

        /**
         * Compare 页面下的 Action 地址
         */
        public static final class Compare {
            public static final String INIT = "/wms/stockTake/compare/doCompareInit";
            public static final String CHECKBOXVALUECHANGE = "/wms/stockTake/compare/doCheckBoxValueChange";
            public static final String CHECKSESSIONSTATUS = "/wms/stockTake/compare/doCheckSessionStatus";
            public static final String SESSIONDONE = "/wms/stockTake/compare/doSessionDone";
            public static final String COMPRESDOWNLOAD = "/wms/stockTake/compare/compResdownload";
        }

    }

    /**
     * backOrder 超卖管理需要的操作地址
     */
    public static final class BackOrderUrls {
        public static final String SEARCH = "/wms/backOrder/list/doGetBackOrderInfoList";
        public static final String DELETE = "/wms/backOrder/list/doDelBackOrderInfo";
        public static final String ADD = "/wms/backOrder/list/doAddBackOrderInfo";
        public static final String POPINIT = "/wms/backOrder/list/doPopInit";
        public static final String LISTINIT = "/wms/backOrder/list/doListInit";
    }

    /**
     * report 报告管理
     */
    public static final class ReportUrls {
        public static final String INIT = "/wms/report/list/doInit";
        public static final String DOWNLOAD = "/wms/report/list/download";
    }

}
