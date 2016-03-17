package com.voyageone.web2.cms.views.search;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.search.index.CmsSearchInfoBean;
import com.voyageone.web2.cms.views.channel.CmsFeedCustPropService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 15/12/14
 */
@RestController
@RequestMapping(
        value = CmsUrlConstants.SEARCH.ADVANCE.ROOT,
        method = RequestMethod.POST
)
public class CmsSearchAdvanceController extends CmsController {

    @Autowired
    private CmsSearchAdvanceService searchIndexService;
    @Autowired
    private CmsFeedCustPropService cmsFeedCustPropService;

    /**
     * 初始化,获取master数据
     * @return
     */
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.INIT)
    public AjaxResponse init() throws Exception {
        return success(searchIndexService.getMasterData(getUser(), getLang()));
    }

    /**
     * 检索出group和product数据
     * @param params
     * @return
     */
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.SEARCH)
    public AjaxResponse search(@RequestBody CmsSearchInfoBean params) {
        Map<String, Object> resultBean = new HashMap<>();

        // 获取product列表
        List<CmsBtProductModel> productList = searchIndexService.getProductList(params, getUser(), getCmsSession());
        resultBean.put("productList", productList);
        long productListTotal = searchIndexService.getProductCnt(params, getUser(), getCmsSession());
        resultBean.put("productListTotal", productListTotal);

        // 获取group列表
        List<CmsBtProductModel> groupList = searchIndexService.getGroupList(params, getUser(), getCmsSession());
        resultBean.put("groupList", groupList);
        long groupListTotal = searchIndexService.getGroupCnt(params, getUser(), getCmsSession());
        resultBean.put("groupListTotal", groupListTotal);

        // 获取该用户自定义显示列设置
        Map<String, Object> colData = searchIndexService.getUserCustColumns(getUser().getUserId());
        if (colData != null) {
            resultBean.putAll(colData);
        }

        // 返回用户信息
        return success(resultBean);
    }

    /**
     * 分页检索group数据
     * @param params
     * @return
     */
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.GET_GROUP_LIST)
    public AjaxResponse getGroupList(@RequestBody CmsSearchInfoBean params) {

        Map<String, Object> resultBean = new HashMap<>();

        // 获取group列表
        List<CmsBtProductModel> groupList = searchIndexService.getGroupList(params, getUser(), getCmsSession());
        resultBean.put("groupList", groupList);
        long groupListTotal = searchIndexService.getGroupCnt(params, getUser(), getCmsSession());
        resultBean.put("groupListTotal", groupListTotal);

        // 返回用户信息
        return success(resultBean);
    }

    /**
     * 分页检索product数据
     * @param params
     * @return
     */
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.GET_PRODUCT_LIST)
    public AjaxResponse getProductList(@RequestBody CmsSearchInfoBean params) {

        Map<String, Object> resultBean = new HashMap<>();

        // 获取product列表
        List<CmsBtProductModel> productList = searchIndexService.getProductList(params, getUser(), getCmsSession());
        resultBean.put("productList", productList);
        long productListTotal = searchIndexService.getProductCnt(params, getUser(), getCmsSession());
        resultBean.put("productListTotal", productListTotal);

        // 返回用户信息
        return success(resultBean);
    }

    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.EXPORT_PRODUCTS)
    public ResponseEntity<byte[]> doExport(@RequestParam String params)
            throws Exception {

        CmsSearchInfoBean p = JacksonUtil.json2Bean(params,CmsSearchInfoBean.class);
        byte[] data = searchIndexService.getCodeExcelFile(p, getUser(), getCmsSession());
//        byte[] data = new byte[2];
        return genResponseEntityFromBytes("product_" + DateTimeUtil.getLocalTime(getUserTimeZone())+".xlsx", data);
    }

    /**
     * @api {post} /cms/search/advance/getCustColumnsInfo 取得自定义显示列设置
     * @apiName getCustColumnsInfo
     * @apiDescription 取得自定义显示列设置
     * @apiGroup search
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {Object[]} customProps 自定义显示列信息，没有数据时返回空数组
     * @apiSuccess (应用级返回字段) {Object[]} commonProps 共同属性显示列信息，没有数据时返回空数组
     * @apiSuccessExample 成功响应查询请求
     * {
     *  "code":null, "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "customProps": [ {"feed_prop_original":"a_b_c", "feed_prop_translation":"yourname" }...],
     *   "commonProps": [ {"propId":"a_b_c", "propName":"yourname" }...]
     *  }
     * }
     * @apiExample  业务说明
     *  取得自定义显示列设置
     * @apiExample 使用表
     *  使用cms_bt_feed_custom_prop表, cms_mt_common_prop表
     * @apiSampleRequest off
     */
    @RequestMapping("getCustColumnsInfo")
    public AjaxResponse getCustColumnsInfo() {
        Map<String, Object> resultBean = new HashMap<>();
        UserSessionBean userInfo = getUser();
        List<Map<String, Object>> list1 = cmsFeedCustPropService.selectAllAttr(userInfo.getSelChannelId(), "0");
        List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>(list1.size());
        for (Map<String, Object> obj : list1) {
            Map<String, Object> obj2 = new HashMap<String, Object>(2);
            obj2.put("feed_prop_original", obj.get("feed_prop_original"));
            obj2.put("feed_prop_translation", obj.get("feed_prop_translation"));
            list2.add(obj2);
        }

        resultBean.put("customProps", list2);
        resultBean.put("commonProps", searchIndexService.getCustColumns());

        // 获取该用户自定义显示列设置
        Map<String, Object> colData = searchIndexService.getUserCustColumns(userInfo.getUserId());
        if (colData != null) {
            resultBean.putAll(colData);
        }
        // 返回用户信息
        return success(resultBean);
    }

    /**
     * @api {post} /cms/search/advance/saveCustColumnsInfo 保存用户自定义显示列设置
     * @apiName saveCustColumnsInfo
     * @apiDescription 保存用户自定义显示列设置
     * @apiGroup search
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiParam (应用级参数) {String[]} customProps 自定义显示列信息，空数组表示该用户没有指定要显示的列
     * @apiParam (应用级参数) {String[]} commonProps 共同属性显示列信息，空数组表示该用户没有指定要显示的列
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccessExample 成功响应请求
     * {
     *  "code":null, "message":null, "displayType":null, "redirectTo":null,
     *  "data":null
     * }
     * @apiExample  业务说明
     *  保存用户自定义显示列设置
     *  当参数为空时将清除该用户原有设置
     * @apiExample 使用表
     *  使用ct_user_config表
     * @apiSampleRequest off
     */
    @RequestMapping("saveCustColumnsInfo")
    public AjaxResponse saveCustColumnsInfo(@RequestBody Map<String, Object> params) {
        List<String> customProps = (List<String>) params.get("customProps");
        List<String> commonProps = (List<String>) params.get("commonProps");
        String customStrs = StringUtils.trimToEmpty(StringUtils.join(customProps, ","));
        String commonStrs = StringUtils.trimToEmpty(StringUtils.join(commonProps, ","));

        searchIndexService.saveCustColumnsInfo(getUser().getUserId(), getUser().getUserName(), customStrs, commonStrs);
        // 返回用户信息
        return success(null);
    }
}
