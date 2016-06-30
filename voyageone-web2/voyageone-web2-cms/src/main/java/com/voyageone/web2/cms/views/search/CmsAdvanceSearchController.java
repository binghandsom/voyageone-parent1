package com.voyageone.web2.cms.views.search;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.impl.cms.PlatformService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.bean.search.index.CmsSearchInfoBean2;
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
        value = "/cms/search/advanceSearch/",
        method = RequestMethod.POST
)
public class CmsAdvanceSearchController extends CmsController {

    @Autowired
    private CmsAdvanceSearchService searchIndexService;
    @Autowired
    private CmsAdvSearchQueryService advSearchQueryService;
    @Autowired
    private CmsAdvSearchCustColumnService advSearchCustColumnService;
    @Autowired
    private CmsAdvSearchExportFileService advSearchExportFileService;
    @Autowired
    PlatformService platformService;

    /**
     * 初始化,获取master数据
     * @return
     */
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.INIT)
    public AjaxResponse init() throws Exception {
        CmsSessionBean cmsSession = getCmsSession();
        UserSessionBean userInfo = getUser();
        advSearchCustColumnService.getUserCustColumns(userInfo.getSelChannelId(), userInfo.getUserId(), cmsSession, getLang());
        return success(searchIndexService.getMasterData(userInfo, cmsSession, getLang()));
    }

    /**
     * 检索出group和product数据
     * @param params
     * @return
     */
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.SEARCH)
    public AjaxResponse search(@RequestBody CmsSearchInfoBean2 params) {
        Map<String, Object> resultBean = new HashMap<>();
        UserSessionBean userInfo = getUser();
        CmsSessionBean cmsSession = getCmsSession();
        cmsSession.putAttribute("_adv_search_params", params);

        // 获取product列表
        // 分页
        int endIdx = params.getProductPageSize();
        // 先统计product件数
        long productListTotal = searchIndexService.countProductCodeList(params, userInfo, cmsSession);
        List<String> prodCodeList = searchIndexService.getProductCodeList(params, userInfo, cmsSession, 1000);

        if (endIdx > productListTotal) {
            endIdx = (int) productListTotal;
        }
        List<String> currCodeList = prodCodeList.subList(0, endIdx);
        List<CmsBtProductBean> prodInfoList = searchIndexService.getProductInfoList(currCodeList, params, userInfo, cmsSession);
        searchIndexService.checkProcStatus(prodInfoList, getLang());
        resultBean.put("productList", prodInfoList);
        resultBean.put("productListTotal", productListTotal);

        // 查询平台显示商品URL
        Integer cartId = params.getCartId();
        resultBean.put("productUrl", platformService.getPlatformProductUrl(cartId.toString()));

        // 查询商品其它画面显示用的信息
        List[] infoArr = advSearchQueryService.getGroupExtraInfo(prodInfoList, userInfo.getSelChannelId(), cartId, false);
        resultBean.put("prodChgInfoList", infoArr[0]);
        resultBean.put("prodOrgChaNameList", infoArr[1]);
        resultBean.put("freeTagsList", infoArr[2]);

        // 获取group列表
        // 先统计group件数
        long groupListTotal = searchIndexService.countGroupCodeList(params, userInfo, cmsSession);
        // 然后再取得当页显示用的group信息
        List<String> groupCodeList = searchIndexService.getGroupCodeList(prodCodeList, userInfo, params, cartId);
        List<CmsBtProductBean> grpInfoList = searchIndexService.getProductInfoList(groupCodeList, params, userInfo, cmsSession);

        searchIndexService.checkProcStatus(grpInfoList, getLang());
        resultBean.put("groupList", grpInfoList);
        resultBean.put("groupListTotal", groupListTotal);

        infoArr = advSearchQueryService.getGroupExtraInfo(grpInfoList, userInfo.getSelChannelId(), cartId, true);
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
    public AjaxResponse getGroupList(@RequestBody CmsSearchInfoBean2 params) {
        Map<String, Object> resultBean = new HashMap<>();
        UserSessionBean userInfo = getUser();
        CmsSessionBean cmsSession = getCmsSession();
        Integer cartId = params.getCartId();
        resultBean.put("productUrl", platformService.getPlatformProductUrl(cartId.toString()));

        params.setProductPageNum(params.getGroupPageNum());
        params.setProductPageSize(params.getGroupPageSize());
        List<String> prodCodeList = searchIndexService.getProductCodeList(params, userInfo, cmsSession, 1000);

        // 获取group列表
        // 先统计group件数
        long groupListTotal = searchIndexService.countGroupCodeList(params, userInfo, cmsSession);
        // 然后再取得当页显示用的group信息
        List<String> groupCodeList = searchIndexService.getGroupCodeList(prodCodeList, userInfo, params, cartId);
        List<CmsBtProductBean> grpInfoList = searchIndexService.getProductInfoList(groupCodeList, params, userInfo, cmsSession);

        searchIndexService.checkProcStatus(grpInfoList, getLang());
        resultBean.put("groupList", grpInfoList);
        resultBean.put("groupListTotal", groupListTotal);

        List[] infoArr = advSearchQueryService.getGroupExtraInfo(grpInfoList, userInfo.getSelChannelId(), cartId, true);
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
    public AjaxResponse getProductList(@RequestBody CmsSearchInfoBean2 params) {
        Map<String, Object> resultBean = new HashMap<>();
        UserSessionBean userInfo = getUser();
        CmsSessionBean cmsSession = getCmsSession();
        Integer cartId = params.getCartId();
        resultBean.put("productUrl", platformService.getPlatformProductUrl(cartId.toString()));

        // 先统计product件数
        long productListTotal = searchIndexService.countProductCodeList(params, userInfo, cmsSession);

        // 获取product列表
        List<String> prodCodeList = searchIndexService.getProductCodeList(params, userInfo, cmsSession, 0);
        List<CmsBtProductBean> prodInfoList = searchIndexService.getProductInfoList(prodCodeList, params, userInfo, cmsSession);
        searchIndexService.checkProcStatus(prodInfoList, getLang());
        resultBean.put("productList", prodInfoList);
        resultBean.put("productListTotal", productListTotal);

        // 查询商品其它画面显示用的信息
        List[] infoArr = advSearchQueryService.getGroupExtraInfo(prodInfoList, userInfo.getSelChannelId(), cartId, false);
        resultBean.put("prodChgInfoList", infoArr[0]);
        resultBean.put("prodOrgChaNameList", infoArr[1]);
        resultBean.put("freeTagsList", infoArr[2]);

        // 返回用户信息
        return success(resultBean);
    }

    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.EXPORT_PRODUCTS)
    public ResponseEntity<byte[]> doExport(@RequestParam String params) {
        CmsSearchInfoBean2 p = null;
        try {
            p = JacksonUtil.json2Bean(params, CmsSearchInfoBean2.class);
        } catch (Exception exp) {
            $error("查询参数不正确", exp);
            throw new BusinessException("4001");
        }

        String fileName = null;
        if (p.getFileType() == 1) {
            fileName = "productList_";
        } else if (p.getFileType() == 2) {
            fileName = "groupList_";
        } else if (p.getFileType() == 3) {
            fileName = "skuList_";
        }
        if (fileName == null) {
            throw new BusinessException("4002");
        }

        byte[] data = null;
        try {
            // 文件下载时分页查询要做特殊处理
            p.setGroupPageNum(0);
            data = advSearchExportFileService.getCodeExcelFile(p, getUser(), getCmsSession(), getLang());
        } catch (Exception e) {
            $error("创建文件时出错", e);
            throw new BusinessException("4003");
        }
        fileName += DateTimeUtil.getLocalTime(getUserTimeZone()) + ".xlsx";
        return genResponseEntityFromBytes(fileName, data);
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
        Map<String, Object> colData = advSearchCustColumnService.getUserCustColumns(userInfo, getLang());
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

        advSearchCustColumnService.saveCustColumnsInfo(getUser(), getCmsSession(), customProps, commonProps, getLang(), selSalesTypeList);
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
        UserSessionBean userInfo = getUser();

        searchIndexService.addProdTag(userInfo.getSelChannelId(), params, "freeTags", userInfo.getUserName(), getCmsSession());
        return success(null);
    }

    /**
     * 自定义搜索条件中，当选择的项目为下拉列表时，获取下拉列表的值
     *
     * @param params
     * @return
     */
    @RequestMapping("getCustSearchList")
    public AjaxResponse getCustSearchList(@RequestBody Map<String, Object> params) {
        String tagPath = StringUtils.trimToNull((String) params.get("fieldsId"));
        if (tagPath != null) {
            int idx = tagPath.lastIndexOf('.');
            if (idx > 0) {
                tagPath = tagPath.substring(idx + 1);
            }
        }
        String inputType = StringUtils.trimToNull((String) params.get("inputType"));
        if ("list-2".equals(inputType)) {
            List<TypeChannelBean> typeChannelBeanList = TypeChannels.getTypeWithLang(tagPath, getUser().getSelChannelId(), getLang());
            return success(typeChannelBeanList);
        } else {
            List<TypeBean> typeBeanList = Types.getTypeList(tagPath, getLang());
            return success(typeBeanList);
        }
    }
}
