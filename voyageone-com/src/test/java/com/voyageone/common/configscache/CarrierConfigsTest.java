package com.voyageone.common.configscache;

import com.google.common.base.Joiner;
import com.voyageone.common.configs.Enums.CarrierEnums;
import com.voyageone.common.configs.beans.CarrierBean;
import com.voyageone.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aooer 2016/3/23.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CarrierConfigsTest {

    @Test
    public void testGetCarrier() throws Exception {
        System.out.println(JsonUtil.bean2Json(CarrierConfigs.getCarrier("001", CarrierEnums.Name.valueOf("EMS"))));
    }

    @Test
    public void testGetCarrier1() throws Exception {
        System.out.println(JsonUtil.getJsonString(CarrierConfigs.getCarrier("001")));
    }
}