package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtStoreOperationHistoryModel;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.Map;
import java.util.List;

@Repository
public interface CmsBtStoreOperationHistoryDao {
    public List<CmsBtStoreOperationHistoryModel> selectList(Map<String, Object> map);

    public CmsBtStoreOperationHistoryModel selectOne(Map<String, Object> map);

    public CmsBtStoreOperationHistoryModel select(long id);

    public int insert(CmsBtStoreOperationHistoryModel entity);

    public int update(CmsBtStoreOperationHistoryModel entity);

    public int delete(long id);
}
