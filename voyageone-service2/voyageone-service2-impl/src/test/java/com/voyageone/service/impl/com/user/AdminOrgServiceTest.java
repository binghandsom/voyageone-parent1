package com.voyageone.service.impl.com.user;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.security.model.ComOrganizationModel;
import com.voyageone.service.bean.com.AdminUserBean;
import com.voyageone.service.model.com.PageModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;


/**
 * Created by Ethan Shi on 2016-08-17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class AdminOrgServiceTest {

    @Autowired
    AdminOrgService adminOrgService;

    @Test
    public void testGetAllOrg() throws Exception {

        Map<Integer, String> result = adminOrgService.getAllOrg();

        System.out.println(JacksonUtil.bean2Json(result));
    }

    @Test
    public void testSearchOrg() throws Exception {

        System.out.println(JacksonUtil.bean2Json(adminOrgService.searchOrg(new ComOrganizationModel(), 1, 10)));
    }
}