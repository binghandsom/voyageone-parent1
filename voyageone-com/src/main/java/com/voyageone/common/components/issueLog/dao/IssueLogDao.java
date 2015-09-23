package com.voyageone.common.components.issueLog.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.beans.IssueLogBean;
import com.voyageone.common.util.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Leo on 5/13/2015.
 * @author Leo
 */
@Repository
public class IssueLogDao extends BaseDao {

    /**
     * 插入一个错误记录
     */
    public int insert(IssueLogBean issueLogBean) {
        return updateTemplate.insert(Constants.DAO_NAME_SPACE_COMMON + "com_issue_log_insert", issueLogBean);
    }

    public List<IssueLogBean> selectBySendFlg(int subsystem, int send_flg, int row_count) {
        Map<String, Object> params = new HashMap<>();

        params.put("sub_system", subsystem);
        params.put("send_flg", send_flg);
        params.put("offset", 0);
        params.put("limit", row_count);

        return selectList(Constants.DAO_NAME_SPACE_COMMON + "com_issue_log_selectBySendflg", params);
    }

    public int updateSendFlgByIds(Set<String> idSet, int send_flg)
    {
        // ids 作为 update table XX where id in 后的内容，格式为(N1, N2, ...)
        String ids = StringUtils.join(idSet.iterator(), ",");
        ids = "(" + ids + ")";

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("ids", ids);
        dataMap.put("send_flg", send_flg);

        return updateTemplate.update(Constants.DAO_NAME_SPACE_COMMON + "com_issue_log_updateSendflgByIds", dataMap);

    }
}
