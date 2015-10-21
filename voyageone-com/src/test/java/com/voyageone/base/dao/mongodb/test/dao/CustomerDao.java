package com.voyageone.base.dao.mongodb.test.dao;


import org.springframework.data.repository.CrudRepository;

/**
 * Created by DELL on 2015/10/21.
 */
public interface CustomerDao extends CrudRepository<Customer, String> {
}
