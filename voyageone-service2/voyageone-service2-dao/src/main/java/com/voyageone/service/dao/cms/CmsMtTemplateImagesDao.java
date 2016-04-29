package com.voyageone.service.dao.cms;
import com.voyageone.service.model.cms.CmsMtTemplateImagesModel;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;

@Repository
public interface CmsMtTemplateImagesDao {
     public List<CmsMtTemplateImagesModel> selectList(Map<String, Object> map);
    public CmsMtTemplateImagesModel selectOne(Map<String, Object> map);
    public CmsMtTemplateImagesModel select(long id);
    public int insert(CmsMtTemplateImagesModel entity);
    public int update(CmsMtTemplateImagesModel entity);
    public int delete(long id);
    }
