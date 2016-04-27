package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtPlatformImagesModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtPlatformImagesDao {
    List<CmsBtPlatformImagesModel> selectList(Map<String, Object> map);

    CmsBtPlatformImagesModel selectOne(Map<String, Object> map);

    CmsBtPlatformImagesModel select(long id);

    int insert(CmsBtPlatformImagesModel entity);

    int update(CmsBtPlatformImagesModel entity);

    int delete(long id);
}
