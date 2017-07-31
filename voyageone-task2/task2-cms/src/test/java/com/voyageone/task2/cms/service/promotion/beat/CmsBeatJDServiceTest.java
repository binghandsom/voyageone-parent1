package com.voyageone.task2.cms.service.promotion.beat;

import com.jd.open.api.sdk.domain.ware.ImageReadService.Image;
import com.jd.open.api.sdk.response.ware.ImageReadFindImagesByWareIdResponse;
import com.jd.open.api.sdk.response.ware.SkuReadFindSkuByIdResponse;
import com.jd.open.api.sdk.response.ware.WareReadFindWareByIdResponse;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.jd.service.JdWareNewService;
import com.voyageone.service.bean.cms.CmsBtBeatInfoBean;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by james on 2017/6/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBeatJDServiceTest {

    @Autowired
    CmsBeatJDService cmsBeatJDService;
    @Autowired
    JdWareNewService jdWareNewService;
    @Test
    public void uploadImage() throws IOException {
        ShopBean shopBean = Shops.getShop("001",26);
        shopBean.setShop_name("Sneakerhead国际旗舰店");
        shopBean.setApp_url("https://api.jd.com/routerjson");
        shopBean.setAppKey("BFA3102EFD4B981E9EEC2BE32DF1E44E");
        shopBean.setAppSecret("90742900899f49a5acfaf3ec1040a35c");
        shopBean.setSessionKey("614a5873-f72e-4efc-9208-c0c5db4e07ac");

        List<Long> jdSkuNo = Arrays.asList(1983231825L, 1983231842L);
        String [] urls = {"jfs/t5014/47/328086421/113745/1fe16ecc/58df572aN05eaef24.jpg","jfs/t4876/44/357394718/113024/77012b0e/58df572dNab0554b9.jpg"};
        ImageReadFindImagesByWareIdResponse imageReadFindImagesByWareIdResponse = jdWareNewService.getImageByWareId(shopBean, 1956654138L);
        WareReadFindWareByIdResponse wareReadFindWareByIdResponse = jdWareNewService.wareReadFindWareById(shopBean, 1956654138L);
        int i=0;
        for(Long skuno : jdSkuNo) {
            SkuReadFindSkuByIdResponse skuReadFindSkuByIdResponse = jdWareNewService.skuReadFindSkuById(shopBean, skuno, "outerId,logo");

            Image image = imageReadFindImagesByWareIdResponse.getImages().stream().filter(item -> !item.getColorId().equals("0000000000")).filter(item -> item.getImgUrl().equals(skuReadFindSkuByIdResponse.getSku().getLogo())).findFirst().orElse(null);
            jdWareNewService.imageWriteUpdate(shopBean, 1956654138L,Arrays.asList(image.getColorId()), Arrays.asList(urls[i]),Arrays.asList("1"));
            System.out.println(skuno +" "+ image.getColorId());
            i++;
        }
    }

    @Test
    public void getJDImage(){
//        ShopBean shopBean = Shops.getShop("001",26);
//        shopBean.setShop_name("Sneakerhead国际旗舰店");
//        shopBean.setApp_url("https://api.jd.com/routerjson");
//        shopBean.setAppKey("BFA3102EFD4B981E9EEC2BE32DF1E44E");
//        shopBean.setAppSecret("90742900899f49a5acfaf3ec1040a35c");
//        shopBean.setSessionKey("614a5873-f72e-4efc-9208-c0c5db4e07ac");
//        Image image = cmsBeatJDService.getJDImage(shopBean, "001",26, "s74990", 1956654138L);
//        System.out.println(JacksonUtil.bean2Json(image));
    }

    @Test
    public void beatMain(){
        TaskControlBean threadCount = new TaskControlBean();
        threadCount.setCfg_name(TaskControlEnums.Name.thread_count.toString());
        threadCount.setCfg_val1("5");

        TaskControlBean atomCount = new TaskControlBean();
        atomCount.setCfg_name(TaskControlEnums.Name.atom_count.toString());
        atomCount.setCfg_val1("10");

        List< TaskControlBean > taskControlList = new ArrayList<>();
        taskControlList.add(threadCount);
        taskControlList.add(atomCount);
        try {
            cmsBeatJDService.onStartup(taskControlList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}