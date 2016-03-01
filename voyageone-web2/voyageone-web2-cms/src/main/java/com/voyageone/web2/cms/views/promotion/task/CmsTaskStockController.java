package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionTaskModel;
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
        value = CmsUrlConstants.PROMOTION.TASK.STOCK.ROOT,
        method = RequestMethod.POST
)
public class CmsTaskStockController extends CmsController {

    @Autowired
//    private CmsTaskStockService cmsTaskStockService;

    /**
     * @api {post} /cms/promotion/task_stock/initNewTask 新建库存隔离任务前初始化数据取得（取得隔离平台和共享平台）
     * @apiName initNewTask
     * @apiDescription 新建库存隔离任务前初始化数据取得（取得隔离平台和共享平台）
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (系统级参数) {String} channel_id 渠道id
     * @apiParam (应用级参数) {List} selPromotionList 选择的Promotion列表
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {String} taskName 任务名
     * @apiSuccess (应用级返回字段) {Boolean} onlySku 只导入Sku和库存
     * @apiSuccess (应用级返回字段) {Object} platformList 隔离平台列表（json数组）
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "taskName":"",
     *   "onlySku":false,
     *   "platformList": [ {"carId":"23", "cartName":"天猫国际", "value":"", "restoreTime":"", "addPriority":"1", "subtractPriority":"3},
     *                     {"cartId":"27", "cartName":"聚美优品", "value":"", "restoreTime":"", "addPriority":"2", "subtractPriority":"2"}
     *                     {"cartId":"-1", "cartName":"共享（京东|京东国际|官网）", "value":"", "restoreTime":"", "addPriority":"3", "subtractPriority":"1"}...],
     *  }
     * }
     * 说明：先列出隔离平台，后列出共享平台，共享平台合并成一条数据，并且指定"cart_id":"-1"作为共享平台。
     * @apiErrorExample  错误示例
     * {
     *  "code": "1", "message": "选择的Promotion不能库存隔离（已经隔离中）", "displayType":null, "redirectTo":null, "data":null
     * }     *
     * @apiExample  业务说明
     *  1.某个渠道下，根据选择的Promotion，如果选择Promotion对应的平台已经在隔离状态中（cms_bt_promotion_task.status = '1:活动中'），那么提示该Promotion已经隔离的错误信息。
     *  2.根据选择的Promotion，从cms_bt_promotion表取得 任务的隔离平台信息。
     *  3.从tm_channel_shop取得该渠道对应的所有平台信息，除去1取得的隔离平台，剩余的平台作为共享平台。
     * @apiExample 使用表
     *  cms_bt_promotion
     *  tm_channel_shop
     *  cms_bt_promotion_task
     *  cms_bt_stock_separate_platform_info
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.INIT_NEW_TASK)
    public AjaxResponse initNewTask(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/saveTask 新建/修改库存隔离任务
     * @apiName saveTask
     * @apiDescription 新建/修改库存隔离任务
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (系统级参数) {String} channel_id 渠道id
     * @apiParam (应用级参数) {Object} stockTaskInfo（json格式） 选择的Promotion列表
     * @apiParamExample  stockTaskInfo参数示例
     *  "stockTaskInfo":{
     *   "taskId":1
     *   "taskName":"天猫国际双11库存隔离任务",
     *   "onlySku":false,
     *   "platformList": [ {"cartId":"23", "cartName":"天猫国际", "value":"70%", "restoreTime":"2016/11/2 03:00:00", "addPriority":"1", "subtractPriority":"3},
     *                     {"cartId":"27", "cartName":"聚美优品", "value":"30%", "restoreTime":"2016/11/2 10:00:00", "addPriority":"2", "subtractPriority":"2"}
     *                     {"cartId":"-1", "cartName":"共享（京东|京东国际|官网）", "value":"", "restoreTime":"", "addPriority":"3", "subtractPriority":"1"}...],
     *  }
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {Object} platformList 隔离平台列表（json数组）
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiErrorExample  错误示例
     * {
     *  "code": "1", "message": "隔离比例不正确", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  1.check输入信息。隔离比例和优先顺必须设定而且为大于0的整数。优先顺必须是1开始的连续整数。 隔离结束时间必须是时间格式。
     *                   隔离结束时间如果不输入，指定为活动结束的时间 加上 指定的分钟数（在com_mt_value表中定义id=426）。隔离结束时间必须大于指定为活动结束的时间 加上 指定的分钟数。
     *  2.将隔离信息（对应平台隔离比例，还原时间，优先顺等）反应到cms_bt_promotion_task表和cms_bt_stock_separate_platform_info表。(新建的场合，任务状态 0:Ready；修改的场合，隔离比例不能变更)
     *  新建的场合，继续下面的步骤
     *  3.抽出隔离平台下面的所有Sku和逻辑库存，
     *    如果参数stockTaskInfo.onlySku = false,将隔离平台的信息插入到cms_bt_stock_separate_item表（只有Sku和逻辑库存信息，可用库存为0，状态为"0:未进行"）
     *    如果参数stockTaskInfo.onlySku = true,则按设定的隔离比例计算出各个平台的隔离库存更新到cms_bt_stock_separate_item表。（状态为"0:未进行"）
     * @apiExample 使用表
     *  cms_bt_promotion
     *  cms_bt_promotion_task
     *  cms_bt_stock_separate_platform_info
     *  cms_bt_stock_separate_item
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.SAVE_TASK)
    public AjaxResponse savTask(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/search 检索
     * @apiName search
     * @apiDescription 检索
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} task_id 任务id
     * @apiParam (应用级参数) {String} code 商品Code
     * @apiParam (应用级参数) {String} sku Sku
     * @apiParam (应用级参数) {String} status 状态（0：未进行； 1：等待隔离； 2：隔离成功； 3：隔离失败； 4：等待还原； 5：还原成功； 6：还原失败； 7：再修正； 空白:ALL）
     * @apiParam (应用级参数) {String} start 检索开始Index
     * @apiParam (应用级参数) {String} length 检索件数
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (系统级返回字段) {String} task_id 任务id
     * @apiSuccess (应用级返回字段) {String} codeNum 商品Code数
     * @apiSuccess (应用级返回字段) {String} skuNum Sku数
     * @apiSuccess (应用级返回字段) {String} readyNum 未进行数
     * @apiSuccess (应用级返回字段) {String} waitSeparationNum 等待隔离数
     * @apiSuccess (应用级返回字段) {String} separationOKNum 隔离成功数
     * @apiSuccess (应用级返回字段) {String} separationFailNum 隔离失败数
     * @apiSuccess (应用级返回字段) {String} waitRestoreNum 等待还原数
     * @apiSuccess (应用级返回字段) {String} restoreOKNum 还原成功数
     * @apiSuccess (应用级返回字段) {String} restoreFailNum 还原失败数
     * @apiSuccess (应用级返回字段) {String} changedNum 再修正数
     * @apiSuccess (应用级返回字段) {String} platformList 隔离平台列表
     * @apiSuccess (应用级返回字段) {Object} stockList 库存隔离明细（json数组）
     * @apiSuccess (应用级返回字段) {Object} realStockList 实时库存状态（json数组）
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "taskId":"1",
     *   "codeNum":5000,
     *   "skuNum":10000,
     *   "readyNum":1000,
     *   "waitSeparationNum":5000,
     *   "separationOKNum":1000,
     *   "separationFailNum":0,
     *   "waitRestoreNum":5000,
     *   "restoreOKNum":1000,
     *   "restoreFailNum":0,
     *   "changedNum":0,
     *   "platformList": [ {"cartId":"23", "cartName":"天猫国际"},
     *                     {"cartId":"27", "cartName":"聚美优品"} ]，
     *   "stockList": [ {"code":"35265465", "sku":"256354566-9", "name":"Puma Suede Classic+", "qty":50,
     *                                                         "platformStock":[{"cartId":"23", "qty":30, "status":"隔离成功"},
     *                                                                          {"cartId":"27", "qty":10, "status":"隔离失败"}]},
     *                   {"code":"35265465", "sku":"256354566-10", "name":"Puma Suede Classic +Puma Suede Classic+", "qty":80,
     *                                                         "platformStock":[{"cartId":"23", "qty":40, "status":"等待隔离"},
     *                                                                          {"cartId":"27", "qty":30, "status":"还原成功"}]},
     *                   {"code":"35265465", "sku":"256354566-11", "name":"Puma Suede Classic+", "qty":80,
     *                                                         "platformStock":[{"cartId":"23", "qty":-1, "status":""},
     *                                                                          {"cartId":"27", "qty":10, "status":"还原成功"}]},
     *                    ...]，
     *   "realStockList": [ {"code":"35265465", "Sku":"256354566-9", "name":"Puma Suede Classic+", "qty":50,
     *                                                         "platformStock":[{"cart_id":"23", "separationQty":30, "salesQty":10},
     *                                                                          {"cart_id":"27", "separationQty":20, "salesQty":0}]},
     *                   {"code":"35265465", "Sku":"256354566-10", "name":"Puma Suede Classic +Puma Suede Classic+", "qty":80,
     *                                                         "platformStock":[{"cart_id":"23", "separationQty":30, "salesQty":10},
     *                                                                          {"cart_id":"27", "separationQty":20, "salesQty":0}]},
     *                   {"code":"35265465", "Sku":"256354566-11", "name":"Puma Suede Classic+", "qty":80,
     *                                                         "platformStock":[{"cart_id":"23", "separationQty":-1, },
     *                                                                          {"cart_id":"27", "separationQty":-1, }]}
     *                    ...]，
     *  }
     * }
     * 说明："qty":-1为动态。"separationQty":-1为动态。
     * @apiExample  业务说明
     *  1.根据任务ID，Code，Sku和状态从cms_bt_stock_separate_item表检索库存隔离明细。（按一个sku一条记录，按sku进行分页）
     * @apiExample 使用表
     *  cms_bt_stock_separate_item
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.SEARCH)
    public AjaxResponse search(@RequestBody Map param) {


        // 返回
        return success(null);
    }


    /**
     * @api {post} /cms/promotion/task_stock/getCommonStockList 获取库存隔离明细(翻页用)
     * @apiName getCommonStockList
     * @apiDescription 获取库存隔离明细
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} task_id 任务id
     * @apiParam (应用级参数) {String} code 商品Code
     * @apiParam (应用级参数) {String} sku Sku
     * @apiParam (应用级参数) {String} status 状态（0：未进行； 1：等待隔离； 2：隔离成功； 3：隔离失败； 4：等待还原； 5：还原成功； 6：还原失败； 7：再修正； 空白:ALL）
     * @apiParam (应用级参数) {String} start 检索开始Index
     * @apiParam (应用级参数) {String} length 检索件数
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {String} countNum 合计数
     * @apiSuccess (应用级返回字段) {Object} stockList 库存隔离明细（json数组）
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "countNum":5000,
     *   "stockList": [ {"code":"35265465", "sku":"256354566-9", "name":"Puma Suede Classic+", "qty":50,
     *                                                         "platformStock":[{"cartId":"23", "qty":30, "status":"隔离成功"},
     *                                                                          {"cartId":"27", "qty":10, "status":"隔离失败"}]},
     *                   {"code":"35265465", "sku":"256354566-10", "name":"Puma Suede Classic +Puma Suede Classic+", "qty":80,
     *                                                         "platformStock":[{"cartId":"23", "qty":40, "status":"等待隔离"},
     *                                                                          {"cartId":"27", "qty":30, "status":"还原成功"}]},
     *                   {"code":"35265465", "sku":"256354566-11", "name":"Puma Suede Classic+", "qty":80,
     *                                                         "platformStock":[{"cartId":"23", "qty":-1, "status":""},
     *                                                                          {"cartId":"27", "qty":10, "status":"还原成功"}]},
     *                    ...]
     *  }
     * }
     * 说明："qty":-1为动态。
     * @apiExample  业务说明
     *  1.根据任务ID，Code，Sku和状态从cms_bt_stock_separate_item表检索库存隔离明细。（按一个sku一条记录，按sku进行分页）
     * @apiExample 使用表
     *  cms_bt_stock_separate_item
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.GET_COMMON_STOCK_LIST)
    public AjaxResponse getCommonStockList(@RequestBody Map param) {


        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/getRealStockInfo 获取实时库存状态(翻页用)
     * @apiName getRealStockInfo
     * @apiDescription 获取实时库存状态
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} task_id 任务id
     * @apiParam (应用级参数) {String} code 商品Code
     * @apiParam (应用级参数) {String} sku Sku
     * @apiParam (应用级参数) {String} status 状态（0：未进行； 1：等待隔离； 2：隔离成功； 3：隔离失败； 4：等待还原； 5：还原成功； 6：还原失败； 7：再修正； 空白:ALL）
     * @apiParam (应用级参数) {String} start 检索开始Index
     * @apiParam (应用级参数) {String} length 检索件数
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {String} countNum 合计数
     * @apiSuccess (应用级返回字段) {Object} realStockList 实时库存状态（json数组）
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "countNum":10000,
     *   "realStockList": [ {"code":"35265465", "Sku":"256354566-9", "name":"Puma Suede Classic+", "qty":50,
     *                                                         "platformStock":[{"cart_id":"23", "separationQty":30, "salesQty":10},
     *                                                                          {"cart_id":"27", "separationQty":20, "salesQty":0}]},
     *                   {"code":"35265465", "Sku":"256354566-10", "name":"Puma Suede Classic +Puma Suede Classic+", "qty":80,
     *                                                         "platformStock":[{"cart_id":"23", "separationQty":30, "salesQty":10},
     *                                                                          {"cart_id":"27", "separationQty":20, "salesQty":0}]},
     *                   {"code":"35265465", "Sku":"256354566-11", "name":"Puma Suede Classic+", "qty":80,
     *                                                         "platformStock":[{"cart_id":"23", "separationQty":-1, },
     *                                                                          {"cart_id":"27", "separationQty":-1, }]},
     *                    ...]
     *  }
     * }
     * 说明："separationQty":-1为动态。
     * @apiExample  业务说明
     *  1.根据Code，Sku和状态从cms_bt_stock_separate_item表检索库存隔离明细。（按一个sku一条记录，按sku进行分页）
     * @apiExample 使用表
     *  cms_bt_stock_separate_item
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.GET_REAL_STOCK_INFO)
    public AjaxResponse getRealStockInfo(@RequestBody Map param) {


        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/initNewRecord 新建新隔离明细前初始化操作（取得隔离平台）
     * @apiName initNewRecord
     * @apiDescription 新建新隔离明细前初始化操作（取得隔离平台）
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} task_id 任务id
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
     * @apiExample  业务说明
     *  1.根据任务id，从cms_bt_stock_separate_platform_info取得隔离平台的信息。
     * @apiExample 使用表
     *  cms_bt_stock_separate_platform_info
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.INIT_NEW_RECORD)
    public AjaxResponse initNewRecord(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/getUsableStock 取得可用库存
     * @apiName getUsableStock
     * @apiDescription 取得可用库存
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} sku Sku
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {Integer} stockNum 可用库存数
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "stockNum":10000
     *  }
     * }
     * @apiExample  业务说明
     *  按下面的逻辑计算出可用库存数
     *  1.根据sku从wms_bt_inventory_center_logic表取得逻辑库存。
     *  2.根据sku从cms_bt_stock_separate_item表取得状态='2:隔离成功'的隔离库存数。
     *  3.根据sku从cms_bt_increment_stock_separate_item表取得状态='2:隔离成功'的增量隔离库存数。
     *  4.根据sku从cms_bt_sales_quantity表取得隔离期间各个隔离平台的销售数量。
     *  5.可用库存数 = 1：取得逻辑库存 - （2：隔离库存数 + 3：增量隔离库存数 - 4：隔离平台的销售数量）
     *
     * @apiExample 使用表
     *  wms_bt_inventory_center_logic
     *  cms_bt_stock_separate_item
     *  cms_bt_increment_stock_separate_item
     *  cms_bt_sales_quantity
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.GET_USABLE_STOCK)
    public AjaxResponse getUsableStock(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock/getUsableStock 取得可用库存
     * @apiName getUsableStock
     * @apiDescription 取得可用库存
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} task_id 任务id
     * @apiParam (应用级参数) {String} code 商品Code
     * @apiParam (应用级参数) {String} sku Sku
     * @apiParam (应用级参数) {String} usableStock 可用库存
     * @apiParam (应用级参数) {Object} platformList 隔离平台信息（json数组）
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {Integer} stockNum 可用库存数
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "stockNum":10000
     *  }
     * }
     * @apiExample  业务说明
     *  按下面的逻辑计算出可用库存数
     *  1.根据sku从wms_bt_inventory_center_logic表取得逻辑库存。
     *  2.根据sku从cms_bt_stock_separate_item表取得状态='2:隔离成功'的隔离库存数。
     *  3.根据sku从cms_bt_increment_stock_separate_item表取得状态='2:隔离成功'的增量隔离库存数。
     *  4.根据sku从cms_bt_sales_quantity表取得隔离期间各个隔离平台的销售数量。
     *  5.可用库存数 = 1：取得逻辑库存 - （2：隔离库存数 + 3：增量隔离库存数 - 4：隔离平台的销售数量）
     *
     * @apiExample 使用表
     *  wms_bt_inventory_center_logic
     *  cms_bt_stock_separate_item
     *  cms_bt_increment_stock_separate_item
     *  cms_bt_sales_quantity
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK.SAVE_NEW_RECORD)
    public AjaxResponse saveNewRecord(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    //    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.IMPORT_STOCK_INFO)
    public AjaxResponse importStockInfo(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    //    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.EXPORT_STOCK_INFO)
    public AjaxResponse exportStockInfo(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    //    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.EXECUTE_STOCK_SEPARATION)
    public AjaxResponse executeStockSeparation(@RequestBody Map param) {

        // 返回
        return success(null);
    }


    //    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.EXECUTE_STOCK_RESTORE)
    public AjaxResponse executeStockRestore(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    //    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.SAVE_STOCK_INFO)
    public AjaxResponse saveStockInfo(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    //    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.DEL_STOCK_INFO)
    public AjaxResponse delStockInfo(@RequestBody Map param) {

        // 返回
        return success(null);
    }


    //    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.GET_ALL_SEPARATION_INFO)
    public AjaxResponse getAllSeparationInfo(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    //    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.EXPORT_ERROR_INFO)
    public AjaxResponse exportErrorInfo(@RequestBody Map param) {

        // 返回
        return success(null);
    }
}
