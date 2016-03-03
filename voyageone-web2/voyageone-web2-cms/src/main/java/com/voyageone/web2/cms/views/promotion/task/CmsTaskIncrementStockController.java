package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by jeff.duan on 2016/02/29.
 */
@RestController
@RequestMapping(
        value = CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK.ROOT,
        method = RequestMethod.POST
)
public class CmsTaskIncrementStockController extends CmsController {

    @Autowired
//    private CmsTaskIncrementStockService cmsTaskIncrementStockService;

    /**
     * @api {post} /cms/promotion/task_increment_stock/searchTask 检索
     * @apiName searchTask
     * @apiDescription 检索
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiParam (应用级参数) {String} subTaskName 增量任务名
     * @apiParam (应用级参数) {String} cartId 平台id
     * @apiParam (应用级参数) {String} start 检索开始Index
     * @apiParam (应用级参数) {String} length 检索件数
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {String} countNum 合计数
     * @apiSuccess (应用级返回字段) {Object} taskList 增量任务列表（json数组）
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "countNum":5,
     *   "taskList": [ {"taskName":"天猫国际双11-增量任务1", "cartName":"天猫"},
     *                 {"taskName":"天猫国际双11-增量任务2", "cartName":京东},
     *                    ...]，
     *  }
     * }
     * 说明："taskName":增量任务名。"cartName":平台名。
     * @apiExample  业务说明
     *  1.根据任务id，增量任务名从cms_bt_incre_stock_separate_task表检索增量库存隔离任务。
     * @apiExample 使用表
     *  cms_bt_incre_stock_separate_task
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK.SEARCH_TASK)
    public AjaxResponse searchTask(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_increment_stock/saveTask 新建/修改增量库存隔离任务
     * @apiName saveTask
     * @apiDescription 新建/修改库存隔离任务
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
     * @apiErrorExample  错误示例
     * {
     *  "code": "1", "message": "增量比例/值不正确", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  1.check输入信息。增量比例或增量值必须输入，并且必须设定而且为大于0的整数。
     *                   如果从任务一览新建，那么共享平台的优先顺必须一致。
     *                   如果从任务一览新建活动时，隔离结束时间可以不输入，不输入的情况下，指定为活动结束的时间 加上 指定的分钟数（在com_mt_value表中定义id=426）。
     *                                             隔离结束时间输入时，必须大于指定为活动结束的时间 加上 指定的分钟数。
     *  2.将隔离信息（对应平台隔离比例，活动开始时间，还原时间，优先顺等）反应到cms_bt_tasks表和cms_bt_stock_separate_platform_info表。(新建的场合，任务状态 0:Ready；修改的场合，隔离比例不能变更)
     *  新建的场合，继续下面的步骤
     *  3.如果从活动一览新建活动时，抽出隔离平台下面的所有Sku后，取得商品基本情报，计算出可用库存数和各隔离平台的隔离数。
     *     3.0.从mangoDB的商品表，取得商品基本情报(每个channel需要取得的字段是不一样的，需要读取com_mt_value_channel的配置type_id=61)
     *    如果参数stockTaskInfo.onlySku = false,将隔离平台的信息插入到cms_bt_stock_separate_item表（只有基本信息和可用库存，各平台的隔离库存为0，状态为"0:未进行"）
     *    如果参数stockTaskInfo.onlySku = true,则按设定的隔离比例计算出各个平台的隔离库存更新到cms_bt_stock_separate_item表。（状态为"0:未进行"）
     *    3.1.根据sku从wms_bt_inventory_center_logic表取得逻辑库存。
     *    3.2.根据sku从cms_bt_stock_separate_item表取得状态='2:隔离成功'的隔离库存数。
     *    3.3.根据sku从cms_bt_increment_stock_separate_item表取得状态='2:隔离成功'的增量隔离库存数。
     *    3.4.根据sku从cms_bt_sales_quantity表取得隔离期间各个隔离平台的销售数量。
     *    3.5.可用库存数 = 1：取得逻辑库存 - （2：隔离库存数 + 3：增量隔离库存数 - 4：隔离平台的销售数量）
     *    3.6.隔离平台隔离库存 = 可用库存数 * 设定的百分比
     * @apiExample 使用表
     *  cms_bt_promotion
     *  cms_bt_tasks
     *  cms_bt_stock_separate_platform_info
     *  cms_bt_stock_separate_item
     *  com_mt_value_channel
     *  cms_bt_product_cxxx
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK.SAVE_TASK)
    public AjaxResponse saveTask(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK.DEL_TASK)
    public AjaxResponse delTask(@RequestBody Map param) {

        // 返回
        return success(null);
    }


    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK.GET_PLATFORM_INFO)
    public AjaxResponse getPlatformInfo(@RequestBody Map param) {

        // 返回
        return success(null);
    }


    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK.SEARCH_ITEM)
    public AjaxResponse searchItem(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK.SAVE_RECORD)
    public AjaxResponse saveRecord(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK.DEL_RECORD)
    public AjaxResponse delRecord(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK.IMPORT_STOCK_INFO)
    public AjaxResponse importStockInfo(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK.EXPORT_STOCK_INFO)
    public ResponseEntity exportStockInfo(@RequestBody Map param) {

        // 返回
        return genResponseEntityFromBytes("fileName", new byte[]{});
    }

    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK.EXECUTE_INCREMENT_STOCK_SEPARATION)
    public AjaxResponse executeIncrementStockSeparation(@RequestBody Map param) {

        // 返回
        return success(null);
    }
}
