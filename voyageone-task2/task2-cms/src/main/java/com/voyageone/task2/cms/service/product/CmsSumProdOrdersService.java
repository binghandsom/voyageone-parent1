package com.voyageone.task2.cms.service.product;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JomgoAggregate;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.logger.VOAbsIssueLoggable;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsMtProdSalesHisDao;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sales;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 从订单历史记录表中统计出指定销量数据
 * 关联表:
 * mongo: cms_bt_product_cxxx
 * mongo: cms_mt_prod_sales_his
 *
 * @author jason.jiang on 2016/05/24
 * @version 2.0.0
 */
@Service
public class CmsSumProdOrdersService extends VOAbsIssueLoggable {

    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsMtProdSalesHisDao cmsMtProdSalesHisDao;

    private final static int PAGE_LIMIT = 500;
    private static final String queryStr = "{$match:{'date':{$gte:#,$lte:#},'cart_id':{$in:#},'channel_id':#,'sku':{$in:#}}}";
    private static final String queryStr2 = "{$match:{'cart_id':{$in:#},'channel_id':#,'sku':{$in:#}}}";
    private static final String queryStr3 = "{$group:{_id:{cart_id:'$cart_id',channel_id:'$channel_id',sku:'$sku'},count:{$sum:'$qty'}}}";

    private static final String queryCodeStr = "{$match:{'date':{$gte:#,$lte:#},'cart_id':{$in:#},'channel_id':#,'prodCode':#}}";
    private static final String queryCodeStr2 = "{$match:{'cart_id':{$in:#},'channel_id':#,'prodCode':#}}";
    private static final String queryCodeStr3 = "{$group:{_id:{cart_id:'$cart_id',channel_id:'$channel_id',prodCode:'$prodCode'},count:{$sum:'$qty'}}}";

    /**
     * 统计产品的销售数据
     */
    public void sumProdOrders(List<CmsBtProductModel> prodList, String channelId, String begDate1, String begDate2, String endDate, String taskName) {
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        for (CmsBtProductModel prodObj : prodList) {
            // 对每个产品统计其sku数据
            String prodCode = prodObj.getFields().getCode();
            List<CmsBtProductModel_Sku> skusList = prodObj.getSkus();
            if (skusList == null || skusList.isEmpty()) {
                $warn(String.format("CmsFindProdOrdersInfoService 该产品无sku数据！ + channel_id=%s, code=%s", channelId, prodCode));
                continue;
            }

            List<Integer> cartList = new ArrayList<>();
            List<String> skuCodeList = new ArrayList<>();
            skusList.forEach(skuObj -> {
                skuCodeList.add(skuObj.getSkuCode());
                cartList.addAll(skuObj.getSkuCarts());
            });
            List<Integer> cartList2 = cartList.stream().distinct().collect(Collectors.toList());
            if (cartList.isEmpty() || cartList2.isEmpty()) {
                continue;
            }

            Map<String, Object> salesMap = new HashMap<>();
            List<Map<String, Object>> skuSum7List = new ArrayList<>();
            List<Map<String, Object>> skuSum30List = new ArrayList<>();
            List<Map<String, Object>> skuSumAllList = new ArrayList<>();

            // 7天销售sku数据
            Object[] params = new Object[]{begDate1, endDate, cartList2, channelId, skuCodeList};
            List<Map<String, Object>> amt7days = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryStr, params), new JomgoAggregate(queryStr3));
            if (!amt7days.isEmpty()) {
                for (Map hisInfo : amt7days) {
                    int qty = ((Number) hisInfo.get("count")).intValue();
                    Map groupKey = (Map) hisInfo.get("_id");

                    Map<String, Object> skuSalesMap = new HashMap<>();
                    skuSalesMap.put("skuCode", groupKey.get("sku"));
                    skuSalesMap.put("cartId", groupKey.get("cart_id"));
                    skuSalesMap.put(CmsBtProductModel_Sales.CODE_SUM_7, qty);
                    skuSum7List.add(skuSalesMap);
                }
            }

            // 30天销售sku数据
            params = new Object[]{begDate2, endDate, cartList2, channelId, skuCodeList};
            List<Map<String, Object>> amt30days = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryStr, params), new JomgoAggregate(queryStr3));
            if (!amt30days.isEmpty()) {
                for (Map hisInfo : amt30days) {
                    int qty = ((Number) hisInfo.get("count")).intValue();
                    Map groupKey = (Map) hisInfo.get("_id");

                    Map<String, Object> skuSalesMap = new HashMap<>();
                    skuSalesMap.put("skuCode", groupKey.get("sku"));
                    skuSalesMap.put("cartId", groupKey.get("cart_id"));
                    skuSalesMap.put(CmsBtProductModel_Sales.CODE_SUM_30, qty);
                    skuSum30List.add(skuSalesMap);
                }
            }

            // 所有销售sku数据
            params = new Object[]{cartList2, channelId, skuCodeList};
            List<Map<String, Object>> amtall = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryStr2, params), new JomgoAggregate(queryStr3));
            if (amtall.isEmpty()) {
                $debug(String.format("CmsFindProdOrdersInfoService 该产品无销售数据！ + channel_id=%s, code=%s", channelId, prodCode));
                for (String skuCode : skuCodeList) {
                    for (Integer cartId : cartList2) {
                        Map<String, Object> skuSalesMap = new HashMap<>();
                        skuSalesMap.put("skuCode", skuCode);
                        skuSalesMap.put("cartId", cartId);
                        skuSalesMap.put(CmsBtProductModel_Sales.CODE_SUM_ALL, 0);
                        skuSumAllList.add(skuSalesMap);
                    }
                }
            } else {
                for (Map hisInfo : amtall) {
                    int qty = ((Number) hisInfo.get("count")).intValue();
                    Map groupKey = (Map) hisInfo.get("_id");

                    Map<String, Object> skuSalesMap = new HashMap<>();
                    skuSalesMap.put("skuCode", groupKey.get("sku"));
                    skuSalesMap.put("cartId", groupKey.get("cart_id"));
                    skuSalesMap.put(CmsBtProductModel_Sales.CODE_SUM_ALL, qty);
                    skuSumAllList.add(skuSalesMap);
                }
            }

            // 合并sku销售数据
            for (Map<String, Object> sumInfo : skuSumAllList) {
                for (Map sum7Info : skuSum7List) {
                    if (sumInfo.get("skuCode").equals(sum7Info.get("skuCode")) && sumInfo.get("cartId").equals(sum7Info.get("cartId"))) {
                        sumInfo.put(CmsBtProductModel_Sales.CODE_SUM_7, sum7Info.get(CmsBtProductModel_Sales.CODE_SUM_7));
                    }
                }
                for (Map sum30Info : skuSum30List) {
                    if (sumInfo.get("skuCode").equals(sum30Info.get("skuCode")) && sumInfo.get("cartId").equals(sum30Info.get("cartId"))) {
                        sumInfo.put(CmsBtProductModel_Sales.CODE_SUM_30, sum30Info.get(CmsBtProductModel_Sales.CODE_SUM_30));
                    }
                }
            }

            List<Map<String, Object>> skuCartSumList = new ArrayList<>();
            Map<String, String> skuCodeMap = new HashMap<>();
            for (Map<String, Object> sumInfo : skuSumAllList) {
                skuCodeMap.put((String) sumInfo.get("skuCode"), "");
                if (sumInfo.get(CmsBtProductModel_Sales.CODE_SUM_7) == null) {
                    sumInfo.put(CmsBtProductModel_Sales.CODE_SUM_7, 0);
                }
                if (sumInfo.get(CmsBtProductModel_Sales.CODE_SUM_30) == null) {
                    sumInfo.put(CmsBtProductModel_Sales.CODE_SUM_30, 0);
                }
                if (sumInfo.get(CmsBtProductModel_Sales.CODE_SUM_ALL) == null) {
                    sumInfo.put(CmsBtProductModel_Sales.CODE_SUM_ALL, 0);
                }
            }

            // 再统计产品code级别的数据，由于是多维度的统计，由上面的sku数据合并较复杂，不如直接统计
            // 7天销售code数据
            params = new Object[]{begDate1, endDate, cartList2, channelId, prodCode};
            amt7days = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryCodeStr, params), new JomgoAggregate(queryCodeStr3));
            if (!amt7days.isEmpty()) {
                Map<String, Object> sum7Map = new HashMap<>();
                int sum7 = 0;
                for (Map hisInfo : amt7days) {
                    int qty = ((Number) hisInfo.get("count")).intValue();
                    sum7 += qty;
                    Map groupKey = (Map) hisInfo.get("_id");
                    sum7Map.put("cartId_" + groupKey.get("cart_id"), qty);
                }
                sum7Map.put("cartId_0", sum7);
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_7, sum7Map);
            }

            // 30天销售code数据
            params = new Object[]{begDate2, endDate, cartList2, channelId, prodCode};
            amt30days = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryCodeStr, params), new JomgoAggregate(queryCodeStr3));
            if (!amt30days.isEmpty()) {
                Map<String, Object> sum30Map = new HashMap<>();
                int sum30 = 0;
                for (Map hisInfo : amt30days) {
                    int qty = ((Number) hisInfo.get("count")).intValue();
                    sum30 += qty;
                    Map groupKey = (Map) hisInfo.get("_id");
                    sum30Map.put("cartId_" + groupKey.get("cart_id"), qty);
                }
                sum30Map.put("cartId_0", sum30);
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_30, sum30Map);
            }

            // 所有销售code数据
            params = new Object[]{cartList2, channelId, prodCode};
            amtall = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryCodeStr2, params), new JomgoAggregate(queryCodeStr3));
            if (amtall.isEmpty()) {
                Map<String, Object> sumallMap = new HashMap<>();
                for (Integer cartItem : cartList2) {
                    sumallMap.put("cartId_" + cartItem, 0);
                }
                sumallMap.put("cartId_0", 0);
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_ALL, sumallMap);
            } else {
                Map<String, Object> sumallMap = new HashMap<>();
                int sumall = 0;
                for (Map hisInfo : amtall) {
                    int qty = ((Number) hisInfo.get("count")).intValue();
                    sumall += qty;
                    Map groupKey = (Map) hisInfo.get("_id");
                    sumallMap.put("cartId_" + groupKey.get("cart_id"), qty);
                }
                sumallMap.put("cartId_0", sumall);
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_ALL, sumallMap);
            }

            // sku合计
            for (String skuCode : skuCodeMap.keySet()) {
                int skuSum7 = 0;
                int skuSum30 = 0;
                int skuSumAll = 0;
                for (Map sumInfo : skuSumAllList) {
                    if (skuCode.equals(sumInfo.get("skuCode"))) {
                        skuSum7 += StringUtils.toIntValue((Integer) sumInfo.get(CmsBtProductModel_Sales.CODE_SUM_7));
                        skuSum30 += StringUtils.toIntValue((Integer) sumInfo.get(CmsBtProductModel_Sales.CODE_SUM_30));
                        skuSumAll += StringUtils.toIntValue((Integer) sumInfo.get(CmsBtProductModel_Sales.CODE_SUM_ALL));
                    }
                }
                Map<String, Object> skuMap = new HashMap<>();
                skuMap.put("skuCode", skuCode);
                skuMap.put("cartId", 0);
                skuMap.put(CmsBtProductModel_Sales.CODE_SUM_7, skuSum7);
                skuMap.put(CmsBtProductModel_Sales.CODE_SUM_30, skuSum30);
                skuMap.put(CmsBtProductModel_Sales.CODE_SUM_ALL, skuSumAll);
                skuCartSumList.add(skuMap);
            }
            skuSumAllList.addAll(skuCartSumList);
            salesMap.put("skus", skuSumAllList);

            // 没有的数据补零
            Map<String, Object> sum7Map = (Map<String, Object>) salesMap.get(CmsBtProductModel_Sales.CODE_SUM_7);
            if (sum7Map == null) {
                sum7Map = new HashMap<>();
                for (Integer cartItem : cartList2) {
                    sum7Map.put("cartId_" + cartItem, 0);
                }
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_7, sum7Map);
            } else {
                for (Integer cartItem : cartList2) {
                    Integer qty = (Integer) sum7Map.get("cartId_" + cartItem);
                    if (qty == null) {
                        sum7Map.put("cartId_" + cartItem, 0);
                    }
                }
            }
            Integer qty0 = (Integer) sum7Map.get("cartId_0");
            if (qty0 == null) {
                sum7Map.put("cartId_0", 0);
            }
            Map<String, Object> sum30Map = (Map<String, Object>) salesMap.get(CmsBtProductModel_Sales.CODE_SUM_30);
            if (sum30Map == null) {
                sum30Map = new HashMap<>();
                for (Integer cartItem : cartList2) {
                    sum30Map.put("cartId_" + cartItem, 0);
                }
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_30, sum30Map);
            } else {
                for (Integer cartItem : cartList2) {
                    Integer qty = (Integer) sum30Map.get("cartId_" + cartItem);
                    if (qty == null) {
                        sum30Map.put("cartId_" + cartItem, 0);
                    }
                }
            }
            qty0 = (Integer) sum30Map.get("cartId_0");
            if (qty0 == null) {
                sum30Map.put("cartId_0", 0);
            }
            Map<String, Object> sumAllMap = (Map<String, Object>) salesMap.get(CmsBtProductModel_Sales.CODE_SUM_ALL);
            if (sumAllMap == null) {
                sumAllMap = new HashMap<>();
                for (Integer cartItem : cartList2) {
                    sumAllMap.put("cartId_" + cartItem, 0);
                }
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_ALL, sumAllMap);
            } else {
                for (Integer cartItem : cartList2) {
                    Integer qty = (Integer) sumAllMap.get("cartId_" + cartItem);
                    if (qty == null) {
                        sumAllMap.put("cartId_" + cartItem, 0);
                    }
                }
            }
            qty0 = (Integer) sumAllMap.get("cartId_0");
            if (qty0 == null) {
                sumAllMap.put("cartId_0", 0);
            }

            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("fields.code", prodCode);
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
                BulkWriteResult rs = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, taskName, "$set", false);
                $debug(String.format("更新product 店铺%s 执行数 %d, 执行结果 %s", channelId, bulkList.size(), rs.toString()));
                bulkList = new ArrayList<>();
            }
        } // end for product list

        // 批量更新
        if (!bulkList.isEmpty()) {
            BulkWriteResult rs = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, taskName, "$set");
            $debug(String.format("更新product 店铺%s 执行数 %d, 执行结果 %s", channelId, bulkList.size(), rs.toString()));
        }
    }

}
