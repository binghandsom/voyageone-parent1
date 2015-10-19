package com.voyageone.batch.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.wms.modelbean.ClientPackageBean;
import com.voyageone.batch.wms.modelbean.ClientPackageItemBean;
import com.voyageone.batch.wms.modelbean.ClientShipmentBean;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

@Repository
public class ClientShipmentDao extends BaseDao {

     /**
     * 插入wms_bt_client_shipment
     * @param clientShipmentBean client_shipment记录
     * @return long
     */
    public long insertClientShipment(ClientShipmentBean clientShipmentBean) {

        long shipmentId = 0;
        int retCount =  updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_bt_client_shipment_insert", clientShipmentBean);

        if (retCount > 0) {
            shipmentId = clientShipmentBean.getShipment_id();
        }
        return shipmentId;

    }

    /**
     * 插入wms_bt_client_package
     * @param clientPackageBean client_package记录
     * @return long
     */
    public long insertClientPackage(ClientPackageBean clientPackageBean) {

        long packageId = 0;
        int retCount =  updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_bt_client_package_insert", clientPackageBean);

        if (retCount > 0) {
            packageId = clientPackageBean.getPackage_id();
        }
        return packageId;

    }

    /**
     * 插入wms_bt_client_package_item
     * @param clientPackageItemBean client_package_item记录
     * @return int
     */
    public int insertClientPackageItem(ClientPackageItemBean clientPackageItemBean) {

        return  updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_bt_client_package_item_insert", clientPackageItemBean);

    }

}
