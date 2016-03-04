package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsUrlConstants.PROMOTION.TASK.INDEX;
import com.voyageone.web2.cms.wsdl.models.CmsBtTaskModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by jonasvlag on 16/3/1.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping(value = INDEX.ROOT, method = RequestMethod.POST)
public class CmsTaskController extends BaseController {

    @Autowired
    private CmsTaskService taskService;

    /**
     * 这里暂时没实现分页, 临时使用全部
     */
    @RequestMapping(INDEX.PAGE)
    public AjaxResponse page() {
        List<CmsBtTaskModel> models = taskService.getAllTasks(getUser());
        return success(models);
    }
}
