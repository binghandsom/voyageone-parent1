package com.voyageone.web2.cms.views.channel;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiang on 2016/2/24.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM_VALUE.ROOT)
public class CmsFeedCustPropValueController extends CmsController {

    @Autowired
    private CmsFeedCustPropService cmsFeedCustPropService;

    /**
     * @api {get} /cms/channel/custom/value/get 5. 获取Feed自定义属性值一览
     * @apiName getFeedCustPropValueList
     * @apiDescription 获取Feed自定义属性值一览
     * @apiGroup channel
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} cat_path 类目路径（为'0'时表示查询共通属性，不设值时表示查询所有，即包含共通属性和所有类目）
     * @apiParam (应用级参数) {String} sts 翻译状态（为'1'时表示查询已翻译的属性值，为'0'时表示查询未翻译的属性值）
     * @apiParam (应用级参数) {String} propName 属性名（已翻译或未翻译的属性名）
     * @apiParam (应用级参数) {String} propValue 属性值（已翻译或未翻译的属性值）
     * @apiParam (应用级参数) {Integer} skip 翻页用参数，显示起始序号
     * @apiParam (应用级参数) {Integer} limit 翻页用参数，每页显示数据条数
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {Integer} total 查询结果总数
     * @apiSuccess (应用级返回字段) {Object[]} resultData 当前页的属性值列表（json数组），没有数据时返回空数组
     * @apiSuccessExample 成功响应查询请求
     * {
     *  "code":null, "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "total": 68,
     *   "resultData": [ {"cat_path":"类目1", "prop_id":"10", "prop_original":"gravity", "prop_translation":"重量", "value_id":"101", "value_original":"kg", "value_translation":"公斤", "ref_value":"公斤"},
     *               {"cat_path":"类目1", "prop_id":"10", "prop_original":"gravity", "prop_translation":"重量", "value_id":"102", "value_original":"lb", "value_translation":"磅", "ref_value":"磅"}...]
     *  }
     * }
     * @apiExample  业务说明
     *  获取Feed自定义属性值一览
     *  返回值中项目"cat_path"为'0'时，画面列表上显示为"共通属性"
     * @apiExample 使用表
     *  使用cms_bt_feed_custom_prop表
     *  使用cms_bt_feed_custom_prop_value表
     * @apiSampleRequest off
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM_VALUE.INIT, method = RequestMethod.GET)
    public AjaxResponse getFeedCustPropValueList(@RequestParam Map<String, String> params) {
        logger.debug("getFeedCustPropValueList() >>>> start");
        logger.debug("getFeedCustPropValueList() >>>> params" + params.toString());
        String catPath = StringUtils.trimToNull(params.get("cat_path"));
        int tSts = NumberUtils.toInt(params.get("sts"));
        String propName = StringUtils.trimToNull(params.get("propName"));
        String propValue = StringUtils.trimToNull(params.get("propValue"));
        if (tSts == 0) {
            // 查询未翻译的属性值时，不需要输入属性值
            propValue = null;
        }
        int skip = NumberUtils.toInt(params.get("skip"));
        int limit = NumberUtils.toInt(params.get("limit"));
        UserSessionBean userInfo = getUser();
        List<Map<String, Object>> rslt1 = null;

        if (catPath == null) {
            // 查询所有属性
            rslt1 = cmsFeedCustPropService.selectPropValue("0", tSts, propName, propValue, userInfo.getSelChannelId());
            List<Map<String, Object>> rslt2 = cmsFeedCustPropService.selectPropValue(null, tSts, propName, propValue, userInfo.getSelChannelId());
            if (rslt1 == null) {
                if (rslt2 == null) {
                   rslt1 = new ArrayList<Map<String, Object>>(0);
                } else {
                    rslt1 = rslt2;
                }
            } else {
                if (rslt2 != null) {
                    rslt1.addAll(rslt2);
                }
            }
        } else {
            // 查询共通属性及类目属性
            rslt1 = cmsFeedCustPropService.selectPropValue(catPath, tSts, propName, propValue, userInfo.getSelChannelId());
            if (rslt1 == null) {
                rslt1 = new ArrayList<Map<String, Object>>(0);
            }
        }
        HashMap dataMap = new HashMap();
        int listCnt = rslt1.size();
        int endIdx = skip + limit;
        if (listCnt < endIdx) {
            endIdx = listCnt;
        }
        dataMap.put("total", listCnt);
        dataMap.put("resultData", rslt1.subList(skip, endIdx));
        AjaxResponse resp = success(null);
        resp.setData(dataMap);
        return resp;
    }

    /**
     * @api {post} /cms/channel/custom/value/create 6. 新增Feed自定义属性值
     * @apiName addFeedCustPropValue
     * @apiDescription 新增Feed自定义属性值
     * @apiGroup channel
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} prop_id 属性名ID（必须项）
     * @apiParam (应用级参数) {String} value_original 未翻译的属性值（必须项）
     * @apiParam (应用级参数) {String} value_translation 已翻译的属性值
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code": "0", "message":null, "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiErrorExample  错误示例1
     * {
     *  "code": "1", "message": "参数错误/缺少参数", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiErrorExample  错误示例2
     * {
     *  "code": "2", "message": "重复翻译的属性值", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  新增Feed自定义属性值
     *  新增时，翻译前的属性值不能重复（相同属性名下）
     * @apiExample 使用表
     *  使用cms_bt_feed_custom_prop_value表
     * @apiSampleRequest off
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM_VALUE.ADD, method = RequestMethod.POST)
    public AjaxResponse addFeedCustPropValue(@RequestBody Map<String, String> params) {
        logger.debug("addFeedCustPropValue() >>>> start");
        logger.debug("addFeedCustPropValue() >>>> params" + params.toString());
        int propId = NumberUtils.toInt(params.get("prop_id"));
        String origValue = StringUtils.trimToNull(params.get("value_original"));
        String transValue = StringUtils.trimToEmpty(params.get("value_translation"));
        if (propId == 0 || origValue == null) {
            // 缺少参数
            AjaxResponse resp = success(null);
            resp.setCode("1");
            resp.setMessage("参数错误/缺少参数");
            return resp;
        }

        UserSessionBean userInfo = getUser();
        // 先判断该属性值是否已存在
        if (cmsFeedCustPropService.isPropValueExist(propId, userInfo.getSelChannelId(), origValue)) {
            AjaxResponse resp = success(null);
            resp.setCode("2");
            resp.setMessage("重复翻译的属性值");
            return resp;
        }

        int rslt = cmsFeedCustPropService.addPropValue(propId, userInfo.getSelChannelId(), origValue, transValue, userInfo.getUserName());
        if (rslt == 0) {
            logger.error("新增翻译后的属性值不成功");
        } else {
            logger.debug("新增翻译后的属性值成功");
        }
        AjaxResponse resp = success(null);
        resp.setCode("0");
        return resp;
    }

    /**
     * @api {post} /cms/channel/custom/value/update 7. 保存Feed自定义属性值
     * @apiName saveFeedCustPropValue
     * @apiDescription 保存Feed自定义属性值
     * @apiGroup channel
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {Integer} value_id 属性值ID（必须项）
     * @apiParam (应用级参数) {String} value_translation 已翻译的属性值（为空时表示清空其已翻译的属性值）
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code": "0", "message":null, "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiErrorExample  错误示例1
     * {
     *  "code": "1", "message": "参数错误/缺少参数", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiErrorExample  错误示例2
     * {
     *  "code": "2", "message": "该属性值不存在", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  保存Feed自定义属性值
     * @apiExample 使用表
     *  使用cms_bt_feed_custom_prop_value表
     * @apiSampleRequest off
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM_VALUE.SAVE, method = RequestMethod.POST)
    public AjaxResponse saveFeedCustPropValue(@RequestBody Map<String, String> params) {
        logger.debug("saveFeedCustPropValue() >>>> start");
        logger.debug("saveFeedCustPropValue() >>>> params" + params.toString());
        int valueId = NumberUtils.toInt(params.get("value_id"));
        String transValue = StringUtils.trimToEmpty(params.get("value_translation"));
        if (valueId == 0) {
            // 缺少参数
            AjaxResponse resp = success(null);
            resp.setCode("1");
            resp.setMessage("参数错误/缺少参数");
            return resp;
        }

        UserSessionBean userInfo = getUser();
        // 先判断该属性值是否已存在
        if (!cmsFeedCustPropService.isPropValueExist(valueId)) {
            AjaxResponse resp = success(null);
            resp.setCode("2");
            resp.setMessage("该属性值不存在");
            return resp;
        }

        int rslt = cmsFeedCustPropService.updatePropValue(valueId, transValue, userInfo.getUserName());
        if (rslt == 0) {
            logger.error("更新翻译后的属性值不成功");
        } else {
            logger.debug("更新翻译后的属性值成功");
        }
        AjaxResponse resp = success(null);
        resp.setCode("0");
        return resp;
    }
}
