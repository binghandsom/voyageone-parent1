package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsPlatformMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 主数据到平台的默认mapping数据做成
 *
 * @author james.li on 2015/12/10
 * @since 2.0.0
 */
@Component("CmsPlatformMappingJob")
public class CmsPlatformMappingJob extends BaseTaskJob {

    @Autowired
    private CmsPlatformMappingService cmsPlatformMappingService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsPlatformMappingService;
    }
}
