package com.voyageone.batch.synship.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.synship.service.SynShipValidIdCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 从 Synship CloudClient 迁移的身份证验证任务
 *
 * Created by Jonas on 9/22/15.
 */
@Component("synShipValidIdCardTask")
public class SynShipValidIdCardJob extends BaseTaskJob {

    @Autowired
    private SynShipValidIdCardService service;

    @Override
    protected BaseTaskService getTaskService() {
        return service;
    }
}
