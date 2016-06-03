package com.voyageone.web2.cms.views.search;

import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.impl.cms.product.ProductTagService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.bean.search.index.CmsSearchInfoBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private ProductTagService productTagService;

    /**
     * 初始化,获取master数据
     * @return
     */
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.INIT)
    public AjaxResponse init() throws Exception {
        CmsSessionBean cmsSession = getCmsSession();
        UserSessionBean userInfo = getUser();
        searchIndexService.getUserCustColumns(userInfo.getSelChannelId(), userInfo.getUserId(), cmsSession, getLang());
        return success(searchIndexService.getMasterData(userInfo, cmsSession, getLang()));
    }

    /**
     * 检索出group和product数据
     * @param params
     * @return
     */
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.SEARCH)
    public AjaxResponse search(@RequestBody CmsSearchInfoBean params) {
        Map<String, Object> resultBean = new HashMap<>();
        UserSessionBean userInfo = getUser();
        CmsSessionBean cmsSession = getCmsSession();

        // 获取product列表
        List<String> prodCodeList = searchIndexService.getProductCodeList(params, userInfo, cmsSession);
        // 分页
        int endIdx = params.getProductPageSize();
        int productListTotal = prodCodeList.size();
        if (endIdx > productListTotal) {
            endIdx = productListTotal;
        }
        List<String> currCodeList = prodCodeList.subList(0, endIdx);
        List<CmsBtProductBean> prodInfoList = searchIndexService.getProductInfoList(currCodeList, params, userInfo, cmsSession);
        searchIndexService.checkProcStatus(prodInfoList, getLang());
        resultBean.put("productList", prodInfoList);
        resultBean.put("productListTotal", productListTotal);

        // 查询该商品是否有价格变动
        List[] infoArr = searchIndexService.getGroupExtraInfo(prodInfoList, userInfo.getSelChannelId(), Integer.parseInt(cmsSession.getPlatformType().get("cartId").toString()), false);
        resultBean.put("prodChgInfoList", infoArr[0]);
        resultBean.put("prodOrgChaNameList", infoArr[1]);
        resultBean.put("freeTagsList", infoArr[2]);

        // 获取group列表
        List<String> groupCodeList = searchIndexService.getGroupCodeList(prodCodeList, userInfo, cmsSession);
        endIdx = params.getGroupPageSize();
        int groupListTotal = groupCodeList.size();
        if (endIdx > groupListTotal) {
            endIdx = groupListTotal;
        }

        List<CmsBtProductBean> grpInfoList = searchIndexService.getProductInfoList(groupCodeList, params, userInfo, cmsSession);
        List<CmsBtProductBean> currGrpList = grpInfoList.subList(0, endIdx);
        searchIndexService.checkProcStatus(currGrpList, getLang());
        resultBean.put("groupList", currGrpList);
        resultBean.put("groupListTotal", groupListTotal);

        infoArr = searchIndexService.getGroupExtraInfo(currGrpList, userInfo.getSelChannelId(), (int) cmsSession.getPlatformType().get("cartId"), true);
        // 获取该组商品图片
        resultBean.put("grpImgList", infoArr[1]);
        // 查询该组商品是否有价格变动
        resultBean.put("grpProdChgInfoList", infoArr[0]);
        // 获取该组商品的prodId
        resultBean.put("grpProdIdList", infoArr[2]);

        // 获取该用户自定义显示列设置
        resultBean.put("customProps", cmsSession.getAttribute("_adv_search_customProps"));
        resultBean.put("commonProps", cmsSession.getAttribute("_adv_search_commonProps"));
        resultBean.put("selSalesType", cmsSession.getAttribute("_adv_search_selSalesType"));
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
        UserSessionBean userInfo = getUser();
        CmsSessionBean cmsSession = getCmsSession();

        // 获取product列表
        List<String> prodCodeList = searchIndexService.getProductCodeList(params, userInfo, cmsSession);

        // 获取group列表
        List<String> groupCodeList = searchIndexService.getGroupCodeList(prodCodeList, userInfo, cmsSession);
        int staIdx = (params.getGroupPageNum() - 1) * params.getGroupPageSize();
        int endIdx = staIdx + params.getGroupPageSize();
        int groupListTotal = groupCodeList.size();
        if (endIdx > groupListTotal) {
            endIdx = groupListTotal;
        }

        List<CmsBtProductBean> grpInfoList = searchIndexService.getProductInfoList(groupCodeList, params, userInfo, cmsSession);
        List<CmsBtProductBean> currGrpList = grpInfoList.subList(staIdx, endIdx);
        searchIndexService.checkProcStatus(currGrpList, getLang());
        resultBean.put("groupList", currGrpList);
        resultBean.put("groupListTotal", groupListTotal);

        List[] infoArr = searchIndexService.getGroupExtraInfo(currGrpList, userInfo.getSelChannelId(), (int) cmsSession.getPlatformType().get("cartId"), true);
        // 获取该组商品图片
        resultBean.put("grpImgList", infoArr[1]);
        // 查询该组商品是否有价格变动
        resultBean.put("grpProdChgInfoList", infoArr[0]);
        // 获取该组商品的prodId
        resultBean.put("grpProdIdList", infoArr[2]);

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
        UserSessionBean userInfo = getUser();
        CmsSessionBean cmsSession = getCmsSession();

        // 获取product列表
        List<String> prodCodeList = searchIndexService.getProductCodeList(params, userInfo, cmsSession);
        // 分页
        int staIdx = (params.getProductPageNum() - 1) * params.getProductPageSize();
        int endIdx = staIdx + params.getProductPageSize();
        int productListTotal = prodCodeList.size();
        if (endIdx > productListTotal) {
            endIdx = productListTotal;
        }
        List<String> currCodeList = prodCodeList.subList(staIdx, endIdx);
        List<CmsBtProductBean> prodInfoList = searchIndexService.getProductInfoList(currCodeList, params, userInfo, cmsSession);
        searchIndexService.checkProcStatus(prodInfoList, getLang());
        resultBean.put("productList", prodInfoList);
        resultBean.put("productListTotal", productListTotal);
        // 查询该商品是否有价格变动
        List[] infoArr = searchIndexService.getGroupExtraInfo(prodInfoList, userInfo.getSelChannelId(), Integer.parseInt(cmsSession.getPlatformType().get("cartId").toString()), false);
        resultBean.put("prodChgInfoList", infoArr[0]);
        resultBean.put("prodOrgChaNameList", infoArr[1]);
        resultBean.put("freeTagsList", infoArr[2]);

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
     * @api {post} /cms/search/advance/getCustColumnsInfo 1.6 取得自定义显示列设置
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
     * @apiSuccess (应用级返回字段) {String[]} custAttrList 用户保存的自定义显示列信息，没有数据时返回空数组
     * @apiSuccess (应用级返回字段) {String[]} commList 用户保存的共同属性显示列信息，没有数据时返回空数组
     * @apiSuccessExample 成功响应查询请求
     * {
     *  "code":null, "message":null, "displayType":null, "redirectTo":null,
     *  "data":{
     *   "customProps": [ {"feed_prop_original":"a_b_c", "feed_prop_translation":"yourname" }...],
     *   "commonProps": [ {"propId":"a_b_c", "propName":"yourname" }...],
     *   "custAttrList": [ "a_b_c", "a_b_d", "a_b_e"...],
     *   "commList": [ "q_b_c", "q_b_d", "q_b_e"...]
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

        // 取得自定义显示列设置
        resultBean.put("customProps", searchIndexService.selectAttrs(userInfo.getSelChannelId(), "0"));
        resultBean.put("commonProps", searchIndexService.getCustColumns());

        // 获取该用户自定义显示列设置
        Map<String, Object> colData = searchIndexService.getUserCustColumns(userInfo, getLang());
        if (colData != null) {
            resultBean.putAll(colData);
        }
        // 返回用户信息
        return success(resultBean);
    }

    /**
     * @api {post} /cms/search/advance/saveCustColumnsInfo 1.7 保存用户自定义显示列设置
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
        List<String> selSalesTypeList = (List<String>) params.get("selSalesTypeList");

        searchIndexService.saveCustColumnsInfo(getUser(), getCmsSession(), customProps, commonProps, getLang(), selSalesTypeList);
        return success(null);
    }

    /**
     * 对产品添加指定自由标签
     *
     * @param params
     * @return
     */
    @RequestMapping("addFreeTag")
    public AjaxResponse addFreeTag(@RequestBody Map<String, Object> params) {
        List<Long> prodIdList = CommonUtil.changeListType((List<Integer>) params.get("prodIdList"));
        String tagPath = StringUtils.trimToNull((String) params.get("tagPath"));
        UserSessionBean userInfo = getUser();

        productTagService.addProdTag(userInfo.getSelChannelId(), tagPath, prodIdList, "freeTags", userInfo.getUserName());
        return success(null);
    }

}
