package com.voyageone.service.impl.com.user;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.com.AdminRoleBean;
import com.voyageone.service.bean.com.AdminUserBean;
import com.voyageone.service.model.com.PageModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by Ethan Shi on 2016-08-18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class AdminRoleServiceTest {

    @Autowired
    AdminRoleService adminRoleService;

    @Test
    public void testSearchRole() throws Exception {
        PageModel<AdminRoleBean> result =  adminRoleService.searchRole(null, null, null, null, null,null, null, null);

        System.out.println(JacksonUtil.bean2Json(result));

    }
}