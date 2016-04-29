package com.voyageone.service.dao.cms;
import com.voyageone.service.model.cms.CmsMtJmCategoryModel;
import org.springframework.stereotype.Repository;

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
