package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface CmsBtJmImagesDao {
    public List<CmsBtJmImagesModel>  getList();
    public  CmsBtJmImagesModel get(long id);
    public int create(CmsBtJmImagesModel entity);
    public  int update(CmsBtJmImagesModel entity);

    public  int delete(long id);
    }
