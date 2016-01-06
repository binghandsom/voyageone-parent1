package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.CmsImagePostScene7Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 商品图片上传到Scene7
 *
 * @author james
 */
@Component("cmsImagePostScene7Job")
public class CmsImagePostScene7Job extends BaseTaskJob {

    @Autowired
    CmsImagePostScene7Service cmsImagePostScene7Service;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsImagePostScene7Service;
    }

}
