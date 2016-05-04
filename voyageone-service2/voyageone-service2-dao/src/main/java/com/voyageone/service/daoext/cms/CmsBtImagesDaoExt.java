package com.voyageone.service.daoext.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtImagesModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Edward
 * @version 2.0.0, 16/4/29
 */
@Repository
public class CmsBtImagesDaoExt extends ServiceBaseDao {

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

    public int updateImage(CmsBtImagesModel imagesModel) {
        return update("update_cms_bt_images", imagesModel);
    }
}