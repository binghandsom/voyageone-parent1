package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jeff.duan on 2016/02/29.
 */
@RestController
@RequestMapping(
        value = CmsUrlConstants.PROMOTION.TASK.STOCK_INCREMENT_DETAIL.ROOT,
        method = RequestMethod.POST
)
public class CmsTaskStockIncrementDetailController extends CmsController {

    @Autowired
    private CmsTaskStockService cmsTaskStockService;

    @Autowired
    private CmsTaskStockIncrementDetailService cmsTaskStockIncrementDetailService;

    /**
     * @api {post} /cms/promotion/task_stock_increment_detail/searchItem 3.1 检索增量隔离明细
     * @apiName CmsTaskStockIncrementDetailController.searchItem
     * @apiDescription 检索增量隔离明细
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (系统级参数) {String} channelId 渠道id
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiParam (应用级参数) {String} cartId 平台id
     * @apiParam (应用级参数) {String} subTaskId 增量任务id
     * @apiParam (应用级参数) {String} model 商品model
     * @apiParam (应用级参数) {String} code 商品Code
     * @apiParam (应用级参数) {String} sku Sku
     * @apiParam (应用级参数) {String} status 状态（0：未进行； 1：等待隔离； 2：隔离成功； 3：隔离失败； 空白:ALL）
     * @apiParam (应用级参数) {Object} propertyList 属性列表（json数组）
     * @apiParamExample  propertyList参数示例
     *   "propertyList": [ {"property":"property1", "name":"品牌", "logic":"", "type":"", "show":false, "value"=""},
     *                     {"property":"property2", "name":"英文短描述", "logic":"Like", "type":"", "show":false, "value"=""},
     *                     {"property":"property3", "name":"性别", "logic":"", "type":"", "show":false, "value"=""}，
     *                     {"property":"property4", "name":"Size", "logic":"", "type":"int", "show":false, "value"=""}]
     * @apiParam (应用级参数) {String} start 检索开始Index
     * @apiParam (应用级参数) {String} length 检索件数
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {String} allNum 总数
     * @apiSuccess (应用级返回字段) {String} readyNum 未进行数
     * @apiSuccess (应用级返回字段) {String} waitIncrementNum 等待增量数
     * @apiSuccess (应用级返回字段) {String} increasingNum 增量中数
     * @apiSuccess (应用级返回字段) {String} incrementSuccessNum 增量成功数
     * @apiSuccess (应用级返回字段) {String} incrementFailureNum 增量失败数
     * @apiSuccess (应用级返回字段) {String} revertNum 还原数
     * @apiSuccess (应用级返回字段) {String} skuNum sku数
     * @apiSuccess (应用级返回字段) {String} cartId 平台id
     * @apiSuccess (应用级返回字段) {String} cartName 平台名
     * @apiSuccess (应用级返回字段) {String} taskId 任务id
     * @apiSuccess (应用级返回字段) {Object} propertyList 属性列表（json数组）
     * @apiSuccess (应用级返回字段) {Object} stockList 库存隔离明细（json数组）
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "allNum":10000,
     *   "readyNum":1000,
     *   "waitIncrementNum":5000,
     *   "increasingNum":4000,
     *   "incrementSuccessNum":1000,
     *   "incrementFailureNum":0,
     *   "revertNum":0,
     *   "skuNum":2000,
     *   "cartId":"23",
     *   "cartName":"天猫国际",
     *   "taskId":"1",
     *   "propertyList": [ {"property":"property1", "name":"品牌", "logic":"", "type":"", "show":false, "value"=""},
     *                     {"property":"property2", "name":"英文短描述", "logic":"Like", "type":"", "show":false, "value"=""},
     *                     {"property":"property3", "name":"性别", "logic":"", "type":"", "show":false, "value"=""}，
     *                     {"property":"property4", "name":"Size", "logic":"", "type":"int", "show":false, "value"=""}],
     *   "stockList": [ {"model":"35265", "code":"35265465", "sku":"256354566-9", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":"50", "incrementQty":"50", "status":"未进行", "fixFlg":false},
     *                   {"model":"35265", "code":"35265465", "sku":"256354566-10", "property1":"Puma", "property2":"Puma Suede Classic +Puma Suede Classic+", "property3":"women", "property4":"10", "qty":"80", "incrementQty":"80", "status":"未进行", "fixFlg":false},
     *                   {"model":"35265", "code":"35265465", "sku":"256354566-11", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":"20", "incrementQty":"20", "status":"增量成功", "fixFlg":false},
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
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK_INCREMENT_DETAIL.SEARCH_ITEM)
    public AjaxResponse searchItem(@RequestBody Map<String, Object> param) {
        // 渠道id
        param.put("channelId", this.getUser().getSelChannelId());
        // 语言
        param.put("lang", this.getLang());
        // 返回内容
        Map<String, Object> resultBean = new HashMap<>();
        // 子任务id
        String subTaskId =(String) param.get("subTaskId");

        // 根据子任务id取得任务信息 只有首次取得
        if(StringUtils.isEmpty((String) param.get("taskId"))) {
            Map<String, Object> taskInfo = cmsTaskStockIncrementDetailService.getTaskInfo(subTaskId, this.getUser().getSelChannelId(), this.getLang());
            // 任务id取得失败的情况下，认为没有权限
            if (taskInfo == null) {
                resultBean.put("stockList", new ArrayList());
//                resultBean.put("hasAuthority", false);
                return success(resultBean);
            } else {
//                // 增量任务/渠道id权限check
//                boolean hasAuthority = cmsTaskStockIncrementDetailService.hasAuthority(String.valueOf(taskInfo.get("task_id")), String.valueOf(taskInfo.get("cart_id")), this.getUser().getSelChannelId(), this.getLang());
//                resultBean.put("hasAuthority", hasAuthority);
//                if (!hasAuthority) {
//                    return success(resultBean);
//                }
                // 任务id
                resultBean.put("taskId", String.valueOf(taskInfo.get("task_id")));
                param.put("taskId", String.valueOf(taskInfo.get("task_id")));
                // 平台id
                resultBean.put("cartId", String.valueOf(taskInfo.get("cart_id")));
                param.put("cartId", String.valueOf(taskInfo.get("cart_id")));
                // 平台名
                resultBean.put("cartName", taskInfo.get("name"));
            }
        } else {
            resultBean.put("taskId", param.get("taskId"));
            resultBean.put("cartId", param.get("cartId"));
            resultBean.put("cartName", param.get("cartName"));
//            resultBean.put("hasAuthority", true);
        }

        // 取得属性列表 只有首次取得
        if (param.get("propertyList") == null || ((List<Map<String, Object>>)param.get("propertyList")).isEmpty()) {
            List<Map<String, Object>> propertyList = cmsTaskStockService.getPropertyList(this.getUser().getSelChannelId(), this.getLang());
            resultBean.put("propertyList", propertyList);
            param.put("propertyList", propertyList);
        } else {
            resultBean.put("propertyList", param.get("propertyList"));
        }

        // 子任务id对应的库存隔离数据是否移到history表
        boolean historyFlg = cmsTaskStockIncrementDetailService.isHistoryExist(subTaskId);
        if (historyFlg) {
            param.put("tableNameSuffix", "_history");
        } else {
            param.put("tableNameSuffix", "");
        }

        // 取得增量库存隔离数据各种状态的数量(翻页是不计算)
        if (!(boolean) param.get("page")) {
            // 所有增量库存隔离明细的计数
            int cntAll = 0;
            // 画面的sku数
            int skuNum = 0;
            // 画面条件选择的status
            String currentStatus = (String) param.get("status");
            List<Map<String, Object>> statusCountList = cmsTaskStockIncrementDetailService.getStockStatusCount(param);
            for (Map<String, Object> statusInfo : statusCountList) {
                String status = (String) statusInfo.get("status");
                int cnt = Integer.parseInt(String.valueOf(statusInfo.get("count")));
                cntAll += cnt;
                String key = "";
                if (CmsTaskStockService.STATUS_READY.equals(status)) {
                    key = "readyNum";
                } else if (CmsTaskStockService.STATUS_WAITING_INCREMENT.equals(status)) {
                    key = "waitIncrementNum";
                } else if (CmsTaskStockService.STATUS_INCREASING.equals(status)) {
                    key = "increasingNum";
                } else if (CmsTaskStockService.STATUS_INCREMENT_SUCCESS.equals(status)) {
                    key = "incrementSuccessNum";
                } else if (CmsTaskStockService.STATUS_INCREMENT_FAIL.equals(status)) {
                    key = "incrementFailureNum";
                } else if (CmsTaskStockService.STATUS_REVERT.equals(status)) {
                    key = "revertNum";
                }
                resultBean.put(key, cnt);
                // 设置画面的sku数
                if (status.equals(currentStatus)) {
                    skuNum = cnt;
                }
            }
            if (StringUtils.isEmpty(currentStatus)) {
                skuNum = cntAll;
            }
            if (!resultBean.containsKey("readyNum")) {
                resultBean.put("readyNum", 0);
            }
            if (!resultBean.containsKey("waitIncrementNum")) {
                resultBean.put("waitIncrementNum", 0);
            }
            if (!resultBean.containsKey("increasingNum")) {
                resultBean.put("increasingNum", 0);
            }
            if (!resultBean.containsKey("incrementSuccessNum")) {
                resultBean.put("incrementSuccessNum", 0);
            }
            if (!resultBean.containsKey("incrementFailureNum")) {
                resultBean.put("incrementFailureNum", 0);
            }
            if (!resultBean.containsKey("revertNum")) {
                resultBean.put("revertNum", 0);
            }

            // 总数
            resultBean.put("allNum", cntAll);

            // sku数
            resultBean.put("skuNum", skuNum);

            // 条件结果为0件的情况下，直接返回
            if (skuNum == 0) {
                resultBean.put("stockList", new ArrayList());
                return success(resultBean);
            }
        }

        // 取得增量库存隔离明细
        List<Map<String, Object>> stockList = cmsTaskStockIncrementDetailService.getStockList(param);
        resultBean.put("stockList", stockList);

        // 返回
        return success(resultBean);
    }

    /**
     * @api {post} /cms/promotion/task_stock_increment_detail/saveItem 3.2 保存一条增量隔离库存明细
     * @apiName CmsTaskStockIncrementDetailController.saveItem
     * @apiDescription 保存一条增量隔离库存明细
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiParam (应用级参数) {String} cartId 平台id
     * @apiParam (应用级参数) {String} subTaskId 增量任务id
     * @apiParam (应用级参数) {Object} stockInfo（json数组） 增量库存隔离明细
     * @apiParamExample  stockInfo 示例
     * {
     *  "stockInfo":  {"model":"35265", "code":"35265465", "sku":"256354566-9", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":"50", "incrementQty":"50", "status":"未进行", "fixFlg":false}
     * }
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":"0", "message":null, "displayType":null, "redirectTo":null, "data":null
     *  }
     * @apiErrorExample  错误示例
     * {
     *  "code": "1", "message": "保存失败（增量库存隔离后不能进行变更）", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  1.check输入信息。增量隔离库存必须是大于0的整数。
     *                   如果状态="1:等待增量","2：增量中","3：增量成功","4：增量失败","5：还原"，则不能进行修改。
     *  2.如果修改的增量隔离库存<>cms_bt_increment_stock_separate_item表里对应的值，那么更新cms_bt_increment_stock_separate_item表的值。
     * @apiExample 使用表
     *  cms_bt_increment_stock_separate_item
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK_INCREMENT_DETAIL.SAVE_ITEM)
    public AjaxResponse saveItem(@RequestBody Map param) {
        // 创建者/更新者用
        param.put("userName", this.getUser().getUserName());
        // 增量隔离库存明细
        cmsTaskStockIncrementDetailService.saveItem(param);

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock_increment_detail/delItem 3.3 删除一条增量隔离库存明细
     * @apiName CmsTaskStockIncrementDetailController.delItem
     * @apiDescription 删除一条增量隔离库存明细
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiParam (应用级参数) {String} cartId 平台id
     * @apiParam (应用级参数) {String} subTaskId 增量任务id
     * @apiParam (应用级参数) {String} sku Sku
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
     *  如果选择的增量隔离明细的状态为"1:等待增量","2：增量中","3：增量成功","4：增量失败","5：还原"则不能删除这条明细，否则删除这条增量隔离明细。
     * @apiExample 使用表
     *  cms_bt_increment_stock_separate_item
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK_INCREMENT_DETAIL.DEL_ITEM)
    public AjaxResponse delItem(@RequestBody Map<String, Object> param) {

        // 删除增量隔离库存明细
        cmsTaskStockIncrementDetailService.delItem((String) param.get("taskId"), (String) param.get("subTaskId"), (String) param.get("cartId"), (String) param.get("sku"));

        // 返回
        return success(null);
    }

    /**
     * @api {post} /cms/promotion/task_stock_increment_detail/exportStockInfo 3.4 批量导出增量隔离库存明细
     * @apiName CmsTaskStockIncrementDetailController.exportStockInfo
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
     *  Model    Code        Sku	            品牌        Name(英文)              性别    SIZE    可调配库存  天猫   固定值增量
     *  302370   302370-013  302370-013-10   Air Jordan  Air Jordan IX (9) Retro	Women   10      13          12     yes
     *  302370   302370-013  302370-013-10.5 Air Jordan  Air Jordan IX (9) Retro	Women   10.5    10          9
     *  302370   302370-013  302370-013-11   Air Jordan  Air Jordan IX (9) Retro	Man     11      3           3
     * @apiExample 使用表
     *  cms_bt_stock_separate_increment_item
     *  com_mt_value_channel
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK_INCREMENT_DETAIL.EXPORT_STOCK_INFO)
    public ResponseEntity exportStockInfo(@RequestParam Map<String, Object> param) {

        Map<String, Object> searchParam = new HashMap<>();
        // 渠道id
        searchParam.put("channelId", this.getUser().getSelChannelId());
        // 语言
        searchParam.put("lang", this.getLang());

        // 任务id对应的增量隔离库存数据是否移到history表
        boolean historyFlg = cmsTaskStockIncrementDetailService.isHistoryExist((String) param.get("task_id"));
        if (historyFlg) {
            searchParam.put("tableName", "voyageone_cms2.cms_bt_stock_separate_increment_item_history");
        } else {
            searchParam.put("tableName", "voyageone_cms2.cms_bt_stock_separate_increment_item");
        }

        searchParam.put("taskId", param.get("taskId"));
        searchParam.put("subTaskId", param.get("subTaskId"));
        searchParam.put("model", param.get("model"));
        searchParam.put("code", param.get("code"));
        searchParam.put("sku", param.get("sku"));
        searchParam.put("status", param.get("status"));
        searchParam.put("cartId", param.get("cartId"));
        searchParam.put("cartName", param.get("cartName"));

        String propertyList = (String) param.get("propertyList");
        searchParam.put("propertyList", JacksonUtil.json2Bean(propertyList, List.class));

        byte[] data;
        try {
            data = cmsTaskStockIncrementDetailService.getExcelFileStockIncrementInfo(searchParam);
        } catch (Exception e) {
            $error(e.getMessage());
            // 导出异常
            throw new BusinessException("7000069");
        }

        // 返回
        return genResponseEntityFromBytes("StockIncrementInfo_" + DateTimeUtil.getLocalTime(getUserTimeZone(), DateTimeUtil.DATE_TIME_FORMAT_2)+".xlsx", data);
    }


    /**
     * @api {post} /cms/promotion/task_stock_increment_detail/importStockInfo 3.5 批量导入增量隔离库存明细
     * @apiName CmsTaskStockIncrementDetailController.importStockInfo
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
     *      1.0 如果在cms_bt_stock_separate_increment_item表中，这个增量任务有状态<>0:未进行的数据，则不允许导入
     *      1.1 Code和Sku都不能为空白。
     *      1.2 所有字段的长度check。
     *      1.3 根据参数.渠道id从com_mt_value_channel表（type=62）取得有几个属性（例如 N个）
     *      1.4 第一行的第3列开始的属性名称必须和1.3取得的属性名称一致。
     *      1.5 第一行的第N+5列（平台信息）必须和任务所对应的平台匹配。
     *      1.6 第二行开始，第N+4列必须和cms_bt_stock_separate_increment_item表的可用库存相同。
     *      1.7 第二行开始，第N+5列的内容必须是大于0的整数。（增量库存列）
     *      1.8 第二行开始，第N+6列的内容必须是"yes"或者空白。（固定值增量列）
     *
     *  2.将导入的增量库存隔离数据插入到cms_bt_stock_separate_increment_item表。
     *    2.1 如果这条增量明细信息（参数.增量任务id + sku）在cms_bt_stock_separate_increment_item表中不存在，则追加。
     *    2.2 如果这条增量明细信息（参数.增量任务id + sku）在cms_bt_stock_separate_increment_item表中存在，则更新增量库存和是否固定值增量两个字段。
     *    2.3 删除怎么办？
     *
     *  导入文件示例
     *  Model    Code        Sku	            品牌        Name(英文)              性别    SIZE    可调配库存  天猫   固定值增量
     *  302370   302370-013  302370-013-10   Air Jordan  Air Jordan IX (9) Retro	Women   10      13          12     yes
     *  302370   302370-013  302370-013-10.5 Air Jordan  Air Jordan IX (9) Retro	Women   10.5    10          9
     *  302370   302370-013  302370-013-11   Air Jordan  Air Jordan IX (9) Retro	Man     11      3           3
     * @apiExample 使用表
     *  com_mt_value_channel
     *  cms_bt_stock_separate_increment_task
     *  cms_bt_stock_separate_increment_item
     *
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK_INCREMENT_DETAIL.IMPORT_STOCK_INFO)
    public AjaxResponse importStockInfo(@RequestParam Map<String, Object> param, @RequestParam MultipartFile file) {
        // 返回内容
        Map<String, Object> resultBean = new HashMap<>();

        // 渠道id
        param.put("channelId", this.getUser().getSelChannelId());
        // 创建者/更新者用
        param.put("userName", this.getUser().getUserName());
        // import Excel
        cmsTaskStockIncrementDetailService.importExcelFileStockIncrementInfo(param, file, resultBean);
        // 返回
        return success(resultBean);
    }


    /**
     * @api {post} /cms/promotion/task_stock_increment_detail/executeStockIncrementSeparation 3.6 启动/重刷增量库存隔离
     * @apiName CmsTaskStockIncrementDetailController.executeStockIncrementSeparation
     * @apiDescription 启动/重刷增量库存隔离
     * @apiGroup promotion
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} taskId 任务id
     * @apiParam (应用级参数) {String} cartId 平台id
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
     *  将状态为"0:未进行"，"4:增量失败"的增量隔离明细进行处理，将其状态变为"1:等待增量"。等待Batch处理。
     *
     * @apiExample 使用表
     *  cms_bt_stock_separate_platform_info
     *
     */
    @RequestMapping(CmsUrlConstants.PROMOTION.TASK.STOCK_INCREMENT_DETAIL.EXECUTE_STOCK_INCREMENT_SEPARATION)
    public AjaxResponse executeStockIncrementSeparation(@RequestBody Map<String, Object> param) {

        // 创建者/更新者用
        param.put("userName", this.getUser().getUserName());

        // 启动/重刷增量库存隔离
        cmsTaskStockIncrementDetailService.executeStockIncrementSeparation(param);

        // 返回
        return success(null);
    }
}
