package com.voyageone.task2.cms.service.platform;

import com.jd.open.api.sdk.response.ware.WareUpdateDelistingResponse;
import com.jd.open.api.sdk.response.ware.WareUpdateListingResponse;
import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import com.taobao.api.response.ItemUpdateDelistingResponse;
import com.taobao.api.response.ItemUpdateListingResponse;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.service.CnnWareService;
import com.voyageone.components.dt.enums.DtConstants;
import com.voyageone.components.dt.service.DtWareService;
import com.voyageone.components.jd.service.JdSaleService;
import com.voyageone.components.jumei.reponse.HtMallStatusUpdateBatchResponse;
import com.voyageone.components.jumei.service.JumeiSaleService;
import com.voyageone.components.tmall.service.TbSaleService;
import com.voyageone.service.dao.cms.mongo.CmsBtPlatformActiveLogDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.CmsBtPlatformActiveLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * 记录上下架操作历史(新增记录), 并调用上下架API
 * create by jiangjusheng on 2016/07/11
 *
 * @author jiangjusheng
 * @version 2.6.0
 * @since 2.0.0
 */
@Service
public class CmsPlatformActiveLogService extends BaseService {

    private final CmsBtPlatformActiveLogDao platformActiveLogDao;
    private final CmsBtProductDao cmsBtProductDao;
    private final CmsBtProductGroupDao cmsBtProductGroupDao;
    private final TbSaleService tbSaleService;
    private final JdSaleService jdSaleService;
    private final JumeiSaleService jmSaleService;
    private final MongoSequenceService sequenceService;
    private final DtWareService dtWareService;
    private final CnnWareService cnnWareService;

    @Autowired
    public CmsPlatformActiveLogService(CmsBtProductGroupDao cmsBtProductGroupDao, JumeiSaleService jmSaleService,
                                       TbSaleService tbSaleService, JdSaleService jdSaleService,
                                       MongoSequenceService sequenceService, CmsBtPlatformActiveLogDao platformActiveLogDao,
                                       CmsBtProductDao cmsBtProductDao, DtWareService dtWareService,
                                       CnnWareService cnnWareService) {
        this.cmsBtProductGroupDao = cmsBtProductGroupDao;
        this.jmSaleService = jmSaleService;
        this.tbSaleService = tbSaleService;
        this.jdSaleService = jdSaleService;
        this.sequenceService = sequenceService;
        this.platformActiveLogDao = platformActiveLogDao;
        this.cmsBtProductDao = cmsBtProductDao;
        this.dtWareService = dtWareService;
        this.cnnWareService = cnnWareService;
    }

    /**
     * 执行产品上下架操作
     */
    public List<CmsBtOperationLogModel_Msg> setProductOnSaleOrInStock(Map<String, Object> messageMap) throws Exception {
        $info("CmsPlatformActiveLogService start 参数 " + JacksonUtil.bean2Json(messageMap));

        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();

        JongoQuery queryObj = new JongoQuery();
        String channelId = ((String) messageMap.get("channelId"));
        Integer cartId = (Integer) messageMap.get("cartId");
        String activeStatus = ((String) messageMap.get("activeStatus"));
        String userName = ((String) messageMap.get("creator"));
        String comment = (String) messageMap.get("comment");
        Collection<String> codeList = (Collection<String>) messageMap.get("codeList");

        // 判断是否缺少参数
        checkParameters(channelId, codeList, activeStatus, userName, cartId, messageMap);

        // 取得batchNo
        long batchNo = sequenceService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_PLATFORMACTIVEJOB_ID);

        // 调用实际的上下架Api，记录调用结果，在group表和product表更新相关状态
        BulkWriteResult rs;
        BulkJongoUpdateList bulkList2 = new BulkJongoUpdateList(100, cmsBtProductGroupDao, channelId);
        BulkJongoUpdateList bulkList3 = new BulkJongoUpdateList(100, cmsBtProductDao, channelId);

        boolean updRsFlg;
        String errMsg;
        // 根据cartId和productCodes取得Shops信息
        ShopBean shopProp = Shops.getShop(channelId, cartId);
        // 根据取得Shops信息是否有异常，如果有异常保留异常信息
        isNullShopProp(shopProp, channelId, cartId);

        //根据cartId和productCodes取得cmsBtProductGroup信息
        List<CmsBtProductGroupModel> grpObjList = getCmsBtProductGroupModelInfo(codeList, cartId, channelId);
        //根据取得grpObjList信息是否有异常，如果有异常保留异常信息
        isNullGrpObjList(grpObjList, codeList, cartId, channelId);

        for (CmsBtProductGroupModel cmsBtProductGroupModel : grpObjList) {
            //根据取得cmsBtProductGroupModel信息是否有异常，如果有异常保留异常信息
            CmsBtOperationLogModel_Msg errorInfo = isNullCmsBtProductGroupModel(cmsBtProductGroupModel, cartId, channelId);
            if (errorInfo != null) {
                failList.add(errorInfo);
                continue;
            }

            //调用api上架或者下架
            String numIId = cmsBtProductGroupModel.getNumIId();
            List<String> pcdList = cmsBtProductGroupModel.getProductCodes();
            updRsFlg = false;
            errMsg = null;

            // 天猫国际上下架
            if (PlatFormEnums.PlatForm.TM.getId().equals(shopProp.getPlatform_id())) {

                if (CmsConstants.PlatformActive.ToOnSale.name().equals(activeStatus)) {
                    // 上架
                    ItemUpdateListingResponse response = tbSaleService.doWareUpdateListing(shopProp, numIId);
                    if (response == null) {
                        errMsg = "调用天猫系商品上架API失败";
                    } else {
                        if (StringUtils.isEmpty(response.getErrorCode())) {
                            updRsFlg = true;
                        } else {
                            errMsg = response.getBody();
                        }
                    }
                } else if (CmsConstants.PlatformActive.ToInStock.name().equals(activeStatus)) {
                    // 下架
                    ItemUpdateDelistingResponse response = tbSaleService.doWareUpdateDelisting(shopProp, numIId);
                    if (response == null) {
                        errMsg = "调用天猫系商品下架API失败";
                    } else {
                        if (StringUtils.isEmpty(response.getErrorCode())) {
                            updRsFlg = true;
                        } else {
                            errMsg = response.getBody();
                        }
                    }
                }
            }

            // 京东国际上下架
            else if (PlatFormEnums.PlatForm.JD.getId().equals(shopProp.getPlatform_id())) {

                if (CmsConstants.PlatformActive.ToOnSale.name().equals(activeStatus)) {
                    // 上架
                    WareUpdateListingResponse response = jdSaleService.doWareUpdateListing(shopProp, numIId);
                    if (response == null) {
                        errMsg = "调用京东商品上架API失败";
                    } else {
                        if ("0".equals(response.getCode())) {
                            updRsFlg = true;
                        } else {
                            errMsg = response.getMsg();
                        }
                    }
                } else if (CmsConstants.PlatformActive.ToInStock.name().equals(activeStatus)) {
                    // 下架
                    WareUpdateDelistingResponse response = jdSaleService.doWareUpdateDelisting(shopProp, numIId);
                    if (response == null) {
                        errMsg = "调用京东商品下架API失败";
                    } else {
                        if ("0".equals(response.getCode())) {
                            updRsFlg = true;
                        } else {
                            errMsg = response.getMsg();
                        }
                    }
                }
            }

            // 聚美上下架
            else if (PlatFormEnums.PlatForm.JM.getId().equals(shopProp.getPlatform_id())) {

                HtMallStatusUpdateBatchResponse response = null;
                if (CmsConstants.PlatformActive.ToOnSale.name().equals(activeStatus)) {
                    // 上架
                    response = jmSaleService.doWareUpdateListing(shopProp, numIId);
                } else if (CmsConstants.PlatformActive.ToInStock.name().equals(activeStatus)) {
                    // 下架
                    response = jmSaleService.doWareUpdateDelisting(shopProp, numIId);
                }
                if (response == null) {
                    errMsg = String.format("调用聚美商品上架/下架(%s)API失败", activeStatus);
                } else {
                    if (response.isSuccess()) {
                        updRsFlg = true;
                    } else {
                        errMsg = response.getErrorMsg();
                    }
                }
            }

            // 分销上下架
            else if (PlatFormEnums.PlatForm.DT.getId().equals(shopProp.getPlatform_id())) { // 分销平台

                String result = "";
                if (CmsConstants.PlatformActive.ToOnSale.name().equals(activeStatus)) {
                    // 上架
                    result = dtWareService.onShelfProduct(shopProp, numIId);
                } else if (CmsConstants.PlatformActive.ToInStock.name().equals(activeStatus)) {
                    // 下架
                    result = dtWareService.offShelfProduct(shopProp, numIId);
                }
                if (StringUtils.isNotBlank(result)) {
                    Map<String, Object> responseMap = JacksonUtil.jsonToMap(result);
                    if (responseMap != null && responseMap.containsKey("data") && responseMap.get("data") != null) {
                        Map<String, Object> resultMap = (Map<String, Object>) responseMap.get("data");
                        if (DtConstants.C_DT_RETURN_SUCCESS_OK.equals(resultMap.get("result"))) {
                            updRsFlg = true;
                        } else {
                            errMsg = (String) resultMap.get("reason");
                        }
                    }
                }
            }

            // 独立官网
            else if (PlatFormEnums.PlatForm.CNN.getId().equals(shopProp.getPlatform_id())) {

                String result = "";
                if (CmsConstants.PlatformActive.ToOnSale.name().equals(activeStatus)) {
                    // 上架
                    result = cnnWareService.doWareUpdateListing(shopProp, Long.valueOf(numIId));
                } else if (CmsConstants.PlatformActive.ToInStock.name().equals(activeStatus)) {
                    // 下架
                    result = cnnWareService.doWareUpdateDelisting(shopProp, Long.valueOf(numIId));
                }
                if (!com.voyageone.common.util.StringUtils.isEmpty(result)) {
                    Map<String, Object> responseMap = JacksonUtil.jsonToMap(result);
                    if (responseMap != null && responseMap.containsKey("code") && responseMap.get("code") != null) {
                        if (CnnConstants.C_CNN_RETURN_SUCCESS_0 == (int) responseMap.get("code")) {
                            updRsFlg = true;
                        } else {
                            errMsg = String.format("[errMsg=%s]", responseMap.get("msg"));
                        }
                    }
                }
            }

            // 其他平台
            else {

                $error("CmsPlatformActiveLogService 不正确的平台 cartId=%d", cartId);

                errorInfo = new CmsBtOperationLogModel_Msg();
                errorInfo.setSkuCode("numberIid:" + cmsBtProductGroupModel.getNumIId());
                errorInfo.setMsg(String.format("不正确的平台 cartId=%d", cartId));
                failList.add(errorInfo);
            }

            if (!updRsFlg) {

                errorInfo = new CmsBtOperationLogModel_Msg();
                errorInfo.setSkuCode("numberId:" + cmsBtProductGroupModel.getNumIId());
                errorInfo.setMsg(String.format("调用平台%s商品上架/下架(%s)API失败: %s", cartId, activeStatus, errMsg));
                failList.add(errorInfo);
            }

            // 保存调用结果
            for (String prodCode : pcdList) {

                //根据group的code取得cms_bt_product信息数据
                String failedComment = null;
                CmsBtProductModel prodObj = getCmsBtProductModelInfo(cartId, prodCode, channelId, queryObj);

                //根据group的code判断cms_bt_product表是否有异常信息数据
                String result;
                failedComment = failedComment(failedComment, prodObj, cartId, prodCode, channelId);
                if (failedComment != null) {
                    result = "3";
                } else {
                    if (updRsFlg) {
                        result = "1";
                    } else {
                        result = "2";
                        failedComment = "调用API失败";
                    }
                    // 更新cms_bt_product表
                    rs = bulkList3.addBulkJongo(updateCmsBtProductInfo(cartId, activeStatus, userName, prodCode, updRsFlg, errMsg));
                    if (rs != null) {
                        $debug("CmsPlatformActiveLogService cartId=%d, channelId=%s, code=%s cmsBtProduct更新结果=%s", cartId, channelId, prodCode, rs.toString());
                    }
                }
                //插入cms_bt_platform_active_log表
                insertCmsBtPlatformActiveLogModel(prodCode, batchNo, cartId, channelId, activeStatus, comment, cmsBtProductGroupModel, userName, result, failedComment);
            }

            if (updRsFlg) {
                //更新cms_bt_product_group表
                rs = bulkList2.addBulkJongo(updateCmsBtProductGroupInfo(cartId, numIId, activeStatus, userName));
                if (rs != null) {
                    $debug("CmsPlatformActiveLogService cartId=%d, channelId=%s, numIId=%s cmsBtProductGroup更新结果=%s", cartId, channelId, numIId, rs.toString());
                }
            }
        }
        rs = bulkList2.execute();
        if (rs != null) {
            $debug("CmsPlatformActiveLogService cartId=%d, channelId=%s, batchNo=%d cmsBtProductGroup更新结果=%s", cartId, channelId, batchNo, rs.toString());
        }
        rs = bulkList3.execute();
        if (rs != null) {
            $debug("CmsPlatformActiveLogService cartId=%d, channelId=%s, batchNo=%d cmsBtProduct更新结果=%s", cartId, channelId, batchNo, rs.toString());
        }
        return failList;
    }

    /**
     * 根据取得Shops信息是否有异常，如果有异常保留异常信息
     * @param shopProp
     * @param channelId
     * @param cartId
     */
    private void isNullShopProp(ShopBean shopProp, String channelId, Integer cartId) {
        if (shopProp == null) {

            $error("CmsPlatformActiveLogService 获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);

            throw new BusinessException(String.format("获取到店铺信息失败, channelId=%s, cartId=%d", channelId, cartId));
        }
    }

    /**
     * 根据取得grpObjList信息是否有异常，如果有异常保留异常信息
     * @param grpObjList
     * @param codeList
     * @param cartId
     * @param channelId
     */
    private void isNullGrpObjList(List<CmsBtProductGroupModel> grpObjList, Collection<String> codeList, Integer cartId, String channelId) {
        if (grpObjList == null || grpObjList.isEmpty()) {

            $error(String.format("CmsPlatformActiveLogService 产品对应的group不存在 cartId=%d, channelId=%s, codes=%s", cartId, channelId, codeList.toString()));

            throw new BusinessException(String.format("产品对应的group不存在 cartId=%d, channelId=%s, codes=%s", cartId, channelId, codeList.toString()));
        }
    }

    /**
     * 根据取得cmsBtProductGroupModel信息是否有异常，如果有异常保留异常信息
     * @param cmsBtProductGroupModel
     * @param cartId
     * @param channelId
     * @return
     */
    private CmsBtOperationLogModel_Msg isNullCmsBtProductGroupModel(CmsBtProductGroupModel cmsBtProductGroupModel, Integer cartId, String channelId) {
        CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
        if (cmsBtProductGroupModel.getProductCodes() == null || cmsBtProductGroupModel.getProductCodes().isEmpty()) {
            errorInfo.setSkuCode(cmsBtProductGroupModel.getMainProductCode());
            errorInfo.setMsg(String.format("产品group下没有商品codes, cartId=%d, channelId=%s, grpInfo=%s", cartId, channelId, JacksonUtil.bean2Json(cmsBtProductGroupModel)));

            $error("CmsPlatformActiveLogService cartId=%d, channelId=%s, grpInfo=%s", cartId, channelId, cmsBtProductGroupModel.toString());
            return errorInfo;
        }
        return null;
    }

    /**
     * 判断是否缺少参数
     * @param channelId
     * @param codeList
     * @param activeStatus
     * @param userName
     * @param cartId
     * @param messageMap
     */
    private void checkParameters(String channelId, Collection<String> codeList, String activeStatus, String userName
            , Integer cartId, Map<String, Object> messageMap) {

        if (StringUtils.isBlank(channelId) || CollectionUtils.isEmpty(codeList) || StringUtils.isBlank(activeStatus)
                || StringUtils.isBlank(userName) || cartId == null) {
            $error("CmsPlatformActiveLogService 缺少参数");
            throw new BusinessException(String.format("缺少channelId/userName/activeStatus/codeList/cartId参数, params=%s"
                    , JacksonUtil.bean2Json(messageMap)));
        }
    }

    /**
     * 根据cartId和productCodes取得cmsBtProductGroup信息
     * @param codeList
     * @param cartId
     * @param channelId
     * @return
     */
    private List<CmsBtProductGroupModel> getCmsBtProductGroupModelInfo(Collection<String> codeList, Integer cartId, String channelId) {
        JongoQuery queryObj = new JongoQuery();
        queryObj.setQuery("{'productCodes':{$in:#},'cartId':#, \"numIId\": {$nin: [\"\", null]}}");
        queryObj.setParameters(codeList, cartId);
        queryObj.setProjectionExt("mainProductCode", "productCodes", "groupId", "numIId", "platformMallId");
        return cmsBtProductGroupDao.select(queryObj, channelId);
    }

    /**
     * 根据参数插入cms_bt_platform_active_log表记录
     * @param prodCode
     * @param batchNo
     * @param cartId
     * @param channelId
     * @param activeStatus
     * @param comment
     * @param cmsBtProductGroupModel
     * @param userName
     * @param result
     * @param failedComment
     */
    private void insertCmsBtPlatformActiveLogModel(String prodCode, long batchNo, Integer cartId, String channelId
            , String activeStatus, String comment, CmsBtProductGroupModel cmsBtProductGroupModel, String userName
            , String result, String failedComment) {
        CmsBtPlatformActiveLogModel model = new CmsBtPlatformActiveLogModel();
        model.setBatchNo(batchNo);
        model.setCartId(cartId);
        model.setChannelId(channelId);
        model.setActiveStatus(activeStatus);
        model.setPlatformStatus(getPlatformStatus(prodCode, channelId, cartId));
        if (failedComment != null) {
            model.setFailedComment(failedComment);
        } else {
            model.setComment(comment);
        }
        model.setGroupId(cmsBtProductGroupModel.getGroupId());
        model.setMainProdCode(cmsBtProductGroupModel.getMainProductCode());
        model.setProdCode(prodCode);
        String numId;
        if (CartEnums.Cart.JM.getId().equals(String.valueOf(cartId))) {
            numId = cmsBtProductGroupModel.getPlatformMallId();
        } else if (CartEnums.Cart.DT.getId().equals(String.valueOf(cartId))) {
            // 如果是分销平台，numIId设置为商品code
            numId = prodCode;
        } else {
            numId = cmsBtProductGroupModel.getNumIId();
        }
        model.setNumIId(numId);
        model.setResult(result);
        model.setCreater(userName);
        model.setCreated(DateTimeUtil.getNow());
        model.setModified("");
        model.setModifier("");
        WriteResult rs = platformActiveLogDao.insert(model);
        $debug("CmsPlatformActiveLogService cartId=%d, channelId=%s, code=%s platformActiveLog保存结果=%s", cartId, channelId, prodCode, rs.toString());
    }

    /**
     * 取得商品对应的平台状态
     * @param prodCode
     * @param channelId
     * @param cartId
     * @return
     */
    private String getPlatformStatus(String prodCode, String channelId, int cartId) {
        JongoQuery queryObj = new JongoQuery();
        queryObj.setQuery("{'common.fields.code':#}");
        queryObj.setParameters(prodCode);
        queryObj.setProjectionExt("platforms.P" + cartId + ".pStatus");
        CmsBtProductModel prodObj = cmsBtProductDao.selectOneWithQuery(queryObj, channelId);
        if (prodObj == null) {
            $warn("CmsPlatformActiveLogService 找不到商品 channelId=%s，code=%s", channelId, prodCode);
            return null;
        }
        CmsConstants.PlatformStatus pStatus = prodObj.getPlatformNotNull(cartId).getpStatus();
        if (pStatus != null) {
            return pStatus.name();
        }
        return null;
    }

    /**
     * 根据group的code取得cms_bt_product信息数据
     * @param cartId
     * @param prodCode
     * @param channelId
     * @param queryObj
     * @return
     */
    private CmsBtProductModel getCmsBtProductModelInfo(Integer cartId, String prodCode, String channelId, JongoQuery queryObj) {
        queryObj.setQuery("{'common.fields.code':#}");
        queryObj.setParameters(prodCode);
        queryObj.setProjectionExt("lock", "platforms.P" + cartId + ".pNumIId", "platforms.P" + cartId + ".pPlatformMallId", "platforms.P" + cartId + ".status", "platforms.P" + cartId + ".pStatus", "platforms.P" + cartId + ".mainProductCode");
        return cmsBtProductDao.selectOneWithQuery(queryObj, channelId);
    }

    /**
     * 根据group的code判断cms_bt_product表是否有异常信息数据
     * @param failedComment
     * @param prodObj
     * @param cartId
     * @param prodCode
     * @param channelId
     * @return
     */
    private String failedComment(String failedComment, CmsBtProductModel prodObj, Integer cartId, String prodCode, String channelId) {
        if (prodObj == null) {
            $warn("CmsPlatformActiveLogService 找不到商品code cartId=%d", cartId);
            failedComment = "商品不存在";
        } else {
            String mainCode = StringUtils.trimToNull(prodObj.getPlatformNotNull(cartId).getMainProductCode());
            String proNumIId = CartEnums.Cart.JM.getId().equals(String.valueOf(cartId))
                    ? prodObj.getPlatformNotNull(cartId).getpPlatformMallId()
                    : prodObj.getPlatformNotNull(cartId).getpNumIId();
            if (proNumIId != null && StringUtils.isNotEmpty(proNumIId.trim()) && !"0".equals(proNumIId.trim())) {
                if (mainCode == null) {
                    $warn("CmsPlatformActiveLogService 产品数据错误(没有MainProductCode数据) channelId=%s, code=%s", channelId, prodCode);
                    failedComment = "未设置主商品, 上下架操作无效";
                } else if ("1".equals(StringUtils.trimToNull(prodObj.getLock()))) {
                    $warn("CmsPlatformActiveLogService 商品lock channelId=%s, code=%s", channelId, prodCode);
                    failedComment = "商品已锁定, 上下架操作无效";
                }
            } else {
                failedComment = "商品未上新, 上下架操作无效";
            }
        }
        return failedComment;
    }

    /**
     * 更新cms_bt_product表
     * @param cartId
     * @param activeStatus
     * @param userName
     * @param prodCode
     * @param updRsFlg
     * @param errMsg
     * @return
     */
    private JongoUpdate updateCmsBtProductInfo(Integer cartId, String activeStatus, String userName, String prodCode, boolean updRsFlg, String errMsg) {
        JongoUpdate updObj = new JongoUpdate();
        updObj.setQuery("{'common.fields.code':#}");
        updObj.setQueryParameters(prodCode);
        if (updRsFlg) {
            String platformStatus = CmsConstants.PlatformActive.ToInStock.name().equals(activeStatus)
                    ? CmsConstants.PlatformStatus.InStock.name()
                    : CmsConstants.PlatformStatus.OnSale.name();
            updObj.setUpdate("{$set:{'platforms.P#.pStatus':#,'platforms.P#.pReallyStatus':#,'platforms.P#.pPublishError':'','platforms.P#.pPublishMessage':'','modified':#,'modifier':#}}");
            updObj.setUpdateParameters(cartId, platformStatus, cartId, platformStatus, cartId, cartId, DateTimeUtil.getNowTimeStamp(), userName);
        } else {
            updObj.setUpdate("{$set:{'platforms.P#.pPublishError':'Error','platforms.P#.pPublishMessage':#,'modified':#,'modifier':#}}");
            updObj.setUpdateParameters(cartId, cartId, errMsg, DateTimeUtil.getNowTimeStamp(), userName);
        }
        return updObj;
    }

    /**
     * 更新cms_bt_product_group表
     * @param cartId
     * @param numIId
     * @param activeStatus
     * @param userName
     * @return
     */
    private JongoUpdate updateCmsBtProductGroupInfo(Integer cartId, String numIId, String activeStatus, String userName) {
        // 在group表更新相关状态
        JongoUpdate updObj = new JongoUpdate();
        updObj.setQuery("{'cartId':#,'numIId':#}");
        updObj.setQueryParameters(cartId, numIId);
        if (CmsConstants.PlatformActive.ToOnSale.name().equals(activeStatus)) {
            // 上架
            updObj.setUpdate("{$set:{'platformActive':#,'platformStatus':#,'onSaleTime':#,'modified':#,'modifier':#}}");
            updObj.setUpdateParameters(activeStatus, CmsConstants.PlatformStatus.OnSale.name(), DateTimeUtil.getNow(), DateTimeUtil.getNowTimeStamp(), userName);
        } else if (CmsConstants.PlatformActive.ToInStock.name().equals(activeStatus)) {
            // 下架
            updObj.setUpdate("{$set:{'platformActive':#,'platformStatus':#,'inStockTime':#,'modified':#,'modifier':#}}");
            updObj.setUpdateParameters(activeStatus, CmsConstants.PlatformStatus.InStock.name(), DateTimeUtil.getNow(), DateTimeUtil.getNowTimeStamp(), userName);
        }
        return updObj;
    }
}
