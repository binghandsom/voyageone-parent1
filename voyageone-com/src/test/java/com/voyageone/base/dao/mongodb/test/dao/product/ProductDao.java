package com.voyageone.base.dao.mongodb.test.dao.product;

import com.voyageone.base.dao.mongodb.test.model.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductDao extends MongoRepository<Product, String> {
    //public NewCustomer findByFirstname(String firstName);
}
