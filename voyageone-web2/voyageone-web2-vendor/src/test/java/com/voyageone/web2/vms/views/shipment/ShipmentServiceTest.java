package com.voyageone.web2.vms.views.shipment;

import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.web2.vms.bean.shipment.ShipmentStatusBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * shipment service 测试
 * Created by vantis on 16-7-14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:META-INF/context-web2.xml","classpath*:META-INF/context-web2-mvc.xml"})
public class ShipmentServiceTest extends VOAbsLoggable{

    @Autowired
    private VmsShipmentService shipmentService;

    @Test
    public void getAllStatus() throws Exception {
        List<ShipmentStatusBean> shipmentStatuseBeen = shipmentService.getAllStatus();
        $info(JsonUtil.bean2Json(shipmentStatuseBeen));
    }

}