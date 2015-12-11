package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.model.SxWorkloadModel;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Leo on 15-12-7.
 */
@Repository
public class SxWorkloadDao extends BaseDao {
    public List<SxWorkloadModel> getSxWorkloadModelWithChannel(int recordCount, String channelId) {
        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_select_sx_workload", parameters("record_count", recordCount, "channel_id", channelId));
    }

    public List<SxWorkloadModel> getSxWorkloadModel(int recordCount) {
        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_select_sx_workload", parameters("record_count", recordCount));
    }
}
