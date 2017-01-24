package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.mongo.CmsMtGiltSizeChartDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsMtGiltSizeChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by james on 2017/1/24.
 */
@Service
public class CmsMtGiltSizeChartService extends BaseService {

    private final CmsMtGiltSizeChartDao cmsMtGiltSizeChartDao;

    @Autowired
    public CmsMtGiltSizeChartService(CmsMtGiltSizeChartDao cmsMtGiltSizeChartDao) {
        this.cmsMtGiltSizeChartDao = cmsMtGiltSizeChartDao;
    }

    public CmsMtGiltSizeChartModel getGiltSizeChartById(Long id){
        return cmsMtGiltSizeChartDao.getGitlSizeChartById(id);
    }

    public void insert(CmsMtGiltSizeChartModel cmsMtGiltSizeChartModel){
        cmsMtGiltSizeChartDao.insert(cmsMtGiltSizeChartModel);
    }
}
