package com.voyageone.batch.ims.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.ims.service.ImsJumeiSendProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * Created by simin on 2016/1/26.
 */
@Component("ImsJumeiSendProductJob")
public class ImsJumeiSendProductJob extends BaseTaskJob{
    @Autowired
    ImsJumeiSendProductService imsJumeiSendProductService;
    protected BaseTaskService getTaskService(){
        return imsJumeiSendProductService;
    }
}

