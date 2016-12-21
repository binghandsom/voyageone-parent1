package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsMtPlatformSkusDao;
import com.voyageone.service.daoext.cms.CmsMtPlatformSkusDaoExt;
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
    @Autowired
    private CmsMtPlatformSkusDaoExt cmsMtPlatformSkusDaoExt;

    /**
     * 获取voyageone_cms2.cms_mt_platform_skus表中指定渠道指定类目对应的所有颜色和尺寸信息列表
     */
    public List<CmsMtPlatformSkusModel> getModesByAttrType(String channelId, int cartId, String platformCatId, int active) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("channelId", channelId);
        paramsMap.put("cartId", cartId);
        paramsMap.put("platformCategoryId", platformCatId);
        paramsMap.put("active", active);

        return cmsMtPlatformSkusDao.selectList(paramsMap);
    }

    /**
     * 获取voyageone_cms2.cms_mt_platform_skus表中指定渠道下所有类目对应的颜色和尺寸件数列表(件数存放在idx字段返回)
     */
    public List<CmsMtPlatformSkusModel> getCategorySaleAttrCount(String channelId, int cartId) {
        Map<String, Object> param = new HashMap() {{
            put("channelId", channelId);
            put("cartId", cartId);
        }};

        return cmsMtPlatformSkusDaoExt.getPlatformSkusSaleAttrCount(param);
    }

    /**
     * 删除voyageone_cms2.cms_mt_platform_skus表中指定渠道下指定类目对应的颜色和尺寸记录
     */
    public void deleteCategorySaleAttr(String channelId, int cartId, String catId) {
        Map<String, Object> param = new HashMap() {{
            put("channelId", channelId);
            put("cartId", cartId);
            put("catId", catId);
        }};

        cmsMtPlatformSkusDaoExt.deletePlatformSkusSaleAttr(param);
    }

    /**
     * 插入多条voyageone_cms2.cms_mt_platform_skus表记录
     */
    public void insertCategorySaleAttrList(List<CmsMtPlatformSkusModel> models) {
        models.forEach(model -> cmsMtPlatformSkusDao.insert(model));
    }
}
