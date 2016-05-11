package com.voyageone.cacheable;

import com.voyageone.cacheable.entity.TCacheable;
import com.voyageone.cacheable.service.TCacheableBean1Service;
import com.voyageone.cacheable.service.TCacheableBean2Service;
import com.voyageone.cacheable.service.TCacheableBeanService;
import com.voyageone.cacheable.service.TCacheableService;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.dao.cms.CmsMtImageCreateTemplateDao;
import com.voyageone.service.model.cms.CmsMtImageCreateTemplateModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author aooer 2016/5/5.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TCacheableServiceTest {

    @Autowired
    private TCacheableService tCacheableService;

    @Test
    public void testQueryByName() throws Exception {
        System.out.println("\n");

        System.out.println("测试CacheAble缓存：");
        for (int i = 0; i < 3; i++) {
            System.out.println("QUERY:\t" + tCacheableService.queryByName("asdsadf"));
        }
        System.out.println("\n");

        System.out.println("测试CachePut缓存：");
        for (int i = 0; i < 3; i++) {
            System.out.println("UPDATE:\t" + tCacheableService.updateByName("asdsadf"));
            System.out.println("QUERY:\t" + tCacheableService.queryByName("asdsadf"));
        }
        System.out.println("\n");

        System.out.println("测试CacheEvict缓存：");
        for (int i = 0; i < 3; i++) {
            System.out.println("REMOVE:\t" + tCacheableService.removeByName("asdsadf"));
            System.out.println("QUERY:\t" + tCacheableService.queryByName("asdsadf"));
        }
        System.out.println("\n");
    }

    @Autowired
    private TCacheableBeanService tCacheableBeanService;

    @Test
    public void testQueryByBeanName() throws Exception {
        System.out.println("\n");

        System.out.println("测试CacheAble缓存：");
        for (int i = 0; i < 3; i++) {
            TCacheable tCacheable = new TCacheable();
            tCacheable.setName("asdsadf");
            System.out.println("QUERY:\t" + tCacheableBeanService.queryByName(tCacheable));
        }
        System.out.println("\n");

        System.out.println("测试CachePut缓存：");
        for (int i = 0; i < 3; i++) {
            TCacheable tCacheable = new TCacheable();
            tCacheable.setName("asdsadf");
            System.out.println("UPDATE:\t" + tCacheableBeanService.updateByName(tCacheable));
            System.out.println("QUERY:\t" + tCacheableBeanService.queryByName(tCacheable));
        }
        System.out.println("\n");

        System.out.println("测试CacheEvict缓存：");
        for (int i = 0; i < 3; i++) {
            TCacheable tCacheable = new TCacheable();
            tCacheable.setName("asdsadf");
            System.out.println("REMOVE:\t" + tCacheableBeanService.removeByName(tCacheable));
            System.out.println("QUERY:\t" + tCacheableBeanService.queryByName(tCacheable));
        }
        System.out.println("\n");
    }

    @Autowired
    private TCacheableBean1Service tCacheableBean1Service;

    @Test
    public void testQueryByBean1Name() throws Exception {
        System.out.println("\n");

        System.out.println("测试CacheAble缓存：");
        for (int i = 0; i < 3; i++) {
            TCacheable tCacheable = new TCacheable();
            tCacheable.setName("asdsadf");
            System.out.println("QUERY:\t" + tCacheableBean1Service.queryByName(tCacheable));
        }
        System.out.println("\n");

        System.out.println("测试CachePut缓存：");
        for (int i = 0; i < 3; i++) {
            TCacheable tCacheable = new TCacheable();
            tCacheable.setName("asdsadf");
            System.out.println("UPDATE:\t" + tCacheableBean1Service.updateByName(tCacheable));
            System.out.println("QUERY:\t" + tCacheableBean1Service.queryByName(tCacheable));
        }
        System.out.println("\n");

        System.out.println("测试CacheEvict缓存：");
        for (int i = 0; i < 3; i++) {
            TCacheable tCacheable = new TCacheable();
            tCacheable.setName("asdsadf");
            System.out.println("REMOVE:\t" + tCacheableBean1Service.removeByName(tCacheable));
            System.out.println("QUERY:\t" + tCacheableBean1Service.queryByName(tCacheable));
        }
        System.out.println("\n");
    }

    @Autowired
    private TCacheableBean2Service tCacheableBean2Service;

    @Test
    public void testQueryByBean2Name() throws Exception {
        System.out.println("\n");

        System.out.println("测试CacheAble缓存：");
        for (int i = 0; i < 3; i++) {
            TCacheable tCacheable = new TCacheable();
            tCacheable.setName("asdsadf_query");
            System.out.println("QUERY:\t" + tCacheableBean2Service.queryByName(tCacheable));
        }
        System.out.println("\n");

        System.out.println("测试CachePut缓存：");
        for (int i = 0; i < 3; i++) {
            TCacheable tCacheable = new TCacheable();
            tCacheable.setName("asdsadf_update");
            System.out.println("UPDATE:\t" + tCacheableBean2Service.updateByName(tCacheable));
            System.out.println("QUERY:\t" + tCacheableBean2Service.queryByName(tCacheable));
        }
        System.out.println("\n");

        System.out.println("测试CacheEvict缓存：");
        for (int i = 0; i < 3; i++) {
            TCacheable tCacheable = new TCacheable();
            tCacheable.setName("asdsadf_delete");
            System.out.println("REMOVE:\t" + tCacheableBean2Service.removeByName(tCacheable));
            System.out.println("QUERY:\t" + tCacheableBean2Service.queryByName(tCacheable));
        }
        System.out.println("\n");
    }

    @Autowired
    private CmsMtImageCreateTemplateDao cmsMtImageCreateTemplateDao;

    @Test
    public void testQueryImageCreateTemplate() throws Exception {
        System.out.println("测试 CmsMtImageCreateTemplateDao 缓存：");
        for (int i = 0; i < 3; i++) {
            CmsMtImageCreateTemplateModel model = cmsMtImageCreateTemplateDao.select(15);
            System.out.println(JacksonUtil.bean2Json(model));
        }

    }

    @Test
    public void testUpdateImageCreateTemplate() throws Exception {
        CmsMtImageCreateTemplateModel model = cmsMtImageCreateTemplateDao.select(15);
        model.setModifier("ccc3");
        cmsMtImageCreateTemplateDao.update(model);

        System.out.println("测试 CmsMtImageCreateTemplateDao 缓存：");
        model = cmsMtImageCreateTemplateDao.select(15);
        System.out.println(JacksonUtil.bean2Json(model));
    }

}