package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtDataAmountModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtDataAmountDao {
    List<CmsBtDataAmountModel> selectList(Map<String, Object> map);

    CmsBtDataAmountModel selectOne(Map<String, Object> map);

    CmsBtDataAmountModel select(long id);

    int insert(CmsBtDataAmountModel entity);

    int update(CmsBtDataAmountModel entity);

    int delete(long id);
}
