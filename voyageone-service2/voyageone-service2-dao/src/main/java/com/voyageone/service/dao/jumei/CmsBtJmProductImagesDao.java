package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface CmsBtJmProductImagesDao {
    public List<CmsBtJmProductImagesModel>  selectList();
    public  CmsBtJmProductImagesModel select(long id);
    public int insert(CmsBtJmProductImagesModel entity);
    public  int update(CmsBtJmProductImagesModel entity);

    public  int delete(long id);
    }
