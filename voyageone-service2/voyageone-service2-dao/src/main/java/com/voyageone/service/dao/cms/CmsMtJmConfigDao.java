package com.voyageone.service.dao.cms;
import com.voyageone.service.model.cms.CmsMtJmConfigModel;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;

@Repository
public interface CmsMtJmConfigDao {
     public List<CmsMtJmConfigModel> selectList(Map<String, Object> map);
    public CmsMtJmConfigModel selectOne(Map<String, Object> map);
    public CmsMtJmConfigModel select(long id);
    public int insert(CmsMtJmConfigModel entity);
    public int update(CmsMtJmConfigModel entity);
    public int delete(long id);
    }
