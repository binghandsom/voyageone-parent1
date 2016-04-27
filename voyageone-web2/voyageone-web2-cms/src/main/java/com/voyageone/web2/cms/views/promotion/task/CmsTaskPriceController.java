package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.service.model.cms.CmsBtTaskTejiabaoModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/12/11.
 */
@RestController
@RequestMapping(
        value = CmsUrlConstants.PROMOTION.TASK.PRICE.ROOT,
        method = RequestMethod.POST
)
public class CmsTaskPriceController extends CmsController {

    @Autowired
    private CmsTaskPriceService cmsTaskPriceService;

    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.PRICE.GET_PRICE_LIST)
    public AjaxResponse getPriceList(@RequestBody Map param) {

        int cnt = cmsTaskPriceService.getPriceListCnt(param);
        List<Map<String,Object>> resultBean = cmsTaskPriceService.getPriceList(param);
        Map<String,Object> result = new HashMap<>();
        result.put("resultData",resultBean);
        result.put("total", cnt);
        // 返回用户信息
        return success(result);
    }

    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.PRICE.UPDATE_TASK_STATUS)
    public AjaxResponse updateTaskStatus(@RequestBody CmsBtTaskTejiabaoModel param) {

        param.setModifier(getUser().getUserName());
        cmsTaskPriceService.updateTaskStatus(param);
        // 返回用户信息
        return success(null);
    }
}
