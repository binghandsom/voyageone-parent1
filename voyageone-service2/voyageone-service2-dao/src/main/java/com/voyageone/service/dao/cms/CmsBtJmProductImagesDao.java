package com.voyageone.service.dao.cms;
import com.voyageone.service.model.cms.CmsBtJmProductImagesModel;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;

@Repository
public interface CmsBtJmProductImagesDao {
     public List<CmsBtJmProductImagesModel> selectList(Map<String, Object> map);
    public CmsBtJmProductImagesModel selectOne(Map<String, Object> map);
    public CmsBtJmProductImagesModel select(long id);
    public int insert(CmsBtJmProductImagesModel entity);
    public int update(CmsBtJmProductImagesModel entity);
    public int delete(long id);
    }
