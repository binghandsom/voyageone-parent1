package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtStoreOperationHistoryModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 15-12-7.
 */
@Repository
public interface CmsBtSxWorkloadDao {
    List<CmsBtStoreOperationHistoryModel> selectList(Map<String, Object> map);

    CmsBtStoreOperationHistoryModel selectOne(Map<String, Object> map);

    CmsBtStoreOperationHistoryModel select(long id);

    int insert(CmsBtStoreOperationHistoryModel entity);

    int update(CmsBtStoreOperationHistoryModel entity);

    int delete(long id);
}
