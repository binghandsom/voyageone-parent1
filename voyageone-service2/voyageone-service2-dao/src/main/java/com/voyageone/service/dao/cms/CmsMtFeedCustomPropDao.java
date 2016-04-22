package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtFeedCustomPropModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtFeedCustomPropDao {
    List<CmsMtFeedCustomPropModel> selectList(Map<String, Object> map);

    CmsMtFeedCustomPropModel selectOne(Map<String, Object> map);

    CmsMtFeedCustomPropModel select(long id);

    int insert(CmsMtFeedCustomPropModel entity);

    int update(CmsMtFeedCustomPropModel entity);

    int delete(long id);
}
