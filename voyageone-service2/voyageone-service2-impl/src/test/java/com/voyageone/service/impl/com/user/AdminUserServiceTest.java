package com.voyageone.service.impl.com.user;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.com.AdminResourceBean;
import com.voyageone.service.bean.com.AdminRoleBean;
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

    @Autowired
    AdminResService adminResService;

    @Test
    public void testSearchUserByPage() throws Exception {

        PageModel<AdminUserBean> result =  adminUserService.searchUser(null, null, null, null, null,null, null, null,null, null);

        System.out.println(JacksonUtil.bean2Json(result));

    }

    @Test
    public void testAddUser() throws Exception {

        AdminUserBean model = new AdminUserBean();
        model.setActive(1);
        model.setUserName("admin");
        model.setPassword("123456");
//        model.setCredentialSalt("333");
        model.setUserAccount("admin");
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

        List<AdminResourceBean> result = adminUserService.showUserAuth("admin","admin");

        System.out.println(JacksonUtil.bean2Json(result));
    }

    @Test
    public void testGetAllApp() throws Exception {
        System.out.println(JacksonUtil.bean2Json(adminUserService.getAllApp()));
    }

    @Test
    public void testForgetPass() throws Exception {

        adminUserService.forgetPass("admin");

    }

    @Test
    public void testRestPass() throws Exception {
        adminUserService.restPass("0c357cc3f9454477944fc968f9ab36ec", "123456");

    }

    @Test
    public void testGetUserByToken() throws Exception {

       Map result = adminUserService.getUserByToken("0c357cc3f9454477944fc968f9ab36ec");
        System.out.println(JacksonUtil.bean2Json(result));

    }

    @Test
    public void testMoveUser() throws Exception {
        adminUserService.moveUser();

        AdminUserBean model = new AdminUserBean();
        model.setActive(1);
        model.setUserName("admin");
        model.setPassword("123456");
        model.setUserAccount("admin");
        model.setEmail("ethan.shi@voyageone.cn");
        model.setOrgId(1);
        model.setRoleId("4");

        adminUserService.addUser(model, "admin");
    }

    @Test
    public void testMoveApplication() throws Exception {
        adminUserService.moveApplication();
    }


    @Test
    public void testMovePermission() throws Exception {

        adminUserService.movePermission("001", 2 , "普通客服1组");
        adminUserService.movePermission("001", 5 , "售后客服1组");
        adminUserService.movePermission("001", 11 , "客服主管1组");
        adminUserService.addCsWmsPermission("仓库客服1组");

        adminUserService.movePermission("005", 2 , "普通客服2组");
        adminUserService.movePermission("005", 5 , "售后客服2组");
        adminUserService.movePermission("005", 11 , "客服主管2组");
        adminUserService.addCsWmsPermission("仓库客服2组");

        adminUserService.movePermission("023", 2 , "普通客服3组");
        adminUserService.movePermission("023", 5 , "售后客服3组");
        adminUserService.movePermission("023", 11 , "客服主管3组");
        adminUserService.addCsWmsPermission("仓库客服3组");

        adminUserService.movePermission("010", 2 , "普通客服4组");
        adminUserService.movePermission("010", 5 , "售后客服4组");
        adminUserService.movePermission("010", 11 , "客服主管4组");
        adminUserService.addCsWmsPermission("仓库客服4组");

        adminUserService.movePermission("018", 2 , "普通客服5组");
        adminUserService.movePermission("018", 5 , "售后客服5组");
        adminUserService.movePermission("018", 11 , "客服主管5组");
        adminUserService.addCsWmsPermission("仓库客服5组");
    }

    @Test
    public void testCreateRoles() throws Exception {
        adminUserService.createRoles();
    }

    @Test
    public void testAddRole4User() throws Exception {
        List<String>  cs1 = new ArrayList<String>(){ {add("普通客服1组") ; add("仓库客服1组") ;}};
        List<String>  cs1as = new ArrayList<String>(){ {add("普通客服1组") ;add("售后客服1组") ;add("仓库客服1组");}};
        List<String>  cs1mgr = new ArrayList<String>(){ {add("普通客服1组") ;add("售后客服1组") ;add("仓库客服1组");add("客服主管1组");}};
        List<String>  cs1com = new ArrayList<String>(){ {add("普通客服1组") ;}};

        adminUserService.addRole4User("emma.han", cs1mgr);
        adminUserService.addRole4User("wendy.liu", cs1mgr);
        adminUserService.addRole4User("susie.wang", cs1mgr);
        adminUserService.addRole4User("jon.jia", cs1mgr);

        adminUserService.addRole4User("nik.guo", cs1as);
        adminUserService.addRole4User("sunny.su", cs1as);
        adminUserService.addRole4User("tina.wang", cs1as);
        adminUserService.addRole4User("dora.zhang", cs1as);
        adminUserService.addRole4User("angela.yuan", cs1as);
        adminUserService.addRole4User("amy.li", cs1as);
        adminUserService.addRole4User("sofina.zhen", cs1as);
        adminUserService.addRole4User("elina.li", cs1as);
        adminUserService.addRole4User("believe.wang", cs1as);

        adminUserService.addRole4User("karl.sun", cs1);
        adminUserService.addRole4User("chard.cui", cs1);
        adminUserService.addRole4User("cloris.li", cs1);
        adminUserService.addRole4User("doris.xu", cs1);
        adminUserService.addRole4User("lucy.geng", cs1);
        adminUserService.addRole4User("karl.sun", cs1);
        adminUserService.addRole4User("karl.sun", cs1);
        adminUserService.addRole4User("karl.sun", cs1);
        adminUserService.addRole4User("karl.sun", cs1);
        adminUserService.addRole4User("karl.sun", cs1);
        adminUserService.addRole4User("karl.sun", cs1);
        adminUserService.addRole4User("karl.sun", cs1);
        adminUserService.addRole4User("karl.sun", cs1);
        adminUserService.addRole4User("karl.sun", cs1);
        adminUserService.addRole4User("karl.sun", cs1);

        adminUserService.addRole4User("karl.sun", cs1);
        adminUserService.addRole4User("karl.sun", cs1);
        adminUserService.addRole4User("karl.sun", cs1);
        adminUserService.addRole4User("karl.sun", cs1);
        adminUserService.addRole4User("karl.sun", cs1);
        adminUserService.addRole4User("karl.sun", cs1);
        adminUserService.addRole4User("karl.sun", cs1);





    }
}