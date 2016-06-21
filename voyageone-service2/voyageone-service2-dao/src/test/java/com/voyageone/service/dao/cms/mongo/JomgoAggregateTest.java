package com.voyageone.service.dao.cms.mongo;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JomgoAggregate;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class JomgoAggregateTest {

    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;

    @Test
    public void testAggregate() throws Exception {
        /**
         db.cms_bt_feed_info_c016.aggregate(
         { $unwind : "$skus" },
         { $match : { "color" : "Black", "skus.qty" : 1 } },
         { $sort : { "code" : 1, "skus.barcode" : -1 } },
         { $skip : 370 },
         { $limit : 10 },
         { $project : { "_id" : 0, "color" : 1, "code" : 1,  "skus" : "$skus" } }
         )
         */

        List<JomgoAggregate> aggregateList = new ArrayList<>();
        aggregateList.add(new JomgoAggregate("{ $unwind : \"$skus\" }"));
        aggregateList.add(new JomgoAggregate("{ $match : { \"color\":#, \"skus.qty\":# } }", "Black", 1));
        aggregateList.add(new JomgoAggregate("{ $sort : { \"code\":1, \"skus.barcode\":-1 } }"));
        aggregateList.add(new JomgoAggregate("{ $skip:# }", 370));
        aggregateList.add(new JomgoAggregate("{ $limit:#}", 10));
        aggregateList.add(new JomgoAggregate("{ $project : { \"_id\" : 0, \"color\" : 1, \"code\" : 1,  \"skus\" : \"$skus\" } }"));

        List<Map<String, Object>> maps = cmsBtFeedInfoDao.aggregateToMap("016",  (JomgoAggregate[])aggregateList.toArray(new JomgoAggregate[aggregateList.size()]));
        for (Map<String, Object> map : maps) {
            System.out.println(JacksonUtil.bean2Json(map));
        }
    }

    @Test
    public void testAggregateCount() throws Exception {
        /**
         db.cms_bt_feed_info_c016.aggregate(
         { $unwind : "$skus" },
         { $match : { "color" : "Black", "skus.qty" : 1 } },
         { $group : { _id : 0, count: { $sum : 1 } } }
         )
         */

        List<JomgoAggregate> aggregateList = new ArrayList<>();
        aggregateList.add(new JomgoAggregate("{ $unwind : \"$skus\" }"));
        aggregateList.add(new JomgoAggregate("{ $match : { \"color\":#, \"skus.qty\":# } }", "Black", 1));
        aggregateList.add(new JomgoAggregate("{ $group : { _id : 0, count: { $sum : 1 } } }"));
        List<Map<String, Object>> maps = cmsBtFeedInfoDao.aggregateToMap("016",  (JomgoAggregate[])aggregateList.toArray(new JomgoAggregate[aggregateList.size()]));
        for (Map<String, Object> map : maps) {
            System.out.println(JacksonUtil.bean2Json(map));
        }
    }

    @Test
    public void testBulkUpdateWithMap() throws Exception {
        List<BulkUpdateModel> bulkList = new ArrayList<>();

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("code", "30892-BLK-0201001");
        Map<String, Object> updateMap = new HashMap<>();

        Map<String, Object> skusMap = new HashMap<>();
        skusMap.put("sku", "016-30892-BLK-0201001-058");
        skusMap.put("size", "5.6");

        updateMap.put("skus", skusMap);

        BulkUpdateModel updModel = new BulkUpdateModel();
        updModel.setQueryMap(queryMap);
        updModel.setUpdateMap(updateMap);
        bulkList.add(updModel);

        BulkWriteResult rs = cmsBtFeedInfoDao.bulkUpdateWithMap("016", bulkList, "system1", "$addToSet", false);
    }
}
