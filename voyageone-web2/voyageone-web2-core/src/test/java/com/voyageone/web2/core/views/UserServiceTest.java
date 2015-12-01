package com.voyageone.web2.core.views;

import com.voyageone.web2.core.model.UserSessionBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * UserService 单元测试
 * Created on 12/1/15.
 *
 * @author Jonas
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testSetSelectChannel() throws Exception {

        UserSessionBean userSessionBean = new UserSessionBean();
        userSessionBean.setUserName("wms");

        userService.setSelectChannel(userSessionBean, "001");

        assert userSessionBean.getActionPermission().size() > 0;
    }
}