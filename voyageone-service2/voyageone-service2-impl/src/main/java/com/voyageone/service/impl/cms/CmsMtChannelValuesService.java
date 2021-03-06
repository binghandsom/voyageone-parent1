package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsMtChannelValuesDao;
import com.voyageone.service.daoext.cms.CmsMtChannelValuesDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtChannelValuesModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/4/19.
 * @version 2.0.0
 */
@Service
public class CmsMtChannelValuesService extends BaseService {

    @Autowired
    private CmsMtChannelValuesDao cmsMtChannelValuesDao;

    @Autowired
    private CmsMtChannelValuesDaoExt cmsMtChannelValuesDaoExt;

    public static final int BRAND = 0;
    public static final int SIZE_TYPE = 1;
    public static final int PRODUCT_TYPE = 2;

    /**
     * 插入ChannelValues
     *
     * @param cmsMtChannelValuesModel cmsMtChannelValuesModel
     */
    public int insertCmsMtChannelValues(CmsMtChannelValuesModel cmsMtChannelValuesModel) {
        return cmsMtChannelValuesDaoExt.insertIgnore(cmsMtChannelValuesModel);
    }

    /**
     * 根据channelId 获取channelValuesList
     *
     * @param channelId 根据channelId
     */
    public List<CmsMtChannelValuesModel> getCmsMtChannelValuesListByChannelId(String channelId) {
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", channelId);
        return cmsMtChannelValuesDao.selectList(param);
    }

    /**
     * 根据channelId和类型获取 获取channelValuesList
     *
     * @param channelId 根据channelId
     * @param type      （0:brand 1:sizeType 2:productType）
     */
    public List<CmsMtChannelValuesModel> getCmsMtChannelValuesListByChannelIdType(String channelId, int type) {
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", channelId);
        param.put("type", type);
        return cmsMtChannelValuesDao.selectList(param);
    }


}
