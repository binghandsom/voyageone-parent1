package com.voyageone.batch.bi.spider.service.jumei;

import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiDealBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiProductBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiSkuBean;

import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * Created by Kylin on 2015/7/16.
 */
public interface JumeiUpdateService {

    void updateProduct(WebDriver driver, String strPID, JumeiProductBean jumeiProductBean, List<JumeiSkuBean> skuLst) throws Exception;

    void updateDeal(WebDriver driver, String strPID, JumeiDealBean jumeiDealBean, List<JumeiSkuBean> skuLst) throws Exception;

    void checkProductImageWait(WebDriver driver, String strPID, JumeiProductBean jumeiProductBean) throws Exception;
    void checkProductImageReview(WebDriver driver, String strPID, JumeiProductBean jumeiProductBean) throws Exception;

}
