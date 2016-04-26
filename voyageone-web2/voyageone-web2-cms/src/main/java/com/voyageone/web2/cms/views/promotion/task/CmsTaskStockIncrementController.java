package com.voyageone.web2.cms.views.promotion.task;

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
 * Created by jeff.duan on 2016/02/29.
 */
@RestController
@RequestMapping(
        value = CmsUrlConstants.PROMOTION.TASK.STOCK_INCREMENT.ROOT,
        method = RequestMethod.POST
)
public class CmsTaskStockIncrementController extends CmsController {

    @Autowired
    private CmsTaskStockIncrementService cmsTaskStockIncrementService;

    @Autowired
    private CmsTaskStockService cmsTaskStockService;

    /**
     * searchSubTask
     *
     * @return resultBean
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK_INCREMENT.SEARCH_SUB_TASK)
    public AjaxResponse searchSubTask(@RequestBody Map<String, Object> param) {
        //调用CmsTaskStockIncrementService
        Map<String, Object> resultBean = cmsTaskStockIncrementService.getIncrementInfoBySubTaskID(param);
        // 返回
        return success(resultBean);
    }

    /**
     * @api {post} /cms/promotion/task_stock_increment/getPlatFormList 2.1 取得隔离任务的隔离平台
     * @apiName CmsTaskStockIncrementController.getPlatFormList
     * @apiDescription 取得隔离任务的隔离平台（新建增量库存隔离任务前初始化操作/ 增量库存隔离任务一览表示后）
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {Object} platformList 隔离平台信息（json数组）
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "platformList": [ {"cartId":"23", "cartName":"天猫国际"},
     *                     {"cartId":"27", "cartName":"聚美优品"} ]
     *  }
     * }
     * @apiExample 业务说明
     * 1.根据任务id，从cms_bt_stock_separate_platform_info取得隔离平台的信息。
     * @apiExample 使用表
     * cms_bt_stock_separate_platform_info
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK_INCREMENT.GET_PLATFORM_LIST)
    public AjaxResponse getPlatFormList(@RequestBody Map<String, Object> param) {
        // 返回内容
        Map<String, Object> resultBean = new HashMap<>();

        // 取得任务对应平台信息列表
        List<Map<String, Object>> platformList = cmsTaskStockService.getPlatformList((String) param.get("taskId"), this.getUser().getSelChannelId(), this.getLang());
        resultBean.put("platformList", platformList);
//        if (platformList == null || platformList.size() == 0) {
//            resultBean.put("hasAuthority", false);
//            return success(resultBean);
//        }

//        // 任务id/渠道id权限check
//        boolean hasAuthority = cmsTaskStockService.hasAuthority(this.getUser().getSelChannelId(), platformList);
//        resultBean.put("hasAuthority", hasAuthority);

        // 返回
        return success(resultBean);
    }

    /**
     * @api {post} /cms/promotion/task_stock_increment/searchTask 2.2 检索增量库存隔离任务
     * @apiName CmsTaskStockIncrementController.searchTask
     * @apiDescription 检索增量库存隔离任务
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiParam (应用级参数) {String} subTaskName 增量任务名
     * @apiParam (应用级参数) {String} cartId 平台id
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {Object} taskList 增量任务列表（json数组）
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "taskList": [ {"subTaskId":"1", "subTaskName":"天猫国际双11-增量任务1", "cartName":"天猫"},
     *                 {"subTaskId":"2", "subTaskName":"天猫国际双11-增量任务2", "cartName":"京东"},
     *                    ...]，
     *  }
     * }
     * 说明："taskName":增量任务名。"cartName":平台名。
     * @apiExample 业务说明
     *  1.根据任务id，从cms_bt_stock_separate_platform_info取得隔离平台的信息。
     *  2.根据参数.任务id，增量任务名从cms_bt_stock_separate_increment_task表检索增量库存隔离任务。
     * @apiExample 使用表
     *  cms_bt_stock_separate_platform_info
     *  cms_bt_stock_separate_increment_task
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK_INCREMENT.SEARCH_TASK)
    public AjaxResponse searchTask(@RequestBody Map<String, Object> param) {
        // 渠道id
        param.put("channelId", this.getUser().getSelChannelId());
        // 语言
        param.put("lang", this.getLang());
        // 返回内容
        Map<String, Object> resultBean = new HashMap<>();

        // 取得增量库存隔离任务
        List<Map<String, Object>> taskList = cmsTaskStockIncrementService.getStockIncrementTaskList(param);
        resultBean.put("taskList", taskList);

        // 返回
        return success(resultBean);
    }

    /**
     * @api {post} /cms/promotion/task_stock_increment/saveTask 2.3 新建/修改增量库存隔离任务
     * @apiName CmsTaskStockIncrementController.saveTask
     * @apiDescription 新建/修改增量库存隔离任务
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiParam (应用级参数) {String} subTaskId 增量任务id
     * @apiParam (应用级参数) {String} cartId 平台id
     * @apiParam (应用级参数) {String} type 增量类型（0：百分比增量；1：数值增量）
     * @apiParam (应用级参数) {String} value 增量比例或增量值
     * @apiParam (应用级参数) {String} subTaskName 增量任务名
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiErrorExample 错误示例
     * {
     *  "code": "1", "message": "增量比例/值不正确", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample 业务说明
     *  1.check输入信息。增量比例或增量值必须输入，并且必须设定而且为大于0的整数。
     *                   任务名长度check。
     *  2.如果是新建增量任务时（参数.增量任务id没有内容），将增量任务信息插入到cms_bt_stock_separate_increment_task表。
     *    如果是修改增量任务时（参数.增量任务id有内容），只能修改任务名，将任务名更新到cms_bt_stock_separate_increment_task表。
     *  3.如果是新建增量任务时（参数.增量任务id没有内容），抽出cms_bt_stock_separate_item表中的所有sku后，计算出各sku的可用库存数
     *    并且根据增量任务的设定，计算出增量库存隔离数，插入到cms_bt_stock_separate_increment_item表中。（固定值隔离标志位=0：按动态值进行增量隔离）
     *    3.0 参数.任务id从cms_bt_stock_separate_item表抽出所有的sku
     *    3.1.根据sku从wms_bt_inventory_center_logic表取得逻辑库存。
     *    3.2.根据sku从cms_bt_stock_separate_item表取得状态='2:隔离成功'的隔离库存数。
     *    3.3.根据sku从cms_bt_stock_separate_increment_item表取得状态='2:增量成功'的增量隔离库存数。
     *    3.4.根据sku从cms_bt_stock_sales_quantity表取得隔离期间各个隔离平台的销售数量。
     *    3.5.可用库存数 = 3.1：取得逻辑库存 - （3.2：隔离库存数 + 3.3：增量隔离库存数 - 3.4：隔离平台的销售数量）
     *    3.6.参数.增量类型 = 0：百分比增量时，增量库存隔离数 = 可用库存数 * 增量的百分比
     *        参数.增量类型 = 1：数值增量时，增量库存隔离数 = 增量值
     * @apiExample 使用表
     *  cms_bt_stock_separate_increment_task
     *  cms_bt_stock_separate_item
     *  cms_bt_stock_separate_increment_item
     *  wms_bt_inventory_center_logic
     *  cms_bt_stock_sales_quantity
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK_INCREMENT.SAVE_TASK)
    public AjaxResponse saveTask(@RequestBody Map<String, Object> param) {
        //创建者/更新者用
        param.put("userName", this.getUser().getUserName());
        //公司平台销售渠道
        param.put("channel_id", this.getUser().getSelChannelId());
        //调用CmsTaskStockIncrementService
        cmsTaskStockIncrementService.saveIncrementInfoByTaskID(param);
        //返回数据的类型
        return success(param);
    }

    /**
     * @api {post} /cms/promotion/task_stock_increment/delTask 2.4 删除增量库存隔离任务
     * @apiName CmsTaskStockIncrementController.delTask
     * @apiDescription 删除增量库存隔离任务
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} subTaskId 增量任务id
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiErrorExample  错误示例
     * {
     *  "code": "1", "message": "已经开始增量隔离，删除失败", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  1.check是否可以删除。如果这个任务在cms_bt_stock_separate_increment_item表中存在状态<>0:未进行的增量隔离数据（已经启动过这个任务的增量库存隔离），则不允许删除。
     *                       如果这个任务在cms_bt_stock_separate_increment_item_history表中存在,则不允许删除。
     *  2.删除cms_bt_stock_separate_increment_item表，cms_bt_stock_separate_increment_task表中对应的数据。
     * @apiExample 使用表
     *  cms_bt_stock_separate_increment_item
     *  cms_bt_stock_separate_increment_task
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK_INCREMENT.DEL_TASK)
    public AjaxResponse delTask(@RequestBody Map<String, Object> param) {

        // 删除增量库存隔离任务
        cmsTaskStockIncrementService.delTask((String) param.get("taskId"), (String) param.get("subTaskId"));

        // 返回
        return success(null);
    }


}
