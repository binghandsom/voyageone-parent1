package com.voyageone.base.dao.mongodb.test.dao.product;

import com.voyageone.base.dao.mongodb.BaseJomgoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductJomgoDao {
    @Autowired
    BaseJomgoTemplate jomgoDao;

    public void queryWithJongo() {

        //List<ProductMo> lst =  mongoTemplate.find(query, ProductMo.class, "product_c010");
//        for(ProductMo product : lst) {
//            System.out.println(product);
//        }
//        System.out.println(lst.size());
    }
}
