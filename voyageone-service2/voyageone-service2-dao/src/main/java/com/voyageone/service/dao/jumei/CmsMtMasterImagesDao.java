package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface CmsMtMasterImagesDao {
    public List<CmsMtMasterImagesModel>  getList();
    public  CmsMtMasterImagesModel get(long id);
    public int create(CmsMtMasterImagesModel entity);
    public  int update(CmsMtMasterImagesModel entity);

    public  int delete(long id);
    }
