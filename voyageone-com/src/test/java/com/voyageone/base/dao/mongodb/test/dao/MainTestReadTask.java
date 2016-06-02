package com.voyageone.base.dao.mongodb.test.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Component("mainTestReadTask")
public class MainTestReadTask {

//    @Autowired
//    CustomerDao customerDao;
//    @Autowired
//    PersonDao personDao;
    @Autowired
    private MongoTemplate mongoTemplate;

    public void testSelect() {

//
//        // http://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#repositories.query-methods
//
//        // start
////        Customer customer = new Customer("aa", "bb");
//        List<Customer> list = new ArrayList<Customer>();
//
//        for (int i=0; i<10 ; i++ ){
//
//            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date date = new Date();
//            String returnStr = i + "--" + f.format(date);
//
//            Customer customer = new Customer("aa"+returnStr, "bb");
//            list.add(customer);
//
//        }
//
//        //插入List
////        customerDao.save(list);
//
//        //获取搜索结果记录数
//        System.out.println(customerDao.count());
//
//        //搜索所有记录
//        Iterable<Customer> result = customerDao.findAll();
//
////        Iterable<Customer> result = customerDao.findOneByFirstName("aa1");
//
//        //循环遍历所有结果
//        for (Customer row : result) {
//            System.out.println("findByLastName:=" + row);
////            customerDao.delete(row);
//        }
//
//        //按照默认顺序搜索第一条记录
//        Customer result1 = customerDao.findOne("562758fbf1fe8c4cffeeaa9b");
//        if (result1 != null){
//            System.out.println("result1:=" + result1);
//        }
//
//        //根据指定字段搜索1条记录
//        Customer result2 = customerDao.findOneByFirstName("aa0--2015-10-21 17:28:03");
//        if (result2 != null){
//            System.out.println("result2:=" + result2);
//        }
//
//        //根据指定字段搜索结果集
//        Iterable<Customer> result3 = customerDao.findByFirstName("aa2015-10-21 17:24:34");
//        for (Customer row : result3) {
//            System.out.println("result3:=" + row);
//        }
//
//        //根据指定字段模糊搜索结果集
//        Iterable<Customer> result4 = customerDao.findByFirstNameRegex("1aa2015-10-21");
//        for (Customer row : result4) {
//            System.out.println("result4:=" + row);
//        }
//
//        //根据指定字段获取搜索结果件数
//        Integer count1 = customerDao.countByFirstName("aa0--2015-10-21 17:28:03");
//        System.out.println("count1:=" + count1);
//
//        //根据指定组合字段搜索
//        Iterable<Customer> result5 = customerDao.findByFirstNameAndLastName("aa2--2015-10-21 17:28:03", "bb");
//        for (Customer row : result5) {
//            System.out.println("result5:=" + row);
//        }
//
//        Iterable<Customer> result6 = customerDao.findByThePersonsFirstName("aa2015-10-21 17:20:58","11bb");
//        for (Customer row : result6) {
//            System.out.println("result6:=" + row);
//        }
//
//
//        //插入复杂属性
//        List<Person> personList = new ArrayList<Person>();
//        for (int i=0; i<10 ; i++ ){
//
//            SimpleDateFormat f = new SimpleDateFormat("yyyyMMddhhmmssSSS");
//            Date date = new Date();
//        String returnStr = i + "--" + f.format(date);
//
//        Person person = new Person();
//        person.setPerson_id(String.valueOf(i));
//        person.setPerson_name(String.valueOf(i) + returnStr);
//        DepartInfo info = new DepartInfo();
//        info.setDepartName("DepartName--"+ i);
//        info.setRole("Role--" + i);
//        person.setDepartInfo(info);
//        personList.add(person);
//
//    }
//
////        personDao.save(personList);
//        //搜索两级属性
////        Iterable<Person> result7 = personDao.findByDepartInfo$DepartName("DepartName--5");
////        for (Person row : result7) {
////            System.out.println("result7:=" + row);
////        }
////
//        Aggregation agg = newAggregation(
//                match(Criteria.where("cat_id").lt(10)),
//                group("cat_id").sum("field.prop_1").as("count"),
//                project("count").and("mmm").previousOperation()
//
//        );
//
//        AggregationResults<CatCount> groupResults
//                = mongoTemplate.aggregate(agg, "cat_010", CatCount.class);
//        List<CatCount> aa = groupResults.getMappedResults();
////        AggregationResults<OutputType> results = mongoTemplate.aggregate(agg, "INPUT_COLLECTION_NAME", OutputType.class);
//        //
    }
}
