package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;
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
