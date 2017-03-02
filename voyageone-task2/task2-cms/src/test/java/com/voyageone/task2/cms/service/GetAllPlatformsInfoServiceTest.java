package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by desmond on 2016/11/11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class GetAllPlatformsInfoServiceTest {

    @Autowired
    private GetAllPlatformsInfoService getAllPlatformsInfoService;
    @Autowired
    private PlatformCategoryService platformCategoryService;

    @Test
    public void testDoTmPlatformCategory() throws Exception {
        // 天猫类目和类目schema取得
        String channnelId = "002";
        int cartId = 23;

        // 测试用PortAmerican海外专营店
        ShopBean shop = new ShopBean();
        shop.setOrder_channel_id(channnelId);
        shop.setCart_id(String.valueOf(cartId));
        shop.setApp_url("http://gw.api.taobao.com/router/rest");
        shop.setAppKey("");
        shop.setAppSecret("");
        shop.setSessionKey("");
        // platformid设成天猫（1）
        shop.setPlatform_id("1");

        // 取得天猫类目和类目schema
        getAllPlatformsInfoService.doTmPlatformCategory(channnelId, cartId, shop);
        System.out.println("取得天猫类目和类目schema成功！");
    }

    /**
     * 从mongodb的tree表， 获取指定平台所有的叶子类目列表
	 * 平时用不到， 难般般帮产品抽一下用于数据分析
     */
    @Test
    public void printAllCategoryPath() {
        List<Integer> cartIdList = new ArrayList<>();
        String cart = "TM";
//        String cart = "JD";

        if ("JD".equals(cart)) {
            // 京东系
            cartIdList.add(24);
            cartIdList.add(26);
            cartIdList.add(28);
            cartIdList.add(29);
        } else if ("TM".equals(cart)) {
            // 天猫系
            cartIdList.add(20);
            cartIdList.add(23);
            cartIdList.add(30);
            cartIdList.add(31);
        }

        for (Integer cartId : cartIdList) {
            List<CmsMtPlatformCategoryTreeModel> allCategoryTreeLeaves = platformCategoryService.getCmsMtPlatformCategoryTreeModelLeafList(cartId);

            if (allCategoryTreeLeaves != null) {
                for (CmsMtPlatformCategoryTreeModel category : allCategoryTreeLeaves) {
                    System.out.println(category.getCatId() + "\t" + category.getCatPath());
                }
            }
        }


    }
}
