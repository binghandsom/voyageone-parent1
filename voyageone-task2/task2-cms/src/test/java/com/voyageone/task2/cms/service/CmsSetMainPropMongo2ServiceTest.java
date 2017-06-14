package com.voyageone.task2.cms.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.ListUtils;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by james on 2017/3/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsSetMainPropMongo2ServiceTest {

    @Autowired
    CmsSetMainPropMongo2Service cmsSetMainPropMongo2Service;

    @Test
    public void onStartup() throws Exception {
        List<TaskControlBean> taskControlList = new ArrayList<>();
        TaskControlBean taskControlBean = new TaskControlBean();
//        taskControlBean.setCfg_name("order_channel_id");
//        taskControlBean.setCfg_val1("028");
//        taskControlList.add(taskControlBean);
//        taskControlBean = new TaskControlBean();
        taskControlBean.setCfg_name("order_channel_id");
        taskControlBean.setCfg_val1("015");
        taskControlList.add(taskControlBean);

        cmsSetMainPropMongo2Service.onStartup(taskControlList);


    }

    @Test
    public void test(){

        List<String> categoryWhite = Arrays.asList("鞋靴","服饰");
        if (!ListUtils.isNull(categoryWhite)) {
            if (categoryWhite.stream().noneMatch(cat -> "服饰>服饰配件>手提包袋>手包".indexOf(cat) == 0)) {
                throw new BusinessException("主类目属于黑名单不能导入CMS：" );
            }
        }
    }

}