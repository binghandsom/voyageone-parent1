package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtImageCreateTemplateModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtImageCreateTemplateDao {
    List<CmsMtImageCreateTemplateModel> selectList(Map<String, Object> map);

    CmsMtImageCreateTemplateModel selectOne(Map<String, Object> map);

    CmsMtImageCreateTemplateModel select(long id);

    int insert(CmsMtImageCreateTemplateModel entity);

    int update(CmsMtImageCreateTemplateModel entity);

    int delete(long id);
}
