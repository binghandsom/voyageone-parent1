package com.voyageone.service.impl.cms.jumei;

import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.dao.OrderChannelDao;
import com.voyageone.service.impl.cms.jumei2.JmBtDealImportService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class JmBtDealImportServiceTest {
    @Autowired
    JmBtDealImportService service;
@Autowired
OrderChannelDao daoOrderChannelDao;
    @Test
    public void test() {
        System.out.println("begin:"+new Date());
        service.importJM("015");//015  001
        System.out.println("end:"+new Date());
    }
    @Test
    public void testImportAll() {
        List<String> listChannel = new ArrayList<>();
        listChannel.add("010");
        listChannel.add("015");
        listChannel.add("016");
        listChannel.add("017");
        listChannel.add("018");
        listChannel.add("019");
        listChannel.add("021");
        //List<OrderChannelBean> list=  daoOrderChannelDao.getAll();
        for (String channelId : listChannel) {
            System.out.println(channelId + "begin:" + new Date());
            service.importJM(channelId);
            System.out.println(channelId + "end:" + new Date());
        }
    }
}
