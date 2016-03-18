package com.voyageone.web2.cms.views.translation;

import com.voyageone.common.configs.Enums.TypeConfigEnums;
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
 */
@RestController
@RequestMapping(method = RequestMethod.POST,value = CmsUrlConstants.TRANSLATION.TASKS.ROOT)
public class TranslationController extends CmsController{

    @Autowired
    TranslationService feedPropsTranslateService;

    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.GET_TASKS)
    public AjaxResponse doGetTasks() {

        Map<String,Object> translateTaskBeanInfo = new HashMap<>();

        translateTaskBeanInfo.put("taskInfos",feedPropsTranslateService.getUndoneTasks(getUser()));
        translateTaskBeanInfo.put("sortFieldOptions", TypeConfigEnums.MastType.translateTask.getList(getLang()));

        // 获取翻译时标题和描述的长度设置
        translateTaskBeanInfo.put("lenSetInfo",feedPropsTranslateService.getTransLenSet(getUser().getSelChannelId()));

        return success(translateTaskBeanInfo);
    }


    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.ASSIGN_TASKS)
    public AjaxResponse doAssignTask(@RequestBody TranslateTaskBean request){

        return success(feedPropsTranslateService.assignTask(getUser(),request));

    }

    // 保存翻译内容
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.SAVE_TASK)
    public AjaxResponse doSaveTask(@RequestBody ProductTranslationBean requestBean){
        return success(feedPropsTranslateService.saveTask(getUser(), requestBean, "0"));
    }

    // 保存并提交翻译内容（完成翻译任务）
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.SUBMIT_TASK)
    public AjaxResponse doSubmitTask(@RequestBody ProductTranslationBean requestBean){
        return success(feedPropsTranslateService.saveTask(getUser(), requestBean, "1"));
    }
//
//    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.COPY_FORM_MAIN_PRODUCT)
//    public AjaxResponse doCopyFormMainProduct(@RequestBody ProductTranslationBean requestBean){
//        return success(feedPropsTranslateService.copyFormMainProduct(getUser().getSelChannelId(),requestBean));
//    }

    /**
     * 获取品牌方所有属性.
     * @param requestBean
     * @return
     */
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.GET_FEED_ATTRIBUTES)
    public AjaxResponse doGetFeedAttributes(@RequestBody ProductTranslationBean requestBean) {
        return success(feedPropsTranslateService.getFeedAttributes(getUser().getSelChannelId(),requestBean.getProductCode()));

    }


    // TODO
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.SEARCH_HISTORY_TASKS)
    public AjaxResponse doSearchTasks(@RequestBody TranslateTaskBean requestBean) {
        return success(feedPropsTranslateService.searchUserTasks(getUser(),requestBean.getSearchCondition()));


    }


}
