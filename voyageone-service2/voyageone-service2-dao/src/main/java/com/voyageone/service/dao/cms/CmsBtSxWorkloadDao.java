package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtSxWorkloadDao {
    List<CmsBtSxWorkloadModel> selectList(Map<String, Object> map);

    CmsBtSxWorkloadModel selectOne(Map<String, Object> map);

    CmsBtSxWorkloadModel select(long id);

    int insert(CmsBtSxWorkloadModel entity);

    int update(CmsBtSxWorkloadModel entity);

    public void updateSxWorkloadModel(CmsBtSxWorkloadModel model) {
        update("cms_update_sx_workload", parameters("seq", model.getSeq(), "publish_status", model.getPublishStatus()));
    }

    public int updateSxWorkloadModelWithModifier(CmsBtSxWorkloadModel model) {
       return update("cms_update_sx_workload_with_modifier", model);
    }

    public void insertSxWorkloadModel(CmsBtSxWorkloadModel model) {
        insert("cms_insert_sx_workload", model);
    }

    public void insertSxWorkloadModels(List<CmsBtSxWorkloadModel> models) {
        if (models.size() == 0) {
            return;
        }
        insert("cms_insert_sx_workloads", models);
    }

    /**
     * 如果数据库中存在对应的channelId,groupId,cartId,publishStatus为0的记录那么返回true
     * @return
     */
    public boolean hasUnpublishRecord(CmsBtSxWorkloadModel model) {
        return countSxWorkloadModelBy(model.getChannelId(),model.getGroupId(),0,model.getCartId())>0;
    }

    public int countSxWorkloadModelBy(String channelId,Long groupId,Integer publishStatus,Integer cartId){
        List<CmsBtSxWorkloadModel> result = selectList("cms_select_sx_workload", parameters("channel_id", channelId,
                "groupId", groupId,
                "publishStatus", publishStatus,
                "cartId", cartId
        ));
        return result.size();
    }
    int delete(long id);
}
