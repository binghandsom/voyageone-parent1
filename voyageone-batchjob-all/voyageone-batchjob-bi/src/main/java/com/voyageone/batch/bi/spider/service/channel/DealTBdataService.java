package com.voyageone.batch.bi.spider.service.channel;

import org.openqa.selenium.WebDriver;

/**
 * Created by Kylin on 2015/7/6.
 */
public interface DealTBdataService {
    void doTBstoreDataProcess(WebDriver driver, int icircle) throws Exception;

    void doTBproductDataProcess(WebDriver driver, int icircle) throws Exception;

    void doTBproductOnsaleReview() throws Exception;
}
