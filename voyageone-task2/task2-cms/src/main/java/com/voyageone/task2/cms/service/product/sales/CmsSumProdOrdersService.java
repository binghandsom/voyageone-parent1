package com.voyageone.task2.cms.service.product.sales;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoAggregate;
import com.voyageone.base.dao.mongodb.model.BulkModelUpdateList;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.logger.VOAbsIssueLoggable;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsMtProdSalesHisDao;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sales;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sales_Sku;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;

/**
 * 从订单历史记录表中统计出指定销量数据
 * 关联表:
 * mongo: cms_bt_product_cxxx
 * mongo: cms_mt_prod_sales_his
 *
 * @author jason.jiang on 2016/05/24
 * @author Wangtd on 2016/11/23
 * @since 2.0.0
 * @version 2.0.9
 */
@Service
public class CmsSumProdOrdersService extends VOAbsIssueLoggable {
    private final CmsBtProductDao cmsBtProductDao;
    private final CmsMtProdSalesHisDao cmsMtProdSalesHisDao;

    private final static int PAGE_LIMIT = 500;
    private static final String queryStr = "{$match:{'date':{$gte:#,$lte:#},'cart_id':{$in:#},'channel_id':#,'sku':{$in:#}}}";
    private static final String queryStr2 = "{$match:{'cart_id':{$in:#},'channel_id':#,'sku':{$in:#}}}";
    private static final String queryStr3 = "{$group:{_id:{cart_id:'$cart_id',channel_id:'$channel_id',sku:'$sku'},count:{$sum:'$qty'}}}";

    private static final String queryCodeStr = "{$match:{'date':{$gte:#,$lte:#},'cart_id':{$in:#},'channel_id':#,'prodCode':#}}";
    private static final String queryCodeStr2 = "{$match:{'cart_id':{$in:#},'channel_id':#,'prodCode':#}}";
    private static final String queryCodeStr3 = "{$group:{_id:{cart_id:'$cart_id',channel_id:'$channel_id',prodCode:'$prodCode'},count:{$sum:'$qty'}}}";

    @Autowired
    public CmsSumProdOrdersService(CmsMtProdSalesHisDao cmsMtProdSalesHisDao, CmsBtProductDao cmsBtProductDao) {
        this.cmsMtProdSalesHisDao = cmsMtProdSalesHisDao;
        this.cmsBtProductDao = cmsBtProductDao;
    }

    /**
     * 统计产品的销售数据
     */
    void sumProdOrders(List<CmsBtProductModel> prodList, String channelId, String begDate1, String begDate2, String endDate, String taskName) {
        BulkModelUpdateList bulkList = new BulkModelUpdateList(PAGE_LIMIT, cmsBtProductDao, channelId);
        for (CmsBtProductModel prodObj : prodList) {
            // 对每个产品统计其sku数据
            String prodCode = prodObj.getCommon().getFields().getCode();
            List<CmsBtProductModel_Sku> skusList = prodObj.getCommon().getSkus();
            if (skusList == null || skusList.isEmpty()) {
                $warn(String.format("CmsFindProdOrdersInfoService 该产品无sku数据！ + channel_id=%s, code=%s", channelId, prodCode));
                continue;
            }

            List<Integer> cartList = prodObj.getCartIdList();
            List<String> skuCodeList = new ArrayList<>();
            skusList.forEach(skuObj -> skuCodeList.add(skuObj.getSkuCode()));
            CmsBtProductModel_Sales saleObj = prodObj.getSales();
            Map<String, Object> salesMap = new HashMap<>();

            //--------------------------------------- sku销量数据 ----------------------------------
            // 7天sku销量数据
            Object[] params = new Object[]{begDate1, endDate, cartList, channelId, skuCodeList};
            List<Map<String, Object>> skuSum7List = summarySkuSales(queryStr, params, CmsBtProductModel_Sales.CODE_SUM_7);

            // 30天sku销量数据
            params = new Object[]{begDate2, endDate, cartList, channelId, skuCodeList};
            List<Map<String, Object>> skuSum30List = summarySkuSales(queryStr, params, CmsBtProductModel_Sales.CODE_SUM_30);

            // 今年的sku销量数据
            String today = new DateTime().toString(DateTimeUtil.DEFAULT_DATETIME_FORMAT);
            String firstDay = String.format("%s-01-01 00:00:00", Calendar.getInstance().getWeekYear());
            params = new Object[]{firstDay, today, cartList, channelId, skuCodeList};
            List<Map<String, Object>> skuSumYearList = summarySkuSales(queryStr, params, CmsBtProductModel_Sales.CODE_SUM_YEAR);

            // 所有sku销量数据
            params = new Object[]{cartList, channelId, skuCodeList};
            List<Map<String, Object>> skuSumAllList = summarySkuSales(queryStr2, params, CmsBtProductModel_Sales.CODE_SUM_ALL);
            if (skuSumAllList.isEmpty()) {
                $debug(String.format("CmsFindProdOrdersInfoService 该产品无销售数据！ + channel_id=%s, code=%s", channelId, prodCode));
                for (String skuCode : skuCodeList) {
                    for (Integer cartId : cartList) {
                        Map<String, Object> skuSalesMap = new HashMap<>();
                        skuSalesMap.put("skuCode", skuCode);
                        skuSalesMap.put("cartId", cartId);
                        skuSalesMap.put(CmsBtProductModel_Sales.CODE_SUM_ALL, 0);
                        skuSumAllList.add(skuSalesMap);
                    }
                }
            }

            // 合并sku销售数据
            for (Map<String, Object> sumInfo : skuSumAllList) {
                skuSum7List.stream().filter(sum7Info -> sumInfo.get("skuCode").equals(sum7Info.get("skuCode")) 
                		&& sumInfo.get("cartId").equals(sum7Info.get("cartId"))).forEach(sum7Info ->
                        	sumInfo.put(CmsBtProductModel_Sales.CODE_SUM_7, sum7Info.get(CmsBtProductModel_Sales.CODE_SUM_7))
                        );
                skuSum30List.stream().filter(sum30Info -> sumInfo.get("skuCode").equals(sum30Info.get("skuCode"))
                		&& sumInfo.get("cartId").equals(sum30Info.get("cartId"))).forEach(sum30Info ->
                        	sumInfo.put(CmsBtProductModel_Sales.CODE_SUM_30, sum30Info.get(CmsBtProductModel_Sales.CODE_SUM_30))
                        );
                skuSumYearList.stream().filter(sumYearInfo -> sumInfo.get("skuCode").equals(sumYearInfo.get("skuCode"))
                		&& sumInfo.get("cartId").equals(sumYearInfo.get("cartId"))).forEach(sumYearInfo ->
                        	sumInfo.put(CmsBtProductModel_Sales.CODE_SUM_YEAR, sumYearInfo.get(CmsBtProductModel_Sales.CODE_SUM_YEAR))
                        );
            }

            List<Map<String, Object>> skuCartSumList = new ArrayList<>();
            Set<String> skuCodeSet = new HashSet<>();
            Set<Integer> skuCartSet = new HashSet<>();
            for (Map<String, Object> sumInfo : skuSumAllList) {
                skuCodeSet.add((String) sumInfo.get("skuCode"));
                skuCartSet.add((Integer) sumInfo.get("cartId"));
                sumInfo.putIfAbsent(CmsBtProductModel_Sales.CODE_SUM_7, 0);
                sumInfo.putIfAbsent(CmsBtProductModel_Sales.CODE_SUM_30, 0);
                sumInfo.putIfAbsent(CmsBtProductModel_Sales.CODE_SUM_YEAR, 0);
                sumInfo.putIfAbsent(CmsBtProductModel_Sales.CODE_SUM_ALL, 0);
            }
            // 合并不在此次统计sku销量之列的sku
			if (saleObj != null) {
	            List<CmsBtProductModel_Sales_Sku> skus = saleObj.getSkus();
	            if (CollectionUtils.isNotEmpty(skus)) {
	            	for (CmsBtProductModel_Sales_Sku sku : skus) {
	            		if (0 != sku.getCartId()) {
	            			skuCodeSet.add(sku.getSkuCode());
	            			if (!skuCodeSet.contains(sku.getSkuCode()) || !skuCartSet.contains(sku.getCartId())) {
	            				skuSumAllList.add(sku);
		            		}
	            		}
	            	}
	            }
            }

            //------------------------------------ sku合计 -----------------------------------
            for (String skuCode : skuCodeSet) {
                int skuSum7 = 0, skuSum30 = 0, skuSumYear = 0, skuSumAll = 0;
                for (Map sumInfo : skuSumAllList) {
                    if (skuCode.equals(sumInfo.get("skuCode"))) {
                        skuSum7 += StringUtils.toIntValue((Integer) sumInfo.get(CmsBtProductModel_Sales.CODE_SUM_7));
                        skuSum30 += StringUtils.toIntValue((Integer) sumInfo.get(CmsBtProductModel_Sales.CODE_SUM_30));
                        skuSumYear += StringUtils.toIntValue((Integer) sumInfo.get(CmsBtProductModel_Sales.CODE_SUM_YEAR));
                        skuSumAll += StringUtils.toIntValue((Integer) sumInfo.get(CmsBtProductModel_Sales.CODE_SUM_ALL));
                    }
                }
                Map<String, Object> skuMap = new HashMap<>();
                skuMap.put("skuCode", skuCode);
                skuMap.put("cartId", 0);
                skuMap.put(CmsBtProductModel_Sales.CODE_SUM_7, skuSum7);
                skuMap.put(CmsBtProductModel_Sales.CODE_SUM_30, skuSum30);
                skuMap.put(CmsBtProductModel_Sales.CODE_SUM_YEAR, skuSumYear);
                skuMap.put(CmsBtProductModel_Sales.CODE_SUM_ALL, skuSumAll);
                skuCartSumList.add(skuMap);
            }
            skuSumAllList.addAll(skuCartSumList);
            salesMap.put("skus", skuSumAllList);
            
            //----------------------------------- code销售数据 -------------------------------------------
            // 7天销售code数据
            params = new Object[]{begDate1, endDate, cartList, channelId, prodCode};
            salesMap.put(CmsBtProductModel_Sales.CODE_SUM_7, summaryCodeSales(queryCodeStr, params));

            // 30天销售code数据
            params = new Object[]{begDate2, endDate, cartList, channelId, prodCode};
            salesMap.put(CmsBtProductModel_Sales.CODE_SUM_30, summaryCodeSales(queryCodeStr, params));

            // 今年code销量
            params = new Object[]{firstDay, today, cartList, channelId, prodCode};
            salesMap.put(CmsBtProductModel_Sales.CODE_SUM_YEAR, summaryCodeSales(queryCodeStr, params));

            // 所有销售code数据
            params = new Object[]{cartList, channelId, prodCode};
            Map<String, Object> codeSumAllMap = summaryCodeSales(queryCodeStr2, params);
            if (codeSumAllMap.isEmpty()) {
                Map<String, Object> sumallMap = new HashMap<>();
                for (Integer cartItem : cartList) {
                    sumallMap.put(CmsBtProductModel_Sales.CARTID + cartItem, 0);
                }
                sumallMap.put(CmsBtProductModel_Sales.CARTID_0, 0);
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_ALL, sumallMap);
            } else {
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_ALL, codeSumAllMap);
            }

            //---------------------------------- 重新处理统计数据-----------------------------------
            // 7天销量
            checkAndRecalculateSales(salesMap, cartList, CmsBtProductModel_Sales.CODE_SUM_7, saleObj);
            // 30天销量
            checkAndRecalculateSales(salesMap, cartList, CmsBtProductModel_Sales.CODE_SUM_30, saleObj);
            // 年销量
            checkAndRecalculateSales(salesMap, cartList, CmsBtProductModel_Sales.CODE_SUM_YEAR, saleObj);
            // 总销量
            checkAndRecalculateSales(salesMap, cartList, CmsBtProductModel_Sales.CODE_SUM_ALL, saleObj);

            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("common.fields.code", prodCode);
            Map<String, Object> updateMap = new HashMap<>();
            updateMap.put("sales", salesMap);
            updateMap.put("modifier", taskName);
            updateMap.put("modified", DateTimeUtil.getNow());

            BulkUpdateModel updModel = new BulkUpdateModel();
            updModel.setQueryMap(queryMap);
            updModel.setUpdateMap(updateMap);
            // 批量更新
            BulkWriteResult rs = bulkList.addBulkModel(updModel);
            if (rs != null) {
                $info(String.format("更新product 店铺%s 执行结果1 %s", channelId, rs.toString()));
            }
        } // end for product list

        // 批量更新
        BulkWriteResult rs = bulkList.execute();
        if (rs != null) {
            $info(String.format("更新product 店铺%s 执行结果2 %s", channelId, rs.toString()));
        }
    }
    
    /**
     * 统计SKU级别的（7天，30天，年，全部）销量
     * @param strQuery 查询语句
     * @param params 查询参数
     * @param codeSumLevel 统计的量级（7天，30天，年，全部）
     * @return 统计结果
     */
    private List<Map<String, Object>> summarySkuSales(String strQuery, Object[] params, String codeSumLevel) {
        List<Map<String, Object>> amtDays = cmsMtProdSalesHisDao.aggregateToMap(new JongoAggregate(strQuery, params),
        		new JongoAggregate(queryStr3));
        if (CollectionUtils.isNotEmpty(amtDays)) {
        	List<Map<String, Object>> skuSumList = new ArrayList<>();
        	
            for (Map<String, Object> hisInfo : amtDays) {
                int qty = ((Number) hisInfo.get("count")).intValue();
                Map groupKey = (Map) hisInfo.get("_id");

                Map<String, Object> skuSalesMap = new HashMap<>();
                skuSalesMap.put("skuCode", groupKey.get("sku"));
                skuSalesMap.put("cartId", groupKey.get("cart_id"));
                skuSalesMap.put(codeSumLevel, qty);
                skuSumList.add(skuSalesMap);
            }
            
            return skuSumList;
        }
        
        return Collections.emptyList();
    }
    
    /**
     * 统计Code级别的（7天，30天，年，全部）销量
     * @param strQuery 查询语句
     * @param params 查询参数
     * @return 统计结果
     */
    private Map<String, Object> summaryCodeSales(String strQuery, Object[] params) {
        List<Map<String, Object>> amtDays = cmsMtProdSalesHisDao.aggregateToMap(new JongoAggregate(strQuery, params),
        		new JongoAggregate(queryCodeStr3));
        if (CollectionUtils.isNotEmpty(amtDays)) {
            Map<String, Object> sumMap = new HashMap<>();
            int sum = 0;
            for (Map<String, Object> hisInfo : amtDays) {
                int qty = ((Number) hisInfo.get("count")).intValue();
                sum += qty;
                Map groupKey = (Map) hisInfo.get("_id");
                sumMap.put(CmsBtProductModel_Sales.CARTID + groupKey.get("cart_id"), qty);
            }
            sumMap.put(CmsBtProductModel_Sales.CARTID_0, sum);
            
            return sumMap;
        }
        
        return Collections.emptyMap();
    }
    
    /**
     * 给（7天，30天，年，全部）销量为空的Cart补0，并重新计算Cart0的销量
     * @param salesMap 销量数据
     * @param cartList Cart列表
     * @param codeSumLevel 统计的量级（7天，30天，年，全部）
     */
    private void checkAndRecalculateSales(Map<String, Object> salesMap, List<Integer> cartList, String codeSumLevel,
    		CmsBtProductModel_Sales saleObj) {
        Map<String, Object> sumMap = (Map<String, Object>) salesMap.get(codeSumLevel);
        if (MapUtils.isEmpty(sumMap)) {
            sumMap = new HashMap<>();
            for (Integer cartItem : cartList) {
                sumMap.put(CmsBtProductModel_Sales.CARTID + cartItem, 0);
            }
            salesMap.put(codeSumLevel, sumMap);
            // 设置Cart0的销量
            sumMap.put(CmsBtProductModel_Sales.CARTID_0, 0);
        } else {
            for (Integer cartItem : cartList) {
                sumMap.putIfAbsent(CmsBtProductModel_Sales.CARTID + cartItem, 0);
            }
            sumMap.putIfAbsent(CmsBtProductModel_Sales.CARTID_0, 0);
            
            if (saleObj != null) {
                // 加入不在此次统计之列的Cart销量
            	boolean isNotContained = false;
            	Map<String, Object> codeSumMap = (Map<String, Object>) saleObj.get(codeSumLevel);
            	if (MapUtils.isNotEmpty(codeSumMap)) {
            		for (String codeSum : codeSumMap.keySet()) {
            			if (!CmsBtProductModel_Sales.CARTID_0.equals(codeSum) && !sumMap.containsKey(codeSum)) {
            				isNotContained = true;
                			sumMap.put(codeSum, codeSumMap.get(codeSum));	
            			}
            		}
            	}
                // 若有未包含的Cart，则重新计算Cart0的销量
            	if (isNotContained) {
            		int sumAll = 0;
            		for (String sumKey : sumMap.keySet()) {
            			if (!CmsBtProductModel_Sales.CARTID_0.equals(sumKey)) {
            				sumAll += StringUtils.toIntValue((Integer) sumMap.get(sumKey));
            			}
            		}
            		sumMap.put(CmsBtProductModel_Sales.CARTID_0, sumAll);
            	}
            }
        }
    }

}
