package com.voyageone.base.dao.mongodb.test.dao.product;

import com.voyageone.base.dao.mongodb.test.model.product.Product;
import com.voyageone.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by DELL on 2015/10/22.
 */

@Service
public class ProductDaoTest {

    @Autowired
    ProductDao productDao;

    public void saveProduct1() {
        Product product = new Product("Camera bag", "a1", "a2");
        product = productDao.save(product);
        System.out.println(product);
    }

    public void saveProduct2(String channel_id, String cat_id, String product_id) {
        Product product = new Product(channel_id, cat_id, product_id);
        for (int i=0; i<101; i++) {
            product.setAttribute("prop_" + i, i);
        }
        product = productDao.save(product);
        //System.out.println(product);
    }

    public void saveProduct3(Random random, int page, int size) {
        List<Product> lst = new ArrayList<>();
        String channel_id = "10";
        for (int i=1; i<=size; i++) {
            String cat_id = String.valueOf(random.nextInt(1000)+1);
            String product_id = String.valueOf(page*size + i);
            Product product = new Product(channel_id, cat_id, product_id);
            for (int j=0; j<101; j++) {
                product.setAttribute("prop_" + j, 100+j, false);
            }
            lst.add(product);
        }
        System.out.println(String.valueOf((page+1)*size));
        productDao.insert(lst);
    }

}
