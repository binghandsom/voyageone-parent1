package com.voyageone.batch.synship.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.synship.service.SendSMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Fred on 2015/8/28.
 */
@Component("sendSMSTask")
public class SendSMSJob extends BaseTaskJob {

    @Autowired
    private SendSMSService sendSMSService;

    @Override
    protected BaseTaskService getTaskService() {
        return sendSMSService;
    }
}
