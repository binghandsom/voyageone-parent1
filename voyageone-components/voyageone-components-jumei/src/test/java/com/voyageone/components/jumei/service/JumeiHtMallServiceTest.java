package com.voyageone.components.jumei.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jumei.JumeiHtMallService;
import com.voyageone.components.jumei.bean.HtMallSkuPriceUpdateInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2016/11/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class JumeiHtMallServiceTest {

    @Autowired
    private JumeiHtMallService jumeiHtMallService;

    @Test
    public void testUpdateMallSkuPriceBatch() throws Exception {

        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("http://openapi.ext.jumei.com/");
        shopBean.setAppKey("131");
        shopBean.setSessionKey("");
        shopBean.setAppSecret("");

        List<HtMallSkuPriceUpdateInfo> updateData = new ArrayList<>();
        HtMallSkuPriceUpdateInfo skuPriceUpdateInfo = null;
        for (int i = 0; i < 21; i++) {
            skuPriceUpdateInfo = new HtMallSkuPriceUpdateInfo();
            updateData.add(skuPriceUpdateInfo);
            int index = i + 1;
            skuPriceUpdateInfo.setJumei_sku_no("skuNo"+index);
            skuPriceUpdateInfo.setMall_price(Double.parseDouble("905."+index));
            skuPriceUpdateInfo.setMarket_price(Double.parseDouble("990."+index));
        }

        StringBuffer failCause = new StringBuffer("");

        boolean result = jumeiHtMallService.updateMallSkuPriceBatch(shopBean, updateData, failCause);
        if (result) {
            System.out.println("批量更新聚美商城SKU价格成功！");
        } else {
            System.out.println(String.format("批量更新聚美商城SKU价格失败！[errMsg:%s]", failCause.toString()));
        }
    }
}
