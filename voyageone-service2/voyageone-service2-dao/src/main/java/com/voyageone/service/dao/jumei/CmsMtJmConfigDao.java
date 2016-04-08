package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;
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
