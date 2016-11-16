package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformCategorySchemaJdMqServiceTest {

//    @Autowired
//    private MqSender sender;

    @Autowired
    private CmsBuildPlatformCategorySchemaJdMqService cmsBuildPlatformCategorySchemaJdMqService;

    @Autowired
    private PlatformCategoryService platformCategoryService;

    @Test
    public void testSendMessage() throws Exception {
        List<Integer> list=new ArrayList<>();
        for (int i=0;i<40;i++)
        {
            list.add(i);
        }
       List<List<Integer>> pageList= CommonUtil.splitList(list,20);
        Map<String,Object> message=new HashMap<>();
        message.put("test","111");
//        sender.sendMessage(MqRoutingKey.CMS_BATCH_PlatformCategorySchemaJdJob, message);
    }

    @Test
    public void testDoSetPlatformJdSchemaCommon() throws Exception {
        int cartId = 28;  // 京东国际Liking匠心界店铺

        cmsBuildPlatformCategorySchemaJdMqService.doSetPlatformJdSchemaCommon(cartId);
    }

    @Test
    public void testDoSetPlatformPropJdSub() throws Exception {
        String channelId = "928";
        int cartId = 28;  // 京东国际Liking匠心界店铺
//        int cartId = 29;  // 京东国际Liking悦境店铺
        String catId = "9744"; // 服饰内衣>内衣>男式内裤

//        ShopBean shopProp = Shops.getShop(channelId, StringUtils.toString(cartId));
        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("https://api.jd.com/routerjson");
        shopBean.setAppKey("");
        shopBean.setAppSecret("");
        shopBean.setSessionKey(""); // 京东国际匠心界全球购专营店(SessionKey)
        shopBean.setOrder_channel_id(channelId);
        shopBean.setCart_id(StringUtils.toString(cartId));
        shopBean.setShop_name("京东国际匠心界全球购专营店");
        // platformid一定要设成京东，否则默认为天猫（1）的话，expressionParser.parse里面会上传照片到天猫空间，出现异常
        shopBean.setPlatform_id("2");


        // 取得类目属性叶子数据并去掉重复叶子类目
        List<CmsMtPlatformCategoryTreeModel> allCategoryTreeLeaves = platformCategoryService.getCmsMtPlatformCategoryTreeModelLeafList(cartId);
        // 去掉重复项的类目叶子件数大于0的场合
        // 取得每个叶子类目的属性和属性值插入到MangoDB的schema表中
        for (CmsMtPlatformCategoryTreeModel platformCategoriesModel : allCategoryTreeLeaves) {
            if (//channelId.equals(platformCategoriesModel.getChannelId())
                    //&&
               catId.equals(platformCategoriesModel.getCatId())) {
                // 取得propsItem属性(进去打个断点可以得到当前catId对应的propsItem属性值)
                cmsBuildPlatformCategorySchemaJdMqService.doSetPlatformPropJdSub(shopBean, platformCategoriesModel);
            }
        }

        System.out.println("正常结束");
    }


}