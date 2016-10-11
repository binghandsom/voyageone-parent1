package com.voyageone.service.impl.cms;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.service.dao.cms.CmsMtPlatformBrandsDao;
import com.voyageone.service.daoext.cms.CmsMtPlatformBrandsDaoExt;
import com.voyageone.service.model.cms.CmsMtPlatformBrandsModel;

/**
 * 平台品牌服务
 *
 * @author Wangtd 2016/08/01
 * @since 2.3.0
 */
@Service
public class CmsMtPlatformBrandService {

    @Autowired
    private CmsMtPlatformBrandsDao cmsMtPlatformBrandsDao;

    @Autowired
    private CmsMtPlatformBrandsDaoExt cmsMtPlatformBrandsDaoExt;

    public void deleteBrandsByChannelIdAndCartId(String channelId, String cartId) {
        cmsMtPlatformBrandsDaoExt.deleteBrandsByChannelIdAndCartId(channelId, cartId);
    }

    public void saveList(List<CmsMtPlatformBrandsModel> brandModels) {
        for (CmsMtPlatformBrandsModel brandModel : brandModels) {
            if (brandModel.getId() == null || brandModel.getId() <= 0) {
                cmsMtPlatformBrandsDao.insert(brandModel);
            } else {
                cmsMtPlatformBrandsDao.update(brandModel);
            }
        }
    }

    /**
     * 获取所有有效的品牌
     *
     * @param channelId 渠道
     * @param cartId    平台
     * @return 品牌模型列表
     * @since 2.6.0
     */
    public List<CmsMtPlatformBrandsModel> getAll(String channelId, Integer cartId) {
        return cmsMtPlatformBrandsDao.selectList(new HashMap<String, Object>(3, 1f) {{
            put("active", 1);
            put("channelId", channelId);
            put("cartId", cartId);
        }});
    }
}
