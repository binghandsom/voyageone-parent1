package com.voyageone.task2.cms.service.product;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JomgoAggregate;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.dao.cms.mongo.CmsMtProdSalesHisDao;
import com.voyageone.service.impl.cms.ChannelService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public class CmsFindProdOrdersInfoService extends BaseTaskService {

    @Autowired
    private ChannelService channelService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    private CmsMtProdSalesHisDao cmsMtProdSalesHisDao;

    private static String queryStr = "{$match:{'date':{$gte:#,$lte:#},'cart_id':{$in:#},'channel_id':#,'sku':{$in:#}}}";
    private static String queryStr2 = "{$match:{'cart_id':{$in:#},'channel_id':#,'sku':{$in:#}}}";
    private static String queryStr3 = "{$group:{_id:{cart_id:'$cart_id',channel_id:'$channel_id',sku:'$sku'},count:{$sum:'$qty'}}}";
    private static String grpqueryStr = "{'cartId':{$ne:1}},{'cartId':1,'groupId':1,'productCodes':1,'_id':1}";
    private static String prodqueryStr = "{'fields.code':{$in:[%s]}},{'sales':1,'fields.code':1,'_id':0}";

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsFindProdOrdersInfoJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        List<OrderChannelBean> list = channelService.getChannelListBy(null, null, -1, "1");
        if (list == null || list.isEmpty()) {
            $warn("CmsFindProdOrdersInfoService 无店铺数据！");
            return;
        }

        String endDate = DateTimeUtil.getNow(DateTimeUtil.DEFAULT_DATE_FORMAT);
        String begDate1 = DateTimeUtil.getDateBeforeDays(7);
        String begDate2 = DateTimeUtil.getDateBeforeDays(30);

        for (OrderChannelBean chnObj : list) {
            if (!"010".equals(chnObj.getOrder_channel_id())) {
                continue;
            }
            // 针对各店铺产品进行统计
            List<CmsBtProductModel> prodList = cmsBtProductDao.select("{},{'_id':0,'prodId':1,'skus.skuCode':1,'skus.skuCarts':1}", chnObj.getOrder_channel_id());
            if (prodList == null || prodList.isEmpty()) {
                $warn("CmsFindProdOrdersInfoService 该店铺无产品数据！ + channel_id=" + chnObj.getOrder_channel_id());
                continue;
            }

            List<BulkUpdateModel> bulkList = new ArrayList<>();
            for (CmsBtProductModel prodObj : prodList) {
                // 对每个产品统计其sku数据
                List<CmsBtProductModel_Sku> skusList = prodObj.getSkus();
                if (skusList == null || skusList.isEmpty()) {
                    $warn(String.format("CmsFindProdOrdersInfoService 该产品无sku数据！ + channel_id=%s, prodId=%d", chnObj.getOrder_channel_id(), prodObj.getProdId()));
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

                Map salesMap = new HashMap<>();
                List<Map> skuSum7List = new ArrayList<>();
                List<Map> skuSum30List = new ArrayList<>();
                List<Map> skuSumAllList = new ArrayList<>();

                // 7天销售数据
                Object[] params = new Object[] { begDate1, endDate, cartList2, chnObj.getOrder_channel_id(), skuCodeList };
                List<Map> amt7days = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryStr, params), new JomgoAggregate(queryStr3, null));
                if (!amt7days.isEmpty()) {
                    Map sum7Map = new HashMap<>();
                    int sum7 = 0;
                    for (Map hisInfo : amt7days) {
                        int qty = ((Number) hisInfo.get("count")).intValue();
                        sum7 += qty;
                        Map groupKey = (Map) hisInfo.get("_id");
                        sum7Map.put("cartId_" + groupKey.get("cart_id"), qty);

                        Map skuSalesMap = new HashMap<>();
                        skuSalesMap.put("skuCode", groupKey.get("sku"));
                        skuSalesMap.put("cartId", groupKey.get("cart_id"));
                        skuSalesMap.put("code_sum_7", qty);
                        skuSum7List.add(skuSalesMap);
                    }
                    sum7Map.put("cartId_0", sum7);
                    salesMap.put("code_sum_7", sum7Map);
                }

                // 30天销售数据
                params = new Object[] { begDate2, endDate, cartList2, chnObj.getOrder_channel_id(), skuCodeList };
                List<Map> amt30days = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryStr, params), new JomgoAggregate(queryStr3, null));
                if (!amt30days.isEmpty()) {
                    Map sum30Map = new HashMap<>();
                    int sum30 = 0;
                    for (Map hisInfo : amt30days) {
                        int qty = ((Number) hisInfo.get("count")).intValue();
                        sum30 += qty;
                        Map groupKey = (Map) hisInfo.get("_id");
                        sum30Map.put("cartId_" + groupKey.get("cart_id"), qty);

                        Map skuSalesMap = new HashMap<>();
                        skuSalesMap.put("skuCode", groupKey.get("sku"));
                        skuSalesMap.put("cartId", groupKey.get("cart_id"));
                        skuSalesMap.put("code_sum_30", qty);
                        skuSum30List.add(skuSalesMap);
                    }
                    sum30Map.put("cartId_0", sum30);
                    salesMap.put("code_sum_30", sum30Map);
                }

                // 所有销售数据
                params = new Object[] { cartList2, chnObj.getOrder_channel_id(), skuCodeList };
                List<Map> amtall = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryStr2, params), new JomgoAggregate(queryStr3, null));
                if (amtall.isEmpty()) {
                    $debug(String.format("CmsFindProdOrdersInfoService 该产品无销售数据！ + channel_id=%s, prodId=%d", chnObj.getOrder_channel_id(), prodObj.getProdId()));
                } else {
                    Map sumallMap = new HashMap<>();
                    int sumall = 0;
                    for (Map hisInfo : amtall) {
                        int qty = ((Number) hisInfo.get("count")).intValue();
                        sumall += qty;
                        Map groupKey = (Map) hisInfo.get("_id");
                        sumallMap.put("cartId_" + groupKey.get("cart_id"), qty);

                        Map skuSalesMap = new HashMap<>();
                        skuSalesMap.put("skuCode", groupKey.get("sku"));
                        skuSalesMap.put("cartId", groupKey.get("cart_id"));
                        skuSalesMap.put("code_sum_all", qty);
                        skuSumAllList.add(skuSalesMap);
                    }
                    sumallMap.put("cartId_0", sumall);
                    salesMap.put("code_sum_all", sumallMap);
                }

                // 合并sku销售数据
                for (Map sumInfo : skuSumAllList) {
                    for (Map sum7Info : skuSum7List) {
                        if (sumInfo.get("skuCode").equals(sum7Info.get("skuCode")) && sumInfo.get("cartId").equals(sum7Info.get("cartId"))) {
                            sumInfo.put("code_sum_7", sum7Info.get("code_sum_7"));
                        }
                    }
                    for (Map sum30Info : skuSum30List) {
                        if (sumInfo.get("skuCode").equals(sum30Info.get("skuCode")) && sumInfo.get("cartId").equals(sum30Info.get("cartId"))) {
                            sumInfo.put("code_sum_30", sum30Info.get("code_sum_30"));
                        }
                    }
                }

                List<Map> skuCartSumList = new ArrayList<>();
                Map<String, String> skuCodeMap = new HashMap<>();
                for (Map sumInfo : skuSumAllList) {
                    skuCodeMap.put((String) sumInfo.get("skuCode"), "");
                }
                for (String skuCode : skuCodeMap.keySet()) {
                    int skuSum7 = 0;
                    int skuSum30 = 0;
                    int skuSumAll = 0;
                    for (Map sumInfo : skuSumAllList) {
                        if (skuCode.equals(sumInfo.get("skuCode"))) {
                            skuSum7 += StringUtils.toIntValue((Integer) sumInfo.get("code_sum_7"));
                            skuSum30 += StringUtils.toIntValue((Integer) sumInfo.get("code_sum_30"));
                            skuSumAll += StringUtils.toIntValue((Integer) sumInfo.get("code_sum_all"));
                        }
                    }
                    Map<String, Object> skuMap = new HashMap<>();
                    skuMap.put("skuCode", skuCode);
                    skuMap.put("cartId", 0);
                    skuMap.put("code_sum_7", skuSum7);
                    skuMap.put("code_sum_30", skuSum30);
                    skuMap.put("code_sum_all", skuSumAll);
                    skuCartSumList.add(skuMap);
                }
                skuSumAllList.addAll(skuCartSumList);
                salesMap.put("skus", skuSumAllList);

                // 没有的数据补零
                Map sum7Map = (Map) salesMap.get("code_sum_7");
                if (sum7Map == null) {
                    sum7Map = new HashMap<>();
                    for (Integer cartItem : cartList2) {
                        sum7Map.put("cartId_" + cartItem, 0);
                    }
                    salesMap.put("code_sum_7", sum7Map);
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
                Map sum30Map = (Map) salesMap.get("code_sum_30");
                if (sum30Map == null) {
                    sum30Map = new HashMap<>();
                    for (Integer cartItem : cartList2) {
                        sum30Map.put("cartId_" + cartItem, 0);
                    }
                    salesMap.put("code_sum_30", sum30Map);
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
                Map sumAllMap = (Map) salesMap.get("code_sum_all");
                if (sumAllMap == null) {
                    sumAllMap = new HashMap<>();
                    for (Integer cartItem : cartList2) {
                        sumAllMap.put("cartId_" + cartItem, 0);
                    }
                    salesMap.put("code_sum_all", sumAllMap);
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

                Map queryMap = new HashMap<>();
                queryMap.put("prodId", prodObj.getProdId());
                Map updateMap = new HashMap<>();
                updateMap.put("sales", salesMap);

                BulkUpdateModel updModel = new BulkUpdateModel();
                updModel.setQueryMap(queryMap);
                updModel.setUpdateMap(updateMap);
                bulkList.add(updModel);
            }

            // 批量更新
            if (!bulkList.isEmpty()) {
                BulkWriteResult rs = cmsBtProductDao.bulkUpdateWithMap(chnObj.getOrder_channel_id(), bulkList, getTaskName(), "$set");
                $debug(String.format("店铺%s执行结果 %s", chnObj.getOrder_channel_id(), rs.toString()));
            }
        }

        // 再统计group的数据
        for (OrderChannelBean chnObj : list) {
            List<BulkUpdateModel> bulkList = new ArrayList<>();
            List<CmsBtProductGroupModel> getList = cmsBtProductGroupDao.select(grpqueryStr, chnObj.getOrder_channel_id());
            if (getList == null || getList.isEmpty()) {
                $warn(String.format("CmsFindProdOrdersInfoService 该店铺无group数据！ channel_id=%s", chnObj.getOrder_channel_id()));
                continue;
            }

            for (CmsBtProductGroupModel grpObj : getList) {
                List<String> codeList = grpObj.getProductCodes();
                if (codeList == null || codeList.isEmpty()) {
                    $warn(String.format("CmsFindProdOrdersInfoService 该group无产品code数据！ channel_id=%s groupId=%d", chnObj.getOrder_channel_id(), grpObj.getGroupId()));
                    continue;
                }

                int sum7 = 0;
                int sum30 = 0;
                int sumall = 0;
                String codeStr = codeList.stream().map(code -> "'" + code + "'").collect(Collectors.joining(","));
                List<CmsBtProductModel> prodList = cmsBtProductDao.select(String.format(prodqueryStr, codeStr), chnObj.getOrder_channel_id());
                if (prodList == null || prodList.isEmpty()) {
                    $warn(String.format("CmsFindProdOrdersInfoService 该group无产品code数据！ channel_id=%s groupId=%d codeStr=%s", chnObj.getOrder_channel_id(), grpObj.getGroupId(), codeList.toString()));
                    continue;
                }

                for (CmsBtProductModel prodObj : prodList) {
                    Map salesMap = prodObj.getSales();
                    if (salesMap == null || salesMap.isEmpty()) {
                        $debug(String.format("CmsFindProdOrdersInfoService 该产品无销售数据！ + channel_id=%s, code=%s", chnObj.getOrder_channel_id(), prodObj.getFields().getCode()));
                        continue;
                    }
                    Map sum7Map = (Map) salesMap.get("code_sum_7");
                    if (sum7Map != null) {
                        sum7 += StringUtils.toIntValue((Integer) sum7Map.get("cartId_" + grpObj.getCartId()));
                    }
                    Map sum30Map = (Map) salesMap.get("code_sum_30");
                    if (sum30Map != null) {
                        sum30 += StringUtils.toIntValue((Integer) sum30Map.get("cartId_" + grpObj.getCartId()));
                    }
                    Map sumallMap = (Map) salesMap.get("code_sum_all");
                    if (sumallMap != null) {
                        sumall += StringUtils.toIntValue((Integer) sumallMap.get("cartId_" + grpObj.getCartId()));
                    }
                }

                Map queryMap = new HashMap<>();
                queryMap.put("groupId", grpObj.getGroupId());
                Map updateMap = new HashMap<>();
                Map salesMap = new HashMap<>();
                salesMap.put("code_sum_7", sum7);
                salesMap.put("code_sum_30", sum30);
                salesMap.put("code_sum_all", sumall);
                updateMap.put("sales", salesMap);

                BulkUpdateModel updModel = new BulkUpdateModel();
                updModel.setQueryMap(queryMap);
                updModel.setUpdateMap(updateMap);
                bulkList.add(updModel);
            }

            // 批量更新
            if (!bulkList.isEmpty()) {
                BulkWriteResult rs = cmsBtProductGroupDao.bulkUpdateWithMap(chnObj.getOrder_channel_id(), bulkList, getTaskName(), "$set",false);
                $debug(String.format("店铺%s执行结果 %s", chnObj.getOrder_channel_id(), rs.toString()));
            }
        }
    }

}
