package com.voyageone.web2.cms.views.translation;

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

        String channelId = getUser().getSelChannelId();

        String userName = getUser().getUserName();

        Map<String,Object> translateTaskBeanInfo = new HashMap<>();

        TranslateTaskBean taskBean = feedPropsTranslateService.getUndoneTasks(channelId,userName);

        translateTaskBeanInfo.put("taskInfo",taskBean);
        translateTaskBeanInfo.put("sortFieldOptions",feedPropsTranslateService.getSortFieldOptions());

        // 获取翻译时标题和描述的长度设置
        translateTaskBeanInfo.put("lenSetInfo",feedPropsTranslateService.getTransLenSet(channelId));

        return success(translateTaskBeanInfo);
    }


    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.ASSIGN_TASKS)
    public AjaxResponse doAssignTask(@RequestBody TranslateTaskBean request){

        String channelId = getUser().getSelChannelId();

        String userName = getUser().getUserName();

        int distributeStrategy = request.getDistributeRule();

        int distCount = request.getDistributeCount();

        Map<String,Object> TranslateTaskBeanInfo = new HashMap<>();

        TranslateTaskBean taskBean = feedPropsTranslateService.assignTask(channelId,userName,distributeStrategy,distCount,request.getSortCondition(),request.getSortRule());

        TranslateTaskBeanInfo.put("taskInfo",taskBean);

        return success(TranslateTaskBeanInfo);

    }

    // 保存翻译内容
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.SAVE_TASK)
    public AjaxResponse doSaveTask(@RequestBody ProductTranslationBean requestBean){
//        feedPropsTranslateService.verifyParameter(requestBean);

        String channelId = getUser().getSelChannelId();
        String userName = getUser().getUserName();

        TranslateTaskBean taskBean = feedPropsTranslateService.saveTask(channelId, userName, requestBean, "0");

        Map<String,Object> updateInfo = new HashMap<>();
        updateInfo.put("taskInfo",taskBean);
        return success(updateInfo);
    }

    // 保存并提交翻译内容（完成翻译任务）
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.SUBMIT_TASK)
    public AjaxResponse doSubmitTask(@RequestBody ProductTranslationBean requestBean){
        feedPropsTranslateService.verifyParameter(requestBean);

        String channelId = getUser().getSelChannelId();
        String userName = getUser().getUserName();

        TranslateTaskBean taskBean = feedPropsTranslateService.saveTask(channelId, userName, requestBean, "1");

        Map<String,Object> updateInfo = new HashMap<>();
        updateInfo.put("taskInfo",taskBean);
        return success(updateInfo);
    }

    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.COPY_FORM_MAIN_PRODUCT)
    public AjaxResponse doCopyFormMainProduct(@RequestBody ProductTranslationBean requestBean){

        Map<String,Object> translateTaskBeanInfo = new HashMap<>();

        String channelId = getUser().getSelChannelId();

        ProductTranslationBean translationBean = feedPropsTranslateService.copyFormMainProduct(channelId,requestBean);

        translateTaskBeanInfo.put("translationInfo",translationBean);

        return success(translateTaskBeanInfo);
    }

    /**
     * 获取品牌方所有属性.
     * @param requestBean
     * @return
     */
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.GET_FEED_ATTRIBUTES)
    public AjaxResponse doGetFeedAttributes(@RequestBody ProductTranslationBean requestBean) {

        String channelId = getUser().getSelChannelId();

        Map feedAttributesMap = feedPropsTranslateService.getFeedAttributes(channelId,requestBean.getProductCode());

        Map<String,Object> TranslateTaskBeanInfo = new HashMap<>();

        TranslateTaskBeanInfo.put("feedAttributes",feedAttributesMap);

        return success(TranslateTaskBeanInfo);

    }


    // TODO
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.SEARCH_HISTORY_TASKS)
    public AjaxResponse doSearchTasks(@RequestBody TranslateTaskBean requestBean) {

        String channelId = getUser().getSelChannelId();

        String userName = getUser().getUserName();

        Map<String,Object> translateTaskBeanInfo = new HashMap<>();

        TranslateTaskBean taskBean = feedPropsTranslateService.searchUserTasks(channelId,userName,requestBean.getSearchCondition());

        translateTaskBeanInfo.put("taskInfo",taskBean);

        return success(translateTaskBeanInfo);


    }


}
