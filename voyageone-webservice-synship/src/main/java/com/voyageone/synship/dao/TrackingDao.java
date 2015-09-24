package com.voyageone.synship.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.synship.formbean.TrackInfoBean;
import com.voyageone.synship.modelbean.WaybillRouteBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2015/7/23.
 */
@Repository
public class TrackingDao extends BaseDao {

    @Override
    protected String namespace() {
        return "com.voyageone.synship.sql";
    }

    /**
     * 获得物流单号
     * @param cwb 查询参数
     * @return syn_ship_no
     */
    public String getSynShipNo(String cwb) {
        return selectOne("tt_orders_getSynShipNo", cwb);
    }

    /**
     * 获得物流信息
     * @param syn_ship_no synShip物流单号
     * @return List<FromReservation>
     */
    public List<TrackInfoBean> getTrackingInfo(String syn_ship_no) {
        return selectList("tt_tracking_info_select", syn_ship_no);
    }

    /**
     * 获得具体物流信息
     * @param tracking_no 快递单号
     * @param tracking_type 快递公司
     * @return  List<WaybillRouteBean>
     */
    public List<WaybillRouteBean> getWaybillRoute(String tracking_no, String tracking_type) {

        Map<String, Object> params = new HashMap<>();

        params.put("tracking_no", tracking_no);
        params.put("tracking_type", tracking_type);

        return selectList("tt_waybillRoute_select", params);
    }
}
