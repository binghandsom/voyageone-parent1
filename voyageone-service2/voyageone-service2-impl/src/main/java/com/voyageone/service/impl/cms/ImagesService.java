package com.voyageone.service.impl.cms;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.dao.cms.CmsBtImagesDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtImagesModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Edward
 * @version 2.0.0, 16/4/28
 */
public class ImagesService extends BaseService {

    @Autowired
    private CmsBtImagesDao cmsBtImagesDao;

    public List<CmsBtImagesModel> getImageList (CmsBtImagesModel model) {
        return cmsBtImagesDao.selectList(JacksonUtil.jsonToMap(JacksonUtil.bean2Json(model)));
    }

    public int insert(CmsBtImagesModel model) {
        return cmsBtImagesDao.insert(model);
    }

    public int update(CmsBtImagesModel model) {
        return cmsBtImagesDao.update(model);
    }
}
