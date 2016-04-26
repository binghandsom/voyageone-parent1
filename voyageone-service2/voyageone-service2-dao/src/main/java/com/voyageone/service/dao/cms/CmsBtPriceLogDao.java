package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtPriceLogDao {
    List<CmsBtPriceLogModel> selectList(Map<String, Object> map);

    CmsBtPriceLogModel selectOne(Map<String, Object> map);

    CmsBtPriceLogModel select(long id);

    int insert(CmsBtPriceLogModel entity);

    int update(CmsBtPriceLogModel entity);

    int delete(long id);
}
