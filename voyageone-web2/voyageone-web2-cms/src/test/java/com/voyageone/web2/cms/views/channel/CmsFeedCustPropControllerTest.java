package com.voyageone.web2.cms.views.channel;

import com.voyageone.web2.base.BaseConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;

/**
 * @author jiang, 2016/2/26
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:META-INF/context-web2.xml","classpath*:META-INF/context-web2-mvc.xml"})
public class CmsFeedCustPropControllerTest {

    @Autowired
    CmsFeedCustPropController cmsFeedCustPropController;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(cmsFeedCustPropController).build();
    }

    @Test
    public void testGetFeedCustProp1() throws Exception {
        UserSessionBean userInfo = new UserSessionBean();
        userInfo.setSelChannelId("010");

        MockHttpServletRequestBuilder mb = MockMvcRequestBuilders.get("/cms/channel/custom/prop/get").sessionAttr(BaseConstants.SESSION_USER, userInfo);
        mb.param("cat_path", "NOT_APPLICABLE - NOT_APPLICABLE - No Stone");
        MockHttpServletResponse msr = mockMvc.perform(mb).andReturn().getResponse();
        String json = msr.getContentAsString();
        int sts = msr.getStatus();
        System.out.println(json);
        assertEquals(sts, 200);
    }

    @Test
    public void testGetCatTree1() throws Exception {
        UserSessionBean userInfo = new UserSessionBean();
        userInfo.setSelChannelId("010");

        MockHttpServletRequestBuilder mb = MockMvcRequestBuilders.get("/cms/channel/custom/prop/getCatTree").sessionAttr(BaseConstants.SESSION_USER, userInfo);
        MockHttpServletResponse msr = mockMvc.perform(mb).andReturn().getResponse();
        String json = msr.getContentAsString();
        int sts = msr.getStatus();
        System.out.println(json);
        assertEquals(sts, 200);
    }

    @Test
    public void testGetCatList1() throws Exception {
        UserSessionBean userInfo = new UserSessionBean();
        userInfo.setSelChannelId("010");

        MockHttpServletRequestBuilder mb = MockMvcRequestBuilders.get("/cms/channel/custom/prop/getCatList").sessionAttr(BaseConstants.SESSION_USER, userInfo);
        MockHttpServletResponse msr = mockMvc.perform(mb).andReturn().getResponse();
        String json = msr.getContentAsString();
        int sts = msr.getStatus();
        System.out.println(json);
        assertEquals(sts, 200);
    }
}