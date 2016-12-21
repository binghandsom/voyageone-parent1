package com.voyageone.task2.cms.service.feed;

import com.voyageone.base.dao.mongodb.BaseJongoDao;
import com.voyageone.components.onestop.bean.OneStopProduct;
import com.voyageone.service.model.cms.mongo.CmsBtCAdProductModel;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created by gjl on 2016/12/19.
 */
@Repository
public class productDao extends BaseJongoDao<OneStopProduct> {

    public List<OneStopProduct> getList() {
        return mongoTemplate.find("{}", OneStopProduct.class, "product_c032");
    }
}
