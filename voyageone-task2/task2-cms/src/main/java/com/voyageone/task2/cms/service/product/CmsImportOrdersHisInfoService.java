package com.voyageone.task2.cms.service.product;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.DBCollection;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.service.dao.cms.mongo.CmsMtProdSalesHisDao;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
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
public class CmsImportOrdersHisInfoService extends BaseTaskService {

    @Autowired
    private ProductPublishDao productDao;
    @Autowired
    private CmsMtProdSalesHisDao cmsMtProdSalesHisDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsImportOrdersHisInfoJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        List<Map> rs = productDao.selectProductOrderCount();
        if (rs ==  null || rs.isEmpty()) {
            return;
        }

        DBCollection coll = cmsMtProdSalesHisDao.getDBCollection();
        BulkWriteOperation bbulkOpe = coll.initializeUnorderedBulkOperation();

        for (Map orderObj : rs) {
            BasicDBObject queryObj = new BasicDBObject();
            queryObj.put("cart_id", orderObj.get("cart_id"));
            queryObj.put("channel_id", orderObj.get("channel_id"));
            queryObj.put("sku", orderObj.get("sku"));
            queryObj.put("date", orderObj.get("date"));

            BasicDBObject updateValue = new BasicDBObject();
            updateValue.putAll(orderObj);
            BasicDBObject updateObj = new BasicDBObject("$set", updateValue);

            bbulkOpe.find(queryObj).upsert().update(updateObj);
        }
        if (rs.size() > 0) {
            BulkWriteResult rslt = bbulkOpe.execute();
            $debug(rslt.toString());
        }
    }

}
