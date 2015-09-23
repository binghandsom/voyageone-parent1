package com.voyageone.batch.ims.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.ims.service.beat.ImsBeatPicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 价格披露启动类
 *
 * Created by Jonas on 7/24/15.
 *
 * Version: 0.2.1
 */
@Component("ImsBeatPicJob")
public class ImsBeatPicJob extends BaseTaskJob {
    @Autowired
    private ImsBeatPicService imsBeatPicService;

    @Override
    protected BaseTaskService getTaskService() {
        return imsBeatPicService;
    }
}
