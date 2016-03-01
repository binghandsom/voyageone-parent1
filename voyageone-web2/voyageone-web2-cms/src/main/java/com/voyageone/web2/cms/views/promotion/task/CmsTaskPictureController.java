package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsUrlConstants.PROMOTION.TASK.BEAT;
import com.voyageone.web2.cms.bean.beat.ReqParam;
import com.voyageone.web2.cms.bean.beat.TaskBean;
import com.voyageone.web2.cms.model.CmsBtBeatInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(BEAT.PAGE)
    public AjaxResponse page(@RequestBody ReqParam param) {
        List<CmsBtBeatInfoModel> beatInfoModels =
                taskPictureService.getAllBeat(param.getTask_id(), param.getOffset(), param.getSize());
        int total = taskPictureService.getAllBeatCount(param.getTask_id());
        Map<String, Object> map = new HashMap<>();
        map.put("list", beatInfoModels);
        map.put("total", total);
        return success(map);
    }

    @RequestMapping(BEAT.IMPORT)
    public AjaxResponse importBeat(@RequestParam int task_id, @RequestParam int size, @RequestParam MultipartFile file) {
        List<CmsBtBeatInfoModel> beatInfoModels = taskPictureService.importBeatInfo(task_id, size, file, getUser());
        int total = taskPictureService.getAllBeatCount(task_id);
        Map<String, Object> map = new HashMap<>();
        map.put("list", beatInfoModels);
        map.put("total", total);
        return success(map);
    }
}
