package com.voyageone.web2.cms.views.bi_report;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.dao.report.BiReportDownloadTaskDao;
import com.voyageone.service.daoext.report.BiReportDownloadTaskDaoExt;
import com.voyageone.service.model.report.BiReportDownloadTaskModel;
import com.voyageone.web2.cms.views.biReport.consult.BiRepConsultService;
import com.voyageone.web2.cms.views.biReport.consult.NameCreator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by dell on 2017/1/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class BiRepTest {
    @Autowired
    private BiRepConsultService biRepConsultService;
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
//    @Test
    public void testDemo() {
        String exportPath="E://bi_report//xlsxFile//";
        File pathFileObj = new File(exportPath);
        if (!pathFileObj.exists()) {
            System.out.println("高级检索 文件下载任务 文件目录不存在 " + exportPath);
        }
        else
        {
            System.out.println("directory exists!");
        }
        String fileName="010_2016-12-01_2016-12-31_店铺月报_店铺周报_店铺日报_.xlsx";
        exportPath += fileName;
        pathFileObj = new File(exportPath);
        if (!pathFileObj.exists()) {
            System.out.println("高级检索 文件下载任务 文件不存在 " + exportPath);
        }else
        {
            System.out.println("file exist");
        }
    }

    @Test
    public void testDate() {
    /*    BiReportDownloadTaskModel model=new BiReportDownloadTaskModel();
        model.setCreater("admin");
        model.setFilePath("E//hello//");
        model.setFileName("hello.txt");
        System.out.println(" model id before insert : " +model.getId());
        biReportDownloadTaskDao.insert(model);
        System.out.println(" model id after insert : " + model.getId());*/
//        System.out.println(biReportDownloadTaskDaoExt.softDel(4));
        Integer[] list = {7};
        String[]  channelList = {" 010 "};
        System.out.println(NameCreator.getTheChannelTypeName(Arrays.asList(channelList)));
//        System.out.println(NameCreator.getTheFileTypeName(Arrays.asList(list)));
//        System.out.println(" 010 ".trim());
    }

   /* public void testSelect*/
}
