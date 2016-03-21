package com.voyageone.web2.cms.views.channel;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiang on 2016/2/24.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM_PROP.ROOT, method = RequestMethod.POST)
public class CmsFeedCustPropController extends CmsController {

    @Autowired
    private CmsFeedCustPropService cmsFeedCustPropService;

    /**
     * @api {get} /cms/channel/custom/prop/get 1. 获取Feed自定义属性
     * @apiName getFeedCustProp
     * @apiDescription 获取Feed自定义属性
     * @apiGroup channel
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} cat_path 类目路径（必须项，为'0'时表示查询共通属性）
     * @apiParam (应用级参数) {String} unsplitFlg 输出区分（为'1'时表示不区分已翻译的属性名和未翻译的属性名，合并为一个list输出，
     *                                                      缺省时不用设值，表示已翻译的属性名和未翻译的属性名分为两个list输出）
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {String} sameAttr 全店铺共通属性标志（为'1'时表示全店铺共通属性）
     * @apiSuccess (应用级返回字段) {Object[]} valList 已翻译的属性名列表（json数组），没有数据时返回空数组
     * @apiSuccess (应用级返回字段) {Object[]} unvalList 未翻译的属性名列表（json数组），没有数据时返回空数组
     * @apiSuccessExample 成功响应查询请求（unsplitFlg未设值）
     * {
     *  "code":null, "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "sameAttr": "0",
     *   "valList": [ {"prop_id":"01", "prop_original":"size", "prop_translation":"尺寸", "cat_path":"0"}, {"prop_id":"02", "prop_original":"color", "prop_translation":"颜色"}...],
     *   "unvalList": [ {"prop_id":"03", "prop_original":"status", "cat_path":"0"}, {"prop_id":"04", "prop_original":"type"}...]
     *  }
     * }
     * 说明：当查询类目自定义属性时，输出的共通属性中包含"cat_path"字段，值为"0"，若是类目自定义属性则不包含该字段
     * @apiSuccessExample 成功响应查询请求（unsplitFlg='1'）
     * {
     *  "code":null, "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "valList": [ {"prop_id":"01", "prop_original":"size", "prop_translation":"尺寸", "cat_path":"0"}, {"prop_id":"02", "prop_original":"color", "prop_translation":""}...]
     *  }
     * }
     * @apiErrorExample  错误示例
     * {
     *  "code": "1", "message": "缺少参数", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  若该渠道有设置全店铺共通属性标志位，则只可查询共通属性，不可查询类目
     *  查询类目时，共通属性下的属性也要显示(不可编辑)，且要排在类目属性的前面，有重复属性的只显示类目属性
     *  查询类目时，未翻译列表从mongo:cms_mt_feed_category_tree中取得，已翻译列表从mysql:cms_bt_feed_custom_prop中取得，因为是从两个不同地方取数据，所以在显示未翻译列表时要作过滤，除去已翻译的部分
     * @apiExample 使用表
     *  使用 mysql: cms_bt_feed_custom_prop, cms_mt_channel_config 表
     *  使用 mongo: cms_mt_feed_category_tree 表
     * @apiSampleRequest off
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM_PROP.INIT)
    public AjaxResponse getFeedCustProp(@RequestBody Map<String, String> params) {
        String catPath = StringUtils.trimToNull((String) params.get("cat_path"));
        if (catPath == null) {
            throw new BusinessException("缺少类目路径");
        }
        return success(cmsFeedCustPropService.getFeedCustProp(params, getUser()));
    }

    /**
     * @api {post} /cms/channel/custom/prop/update 2. 保存Feed自定义属性
     * @apiName saveFeedCustProp
     * @apiDescription 保存Feed自定义属性，新增Feed自定义属性
     * @apiGroup channel
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String} cat_path 类目路径（必须项，为'0'时表示共通属性）
     * @apiParam (应用级参数) {Object[]} valList 已翻译的属性名列表（json数组）
     * @apiParam (应用级参数) {Object[]} unvalList 未翻译的属性名列表（json数组）
     * @apiParamExample  valList参数示例
     *  "valList": [ {"prop_id":"01", "prop_original":"size", "prop_translation":"尺寸", "cat_path":"0"}, {"prop_id":"02", "prop_original":"color", "prop_translation":"颜色"}...]
     *  说明：其中，若项目中"prop_id"为空，则表示该属性为新增Feed自定义属性（只能新增共通属性）
     *       若当前选择的是具体的类目时，则不可新增该类目下的属性，也不可修改共通属性的值
     * @apiParamExample  unvalList参数示例
     *  "unvalList": [ {"prop_id":"03", "prop_original":"status", "cat_path":"0"}, {"prop_id":"04", "prop_original":"type"}...]
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应更新请求
     * {
     *  "code":null, "message":null, "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiErrorExample  错误示例
     * {
     *  "code": "1", "message": "参数错误", "displayType":null, "redirectTo":null, "data":null
     * }
     * @apiExample  业务说明
     *  保存修改过的属性信息（因为前台提交的参数是两个list，分别对应于已翻译的属性名列表和未翻译的属性名列表，
     *    后台是把这两个list的数据直接写回数据库，已翻译的直接设置其翻译后的属性名，未翻译的清空其翻译后的属性名）
     *  删除属性时实际只清空对应的翻译后属性名，不删除本条属性数据
     *  参数列表中若项目"prop_id"为空时，表示该属性为新增属性
     *  类目选择状态时，不能修改共通属性(目前方案，以后可能会修改)
     * @apiExample 使用表
     *  使用cms_bt_feed_custom_prop表
     * @apiSampleRequest off
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM_PROP.SAVE)
    public AjaxResponse saveFeedCustProp(@RequestBody Map<String, Object> params) {
        String catPath = StringUtils.trimToNull((String) params.get("cat_path"));
        if (catPath == null) {
            throw new BusinessException("缺少类目路径");
        }

        List<Map<String, Object>> valList = (List<Map<String, Object>>) params.get("valList");
        List<Map<String, Object>> unvalList = (List<Map<String, Object>>) params.get("unvalList");
        if ((valList == null || valList.size() == 0) && (unvalList == null || unvalList.size() == 0)) {
            throw new BusinessException("缺少属性参数");
        }

        return success(cmsFeedCustPropService.saveFeedCustProp(params, getUser()));
    }

    /**
     * @api {get} /cms/channel/custom/prop/getCatTree 3. 获取类目一览（树型结构）
     * @apiName getCategoryTree
     * @apiDescription 获取类目一览（树型结构数据）
     * @apiGroup channel
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {Object[]} categoryTree 类目树（@see com.voyageone.cms.service.model.CmsMtFeedCategoryTreeModelx）
     *                                               没有数据时返回空数组
     * @apiSuccessExample 成功响应查询请求
     * {
     *  "code":null, "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "categoryTree": [ {"path":"a_b_c", "name":"yourname", "cid":"O0012", "isChild":1,
     *                      "child":#child子节点#, "attribute":#attribute子节点# }...]
     *  }
     * }
     * 其它说明：
     *  1。child子节点
     *     [categoryTree1, categoryTree2...] #为categoryTree循环嵌套结构
     *  2。attribute子节点 (只有当"isChild"=1时才会有attribute项目)
     *     { "Motor":[ "Belt driven"], "Replaceable Cartridge":[ "No1","No2","No3" ]...}
     *     #其中的key是不固定的，视具体数据而定
     * @apiExample  业务说明
     *  获取类目一览（树型结构数据）
     * @apiExample 使用表
     *  使用mongo:cms_mt_feed_category_tree表
     * @apiSampleRequest off
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM_PROP.GET_CAT_TREE)
    public AjaxResponse getCategoryTree() {
        Map<String, Object> result = new HashMap<>();
        result.put("categoryTree", cmsFeedCustPropService.getTopCategories(getUser()));
        return success(result);
    }

    /**
     * @api {get} /cms/channel/custom/prop/getCatList 4. 获取类目一览（数组）
     * @apiName getCategoryList
     * @apiDescription 获取类目一览（数组结构数据）
     * @apiGroup channel
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {Object[]} categoryList 类目列表（没有数据时返回空数组）
     * @apiSuccessExample 成功响应查询请求
     * {
     *  "code":null, "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "categoryList": [ {"path":"a_b_c", "name":"yourname", "cid":"O0012" }...]
     *  }
     * }
     * @apiExample  业务说明
     *  获取类目一览（数组结构数据）
     * @apiExample 使用表
     *  使用mongo:cms_mt_feed_category_tree表
     * @apiSampleRequest off
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM_PROP.GET_CAT_LIST)
    public AjaxResponse getCategoryList() {
        Map<String, Object> result = new HashMap<>();
        result.put("categoryList", cmsFeedCustPropService.getCategoryList(getUser()));
        return success(result);
    }
}
