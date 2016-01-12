package com.voyageone.batch.bi.etl.service;

import org.springframework.stereotype.Service;

@Service
public class VfOrdersPriceService extends BaseBiKettleService {

    private final static String TASK_NAME = "orders_price";
    private final static String KBJ_FILE_NAME = "orders_price";

    public VfOrdersPriceService() {
        super.taskName = TASK_NAME;
        super.jobXmlFileName = KBJ_FILE_NAME;
    }

}
