package com.voyageone.web2.cms.views.tools.product;

import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.service.bean.cms.translation.TranslationTaskBean;
import com.voyageone.service.impl.cms.TranslationTaskService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.ProductTranslationBean;
import com.voyageone.web2.cms.bean.TranslateTaskBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lewis on 15-12-16.
 *
 * @author Ethan Shi
 * @version 2.2.0
 *
 */
@RestController
@RequestMapping(method = RequestMethod.POST,value = CmsUrlConstants.TRANSLATION.TASKS.ROOT)
public class TranslationController extends CmsController{

    private static final  String  TASK_COMPLETE = "1";

    private static final  String  TASK_INCOMPLETE = "0";


    @Autowired
    TranslationTaskService translationTaskService;


    /**
     * 初始化页面
     *
     * @return
     */
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.INIT)
    public AjaxResponse doInit()
    {

        String channelId = this.getUser().getSelChannelId();
        String user =this.getUser().getUserName();
        Map<String,Object> translateTaskInitResponse = new HashMap<>();

        translateTaskInitResponse.put("sortFieldOptions", TypeConfigEnums.MastType.translateTask.getList(getLang()));
        translateTaskInitResponse.put("taskSummary", translationTaskService.getTaskSummary(channelId, user));
        translateTaskInitResponse.put("taskDetail", translationTaskService.getCurrentTask(channelId,user));

        return success(translateTaskInitResponse);
    }

    /**
     * 保存任务
     *
     * @param requestBean
     * @return
     */
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.SAVE)
    public AjaxResponse doSave(@RequestBody TranslationTaskBean requestBean){
        return success(save(requestBean, TASK_INCOMPLETE));
    }

    /**
     * 提交任务
     *
     * @param requestBean
     * @return
     */
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.SUBMIT)
    public AjaxResponse doSubmit(@RequestBody TranslationTaskBean requestBean){
        return success(save(requestBean, TASK_COMPLETE));
    }


    /**
     * 检索历史任务
     *
     * @param requestBean
     * @return
     */
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.SEARCH)
    public AjaxResponse doSearch(@RequestBody Map requestBean){
        int pageNum = Integer.valueOf(requestBean.getOrDefault("pageNum", 1).toString());
        int pageSize = Integer.valueOf(requestBean.getOrDefault("pageSize", 10).toString());
        String keyWord = requestBean.getOrDefault("keyWord","").toString();
        String translateStatus = requestBean.getOrDefault("translateStatus", "").toString();

        String channelId = this.getUser().getSelChannelId();
        String user =this.getUser().getUserName();
        return success(translationTaskService.searchTask(pageNum,pageSize,keyWord,channelId, user,translateStatus));
    }


    /**
     * 按prodId取翻译任务详细
     *
     * @param requestBean
     * @return
     */
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.GET)
    public AjaxResponse doGet(@RequestBody Map requestBean) {

        String channelId = this.getUser().getSelChannelId();
        String user =this.getUser().getUserName();
        int prodId = Integer.valueOf(requestBean.get("prodId").toString());

        Map<String,Object> translateTaskGetResponse = new HashMap<>();
        translateTaskGetResponse.put("taskDetail", translationTaskService.getTaskById( channelId, user, prodId));
        return success(translateTaskGetResponse);
    }


    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.ASSIGN)
    public AjaxResponse doAssign(@RequestBody Map requestBean) {

        String channelId = this.getUser().getSelChannelId();
        String user =this.getUser().getUserName();

        String keyWord = requestBean.getOrDefault("keyWord","").toString();
        String priority = requestBean.getOrDefault("priority","").toString();
        String sort = requestBean.getOrDefault("sort","").toString();

        Map<String,Object> translateTaskAssignResponse = new HashMap<>();
        translateTaskAssignResponse.put("taskSummary", translationTaskService.getTaskSummary(channelId, user));
        translateTaskAssignResponse.put("taskDetail", translationTaskService.assignTask( channelId, user, priority, sort, keyWord));
        return success(translateTaskAssignResponse);
    }

    /**
     * 保存任务
     *
     * @param requestBean
     * @param status
     * @return
     */
    private Map<String, Object> save(@RequestBody TranslationTaskBean requestBean, String status) {
        String channelId = this.getUser().getSelChannelId();
        String user =this.getUser().getUserName();
        Map<String,Object> translateTaskSaveResponse = new HashMap<>();
        translateTaskSaveResponse.put("taskSummary", translationTaskService.getTaskSummary(channelId, user));
        translateTaskSaveResponse.put("taskDetail", translationTaskService.saveTask(requestBean, channelId,user, status));
        return translateTaskSaveResponse;
    }



}
