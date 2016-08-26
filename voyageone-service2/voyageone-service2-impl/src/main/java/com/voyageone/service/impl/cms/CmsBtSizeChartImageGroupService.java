package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsBtSizeChartImageGroupDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtSizeChartImageGroupModel;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/8/25.
 */
@Service
public class CmsBtSizeChartImageGroupService extends BaseService {
    @Autowired
    CmsBtSizeChartImageGroupDao dao;
    public List<CmsBtSizeChartImageGroupModel> getList(String channelId) {
        Map<String, Object> map = new HashedMap();
        map.put("channelId", channelId);
        return dao.selectList(map);
    }
}
