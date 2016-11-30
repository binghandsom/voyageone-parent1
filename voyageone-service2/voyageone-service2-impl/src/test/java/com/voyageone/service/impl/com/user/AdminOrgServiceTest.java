package com.voyageone.service.impl.com.user;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.security.model.ComOrganizationModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;


/**
 * Created by Ethan Shi on 2016-08-17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class AdminOrgServiceTest {

    @Autowired
    AdminOrgService adminOrgService;

    @Test
    public void testGetAllOrg() throws Exception {

        List<Map<String, Object> > result = adminOrgService.getAllOrg();

        System.out.println(JacksonUtil.bean2Json(result));
    }

    @Test
    public void testSearchOrg() throws Exception {

        System.out.println(JacksonUtil.bean2Json(adminOrgService.searchOrg(null,null, 1, 10)));
    }

    @Test
    public void testAddOrg() throws Exception {
        ComOrganizationModel model = new ComOrganizationModel();

        model.setActive(1);
        model.setOrgName("6677788");
        model.setWeight(1);
        model.setParentId(1);
        model.setCreater("admin");
        adminOrgService.addOrg(model);

    }

    @Test
    public void testUpdateOrg() throws Exception {
        ComOrganizationModel model = new ComOrganizationModel();
        model.setId(7);
        model.setActive(1);
        model.setOrgName("554433");
        model.setWeight(1);
        model.setParentId(2);
        model.setModifier("admin");
        adminOrgService.updateOrg(model);
    }

    @Test
    public void testDeleteOrg() throws Exception {

    }
}