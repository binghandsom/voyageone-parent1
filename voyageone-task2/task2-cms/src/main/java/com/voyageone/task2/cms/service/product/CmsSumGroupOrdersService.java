package com.voyageone.task2.cms.service.product;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JomgoAggregate;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.logger.VOAbsIssueLoggable;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.dao.cms.mongo.CmsMtProdSalesHisDao;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从订单历史记录表中统计出指定销量数据
 * 关联表:
 *   mongo: cms_bt_product_cxxx
 *   mongo: cms_mt_prod_sales_his
 *
 * @author jason.jiang on 2016/05/24
 * @version 2.0.0
 */
@Service
public class CmsSumGroupOrdersService extends VOAbsIssueLoggable {

    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    private CmsMtProdSalesHisDao cmsMtProdSalesHisDao;

    private final static int PAGE_LIMIT = 500;
    private static String queryGrpStr = "{$match:{'date':{$gte:#,$lte:#},'cart_id':#,'channel_id':#,'prodCode':{$in:#}}}";
    private static String queryGrpStr2 = "{$match:{'cart_id':#,'channel_id':#,'prodCode':{$in:#}}}";
    private static String queryGrpStr3 = "{$group:{_id:{cart_id:'$cart_id',channel_id:'$channel_id'},count:{$sum:'$qty'}}}";

    private static String queryCartStr = "{$match:{'date':{$gte:#,$lte:#},'channel_id':#,'prodCode':{$in:#}}}";
    private static String queryCartStr2 = "{$match:{'channel_id':#,'prodCode':{$in:#}}}";
    private static String queryCartStr3 = "{$group:{_id:'$channel_id',count:{$sum:'$qty'}}}";

    /**
     * 统计group级(各个cart)的销售数据，直接从[cms_mt_prod_sales_his]统计
     */
    public void sumPerCartGroupOrders(List<CmsBtProductGroupModel> list, String channelId, String begDate1, String begDate2, String endDate, String taskName) {
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        for (CmsBtProductGroupModel grpObj : list) {
            List<String> codeList = grpObj.getProductCodes();
            if (codeList == null || codeList.isEmpty()) {
                $warn(String.format("CmsFindProdOrdersInfoService 该group无产品code数据！ channel_id=%s groupId=%d", channelId, grpObj.getGroupId()));
                continue;
            }

            Map<String, Object> salesMap = new HashMap<>();

            // 7天销售group级数据
            Object[] params = new Object[]{begDate1, endDate, grpObj.getCartId(), channelId, codeList};
            List<Map> amt7days = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryGrpStr, params), new JomgoAggregate(queryGrpStr3, null));
            if (amt7days.isEmpty()) {
                salesMap.put("code_sum_7", 0);
            } else {
                int qty = ((Number) amt7days.get(0).get("count")).intValue();
                salesMap.put("code_sum_7", qty);
            }

            // 30天销售group级数据
            params = new Object[]{begDate2, endDate, grpObj.getCartId(), channelId, codeList};
            List<Map> amt30days = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryGrpStr, params), new JomgoAggregate(queryGrpStr3, null));
            if (amt30days.isEmpty()) {
                salesMap.put("code_sum_30", 0);
            } else {
                int qty = ((Number) amt30days.get(0).get("count")).intValue();
                salesMap.put("code_sum_30", qty);
            }

            // 所有销售group级数据
            params = new Object[]{grpObj.getCartId(), channelId, codeList};
            List<Map> amtall = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryGrpStr2, params), new JomgoAggregate(queryGrpStr3, null));
            if (amtall.isEmpty()) {
                $debug(String.format("CmsFindProdOrdersInfoService 该产品group无销售数据！ + channel_id=%s, cart_id=%d, groupId=%d", channelId, grpObj.getCartId(), grpObj.getGroupId()));
                salesMap.put("code_sum_all", 0);
            } else {
                int qty = ((Number) amtall.get(0).get("count")).intValue();
                salesMap.put("code_sum_all", qty);
            }

            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("groupId", grpObj.getGroupId());
            Map<String, Object> updateMap = new HashMap<>();
            updateMap.put("sales", salesMap);
            updateMap.put("modifier", taskName);
            updateMap.put("modified", DateTimeUtil.getNow());

            BulkUpdateModel updModel = new BulkUpdateModel();
            updModel.setQueryMap(queryMap);
            updModel.setUpdateMap(updateMap);
            bulkList.add(updModel);

            // 批量更新
            if (!bulkList.isEmpty() && bulkList.size() % PAGE_LIMIT == 0) {
                BulkWriteResult rs = cmsBtProductGroupDao.bulkUpdateWithMap(channelId, bulkList, taskName, "$set", false);
                $debug(String.format("更新group 店铺%s 执行数 %d, 执行结果 %s", channelId, bulkList.size(), rs.toString()));
                bulkList = new ArrayList<>();
            }
        } // end for group list

        // 批量更新
        if (!bulkList.isEmpty()) {
            BulkWriteResult rs = cmsBtProductGroupDao.bulkUpdateWithMap(channelId, bulkList, taskName, "$set", false);
            $debug(String.format("更新group 店铺%s 执行数 %d, 执行结果 %s", channelId, bulkList.size(), rs.toString()));
        }
    }

    /**
     * 统计group级(cart合计)的销售数据，直接从[cms_mt_prod_sales_his]统计
     */
    public void sumAllCartGroupOrders(List<CmsBtProductGroupModel> list, String channelId, String begDate1, String begDate2, String endDate, String taskName) {
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        for (CmsBtProductGroupModel grpObj : list) {
            List<String> codeList = grpObj.getProductCodes();
            if (codeList == null || codeList.isEmpty()) {
                continue;
            }

            Map<String, Object> salesMap = new HashMap<>();

            // 7天销售group级数据
            Object[] params = new Object[] { begDate1, endDate, channelId, codeList };
            List<Map> amt7days = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryCartStr, params), new JomgoAggregate(queryCartStr3, null));
            if (amt7days.isEmpty()) {
                salesMap.put("code_sum_7", 0);
            } else {
                int qty = ((Number) amt7days.get(0).get("count")).intValue();
                salesMap.put("code_sum_7", qty);
            }

            // 30天销售group级数据
            params = new Object[] { begDate2, endDate, channelId, codeList };
            List<Map> amt30days = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryCartStr, params), new JomgoAggregate(queryCartStr3, null));
            if (amt30days.isEmpty()) {
                salesMap.put("code_sum_30", 0);
            } else {
                int qty = ((Number) amt30days.get(0).get("count")).intValue();
                salesMap.put("code_sum_30", qty);
            }

            // 所有销售group级数据
            params = new Object[] { channelId, codeList };
            List<Map> amtall = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryCartStr2, params), new JomgoAggregate(queryCartStr3, null));
            if (amtall.isEmpty()) {
                $debug(String.format("CmsFindProdOrdersInfoService 该产品group无销售数据！ + channel_id=%s, groupId=%d", channelId, grpObj.getGroupId()));
                salesMap.put("code_sum_all", 0);
            } else {
                int qty = ((Number) amtall.get(0).get("count")).intValue();
                salesMap.put("code_sum_all", qty);
            }

            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("groupId", grpObj.getGroupId());
            Map<String, Object> updateMap = new HashMap<>();
            updateMap.put("sales", salesMap);
            updateMap.put("modifier", taskName);
            updateMap.put("modified", DateTimeUtil.getNow());

            BulkUpdateModel updModel = new BulkUpdateModel();
            updModel.setQueryMap(queryMap);
            updModel.setUpdateMap(updateMap);
            bulkList.add(updModel);

            // 批量更新
            if (!bulkList.isEmpty() && bulkList.size() % PAGE_LIMIT == 0) {
                BulkWriteResult rs = cmsBtProductGroupDao.bulkUpdateWithMap(channelId, bulkList, taskName, "$set",false);
                $debug(String.format("更新group(合计) 店铺%s 执行数 %d, 执行结果 %s", channelId, bulkList.size(), rs.toString()));
                bulkList = new ArrayList<>();
            }
        } // end for group list

        // 批量更新
        if (!bulkList.isEmpty()) {
            BulkWriteResult rs = cmsBtProductGroupDao.bulkUpdateWithMap(channelId, bulkList, taskName, "$set",false);
            $debug(String.format("更新group(合计) 店铺%s 执行数 %d, 执行结果 %s", channelId, bulkList.size(), rs.toString()));
        }
    }
}
