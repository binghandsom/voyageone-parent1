package com.voyageone.service.impl.vms.shipment;

import com.voyageone.service.dao.vms.VmsBtShipmentDao;
import com.voyageone.service.model.vms.VmsBtShipmentModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * =。=
 * Created by vantis on 16-7-6.
 */
public class VmsShipmentService {

    private VmsBtShipmentDao vmsBtShipmentDao;

    @Autowired
    public VmsShipmentService(VmsBtShipmentDao vmsBtShipmentDao) {
        this.vmsBtShipmentDao = vmsBtShipmentDao;
    }

    public VmsBtShipmentModel select(Map<String, Object> shipmentSelectParams) {
        return vmsBtShipmentDao.selectOne(shipmentSelectParams);
    }

}
