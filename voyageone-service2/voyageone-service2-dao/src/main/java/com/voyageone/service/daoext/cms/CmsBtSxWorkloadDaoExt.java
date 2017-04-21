package com.voyageone.service.daoext.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Leo on 15-12-7.
 *
 * @version 2.0.1
 * @author Ethan Shi
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

    public List<CmsBtSxWorkloadModel> selectSxWorkloadModelWithChannelIdCartIdGroupBy(int recordCount, String channelId, int cartId) {
        return selectList("cms_select_sx_workload_groupby", parameters("record_count", recordCount, "channel_id", channelId, "cartId", cartId));
    }

    public List<CmsBtSxWorkloadModel> selectSxWorkloadModelWithModifiedAscGroupBy(int recordCount, List<String> channelIdList, int cartId) {
        return selectList("cms_select_sx_workload_with_modified_groupby", parameters("record_count", recordCount, "channelIdList", channelIdList, "cartId", cartId));
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

    public int insertSxWorkloadModels(List<CmsBtSxWorkloadModel> models) {
        return insert("cms_insert_sx_workloads", models);
    }

    public int insertPlatformWorkloadModels(List<CmsBtSxWorkloadModel> models) {
        return insert("cms_insert_platform_workloads", models);
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

    public int updatePublishStatus(CmsBtSxWorkloadModel model) {
        return update("update_publish_status", model);
    }

    public int updatePlatformWorkloadPublishStatus(CmsBtSxWorkloadModel model) {
        return update("update_publish_status_platformWorkload", model);
    }

    public int delayWorkload(CmsBtSxWorkloadModel model) {
        return update("delay_work_load", model);
    }

    public List<CmsBtSxWorkloadModel> selectSxWorkloadModelWithChannelIdListCartIdList(int recordCount, List<String> channelIdList, List<String> cartList) {
        return selectList("cms_select_platform_workload", parameters("record_count", recordCount, "channelIdList", channelIdList, "cartList", cartList));
    }

    public List<CmsBtSxWorkloadModel> selectSxWorkloadModelByChannelIdCartIdWorkloadName(int recordCount, List<String> channelIdList, List<String> cartList, String workloadName) {
        return selectList("cms_select_platform_workload_by_name", parameters("record_count", recordCount, "channelIdList", channelIdList, "cartList", cartList, "workloadName", workloadName));
    }

}
