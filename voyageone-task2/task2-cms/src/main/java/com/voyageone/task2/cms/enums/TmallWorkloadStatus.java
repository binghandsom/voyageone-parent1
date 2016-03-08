package com.voyageone.task2.cms.enums;

/**
 * Created by Leo on 2015/5/27.
 */
public class TmallWorkloadStatus extends PlatformWorkloadStatus {
        public final static int ADD_INIT = ADD_START;
        public final static int ADD_SEARCH_PRODUCT = ADD_START + 1;
        public final static int ADD_CHECK_PRODUCT_STATUS = ADD_START + 2;
        public final static int ADD_UPLOAD_PRODUCT = ADD_START + 3;
        public final static int ADD_WAIT_CHECK = ADD_START + 4;
        public final static int ADD_WAIT_PRODUCT_PIC = ADD_START + 5;
        public final static int ADD_PRODUCT_PIC_UPLOADED = ADD_START + 6;
        public final static int ADD_UPLOAD_ITEM = ADD_START + 7;
        public final static int ADD_WAIT_ITEM_PIC = ADD_START + 8;
        public final static int ADD_ITEM_PIC_UPLOADED = ADD_START + 9;

        public final static int UPDATE_INIT = UPDATE_START;
        public final static int UPDATE_SEARCH_ITEM = UPDATE_START + 1;
        public final static int UPDATE_ITEM = UPDATE_START + 2;
        public final static int UPDATE_WAIT_ITEM_PIC = UPDATE_START + 3;
        public final static int UPDATE_ITEM_PIC_UPLOADED = UPDATE_START + 4;

        public TmallWorkloadStatus(PlatformWorkloadStatus platformWorkloadStatus) {
                this(platformWorkloadStatus.getValue());
        }

        public TmallWorkloadStatus(int value) {
                super(value);
        }

        @Override
        public String toString() {
                switch (value)
                {
                        case ADD_INIT:
                                return "ADD_INIT";
                        case ADD_SEARCH_PRODUCT:
                                return "ADD_SEARCH_PRODUCT";
                        case ADD_CHECK_PRODUCT_STATUS:
                                return "ADD_CHECK_PRODUCT_STATUS";
                        case ADD_UPLOAD_PRODUCT:
                                return "ADD_UPLOAD_PRODUCT";
                        case ADD_WAIT_CHECK:
                                return "ADD_WAIT_CHECK";
                        case ADD_WAIT_PRODUCT_PIC:
                                return "ADD_WAIT_PRODUCT_PIC";
                        case ADD_PRODUCT_PIC_UPLOADED:
                                return "ADD_PRODUCT_PIC_UPLOADED";
                        case ADD_UPLOAD_ITEM:
                                return "ADD_UPLOAD_ITEM";
                        case ADD_WAIT_ITEM_PIC:
                                return "ADD_WAIT_ITEM_PIC";
                        case ADD_ITEM_PIC_UPLOADED:
                                return "ADD_ITEM_PIC_UPLOADED";
                        case UPDATE_INIT:
                                return "UPDATE_INIT";
                        case UPDATE_SEARCH_ITEM:
                                return "UPDATE_SEARCH_ITEM";
                        case UPDATE_ITEM:
                                return "UPDATE_ITEM";
                        case UPDATE_WAIT_ITEM_PIC:
                                return "UPDATE_WAIT_ITEM_PIC";
                        case UPDATE_ITEM_PIC_UPLOADED:
                                return "UPDATE_ITEM_PIC_UPLOADED";
                        default:
                                return super.toString();
                }
        }

}
