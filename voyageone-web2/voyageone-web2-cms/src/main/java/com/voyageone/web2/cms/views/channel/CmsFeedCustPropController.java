package com.voyageone.web2.cms.views.channel;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.views.mapping.feed.CmsFeedMappingService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by jiang on 2016/2/24.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM_PROP.ROOT, method = RequestMethod.POST)
public class CmsFeedCustPropController extends CmsController {

    @Autowired
    private CmsFeedMappingService cmsFeedMappingService;
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
        logger.debug("getFeedCustProp() >>>> start");
        logger.debug("getFeedCustProp() >>>> params" + params.toString());
        String catPath = StringUtils.trimToNull(params.get("cat_path"));
        if (catPath == null) {
            // 缺少参数
            logger.warn("getFeedCustProp() >>>> 缺少catPath参数");
            throw new BusinessException("1", "缺少参数", null);
        }

        UserSessionBean userInfo = getUser();
        int splitFlg = NumberUtils.toInt(params.get("unsplitFlg"), 0);
        if (splitFlg == 1) {
            // 合并为一个list输出
            List<Map<String, Object>> list = cmsFeedCustPropService.selectAllAttr(userInfo.getSelChannelId(), catPath);
            List<Map<String, Object>> valList = convertList(list, false, "0".equals(catPath));

            HashMap dataMap = new HashMap();
            dataMap.put("valList", valList);
            AjaxResponse resp = success(dataMap);
            return resp;

        } else {
            // 分为两个list输出
            List<Map<String, Object>> list1 = cmsFeedCustPropService.selectOrigProp(userInfo.getSelChannelId(), "0");
            List<Map<String, Object>> list2 = cmsFeedCustPropService.selectTransProp(userInfo.getSelChannelId(), "0");
            List<Map<String, Object>> valList = convertList(list2, true, true);
            List<Map<String, Object>> unvalList = convertList(list1, false, true);

            // 判断是否全店铺共通属性
            String commFlg = cmsFeedCustPropService.getSameAttr(userInfo.getSelChannelId());
            if (!"1".equals(commFlg)) {
                if (!"0".equals(catPath)) {
                    List<Map<String, Object>> initAttrList = null;
                    // 查询指定类目(从mongo来的原始数据)
                    List<Object> catgAttrList = cmsFeedCustPropService.selectCatAttr(userInfo.getSelChannelId());
                    if (catgAttrList != null && catgAttrList.size() > 0) {
                        List<Map> childList = (List<Map>)((Map) catgAttrList.get(0)).get("categoryTree");
                        if (childList != null && childList.size() > 0) {
                            attrMap = null;
                            getSubCatTree(childList, catPath);
                            if (attrMap != null) {
                                initAttrList = new ArrayList<Map<String, Object>>(attrMap.size());
                                Iterator iter = attrMap.keySet().iterator();
                                while (iter.hasNext()) {
                                    HashMap objMap = new HashMap();
                                    objMap.put("prop_id", "");
                                    objMap.put("prop_original", (String) iter.next());
                                    objMap.put("cat_path", "");
                                    initAttrList.add(objMap);
                                }
                            }
                        }
                    }
                    // 过滤已翻译的属性
                    List<Map<String, Object>> custArr1 = cmsFeedCustPropService.selectOrigProp(userInfo.getSelChannelId(), catPath);
                    List<Map<String, Object>> custArr2 = cmsFeedCustPropService.selectTransProp(userInfo.getSelChannelId(), catPath);
                    if (custArr1 == null) {
                        custArr1 = new ArrayList<Map<String, Object>>();
                    }
                    if (custArr2 == null) {
                        custArr2 = new ArrayList<Map<String, Object>>();
                    }
                    List<Map<String, Object>> valList2 = convertList(custArr2, true, false);
                    List<Map<String, Object>> unvalList2 = convertList(custArr1, false, false);

                    // 合并过滤后的未翻译自定义属性
                    filterList(valList2, initAttrList);
                    filterList(unvalList2, initAttrList);
                    if (initAttrList != null && initAttrList.size() > 0) {
                        unvalList2.addAll(initAttrList);
                    }

                    // 最后合并共通属性和自定义属性
                    filterList(valList2, valList);
                    filterList(unvalList2, unvalList);
                    valList.addAll(valList2);
                    unvalList.addAll(unvalList2);
                }
            }
            HashMap dataMap = new HashMap();
            dataMap.put("sameAttr", commFlg);
            dataMap.put("valList", valList);
            dataMap.put("unvalList", unvalList);
            AjaxResponse resp = success(dataMap);
            return resp;
        }
    }

    private List<Map<String, Object>> convertList(List<Map<String, Object>> inputList, boolean hasValue, boolean isComm) {
        List<Map<String, Object>> rslt = new ArrayList<Map<String, Object>>();
        if (inputList == null || inputList.size() == 0) {
            return rslt;
        }
        for (Map<String, Object> item : inputList) {
            HashMap objMap = new HashMap();
            objMap.put("prop_id", item.get("prop_id"));
            objMap.put("prop_original", item.get("feed_prop_original"));
            if (hasValue) {
                objMap.put("prop_translation", item.get("feed_prop_translation"));
            }
            if (isComm) {
                objMap.put("cat_path", "0");
            }
            rslt.add(objMap);
        }
        return rslt;
    }

    private void filterList(List<Map<String, Object>> list1, List<Map<String, Object>> list2) {
        if (list1 == null || list1.size() == 0) {
            return;
        }
        for (Map<String, Object> item : list1) {
            String propName = (String) item.get("prop_original");
            if (list2 != null && list2.size() > 0) {
                int i = 0;
                boolean hasValue = false;
                for (Map<String, Object> item2 : list2) {
                    String propName2 = (String) item2.get("prop_original");
                    if (propName.equals(propName2)) {
                        hasValue = true;
                        break;
                    }
                    i ++;
                }
                if (hasValue) {
                    list2.remove(i);
                }
            }
        }
    }

    Map attrMap = null;

    private void getSubCatTree(List<Map> childList, String catPath) {
        for (Map catItem : childList) {
            String isChild = null;
            Object chdFlg = catItem.get("isChild");
            if (chdFlg != null) {
                isChild = chdFlg.toString();
            }
            if ("1".equals(isChild)) {
                if (catPath.equals(catItem.get("path"))) {
                    attrMap = (Map) catItem.get("attribute");
                }
            } else {
                List<Map> subList = (List<Map>) catItem.get("child");
                getSubCatTree(subList, catPath);
            }
        }
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
        logger.debug("saveFeedCustProp() >>>> start");
        logger.debug("saveFeedCustProp() >>>> params" + params.toString());
        String catPath = StringUtils.trimToNull((String) params.get("cat_path"));
        if (catPath == null) {
            // 缺少参数
            logger.warn("saveFeedCustProp() >>>> 缺少catpath参数");
            throw new BusinessException("缺少参数");
        }

        List<Map<String, Object>> valList = (List<Map<String, Object>>) params.get("valList");
        List<Map<String, Object>> unvalList = (List<Map<String, Object>>) params.get("unvalList");
        if ((valList == null || valList.size() == 0) && (unvalList == null || unvalList.size() == 0)) {
            // 缺少参数
            logger.warn("saveFeedCustProp() >>>> 缺少属性相关参数");
            throw new BusinessException("缺少参数");
        }
        UserSessionBean userInfo = getUser();
        List<Map<String, Object>> addList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> updList = new ArrayList<Map<String, Object>>();
        String propId = null;
        String cat_path = null;

        if (valList != null) {
            for (Map<String, Object> item : valList) {
                cat_path = StringUtils.trimToNull((String) item.get("cat_path"));
                Object propIdObj = item.get("prop_id");
                propId = null;
                if (propIdObj != null) {
                    propId = StringUtils.trimToNull(propIdObj.toString());
                }
                if (propId == null) {
                    if ("0".equals(catPath) && "0".equals(cat_path)) {
                        // 新增属性,只能新增共通属性
                        if (cmsFeedCustPropService.isAttrExist(item, catPath, userInfo.getSelChannelId())) {
                            logger.warn("该属性亦存在 " + item.toString());
                        } else {
                            addList.add(item);
                        }
                    }
                } else {
                    // 修改属性
                    if ("0".equals(catPath) && "0".equals(cat_path)) {
                        updList.add(item);
                    } else if (!"0".equals(catPath) && !"0".equals(cat_path) && catPath.equals(cat_path)) {
                        updList.add(item);
                    }
                }
            }
        }
        if (unvalList != null) {
            for (Map<String, Object> item : unvalList) {
                item.put("prop_translation", "");
                cat_path = StringUtils.trimToNull((String) item.get("cat_path"));
                Object propIdObj = item.get("prop_id");
                propId = null;
                if (propIdObj != null) {
                    propId = StringUtils.trimToNull(propIdObj.toString());
                }
                if (propId == null) {
                    if ("0".equals(catPath) && "0".equals(cat_path)) {
                        // 新增属性,只能新增共通属性
                        if (cmsFeedCustPropService.isAttrExist(item, catPath, userInfo.getSelChannelId())) {
                            logger.warn("该属性亦存在 " + item.toString());
                        } else {
                            addList.add(item);
                        }
                    }
                } else {
                    // 修改属性
                    if ("0".equals(catPath) && "0".equals(cat_path)) {
                        updList.add(item);
                    } else if (!"0".equals(catPath) && !"0".equals(cat_path) && catPath.equals(cat_path)) {
                        updList.add(item);
                    }
                }
            }
        }
        cmsFeedCustPropService.saveAttr(addList, updList, catPath, userInfo);
        AjaxResponse resp = success(null);
        return resp;
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
        HashMap dataMap = new HashMap(1);
        dataMap.put("categoryTree", cmsFeedCustPropService.getTopCategories(getUser()));
        AjaxResponse resp = success(dataMap);
        return resp;
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
        HashMap dataMap = new HashMap(1);
        List<CmsMtFeedCategoryModel> topTree = cmsFeedCustPropService.getTopFeedCategories(getUser());
        List<CmsMtFeedCategoryModel> rsltList = new ArrayList<CmsMtFeedCategoryModel>();
        CmsMtFeedCategoryModel comMdl = new CmsMtFeedCategoryModel();
        comMdl.setPath("0");
        comMdl.setName("共通属性");
        comMdl.setCid("共通属性");
        rsltList.add(comMdl);
        getSubCatTree2List(topTree, rsltList);
        for (CmsMtFeedCategoryModel catItem : rsltList) {
            catItem.setChild(null);
            catItem.setAttribute(null);
        }

        dataMap.put("categoryList", rsltList);
        AjaxResponse resp = success(dataMap);
        return resp;
    }

    private void getSubCatTree2List(List<CmsMtFeedCategoryModel> childList, List<CmsMtFeedCategoryModel> rsltList) {
        for (CmsMtFeedCategoryModel catItem : childList) {
            rsltList.add(catItem);
            if (catItem.getIsChild() == 0) {
                getSubCatTree2List(catItem.getChild(), rsltList);
            }
        }
    }
}
