package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsMtBrandsMappingDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtBrandsMappingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * CmsMt Brand Service
 *
 * @author chuanyu.liang 2016/06/28
 * @version 2.0.1
 * @author linanbin 2016/07/04
 * @version 2.2.0
 * @since 2.0.0
 */
@Service
public class CmsMtBrandService extends BaseService {

    @Autowired
    private CmsMtBrandsMappingDao cmsMtBrandsMappingDao;

    /**
     * getModelByMap
     *
     * @param map Map
     * @return CmsMtBrandsMappingModel
     */
    public CmsMtBrandsMappingModel getModelByMap(Map<String, Object> map) {
        return cmsMtBrandsMappingDao.selectOne(map);
    }

    /**
     * 通过cms中的品牌名称返回对应的平台BrandId
     * @param brand cms的品牌名称
     * @param cartId 平台Id
     * @param channelId 店铺Id
     * @return
     */
    public CmsMtBrandsMappingModel getModelByName(String brand, String cartId, String channelId) {
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", channelId);
        map.put("cartId", cartId);
        map.put("cmsBrand", brand);

        // TODO: 16/7/4 目前无法取得platform的brand是信息,暂时使用
        return getModelByMap(map);
    }
}
