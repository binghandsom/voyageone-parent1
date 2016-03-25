package com.voyageone.common.configs;

import com.voyageone.common.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author aooer 2016/3/23.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class TypeConfigsTest {

    @Test
    public void testGetTypeName() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Types.getTypeName(1,"04")));
    }

    @Test
    public void testGetTypeBean() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Types.getTypeBean(1)));
    }

    @Test
    public void testGetTypeBean1() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Types.getTypeBean(1,"en")));
    }

    @Test
    public void testGetTypeMap() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Types.getTypeMap(1)));
    }

    @Test
    public void testGetTypeMapList() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Types.getTypeMapList(1,true)));
    }

    @Test
    public void testGetMasterInfoFromId() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Types.getMasterInfoFromId(1,true)));
    }

    @Test
    public void testGetTypeList() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Types.getTypeMapList(1,true)));
    }

    @Test
    public void testGetTypeList1() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Types.getTypeList(1,"en")));
    }

    @Test
    public void testGetValue() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Types.getValue(1,"Pending Refund")));
    }

    @Test
    public void testGetTypeName1() throws Exception {
        System.out.println(Types.getTypeName(1));
    }

    @Test
    public void testGetTypeName2() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Types.getTypeName(1,"en","01")));
    }

    @Test
    public void testGetTypeBean2() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Types.getTypeBean(1,"en")));
    }

    @Test
    public void testGetTypeMap1() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Types.getTypeMap(1,"en")));
    }

    @Test
    public void testGetTypeMapList1() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Types.getTypeMapList(1,true,"en")));
    }

    @Test
    public void testGetMasterInfoFromId1() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Types.getMasterInfoFromId(1,true,"en")));
    }

    @Test
    public void testGetTypeList2() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Types.getTypeList(1,"en")));
    }

    @Test
    public void testGetTypeList3() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Types.getTypeList("quickFilter")));
        System.out.println(JacksonUtil.bean2Json(Types.getTypeList("quickFilter","en")));
    }

    @Test
    public void testGetValue1() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Types.getValue(1,"Pending Refund","en")));
    }

    @Test
    public void testGetTypeListById() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Types.getTypeListById(1)));
    }
}