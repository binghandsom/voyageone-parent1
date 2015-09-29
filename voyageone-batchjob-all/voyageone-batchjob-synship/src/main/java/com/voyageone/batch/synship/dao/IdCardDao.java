package com.voyageone.batch.synship.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.synship.modelbean.IdCardBean;
import com.voyageone.batch.synship.modelbean.IdCardHistory;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 从 Synship CloudClient 迁移
 * 对表 tt_idcard 的数据操作
 * <p>
 * Created by Jonas on 9/22/15.
 */
@Repository
public class IdCardDao extends BaseDao {
    /**
     * 获取 mapper 的 namespace，只在初始化时调用
     */
    @Override
    protected String namespace() {
        return Constants.getDaoNameSpace(SubSystem.SYNSHIP);
    }

    /**
     * 查询审核记录
     */
    public List<IdCardBean> selectNewestByApproved(String approved, int limit) {
        return selectList("tt_idcard_selectNewestByApproved", parameters("approved", approved, "limit", limit));
    }

    /**
     * 根据手机号码、姓名获取审核状态
     */
    public int selectCountByPhoneName(String phone, String receive_name, String approved) {
        return selectOne("tt_idcard_selectCountByPhoneName", parameters("phone", phone, "receive_name", receive_name, "approved", approved));
    }

    /**
     * 更新 tt_idcard 的 approved 和 comments（非 comment）字段
     */
    public int updateApprovedWithMsg(IdCardBean idCardBean) {
        return update("tt_idcard_updateApprovedWithMsg", idCardBean);
    }
}
