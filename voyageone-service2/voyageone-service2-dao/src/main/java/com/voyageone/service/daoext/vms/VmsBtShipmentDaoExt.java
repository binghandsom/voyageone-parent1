package com.voyageone.service.daoext.vms;

import com.voyageone.service.model.vms.VmsBtShipmentModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * shipment扩展dao
 * Created by vantis on 2016/7/15.
 */
@Repository
public interface VmsBtShipmentDaoExt {

    int update(VmsBtShipmentModel vmsBtShipmentModel);

    int count(Map<String, Object> searchParams);

    List<VmsBtShipmentModel> selectList(Map<String, Object> pagedSearchParams);
}
