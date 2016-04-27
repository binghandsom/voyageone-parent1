package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtFeedCustomOptionModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtFeedCustomOptionDao {
    List<CmsMtFeedCustomOptionModel> selectList(Map<String, Object> map);

    CmsMtFeedCustomOptionModel selectOne(Map<String, Object> map);

    CmsMtFeedCustomOptionModel select(long id);

    int insert(CmsMtFeedCustomOptionModel entity);

    int update(CmsMtFeedCustomOptionModel entity);

    int delete(long id);
}
