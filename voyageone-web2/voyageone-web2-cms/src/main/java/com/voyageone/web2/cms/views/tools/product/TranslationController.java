package com.voyageone.web2.cms.views.tools.product;

import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.service.bean.cms.translation.TaskSummaryBean;
import com.voyageone.service.bean.cms.translation.TranslationTaskBean;
import com.voyageone.service.impl.cms.tools.product.TranslationTaskService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取翻译任务
 *
 * @author Ethan Shi
 * @version 2.2.0
 * @author edward.lin
 * @version 2.12.0
 */
@RestController
@RequestMapping(method = RequestMethod.POST, value = CmsUrlConstants.TRANSLATION.TASKS.ROOT)
public class TranslationController extends CmsController {

    private static final String TASK_COMPLETE = "1";

    private static final String TASK_INCOMPLETE = "0";

    @Autowired
    TranslationTaskService translationTaskService;

    /**
     * 初始化页面
     *
     * @return AjaxResponse
     */
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.INIT)
    public AjaxResponse doInit() {

        String channelId = this.getUser().getSelChannelId();
        String user = this.getUser().getUserName();
        Map<String, Object> translateTaskInitResponse = new HashMap<>();

        translateTaskInitResponse.put("sortFieldOptions", TypeConfigEnums.MastType.translateTask.getList(getLang()));
//        translateTaskInitResponse.put("taskSummary", translationTaskService.getTaskSummary(channelId, user));
        translateTaskInitResponse.put("taskSummary",new TaskSummaryBean());
        translateTaskInitResponse.put("taskDetail", translationTaskService.getCurrentTask(channelId, user));

        return success(translateTaskInitResponse);
    }

    /**
     * 暂存翻译任务
     *
     * @param requestBean TranslationTaskBean
     * @return AjaxResponse
     */
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.SAVE)
    public AjaxResponse doSave(@RequestBody TranslationTaskBean requestBean) {
        return success(save(requestBean, TASK_INCOMPLETE));
    }

    /**
     * 提交翻译任务
     *
     * @param requestBean TranslationTaskBean
     * @return AjaxResponse
     */
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.SUBMIT)
    public AjaxResponse doSubmit(@RequestBody TranslationTaskBean requestBean) {
        return success(save(requestBean, TASK_COMPLETE));
    }


    /**
     * 检索历史任务
     *
     * @param requestBean Map
     * @return AjaxResponse
     */
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.SEARCH)
    public AjaxResponse doSearch(@RequestBody Map requestBean) {
        int pageNum = Integer.valueOf(requestBean.getOrDefault("pageNum", 1).toString());
        int pageSize = Integer.valueOf(requestBean.getOrDefault("pageSize", 10).toString());
        String keyWord = requestBean.getOrDefault("keyWord", "").toString();
        String translateStatus = requestBean.getOrDefault("translateStatus", "").toString();

        String channelId = this.getUser().getSelChannelId();
        String user = this.getUser().getUserName();
        return success(translationTaskService.searchTask(pageNum, pageSize, keyWord, channelId, user, translateStatus));
    }


    /**
     * 按prodId取翻译任务详细
     *
     * @param requestBean Map
     * @return AjaxResponse
     */
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.GET)
    public AjaxResponse doGet(@RequestBody Map requestBean) {

        String channelId = this.getUser().getSelChannelId();
        String user = this.getUser().getUserName();
        long prodId = Long.valueOf(requestBean.get("prodId").toString());

        Map<String, Object> translateTaskGetResponse = new HashMap<>();
        translateTaskGetResponse.put("taskDetail", translationTaskService.getTaskById(channelId, user, prodId));
        return success(translateTaskGetResponse);
    }

    /**
     * 获取翻译任务
     * @param requestBean Map
     * @return AjaxResponse
     */
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.ASSIGN)
    public AjaxResponse doAssign(@RequestBody Map requestBean) {

        String channelId = this.getUser().getSelChannelId();
        String user = this.getUser().getUserName();

        String keyWord = requestBean.getOrDefault("keyWord", "").toString();
        String priority = requestBean.getOrDefault("priority", "").toString();
        String sort = requestBean.getOrDefault("sort", "").toString();

        Map<String, Object> translateTaskAssignResponse = new HashMap<>();

        translateTaskAssignResponse.put("taskDetail", translationTaskService.assignTask(channelId, user, priority, sort, keyWord));
        translateTaskAssignResponse.put("taskSummary", translationTaskService.getTaskSummary(channelId, user));
        return success(translateTaskAssignResponse);
    }

    /**
     * 保存任务
     *
     * @param requestBean TranslationTaskBean
     * @param status 保存类型: 暂存, 提交
     * @return Map<String, Object>
     */
    private Map<String, Object> save(@RequestBody TranslationTaskBean requestBean, String status) {
        String channelId = this.getUser().getSelChannelId();
        String user = this.getUser().getUserName();
        Map<String, Object> translateTaskSaveResponse = new HashMap<>();

        translateTaskSaveResponse.put("taskDetail", translationTaskService.saveTask(requestBean, channelId, user, status));
        translateTaskSaveResponse.put("taskSummary", translationTaskService.getTaskSummary(channelId, user));
        return translateTaskSaveResponse;
    }


}
