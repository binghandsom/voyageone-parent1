package com.voyageone.service.impl.com.user;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.com.AdminResourceBean;
import com.voyageone.service.bean.com.AdminUserBean;
import com.voyageone.service.bean.com.PaginationBean;
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
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class AdminUserServiceTest {

    @Autowired
    AdminUserService adminUserService;

    @Autowired
    AdminResService adminResService;

    @Test
    public void testSearchUserByPage() throws Exception {

        PaginationBean<AdminUserBean> result =  adminUserService.searchUser(null, null, null, null, null,null, null, null,null, null);

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
    public void testMovePermission1() throws Exception {
        adminUserService.movePermission("001", 1 , "WMS_ADMIN");
        adminUserService.movePermission("014", 2 , "WMS_CS");
        adminUserService.movePermission("001", 3 , "WMS_WHS");
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
        adminUserService.addRole4User("andy.zhao", cs1);
        adminUserService.addRole4User("alisa.deng", cs1);
        adminUserService.addRole4User("molly.yang", cs1);
        adminUserService.addRole4User("lyric.zhang", cs1);
        adminUserService.addRole4User("janus.guo", cs1);
        adminUserService.addRole4User("archer.fu", cs1);
        adminUserService.addRole4User("king.gao", cs1);
        adminUserService.addRole4User("emi.ai", cs1);
        adminUserService.addRole4User("fare.zou", cs1);
        adminUserService.addRole4User("beca.zhao", cs1);
        adminUserService.addRole4User("ganger.ma", cs1);
        adminUserService.addRole4User("mike.song", cs1);
        adminUserService.addRole4User("lucky.sun", cs1);
        adminUserService.addRole4User("allion.shao", cs1);
        adminUserService.addRole4User("bella.bi", cs1);
        adminUserService.addRole4User("zola.wang", cs1);
        adminUserService.addRole4User("jason.ma", cs1);

        adminUserService.addRole4User("lisa.lin", cs1com);


        List<String>  cs2 = new ArrayList<String>(){ {add("普通客服2组") ; add("仓库客服2组") ;}};
        List<String>  cs2as = new ArrayList<String>(){ {add("普通客服2组") ;add("售后客服2组") ;add("仓库客服2组");}};
        List<String>  cs2mgr = new ArrayList<String>(){ {add("普通客服2组") ;add("售后客服2组") ;add("仓库客服2组");add("客服主管2组");}};
        List<String>  cs2com = new ArrayList<String>(){ {add("普通客服2组") ;}};

        adminUserService.addRole4User("emma.han", cs2mgr);
        adminUserService.addRole4User("wendy.liu", cs2mgr);
        adminUserService.addRole4User("james.gao", cs2mgr);

        adminUserService.addRole4User("jessica.zhao", cs2as);
        adminUserService.addRole4User("simen.li", cs2as);
        adminUserService.addRole4User("vera.xing", cs2as);
        adminUserService.addRole4User("penny.liu", cs2as);

        adminUserService.addRole4User("nancy.zhang", cs2);
        adminUserService.addRole4User("joan.wu", cs2);
        adminUserService.addRole4User("karen.ru", cs2);
        adminUserService.addRole4User("poppy.ya", cs2);
        adminUserService.addRole4User("yedda.yang", cs2);
        adminUserService.addRole4User("len.yang", cs2);
        adminUserService.addRole4User("kris.liu", cs2);
        adminUserService.addRole4User("miya.ma", cs2);
        adminUserService.addRole4User("jason.ma", cs2);

        adminUserService.addRole4User("lisa.lin", cs2com);


        List<String>  cs3 = new ArrayList<String>(){ {add("普通客服3组") ; add("仓库客服3组") ;}};
        List<String>  cs3as = new ArrayList<String>(){ {add("普通客服3组") ;add("售后客服3组") ;add("仓库客服3组");}};
        List<String>  cs3mgr = new ArrayList<String>(){ {add("普通客服3组") ;add("售后客服3组") ;add("仓库客服3组");add("客服主管3组");}};
        List<String>  cs3com = new ArrayList<String>(){ {add("普通客服3组") ;}};

        adminUserService.addRole4User("emma.han", cs3mgr);
        adminUserService.addRole4User("wendy.liu", cs3mgr);
        adminUserService.addRole4User("kane.hou", cs3mgr);

        adminUserService.addRole4User("coco.wang", cs3as);
        adminUserService.addRole4User("kevin.di", cs3as);
        adminUserService.addRole4User("lily.song", cs3as);
        adminUserService.addRole4User("cici.guan", cs3as);

        adminUserService.addRole4User("joyce.chang", cs3);
        adminUserService.addRole4User("jenny.ma", cs3);
        adminUserService.addRole4User("lea.yuan", cs3);
        adminUserService.addRole4User("luna.li", cs3);
        adminUserService.addRole4User("susan.li", cs3);
        adminUserService.addRole4User("ada.zhang", cs3);
        adminUserService.addRole4User("an.dong", cs3);
        adminUserService.addRole4User("dave.yin", cs3);
        adminUserService.addRole4User("jason.ma", cs3);

        adminUserService.addRole4User("lisa.lin", cs3com);


        List<String>  cs4 = new ArrayList<String>(){ {add("普通客服4组") ; add("仓库客服4组") ;}};
        List<String>  cs4as = new ArrayList<String>(){ {add("普通客服4组") ;add("售后客服4组") ;add("仓库客服4组");}};
        List<String>  cs4mgr = new ArrayList<String>(){ {add("普通客服4组") ;add("售后客服4组") ;add("仓库客服4组");add("客服主管4组");}};
        List<String>  cs4com = new ArrayList<String>(){ {add("普通客服4组") ;}};

        adminUserService.addRole4User("emma.han", cs4mgr);
        adminUserService.addRole4User("sophia.shan", cs4mgr);
        adminUserService.addRole4User("hedy.yu", cs4mgr);

        adminUserService.addRole4User("olivia.zhou", cs4as);
        adminUserService.addRole4User("anne.mao", cs4as);
        adminUserService.addRole4User("spring.xu", cs4as);
        adminUserService.addRole4User("flora.yang", cs4as);

        adminUserService.addRole4User("sara.cong", cs4);
        adminUserService.addRole4User("kammy.wu", cs4);
        adminUserService.addRole4User("jodie.liu", cs4);
        adminUserService.addRole4User("ann.jiang", cs4);
        adminUserService.addRole4User("aimee.dai", cs4);
        adminUserService.addRole4User("erica.wang", cs4);
        adminUserService.addRole4User("run.sun", cs4);
        adminUserService.addRole4User("lilian.wang", cs4);
        adminUserService.addRole4User("jason.ma", cs4);

        adminUserService.addRole4User("lisa.lin", cs4com);

        List<String>  cs5 = new ArrayList<String>(){ {add("普通客服5组") ; add("仓库客服5组") ;}};
        List<String>  cs5as = new ArrayList<String>(){ {add("普通客服5组") ;add("售后客服5组") ;add("仓库客服5组");}};
        List<String>  cs5mgr = new ArrayList<String>(){ {add("普通客服5组") ;add("售后客服5组") ;add("仓库客服5组");add("客服主管5组");}};
        List<String>  cs5com = new ArrayList<String>(){ {add("普通客服5组") ;}};

        adminUserService.addRole4User("emma.han", cs5mgr);
        adminUserService.addRole4User("sophia.shan", cs5mgr);
        adminUserService.addRole4User("yoyo.ma", cs5mgr);

        adminUserService.addRole4User("snow.han", cs5as);
        adminUserService.addRole4User("crystal.liu", cs5as);
        adminUserService.addRole4User("abby.liang", cs5as);
        adminUserService.addRole4User("ailsa.li", cs5as);
        adminUserService.addRole4User("gloria.tong", cs5as);

        adminUserService.addRole4User("candy.sun", cs5);
        adminUserService.addRole4User("start.zhang", cs5);
        adminUserService.addRole4User("joy.zhou", cs5);
        adminUserService.addRole4User("merry.li", cs5);
        adminUserService.addRole4User("ella.yang", cs5);
        adminUserService.addRole4User("jason.ma", cs5);

        adminUserService.addRole4User("lisa.lin", cs5com);


    }

    @Test
    public void testCopyRoleAuth() throws Exception {
//        adminUserService.copyRoleAuth("普通客服1组", "财务1组");

        adminUserService.copyRoleAuth("财务1组", "财务2组");
        adminUserService.copyRoleAuth("财务1组", "财务3组");
        adminUserService.copyRoleAuth("财务1组", "财务4组");
        adminUserService.copyRoleAuth("财务1组", "财务5组");

        adminUserService.copyRoleAuth("财务1组", "财务_001");
        adminUserService.copyRoleAuth("财务1组", "财务_010");
        adminUserService.copyRoleAuth("财务1组", "财务_005");
        adminUserService.copyRoleAuth("财务1组", "财务_007");
        adminUserService.copyRoleAuth("财务1组", "财务_007");
        adminUserService.copyRoleAuth("财务1组", "财务_012");

    }
}