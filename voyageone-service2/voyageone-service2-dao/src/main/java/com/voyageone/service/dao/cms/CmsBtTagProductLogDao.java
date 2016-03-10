package com.voyageone.service.dao.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtTagProductLogModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/14
 * @version 2.0.0
 */
//Repository
public class CmsBtTagProductLogDao extends ServiceBaseDao {

    public int insertCmsBtTagLogList(List<CmsBtTagProductLogModel> cmsBtTagLogModelList){
        Map<String, List<CmsBtTagProductLogModel>> insertDataMap = new HashMap<>();
        insertDataMap.put("list", cmsBtTagLogModelList);
        return insert("insert_cms_bt_tag_product_log_list",insertDataMap);
    }
}
