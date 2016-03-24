package com.voyageone.task2.cms.service;

import com.voyageone.service.impl.cms.feed.FeedCustomPropService;
import com.voyageone.service.model.cms.CmsBtFeedCustomPropAndValueModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by zhujiaye on 15/12/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBtFeedCustomPropServiceTest {

    @Autowired
    FeedCustomPropService customPropService;

    @Test
    public void testCustomProp() throws Exception {
        customPropService.doInit("010");

        // 获取属性列表测试
        List<CmsBtFeedCustomPropAndValueModel> propModelList;
        propModelList = customPropService.getPropList("010", "0");
        propModelList = customPropService.getPropList("010", "test - ring - demond - 1");
        propModelList = customPropService.getPropList("010", "test - ring - demond - 2");

        // 获取翻译内容的测试
        System.out.println("");
        System.out.println("+:" + customPropService.getPropTrans("010", "0", "0", "测试一下没有这个属性的"));
        System.out.println("+:" + customPropService.getPropTrans("010", "0", "0", "925"));
        System.out.println("+:" + customPropService.getPropTrans("010", "0", "MetalStamp", "925"));
        System.out.println("+:" + customPropService.getPropTrans("010", "test - ring - demond - 1", "MetalStamp", "925"));
        System.out.println("+:" + customPropService.getPropTrans("010", "test - ring - demond - 1", "MetalStamp", "Topaz"));

        // 测试完成
        System.out.println("OK");
    }

}