package com.voyageone.web2.cms.views.jm;
import com.voyageone.service.impl.jumei.CmsBtJmPromotionExportTaskService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = CmsUrlConstants.CmsBtJmPromotionExportTask.LIST.INDEX.ROOT,
        method = RequestMethod.POST
)
public class CmsBtJmPromotionExportTaskController extends CmsController {
    @Autowired
    private CmsBtJmPromotionExportTaskService service;

    @RequestMapping(CmsUrlConstants.CmsBtJmPromotionExportTask.LIST.INDEX.GET_BY_PROMOTIONID)
    public AjaxResponse getByPromotionId(@RequestBody int promotionId) {
        return success(service.getByPromotionId(promotionId));
    }
}
