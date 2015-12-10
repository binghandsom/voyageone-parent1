package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.PlatformMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 主数据到平台的默认mapping数据做成
 *
 * @author james.li on 2015/12/10
 * @since. 2.0.0
 */
@Component("CmsPlatformMappingJob")
public class CmsPlatformMappingJob extends BaseTaskJob {

    @Autowired
    private PlatformMappingService platformMappingService;

    @Override
    protected BaseTaskService getTaskService() {
        return platformMappingService;
    }
}
