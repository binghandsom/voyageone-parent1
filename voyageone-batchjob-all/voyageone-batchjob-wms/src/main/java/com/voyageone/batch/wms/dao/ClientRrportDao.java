package com.voyageone.batch.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dell on 2015/11/26.
 */
@Repository
public class ClientRrportDao extends BaseDao {

    public int insertClientReport(String tempTable) {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("values", tempTable);
        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_insertClientReport", dataMap);
    }
}
