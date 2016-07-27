package com.voyageone.web2.vms.views.shipment;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.util.BeanUtil;
import com.voyageone.service.impl.vms.order.OrderDetailService;
import com.voyageone.service.impl.vms.shipment.ShipmentService;
import com.voyageone.service.model.vms.VmsBtShipmentModel;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.vms.VmsConstants;
import com.voyageone.web2.vms.bean.shipment.*;
import com.voyageone.web2.vms.views.common.ChannelConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.voyageone.web2.vms.VmsConstants.*;

/**
 * shipment service
 * Created by vantis on 16-7-14.
 */
@Service
public class VmsShipmentService {

    private ShipmentService shipmentService;
    private OrderDetailService orderDetailService;
    private ChannelConfigService channelConfigService;

    @Autowired
    public VmsShipmentService(ShipmentService shipmentService,
                              OrderDetailService orderDetailService,
                              ChannelConfigService channelConfigService) {
        this.shipmentService = shipmentService;
        this.orderDetailService = orderDetailService;
        this.channelConfigService = channelConfigService;
    }

    /**
     * 获取shipment与定义的属性列表
     *
     * @return shipment与定义的属性列表
     */
    public List<ShipmentStatusBean> getAllStatus() {
        List<TypeBean> shipmentStatusList = Types.getTypeList(VmsConstants.TYPE_ID.SHIPMENT_STATUS);
        if (null == shipmentStatusList) return new ArrayList<>();
        return shipmentStatusList.stream()
                .map(typeBean -> new ShipmentStatusBean() {{
                    setName(typeBean.getName());
                    setValue(typeBean.getValue());
                }})
                .collect(Collectors.toList());
    }

    /**
     * 获取预定义的物流公司列表
     *
     * @return 获取预定义的物流公司列表
     */
    public List<ExpressCompanyBean> getAllExpressCompanies() {
        List<TypeBean> expressCompanyList = Types.getTypeList(VmsConstants.TYPE_ID.EXPRESS_COMPANY);
        if (null == expressCompanyList) return new ArrayList<>();
        return expressCompanyList.stream()
                .map(expressCompany -> new ExpressCompanyBean() {{
                    setName(expressCompany.getName());
                    setValue(expressCompany.getValue());
                }})
                .collect(Collectors.toList());
    }

    /**
     * 提交shipment修改
     *
     * @param user         当前用户
     * @param shipmentBean 待修改shipment
     * @return 修改影响条数
     */
    public int submit(UserSessionBean user, ShipmentBean shipmentBean) {

        VmsBtShipmentModel vmsBtShipmentModel = new VmsBtShipmentModel();
        BeanUtil.copy(shipmentBean, vmsBtShipmentModel);

        vmsBtShipmentModel.setChannelId(user.getSelChannelId());
        boolean correct = null != vmsBtShipmentModel.getStatus();
        correct = correct && null != vmsBtShipmentModel.getShipmentName();

        if (correct && (!STATUS_VALUE.SHIPMENT_STATUS.OPEN.equals(vmsBtShipmentModel.getStatus()))) {
            correct = null != vmsBtShipmentModel.getExpressCompany();
            correct = correct && null != vmsBtShipmentModel.getTrackingNo();
        }

        if (!correct) throw new BusinessException("8000021");

        vmsBtShipmentModel.setModifier(user.getUserName());

        return shipmentService.save(vmsBtShipmentModel);
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
        List<VmsBtShipmentModel> vmsBtShipmentModelList = shipmentService.select(shipmentSearchParams);
        if (null == vmsBtShipmentModelList || vmsBtShipmentModelList.size() == 0) return null;
        return ShipmentBean.getInstance(vmsBtShipmentModelList.get(0));
    }

    /**
     * 创建Shipment
     *
     * @param user         当前用户
     * @param shipmentBean 待保存shipment
     * @return 保存后的shipment
     */
    public int create(UserSessionBean user, ShipmentBean shipmentBean) {

        // 对于ORDER级别的channel 要确认是否已有活动的shipment
        if (STATUS_VALUE.VENDOR_OPERATE_TYPE.ORDER.equals(
                channelConfigService.getChannelConfigs(user).getVendorOperateType())
                && null != this.getCurrentShipment(user)) throw new BusinessException("8000022");

        VmsBtShipmentModel vmsBtShipmentModel = new VmsBtShipmentModel() {{
            setChannelId(user.getSelChannelId());
            setCreater(user.getUserName());
            setShipmentName(shipmentBean.getShipmentName());
            setShippedDate(shipmentBean.getShippedDate());
            setComment(shipmentBean.getComment());
            setExpressCompany(shipmentBean.getExpressCompany());
            setTrackingNo(shipmentBean.getTrackingNo());
            setStatus(shipmentBean.getStatus());
        }};
        return shipmentService.insert(vmsBtShipmentModel);
    }

    public ShipmentInfoBean search(UserSessionBean user, ShipmentSearchInfo shipmentSearchInfo) {

        ShipmentInfoBean shipmentInfoBean = new ShipmentInfoBean();

        Map<String, Object> searchParams = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannelId());
            put("shipmentName", shipmentSearchInfo.getShipmentName());
            put("trackingNo", shipmentSearchInfo.getTrackingNo());
            put("status", shipmentSearchInfo.getStatus());
            put("shippedDateFrom", shipmentSearchInfo.getShippedDateFrom());
            put("shippedDateTo", shipmentSearchInfo.getShippedDateTo());
        }};

        shipmentInfoBean.setTotal(shipmentService.count(searchParams));

        Map<String, Object> pagedSearchParams = MySqlPageHelper.build(searchParams)
                .addSort("Id", Order.Direction.DESC)
                .limit(shipmentSearchInfo.getSize())
                .page(shipmentSearchInfo.getCurr())
                .toMap();
        shipmentInfoBean.setShipmentList(shipmentService.searchList(pagedSearchParams).parallelStream()
                .map(ShipmentBean::getInstance)
                .map(shipmentBean -> {
                    shipmentBean.setOrderTotal(orderDetailService.countOrderWithShipment(shipmentBean.getChannelId(),
                            shipmentBean.getId()));
                    shipmentBean.setSkuTotal(orderDetailService.countSkuWithShipment(shipmentBean.getChannelId(),
                            shipmentBean.getId()));
                    return shipmentBean;
                })
                .collect(Collectors.toList()));
        return shipmentInfoBean;
    }

    public ShipmentBean getShipment(UserSessionBean user, Integer shipmentId) {
        VmsBtShipmentModel vmsBtShipmentModel = shipmentService.select(shipmentId);
        if (null == vmsBtShipmentModel
                || !shipmentId.equals(vmsBtShipmentModel.getId())
                || !user.getSelChannelId().equals(vmsBtShipmentModel.getChannelId()))
            throw new BusinessException("8000028"); // 无效的shipment编号
        return ShipmentBean.getInstance(vmsBtShipmentModel);
    }

    public int endShipment(UserSessionBean user, ShipmentBean shipment) {
        // 更新shipment
        int success = this.submit(user, shipment);
        // 更新shipment对应SKU信息
        if (success > 0) {
            orderDetailService.updateOrderStatusWithShipmentId(user.getSelChannelId(),
                    shipment.getId(), shipment.getStatus());
        }
        return success;
    }
}
