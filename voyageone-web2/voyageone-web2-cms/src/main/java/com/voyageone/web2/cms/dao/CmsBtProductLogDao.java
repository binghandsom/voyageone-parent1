package com.voyageone.web2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.web2.cms.model.CmsBtProductLogModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/24
 * @version 2.0.0
 */
@Repository("web2.cms.CmsBtProductLogDao")
public class CmsBtProductLogDao extends BaseDao {

    public int insertCmsBtProductLogList(List<CmsBtProductLogModel> cmsBtProductLogModelList) {
        Map<String, List<CmsBtProductLogModel>> insertDataMap = new HashMap<>();
        insertDataMap.put("list", cmsBtProductLogModelList);
        return updateTemplate.insert("insert_cms_bt_product_log_list", insertDataMap);
    }
}
