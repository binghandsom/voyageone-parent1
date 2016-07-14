package com.voyageone.task2.cms.service.product;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.DBCollection;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsMtProdSalesHisDao;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.cms.dao.ProductPublishDao;
import org.apache.commons.lang3.StringUtils;
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
    private ProductPublishDao productDao;
    @Autowired
    private CmsMtProdSalesHisDao cmsMtProdSalesHisDao;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    private final static int PAGE_LIMIT = 1000;

    /**
     * 导入产品订单信息
     */
    public Map<String, Set<String>> copyOrdersInfo(String modifier) {
        $info("copyOrdersInfo start");
        long oIdx = 0;
        List<Map> rs;
        DBCollection coll = cmsMtProdSalesHisDao.getDBCollection();
        Map<String, Set<String>> prodCodeChannelMap = new HashMap<>();

        JomgoQuery prodQryObj = new JomgoQuery();
        prodQryObj.setQuery("{'common.skus.skuCode':#}");
        prodQryObj.setProjection("{'common.fields.code':1,'_id':0}");
        boolean hasdata = false;
        do {
            rs = productDao.selectProductOrderCount(oIdx * PAGE_LIMIT, PAGE_LIMIT);
            oIdx ++;
            if (rs == null || rs.isEmpty()) {
                break;
            }

            BulkWriteOperation bbulkOpe = coll.initializeUnorderedBulkOperation();
            hasdata = false;
            for (Map orderObj : rs) {
                String channelId = (String) orderObj.get("channel_id");
                String skuCode = (String) orderObj.get("sku");

                // 根据sku找出其产品code（暂不考虑sku重复的情况）
                prodQryObj.setParameters(skuCode);
                CmsBtProductModel prodModel = cmsBtProductDao.selectOneWithQuery(prodQryObj, channelId);
                if (prodModel != null) {
                    if (prodModel.getCommon() == null || prodModel.getCommon().getFields() == null) {
                        $warn("CmsCopyOrdersInfoService 产品数据不正确 channelId=%s, sku=%s", channelId, skuCode);
                        continue;
                    }
                    BasicDBObject queryObj = new BasicDBObject();
                    queryObj.put("cart_id", orderObj.get("cart_id"));
                    queryObj.put("channel_id", channelId);
                    queryObj.put("sku", skuCode);
                    queryObj.put("date", orderObj.get("date"));

                    BasicDBObject updateValue = new BasicDBObject();
                    updateValue.putAll(orderObj);
                    updateValue.put("modifier", modifier);
                    updateValue.put("modified", DateTimeUtil.getNow());
                    String productCode = StringUtils.trimToNull(prodModel.getCommon().getFields().getCode());
                    if (productCode == null) {
                        $warn("CmsCopyOrdersInfoService 产品数据不正确 没有code channelId=%s, sku=%s", channelId, skuCode);
                        continue;
                    }
                    updateValue.put("prodCode", productCode);

                    BasicDBObject updateObj = new BasicDBObject("$set", updateValue);
                    hasdata = true;
                    bbulkOpe.find(queryObj).upsert().update(updateObj);

                    // add prodCode 添加code和channelId到缓存
                    if (!prodCodeChannelMap.containsKey(channelId)) {
                        prodCodeChannelMap.put(channelId, new HashSet<>());
                    }
                    prodCodeChannelMap.get(channelId).add(productCode);
                } else {
                    $warn(String.format("CmsCopyOrdersInfoService 产品不存在 channelId=%s, sku=%s", channelId, skuCode));
                }
            }
            if (hasdata) {
                BulkWriteResult rslt = bbulkOpe.execute();
                $debug(String.format("copyOrdersInfo excute msg:%s", rslt.toString()));
                $info(String.format("copyOrdersInfo excute rows:%s", oIdx * PAGE_LIMIT));
            }
        } while (rs.size() == PAGE_LIMIT);
        $info("copyOrdersInfo end");
        return prodCodeChannelMap;
    }

}
