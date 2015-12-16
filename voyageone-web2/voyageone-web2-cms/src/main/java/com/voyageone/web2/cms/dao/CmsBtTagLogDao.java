package com.voyageone.web2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.web2.cms.model.CmsBtTagLogModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/14
 * @version 2.0.0
 */
@Repository
public class CmsBtTagLogDao extends BaseDao {

    public int insertCmsBtTagLogList(List<CmsBtTagLogModel> cmsBtTagLogModelList){
        Map<String, List<CmsBtTagLogModel>> insertDataMap = new HashMap<>();
        insertDataMap.put("list", cmsBtTagLogModelList);
        return updateTemplate.insert("insert_cms_bt_tag_log_list",insertDataMap);
    }
}
