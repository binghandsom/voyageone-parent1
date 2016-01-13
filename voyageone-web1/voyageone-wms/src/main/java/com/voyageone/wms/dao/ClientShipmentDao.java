package com.voyageone.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
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
     * @return List<ClientShipmentBean>
     */
    public List<ClientShipmentBean> getNotMatchShipmentList(List<String> orderChannelIdList) {

        Map<String, Object> params = new HashMap<>();

        params.put("orderChannelIdList", orderChannelIdList);

        return selectList("client_shipment_selectNotMatchShipment", params);
    }

}
