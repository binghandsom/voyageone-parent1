package com.voyageone.task2.cms.mqjob;

import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.bean.cms.CmsMtCategoryTreeAllBean;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsFeedSetCategoryMQMessageBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by james on 2017/5/17.
 */
@Service
@VOSubRabbitListener
public class CmsFeedSetCategoryMQJob extends TBaseMQCmsSubService<CmsFeedSetCategoryMQMessageBody> {


    @Autowired
    private FeedInfoService feedInfoService;

    @Override
    public void onStartup(CmsFeedSetCategoryMQMessageBody messageBody) throws Exception {

        messageBody.getCodeList().forEach(code->{
            $info(String.format("code:%s  %s", code, messageBody.getMainCategoryInfo().getCatPath()));
            feedInfoService.updateMainCategory(messageBody.getChannelId(), code, messageBody.getMainCategoryInfo(), messageBody.getSender());
        });
    }
}
