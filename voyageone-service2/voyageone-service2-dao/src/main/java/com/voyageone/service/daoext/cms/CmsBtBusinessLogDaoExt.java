package com.voyageone.service.daoext.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtBusinessLogModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/1/20.
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class CmsBtBusinessLogDaoExt extends ServiceBaseDao {

    /**
     * 条件查询
     *
     * @param conditionParams params
     * @return list
     */
    public List<CmsBtBusinessLogModel> selectByCondition(Map<?, ?> conditionParams) {
        return selectList("select_business_log_By_Condition", conditionParams);
    }

    /**
     * 根据查询条件获得总件数
     *
     * @param conditionParams condition Params
     * @return int
     */
    public int selectByConditionCnt(Map<?, ?> conditionParams) {

        return selectOne("select_business_log_By_Condition_cnt", conditionParams);
    }

    /**
     * 修改处理状态
     *
     * @param conditionParams params
     * @return effect count
     */
    public int updateStatusFinish(Map<?, ?> conditionParams) {
        return update("update_business_log_status_finish", conditionParams);
    }

    /**
     * 根据cheannelId(必须)，cartId,groupId,model或者code来修改log状态到已解决(1)
     *
     * @param conditionParams params
     * @return effect count
     */
    public int updateStatusFinishByCondition(Map<?, ?> conditionParams) {
        return update("update_business_log_status_finish_by_condition", conditionParams);
    }

}
