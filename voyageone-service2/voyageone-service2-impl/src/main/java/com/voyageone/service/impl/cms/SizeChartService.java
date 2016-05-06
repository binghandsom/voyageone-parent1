package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.mongo.CmsBtSizeChartDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by gjl on 2016/5/6.
 */
@Service
public class SizeChartService extends BaseService {
    @Autowired
    private CmsBtSizeChartDao cmsBtSizeChartDao;
    public void insert(CmsBtSizeChartModel model) {
        cmsBtSizeChartDao.insert(model);
    }

}
