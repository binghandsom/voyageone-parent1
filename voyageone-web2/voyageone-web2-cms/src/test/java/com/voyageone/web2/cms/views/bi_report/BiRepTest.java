package com.voyageone.web2.cms.views.bi_report;

import com.voyageone.service.dao.report.BiReportDownloadTaskDao;
import com.voyageone.service.daoext.report.BiReportDownloadTaskDaoExt;
import com.voyageone.service.daoext.report.BiReportSalesShop010DaoExt;
import com.voyageone.service.model.report.BiReportDownloadTaskModel;
import com.voyageone.web2.cms.views.biReport.consult.BiRepConsultService;
import com.voyageone.web2.cms.views.biReport.consult.BiRepSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.Date;

/**
 * Created by dell on 2017/1/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class BiRepTest {
    @Autowired
    BiRepSupport biRepSupport;
    @Autowired
    private BiRepConsultService biRepConsultService;
    @Autowired
    private BiReportSalesShop010DaoExt biReportSalesShop010DaoExt;
    @Autowired
    private BiReportDownloadTaskDaoExt biReportDownloadTaskDaoExt;
    @Autowired
    private BiReportDownloadTaskDao biReportDownloadTaskDao;
    /*
        @Test
        public void biReportTest()
        {
            biRepConsultService.createXLSFile();
        }*/
    @Test
    public void testDemo() {

//       System.out.println( biRepConsultService.fileExist(null,null,null));
        BiReportDownloadTaskModel model=new BiReportDownloadTaskModel(1,"E//a.xlsx",new Date(116,4,5),2);
        Integer i=biReportDownloadTaskDao.insert(model);
       System.out.println(i);
//        biRepConsultService.createXLSFile();
    }

    public <T> void testcall(Collection<T> dataset) {
    }

   /* public void testSelect*/
}
