package com.voyageone.service.dao.wms;

import com.voyageone.service.bean.cms.jumei.ProductImportBean;
import com.voyageone.service.model.wms.WmsBtInventoryCenterLogicModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jerry 15/12/14
 * @version 2.0.0
 */

@Repository
public class WmsBtInventoryCenterLogicDao extends com.voyageone.service.dao.ServiceBaseDao {

    /**
     * todo 用于库存隔离使用,以后库存隔离重新设计的时候再删除,目前不会用到
     *
     * @param params
     * @return
     */
    @Deprecated
    public List<WmsBtInventoryCenterLogicModel> selectItemDetail(Map params){
        return selectList("wms_bt_inventory_center_logic_selectLogicInventory", params);
    }

    /**
     * todo 用于库存隔离使用,以后库存隔离重新设计的时候再删除,目前不会用到
     *
     * @param channelId
     * @param skuList
     * @return
     */
    @Deprecated
    public List<WmsBtInventoryCenterLogicModel> selectItemDetailBySkuList(String channelId, List<String> skuList){
        return selectList("wms_bt_inventory_center_logic_selectLogicInventory", parameters("channelId", channelId, "skuList", skuList));
    }

    /**
     * todo 用于库存隔离使用,以后库存隔离重新设计的时候再删除,目前不会用到
     *
     * @param param
     * @return
     */
    @Deprecated
    public Integer selectLogicInventoryCnt(Map<String, Object> param) {
        return selectOne("wms_bt_inventory_center_logic_selectLogicInventoryCnt", param);
    }

    /**
     * todo 用于库存隔离使用,以后库存隔离重新设计的时候再删除,目前不会用到
     * @param channelId
     * @param itemCodeOld
     * @param skuList
     * @param itemCodeNew
     * @param modifier
     * @return
     */
    @Deprecated
    public int updateCodeForMove(String channelId, String itemCodeOld, List<String> skuList, String itemCodeNew, String modifier) {
        Map<String, Object> params = new HashMap<>();
        params.put("channelId", channelId);
        params.put("itemCodeOld", itemCodeOld);
        params.put("skuList", skuList);
        params.put("itemCodeNew", itemCodeNew);
        params.put("modifier", modifier);
        return update("wms_bt_inventory_center_logic_updateCodeForMove", params);
    }

    /**
     * todo 用于库存隔离使用,以后库存隔离重新设计的时候再删除,目前不会用到
     * @param channelId
     * @param codeList
     * @return
     */
    @Deprecated
    public List<WmsBtInventoryCenterLogicModel> getInventoryByCode(String channelId, List<ProductImportBean> codeList) {
        Map<String, Object> params = new HashMap<>();
        params.put("channelId", channelId);
        params.put("codeList", codeList);
        return selectList("wms_bt_inventory_center_logic_selectLogicInventory_bycode", params);
    }
}
