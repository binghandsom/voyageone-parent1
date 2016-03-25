package com.voyageone.service.dao.wms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtInventoryOutputTmpModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jerry 15/12/14
 * @version 2.0.0
 */

@Repository
public class WmsBtInventoryCenterOutputTmpDao extends ServiceBaseDao {

    public boolean insertSkuInventoryInfo(String values) {
        boolean ret = true;

        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("values", values);

        int retCount = insert("wms_bt_inventory_center_output_tmp_insertSKUData", dataMap);

        return  ret;
    }

    public boolean deleteSkuInventoryInfo() {
        delete("wms_bt_inventory_center_output_tmp_deleteAll");

        return true;
    }

    public int selectSkuInventoryInfoRecCount() {
        return selectOne("wms_bt_inventory_center_output_tmp_getRecCount");
    }

    public List<CmsBtInventoryOutputTmpModel> selectSkuInventoryInfoRecInfo(int offset, int pagesize) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("offset", offset * pagesize);
        dataMap.put("pagesize", pagesize);

        List<CmsBtInventoryOutputTmpModel> ret = selectList("wms_bt_inventory_center_output_tmp_getRecInfo", dataMap);

        return  ret;
    }
}
