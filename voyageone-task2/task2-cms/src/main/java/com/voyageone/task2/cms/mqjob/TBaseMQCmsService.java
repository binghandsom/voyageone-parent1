package com.voyageone.task2.cms.mqjob;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
import com.voyageone.components.rabbitmq.service.IMQJobLog;
import com.voyageone.service.impl.cms.CmsBtOperationLogService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.TBaseMQAnnoService;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
/**
 * @author aooer 2016/4/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class TBaseMQCmsService<TMQMessageBody  extends IMQMessageBody> extends TBaseMQAnnoService<TMQMessageBody> {
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return getClass().getSimpleName();
    }

    @Autowired
    CmsBtOperationLogService cmsBtOperationLogService;

    @Override
    public void startup(TMQMessageBody messageBody) throws Exception {
        try {
            onStartup(messageBody);
        } catch (Exception ex) {
            //记异常日志
            // votodo
            cmsBtOperationLogService.log(messageBody, ex);
            throw ex;
        }
    }
    //写日志
    public void cmsLog(TMQMessageBody messageBody, String msg) {
        cmsBtOperationLogService.log(messageBody, msg);
    }
}
