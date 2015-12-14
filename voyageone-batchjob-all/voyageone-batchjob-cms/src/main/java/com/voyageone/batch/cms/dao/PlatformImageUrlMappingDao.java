package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.model.ImageUrlMappingModel;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 15-7-17.
 */
@Repository
public class PlatformImageUrlMappingDao extends BaseDao {

    /**
     *
     * @param cartId
     * @param channelId
     * @return
     */
    public List<ImageUrlMappingModel> getImageUrlMap(int cartId, String channelId,String modelId)
    {
        Map<String, Object> parmMap = new HashMap<>();
        parmMap.put("cartId", cartId);
        parmMap.put("channelId", channelId);
        parmMap.put("groupId", modelId);

        List<ImageUrlMappingModel> resultMap = selectList(Constants.DAO_NAME_SPACE_CMS + "getPlatformImageUrlMap", parmMap);

        return resultMap;
    }

    /**
     *
     * @param imageUrlInfos
     */
    public void insertPlatformSkuInfo(List<ImageUrlMappingModel> imageUrlInfos)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("imgModels", imageUrlInfos);
        updateTemplate.insert(Constants.DAO_NAME_SPACE_CMS + "cms_insertPlatformImageUrl", params);
    }
}
