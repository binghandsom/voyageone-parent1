package com.voyageone.task2.cms.service.platform;

import com.jd.open.api.sdk.response.ware.WareUpdateDelistingResponse;
import com.jd.open.api.sdk.response.ware.WareUpdateListingResponse;
import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import com.taobao.api.response.ItemUpdateDelistingResponse;
import com.taobao.api.response.ItemUpdateListingResponse;
import com.voyageone.base.dao.mongodb.JongoAggregate;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
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
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtPlatformActiveLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private final SxProductService sxProductService;
    private final DtWareService dtWareService;

    @Autowired
    public CmsPlatformActiveLogService(CmsBtProductGroupDao cmsBtProductGroupDao, JumeiSaleService jmSaleService,
                                       TbSaleService tbSaleService, JdSaleService jdSaleService,
                                       MongoSequenceService sequenceService, CmsBtPlatformActiveLogDao platformActiveLogDao,
                                       CmsBtProductDao cmsBtProductDao, SxProductService sxProductService, DtWareService dtWareService) {
        this.cmsBtProductGroupDao = cmsBtProductGroupDao;
        this.jmSaleService = jmSaleService;
        this.tbSaleService = tbSaleService;
        this.jdSaleService = jdSaleService;
        this.sequenceService = sequenceService;
        this.platformActiveLogDao = platformActiveLogDao;
        this.cmsBtProductDao = cmsBtProductDao;
        this.sxProductService = sxProductService;
        this.dtWareService = dtWareService;
    }

    public List<Map<String, String>> onStartup(Map<String, Object> messageMap) throws Exception {
        List<Map<String, String>> failList = new ArrayList<Map<String, String>>();

        $info("CmsPlatformActiceLogService start 参数 " + JacksonUtil.bean2Json(messageMap));

        String channelId = StringUtils.trimToNull((String) messageMap.get("channelId"));
        Collection<String> codeList = (Collection<String>) messageMap.get("codeList");
        String activeStatus = StringUtils.trimToNull((String) messageMap.get("activeStatus"));
        String userName = StringUtils.trimToNull((String) messageMap.get("creater"));
        Collection<Integer> cartIdList = (Collection<Integer>) messageMap.get("cartIdList");

        if (org.apache.commons.lang.StringUtils.isBlank(channelId) || CollectionUtils.isEmpty(codeList)
                || org.apache.commons.lang.StringUtils.isBlank(activeStatus) || org.apache.commons.lang.StringUtils.isBlank(userName)
                || CollectionUtils.isEmpty(cartIdList)) {
            $error("CmsPlatformActiceLogService 缺少参数");
            throw new BusinessException(String.format("CmsPlatformActiceLogService 缺少channelId/userName/activeStatus/codeList/cartIdList参数, params=%s", JacksonUtil.bean2Json(messageMap)));
        }

        long batchNo = sequenceService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_PLATFORMACTIVEJOB_ID);
        // 先记录上下架操作历史（必须以group为单位，不能用已选中的商品，会重复）
        for (Integer cartId : cartIdList) {
            JongoQuery queryObj = new JongoQuery();
            // 取得group信息
            queryObj.setQuery("{'productCodes':{$in:#},'cartId':#}");
            queryObj.setParameters(codeList, cartId);
            queryObj.setProjectionExt("mainProductCode", "productCodes", "groupId", "numIId", "platformMallId");
            List<CmsBtProductGroupModel> grpObjList = cmsBtProductGroupDao.select(queryObj, channelId);
            if (grpObjList == null || grpObjList.isEmpty()) {

                Map<String, String> failMap = new HashMap<String, String>();
                failMap.put(String.format("cartId=%d", cartId), String.format("产品对于的group不存在, cartId=%d, channelId=%s, codeList=%s", cartId, channelId, JacksonUtil.bean2Json(codeList)));
                failList.add(failMap);

                $error("CmsPlatformActiceLogService 产品对应的group不存在 cartId=%d, channelId=%s, codes=%s", cartId, channelId, codeList.toString());
                continue;
            }

            for (CmsBtProductGroupModel grpObj : grpObjList) {
                List<String> pCodeArr = grpObj.getProductCodes();
                if (pCodeArr == null || pCodeArr.isEmpty()) {

                    Map<String, String> failMap = new HashMap<String, String>();
                    failMap.put(String.format("group=%s", grpObj.get_id()), String.format("产品group下没有商品codes, cartId=%d, channelId=%s, grpInfo=%s", cartId, channelId, JacksonUtil.bean2Json(grpObj)));
                    failList.add(failMap);

                    $error("CmsPlatformActiceLogService cartId=%d, channelId=%s, grpInfo=%s", cartId, channelId, grpObj.toString());
                    continue;
                }
                for (String pCode : pCodeArr) {
                    CmsBtPlatformActiveLogModel model = new CmsBtPlatformActiveLogModel();
                    model.setBatchNo(batchNo);
                    model.setCartId(cartId);
                    model.setChannelId(channelId);
                    model.setActiveStatus(activeStatus);
                    model.setPlatformStatus(getPlatformStatus(pCode, channelId, cartId));
                    model.setComment((String) messageMap.get("comment"));
                    model.setGroupId(grpObj.getGroupId());
                    model.setMainProdCode(grpObj.getMainProductCode());
                    model.setProdCode(pCode);
                    String numIId = null;
                    if (CartEnums.Cart.JM.getId().equals(String.valueOf(cartId))) {
                        numIId = grpObj.getPlatformMallId();
                    } else if (CartEnums.Cart.DT.getId().equals(String.valueOf(cartId))) {
                        numIId = pCode; // 如果是分销平台，numIId设置为商品code
                    } else {
                        numIId = grpObj.getNumIId();
                    }
                    // model.setNumIId(CartEnums.Cart.JM.getId().equals(String.valueOf(cartId)) ? grpObj.getPlatformMallId() : grpObj.getNumIId());
                    model.setNumIId(numIId);
                    model.setResult("0");
                    model.setCreater(userName);
                    model.setCreated(DateTimeUtil.getNow());
                    model.setModified("");
                    model.setModifier("");

                    WriteResult rs = platformActiveLogDao.insert(model);
                    $debug("CmsPlatformActiceLogService cartId=%d, channelId=%s, code=%s platformActiveLog保存结果=%s", cartId, channelId, pCode, rs.toString());
                }
            }
        }

        String platformStatus = null;
        if (CmsConstants.PlatformActive.ToOnSale.name().equals(activeStatus)) {
            // 上架
            platformStatus = CmsConstants.PlatformStatus.OnSale.name();
        } else if (CmsConstants.PlatformActive.ToInStock.name().equals(activeStatus)) {
            // 下架
            platformStatus = CmsConstants.PlatformStatus.InStock.name();
        }
        // 调用实际的上下架Api，记录调用结果，在group表和product表更新相关状态
        BulkJongoUpdateList bulkList = new BulkJongoUpdateList(100, platformActiveLogDao, channelId);
        BulkJongoUpdateList bulkList2 = new BulkJongoUpdateList(100, cmsBtProductGroupDao, channelId);
        BulkJongoUpdateList bulkList3 = new BulkJongoUpdateList(100, cmsBtProductDao, channelId);

        boolean updRsFlg;
        String errMsg;
        BulkWriteResult rs;
        JongoQuery queryObj = new JongoQuery();
        for (Integer cartId : cartIdList) {
            ShopBean shopProp = Shops.getShop(channelId, cartId);
            if (shopProp == null) {

                Map<String, String> failMap = new HashMap<String, String>();
                failMap.put(String.format("channelId=%s, cartId=%d", channelId, cartId), String.format("获取到店铺信息失败, channelId=%s, cartId=%d", channelId, cartId));
                failList.add(failMap);

                $error("CmsPlatformActiceLogService 获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);
                continue;
            }

            // 先处理numIId不存在/商品lock/状态异常的情况
            queryObj.setQuery("{'cartId':#,'batchNo':#,'result':'0'}");
            queryObj.setParameters(cartId, batchNo);
            queryObj.setProjectionExt("prodCode", "mainProdCode", "numIId");
            List<CmsBtPlatformActiveLogModel> actLogList = platformActiveLogDao.select(queryObj, channelId);
            if (actLogList.isEmpty()) {

                Map<String, String> failMap = new HashMap<String, String>();
                failMap.put(String.format("cartId=%d, batchNo=%d", cartId, batchNo), String.format("找不到商品上下架历史, cartId=%d, batchNo=%d", cartId, batchNo));
                failList.add(failMap);

                $warn("CmsPlatformActiceLogService 找不到商品上下架历史 cartId=%d，batchNo=%d", cartId, batchNo);
                continue;
            }
            for (CmsBtPlatformActiveLogModel actLogObj : actLogList) {
                String failedComment = null;
                String prodCode = actLogObj.getProdCode();
                if (StringUtils.isEmpty(actLogObj.getNumIId())) {
                    $warn("CmsPlatformActiceLogService numIId(group)错误 channelId=%s, code=%s", channelId, prodCode);
                    failedComment = "NumIId为空";
                } else if (StringUtils.isEmpty(actLogObj.getMainProdCode())) {
                    $warn("CmsPlatformActiceLogService 产品(group)数据错误(没有MainProductCode数据) channelId=%s, code=%s", channelId, prodCode);
                    failedComment = "未设置主商品";
                } else {
                    // 取得商品信息
                    queryObj.setQuery("{'common.fields.code':#}");
                    queryObj.setParameters(actLogObj.getProdCode());
                    queryObj.setProjectionExt("lock", "platforms.P" + cartId + ".pNumIId", "platforms.P" + cartId + ".pPlatformMallId", "platforms.P" + cartId + ".status", "platforms.P" + cartId + ".pStatus", "platforms.P" + cartId + ".mainProductCode");
                    CmsBtProductModel prodObj = cmsBtProductDao.selectOneWithQuery(queryObj, channelId);
                    if (prodObj == null) {
                        $warn("CmsPlatformActiceLogService 找不到商品code cartId=%d", cartId);
                        failedComment = "商品不存在";
                    } else {
                        String mainCode = StringUtils.trimToNull(prodObj.getPlatformNotNull(cartId).getMainProductCode());
                        String numIId = CartEnums.Cart.JM.getId().equals(String.valueOf(cartId)) ? prodObj.getPlatformNotNull(cartId).getpPlatformMallId() : prodObj.getPlatformNotNull(cartId).getpNumIId();

                        if (numIId == null || StringUtil.isEmpty(numIId.trim()) || numIId.trim().equals("0")) {
                            $warn("CmsPlatformActiceLogService numIId错误 channelId=%s, code=%s", channelId, prodCode);
                            failedComment = "NumIId为空";
                        } else if (mainCode == null) {
                            $warn("CmsPlatformActiceLogService 产品数据错误(没有MainProductCode数据) channelId=%s, code=%s", channelId, prodCode);
                            failedComment = "未设置主商品";
                        } else if ("1".equals(StringUtils.trimToNull(prodObj.getLock()))) {
                            $warn("CmsPlatformActiceLogService 商品lock channelId=%s, code=%s", channelId, prodCode);
                            failedComment = "商品已锁定";
                        } else if (!CmsConstants.ProductStatus.Approved.name().equals(StringUtils.trimToNull(prodObj.getPlatformNotNull(cartId).getStatus()))) {
                            $warn("CmsPlatformActiceLogService 商品未审批 channelId=%s, code=%s", channelId, prodCode);
                            failedComment = "商品未审批";
                        } else if (!CmsConstants.PlatformStatus.OnSale.equals(prodObj.getPlatformNotNull(cartId).getpStatus()) && !CmsConstants.PlatformStatus.InStock.equals(prodObj.getPlatformNotNull(cartId).getpStatus())) {
                            $warn("CmsPlatformActiceLogService 商品还未上下架 channelId=%s, code=%s", channelId, prodCode);
                            failedComment = "商品平台状态不是\"在售\"或\"在库\"";
                        }
                    }
                }
                if (failedComment == null) {
                    continue;
                }
                JongoUpdate updObj = new JongoUpdate();
                updObj.setQuery("{'cartId':#,'prodCode':#,'batchNo':#}");
                updObj.setQueryParameters(cartId, prodCode, batchNo);
                updObj.setUpdate("{$set:{'result':#,'failedComment':#,'modified':#,'modifier':#}}");
                updObj.setUpdateParameters("3", failedComment, DateTimeUtil.getNowTimeStamp(), userName);
                rs = bulkList.addBulkJongo(updObj);
                if (rs != null) {
                    $debug("CmsPlatformActiceLogService cartId=%d, channelId=%s, code=%s, batchNo=%d platformActiveLog更新结果=%s", cartId, channelId, prodCode, batchNo, rs.toString());
                }
            }
            rs = bulkList.execute();
            if (rs != null) {
                $debug("CmsPlatformActiceLogService cartId=%d, channelId=%s, batchNo=%d platformActiveLog更新结果=%s", cartId, channelId, batchNo, rs.toString());
            }

            // 经过上面处理剩下的商品就是可以上下架的商品
            List<JongoAggregate> aggrList = new ArrayList<>();
            // 查询条件
            aggrList.add(new JongoAggregate("{ $match: {'cartId':#,'batchNo':#,'result':'0'} }", cartId, batchNo));
            // 分组
            String gp1 = "{ $group : { _id : '$numIId','pcdList':{$addToSet:'$prodCode'} } }";
            aggrList.add(new JongoAggregate(gp1));

            List<Map<String, Object>> prs = platformActiveLogDao.aggregateToMap(channelId, aggrList);
            if (prs == null || prs.isEmpty()) {

                Map<String, String> failMap = new HashMap<String, String>();
                failMap.put(String.format("cartId=%d, channelId=%s, batchNo=%d", cartId, channelId, batchNo), String.format("产品不存在, cartId=%d, channelId=%s, batchNo=%d", cartId, channelId, batchNo));
                failList.add(failMap);

                $error("CmsPlatformActiceLogService 产品不存在 cartId=%d, channelId=%s", cartId, channelId);
                continue;
            }

            // 然后对可以上下架的商品调用API并记录结果
            for (Map<String, Object> prodObj : prs) {
                String numIId = StringUtils.trimToNull((String) prodObj.get("_id"));
                $info("numIId=" + numIId + " activeStatus" + activeStatus);
                List<String> pcdList = (List<String>) prodObj.get("pcdList");
                if (numIId == null || pcdList == null || pcdList.isEmpty()) {

                    Map<String, String> failMap = new HashMap<String, String>();
                    failMap.put(String.format("cartId=%d, channelId=%s, data=%s", cartId, channelId, JacksonUtil.bean2Json(prodObj)), "数据错误");
                    failList.add(failMap);

                    $error("CmsPlatformActiceLogService 数据错误 cartId=%d, channelId=%s data=%s", cartId, channelId, prodObj.toString());
                    continue;
                }
                updRsFlg = false;
                errMsg = null;

                if (PlatFormEnums.PlatForm.TM.getId().equals(shopProp.getPlatform_id())) {
                    // 天猫国际上下架
                    if (CartEnums.Cart.CN.getValue() == cartId) {
                        // 如果是独立官网，不调用API
                        updRsFlg = true;
                        // 开始记入SxWorkLoad表
                        $debug("CmsPlatformActiceLogService 开始记入SxWorkLoad表");
                        sxProductService.insertSxWorkLoad(channelId, pcdList, cartId, userName);
                    } else {
                        if (CmsConstants.PlatformActive.ToOnSale.name().equals(activeStatus)) {
                            // 上架
                            ItemUpdateListingResponse response = tbSaleService.doWareUpdateListing(shopProp, numIId);
                            if (response == null) {
                                errMsg = "调用淘宝商品上架API失败";
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
                                errMsg = "调用淘宝商品下架API失败";
                            } else {
                                if (StringUtils.isEmpty(response.getErrorCode())) {
                                    updRsFlg = true;
                                } else {
                                    errMsg = response.getBody();
                                }
                            }
                        }
                    }

                } else if (PlatFormEnums.PlatForm.JD.getId().equals(shopProp.getPlatform_id())) {
                    // 京东国际上下架
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

                } else if (PlatFormEnums.PlatForm.JM.getId().equals(shopProp.getPlatform_id())) {
                    // 聚美上下架
                    if (CmsConstants.PlatformActive.ToOnSale.name().equals(activeStatus)) {
                        // 上架
                        HtMallStatusUpdateBatchResponse response = jmSaleService.doWareUpdateListing(shopProp, numIId);
                        if (response == null) {
                            errMsg = "调用聚美商品上架API失败";
                        } else {
                            if (response.isSuccess()) {
                                updRsFlg = true;
                            } else {
                                errMsg = response.getErrorMsg();
                            }
                        }

                    } else if (CmsConstants.PlatformActive.ToInStock.name().equals(activeStatus)) {
                        // 下架
                        HtMallStatusUpdateBatchResponse response = jmSaleService.doWareUpdateDelisting(shopProp, numIId);
                        if (response == null) {
                            errMsg = "调用聚美商品下架API失败";
                        } else {
                            if (response.isSuccess()) {
                                updRsFlg = true;
                            } else {
                                errMsg = response.getErrorMsg();
                            }
                        }
                    }

                } else if (PlatFormEnums.PlatForm.DT.getId().equals(shopProp.getPlatform_id())) { // 分销平台
                    // 分销上下架
                    if (CmsConstants.PlatformActive.ToOnSale.name().equals(activeStatus)) {
                        // 上架
                        String result = dtWareService.onShelfProduct(shopProp, numIId);
                        $info(String.format("调用分销平台上架API,channelId=%s,cartId=%s,numIId=%s结果=%s", shopProp.getOrder_channel_id(), shopProp.getCart_id(), numIId, result));
                        if (org.apache.commons.lang.StringUtils.isNotBlank(result)) {
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
                        if (!updRsFlg && org.apache.commons.lang.StringUtils.isBlank(errMsg)) {
                            errMsg = "调用分销平台上架API失败";
                        }
                    } else if (CmsConstants.PlatformActive.ToInStock.name().equals(activeStatus)) {
                        // 下架
                        String result = dtWareService.offShelfProduct(shopProp, numIId);
                        $info(String.format("调用分销平台下架API,channelId=%s,cartId=%s,numIId=%s结果=%s", shopProp.getOrder_channel_id(), shopProp.getCart_id(), numIId, result));
                        if (org.apache.commons.lang.StringUtils.isNotBlank(result)) {
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
                        if (!updRsFlg && org.apache.commons.lang.StringUtils.isBlank(errMsg)) {
                            errMsg = "调用分销平台下架API失败";
                        }
                    }

                } else {

                    Map<String, String> failMap = new HashMap<String, String>();
                    failMap.put(String.format("cartId=%d", cartId), String.format("不正确的平台 cartId=%d", cartId));
                    failList.add(failMap);

                    $error("CmsPlatformActiceLogService 不正确的平台 cartId=%d", cartId);
                }
                if (!updRsFlg) {
                    $error("CmsPlatformActiceLogService API调用返回错误结果");
                }

                // 保存调用结果
                for (String prodCode : pcdList) {
                    JongoUpdate updObj = new JongoUpdate();
                    updObj.setQuery("{'cartId':#,'prodCode':#,'batchNo':#}");
                    updObj.setQueryParameters(cartId, prodCode, batchNo);
                    if (updRsFlg) {
                        updObj.setUpdate("{$set:{'result':#,'modified':#,'modifier':#}}");
                        updObj.setUpdateParameters("1", DateTimeUtil.getNowTimeStamp(), userName);
                    } else {
                        updObj.setUpdate("{$set:{'result':#,'failedComment':#,'modified':#,'modifier':#}}");
                        updObj.setUpdateParameters("2", "调用API失败", DateTimeUtil.getNowTimeStamp(), userName);
                    }
                    rs = bulkList.addBulkJongo(updObj);
                    if (rs != null) {
                        $debug("CmsPlatformActiceLogService cartId=%d, channelId=%s, code=%s, batchNo=%d platformActiveLog更新结果=%s", cartId, channelId, prodCode, batchNo, rs.toString());
                    }
                }

                if (updRsFlg) {
                    // 在group表更新相关状态
                    JongoUpdate updObj2 = new JongoUpdate();
                    updObj2.setQuery("{'cartId':#,'numIId':#}");
                    updObj2.setQueryParameters(cartId, numIId);
                    if (CmsConstants.PlatformActive.ToOnSale.name().equals(activeStatus)) {
                        // 上架
                        updObj2.setUpdate("{$set:{'platformStatus':#,'onSaleTime':#,'modified':#,'modifier':#}}");
                        updObj2.setUpdateParameters(platformStatus, DateTimeUtil.getNow(), DateTimeUtil.getNowTimeStamp(), userName);
                    } else if (CmsConstants.PlatformActive.ToInStock.name().equals(activeStatus)) {
                        // 下架
                        updObj2.setUpdate("{$set:{'platformStatus':#,'inStockTime':#,'modified':#,'modifier':#}}");
                        updObj2.setUpdateParameters(platformStatus, DateTimeUtil.getNow(), DateTimeUtil.getNowTimeStamp(), userName);
                    }
                    rs = bulkList2.addBulkJongo(updObj2);
                    if (rs != null) {
                        $debug("CmsPlatformActiceLogService cartId=%d, channelId=%s, numIId=%s cmsBtProductGroup更新结果=%s", cartId, channelId, numIId, rs.toString());
                    }
                }

                // 更新product表时,要检查该group下商品的有效（numIId不存在/商品lock/状态异常）
                for (String prodCode : pcdList) {
                    JongoUpdate updObj3 = new JongoUpdate();
                    updObj3.setQuery("{'common.fields.code':#}");
                    updObj3.setQueryParameters(prodCode);
                    if (updRsFlg) {
                        updObj3.setUpdate("{$set:{'platforms.P#.pStatus':#,'platforms.P#.pReallyStatus':#,'platforms.P#.pPublishError':'','platforms.P#.pPublishMessage':'','modified':#,'modifier':#}}");
                        updObj3.setUpdateParameters(cartId, platformStatus, cartId, platformStatus, cartId, cartId, DateTimeUtil.getNowTimeStamp(), userName);
                    } else {
                        updObj3.setUpdate("{$set:{'platforms.P#.pPublishError':'Error','platforms.P#.pPublishMessage':#,'modified':#,'modifier':#}}");
                        updObj3.setUpdateParameters(cartId, cartId, errMsg, DateTimeUtil.getNowTimeStamp(), userName);
                    }
                    rs = bulkList3.addBulkJongo(updObj3);
                    if (rs != null) {
                        $debug("CmsPlatformActiceLogService cartId=%d, channelId=%s, code=%s cmsBtProduct更新结果=%s", cartId, channelId, prodCode, rs.toString());
                    }
                }
            }

            rs = bulkList.execute();
            if (rs != null) {
                $debug("CmsPlatformActiceLogService cartId=%d, channelId=%s, batchNo=%d platformActiveLog更新结果=%s", cartId, channelId, batchNo, rs.toString());
            }
            rs = bulkList2.execute();
            if (rs != null) {
                $debug("CmsPlatformActiceLogService cartId=%d, channelId=%s, batchNo=%d cmsBtProductGroup更新结果=%s", cartId, channelId, batchNo, rs.toString());
            }
            rs = bulkList3.execute();
            if (rs != null) {
                $debug("CmsPlatformActiceLogService cartId=%d, channelId=%s, batchNo=%d cmsBtProduct更新结果=%s", cartId, channelId, batchNo, rs.toString());
            }
        }
        return failList;
    }

    // 取得商品对应的平台状态
    private String getPlatformStatus(String prodCode, String channelId, int cartId) {
        JongoQuery queryObj = new JongoQuery();
        queryObj.setQuery("{'common.fields.code':#}");
        queryObj.setParameters(prodCode);
        queryObj.setProjectionExt("platforms.P" + cartId + ".pStatus");
        CmsBtProductModel prodObj = cmsBtProductDao.selectOneWithQuery(queryObj, channelId);
        if (prodObj == null) {
            $warn("CmsPlatformActiceLogService 找不到商品 channelId=%s，code=%s", channelId, prodCode);
            return null;
        }
        CmsConstants.PlatformStatus pStatus = prodObj.getPlatformNotNull(cartId).getpStatus();
        if (pStatus != null) {
            return pStatus.name();
        }
        return null;
    }

}
