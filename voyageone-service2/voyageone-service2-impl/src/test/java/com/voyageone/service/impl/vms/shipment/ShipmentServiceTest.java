package com.voyageone.service.impl.vms.shipment;

import com.voyageone.service.model.vms.VmsBtShipmentModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author vantis
 * @version 1.0.0
 * @date 2017/1/4
 * @description 闲舟江流夕照晚 =。=
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class ShipmentServiceTest {
    @Autowired
    ShipmentService shipmentService;
    @Test
    public void selectList() throws Exception {
        HashMap<String, Object> param = new HashMap<>();
        param.put("channelId", "031");
        param.put("shipmentId", 15281);
        List<VmsBtShipmentModel> vmsBtShipmentModels = shipmentService.selectList(param);
        Assert.assertEquals(1, vmsBtShipmentModels.size());
    }

}