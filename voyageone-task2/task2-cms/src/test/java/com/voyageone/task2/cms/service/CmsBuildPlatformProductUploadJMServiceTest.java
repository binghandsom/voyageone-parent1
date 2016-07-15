package com.voyageone.task2.cms.service;

import com.voyageone.service.dao.cms.CmsBtJmSkuDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Ethan Shi on 2016/6/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:context-cms-test.xml")
public class CmsBuildPlatformProductUploadJMServiceTest {


    @Autowired
    CmsBuildPlatformProductUploadJMService cmsBuildPlatformProductUploadJMService;


    @Autowired
    CmsBtSxWorkloadDaoExt cmsBtSxWorkloadDaoExt;

    @Autowired
    CmsBtProductGroupDao cmsBtProductGroupDao;

    @Autowired
    CmsBtJmSkuDao cmsBtJmSkuDao;


    @Test
    public void TestPrice() throws Exception {


    }



    @Test
    public void TestDate() throws Exception {



        Map<String, String> map = new HashMap<>();
        String value = map.get("1");

        long currentTime = System.currentTimeMillis();
        System.out.println(currentTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(currentTime);
        String dateStr = formatter.format(date);
        System.out.println(dateStr);

        Long time = getTime(dateStr);
        System.out.println(time);

        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.MINUTE, 30);
        System.out.println(rightNow.getTimeInMillis());
        Date date1 = new Date(rightNow.getTimeInMillis());
        date1.getTime();
        String date1Str = formatter.format(date1);
        System.out.println(date1Str);

        time = getTime(date1Str);
        System.out.println(time);


    }

    private static Long getTime(String user_time) throws Exception {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;


        d = sdf.parse(user_time);
        long l = d.getTime()/1000-8*3600;

        return l;
    }



    @Test
    public void testUpdateProduct() throws Exception {

//        List<CmsBtSxWorkloadModel> workloadList = cmsBtSxWorkloadDaoExt.selectSxWorkloadModelWithChannelIdCartIdGroupBy(1, "010", 27);
//
//        for (CmsBtSxWorkloadModel work : workloadList) {
////            work.setGroupId(27214L);
////            work.setGroupId(39342L);
//            work.setGroupId(30222L);
//
//
//            cmsBuildPlatformProductUploadJMService.updateProduct(work);
//        }

        CmsBtSxWorkloadModel work = new CmsBtSxWorkloadModel();
        work.setGroupId(30222L);
        work.setChannelId("010");
        work.setCartId(27);
        work.setPublishStatus(0);
        cmsBuildPlatformProductUploadJMService.updateProduct(work);

    }

    @Test
    public void testUpdateProduct2() throws Exception {

        CmsBtSxWorkloadModel workload = new CmsBtSxWorkloadModel();
        workload.setId(185);
        workload.setChannelId("017");
        workload.setCartId(27);
        workload.setGroupId(Long.parseLong("389898"));
        workload.setPublishStatus(0);

        cmsBuildPlatformProductUploadJMService.updateProduct(workload);

    }

}