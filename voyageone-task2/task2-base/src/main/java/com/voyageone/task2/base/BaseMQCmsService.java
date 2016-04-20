package com.voyageone.task2.base;

import com.voyageone.common.components.issueLog.enums.SubSystem;

/**
 * @author aooer 2016/4/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class BaseMQCmsService extends BaseMQAnnoService {
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsGetSuperFeedJob";
    }

}
