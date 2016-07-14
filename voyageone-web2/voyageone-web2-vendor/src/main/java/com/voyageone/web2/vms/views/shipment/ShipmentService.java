package com.voyageone.web2.vms.views.shipment;

import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.service.impl.vms.shipment.VmsShipmentService;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.vms.VmsConstants;
import com.voyageone.web2.vms.bean.shipment.ExpressCompany;
import com.voyageone.web2.vms.bean.shipment.ShipmentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * shipment service
 * Created by vantis on 16-7-14.
 */
@Service
public class ShipmentService {

    private VmsShipmentService vmsShipmentService;

    @Autowired
    public ShipmentService(VmsShipmentService vmsShipmentService) {
        this.vmsShipmentService = vmsShipmentService;
    }

    public List<ShipmentStatus> getAllStatus() {
        List<TypeBean> shipmentStatusList = Types.getTypeList(VmsConstants.TYPE_ID.SHIPMENT_STATUS);
        if (null == shipmentStatusList) return new ArrayList<>();
        return shipmentStatusList.stream()
                .map(typeBean -> new ShipmentStatus() {{
                    setName(typeBean.getName());
                    setValue(typeBean.getValue());
                }})
                .collect(Collectors.toList());
    }

    public List<ExpressCompany> getAllExpressCompines() {
        List<TypeBean> expressCompanyList = Types.getTypeList(VmsConstants.TYPE_ID.EXPRESS_COMPANY);
        if (null == expressCompanyList) return new ArrayList<>();
        return expressCompanyList.stream()
                .map(expressCompany -> new ExpressCompany() {{
                    setName(expressCompany.getName());
                    setValue(expressCompany.getValue());
                }})
                .collect(Collectors.toList());
    }
}
