package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsSynLastReceiveTimeMQMessageBody;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by james on 2017/7/12.
 */
public class CmsSynLastReceiveTimeMQJob extends TBaseMQCmsService<CmsSynLastReceiveTimeMQMessageBody>{

    @Autowired
    private ProductService productService;

    @Autowired
    private FeedInfoService feedInfoService;

    @Override
    public void onStartup(CmsSynLastReceiveTimeMQMessageBody messageBody) throws Exception {

        String lastReceivedOn = DateTimeUtil.format(new Date(messageBody.getReceiveTime()),null);

        JongoUpdate jongoUpdate = new JongoUpdate();
        jongoUpdate.setQuery("{\"common.fields.code\":{$in:[#]}}");
        jongoUpdate.setQueryParameters(messageBody.getCodes());
        jongoUpdate.setUpdate("{$set:{\"common.fields.lastReceivedOn\":#}}");
        jongoUpdate.setUpdateParameters(lastReceivedOn);
        productService.updateMulti(jongoUpdate,"001");

        jongoUpdate = new JongoUpdate();
        jongoUpdate.setQuery("{\"code\":{$in:[#]}}");
        jongoUpdate.setQueryParameters(messageBody.getCodes());
        jongoUpdate.setUpdate("{$set:{\"lastReceivedOn\":#}}");
        jongoUpdate.setUpdateParameters(lastReceivedOn);
        feedInfoService.updateMulti(jongoUpdate, "001");
    }
}
