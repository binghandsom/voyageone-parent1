package com.voyageone.web2.cms.views.search;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.bean.cms.search.product.CmsProductCodeListBean;
import com.voyageone.service.dao.wms.WmsBtInventoryCenterLogicDao;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.cms.CmsBtExportTaskService;
import com.voyageone.service.impl.cms.PlatformService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.search.CmsAdvSearchQueryService;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.cms.search.product.CmsProductSearchQueryService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.*;

/**
 * @author Edward
 * @version 2.0.0, 15/12/14
 */
@RestController
@RequestMapping(
        value = "/cms/search/advanceSearchSolr/",
        method = RequestMethod.POST
)
public class CmsAdvanceSearchSolrController extends CmsController {

    @Autowired
    private CmsAdvanceSearchService searchIndexService;
    @Autowired
    private CmsAdvSearchOtherService advSearchOtherService;
    @Autowired
    private CmsAdvSearchQueryService advSearchQueryService;
    @Autowired
    private CmsAdvSearchCustColumnService advSearchCustColumnService;
    @Autowired
    private PlatformService platformService;
    @Autowired
    private CmsBtExportTaskService cmsBtExportTaskService;

    @Autowired
    private CmsProductSearchQueryService cmsProductSearchQueryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private WmsBtInventoryCenterLogicDao wmsBtInventoryCenterLogicDao;

    /**
     * 初始化,获取master数据
     *
     * @return
     */
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.INIT)
    public AjaxResponse init() throws Exception {
        CmsSessionBean cmsSession = getCmsSession();
        UserSessionBean userInfo = getUser();
        advSearchCustColumnService.getUserCustColumns(userInfo.getSelChannelId(), userInfo.getUserId(), cmsSession, getLang());
        Map<String, Object> resultMap = searchIndexService.getMasterData(userInfo, cmsSession, getLang());
        resultMap.put("channelId", userInfo.getSelChannelId());
        return success(resultMap);
    }

    /**
     * 检索product数据,group数据只是在点击[GROUP一览]时才加载，性能优化
     *
     * @param params
     * @return
     */
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.SEARCH)
    public AjaxResponse search(@RequestBody CmsSearchInfoBean2 params) {
        if ($isDebugEnabled()) {
            try {
                $debug("高级检索 请求参数: " + JacksonUtil.bean2JsonNotNull(params));
            } catch (Exception e) {
                $error("转换输入参数时出错", e);
            }
        }
        String[] codeList = params.getCodeList();
        if (codeList != null && codeList.length > 0) {
            HashSet<String> codeSet = new HashSet<String>(Arrays.asList(codeList));
            if (codeSet.size() > 2000) {
                throw new BusinessException("款号/Code/SKU去重之后最大不能超过2000");
            }
            String[] codes = new String[codeSet.size()];
            params.setCodeList(codeSet.toArray(codes));
        }

        Map<String, Object> resultBean = new HashMap<>();
        UserSessionBean userInfo = getUser();
        CmsSessionBean cmsSession = getCmsSession();
        cmsSession.putAttribute("_adv_search_params", params);

        // 获取product列表
        // 分页
        int endIdx = params.getProductPageSize();
        // 先统计product件数,并放到session中(这时group总件数为空)
        CmsProductCodeListBean cmsProductCodeListBean = cmsProductSearchQueryService.getProductCodeList(params, userInfo.getSelChannelId(), userInfo.getUserId(), userInfo.getUserName());
        long productListTotal = cmsProductCodeListBean.getTotalCount();
        cmsSession.putAttribute("_adv_search_productListTotal", productListTotal);
        cmsSession.putAttribute("_adv_search_groupListTotal", null);

        List<String> currCodeList = cmsProductCodeListBean.getProductCodeList();
        List<CmsBtProductBean> prodInfoList = searchIndexService.getProductInfoList(currCodeList, params, userInfo, cmsSession);
        prodInfoList.sort((o1, o2) -> Integer.compare(currCodeList.indexOf(o1.getCommon().getFields().getCode()),currCodeList.indexOf(o2.getCommon().getFields().getCode())));

        Map<String, TypeChannelBean> productTypes = TypeChannels.getTypeMapWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_57, userInfo.getSelChannelId(), "cn");
        Map<String, TypeChannelBean> sizeTypes = TypeChannels.getTypeMapWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_58, userInfo.getSelChannelId(), "cn");
        Map<String, Map<String, Integer>> codeMap = new HashMap<>();
        prodInfoList.forEach(cmsBtProductBean -> {
            String productType = cmsBtProductBean.getCommon().getFields().getProductType();
            if (!StringUtil.isEmpty(productType)) {
                TypeChannelBean temp = productTypes.get(productType);
                if (temp != null) {
                    cmsBtProductBean.getCommon().getFields().setProductTypeCn(temp.getName());
                }
            }

            String sizeType = cmsBtProductBean.getCommon().getFields().getSizeType();
            if (!StringUtil.isEmpty(sizeType)) {
                TypeChannelBean temp = sizeTypes.get(sizeType);
                if (temp != null) {
                    cmsBtProductBean.getCommon().getFields().setSizeTypeCn(temp.getName());
                }
            }
            codeMap.putAll(getCodeQty(cmsBtProductBean, userInfo));
        });
        searchIndexService.checkProcStatus(prodInfoList, getLang());
        resultBean.put("codeMap", codeMap);
        resultBean.put("productList", prodInfoList);
        resultBean.put("productListTotal", productListTotal);

        // 查询平台显示商品URL
        Integer cartId = params.getCartId();
        resultBean.put("productUrl", platformService.getPlatformProductUrl(cartId.toString()));

        // 查询商品其它画面显示用的信息
        List[] infoArr = advSearchOtherService.getProductExtraInfo(prodInfoList, userInfo.getSelChannelId(), cartId);
        resultBean.put("prodOrgChaNameList", infoArr[0]);
        resultBean.put("freeTagsList", infoArr[1]);

        // 获取该用户自定义显示列设置
        resultBean.put("customProps", cmsSession.getAttribute("_adv_search_customProps"));
        resultBean.put("commonProps", cmsSession.getAttribute("_adv_search_commonProps"));
        resultBean.put("selSalesType", cmsSession.getAttribute("_adv_search_selSalesType"));
        resultBean.put("selBiDataList", cmsSession.getAttribute("_adv_search_selBiDataList"));

        // 返回用户信息
        return success(resultBean);
    }

    /**
     * 分页检索group数据
     *
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

        // 获取group列表
        // 先统计group件数， 翻页时总件数从session中取得
        Long groupListTotal = (Long) cmsSession.getAttribute("_adv_search_groupListTotal");
        if (groupListTotal == null) {
            groupListTotal = searchIndexService.countGroupCodeList(params, userInfo, cmsSession);
            cmsSession.putAttribute("_adv_search_groupListTotal", groupListTotal);
        }

        // 然后再取得当页显示用的group信息
        List<String> groupCodeList = advSearchQueryService.getGroupCodeList(params, userInfo.getSelChannelId());
        List<CmsBtProductBean> grpInfoList = searchIndexService.getProductInfoList(groupCodeList, params, userInfo, cmsSession);
        searchIndexService.checkProcStatus(grpInfoList, getLang());
        Map<String, TypeChannelBean> productTypes = TypeChannels.getTypeMapWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_57, userInfo.getSelChannelId(), "cn");
        Map<String, TypeChannelBean> sizeTypes = TypeChannels.getTypeMapWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_58, userInfo.getSelChannelId(), "cn");
        grpInfoList.forEach(cmsBtProductBean -> {
            String productType = cmsBtProductBean.getCommon().getFields().getProductType();
            if (!StringUtil.isEmpty(productType)) {
                TypeChannelBean temp = productTypes.get(productType);
                if (temp != null) {
                    cmsBtProductBean.getCommon().getFields().setProductTypeCn(temp.getName());
                }
            }

            String sizeType = cmsBtProductBean.getCommon().getFields().getSizeType();
            if (!StringUtil.isEmpty(sizeType)) {
                TypeChannelBean temp = sizeTypes.get(sizeType);
                if (temp != null) {
                    cmsBtProductBean.getCommon().getFields().setSizeTypeCn(temp.getName());
                }
            }
        });
        resultBean.put("groupList", grpInfoList);
        resultBean.put("groupListTotal", groupListTotal);

        List[] infoArr = advSearchOtherService.getGroupExtraInfo(grpInfoList, userInfo.getSelChannelId(), cartId, true);
        // 获取该组商品图片
        resultBean.put("grpImgList", infoArr[0]);
        // 获取该组商品的prodId
        resultBean.put("grpProdIdList", infoArr[1]);
        resultBean.put("grpPriceInfoList", infoArr[2]);

        // 返回用户信息
        return success(resultBean);
    }

    /**
     * 分页检索product数据
     *
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
//        long productListTotal = (Long) cmsSession.getAttribute("_adv_search_productListTotal");

        // 获取product列表
        CmsProductCodeListBean cmsProductCodeListBean = cmsProductSearchQueryService.getProductCodeList(params, userInfo.getSelChannelId(), userInfo.getUserId(), userInfo.getUserName());
        long productListTotal = cmsProductCodeListBean.getTotalCount();
        List<String> currCodeList = cmsProductCodeListBean.getProductCodeList();
        List<CmsBtProductBean> prodInfoList = searchIndexService.getProductInfoList(currCodeList, params, userInfo, cmsSession);
        prodInfoList.sort((o1, o2) -> Integer.compare(currCodeList.indexOf(o1.getCommon().getFields().getCode()),currCodeList.indexOf(o2.getCommon().getFields().getCode())));
        Map<String, Map<String, Integer>> codeMap = new HashMap<>();
        Map<String, TypeChannelBean> productTypes = TypeChannels.getTypeMapWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_57, userInfo.getSelChannelId(), "cn");
        Map<String, TypeChannelBean> sizeTypes = TypeChannels.getTypeMapWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_58, userInfo.getSelChannelId(), "cn");
        prodInfoList.forEach(cmsBtProductBean -> {
            String productType = cmsBtProductBean.getCommon().getFields().getProductType();
            if (!StringUtil.isEmpty(productType)) {
                TypeChannelBean temp = productTypes.get(productType);
                if (temp != null) {
                    cmsBtProductBean.getCommon().getFields().setProductTypeCn(temp.getName());
                }
            }

            String sizeType = cmsBtProductBean.getCommon().getFields().getSizeType();
            if (!StringUtil.isEmpty(sizeType)) {
                TypeChannelBean temp = sizeTypes.get(sizeType);
                if (temp != null) {
                    cmsBtProductBean.getCommon().getFields().setSizeTypeCn(temp.getName());
                }
            }
            codeMap.putAll(getCodeQty(cmsBtProductBean, userInfo));
        });
        searchIndexService.checkProcStatus(prodInfoList, getLang());
        resultBean.put("codeMap", codeMap);
        resultBean.put("productList", prodInfoList);
        resultBean.put("productListTotal", productListTotal);

        // 查询商品其它画面显示用的信息
        List[] infoArr = advSearchOtherService.getProductExtraInfo(prodInfoList, userInfo.getSelChannelId(), cartId);
        resultBean.put("prodOrgChaNameList", infoArr[0]);
        resultBean.put("freeTagsList", infoArr[1]);

        // 返回用户信息
        return success(resultBean);
    }

    public Map<String, Map<String, Integer>> getCodeQty(CmsBtProductBean cmsBtProductBean, UserSessionBean userInfo) {
        Map<String, Map<String, Integer>> codeMap = new HashMap<>();
        // 店铺(cart/平台)列表
        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(userInfo.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, getLang());

        //sku取得库存
//        Map<String, String> codesMap = new HashMap<>();
//        if (StringUtils.isNotBlank(cmsBtProductBean.getCommon().getFields().getOriginalCode())) {
//            codesMap.put("channelId", cmsBtProductBean.getOrgChannelId());
//            codesMap.put("code", cmsBtProductBean.getCommon().getFields().getOriginalCode());
//        }
//        List<WmsBtInventoryCenterLogicModel> inventoryList = wmsBtInventoryCenterLogicDao.selectItemDetailByCode(codesMap);
        //code取得库存
//        int codeQty = 0;
//        for (WmsBtInventoryCenterLogicModel inventoryInfo : inventoryList) {
//            codeQty = codeQty + inventoryInfo.getQtyChina();
//        }
//        cmsBtProductBean.getCommon().getFields().setQuantity(codeQty);

        Map<String, Integer> cartIdMap = new HashMap();
        for (TypeChannelBean cartObj : cartList) {
            CmsBtProductModel_Platform_Cart ptfObj = cmsBtProductBean.getPlatform(Integer.parseInt(cartObj.getValue()));
            if (ptfObj != null && !ListUtils.isNull(ptfObj.getSkus())) {
                int qty = 0;
                for (BaseMongoMap<String, Object> map : ptfObj.getSkus()) {
                    String sku = (String) map.get("skuCode");
                    Boolean isSale = (Boolean) map.get("isSale");
                    if (isSale != null && isSale) {
                        for(CmsBtProductModel_Sku skus : cmsBtProductBean.getCommonNotNull().getSkus()){
//                        for (WmsBtInventoryCenterLogicModel inventoryInfo : inventoryList) {
                            if (skus.getSkuCode().equals(sku)) {
                                qty = qty + skus.getQty();
                            }
                        }
                    }
                }
                cartIdMap.put(cartObj.getAdd_name2(), qty);
            } else {
                cartIdMap.put(cartObj.getAdd_name2(), 0);
            }
        }
        codeMap.put(cmsBtProductBean.getCommon().getFields().getCode(), cartIdMap);
        return codeMap;
    }

    /**
     * 创建文件下载任务，发送消息到MQ，开始批处理
     */
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.EXPORT_PRODUCTS)
    public AjaxResponse createFile(@RequestBody Map<String, Object> params) {
        String fileName = null;
        Integer fileType = (Integer) params.get("fileType");
        Map<String, Object> resultBean = new HashMap<>();
        if (fileType == null) {
            resultBean.put("ecd", "4002");
            return success(resultBean);
        }
        if (fileType == 1) {
            fileName = "productList_";
        } else if (fileType == 2) {
            fileName = "groupList_";
        } else if (fileType == 3) {
            fileName = "skuList_";
        } else if (fileType == 4) {
            fileName = "publishJMSkuList_";
        } else if (fileType == 5) {
            fileName = "filingList_";
        }
        if (fileName == null) {
            resultBean.put("ecd", "4002");
            return success(resultBean);
        }

        try {
            // 文件下载时分页查询要做特殊处理
            if (searchIndexService.getCodeExcelFile(params, getUser(), getCmsSession(), getLang())) {
                resultBean.put("ecd", "0");
                return success(resultBean);
            } else {
                resultBean.put("ecd", "4004");
                return success(resultBean);
            }
        } catch (Throwable e) {
            $error("创建文件时出错", e);
            resultBean.put("ecd", "4003");
            return success(resultBean);
        }
    }

    /**
     * 查询文件生成的状态
     */
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.EXPORT_SERACH)
    public AjaxResponse searchFile(@RequestBody Map<String, Object> params) {
        Map<String, Object> resultBean = new HashMap<String, Object>();
        UserSessionBean userInfo = getUser();
        Integer pageNum = (Integer) params.get("pageNum");
        Integer pageSize = (Integer) params.get("pageSize");
        resultBean.put("exportList", cmsBtExportTaskService.getExportTaskByUser(userInfo.getSelChannelId(), CmsBtExportTaskService.ADV_SEARCH, userInfo.getUserName(), (pageNum - 1) * pageSize, pageSize));
        resultBean.put("exportListTotal", cmsBtExportTaskService.getExportTaskByUserCnt(userInfo.getSelChannelId(), CmsBtExportTaskService.ADV_SEARCH, userInfo.getUserName()));

        // 返回feed信息
        return success(resultBean);
    }

    /**
     * 下载文件
     * TODO-- 注意：这里没有检查文件访问权限，可能会有问题，可以下载其他人的数据文件(通过伪造文件名)
     * 　　　　　　　 也没考虑过期文件删除的问题
     */
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.EXPORT_DOWNLOAD)
    public ResponseEntity<byte[]> downloadFile(@RequestParam String fileName) {
        String exportPath = Properties.readValue(CmsProperty.Props.SEARCH_ADVANCE_EXPORT_PATH);
        File pathFileObj = new File(exportPath);
        if (!pathFileObj.exists()) {
            $info("高级检索 文件下载任务 文件目录不存在 " + exportPath);
            throw new BusinessException("4004");
        }

        exportPath += fileName;
        pathFileObj = new File(exportPath);
        if (!pathFileObj.exists()) {
            $info("高级检索 文件下载任务 文件不存在 " + exportPath);
            throw new BusinessException("4004");
        }
        return genResponseEntityFromFile(fileName, exportPath);
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
     * "code":null, "message":null, "displayType":null, "redirectTo":null,
     * "data":{
     * "customProps": [ {"feed_prop_original":"a_b_c", "feed_prop_translation":"yourname" }...],
     * "commonProps": [ {"propId":"a_b_c", "propName":"yourname" }...],
     * "custAttrList": [ "a_b_c", "a_b_d", "a_b_e"...],
     * "commList": [ "q_b_c", "q_b_d", "q_b_e"...]
     * }
     * }
     * @apiExample 业务说明
     * 取得自定义显示列设置
     * @apiExample 使用表
     * 使用cms_bt_feed_custom_prop表, cms_mt_common_prop表
     * @apiSampleRequest off
     */
    @RequestMapping("getCustColumnsInfo")
    public AjaxResponse getCustColumnsInfo() {
        UserSessionBean userInfo = getUser();

        // 获取该用户自定义显示列设置
        Map<String, Object> colData = advSearchCustColumnService.getUserCustColumns(userInfo, getLang());
        // 返回用户信息
        return success(colData);
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
     * "code":null, "message":null, "displayType":null, "redirectTo":null,
     * "data":null
     * }
     * @apiExample 业务说明
     * 保存用户自定义显示列设置
     * 当参数为空时将清除该用户原有设置
     * @apiExample 使用表
     * 使用ct_user_config表
     * @apiSampleRequest off
     */
    @RequestMapping("saveCustColumnsInfo")
    public AjaxResponse saveCustColumnsInfo(@RequestBody Map<String, Object> params) {
        advSearchCustColumnService.saveCustColumnsInfo(getUser(), getCmsSession(), params, getLang());
        return success(null);
    }

    /**
     * 对产品设置自由标签
     *
     * @param params
     * @return
     */
    @RequestMapping("addFreeTag")
    public AjaxResponse addFreeTag(@RequestBody Map<String, Object> params) {
        UserSessionBean userInfo = getUser();

        searchIndexService.setProdFreeTag(userInfo.getSelChannelId(), params, userInfo.getUserName(), getCmsSession());
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


    /**
     * 根据Solr 检索product数据,group数据只是在点击[GROUP一览]时才加载，性能优化
     */
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.SEARCH_AUTO_COMPLETE_SOLR)
    public AjaxResponse searchAutoCompleteWithSolr(@RequestBody String params) {

        UserSessionBean userInfo = getUser();
        List<String> resultBean = cmsProductSearchQueryService.getTop10ProductModelCodeSkuList(params, userInfo.getSelChannelId());

        System.out.println(JacksonUtil.bean2Json(resultBean));

        // 返回用户信息
        return success(resultBean);
    }

    /**
     * 取得SKU级的库存属性
     */
    @ResponseBody
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.GET_SKU_INVENTORY)
    public AjaxResponse getSkuInventoryList(@RequestBody String code) {
        CmsBtProductModel cmsBtProductModel = productService.getProductByCode(getUser().getSelChannelId(), code);
        if (cmsBtProductModel != null) {
            return success(advSearchQueryService.getSkuInventoryList(cmsBtProductModel.getOrgChannelId(), code));
        } else {
            throw new BusinessException(code + "：该商品不存在");
        }
    }

    /**
     * 给美国同事抽一份“shoemetro已上新到聚美的sku级别数据”
     *
     * @param params
     * @return
     */
    public AjaxResponse exportShowMetroProduct(@RequestBody String params) {
        return success("");
    }

//    /**
//     * 根据Solr 检索product数据,group数据只是在点击[GROUP一览]时才加载，性能优化
//     */
//    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.SEARCH)
//    public AjaxResponse searchWithSolr(@RequestBody CmsSearchInfoBean2 params) {
//
//        Map<String, Object> resultBean = new HashMap<>();
//        UserSessionBean userInfo = getUser();
//        CmsSessionBean cmsSession = getCmsSession();
//        cmsSession.putAttribute("_adv_search_params", params);
//
//        // 获取product列表
//        CmsProductCodeListBean productCodeListBean = cmsProductSearchQueryService.getProductCodeList(params, userInfo.getSelChannelId());
//        List<String> currCodeList = productCodeListBean.getProductCodeList();
//        List<CmsBtProductBean> prodInfoList = searchIndexService.getProductInfoList(currCodeList, params, userInfo, cmsSession);
//        searchIndexService.checkProcStatus(prodInfoList, getLang());
//        resultBean.put("productList", prodInfoList);
//        resultBean.put("productListTotal", productCodeListBean.getTotalCount());
//        // 先统计product件数,并放到session中(这时group总件数为空)
//        cmsSession.putAttribute("_adv_search_productListTotal", productCodeListBean.getTotalCount());
//        cmsSession.putAttribute("_adv_search_groupListTotal", null);
//
//        // 查询平台显示商品URL
//        Integer cartId = params.getCartId();
//        resultBean.put("productUrl", platformService.getPlatformProductUrl(cartId.toString()));
//
//        // 查询商品其它画面显示用的信息
//        List[] infoArr = advSearchOtherService.getGroupExtraInfo(prodInfoList, userInfo.getSelChannelId(), cartId, false);
//        resultBean.put("prodOrgChaNameList", infoArr[0]);
//        resultBean.put("freeTagsList", infoArr[1]);
//
//        // 获取该用户自定义显示列设置
//        resultBean.put("customProps", cmsSession.getAttribute("_adv_search_customProps"));
//        resultBean.put("commonProps", cmsSession.getAttribute("_adv_search_commonProps"));
//        resultBean.put("selSalesType", cmsSession.getAttribute("_adv_search_selSalesType"));
//        resultBean.put("selBiDataList", cmsSession.getAttribute("_adv_search_selBiDataList"));
//
//        // 返回用户信息
//        return success(resultBean);
//    }

}
