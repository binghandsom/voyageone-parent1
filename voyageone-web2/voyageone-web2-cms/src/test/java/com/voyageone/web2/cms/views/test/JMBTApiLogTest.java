package com.voyageone.web2.cms.views.test;
import com.voyageone.service.impl.jumei.JmBtApiLogService;
import com.voyageone.service.model.jumei.JmBtApiLogModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class JMBTApiLogTest {
    @Autowired
    JmBtApiLogService service;
    @Test
    public  void  testGet()
    {
       // ApplicationContext context = new ClassPathXmlApplicationContext("spring-test.xml");
       // JmBtApiLogService service = (JmBtApiLogService)context.getBean("jmBtApiLogService");
       JmBtApiLogModel jmBtApiLog= service.get(1);
    }
    @Test
    public  void  testGetList()
    {
        //ApplicationContext context = new ClassPathXmlApplicationContext("spring-test.xml");
        //JmBtApiLogService service = (JmBtApiLogService)context.getBean("jmBtApiLogService");
        List<JmBtApiLogModel> list= service.getList();
    }
    @Test
    public  void  testUpdate()
    {
       // ApplicationContext context = new ClassPathXmlApplicationContext("spring-test.xml");
       // JmBtApiLogService service = (JmBtApiLogService)context.getBean("jmBtApiLogService");
        JmBtApiLogModel  jmBtApiLog= service.get(1);
        jmBtApiLog.setApiType(jmBtApiLog.getApiType()+1);
        jmBtApiLog.setErrorCode(jmBtApiLog.getErrorCode()+1);
        jmBtApiLog.setErrorMsg(jmBtApiLog.getErrorMsg()+1);
        jmBtApiLog.setSourceId(jmBtApiLog.getSourceId()+1);
        jmBtApiLog.setRemark(jmBtApiLog.getRemark()+1);
        service.update(jmBtApiLog);
        JmBtApiLogModel jmBtApiLogNew=service.get(1);
    }
    @Test
    public  void  testCreate()
    {
        //ApplicationContext context = new ClassPathXmlApplicationContext("spring-test.xml");
       // JmBtApiLogService service = (JmBtApiLogService)context.getBean("jmBtApiLogService");
        JmBtApiLogModel  jmBtApiLog= service.get(2);
        jmBtApiLog.setApiType(jmBtApiLog.getApiType()+1);
        jmBtApiLog.setErrorCode(jmBtApiLog.getErrorCode()+1);
        jmBtApiLog.setErrorMsg(jmBtApiLog.getErrorMsg()+1);
        jmBtApiLog.setSourceId(jmBtApiLog.getSourceId()+1);
        jmBtApiLog.setRemark(jmBtApiLog.getRemark()+1);
        jmBtApiLog.setId(0);
        service.create(jmBtApiLog);
        JmBtApiLogModel jmBtApiLogNew=service.get(3);
    }
}
