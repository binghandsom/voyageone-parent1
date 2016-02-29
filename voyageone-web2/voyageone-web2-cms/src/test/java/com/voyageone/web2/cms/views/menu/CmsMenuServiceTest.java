package com.voyageone.web2.cms.views.menu;

import com.voyageone.web2.cms.views.home.menu.CmsMenuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Edward
 * @version 2.0.0, 15/12/4
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsMenuServiceTest {

    @Autowired
    private CmsMenuService cmsMenuService;

    @Test
    public void testGetCategoryTypeList() throws Exception {
//        List<Map<String, Object>> resultList = cmsMenuService.getPlatformShowTypeList("004");
//        System.out.println(resultList);
//        assert resultList.size() > 0;
    }

    @Test
    public void testGetCategoryTreeList() throws Exception {
//        List<CmsMtCategoryTreeModel> resultList = cmsMenuService.getCategoryTreeList("Feed", "013");
//        System.out.println(resultList);
//        assert resultList.size() > 0;

    }
}