package com.voyageone.service.impl.vms.shipment;

import com.voyageone.service.dao.vms.VmsBtShipmentDao;
import com.voyageone.service.daoext.vms.VmsBtShipmentDaoExt;
import com.voyageone.service.model.vms.VmsBtOrderDetailModel;
import com.voyageone.service.model.vms.VmsBtShipmentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * =。=
 * Created by vantis on 16-7-6.
 */
@Service
public class ShipmentService {

    private VmsBtShipmentDao vmsBtShipmentDao;

    private VmsBtShipmentDaoExt vmsBtShipmentDaoExt;

    @Autowired
    public ShipmentService(VmsBtShipmentDao vmsBtShipmentDao, VmsBtShipmentDaoExt vmsBtShipmentDaoExt) {
        this.vmsBtShipmentDao = vmsBtShipmentDao;
        this.vmsBtShipmentDaoExt = vmsBtShipmentDaoExt;
    }

    public VmsBtShipmentModel select(int shipmentId) {
        return vmsBtShipmentDao.select(shipmentId);
    }

    public List<VmsBtShipmentModel> select(Map<String, Object> shipmentSelectParams) {
        return vmsBtShipmentDao.selectList(shipmentSelectParams);
    }

    public int save(VmsBtShipmentModel vmsBtShipmentModel) {
        return vmsBtShipmentDaoExt.update(vmsBtShipmentModel);
    }


    public void insert(VmsBtShipmentModel vmsBtShipmentModel) {
        vmsBtShipmentDao.insert(vmsBtShipmentModel);
    }
}
