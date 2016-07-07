package com.voyageone.web2.vms;

/**
 * vendor urls
 * Created by vantis on 16-7-6.
 */
public interface VendorUrlConstants {

    interface ORDER {
        interface ORDER_INFO {
            String ROOT = "/order/order_info";
            String INIT = "/init";
        }
    }

    interface FEED {
        interface FEED_FILE_IMPORT {
            String ROOT = "/vms/feed/file_upload";
            String UPLOAD_FEED_FILE = "/uploadFeedFile";
        }
    }
}
