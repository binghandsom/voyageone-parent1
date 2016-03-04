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
        value = CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK_DETAIL.ROOT,
        method = RequestMethod.POST
)
public class CmsTaskIncrementStockDetailController extends CmsController {

//    @Autowired
//    private CmsTaskIncrementStockDetailService cmsTaskIncrementStockDetailService;


    /**
     * @api {post} /cms/promotion/task_increment_stock_detail/searchItem 3.1 检索增量隔离明细
     * @apiName CmsTaskIncrementStockDetailController.searchItem
     * @apiDescription 检索增量隔离明细
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (系统级参数) {String} channelId 渠道id
     * @apiParam (应用级参数) {String} subTaskId 增量任务id
     * @apiParam (应用级参数) {String} code 商品Code
     * @apiParam (应用级参数) {String} sku Sku
     * @apiParam (应用级参数) {String} status 状态（0：未进行； 1：等待隔离； 2：隔离成功； 3：隔离失败； 空白:ALL）
     * @apiParam (应用级参数) {String} property1 属性1（品牌）
     * @apiParam (应用级参数) {String} property2 属性2（英文短描述）
     * @apiParam (应用级参数) {String} property3 属性3（性别）
     * @apiParam (应用级参数) {String} property4 属性4（SIZE）
     * @apiParam (应用级参数) {String} start 检索开始Index
     * @apiParam (应用级参数) {String} length 检索件数
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {String} countNum 总数
     * @apiSuccess (应用级返回字段) {String} readyNum 未进行数
     * @apiSuccess (应用级返回字段) {String} waitSeparationNum 等待增量数
     * @apiSuccess (应用级返回字段) {String} separationOKNum 增量成功数
     * @apiSuccess (应用级返回字段) {String} separationFailNum 增量失败数
     * @apiSuccess (应用级返回字段) {String} cartId 平台id
     * @apiSuccess (应用级返回字段) {String} cartName 平台名
     * @apiSuccess (应用级返回字段) {Object} propertyList 属性列表（json数组）
     * @apiSuccess (应用级返回字段) {Object} stockList 库存隔离明细（json数组）
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "countNum":10000,
     *   "readyNum":1000,
     *   "waitSeparationNum":5000,
     *   "separationOKNum":1000,
     *   "separationFailNum":0,
     *   "cartId":"23",
     *   "cartName":"天猫国际",
     *   "propertyList": [ {"name":"品牌"},
     *                     {"name":"英文短描述"},
     *                     {"name":"性别"},
     *                     {"name":"Size"}]，
     *   "stockList": [ {"code":"35265465", "sku":"256354566-9", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":"50", "incrementQty":"50", "status":"未进行", "fixFlg":false},
     *                   {"code":"35265465", "sku":"256354566-10", "property1":"Puma", "property2":"Puma Suede Classic +Puma Suede Classic+", "property3":"women", "property4":"10", "qty":"80", "incrementQty":"80", "status":"未进行", "fixFlg":false},
     *                   {"code":"35265465", "sku":"256354566-11", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":"20", "incrementQty":"20", "status":"增量成功", "fixFlg":false},
     *                    ...]
     *  }
     * }
     * 说明：propertyList中，"name":属性名
     * stockList中，"qty"：可调配库存；"incrementQty"：指定平台下的增量库存；"status"：状态；"fixFlg":更新固定值标志位
     * @apiExample  业务说明
     *  1.根据参数.渠道id从com_mt_value_channel表（type=62）取得动态属性。（配置了这个channel显示几个属性，每个属性在画面上的显示名称是什么）
     *  2.根据参数.增量任务id，商品Code，Sku，状态和各个属性从cms_bt_increment_stock_separate_item表检索增量库存隔离明细。（按一个Sku一条记录，按Sku进行分页）
     *
     * @apiExample 使用表
     *  cms_bt_increment_stock_separate_item
     *  com_mt_value_channel
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK_DETAIL.SEARCH_ITEM)
    public AjaxResponse searchItem(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_increment_stock_detail/saveItem 3.2 保存一条增量隔离库存明细
     * @apiName CmsTaskIncrementStockDetailController.saveItem
     * @apiDescription 保存一条增量隔离库存明细
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} subTaskId 增量任务id
     * @apiParam (应用级参数) {Object} stockInfo（json数据） 一条Sku的隔离明细
     * @apiParamExample  stockInfo参数示例
     * {
     *   "subTaskId":1,
     *   "stockInfo":  {"code":"35265465", "sku":"256354566-9", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":"50", "incrementQty":"50", "status":"增量失败", "fixFlg":false}
     * }
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *    "stockInfo":  {"code":"35265465", "sku":"256354566-9", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":"50", "incrementQty":"50", "status":"未进行", "fixFlg":false}
     *  }
     *  }
     * @apiErrorExample  错误示例
     * {
     *  "code": "1", "message": "增量隔离库存格式不正确", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  1.check输入信息。增量隔离库存必须是大于0的整数。
     *                   如果状态="2：增量成功"或者"1:等待增量"，则不能进行修改。
     *  2.如果修改的增量隔离库存<>cms_bt_increment_stock_separate_item表里对应的值，那么更新cms_bt_increment_stock_separate_item表的值。
     *    （修正后状态变为0:未进行，其实只针对3：增量失败的场合）
     * @apiExample 使用表
     *  cms_bt_increment_stock_separate_item
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK_DETAIL.SAVE_ITEM)
    public AjaxResponse saveItem(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_increment_stock_detail/delItem 3.3 删除一条增量隔离库存明细
     * @apiName CmsTaskIncrementStockDetailController.delItem
     * @apiDescription 删除一条增量隔离库存明细
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} subTaskId 增量任务id
     * @apiParam (应用级参数) {Object} stockInfo（json数据） 一条Sku的隔离明细
     * @apiParamExample  stockInfo参数示例
     * {
     *   "subTaskId":1,
     *   "stockInfo":  {"code":"35265465", "sku":"256354566-9", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":"50", "incrementQty":"50", "status":"未进行", "fixFlg":false}
     * }
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
     *  "code": "1", "message": "删除失败（增量库存隔离后不能进行删除）", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  如果选择的增量隔离明细的状态为1:等待增量或者2：增量成功，则删除失败。否则删除这条增量隔离明细。
     * @apiExample 使用表
     *  cms_bt_increment_stock_separate_item
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK_DETAIL.DEL_ITEM)
    public AjaxResponse delItem1(@RequestBody Map param) {

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_increment_stock_detail/exportStockInfo 3.4 批量导出增量隔离库存明细
     * @apiName CmsTaskIncrementStockDetailController.exportStockInfo
     * @apiDescription 批量导出增量隔离库存明细
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (系统级参数) {String} channelId 渠道id
     * @apiParam (应用级参数) {String} subTaskId 增量任务id
     * @apiSuccess (系统级返回字段) {String} statusCode HttpStatus（eg:200:"OK"）
     * @apiSuccess (系统级返回字段) {byte[]} byte 导出的文件流
     * @apiExample  业务说明
     *  根据参数.渠道id从com_mt_value_channel表（type=62）取得有几个属性（例如 N个），和属性的名称
     *  根据参数.增量任务id从cms_bt_increment_stock_separate_item表导出库存隔离数据。
     *
     *  导出文件示例
     *  Code        Sku	            品牌        Name(英文)              性别    SIZE    可调配库存  天猫   固定值增量
     *  302370-013  302370-013-10   Air Jordan  Air Jordan IX (9) Retro	Women   10      13          12     是
     *  302370-013  302370-013-10.5 Air Jordan  Air Jordan IX (9) Retro	Women   10.5    10          9
     *  302370-013  302370-013-11   Air Jordan  Air Jordan IX (9) Retro	Man     11      3           3
     * @apiExample 使用表
     *  cms_bt_increment_stock_separate_item
     *  com_mt_value_channel
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK_DETAIL.EXPORT_STOCK_INFO)
    public ResponseEntity exportStockInfo(@RequestBody Map param) {

        // 返回
        return genResponseEntityFromBytes("fileName", new byte[]{});
    }


    /**
     * @api {post} /cms/promotion/task_increment_stock_detail/importStockInfo 3.5 批量导入增量隔离库存明细
     * @apiName CmsTaskIncrementStockDetailController.importStockInfo
     * @apiDescription 批量导入增量隔离库存明细
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (系统级参数) {String} channelId 渠道id
     * @apiParam (应用级参数) {String} subTaskId 增量任务id
     * @apiParam (应用级参数) {Object} file 导入文件
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
     *  "code": "1", "message": "第1行数据格式不正确", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  1.进行导入文件的Check。
     *      1.0 如果在cms_bt_increment_stock_separate_item表中，这个增量任务有状态<>0:未进行的数据，则不允许导入
     *      1.1 Code和Sku都不能为空白。
     *      1.2 所有字段的长度check。
     *      1.3 根据参数.渠道id从com_mt_value_channel表（type=62）取得有几个属性（例如 N个）
     *      1.4 第一行的第3列开始的属性名称必须和1.3取得的属性名称一致。
     *      1.5 第一行的第N+4列（平台信息）必须和任务所对应的平台匹配。
     *      1.6 第二行开始，第N+3列必须和cms_bt_increment_stock_separate_item表的可用库存相同。
     *      1.7 第二行开始，第N+4列的内容必须是大于0的整数。（增量库存列）
     *      1.8 第二行开始，第N+5列的内容必须是"是"或者空白。（固定值增量列）
     *
     *  2.将导入的增量库存隔离数据插入到cms_bt_increment_stock_separate_item表。
     *    2.1 如果这条增量明细信息（参数.增量任务id + sku）在cms_bt_increment_stock_separate_item表中不存在，则追加。
     *    2.2 如果这条增量明细信息（参数.增量任务id + sku）在cms_bt_increment_stock_separate_item表中存在，则更新增量库存和是否固定值增量两个字段。
     *
     *  导入文件示例
     *  Code        Sku	            品牌        Name(英文)              性别    SIZE    可调配库存  天猫   固定值增量
     *  302370-013  302370-013-10   Air Jordan  Air Jordan IX (9) Retro	Women   10      13          12     是
     *  302370-013  302370-013-10.5 Air Jordan  Air Jordan IX (9) Retro	Women   10.5    10          9
     *  302370-013  302370-013-11   Air Jordan  Air Jordan IX (9) Retro	Man     11      3           3
     * @apiExample 使用表
     *  com_mt_value_channel
     *  cms_bt_increment_stock_separate_task
     *  cms_bt_increment_stock_separate_item
     *
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK_DETAIL.IMPORT_STOCK_INFO)
    public AjaxResponse importStockInfo(@RequestBody Map param) {

        // 返回
        return success(null);
    }


    /**
     * @api {post} /cms/promotion/task_stock/executeIncrementStockSeparation 3.6 启动/重刷增量库存隔离
     * @apiName CmsTaskIncrementStockDetailController.executeIncrementStockSeparation
     * @apiDescription 启动/重刷增量库存隔离
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
     * @apiExample  业务说明
     *  将状态为"0:未进行"，"3:增量失败"的增量隔离明细进行处理，将其状态变为"1:等待增量"。等待Batch处理。
     *
     * @apiExample 使用表
     *  cms_bt_stock_separate_platform_info
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.INCREMENT_STOCK_DETAIL.EXECUTE_INCREMENT_STOCK_SEPARATION)
    public AjaxResponse executeIncrementStockSeparation(@RequestBody Map param) {

        // 返回
        return success(null);
    }
}
