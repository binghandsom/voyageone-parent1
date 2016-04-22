package com.voyageone.service.dao.cms;
import com.voyageone.service.model.cms.*;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.List;
@Repository
public interface CmsMtImageCreateFileDao {
     public List<CmsMtImageCreateFileModel> selectList(Map<String, Object> map);
    public CmsMtImageCreateFileModel selectOne(Map<String, Object> map);
    public CmsMtImageCreateFileModel select(long id);
    public int insert(CmsMtImageCreateFileModel entity);
    public int update(CmsMtImageCreateFileModel entity);
    public int delete(long id);
    }
