package com.voyageone.web2.cms.views.promotion.task;

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
 * Created by jeff.duan on 2016/02/29.
 */
@RestController
@RequestMapping(
        value = CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK.ROOT,
        method = RequestMethod.POST
)
public class CmsTaskIncrementStockController extends CmsController {

    @Autowired
//    private CmsTaskAddStockService cmsTaskAddStockService;




    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK.GET_TASK_LIST)
    public AjaxResponse getTaskList(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK.GET_PLATFORM_INFO)
    public AjaxResponse getPlatformInfo(@RequestBody Map param) {

        // 返回
        return success(null);
    }


    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK.SAVE_TASK)
    public AjaxResponse saveTask(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK.DEL_TASK)
    public AjaxResponse delTask(@RequestBody Map param) {

        // 返回
        return success(null);
    }

}
