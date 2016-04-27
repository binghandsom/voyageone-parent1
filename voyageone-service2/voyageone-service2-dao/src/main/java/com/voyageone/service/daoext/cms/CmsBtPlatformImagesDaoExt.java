package com.voyageone.service.daoext.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtPlatformImagesModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author morse 16/04/21
 */

@Repository
public class CmsBtPlatformImagesDaoExt extends ServiceBaseDao {

    public List<CmsBtPlatformImagesModel> selectPlatformImagesList(String channelId, int cartId, String searchId) {
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", channelId);
        param.put("cartId", cartId);
        param.put("searchId", searchId);
        return selectList("cms_bt_platform_images_select", param);
    }

    public int insertPlatformImagesByList(List<CmsBtPlatformImagesModel> listData) {
        return insert("cms_bt_platform_images_insertByList", listData);
    }

    public int updatePlatformImagesById(CmsBtPlatformImagesModel platformImagesModel, String modifier) {
        platformImagesModel.setModifier(modifier);
        return update("cms_bt_platform_images_updateById", platformImagesModel);
    }

}
