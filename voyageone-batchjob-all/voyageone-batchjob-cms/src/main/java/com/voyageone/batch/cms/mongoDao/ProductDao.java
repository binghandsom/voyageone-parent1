package com.voyageone.batch.cms.mongoDao;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseJomgoTemplate;
import com.voyageone.batch.cms.model.InventoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by dell on 2015/11/27.
 */
@Repository
public class ProductDao {
    @Autowired
    BaseJomgoTemplate mongoTemplate;

    public static final String C_NAME = "product_c010";


    /** 更新产品信息中的code级别库存值
     * @param list
     */
    public void updateCodeInventory(Collection<InventoryModel> list) {
        int i =1;
        WriteResult result = mongoTemplate.insert(list,"inventorycode");
//        System.out.println("i=" + i + "当前时间:" + new Date());
//        for (Inventory item:list){
//
//            int code_qty =  list.getQty();
////            mongoTemplate.updateFirst(list.get_id(), C_NAME);
////            System.out.println("i=" + i + "当前时间:" + new Date());
//            String strQuery = "{\"product_id\":" + i +"}";
//            String strUpdate = "{$seddt:{{\"batch_update.code_qty\":" + code_qty + "}}";
//
//            WriteResult result = mongoTemplate.updateFirst(strQuery, strUpdate, C_NAME);
////            System.out.println(mongoTemplate.findOne(strQuery, Product.class, Product.getCollectionName("013")));
//            i++;
//
//        }


    }

}
