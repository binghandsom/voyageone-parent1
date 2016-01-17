package com.voyageone.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.wms.formbean.ClientShipmentCompareBean;
import com.voyageone.wms.formbean.FormClientShipmentBean;
import com.voyageone.wms.modelbean.ClientShipmentBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ClientShipmentDao extends BaseDao {

    @Override
    protected String namespace() {
        return "com.voyageone.wms.sql";
    }

    /**
     * @Description 获取未匹配的ClientShipment
     * @param orderChannelIdList 对象
     * @param transfer_id 对象Transfer
     * @return List<ClientShipmentBean>
     */
    public List<ClientShipmentBean> getNotMatchShipmentList(List<String> orderChannelIdList,String transfer_id) {

        Map<String, Object> params = new HashMap<>();

        params.put("orderChannelIdList", orderChannelIdList);
        params.put("transfer_id", transfer_id);

        return selectList("client_shipment_selectNotMatchShipment", params);
    }

    /**
     * @Description 获取ClientShipment和Transfer的比较结果
     * @param transfer_id 对象Transfer
     * @param client_shipment_id 对象ClientShipment
     * @return List<ClientShipmentCompareBean>
     */
    public List<ClientShipmentCompareBean> getCompareResult(String transfer_id,String client_shipment_id) {

        Map<String, Object> params = new HashMap<>();

        params.put("transfer_id", transfer_id);
        params.put("shipment_id", client_shipment_id);

        return selectList("client_shipment_selectCompareResult", params);
    }

    /**
     * @Description 获取指定ID的ClientShipment
     * @param shipment_id 对象ClientShipment
     * @return List<ClientShipmentCompareBean>
     */
    public List<FormClientShipmentBean> getPackageResult(String shipment_id) {

        Map<String, Object> params = new HashMap<>();

        params.put("shipment_id", shipment_id);

        return selectList("client_shipment_selectPackageResult", params);
    }

}
