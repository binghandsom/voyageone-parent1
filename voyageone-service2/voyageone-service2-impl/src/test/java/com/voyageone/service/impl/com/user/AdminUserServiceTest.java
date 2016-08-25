package com.voyageone.service.impl.com.user;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.com.AdminUserBean;
import com.voyageone.service.model.com.PageModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ethan Shi on 2016-08-15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class AdminUserServiceTest {

    @Autowired
    AdminUserService adminUserService;

    @Test
    public void testSearchUserByPage() throws Exception {

        PageModel<AdminUserBean> result =  adminUserService.searchUser(null, null, null, null, null,null, null, null,null);

        System.out.println(JacksonUtil.bean2Json(result));

    }

    @Test
    public void testAddUser() throws Exception {

        AdminUserBean model = new AdminUserBean();
        model.setActive(1);
        model.setUserName("111");
        model.setPassword("222");
        model.setCredentialSalt("333");
        model.setUserAccount("444");
        model.setEmail("1111@123.com");
        model.setOrgId(1);
        model.setRoleId("1");

        adminUserService.addUser(model, "test");

    }

    @Test
    public void testUpdateUser() throws Exception {

        AdminUserBean model = new AdminUserBean();
        model.setId(5);
        model.setActive(1);
        model.setUserName("1111");
        model.setPassword("2222");
        model.setCredentialSalt("3333");
        model.setUserAccount("4444");
        model.setEmail("11111@123.com");
        model.setOrgId(1);
        model.setRoleId("1");
        model.setApplication("admin");

        adminUserService.updateUser(model, "test");

    }

    @Test
    public void testDeleteUser() throws Exception {

        List<Integer> list= new ArrayList<>();
        list.add(5);
        adminUserService.deleteUser(list, "test");
    }


    @Test
    public void testShowAuth() throws Exception {

//        Map result = adminUserService.showAuth(1);

//        System.out.println(JacksonUtil.bean2Json(result));
    }

    @Test
    public void testGetAllApp() throws Exception {
        System.out.println(JacksonUtil.bean2Json(adminUserService.getAllApp()));
    }

    @Test
    public void testForgetPass() throws Exception {

        adminUserService.forgetPass("admin");

    }
}