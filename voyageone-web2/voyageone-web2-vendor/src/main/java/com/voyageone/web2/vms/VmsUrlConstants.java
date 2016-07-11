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
        }
    }
}
