package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.common.configs.MQConfigInitTestUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.SaveChannelCategoryMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Edward
 * @version 2.0.0, 2017/2/9
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsSaveChannelCategoryMQJobTest {

    @Autowired
    CmsSaveChannelCategoryMQJob cmsSaveChannelCategoryMQJob;

    @Test
    public void testOnStartup() throws Exception {

        String json = "{\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"james\",\"channelId\":\"001\",\"params\":{\"sellerCats\":[{\"cId\":\"1140387718\",\"cIds\":[\"1140387718\"],\"cName\":\"服饰配件\",\"cNames\":[\"服饰配件\"]},{\"cId\":\"1264026237\",\"cIds\":[\"1140385125\",\"1264026237\"],\"cName\":\"男鞋>跑步鞋\",\"cNames\":[\"男鞋\",\"跑步鞋\"]},{\"cId\":\"1264026238\",\"cIds\":[\"1140385125\",\"1264026238\"],\"cName\":\"男鞋>休闲鞋/板鞋\",\"cNames\":[\"男鞋\",\"休闲鞋/板鞋\"]}],\"cartId\":23,\"_orgDispList\":[],\"productIds\":[\"68220-gem\"],\"isSelAll\":0,\"searchInfo\":{\"compareType\":null,\"brand\":null,\"tags\":[],\"priceChgFlg\":\"\",\"priceDiffFlg\":\"\",\"tagTypeSelectValue\":\"0\",\"promotionList\":[],\"catgoryList\":[],\"cidValue\":[],\"promotionTagType\":1,\"freeTagType\":1,\"supplierType\":1,\"brandSelType\":1,\"mCatPathType\":1,\"fCatPathType\":1,\"shopCatType\":1,\"pCatPathType\":1,\"productSelType\":\"1\",\"sizeSelType\":\"1\",\"salesType\":\"All\",\"custGroupType\":\"1\",\"custAttrMap\":[{\"inputVal\":\"\",\"inputOpts\":\"\",\"inputOptsKey\":\"\"}],\"productStatus\":[],\"platformStatus\":[],\"pRealStatus\":[],\"shopCatStatus\":0,\"pCatStatus\":0},\"channelId\":\"001\",\"userName\":\"james\"},\"subBeanName\":\"001\"}";
        SaveChannelCategoryMQMessageBody body = JacksonUtil.json2Bean(json, SaveChannelCategoryMQMessageBody.class);
        cmsSaveChannelCategoryMQJob.onStartup(body);
    }
}