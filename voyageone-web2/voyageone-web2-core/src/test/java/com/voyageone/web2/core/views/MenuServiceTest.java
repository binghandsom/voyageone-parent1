package com.voyageone.web2.core.views;

import com.voyageone.web2.core.views.menu.MenuService;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * test menuService
 *
 * @author         Edward
 * @version        2.0.0, 15/12/01
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    public void testGetMenuList() throws Exception {
        List<Map<String, Object>> resultList = menuService.getMenuList(26, "001");
        assert resultList.size() > 0;
    }

    @Test
    public void testGetMenuListNoValue() throws Exception {
        List<Map<String, Object>> resultList = menuService.getMenuList(1, "001");
        assert resultList.size() == 0;
    }
}