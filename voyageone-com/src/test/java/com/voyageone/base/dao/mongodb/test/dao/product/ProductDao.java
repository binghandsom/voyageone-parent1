package com.voyageone.base.dao.mongodb.test.dao.product;

import com.mongodb.CommandResult;
import com.voyageone.base.dao.mongodb.BaseJomgoTemplate;
import com.voyageone.base.dao.mongodb.test.model.product.Product;
import com.voyageone.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Repository
public class ProductDao {

    @Autowired
    BaseJomgoTemplate mongoTemplate;

    public void testExecuteCommand() {
        String cmd = "{collMod: \"" + Product.getCollectionName("013") + "\", usePowerOf2Sizes : true }";
        CommandResult result = mongoTemplate.executeCommand(cmd);
    }

    public boolean testCollectionExists() {
        return mongoTemplate.collectionExists(Product.getCollectionName("013"));
    }

    public void createCollection() {
        mongoTemplate.createCollection(Product.getCollectionName("015"));
    }

    public void dropCollection() {
        mongoTemplate.dropCollection(Product.getCollectionName("015"));
    }

    public Product findOne() {
        return mongoTemplate.findOne(Product.class, Product.getCollectionName("012"));
    }

    public Product findOne(String strQuery) {
        return mongoTemplate.findOne(strQuery, Product.class, Product.getCollectionName("012"));
    }

    public boolean exists(String strQuery) {
        return mongoTemplate.exists(strQuery, Product.getCollectionName("012"));
    }

    public List<Product> findAll() {
        List<Product> result = mongoTemplate.findAll(Product.class, "product_j001");
        for(Product product : result) {
            System.out.println(product);
            System.out.println(result.size());
            break;
        }
        return result;
    }

    public void queryWith_() {
        String query = "{\"product_id\":21, \"field.prop_100\":\"562\"}";
        List<Product> lst =  mongoTemplate.find(query, Product.class, Product.getCollectionName("013"));
        for(Product product : lst) {
            System.out.println(product);
        }
        System.out.println(lst.size());
    }

    public void countWithProduct() {
        System.out.println(mongoTemplate.count(Product.getCollectionName("012")));
    }

    public void countQuery() {
        String query = "{\"product_id\":21, \"field.prop_100\":\"562\"}";
        System.out.println(mongoTemplate.count(query, Product.getCollectionName("013")));
    }

    public void insertWithProduct(Product entity) {
        mongoTemplate.save(entity, entity.getCollectionName());
    }

    public void saveWithProduct(Product entity) {
        mongoTemplate.save(entity, entity.getCollectionName());
    }

    public void deleteWithProduct(Product entity) {
        mongoTemplate.removeById(entity.get_id(), entity.getCollectionName());
    }

    public void saveProduct10Wan() {
        System.out.println("start:" + DateTimeUtil.getNow());
        Random random = new Random();
        for (int i=0; i<100; i++) {
            saveProduct1Wan(random, i, 1000) ;
        }
        System.out.println("end:" + DateTimeUtil.getNow());
    }

    public void saveProduct100Wan() {
        System.out.println("start:" + DateTimeUtil.getNow());
        Random random = new Random();
        for (int i=0; i<1000; i++) {
            saveProduct1Wan(random, i, 1000) ;
        }
        System.out.println("end:" + DateTimeUtil.getNow());
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
        String channelId = "013";
        for (int i=1; i<=size; i++) {
            int cat_id = random.nextInt(1000)+1;
            int product_id = page*size + i;
            Product product = new Product(channelId, cat_id, product_id);
            for (int j=0; j<101; j++) {
                int prop_id = random.nextInt(1000)+1;
                if (j>80) {
                    product.setAttribute("prop_" + j, String.valueOf(prop_id), false);
                } else {
                    product.setAttribute("prop_" + j, prop_id, false);
                }
            }
            lst.add(product);
        }

        String collectionName = null;
        if (!lst.isEmpty()) {
            Product product =  lst.get(0);
            collectionName = product.getCollectionName();
        }
        mongoTemplate.insert(lst, collectionName);
        System.out.println(String.valueOf((page + 1) * size));
    }

    public void updateFirst() {
        String strQuery = "{\"product_id\":22}";
        String strUpdate = "{$set:{cat_id:234}}";
        String strUpdate1 = "{$inc: {age: 1}}";
        mongoTemplate.updateFirst(strQuery, strUpdate1, Product.getCollectionName("013"));
        System.out.println(mongoTemplate.findOne(strQuery, Product.class, Product.getCollectionName("013")));
    }

    public void updateFirst1() {
        //http://jzfjeff.blog.51cto.com/1478834/1003191
        String strQuery = "{\"product_id\":23, \"field.prop_100\":\"227\"}";
        String strUpdate = "{\"$set\" : {\"field.$.prop_100\" : \"blah11\"}}";
        //String strUpdate1 = "{$inc: {age: 1}}";
        mongoTemplate.updateFirst(strQuery, strUpdate, Product.getCollectionName("013"));
        strQuery = "{\"product_id\":23}";
        System.out.println(mongoTemplate.findOne(strQuery, Product.class, Product.getCollectionName("013")));

    }



//
//    public void saveWithProduct(ProductJongo entity) {
//        mongoTemplate.save(entity, entity.getCollectionName());
//    }
//
//    public void testExecuteCommand() {
//        String jsonSql = "{ 'count':'product_c010','query':'product_id:20' }";
//        CommandResult commandResult = mongoTemplate.executeCommand(jsonSql);
//        Object count = commandResult.get("n");
//        System.out.println("testExecuteCommand count:=" + count);
//    }
//
//    public void testExecuteCommand1() {
//        String jsonSql = "{ 'distinct':'product_j001','key':'product_id' }";
//        CommandResult commandResult = mongoTemplate.executeCommand(jsonSql);
//        BasicDBList list = (BasicDBList)commandResult.get("values");
//        for (int i = 0; list != null && i < list.size(); i ++) {
//            System.out.println(list.get(i));
//        }
//    }
//
//    public void testExecuteCommand2() {
//        String jsonSql = "{ 'distinct':'product_j001','key':'product_id' }";
//        CommandResult commandResult = mongoTemplate.executeCommand((DBObject) JSON.parse(jsonSql));
//        BasicDBList list = (BasicDBList)commandResult.get("values");
//        for (int i = 0; list != null && i < list.size(); i ++) {
//            System.out.println(list.get(i));
//        }
//    }
//
//    //public CommandResult executeCommand(final DBObject command, final ReadPreference readPreference) {
//
//    public void executeQuery() {
//        Query query = new BasicQuery("{\"product_id\":2}");
//        mongoTemplate.executeQuery(query,"product_j001",new DocumentCallbackHandler() {
//            //处理自己的逻辑，这种为了有特殊需要功能的留的开放接口命令模式
//            public void processDocument(DBObject dbObject) throws MongoException, DataAccessException {
//                //mongoTemplate.updateFirst(query, Update.update("name","houchangren"),"person");
//                System.out.println(dbObject);
//            }
//        });
//    }
//
//    public void executeDbCallback() {
//        mongoTemplate.execute(new DbCallback<Product>() {
//            public Product doInDB(DB db) throws MongoException, DataAccessException {
//                //自己写逻辑和查询处理
//                Product product = new Product("001", 1, 3);
//                return product;
//            }
//        });
//    }
//
//    public void executeCollectionCallback() {
//        mongoTemplate.execute("", new CollectionCallback<Product>() {
//            @Override
//            public Product doInCollection(DBCollection collection) throws MongoException, DataAccessException {
//                Product product = new Product("001", 1, 3);
//                //自己取值然后处理返回对应的处理  collection.find();
//                return product;
//            }
//        });
//    }
//
//    public void createCollection() {
//        mongoTemplate.createCollection("product_k001");
//    }

}
