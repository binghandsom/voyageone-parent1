package com.voyageone.batch.bi.spider.service.channel;

import org.openqa.selenium.WebDriver;

/**
 * Created by Kylin on 2015/7/6.
 */
public interface DealJDdataService {
    void doJDstoreDataProcess(WebDriver driver, int icircle) throws Exception;

    void doJDproductDataProcess(int icircle) throws Exception;

    void  doJDproductOnsaleReview() throws Exception;
}
