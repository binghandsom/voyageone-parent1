package com.voyageone.service.impl.cms.vomqjobservice;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsBrandBlockMQMessageBody;
import com.voyageone.service.model.cms.CmsBtBrandBlockModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dell on 2017/1/4.
 */
@Service
public class CmsBrandBlockService extends BaseService {

    @Autowired
    CmsMqSenderService cmsMqSenderService;

    public void sendMessage(CmsBtBrandBlockModel data, boolean blocking,String sender) {
        CmsBrandBlockMQMessageBody mqMessageBody = new CmsBrandBlockMQMessageBody();
        mqMessageBody.setChannelId(data.getChannelId());
        mqMessageBody.setData(data);
        mqMessageBody.setBlocking(blocking);
        mqMessageBody.setSender(sender);

        cmsMqSenderService.sendMessage(mqMessageBody);
    }

}
