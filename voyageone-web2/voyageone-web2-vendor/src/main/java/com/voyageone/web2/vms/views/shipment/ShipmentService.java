package com.voyageone.web2.vms.views.shipment;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.service.impl.vms.shipment.VmsShipmentService;
import com.voyageone.service.model.vms.VmsBtShipmentModel;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.vms.VmsConstants;
import com.voyageone.web2.vms.bean.shipment.ExpressCompany;
import com.voyageone.web2.vms.bean.shipment.ShipmentBean;
import com.voyageone.web2.vms.bean.shipment.ShipmentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public Object submit(UserSessionBean user, VmsBtShipmentModel vmsBtShipmentModel) {

        if (!user.getSelChannel().getId().equals(vmsBtShipmentModel.getChannelId()))
            throw new BusinessException("8000022");

        boolean correct = null != vmsBtShipmentModel.getChannelId();
        correct = correct && (null != vmsBtShipmentModel.getStatus());
        correct = correct && (null != vmsBtShipmentModel.getShipmentName());

        if (correct && (VmsConstants.STATUS_VALUE.SHIPMENT_STATUS.SHIPPED.equals(vmsBtShipmentModel.getStatus()))) {
            correct = null != vmsBtShipmentModel.getExpressCompany();
            correct = correct && null != vmsBtShipmentModel.getTrackingNo();
        }

        if (!correct) throw new BusinessException("8000021");

        vmsBtShipmentModel.setModifier(user.getUserName());

        return vmsShipmentService.save(vmsBtShipmentModel);
    }

    /**
     * 获取当前用户/Channel已建立的shipment
     *
     * @param user 当前用户
     * @return shipmentBean
     */
    public ShipmentBean getCurrentShipment(UserSessionBean user) {

        Map<String, Object> shipmentSearchParams = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannel().getId());
            put("status", VmsConstants.STATUS_VALUE.SHIPMENT_STATUS.OPEN);
        }};
        VmsBtShipmentModel vmsBtShipmentModel = vmsShipmentService.select(shipmentSearchParams);
        if (null == vmsBtShipmentModel) return null;
        return new ShipmentBean(vmsBtShipmentModel);
    }
}
