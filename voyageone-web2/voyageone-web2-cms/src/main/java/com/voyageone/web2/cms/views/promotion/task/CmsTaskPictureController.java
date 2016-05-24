package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.service.bean.cms.CmsBtBeatInfoBean;
import com.voyageone.service.bean.cms.CmsBtTasksBean;
import com.voyageone.service.bean.cms.task.beat.TaskBean;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsUrlConstants.PROMOTION.TASK.BEAT;
import com.voyageone.web2.cms.bean.beat.ReqParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 价格披露画面的控制器
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

    @Autowired
    private CmsTaskService taskService;

    @RequestMapping(BEAT.CREATE)
    public AjaxResponse create(@RequestBody TaskBean taskBean) {
        TaskBean newBean = taskPictureService.create(taskBean, getUser());
        return success(newBean);
    }

    @RequestMapping(BEAT.PAGE)
    public AjaxResponse page(@RequestBody ReqParam param) {

        List<CmsBtBeatInfoBean> beatInfoModels =
                taskPictureService.getAllBeat(param.getTask_id(), param.getFlag(), param.getSearchKey(),
                        param.getOffset(), param.getSize());

        int total = taskPictureService.getAllBeatCount(param.getTask_id(), param.getFlag(), param.getSearchKey());

        List<Map<String, Object>> summary = taskPictureService.getBeatSummary(param.getTask_id());

        CmsBtTasksBean task = taskService.getTaskWithPromotion(param.getTask_id());

        Map<String, Object> map = new HashMap<>();

        map.put("list", beatInfoModels);
        map.put("total", total);
        map.put("summary", summary);
        map.put("task", task);

        return success(map);
    }

    @RequestMapping(BEAT.IMPORT)
    public AjaxResponse importBeat(@RequestParam int task_id, @RequestParam int size, @RequestParam MultipartFile file) {

        List<CmsBtBeatInfoBean> beatInfoModels = taskPictureService.importBeatInfo(task_id, size, file, getUser());

        int total = taskPictureService.getAllBeatCount(task_id, null, null);

        Map<String, Object> map = new HashMap<>();
        map.put("list", beatInfoModels);
        map.put("total", total);

        return success(map);
    }

    @RequestMapping(BEAT.DOWNLOAD)
    public ResponseEntity<byte[]> downloadBeat(@RequestParam int task_id) {

        CmsBtTasksBean task = taskService.getTaskWithPromotion(task_id);

        String filename = String.format("%s-%s.xls", task.getPromotion().getPromotionName(), task.getTaskName());

        return genResponseEntityFromBytes(filename,
                taskPictureService.downloadBeatInfo(task_id));
    }

    @RequestMapping(BEAT.CONTROL)
    public AjaxResponse control(@RequestBody ReqParam param) {
        return success(taskPictureService.control(param.getBeat_id(), param.getTask_id(), param.getFlag(), param.getForce(), getUser()));
    }

    @RequestMapping(BEAT.ADD)
    public AjaxResponse add(@RequestBody ReqParam param) {
        return success(taskPictureService.add(param.getTask_id(), param.getNum_iid(), param.getCode(), getUser()));
    }

    @RequestMapping(BEAT.ADD_CHECK)
    public AjaxResponse addCheck(@RequestBody ReqParam param) {
        return success(taskPictureService.addCheck(param.getTask_id(), param.getNum_iid()));
    }

    @RequestMapping(BEAT.ADD_CODE)
    public AjaxResponse addCode(@RequestBody ReqParam param) {
        return success(taskPictureService.getCodes(param.getPromotionId(), param.getProductModel()));
    }

    @RequestMapping(BEAT.ADD_NUMIID)
    public AjaxResponse addNumiid(@RequestBody ReqParam param) {
        return success(taskPictureService.getNewNumiid(param.getTask_id()));
    }

    @RequestMapping(BEAT.GET_TEMPLATES)
    public AjaxResponse getTemplates(@RequestBody ReqParam param) {
        List<CmsBtImageTemplateModel> allTemplate = taskPictureService.getTemplatesByPromotion(param.getPromotionId());
        return success(allTemplate);
    }
}
