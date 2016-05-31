package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsBtPlatformImagesDao;
import com.voyageone.service.dao.cms.CmsBtSxWorkloadDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtPlatformImagesModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 16/5/10
 */
@Service
public class PlatformImagesService extends BaseService {

    @Autowired
    CmsBtPlatformImagesDao cmsBtPlatformImagesDao;

    @Autowired
    CmsBtSxWorkloadDao cmsBtSxWorkloadDao;

    /**
     * 根据cartId,imageName,templateId查找对应数据是否存在,如果数据存在则返回该数据,不存在则返回初始化的数据.
     *
     * @param channelId
     * @param cartId
     * @param imageName
     * @param templateId
     * @return
     */
    public CmsBtPlatformImagesModel selectByImageNameWithTemplate(String channelId, Integer cartId, String imageName, Long templateId) {
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", channelId);
        map.put("cartId", cartId);
        map.put("imgName", imageName);
        map.put("templateId", templateId);

        CmsBtPlatformImagesModel platformImage = cmsBtPlatformImagesDao.selectOne(map);
        platformImage = platformImage != null ? platformImage : new CmsBtPlatformImagesModel();
        platformImage.setChannelId(channelId);
        platformImage.setCartId(cartId);
        platformImage.setImgName(imageName);
        platformImage.setTemplateId(Integer.valueOf(templateId.toString()));

        return platformImage;
    }

    /**
     * @param record
     * @return
     */
    public int save(CmsBtPlatformImagesModel record) {
        if (record.getId() != null) {
            return cmsBtPlatformImagesDao.update(record);
        } else {
            return cmsBtPlatformImagesDao.insert(record);
        }
    }
}

