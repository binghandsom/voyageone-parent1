package com.voyageone.web2.vms;

/**
 * vendor urls
 * Created by vantis on 16-7-6.
 */
public interface VmsUrlConstants {

    interface ORDER {
        interface ORDER_INFO {
            String ROOT = "/vms/order/order_info/";
            String INIT = "init";
            String SEARCH = "search";
            String CANCEL_ORDER = "cancelOrder";
            String CANCEL_SKU = "cancelSku";
            String DOWNLOAD_PICKING_LIST = "downloadPickingList";
        }
    }

    interface SHIPMENT {
        interface SHIPMENT_INFO {
            String ROOT = "/vms/shipment/shipment_info/";
            String INIT = "init";
            String SEARCH = "search";
        }
        interface ShipmentDetail {
            String ROOT = "/vms/shipment/shipment_detail";
            String INIT = "init";
            String SCAN = "scan";
            String SHIP = "ship";
            String GET_INFO = "get_info";
        }
    }

    interface FEED {
        interface FEED_FILE_IMPORT {
            String ROOT = "/vms/feed/file_upload/";
            String DOWNLOAD_SAMPLE_FEED_FILE = "downSampleFeedFile";
            String UPLOAD_FEED_FILE = "uploadFeedFile";
        }

        interface FEED_IMPORT_RESULT {
            String ROOT = "/vms/feed/feed_import_result/";
            String INIT = "init";
            String SEARCH = "search";
            String DOWN_FEED_ERROR_FILE = "downloadFeedErrorFile";
        }

        interface FEED_SEARCH {
            String ROOT = "/vms/feed/feed_info_search/";
            String SEARCH = "search";
        }
    }

    interface REPORT {
        interface FINANCIAL_REPORT {
            String ROOT = "/vms/report/financial_report";
            String INIT = "init";
            String SEARCH = "search";
            String CONFIRM = "confirm";
            String DOWNLOAD_FINANCIAL_REPORT = "downloadFinancialReport";
        }
    }

    interface POPUP {
        interface SHIPMENT {
            String ROOT = "/vms/popup/shipment";
            String INIT = "init";
            String SUBMIT = "submit";
            String CREATE = "create";
            String GET = "get";
            String CONFIRM = "confirm";
            String END = "end";
            String GET_INFO = "get_info";
        }

        interface SCAN {
            String ROOT = "vms/popup/scan";
            String INIT = "init";
            String SCAN_BARCODE = "scanBarcode";
        }
    }
}
