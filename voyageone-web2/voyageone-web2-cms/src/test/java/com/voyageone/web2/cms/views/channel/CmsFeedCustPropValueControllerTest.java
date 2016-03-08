package com.voyageone.web2.cms.views.channel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
 * @author jiang, 2016/3/2
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:META-INF/context-web2.xml","classpath*:META-INF/context-web2-mvc.xml"})
public class CmsFeedCustPropValueControllerTest {

    @Autowired
    CmsFeedCustPropValueController cmsFeedCustPropValueController;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(cmsFeedCustPropValueController).build();
    }

    // 测试类目属性产值查询(查询所有)
    @Test
    public void testGetFeedCustPropValueList1() throws Exception {
        UserSessionBean userInfo = new UserSessionBean();
        userInfo.setSelChannelId("010");

        MockHttpServletRequestBuilder mb = MockMvcRequestBuilders.get("/cms/channel/custom/value/get").sessionAttr(BaseConstants.SESSION_USER, userInfo);
        mb.param("cat_path", "");
        mb.param("sts", "");
        mb.param("propName", "");
        mb.param("propValue", "");
        mb.param("skip", "0");
        mb.param("limit", "30");

        MockHttpServletResponse msr = mockMvc.perform(mb).andReturn().getResponse();
        String json = msr.getContentAsString();
        int sts = msr.getStatus();
        Gson gson3 = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String prettyJsonStr2 = gson3.toJson(je);
        System.out.println("prettyJsonStr: " + prettyJsonStr2);
        assertEquals(sts, 200);
    }

    // 测试类目属性产值查询(查询共通属性)
    @Test
    public void testGetFeedCustPropValueList2() throws Exception {
        UserSessionBean userInfo = new UserSessionBean();
        userInfo.setSelChannelId("010");

        MockHttpServletRequestBuilder mb = MockMvcRequestBuilders.get("/cms/channel/custom/value/get").sessionAttr(BaseConstants.SESSION_USER, userInfo);
        mb.param("cat_path", "0");
        mb.param("sts", "1");
        mb.param("propName", "35");
        mb.param("propValue", "y");
        mb.param("skip", "0");
        mb.param("limit", "30");

        MockHttpServletResponse msr = mockMvc.perform(mb).andReturn().getResponse();
        String json = msr.getContentAsString();
        int sts = msr.getStatus();
        Gson gson3 = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String prettyJsonStr2 = gson3.toJson(je);
        System.out.println("prettyJsonStr: " + prettyJsonStr2);
        assertEquals(sts, 200);
    }

    // 测试类目属性产值查询(查询指定类目)
    @Test
    public void testGetFeedCustPropValueList3() throws Exception {
        UserSessionBean userInfo = new UserSessionBean();
        userInfo.setSelChannelId("010");

        MockHttpServletRequestBuilder mb = MockMvcRequestBuilders.get("/cms/channel/custom/value/get").sessionAttr(BaseConstants.SESSION_USER, userInfo);
        mb.param("cat_path", "NOT_APPLICABLE - NOT_APPLICABLE - No Stone");
        mb.param("sts", "");
        mb.param("propName", "");
        mb.param("propValue", "");
        mb.param("skip", "0");
        mb.param("limit", "30");

        MockHttpServletResponse msr = mockMvc.perform(mb).andReturn().getResponse();
        String json = msr.getContentAsString();
        int sts = msr.getStatus();
        Gson gson3 = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String prettyJsonStr2 = gson3.toJson(je);
        System.out.println("prettyJsonStr: " + prettyJsonStr2);
        assertEquals(sts, 200);
    }

    // 测试新增Feed自定义属性值
    @Test
    public void testAddFeedCustPropValue1() throws Exception {
        UserSessionBean userInfo = new UserSessionBean();
        userInfo.setSelChannelId("010");
        userInfo.setUserName("jason");

        String paraStr = "{\"prop_id\":\"62\", \"value_original\":\"size\", \"value_translation\":\"尺寸\"}";

        MockHttpServletRequestBuilder mb = MockMvcRequestBuilders.post("/cms/channel/custom/value/create").sessionAttr(BaseConstants.SESSION_USER, userInfo)
                .accept("application/json").contentType("application/json").content(paraStr);

        MockHttpServletResponse msr = mockMvc.perform(mb).andReturn().getResponse();
        String json = msr.getContentAsString();
        int sts = msr.getStatus();
        Gson gson3 = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String prettyJsonStr2 = gson3.toJson(je);
        System.out.println("prettyJsonStr: " + prettyJsonStr2);
        assertEquals(sts, 200);
    }

    // 测试保存Feed自定义属性值
    @Test
    public void testSaveFeedCustPropValue1() throws Exception {
        UserSessionBean userInfo = new UserSessionBean();
        userInfo.setSelChannelId("010");
        userInfo.setUserName("jason");

        String paraStr = "{\"value_id\":\"56\", \"value_translation\":\"尺寸\" }";

        MockHttpServletRequestBuilder mb = MockMvcRequestBuilders.post("/cms/channel/custom/value/update").sessionAttr(BaseConstants.SESSION_USER, userInfo)
                .accept("application/json").contentType("application/json").content(paraStr);

        MockHttpServletResponse msr = mockMvc.perform(mb).andReturn().getResponse();
        String json = msr.getContentAsString();
        int sts = msr.getStatus();
        Gson gson3 = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String prettyJsonStr2 = gson3.toJson(je);
        System.out.println("prettyJsonStr: " + prettyJsonStr2);
        assertEquals(sts, 200);
    }
}