package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsProductFreeTagsUpdateMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsBtProductUpdateTagsMQMessageBody;
import com.voyageone.service.impl.cms.vomqjobservice.CmsProductFreeTagsUpdateService;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dell on 2017/7/18.
 *
 */
@Service
@RabbitListener()
public class CmsBtProductUpdateTagsMQJob extends TBaseMQCmsSubService<CmsProductFreeTagsUpdateMQMessageBody> {
    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    CmsProductFreeTagsUpdateService cmsProductFreeTagsUpdateService;

    @Override
    public void onStartup(CmsProductFreeTagsUpdateMQMessageBody messageBody) throws Exception {
        if (messageBody != null){
            List<String> productCodeList =  cmsProductFreeTagsUpdateService.setProductFreeTags(messageBody);
            super.count = productCodeList.size();
        }
    }
}
