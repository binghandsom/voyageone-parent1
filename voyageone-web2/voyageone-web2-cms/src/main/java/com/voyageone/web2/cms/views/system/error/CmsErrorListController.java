package com.voyageone.web2.cms.views.system.error;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author aooer 2016/1/21.
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping(
        value = CmsUrlConstants.SYSTEM.ERROR.ROOT,
        method = RequestMethod.POST
)
public class CmsErrorListController extends CmsController {

    @Autowired
    private CmsErrorListService cmsSystemErrorConService;

    @RequestMapping(CmsUrlConstants.SYSTEM.ERROR.INIT)
    public AjaxResponse init() throws Exception {
        return success(cmsSystemErrorConService.getMasterData(getUser(), getLang()));
    }

    @RequestMapping(CmsUrlConstants.SYSTEM.ERROR.SEARCH)
    public AjaxResponse search(@RequestBody Map params) {
        return success(cmsSystemErrorConService.search(params));
    }

    @RequestMapping(CmsUrlConstants.SYSTEM.ERROR.UPDATE_FINISH_STATUS)
    public AjaxResponse updateStatus(@RequestBody Map params) {
        return success(cmsSystemErrorConService.updateStatus(params, getUser()));
    }
}
