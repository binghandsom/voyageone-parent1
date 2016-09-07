package com.voyageone.service.impl.com.user;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.security.model.ComRoleModel;
import com.voyageone.service.bean.com.AdminResourceBean;
import com.voyageone.service.bean.com.AdminRoleBean;
import com.voyageone.service.bean.com.AdminUserBean;
import com.voyageone.service.dao.com.ComMtTypeDao;
import com.voyageone.service.model.com.ComMtTypeModel;
import com.voyageone.service.model.com.PageModel;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Ethan Shi on 2016-08-18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class AdminRoleServiceTest {

    @Autowired
    AdminRoleService adminRoleService;

    @Autowired
    ComMtTypeDao  comMtTypeDao;

    @Test
    public void testSearchRole() throws Exception {
        PageModel<AdminRoleBean> result =  adminRoleService.searchRole( null, null);

        System.out.println(JacksonUtil.bean2Json(result));

    }

    @Test
    public void testAddRole() throws Exception {

        List<String>  applications = new ArrayList<String>(){ {add("cms") ;add("wms") ;add("admin"); }};
        List<String>  channelIds = new ArrayList<String>(){ {add("010") ; }};
        List<String>  StoreIds  =new ArrayList<String>(){ {add("34") ;add("125"); }};

        Map<String, Object> map = new HashMap<>();
        map.put("roleName", "010客服");
        map.put("roleType", 1);
        map.put("applications", applications);
        map.put("allChannel", "0");
        map.put("channelIds", channelIds);
        map.put("allStore", "0");
        map.put("StoreIds",StoreIds);
        map.put("description", "010客服");

        ComRoleModel model = new ComRoleModel();

        BeanUtils.populate(model, map);
        model.setCreater("test");

        adminRoleService.addRole(model, applications, channelIds, StoreIds , "0", "0");


    }

    @Test
    public void testUpdateRole() throws Exception {
        List<String>  applications = new ArrayList<String>(){ {add("cms") ;add("wms") ; }};
        List<String>  channelIds = new ArrayList<String>(){ {add("010") ; }};
        List<String>  StoreIds  =new ArrayList<String>(){ {add("34"); }};

        Map<String, Object> map = new HashMap<>();
        map.put("id", 6);
        map.put("roleName", "010客服");
        map.put("roleType", 1);
        map.put("applications", applications);
        map.put("allChannel", "0");
        map.put("channelIds", channelIds);
        map.put("allStore", "0");
        map.put("StoreIds",StoreIds);
        map.put("description", "010客服111");

        ComRoleModel model = new ComRoleModel();

        BeanUtils.populate(model, map);
        model.setCreater("test");

        adminRoleService.updateRole(model, applications, channelIds, StoreIds , "0", "0");

    }

    @Test
    public void testGetAuthByRoles() throws Exception {
        List<Integer> roles = new ArrayList<>();

        roles.add(4);

        List<AdminResourceBean> result =  adminRoleService.getAuthByRoles(roles, "admin");

        System.out.println(JacksonUtil.bean2Json(result));



    }
}