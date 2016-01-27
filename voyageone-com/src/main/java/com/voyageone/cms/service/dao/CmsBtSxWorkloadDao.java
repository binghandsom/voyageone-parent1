package com.voyageone.cms.service.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.cms.service.model.CmsBtSxWorkloadModel;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Leo on 15-12-7.
 */
@Repository
public class CmsBtSxWorkloadDao extends BaseDao {
    public List<CmsBtSxWorkloadModel> getSxWorkloadModelWithChannel(int recordCount, String channelId) {
        return selectList("cms_select_sx_workload", parameters("record_count", recordCount, "channel_id", channelId));
    }

    public List<CmsBtSxWorkloadModel> getSxWorkloadModel(int recordCount) {
        return selectList("cms_select_sx_workload", parameters("record_count", recordCount));
    }

    public void updateSxWorkloadModel(CmsBtSxWorkloadModel model) {
        update("cms_update_sx_workload", parameters("seq", model.getSeq(), "publish_status", model.getPublishStatus()));
    }

    public void insertSxWorkloadModel(CmsBtSxWorkloadModel model) {
        insert("cms_insert_sx_workload", model);
    }
}
