package com.voyageone.batch.synship.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.synship.modelbean.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ShipmentDao extends BaseDao {

    @Override
    protected String namespace() {
        return "com.voyageone.batch.synship.sql";
    }

    /**
     * 获取模拟的Shipment
     *
     * @param order_channel_id  渠道
     * @param shipment_name     shipment_name
     * @return ShipmentBean
     */
    public ShipmentBean getShipmentDelay(String order_channel_id, String shipment_name) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("shipment_name", shipment_name);

        return updateTemplate.selectOne("synShip_getShipmentDelay", params);
    }

    /**
     * 插入Shipment
     *
     * @param shipmentBean 物流信息
     */
    public int insertShipment(ShipmentBean shipmentBean) {

        return updateTemplate.insert( "synShip_InsertShipment", shipmentBean);
    }

    /**
     * 插入ShipmentInfo
     *
     * @param shipmentInfoBean 物流信息
     */
    public int insertShipmentInfo(ShipmentInfoBean shipmentInfoBean) {

        return updateTemplate.insert( "synShip_InsertShipmentInfo", shipmentInfoBean);
    }

    /**
     * 获取Package
     *
     * @param shipment_id
     * @param tracking_no 快递单号
     * @return PackageBean
     */
    public PackageBean getPackage(String shipment_id, String tracking_no) {
        Map<String, Object> params = new HashMap<>();

        params.put("shipment_id", shipment_id);
        params.put("tracking_no", tracking_no);

        return updateTemplate.selectOne("synShip_getPackage", params);
    }

    /**
     * 插入Package
     *
     * @param packageBean 物流信息
     */
    public int insertPackage(PackageBean packageBean) {

        return updateTemplate.insert( "synShip_InsertPackage", packageBean);
    }

    /**
     * 获取PackageItem
     *
     * @param shipment_id
     * @param package_id
     * @param syn_ship_no
     * @param reservation_id
     */
    public PackageItemBean getPackageItem(String shipment_id, String package_id, String syn_ship_no, long reservation_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("shipment_id", shipment_id);
        params.put("package_id", package_id);
        params.put("syn_ship_no", syn_ship_no);
        params.put("reservation_id", reservation_id);

        return updateTemplate.selectOne("synShip_getPackageItem", params);
    }

    /**
     * 插入PackageItem
     *
     * @param packageItemBean 物流信息
     */
    public int insertPackageItem(PackageItemBean packageItemBean) {

        return updateTemplate.insert( "synShip_InsertPackageItem", packageItemBean);
    }


}
