package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.PropertiesHelper;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeff.duan on 16/6/12.
 *
 * 2016-07-14 14:08:56
 * Jonas: 在新增 sku split 后, 进行测试
 *
 * @version 2.3.0
 * @since 2.1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsImportCategoryTreeServiceTest {
    @Test
    public void onStartup() throws Exception {

    }

    @Autowired
    private CmsImportCategoryTreeService cmsImportCategoryTreeService;

    @Test
    public void testOnStartup() throws Exception {
        PropertiesHelper.putValue("CmsImportCategoryTreeService_import_file_path", "/usr/web/contents/cms/category_import");
        List<TaskControlBean> taskControlList = new ArrayList<>();
        cmsImportCategoryTreeService.onStartup(taskControlList);
    }
}