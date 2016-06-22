package com.voyageone.web2.cms.views.test;
import com.voyageone.service.impl.cms.jumei.CmsBtJmApiLogService;
import com.voyageone.service.model.cms.CmsBtJmApiLogModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsBtJmApiLogTest {
    @Autowired
    CmsBtJmApiLogService service;

    @Test
    public void testGet() {
        // ApplicationContext context = new ClassPathXmlApplicationContext("spring-test.xml");
        // CmsBtJmApiLogService service = (CmsBtJmApiLogService)context.getBean("CmsBtJmApiLogService");
        CmsBtJmApiLogModel CmsBtJmApiLog = service.select(1);
    }

    @Test
    public void testGetList() {
        //ApplicationContext context = new ClassPathXmlApplicationContext("spring-test.xml");
        //CmsBtJmApiLogService service = (CmsBtJmApiLogService)context.getBean("CmsBtJmApiLogService");
       // List<CmsBtJmApiLogModel> list = service.selectList();
    }

    @Test
    public void testUpdate() {
        // ApplicationContext context = new ClassPathXmlApplicationContext("spring-test.xml");
        // CmsBtJmApiLogService service = (CmsBtJmApiLogService)context.getBean("CmsBtJmApiLogService");
        CmsBtJmApiLogModel CmsBtJmApiLog = service.select(1);
        CmsBtJmApiLog.setApiType(CmsBtJmApiLog.getApiType() + 1);
        CmsBtJmApiLog.setErrorCode(CmsBtJmApiLog.getErrorCode() + 1);
        CmsBtJmApiLog.setErrorMsg(CmsBtJmApiLog.getErrorMsg() + 1);
        CmsBtJmApiLog.setSourceId(CmsBtJmApiLog.getSourceId() + 1);
        CmsBtJmApiLog.setRemark(CmsBtJmApiLog.getRemark() + 1);
        service.update(CmsBtJmApiLog);
        CmsBtJmApiLogModel CmsBtJmApiLogNew = service.select(1);
    }

    @Test
    public void testCreate() {
        //ApplicationContext context = new ClassPathXmlApplicationContext("spring-test.xml");
        // CmsBtJmApiLogService service = (CmsBtJmApiLogService)context.getBean("CmsBtJmApiLogService");
        CmsBtJmApiLogModel CmsBtJmApiLog = service.select(1);
        CmsBtJmApiLog.setApiType(CmsBtJmApiLog.getApiType() + 1);
        CmsBtJmApiLog.setErrorCode(CmsBtJmApiLog.getErrorCode() + 1);
        CmsBtJmApiLog.setErrorMsg(CmsBtJmApiLog.getErrorMsg() + 1);
        CmsBtJmApiLog.setSourceId(CmsBtJmApiLog.getSourceId() + 1);
        CmsBtJmApiLog.setRemark(CmsBtJmApiLog.getRemark() + 1);
        CmsBtJmApiLog.setId(0);
        service.create(CmsBtJmApiLog);
        CmsBtJmApiLogModel CmsBtJmApiLogNew = service.select(3);
    }

    @Test
    public void testGetPage() {

       // List<Map<String,Object>> list = service.selectPage();

    }
}
