package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsUrlConstants.PROMOTION.TASK.BEAT;
import com.voyageone.web2.cms.bean.beat.TaskBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jonasvlag on 16/3/1.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping(value = BEAT.ROOT, method = RequestMethod.POST)
public class CmsTaskPictureController extends BaseController {

    @Autowired
    private CmsTaskPictureService taskPictureService;

    @RequestMapping(BEAT.CREATE)
    public AjaxResponse create(@RequestBody TaskBean taskBean) {
        TaskBean newBean = taskPictureService.create(taskBean, getUser());
        return success(newBean);
    }
}
