package com.voyageone.service.dao.ims;

import com.voyageone.service.dao.ServiceBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author morse 16/04/06
 * @version 2.0.0
 */

@Repository
public class ImsBtLogSynInventoryDao extends ServiceBaseDao {

    public int insertByList(List<Map<String, Object>> param) {
        return insert("ims_bt_log_syn_inventory_insertList", param);
    }
}
