package com.voyageone.base.dao.mongodb.test.dao.product;

import com.mongodb.DBObject;
import com.voyageone.base.dao.mongodb.BasePartitionMongoTemplate;
import com.voyageone.base.dao.mongodb.test.model.product.Product;
import com.voyageone.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Repository
public class ProductDao {
    @Autowired
    BasePartitionMongoTemplate mongoTemplate;

    public void saveWithProduct(Product entity) {
        mongoTemplate.save(entity, entity.getCollectionName());
    }

    public void saveProduct1000Wan() {
        System.out.println("start:" + DateTimeUtil.getNow());
        Random random = new Random();
        for (int i=0; i<10000; i++) {
            saveProduct1Wan(random, i, 1000) ;
        }
        System.out.println("end:" + DateTimeUtil.getNow());
    }

    public void saveProduct1Wan(Random random, int page, int size) {
        List<Product> lst = new ArrayList<>();
        String channel_id = "010";
        for (int i=1; i<=size; i++) {
            int cat_id = random.nextInt(1000)+1;
            int product_id = page*size + i;
            Product product = new Product(channel_id, cat_id, product_id);
            for (int j=0; j<101; j++) {
                int prop_id = random.nextInt(1000)+1;
                product.setAttribute("prop_" + j, prop_id, false);
            }
            lst.add(product);
        }

        String collectionName = null;
        if (lst.size()>0) {
            Product product =  lst.get(0);
            collectionName = product.getCollectionName();
        }
        mongoTemplate.insert(lst, collectionName);
        System.out.println(String.valueOf((page+1)*size));
    }

}
