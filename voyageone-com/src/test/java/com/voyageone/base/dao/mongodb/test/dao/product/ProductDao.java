package com.voyageone.base.dao.mongodb.test.dao.product;

import com.mongodb.*;
import com.mongodb.util.JSON;
import com.voyageone.base.dao.mongodb.BasePartitionMongoTemplate;
import com.voyageone.base.dao.mongodb.test.model.product.Product;
import com.voyageone.base.dao.mongodb.test.dao.support.ProductJongo;
import com.voyageone.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.DbCallback;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
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

    public void queryWithJongo() {
        Query query = new BasicQuery("{\"product_id\":20}");
        List<Product> lst =  mongoTemplate.find(query, Product.class, "product_c010");
        for(Product product : lst) {
            System.out.println(product);
        }
        System.out.println(lst.size());
    }

    public void queryWithJongoObj() {
//        List<ProductJongo> lst =  mongoTemplate.find("{\"product_id\":2}", ProductJongo.class, "product_j001");
//        for(ProductJongo product : lst) {
//            System.out.println(product);
//        }
//        System.out.println(lst.size());
    }

    public void saveWithProduct(ProductJongo entity) {
        mongoTemplate.save(entity, entity.getCollectionName());
    }

    public void testExecuteCommand() {
        String jsonSql = "{ 'count':'product_c010','query':'product_id:20' }";
        CommandResult commandResult = mongoTemplate.executeCommand(jsonSql);
        Object count = commandResult.get("n");
        System.out.println("testExecuteCommand count:=" + count);
    }

    public void testExecuteCommand1() {
        String jsonSql = "{ 'distinct':'product_j001','key':'product_id' }";
        CommandResult commandResult = mongoTemplate.executeCommand(jsonSql);
        BasicDBList list = (BasicDBList)commandResult.get("values");
        for (int i = 0; list != null && i < list.size(); i ++) {
            System.out.println(list.get(i));
        }
    }

    public void testExecuteCommand2() {
        String jsonSql = "{ 'distinct':'product_j001','key':'product_id' }";
        CommandResult commandResult = mongoTemplate.executeCommand((DBObject) JSON.parse(jsonSql));
        BasicDBList list = (BasicDBList)commandResult.get("values");
        for (int i = 0; list != null && i < list.size(); i ++) {
            System.out.println(list.get(i));
        }
    }

    //public CommandResult executeCommand(final DBObject command, final ReadPreference readPreference) {

    public void executeQuery() {
        Query query = new BasicQuery("{\"product_id\":2}");
        mongoTemplate.executeQuery(query,"product_j001",new DocumentCallbackHandler() {
            //处理自己的逻辑，这种为了有特殊需要功能的留的开放接口命令模式
            public void processDocument(DBObject dbObject) throws MongoException, DataAccessException {
                //mongoTemplate.updateFirst(query, Update.update("name","houchangren"),"person");
                System.out.println(dbObject);
            }
        });
    }

    public void executeDbCallback() {
        mongoTemplate.execute(new DbCallback<Product>() {
            public Product doInDB(DB db) throws MongoException, DataAccessException {
                //自己写逻辑和查询处理
                Product product = new Product("001", 1, 3);
                return product;
            }
        });
    }

    public void executeCollectionCallback() {
        mongoTemplate.execute("", new CollectionCallback<Product>() {
            @Override
            public Product doInCollection(DBCollection collection) throws MongoException, DataAccessException {
                Product product = new Product("001", 1, 3);
                //自己取值然后处理返回对应的处理  collection.find();
                return product;
            }
        });
    }

    public void createCollection() {
        mongoTemplate.createCollection("product_k001");
    }

}
