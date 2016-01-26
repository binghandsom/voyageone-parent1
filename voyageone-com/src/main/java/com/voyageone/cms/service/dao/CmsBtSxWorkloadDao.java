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
        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_select_sx_workload", parameters("record_count", recordCount, "channel_id", channelId));
    }

    public List<CmsBtSxWorkloadModel> getSxWorkloadModel(int recordCount) {
        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_select_sx_workload", parameters("record_count", recordCount));
    }

    public void updateSxWorkloadModel(CmsBtSxWorkloadModel sxWorkloadModel) {
        update(Constants.DAO_NAME_SPACE_CMS + "cms_update_sx_workload", parameters("seq", sxWorkloadModel.getSeq(), "publish_status", sxWorkloadModel.getPublishStatus()));
    }
}
