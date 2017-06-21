package com.voyageone.task2.cms.service.promotion.beat;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonasvlag on 16/3/8.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class BeatJobServiceTest {

    @Autowired
    private BeatJobService beatJobService;

    @Test
    public void testStartup() {
        beatJobService.startup();
    }

    @Test
    public void testErrorConfigOnStartup() throws Exception {

        // 先测试无配置
        try {
            beatJobService.onStartup(new ArrayList<>());
        } catch (BusinessException ignored) {
            Assert.assertTrue(BeatJobService.getLastErrorTarget() == 1);
        }

        beatJobService.onStartup(new ArrayList<>());

        // 测试部分配置
        TaskControlBean threadCount = new TaskControlBean();
        threadCount.setCfg_name(TaskControlEnums.Name.thread_count.toString());
        threadCount.setCfg_val1("5");

        List< TaskControlBean > taskControlList = new ArrayList<>();
        taskControlList.add(threadCount);

        // 再次测试应该不会再抛错误。因为第一次已经抛出。
        beatJobService.onStartup(taskControlList);

        // 测试值类型错误
        TaskControlBean atomCount = new TaskControlBean();
        atomCount.setCfg_name(TaskControlEnums.Name.atom_count.toString());
        atomCount.setCfg_val1("a");
        taskControlList.add(atomCount);

        try {
            beatJobService.onStartup(taskControlList);
        } catch (BusinessException ignored) {
            Assert.assertTrue(BeatJobService.getLastErrorTarget() == 2);
        }

        // 上述都通过, 说明错误处理机制生效。
    }

    @Test
    public void testOnStartup() throws Exception {

        // 修改 Shop 配置
        ShopBean shopBean = Shops.getShop("010", 23);
//        shopBean.setApp_url("http://gw.api.taobao.com/router/rest");
//        shopBean.setAppKey("21008948");
//        shopBean.setAppSecret("0a16bd08019790b269322e000e52a19f");
//        shopBean.setSessionKey("6201d2770dbfa1a88af5acfd330fd334fb4ZZa8ff26a40b2641101981");

        // 定义固定配置
        TaskControlBean threadCount = new TaskControlBean();
        threadCount.setCfg_name(TaskControlEnums.Name.thread_count.toString());
        threadCount.setCfg_val1("5");

        TaskControlBean atomCount = new TaskControlBean();
        atomCount.setCfg_name(TaskControlEnums.Name.atom_count.toString());
        atomCount.setCfg_val1("10");

        List< TaskControlBean > taskControlList = new ArrayList<>();
        taskControlList.add(threadCount);
        taskControlList.add(atomCount);

        beatJobService.onStartup(taskControlList);
    }
}