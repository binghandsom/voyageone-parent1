package com.voyageone.web2.cms.wsdl.dao;

import com.voyageone.web2.sdk.api.domain.WmsBtInventoryCenterLogicModel;
import com.voyageone.web2.sdk.api.request.ProductSkuRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jerry 15/12/14
 * @version 2.0.0
 */

@Repository("web2.cms.wsdl.WmsBtInventoryCenterLogicDao")
public class WmsBtInventoryCenterLogicDao extends WsdlBaseDao{

    public List<WmsBtInventoryCenterLogicModel> getItemDetailByCode(ProductSkuRequest params){
        return selectList("select_wms_bt_inventory_center_logic_byChannelWithCodeOrSku", params);
    }

    public WmsBtInventoryCenterLogicModel getItemDetailBySku(ProductSkuRequest params){
        return selectOne("select_wms_bt_inventory_center_logic_byChannelWithCodeOrSku", params);
    }
}
