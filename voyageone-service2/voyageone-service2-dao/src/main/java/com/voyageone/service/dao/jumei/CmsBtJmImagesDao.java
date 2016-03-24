package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface CmsBtJmImagesDao {
    public List<CmsBtJmImagesModel>  selectList();
    public  CmsBtJmImagesModel select(long id);
    public int insert(CmsBtJmImagesModel entity);
    public  int update(CmsBtJmImagesModel entity);

    public  int delete(long id);
    }
