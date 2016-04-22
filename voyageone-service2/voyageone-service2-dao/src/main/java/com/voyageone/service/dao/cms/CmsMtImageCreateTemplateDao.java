package com.voyageone.service.dao.cms;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.cms.*;
import java.util.Map;
import java.util.List;
@Repository
public interface CmsMtImageCreateTemplateDao {
     public List<CmsMtImageCreateTemplateModel> selectList(Map<String, Object> map);
    public CmsMtImageCreateTemplateModel selectOne(Map<String, Object> map);
    public CmsMtImageCreateTemplateModel select(long id);
    public int insert(CmsMtImageCreateTemplateModel entity);
    public int update(CmsMtImageCreateTemplateModel entity);
    public int delete(long id);
    }
