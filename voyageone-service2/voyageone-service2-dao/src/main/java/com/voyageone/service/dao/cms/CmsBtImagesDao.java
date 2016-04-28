package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtImagesModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by jeff.duan on 2016/4/20.
 */
@Repository
public interface CmsBtImagesDao {

    List<CmsBtImagesModel> selectList(Map<String, Object> map);

    CmsBtImagesModel selectOne(Map<String, Object> map);

    CmsBtImagesModel select(long id);

    int insert(CmsBtImagesModel entity);

    int update(CmsBtImagesModel entity);

    int delete(long id);
}
