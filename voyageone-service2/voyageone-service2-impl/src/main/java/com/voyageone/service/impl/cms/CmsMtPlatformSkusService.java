package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsMtPlatformSkusDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtPlatformSkusModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 平台SKU颜色尺寸数据的增删改查管理
 * <p>
 * Created by desmond on 2016/04/22.
 */
@Service
public class CmsMtPlatformSkusService extends BaseService {

    @Autowired
    private CmsMtPlatformSkusDao cmsMtPlatformSkusDao;

    /**
     * 获取渠道指定类目对应的所有颜色和尺寸信息列表
     */
    public List<CmsMtPlatformSkusModel> getModesByAttrType(String channelId, int cartId, String platformCatId, int active) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("channelId", channelId);
        paramsMap.put("cart_id", cartId);
        paramsMap.put("platformCategoryId", platformCatId);
        paramsMap.put("active", active);

        return cmsMtPlatformSkusDao.selectList(paramsMap);
    }

}
