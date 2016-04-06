package com.voyageone.web2.cms.views.jm;
import com.voyageone.service.impl.jumei.CmsBtJmPromotionImportTaskService;
import com.voyageone.service.impl.jumei.CmsBtJmPromotionProductService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
@RestController
@RequestMapping(
        value = CmsUrlConstants.CmsBtJmPromotionImportTask.LIST.INDEX.ROOT,
        method = RequestMethod.POST
)
public class CmsBtJmPromotionImportTaskController extends CmsController {
    @Autowired
    private CmsBtJmPromotionImportTaskService service;

    @RequestMapping(CmsUrlConstants.CmsBtJmPromotionImportTask.LIST.INDEX.GET_BY_PROMOTIONID)
    public AjaxResponse getByPromotionId(@RequestBody int promotionId) {
        return success(service.getByPromotionId(promotionId));
    }
}
