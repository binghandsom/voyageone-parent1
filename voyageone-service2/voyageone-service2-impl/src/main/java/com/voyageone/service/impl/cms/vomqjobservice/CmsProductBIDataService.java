package com.voyageone.service.impl.cms.vomqjobservice;

import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.daoext.bi.BiVtSalesProductExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsProductBIDataMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从bi基础数据表取得产品的bi信息，并保存 (浏览量 访客数 加购件数 收藏人数)
 *
 * @author jiangjusheng on 2016/08/30
 * @version 2.0.0
 */
@Service
public class CmsProductBIDataService extends BaseService {
    @Autowired
    private BiVtSalesProductExt biDataDao;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    CmsMqSenderService cmsMqSenderService;

    // 每次查询取得的最大件数
    private final static int PAGE_LIMIT = 100;


    public void onStartup(CmsProductBIDataMQMessageBody messageMap) throws Exception {
        $info("CmsProcductBIDataService start... 参数" + JacksonUtil.bean2Json(messageMap));
        String channelId = StringUtils.trimToNull(messageMap.getChannelId());
        Integer cartId = messageMap.getCartId(); //(Integer) messageMap.get("cartId");

        // 先判断该店铺的cms_bt_product_cxxx表是否存在
        boolean exists = cmsBtProductDao.collectionExists(cmsBtProductDao.getCollectionName(channelId));
        if (!exists) {
            $warn("本店铺对应的cms_bt_product_cxxx表不存在！ channelId=" + channelId);
            throw  new BusinessException("本店铺对应的cms_bt_product_cxxx表不存在！ channelId=" + channelId);
        }

        Map<String, Object> sqlParams = new HashMap<>(6);
        Date lastDay = DateTimeUtil.addDays(DateTimeUtilBeijing.getCurrentBeiJingDate(), -1);
        int lastDayVal = NumberUtils.toInt(DateTimeUtil.format(lastDay, DateTimeUtil.DATE_TIME_FORMAT_3));
        sqlParams.put("endDate", lastDayVal);
        sqlParams.put("channelId", channelId);
        sqlParams.put("cartId", cartId);
        sqlParams.put("oLimit", PAGE_LIMIT);

        BulkJongoUpdateList bulkUpdList = new BulkJongoUpdateList(PAGE_LIMIT, cmsBtProductDao, channelId);
        // 必须判断是否是usjoi店铺，如果是，则在更新usjoi店铺中的商品信息时，同时要更新原始店铺中的商品信息
        boolean isUsJoi = Channels.isUsJoi(channelId);
        Map<String, String> orgChannelIdMap = null;
        if (isUsJoi) {
            orgChannelIdMap = new HashMap<>();
        }

        // 先统计昨天的数据
        sqlParams.put("staDate", lastDayVal);
        setBiData(bulkUpdList, sqlParams, 1, isUsJoi, orgChannelIdMap);

        // 再统计最近7天的数据
        lastDay = DateTimeUtil.addDays(DateTimeUtilBeijing.getCurrentBeiJingDate(), -7);
        int staDayVal = NumberUtils.toInt(DateTimeUtil.format(lastDay, DateTimeUtil.DATE_TIME_FORMAT_3));
        sqlParams.put("staDate", staDayVal);
        setBiData(bulkUpdList, sqlParams, 7, isUsJoi, orgChannelIdMap);

        // 再统计最近30天的数据
        lastDay = DateTimeUtil.addDays(DateTimeUtilBeijing.getCurrentBeiJingDate(), -30);
        staDayVal = NumberUtils.toInt(DateTimeUtil.format(lastDay, DateTimeUtil.DATE_TIME_FORMAT_3));
        sqlParams.put("staDate", staDayVal);
        setBiData(bulkUpdList, sqlParams, 30, isUsJoi, orgChannelIdMap);

        $info("CmsProcductBIDataService 结束");
    }

    /*
     * 查询并保存BI数据
     */
    private void setBiData(BulkJongoUpdateList bulkUpdList, Map<String, Object> sqlParams, int opeType, boolean isUsJoi, Map<String, String> orgChannelIdMap) {
        String channelId = (String) sqlParams.get("channelId");
        Integer cartId = (Integer) sqlParams.get("cartId");

        // 清空现有值
        JongoUpdate updObj = new JongoUpdate();
        updObj.setUpdate("{$set:{'bi.sum#.pv.cartId#':null,'bi.sum#.uv.cartId#':null, 'bi.sum#.gwc.cartId#':null,'bi.sum#.scs.cartId#':null, 'modified':#,'modifier':#}}");
        updObj.setUpdateParameters(opeType, cartId, opeType, cartId, opeType, cartId, opeType, cartId, DateTimeUtil.getNowTimeStamp(), CmsMqRoutingKey.CMS_BATCH_GET_PRODUCT_BI_DATA);
        // 批量更新
        WriteResult rs = cmsBtProductDao.updateMulti(updObj, channelId);
        if (rs != null) {
            $debug(String.format("更新product 清空现有bi数据 执行结果=%s", rs.toString()));
        }

        Map<String, BulkJongoUpdateList> bulkUpdListMap = null;
        if (isUsJoi) {
            bulkUpdListMap = new HashMap<>();
            // 若是usjoi店铺，同时清空原始店铺的bi数据
            clearUsjoiBiData(channelId, cartId, opeType);
        }

        long oIdx = 0;
        List<Map<String, Object>> biData = null;
        BulkWriteResult wrs = null;
        do {
            sqlParams.put("oIdx", oIdx * PAGE_LIMIT);
            biData = biDataDao.selectList(sqlParams);
            if (biData == null || biData.isEmpty()) {
                // 没有销量数据
                $warn("CmsProcductBIDataService 本店铺无BI数据 sqlParams=" + sqlParams.toString());
                break;
            }
            oIdx++;

            for (Map orderObj : biData) {
                String numIid = (String) orderObj.get("num_iid");

                updObj = new JongoUpdate();
                updObj.setQuery("{'platforms.P#.pNumIId':#,'platforms.P#.status':'Approved'}");
                updObj.setQueryParameters(cartId, numIid, cartId);
                updObj.setUpdate("{$set:{'bi.sum#.pv.cartId#':#,'bi.sum#.uv.cartId#':#, 'bi.sum#.gwc.cartId#':#,'bi.sum#.scs.cartId#':#, 'modified':#,'modifier':#}}");
                updObj.setUpdateParameters(opeType, cartId, orderObj.get("pv"), opeType, cartId, orderObj.get("uv"), opeType, cartId, orderObj.get("cartNums"), opeType, cartId, orderObj.get("collNums"), DateTimeUtil.getNowTimeStamp(), CmsMqRoutingKey.CMS_BATCH_GET_PRODUCT_BI_DATA);
                // 批量更新
                wrs = bulkUpdList.addBulkJongo(updObj);
                if (wrs != null) {
                    $debug(String.format("更新product sqlParams=%s 执行结果1=%s", sqlParams.toString(), wrs.toString()));
                }

                // 若是usjoi店铺，根据numiid和cartid取得原始channelid
                if (isUsJoi) {
                    String orgChannelId = getOrgChannelId(numIid, cartId, channelId, orgChannelIdMap);
                    if (orgChannelId == null) {
                        continue;
                    }
                    BulkJongoUpdateList orgUpdList = bulkUpdListMap.get(orgChannelId);
                    if (orgUpdList == null) {
                        orgUpdList = new BulkJongoUpdateList(PAGE_LIMIT, cmsBtProductDao, orgChannelId);
                        bulkUpdListMap.put(orgChannelId, orgUpdList);
                    }
                    // 批量更新
                    JongoUpdate updObj2 = new JongoUpdate();
                    updObj2.setQuery("{'platforms.P#.pNumIId':#,'platforms.P#.status':'Approved'}");
                    updObj2.setQueryParameters(channelId, numIid, channelId);
                    updObj2.setUpdate("{$set:{'bi.sum#.pv.cartId#':#,'bi.sum#.uv.cartId#':#, 'bi.sum#.gwc.cartId#':#,'bi.sum#.scs.cartId#':#, 'modified':#,'modifier':#}}");
                    updObj2.setUpdateParameters(opeType, channelId, orderObj.get("pv"), opeType, channelId, orderObj.get("uv"), opeType, channelId, orderObj.get("cartNums"), opeType, channelId, orderObj.get("collNums"), DateTimeUtil.getNowTimeStamp(), CmsMqRoutingKey.CMS_BATCH_GET_PRODUCT_BI_DATA);

                    wrs = orgUpdList.addBulkJongo(updObj2);
                    if (wrs != null) {
                        $debug(String.format("更新product orgChannelId=%s, sqlParams=%s 执行结果1=%s", orgChannelId, sqlParams.toString(), wrs.toString()));
                    }
                }
            }
        } while (biData.size() == PAGE_LIMIT);

        wrs = bulkUpdList.execute();
        if (wrs != null) {
            $debug(String.format("更新product sqlParams=%s 执行结果2=%s", sqlParams.toString(), wrs.toString()));
        }

        if (bulkUpdListMap != null) {
            for (Map.Entry<String, BulkJongoUpdateList> entry : bulkUpdListMap.entrySet()) {
                wrs = entry.getValue().execute();
                if (wrs != null) {
                    $debug(String.format("更新product orgChannelId=%s, sqlParams=%s 执行结果2=%s", entry.getKey(), sqlParams.toString(), wrs.toString()));
                }
            }
        }
    }

    /*
     * 根据numiid和cartid取得原始channelid
     */
    private String getOrgChannelId(String numIid, int cartId, String channelId, Map<String, String> orgChannelIdMap) {
        String orgChannelId = null;
        String orgKey = cartId + CacheKeyEnums.SKIP + numIid;
        orgChannelId = orgChannelIdMap.get(orgKey);
        if (orgChannelId != null) {
            return orgChannelId;
        }

        JongoQuery queryObj = new JongoQuery();
        queryObj.setQuery("{'platforms.P#.pNumIId':#}");
        queryObj.setParameters(cartId, numIid);
        queryObj.setProjection("{'orgChannelId':1}");

        CmsBtProductModel prodObj = cmsBtProductDao.selectOneWithQuery(queryObj, channelId);
        if (prodObj == null) {
            $error("查询原始channelid无结果 numiid=%s, cartid=%d, channelid=%s", numIid, cartId, channelId);
            return null;
        }
        orgChannelId = StringUtils.trimToNull(prodObj.getOrgChannelId());
        if (orgChannelId == null) {
            $error("原始channelid未设置 numiid=%s, cartid=%d, channelid=%s", numIid, cartId, channelId);
        } else {
            orgChannelIdMap.put(orgKey, orgChannelId);
        }
        return orgChannelId;
    }

    /*
     * 清空usjoi原始店铺的bi数据
     */
    private void clearUsjoiBiData(String channelId, int cartId, int opeType) {
        // 取得orgChannelId一览
        List<String> channelIdList = cmsBtProductDao.distinct("orgChannelId", cmsBtProductDao.getCollectionName(channelId), String.class);
        if (channelIdList == null || channelIdList.isEmpty()) {
            // 没有销量数据
            $warn("CmsProcductBIDataService 本店铺无orgChannelId数据 channelId=" + channelId);
            return;
        }

        for (String orgChannelId : channelIdList) {
            // 先判断该店铺的cms_bt_product_cxxx表是否存在
            boolean exists = cmsBtProductDao.collectionExists(cmsBtProductDao.getCollectionName(orgChannelId));
            if (!exists) {
                $warn("本店铺对应的cms_bt_product_cxxx表不存在！ orgChannelId=" + orgChannelId);
                continue;
            }
            // 清空现有值
            JongoUpdate updObj = new JongoUpdate();
            updObj.setUpdate("{$set:{'bi.sum#.pv.cartId#':null,'bi.sum#.uv.cartId#':null, 'bi.sum#.gwc.cartId#':null,'bi.sum#.scs.cartId#':null, 'modified':#,'modifier':#}}");
            updObj.setUpdateParameters(opeType, cartId, opeType, cartId, opeType, cartId, opeType, cartId, DateTimeUtil.getNowTimeStamp(), CmsMqRoutingKey.CMS_BATCH_GET_PRODUCT_BI_DATA);
            // 批量更新
            WriteResult rs = cmsBtProductDao.updateMulti(updObj, orgChannelId);
            if (rs != null) {
                $debug(String.format("更新product 清空现有bi数据 orgChannelId=%s, cartid=%d, 执行结果=%s", orgChannelId, cartId, rs.toString()));
            }
        }
    }

    /**
     *
     * @param channelId
     * @param cartId
     */
    public void sendMessage(String channelId, Integer cartId,String sender) {
        CmsProductBIDataMQMessageBody mqMessageBody = new CmsProductBIDataMQMessageBody();
        mqMessageBody.setChannelId(channelId);
        mqMessageBody.setCartId(cartId);
        mqMessageBody.setSender(sender);
        cmsMqSenderService.sendMessage(mqMessageBody);
    }
}
