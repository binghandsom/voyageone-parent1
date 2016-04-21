package com.voyageone.service.dao.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtImagesModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jeff.duan on 2016/4/20.
 */
@Repository
public class CmsBtImagesDao extends ServiceBaseDao {

    public List<CmsBtImagesModel> selectImages(CmsBtImagesModel image) {
        return selectList("select_cms_bt_images", image);
    }

    public void insertImages(CmsBtImagesModel image) {
        insert("insert_cms_bt_images", image);
    }

    public void insertImages(List<CmsBtImagesModel> imageModels) {
        imageModels.forEach(model -> {
            insertImages(model);
        });
    }
}
