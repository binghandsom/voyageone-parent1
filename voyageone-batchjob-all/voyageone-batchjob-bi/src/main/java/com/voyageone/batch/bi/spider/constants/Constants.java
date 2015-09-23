package com.voyageone.batch.bi.spider.constants;

public interface Constants {
    int ENABLE = 1;
    int DISABLE = 0;

    int PAGE_SIZE = 300;
    int PAGE_SIZE_ONSALE_API = 500;
    int INITIAL_DAY_SIZE = 600;
    int DAILY_DAY_SIZE = 7;
    int INITIAL_WEEK_SIZE = 84;
    int DAILY_WEEK_SIZE = 1;
    int INITIAL_MONTH_SIZE = 20;
    int DAILY_MONTH_SIZE = 1;

    String FORMAT_JSON_DATE = "yyyy-MM-dd";

    String PHANTOMJS_PATH = "\\voyageone-batchjob-all\\voyageone-batchjob-bi\\phantomjs-1.9.7-windows\\phantomjs.exe";

    String FORMAT_IID = "";

    int DRIVER_THREAD_COUNT = 10;

    int PRODUCT_THREAD_COUNT = 4;

}
