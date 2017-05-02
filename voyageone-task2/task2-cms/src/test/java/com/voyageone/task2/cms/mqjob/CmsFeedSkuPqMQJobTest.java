package com.voyageone.task2.cms.mqjob;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsFeedSkuPqMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by 123 on 2017/3/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsFeedSkuPqMQJobTest {

    @Autowired
    CmsFeedSkuPqMQJob cmsFeedSkuPqMQJob;

    @Test
    public void onStartup() throws Exception {
        String msg1 = "{\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"VmsThirdFileDataProcessingJob\",\"channelId\":\"033\",\"skuList\":[{\"priceCurrent\":null,\"priceMsrp\":null,\"priceNet\":null,\"priceClientRetail\":null,\"priceClientMsrp\":null,\"sku\":null,\"size\":null,\"barcode\":null,\"clientSku\":\"128333\",\"image\":null,\"qty\":10,\"relationshipType\":null,\"variationTheme\":null,\"weightOrg\":null,\"weightOrgUnit\":null,\"weightCalc\":null,\"isSale\":0,\"mainVid\":null,\"attribute\":{},\"errInfo\":null},{\"priceCurrent\":null,\"priceMsrp\":null,\"priceNet\":null,\"priceClientRetail\":null,\"priceClientMsrp\":null,\"sku\":null,\"size\":null,\"barcode\":null,\"clientSku\":\"1187206\",\"image\":null,\"qty\":11,\"relationshipType\":null,\"variationTheme\":null,\"weightOrg\":null,\"weightOrgUnit\":null,\"weightCalc\":null,\"isSale\":0,\"mainVid\":null,\"attribute\":{},\"errInfo\":null},{\"priceCurrent\":null,\"priceMsrp\":null,\"priceNet\":null,\"priceClientRetail\":null,\"priceClientMsrp\":null,\"sku\":null,\"size\":null,\"barcode\":null,\"clientSku\":\"1187205\",\"image\":null,\"qty\":12,\"relationshipType\":null,\"variationTheme\":null,\"weightOrg\":null,\"weightOrgUnit\":null,\"weightCalc\":null,\"isSale\":0,\"mainVid\":null,\"attribute\":{},\"errInfo\":null},{\"priceCurrent\":null,\"priceMsrp\":null,\"priceNet\":null,\"priceClientRetail\":null,\"priceClientMsrp\":null,\"sku\":null,\"size\":null,\"barcode\":null,\"clientSku\":\"1187204\",\"image\":null,\"qty\":13,\"relationshipType\":null,\"variationTheme\":null,\"weightOrg\":null,\"weightOrgUnit\":null,\"weightCalc\":null,\"isSale\":0,\"mainVid\":null,\"attribute\":{},\"errInfo\":null},{\"priceCurrent\":null,\"priceMsrp\":null,\"priceNet\":null,\"priceClientRetail\":null,\"priceClientMsrp\":null,\"sku\":null,\"size\":null,\"barcode\":null,\"clientSku\":\"1187203\",\"image\":null,\"qty\":14,\"relationshipType\":null,\"variationTheme\":null,\"weightOrg\":null,\"weightOrgUnit\":null,\"weightCalc\":null,\"isSale\":0,\"mainVid\":null,\"attribute\":{},\"errInfo\":null},{\"priceCurrent\":null,\"priceMsrp\":null,\"priceNet\":null,\"priceClientRetail\":null,\"priceClientMsrp\":null,\"sku\":null,\"size\":null,\"barcode\":null,\"clientSku\":\"1187202\",\"image\":null,\"qty\":15,\"relationshipType\":null,\"variationTheme\":null,\"weightOrg\":null,\"weightOrgUnit\":null,\"weightCalc\":null,\"isSale\":0,\"mainVid\":null,\"attribute\":{},\"errInfo\":null}],\"subBeanName\":\"033\"}";

        CmsFeedSkuPqMQMessageBody message = JacksonUtil.json2Bean(msg1, CmsFeedSkuPqMQMessageBody.class);
        cmsFeedSkuPqMQJob.onStartup(message);
    }


}
