package com.voyageone.base.dao.mongodb.test.dao;


import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by DELL on 2015/10/21.
 */
public interface PersonDao extends CrudRepository<Person, String> {

    //public Iterable<Person> findByDepartInfo$DepartName(String departName);
}
