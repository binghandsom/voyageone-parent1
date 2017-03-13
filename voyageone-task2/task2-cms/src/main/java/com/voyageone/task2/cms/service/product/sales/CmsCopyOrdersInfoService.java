package com.voyageone.task2.cms.service.product.sales;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.DBCollection;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsMtProdSalesHisDao;
import com.voyageone.service.impl.cms.ChannelService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.cms.dao.ProductPublishDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 从oms系统导入产品前90天订单信息
 * 关联表:
 *   mysql: Synship.oms_bt_order_details
 *   mysql: Synship.oms_bt_orders
 *   mongo: cms_mt_prod_sales_his
 *
 * @author jason.jiang on 2016/05/24
 * @version 2.0.0
 */
@Service
public class CmsCopyOrdersInfoService extends VOAbsLoggable {

    @Autowired
    private ChannelService channelService;
    @Autowired
    private ProductPublishDao productDao;
    @Autowired
    private CmsMtProdSalesHisDao cmsMtProdSalesHisDao;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    private final static int PAGE_LIMIT = 1000;

    /**
     * 导入产品订单信息
     */
    public Map<String, Set<String>> copyOrdersInfo(String modifier, Map<String, Object> rsMap) {
        $info("copyOrdersInfo start");
        long staTime = System.currentTimeMillis();

        // 取得所有店铺
        List<OrderChannelBean> list = channelService.getChannelListBy(null, null, -1, "1");
        if (list == null || list.isEmpty()) {
            $error("CmsCopyOrdersInfoService 无店铺(channel)数据！");
            return null;
        }

        long oIdx;
        long qtySum;
        List<Map> rs;
        DBCollection coll = cmsMtProdSalesHisDao.getDBCollection();
        Map<String, Set<String>> prodCodeChannelMap = new HashMap<>();
        boolean hasData;

        // 根据skuCode取得商品code
        JongoQuery prodQryObj = new JongoQuery();
        prodQryObj.setQuery("{'common.skus.skuCode':#}");
        prodQryObj.setProjection("{'common.fields.code':1,'_id':0}");
        List<OrderChannelBean> list2 = new ArrayList<>();

        for (OrderChannelBean chnObj : list) {
            // 对每个店铺进行处理
            qtySum = 0;
            String channelId = chnObj.getOrder_channel_id();
            // 先判断该店铺的cms_bt_product_cxxx表是否存在
            boolean exists = cmsBtProductDao.collectionExists(cmsBtProductDao.getCollectionName(channelId));
            if (!exists) {
                $warn("CmsCopyOrdersInfoService 本店铺对应的cms_bt_product_cxxx表不存在！ channelId=" + channelId);
                continue;
            }

            // 取得该店铺的所有平台
            List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");
            if (cartList == null || cartList.isEmpty()) {
                $error("CmsCopyOrdersInfoService 本店铺无平台数据！ channelId=" + channelId);
                continue;
            }
            for (TypeChannelBean cartObj : cartList) {
                // 对指定店铺的每个平台进行处理
                int cartId = NumberUtils.toInt(cartObj.getValue());
                oIdx = 0;
                do {
                    rs = productDao.selectProductOrderCount(cartId, channelId, oIdx * PAGE_LIMIT, PAGE_LIMIT);
                    oIdx ++;
                    if (rs == null || rs.isEmpty()) {
                        // 没有销量数据
                        $warn("CmsCopyOrdersInfoService 本店铺无订单数据 cartId=%d, channelId=%s, 请求页数=%d", cartId, channelId, oIdx);
                        break;
                    }

                    BulkWriteOperation bbulkOpe = coll.initializeUnorderedBulkOperation();
                    hasData = false;
                    for (Map orderObj : rs) {
                        String skuCode = (String) orderObj.get("sku");
                        if (channelId.equals("001")) {
                            skuCode = skuCode.toLowerCase();
                            orderObj.put("sku", skuCode);
                        }
                        // 根据sku找出其产品code（暂不考虑sku重复的情况）
                        prodQryObj.setParameters(skuCode);
                        CmsBtProductModel prodModel = cmsBtProductDao.selectOneWithQuery(prodQryObj, channelId);
                        if (prodModel != null) {
                            if (prodModel.getCommon() == null || prodModel.getCommon().getFields() == null) {
                                $error("CmsCopyOrdersInfoService 产品数据不正确 channelId=%s, sku=%s", channelId, skuCode);
                                continue;
                            }
                            // 取得产品Code并决定值的正确性
                            String productCode = StringUtils.trimToNull(prodModel.getCommon().getFields().getCode());
                            if (productCode == null) {
                                $error("CmsCopyOrdersInfoService 产品数据不正确 没有code channelId=%s, sku=%s", channelId, skuCode);
                                continue;
                            }
                            
                            BasicDBObject queryObj = new BasicDBObject();
                            queryObj.put("cart_id", cartId);
                            queryObj.put("channel_id", channelId);
                            queryObj.put("sku", skuCode);
                            queryObj.put("date", orderObj.get("date"));

                            BasicDBObject updateValue = new BasicDBObject();
                            updateValue.putAll(orderObj);
                            updateValue.put("modifier", modifier);
                            updateValue.put("modified", DateTimeUtil.getNow());
                            updateValue.put("prodCode", productCode);

                            BasicDBObject updateObj = new BasicDBObject("$set", updateValue);
                            hasData = true;
                            bbulkOpe.find(queryObj).upsert().update(updateObj);

                            qtySum += (Long) orderObj.get("qty");
                            // add prodCode 添加code和channelId到缓存
                            if (!prodCodeChannelMap.containsKey(channelId)) {
                                prodCodeChannelMap.put(channelId, new HashSet<>());
                            }
                            prodCodeChannelMap.get(channelId).add(productCode);
                        } else {
                            $error(String.format("CmsCopyOrdersInfoService 产品不存在 channelId=%s, sku=%s", channelId, skuCode));
                        }
                    }
                    if (hasData) {
                        BulkWriteResult rslt = bbulkOpe.execute();
                        $debug(String.format("copyOrdersInfo excute msg:%s", rslt.toString()));
                        $info(String.format("copyOrdersInfo excute rows:%s", oIdx * PAGE_LIMIT));
                    } else {
                        $warn("CmsCopyOrdersInfoService 本次查询无有效订单数据 cartId=%d, channelId=%s, 请求页数=%d", cartId, channelId, oIdx);
                    }
                } while (rs.size() == PAGE_LIMIT);
            }

            chnObj.setModifier(Long.toString(qtySum));
            list2.add(chnObj);
        }

        $info("copyOrdersInfo end");
        rsMap.put("fstPhase", (System.currentTimeMillis() - staTime) / 1000);
        rsMap.put("fstPhaseRs", list2);
        return prodCodeChannelMap;
    }

}
