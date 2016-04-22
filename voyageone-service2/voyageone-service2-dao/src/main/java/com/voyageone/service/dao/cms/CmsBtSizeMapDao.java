package com.voyageone.service.dao.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtSizeMapModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author morse 16/04/21
 */

@Repository
public class CmsBtSizeMapDao extends ServiceBaseDao {

    public List<CmsBtSizeMapModel> selectSizeMapList(int sizeMapGroupId) {
        Map<String, Object> param = new HashMap<>();
        param.put("sizeMapGroupId", sizeMapGroupId);
        return selectList("cms_bt_size_map_select", param);
    }

    public CmsBtSizeMapModel selectSizeMap(int sizeMapGroupId, String originalSize) {
        Map<String, Object> param = new HashMap<>();
        param.put("sizeMapGroupId", sizeMapGroupId);
        param.put("originalSize", originalSize);
        return selectOne("cms_bt_size_map_select", param);
    }

}
