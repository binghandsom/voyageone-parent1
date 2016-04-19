package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;
import java.util.Map;
import java.util.List;

@Repository
public interface CmsMtJmCategoryDao {
     public List<CmsMtJmCategoryModel> selectList(Map<String, Object> map);
    public CmsMtJmCategoryModel selectOne(Map<String, Object> map);
    public CmsMtJmCategoryModel select(long id);
    public int insert(CmsMtJmCategoryModel entity);
    public int update(CmsMtJmCategoryModel entity);
    public int delete(long id);
    }
