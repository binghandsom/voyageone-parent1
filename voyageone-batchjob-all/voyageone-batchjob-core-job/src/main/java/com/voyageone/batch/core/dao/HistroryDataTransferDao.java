package com.voyageone.batch.core.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by eric on 2015/10/12.
 */
@Repository
public class HistroryDataTransferDao extends BaseDao {
    /**
     * 插入历史数据
     * @param data
     * @return
     */
    public int insertHistoryData(Map<String, Object> data) {
        return updateTemplate.insert(Constants.DAO_NAME_SPACE_CORE + "insertHistoryData", data);
    }

    /**
     * 删除历史数据
     * @param data
     * @return
     */
    public int deleteHistoryData(Map<String, Object> data) {

        return updateTemplate.insert(Constants.DAO_NAME_SPACE_CORE + "deleteHistoryData", data);
    }
}
