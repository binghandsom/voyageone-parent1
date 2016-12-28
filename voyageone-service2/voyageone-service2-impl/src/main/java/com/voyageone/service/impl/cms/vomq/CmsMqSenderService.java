package com.voyageone.service.impl.cms.vomq;

import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.service.MqSenderService;
import com.voyageone.service.bean.cms.enumcms.OperationLog_Type;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CmsBtOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by dell on 2016/12/28.
 */
public class CmsMqSenderService extends BaseService {

    @Autowired
    MqSenderService mqSenderService;
    @Autowired
    CmsBtOperationLogService cmsBtOperationLogService;

    public void sendMessage(IMQMessageBody message) throws MQMessageRuleException {
        try {

            mqSenderService.sendMessage(message);
        } catch (MQMessageRuleException ex) {
            cmsBtOperationLogService.log(message, OperationLog_Type.parameterException, ex);
            throw ex;
        } catch (Exception ex) {
            cmsBtOperationLogService.log(message, ex);
            throw ex;
        }
    }
}
