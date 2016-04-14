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

    /**
     * @api {post} /cms/translation/tasks/searchHistoryTasks 1.6 根据条件查询与登录用户相关（翻译）的产品
     * @apiName doSearchTasks
     * @apiDescription 根据条件查询与登录用户相关（翻译）的产品
     * @apiGroup translation
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} searchCondition 模糊查询
     * @apiParam (应用级参数) {String} tranSts 翻译状态
     * @apiParam (应用级参数) {Integer} pageNum 当前查询页数，0为第一页
     * @apiParam (应用级参数) {Integer} pageSize 每页显示数据条数
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {Object[]} productTranslationBeanList 产品信息，没有数据时返回空数组
     * @apiSuccess (应用级返回字段) {Integer} totalUndoneCount 所有待翻译的产品总数
     * @apiSuccess (应用级返回字段) {Integer} totalDoneCount 所有已完成翻译的产品总数
     * @apiSuccess (应用级返回字段) {Integer} userDoneCount 个人已完成翻译产品总数
     * @apiSuccessExample 成功响应查询请求
     * {
     *  "code":null, "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "productTranslationBeanList": [ {"prodId":13368, "groupId":89755, "productCode":"A10-2f436", ... }...], #@see com.voyageone.web2.cms.bean.ProductTranslationBean
     *   "totalUndoneCount": 1188,
     *   "totalDoneCount": 2318,
     *   "userDoneCount": 31
     *  }
     * }
     * @apiExample  业务说明
     *  根据条件查询与登录用户相关（翻译）的产品
     *  其中，模糊查询的范围是：产品Code,产品名称（英文）,长标题,中标题,短标题,长描述（中文）,短描述（中文）,长描述（英文）,短描述（英文）
     *  若两个输入项都没有值时，则查询该登录用户还未翻译完成的产品信息
     * @apiExample 使用表
     *  使用mongo:cms_bt_product_cxxx表
     * @apiSampleRequest off
     */
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.SEARCH_HISTORY_TASKS)
    public AjaxResponse doSearchTasks(@RequestBody Map requestBean) {
        return success(feedPropsTranslateService.searchUserTasks(getUser(), requestBean));
    }

    /**
     * @api {post} /cms/translation/tasks/cancelTask 1.7 撤销已翻译的产品（将翻译状态改为未翻译）
     * @apiName doCancelTasks
     * @apiDescription 撤销已翻译的产品（将翻译状态改为未翻译）
     * @apiGroup translation
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} prodCode 产品CODE
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":null, "message":null, "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  撤销已翻译的产品（将翻译状态改为未翻译）
     * @apiExample 使用表
     *  使用mongo:cms_bt_product_cxxx表
     * @apiSampleRequest off
     */
    @RequestMapping(CmsUrlConstants.TRANSLATION.TASKS.CANCEL_TASK)
    public AjaxResponse doCancelTasks(@RequestBody Map requestBean) {
        feedPropsTranslateService.cancelUserTask(getUser(), (String) requestBean.get("prodCode"));
        return success(null);
    }
}
