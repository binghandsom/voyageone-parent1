package com.voyageone.task2.cms.mqjob.usa;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoAggregate;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsMtProdSalesHisDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsSaleDataStatisticsMQMessageBody;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by james on 2017/7/20.
 * 自定义销量 数据准备
 */
@Service
@RabbitListener()
public class CmsSaleDataStatisticsMQJob extends TBaseMQCmsService<CmsSaleDataStatisticsMQMessageBody> {

    private final
    CmsMtProdSalesHisDao cmsMtProdSalesHisDao;

    private final CmsBtProductDao cmsBtProductDao;

    @Autowired
    public CmsSaleDataStatisticsMQJob(CmsMtProdSalesHisDao cmsMtProdSalesHisDao, CmsBtProductDao cmsBtProductDao) {
        this.cmsMtProdSalesHisDao = cmsMtProdSalesHisDao;
        this.cmsBtProductDao = cmsBtProductDao;
    }

    @Override
    public void onStartup(CmsSaleDataStatisticsMQMessageBody messageBody) throws Exception {

        cleanBeforeSaleData(messageBody.getChannelId(), messageBody.getCartId());
        Criteria criteria = new Criteria("channel_id").is(messageBody.getChannelId()).and("date").gte(messageBody.getStartDate()).lte(messageBody.getEndDate());

        if(messageBody.getCartId() != null && messageBody.getCartId() != 0){
            criteria = criteria.and("cart_id").is(messageBody.getCartId());
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("prodCode").sum("qty").as("sale"));

        AggregationResults<SaleMode> items = cmsMtProdSalesHisDao.aggregateToObj(aggregation, SaleMode.class);
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        for (Iterator<SaleMode> iterator = items.iterator(); iterator.hasNext();) {
            SaleMode item = iterator.next();
            HashMap<String, Object> queryMap = new HashMap<>();
            queryMap.put("common.fields.code", item.get_id());
            HashMap<String, Object> updateMap = new HashMap<>();
            updateMap.put("sales.P" + messageBody.getCartId(), item.getSale());
            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
            if(bulkList.size() == 500) {
                BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkUpdateWithMap(messageBody.getChannelId(), bulkList, getTaskName(), "$set");
                $info(bulkWriteResult.toString());
                bulkList.clear();
            }
        }
        if(bulkList.size() > 0) {
            BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkUpdateWithMap(messageBody.getChannelId(), bulkList, getTaskName(), "$set");
            $info(bulkWriteResult.toString());
            bulkList.clear();
        }
    }

    private void cleanBeforeSaleData(String channelId, Integer cartId){
        JongoUpdate jongoUpdate = new JongoUpdate();
        jongoUpdate.setQuery("{'sales.P"+cartId+"':{$ne:0}}");
        jongoUpdate.setUpdate("{$set:{'sales.P"+cartId+"':0}}");
        cmsBtProductDao.updateMulti(jongoUpdate,channelId);
    }
    class SaleMode{
        String _id;
        String sale;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getSale() {
            return sale;
        }

        public void setSale(String sale) {
            this.sale = sale;
        }

    }
}
