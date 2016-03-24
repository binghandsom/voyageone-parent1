package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface CmsMtModelImagesDao {
    public List<CmsMtModelImagesModel>  getList();
    public  CmsMtModelImagesModel get(long id);
    public int create(CmsMtModelImagesModel entity);
    public  int update(CmsMtModelImagesModel entity);

    public  int delete(long id);
    }
