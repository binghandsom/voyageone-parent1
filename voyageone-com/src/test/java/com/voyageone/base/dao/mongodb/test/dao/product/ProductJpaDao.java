package com.voyageone.base.dao.mongodb.test.dao.product;

import com.voyageone.base.dao.mongodb.test.model.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by DELL on 2015/10/26.
 */
public interface ProductJpaDao extends MongoRepository<Product, String> {

}
