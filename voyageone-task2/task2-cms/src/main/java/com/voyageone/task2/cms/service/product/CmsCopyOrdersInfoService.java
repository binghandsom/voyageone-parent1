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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    private final static int PAGE_LIMIT = 5000;

    /**
     * 导入产品订单信息
     */
    public void copyOrdersInfo(String taskName) {
        long oIdx = 0;
        List<Map> rs = null;
        DBCollection coll = cmsMtProdSalesHisDao.getDBCollection();

        JomgoQuery prodQryObj = new JomgoQuery();
        prodQryObj.setQuery("{'skus.skuCode':#}");
        prodQryObj.setProjection("{'fields.code':1,'_id':0}");
        do {
            rs = productDao.selectProductOrderCount(oIdx * PAGE_LIMIT, PAGE_LIMIT);
            oIdx ++;
            if (rs == null || rs.isEmpty()) {
                break;
            }

            BulkWriteOperation bbulkOpe = coll.initializeUnorderedBulkOperation();

            for (Map orderObj : rs) {
                BasicDBObject queryObj = new BasicDBObject();
                queryObj.put("cart_id", orderObj.get("cart_id"));
                queryObj.put("channel_id", orderObj.get("channel_id"));
                queryObj.put("sku", orderObj.get("sku"));
                queryObj.put("date", orderObj.get("date"));

                BasicDBObject updateValue = new BasicDBObject();
                updateValue.putAll(orderObj);
                updateValue.put("modifier", taskName);
                updateValue.put("modified", DateTimeUtil.getNow());

                // 根据sku找出其产品code（暂不考虑sku重复的情况）
                prodQryObj.setParameters(orderObj.get("sku"));
                CmsBtProductModel prodModel = cmsBtProductDao.selectOneWithQuery(prodQryObj, (String) orderObj.get("channel_id"));
                if (prodModel != null) {
                    updateValue.put("prodCode", prodModel.getFields().getCode());
                }
                BasicDBObject updateObj = new BasicDBObject("$set", updateValue);

                bbulkOpe.find(queryObj).upsert().update(updateObj);
            }
            if (rs.size() > 0) {
                BulkWriteResult rslt = bbulkOpe.execute();
                $debug(rslt.toString());
            }
        } while (rs.size() == PAGE_LIMIT);
    }

}
