package com.voyageone.batch.cms.mongoDao;

import com.voyageone.base.dao.mongodb.BaseJomgoTemplate;
import com.voyageone.batch.cms.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by dell on 2015/11/27.
 */
public class ProductDao {
    @Autowired
    BaseJomgoTemplate mongoTemplate;

    public static final String C_NAME = "cms_bt_product";


    public void updateGroupInventory(List<Inventory> list) {

        for (Inventory item:list){
            int code_qty =  item.getQty();
            mongoTemplate.updateFirst(list.get_id(), C_NAME);

            String strQuery = "{\"product_id\":22}";
            String strUpdate = "{$set:{field.code_qty:}}";
            String strUpdate1 = "{$inc: {age: 1}}";
            mongoTemplate.updateFirst(strQuery, strUpdate1, C_NAME);
//            System.out.println(mongoTemplate.findOne(strQuery, Product.class, Product.getCollectionName("013")));

        }


    }

}
