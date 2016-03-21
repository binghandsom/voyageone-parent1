package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface JmBtImagesJuMeiDao {
    public List<JmBtImagesModel>  getList();
    public  JmBtImagesModel get(long id);
    public int create(JmBtImagesModel entity);
    public  int update(JmBtImagesModel entity);

    public  int delete(long id);
    }
