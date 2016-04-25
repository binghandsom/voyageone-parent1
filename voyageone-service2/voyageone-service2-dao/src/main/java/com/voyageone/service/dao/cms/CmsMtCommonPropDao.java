package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtCommonPropModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtCommonPropDao {
    List<CmsMtCommonPropModel> selectList(Map<String, Object> map);

    CmsMtCommonPropModel selectOne(Map<String, Object> map);

    CmsMtCommonPropModel select(long id);

    int insert(CmsMtCommonPropModel entity);

    int update(CmsMtCommonPropModel entity);

    int delete(long id);
}
