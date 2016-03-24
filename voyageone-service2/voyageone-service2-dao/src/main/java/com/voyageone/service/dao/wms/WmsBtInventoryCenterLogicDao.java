package com.voyageone.service.dao.wms;

import com.voyageone.service.model.wms.WmsBtInventoryCenterLogicModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author jerry 15/12/14
 * @version 2.0.0
 */

@Repository
public class WmsBtInventoryCenterLogicDao extends com.voyageone.service.dao.ServiceBaseDao {

    public List<WmsBtInventoryCenterLogicModel> selectItemDetailByCode(Map params){
        return selectList("select_wms_bt_inventory_center_logic_byChannelWithCodeOrSku", params);
    }

    public WmsBtInventoryCenterLogicModel selectItemDetailBySku(Map params){
        return selectOne("select_wms_bt_inventory_center_logic_byChannelWithCodeOrSku", params);
    }
}
