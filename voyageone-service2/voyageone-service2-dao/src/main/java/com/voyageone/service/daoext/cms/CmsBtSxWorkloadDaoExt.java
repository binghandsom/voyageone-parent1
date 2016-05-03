package com.voyageone.service.daoext.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Leo on 15-12-7.
 */
@Repository
public class CmsBtSxWorkloadDaoExt extends ServiceBaseDao {

    public List<CmsBtSxWorkloadModel> selectSxWorkloadModelWithChannel(int recordCount, String channelId) {
        return selectList("cms_select_sx_workload", parameters("record_count", recordCount, "channel_id", channelId));
    }

    public List<CmsBtSxWorkloadModel> selectSxWorkloadModel(int recordCount) {
        return selectList("cms_select_sx_workload", parameters("record_count", recordCount));
    }

    public List<CmsBtSxWorkloadModel> selectSxWorkloadModelWithCartId(int recordCount, int cartId) {
        return selectList("cms_select_sx_workload", parameters("record_count", recordCount, "cartId", cartId));
    }

    public List<CmsBtSxWorkloadModel> selectSxWorkloadModelWithChannelIdCartId(int recordCount, String channelId, int cartId) {
        return selectList("cms_select_sx_workload", parameters("record_count", recordCount, "channel_id", channelId, "cartId", cartId));
    }

    public void updateSxWorkloadModel(CmsBtSxWorkloadModel model) {
        update("cms_update_sx_workload", parameters("id", model.getId(), "publish_status", model.getPublishStatus()));
    }

    public int updateSxWorkloadModelWithModifier(CmsBtSxWorkloadModel model) {
        return update("cms_update_sx_workload_with_modifier", model);
    }

    public void insertSxWorkloadModel(CmsBtSxWorkloadModel model) {
        insert("cms_insert_sx_workload", model);
    }

    public void insertSxWorkloadModels(List<CmsBtSxWorkloadModel> models) {
        insert("cms_insert_sx_workloads", models);
    }

    /**
     * 如果数据库中存在对应的channelId,groupId,cartId,publishStatus为0的记录那么返回true
     *
     * @return
     */
    public boolean hasUnpublishRecord(CmsBtSxWorkloadModel model) {
        return countSxWorkloadModelBy(model.getChannelId(), new Long(model.getGroupId()), 0, model.getCartId()) > 0;
    }

    public int countSxWorkloadModelBy(String channelId, Long groupId, Integer publishStatus, Integer cartId) {
        List<CmsBtSxWorkloadModel> result = selectList("cms_select_sx_workload", parameters("channel_id", channelId,
                "groupId", groupId,
                "publishStatus", publishStatus,
                "cartId", cartId
        ));
        return result.size();
    }
}
